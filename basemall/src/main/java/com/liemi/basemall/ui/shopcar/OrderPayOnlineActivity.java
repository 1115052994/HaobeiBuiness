package com.liemi.basemall.ui.shopcar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.ETHApi;
import com.liemi.basemall.data.api.LoginApi;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.WXPayData;
import com.liemi.basemall.data.entity.eth.ETHRateEntity;
import com.liemi.basemall.data.entity.eth.YMSEntity;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.data.entity.order.OrderPayEntity;
import com.liemi.basemall.data.entity.order.PayResult;
import com.liemi.basemall.data.event.OrderUpdateEvent;
import com.liemi.basemall.data.event.WXPayResultEvent;
import com.liemi.basemall.databinding.ActivityOrderPayOnlineBinding;
import com.liemi.basemall.ui.login.LoginHomeActivity;
import com.liemi.basemall.ui.login.SetPayPasswordActivity;
import com.liemi.basemall.ui.personal.digitalasset.PropertyTakeOutInputPasswordDialog;
import com.liemi.basemall.ui.personal.order.PayResultActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;


import cn.iwgang.countdownview.CountdownView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.liemi.basemall.ui.personal.order.OrderDetailActivity.ORDER_ENTITY;
import static com.netmi.baselibrary.data.Constant.ORDER_MPID;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/10/17 14:24
 * 修改备注：
 */
public class OrderPayOnlineActivity extends BaseActivity<ActivityOrderPayOnlineBinding> {

    public static final String ORDER_PAY_ENTITY = "orderPayEntity";
    private OrderPayEntity payEntity;
    private String orderId;
    //用户输入资产密码弹出框
    private PropertyTakeOutInputPasswordDialog mInputPasswordDialog;
    private String ethPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO 生产环境必须注释掉
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mBinding.cbPayDigital.setChecked(true);
        mBinding.cbPayDigital.setText("使用YMS支付（剩余："+UserInfoCache.get().getYms_remain()+")");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_order_pay_online;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("确认支付");

        payEntity = (OrderPayEntity) getIntent().getSerializableExtra(ORDER_PAY_ENTITY);

        orderId = getIntent().getStringExtra(ORDER_MPID);

        if (payEntity == null && TextUtils.isEmpty(orderId)) {
            ToastUtils.showShort("没有支付订单详情");
            finish();
            return;
        }

        if (!TextUtils.isEmpty(orderId)) {
            doGetOrderPayInfo();
        } else {
            showData(payEntity);
        }

        doGetYms();

    }

    @Override
    protected void initData() {
        doGetETHRate();
    }

    private void showData(OrderPayEntity payEntity) {
        long lastTime = DateUtil.strToLong(payEntity.getEnd_time()) - Calendar.getInstance().getTimeInMillis();
        if (lastTime > 0) {
            mBinding.cvTime.start(lastTime);
            mBinding.cvTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    mBinding.cvTime.setVisibility(View.INVISIBLE);
                    mBinding.tvTip.setText("订单支付超时");
                }
            });
        } else {
            mBinding.cvTime.setVisibility(View.INVISIBLE);
            mBinding.tvTip.setText("订单支付超时");
        }
        mBinding.setItem(payEntity);
        mBinding.executePendingBindings();
    }


    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.cb_pay_wechat) {
            mBinding.cbPayWechat.setChecked(true);
            mBinding.cbPayAli.setChecked(false);
            mBinding.cbPayDigital.setChecked(false);

        } else if (i == R.id.cb_pay_ali) {
            mBinding.cbPayWechat.setChecked(false);
            mBinding.cbPayAli.setChecked(true);
            mBinding.cbPayDigital.setChecked(false);
        }else if (i == R.id.cb_pay_digital) {
            mBinding.cbPayWechat.setChecked(false);
            mBinding.cbPayAli.setChecked(false);
            mBinding.cbPayDigital.setChecked(true);
        } else if (i == R.id.tv_confirm) {
            if (Strings.toDouble(payEntity.getPay_amount()) > 0) {
                if (mBinding.cbPayAli.isChecked()) {
                    doOrderPayAli();
                } else if (mBinding.cbPayDigital.isChecked()){
                    showInputPasswordDialog();
                }else{
                    doOrderPayWechat();
                }
            } else {
               showInputPasswordDialog();
            }

        } else {
        }
    }

    //提醒用户输入支付密码的弹出框
    private void showInputPasswordDialog(){
        //查看用户是否设置支付密码
        if(UserInfoCache.get() == null){
            showError("请先登录");
            JumpUtil.overlay(this,LoginHomeActivity.class);
            return;
        }
        if(UserInfoCache.get().getIs_set_paypassword() != UserInfoEntity.BIND){
            showError("请先设置资产密码");
            JumpUtil.overlay(this,SetPayPasswordActivity.class);
            return;
        }
        if(mInputPasswordDialog == null){
            mInputPasswordDialog = new PropertyTakeOutInputPasswordDialog(this);
        }
        if(!mInputPasswordDialog.isShowing()){
            mInputPasswordDialog.showBottom();
        }
        mInputPasswordDialog.setClickNextStepListener(new PropertyTakeOutInputPasswordDialog.ClickNextStepListener() {
            @Override
            public void clickNextStep(String password,String verify_code) {
                doOrderPayETH(password,verify_code);
            }
        });
        mInputPasswordDialog.setClickVerifyCodeListener(new PropertyTakeOutInputPasswordDialog.ClickVerifyCodeListener(){
            @Override
            public void clickVerifyCode() {
                //获取验证码
                doAuthCode("pay");
            }
        });
    }


    //获取验证码
    private void doAuthCode(String type){
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doAuthCode(UserInfoCache.get().getPhone(),type)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError(data.getErrmsg());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        hideProgress();
                    }
                });
    }


    public void showAliPayResult(PayResult payResult) {
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功
        if (TextUtils.equals(resultStatus, "9000")) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            toPayResultAct("支付成功", false);
        } else {
            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
            payFail(payResult.getMemo());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showWXPayResult(WXPayResultEvent wxPayResultEvent) {
        //支付成功
        if (wxPayResultEvent.errorCode == 0) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            toPayResultAct("支付成功", false);
        }
        //支付失败
        else {
            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
            payFail("支付失败，请稍候再试");
        }
        hideProgress();
    }

    private void toPayResultAct(String tipMessage, boolean isDigitalPrice) {
        if (!TextUtils.isEmpty(orderId)) {
            EventBus.getDefault().post(new OrderUpdateEvent(orderId, Constant.ORDER_WAIT_SEND));
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(ORDER_ENTITY, getOrderDetailed());
        if (isDigitalPrice) {
            bundle.putString("ethPrice", ethPrice);
        }
        JumpUtil.overlay(this, PayResultActivity.class, bundle);
        finish();
    }

    private OrderDetailsEntity getOrderDetailed() {
        OrderDetailsEntity orderDetails = new OrderDetailsEntity();
        orderDetails.setPrice_total(payEntity.getShowPrice());
        return orderDetails;
    }

    private void payFail(String tipMessage) {
        ToastUtils.showShort(tipMessage);
    }

    //微信支付调起
    public void doWxPay(WXPayData entity) {
        if (entity != null) {
            PayReq req = new PayReq();
            req.appId = entity.getAppid();
            req.partnerId = entity.getPartnerid();
            req.prepayId = entity.getPrepayid();
            req.nonceStr = entity.getNoncestr();
            req.timeStamp = entity.getTimestamp();
            req.packageValue = "Sign=WXPay";
            req.sign = entity.getSign();
            req.extData = "app data"; // optional
            // 在支付之前，如果应用没有注册到微信，应该先调用将应用注册到微信
            IWXAPI api = WXAPIFactory.createWXAPI(getContext(), req.appId);
            api.sendReq(req);
        } else {
            hideProgress();
        }
    }

    private void doAliPay(final String key) {
        Observable.create(new ObservableOnSubscribe<PayResult>() {
            public void subscribe(ObservableEmitter<PayResult> oe) throws Exception {
                try {
                    PayTask alipay = new PayTask(getActivity());
                    Map<String, String> result = alipay.payV2(key, true);
                    oe.onNext(new PayResult(result));
                    oe.onComplete();
                } catch (Exception e) {
                    oe.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose((this).<PayResult>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<PayResult>() {
                    public void onSubscribe(Disposable d) {

                    }

                    public void onNext(PayResult payResult) {
                        Logs.e(payResult.toString());
                        showAliPayResult(payResult);
                    }

                    public void onError(Throwable e) {
                        ToastUtils.showShort("支付宝调用失败！");
                    }

                    public void onComplete() {
                        hideProgress();
                    }
                });

    }


    private void doGetOrderPayInfo() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getOrderPayInfo(orderId)
                .compose(RxSchedulers.<BaseData<OrderPayEntity>>compose())
                .compose((this).<BaseData<OrderPayEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<OrderPayEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<OrderPayEntity> data) {
                        payEntity = data.getData();
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }
    //支付宝支付
    private void doOrderPayAli() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .orderPayAli(payEntity.getPay_order_no(), "ALi")
                .compose(RxSchedulers.<BaseData<String>>compose())
                .compose((this).<BaseData<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<String>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<String> data) {
                        doAliPay(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }
    //微信支付
    private void doOrderPayWechat() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .orderPayWechat(payEntity.getPay_order_no(), "WeChat")
                .compose(RxSchedulers.<BaseData<WXPayData>>compose())
                .compose((this).<BaseData<WXPayData>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<WXPayData>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<WXPayData> data) {
                        doWxPay(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }

    //ETH支付
    private void doOrderPayETH(String pwd,String code) {
        if (Strings.isEmpty(pwd)){
            ToastUtils.showShort("请先输入交易密码");
            return;
        }
        pwd= MD5.GetMD5Code(pwd);
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .orderPayYMS(payEntity.getPay_order_no(), "Yms",pwd,code)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        if(mInputPasswordDialog != null){
                            mInputPasswordDialog.dismiss();
                        }
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        toPayResultAct("支付成功",true);
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                         if(mInputPasswordDialog != null){
                             mInputPasswordDialog.dismiss();
                         }
                    }
                });
    }

    private void doGetETHRate(){
        showProgress("");
        RetrofitApiFactory.createApi(ETHApi.class)
                .getETHRate(null)
                .compose(RxSchedulers.<BaseData<ETHRateEntity>>compose())
                .compose((this).<BaseData<ETHRateEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<ETHRateEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@android.support.annotation.NonNull BaseData<ETHRateEntity> ethRateEntityBaseData) {
                        hideProgress();
                        if (ethRateEntityBaseData.getErrcode()!= Constant.SUCCESS_CODE){
                            showError(ethRateEntityBaseData.getErrmsg());
                            return;
                        }

                        doGetETHRateSuccess(ethRateEntityBaseData.getData());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void doGetYms(){
        RetrofitApiFactory.createApi(OrderApi.class)
                .getYMS(0)
                .compose(RxSchedulers.<BaseData<YMSEntity>>compose())
                .compose((this).<BaseData<YMSEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<YMSEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<YMSEntity> data) {

                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doGetETHRateSuccess(ETHRateEntity ethRateEntity) {
//        if (payEntity==null || ethRateEntity==null || Strings.isEmpty(ethRateEntity.getEth_cny())){
//            mBinding.tvDigitalPrice.setVisibility(View.GONE);
//        }else{
//            mBinding.tvDigitalPrice.setVisibility(View.VISIBLE);
//        }
        double ethCny=Double.parseDouble(ethRateEntity.getEth_cny());
        DecimalFormat format = new DecimalFormat("###0.000000");
        format.setRoundingMode(RoundingMode.HALF_UP);
        ethPrice="ETH "+format.format(Double.parseDouble(payEntity.getPay_amount())/ethCny);
        mBinding.tvDigitalPrice.setText("数字YMS支付: "+ethPrice);
    }


    private void doOrderPayEth() {
        showProgress("");
        /*RetrofitApiFactory.createApi(OrderApi.class)
                .orderPayScore(payEntity.getPay_order_no(), "3")
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            toPayResultAct("支付成功");
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });*/

    }


}
