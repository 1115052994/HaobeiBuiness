package com.netmi.workerbusiness.ui.home.online;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewStub;

import com.liemi.basemall.data.event.OrderRefreshEvent;
import com.liemi.basemall.ui.NormalDialog;
import com.liemi.basemall.ui.personal.order.LogisticTrackActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PlatformEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.OrderApi;
import com.netmi.workerbusiness.data.entity.home.lineorder.LineOrderDetailEntity;
import com.netmi.workerbusiness.data.entity.home.lineorder.LineOrderListEntity;
import com.netmi.workerbusiness.databinding.ActivityLineOrderDetailBinding;
import com.netmi.workerbusiness.databinding.SharemallItemOrderDetailsGoodBinding;
import com.netmi.workerbusiness.databinding.SharemallLayoutMineOrderDetailsLogisticsBinding;
import com.netmi.workerbusiness.ui.utils.DensityUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class LineOrderDetailActivity extends BaseActivity<ActivityLineOrderDetailBinding> {
    //需要上个页面传递过来订单id
    public static final String ORDER_DETAILS_ID = "orderDetailsId";
    //根据需求查看是否加载物流信息View
    private ViewStub mLogisticsViewStub;
    //物流信息
    private String mLogisticsInfo;

    //订单id
    private int mOrderId;
    //订单详情数据
    private LineOrderDetailEntity mOrderDetailsEntity;

    //商品列表信息
    private BaseRViewAdapter<LineOrderListEntity.MainOrdersBean.OrderSkusBean, BaseViewHolder> goodAdapter;
    //订单状态
    private int status;
    //请求物流信息订单id
    private String order_no;
    private int type=0;


    @Override
    protected int getContentView() {
        return R.layout.activity_line_order_detail;
    }


    @Override
    protected void initUI() {
        getTvTitle().setText("订单详情");
        mOrderId = getIntent().getExtras().getInt(ORDER_DETAILS_ID);
        type = getIntent().getExtras().getInt("type");
        mLogisticsViewStub = mBinding.vsLogistics.getViewStub();
        mLogisticsViewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                SharemallLayoutMineOrderDetailsLogisticsBinding logisticsBinding = DataBindingUtil.bind(inflated);
                logisticsBinding.setLogisticsInfo(mLogisticsInfo);
            }
        });

        mBinding.rvMineOrderDetailsGoodsList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mBinding.rvMineOrderDetailsGoodsList.setLayoutManager(new LinearLayoutManager(this));
        goodAdapter = new BaseRViewAdapter<LineOrderListEntity.MainOrdersBean.OrderSkusBean, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_order_details_good;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                BaseViewHolder holder = new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        SharemallItemOrderDetailsGoodBinding binding1 = (SharemallItemOrderDetailsGoodBinding) binding;
                        if (binding1.getItem().getStatusFormat().equals(ResourceUtil.getString(R.string.sharemall_order_refund_apply))
                                || binding1.getItem().getStatusFormat().equals(ResourceUtil.getString(R.string.sharemall_order_refund_ing))
                                || binding1.getItem().getStatusFormat().equals(ResourceUtil.getString(R.string.sharemall_order_refund_complete))) {
                            canJump = false;
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
//                        if (view.getId() == R.id.tv_apply_after_sales) {
//                            //跳转到申请售后页面
//                            if (getItem(position).getStatus() == Constant.ORDER_WAIT_SEND || getItem(position).getStatus() == Constant.ORDER_WAIT_RECEIVE) {
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable(ORDER_ENTITY, mOrderDetailsEntity);
//                                bundle.putInt(REFUND_POSITION, position);
//                                JumpUtil.overlay(getActivity(), ApplyRefundTypeActivity.class, bundle);
//                            } else {
//                                //跳转到退款详情页
//                                Bundle bundle = new Bundle();
//                                bundle.putString(REFUND_ID, String.valueOf(getItem(position).getId()));
//                                JumpUtil.overlay(getActivity(), RefundDetailedActivity.class, bundle);
//                            }
//                        } else {
//                            JumpUtil.overlay(getContext(), GoodDetailPageActivity.class, ITEM_ID, getItem(position).getItem_id());
//                        }
                    }
                };
                return holder;
            }
        };
        mBinding.rvMineOrderDetailsGoodsList.setAdapter(goodAdapter);
    }

    @Override
    protected void initData() {
        if (getIntent().getIntExtra(ORDER_DETAILS_ID, -1) == -1) {
            showError(getString(R.string.sharemall_order_error_aguments));
            finish();
            return;
        }
        mOrderId = getIntent().getIntExtra(ORDER_DETAILS_ID, -1);
        doGetOrderDetails();

    }

    boolean canJump = true;

    @Override
    public void doClick(View view) {
        super.doClick(view);
        Bundle args = new Bundle();
        //0-未付款1-待发货2-待收货3-待评价4-退货申请 5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
        if (view.getId() == R.id.tv_order_function1) {
//            clickLeftButton(this.mOrderDetailsEntity);。
            if (mBinding.getDetailsEntity().getStatus() == 9) {
                args.putString(JumpUtil.ID, mBinding.getDetailsEntity().getOrder_id());
                JumpUtil.overlay(getContext(), OrderEvaluationActivity.class, args);
            }
            return;
        }
        if (mOrderDetailsEntity != null) {
            args.putString(LogisticTrackActivity.MPID, mOrderDetailsEntity.getOrder_no());
        }
        if (view.getId() == R.id.tv_order_function2) {
            if (status == 1) {
//                showError("立即发货");
                if (canJump) {
                    //TYPE 2  表示是在订单列表页面进入
                    args.putInt(JumpUtil.TYPE, 2);
                    args.putSerializable(JumpUtil.VALUE, mOrderDetailsEntity);
                    JumpUtil.overlay(getContext(), SendOutActivity.class, args);
                } else {
                    remindDialog();
                }
            } else {
//                showError("查看物流");
                JumpUtil.overlay(this, LogisticTrackActivity.class, args);
            }

            return;
        }
        if (view.getId() == R.id.cl_order_details_logistics) {
            Bundle bundle = new Bundle();
            bundle.putString(LogisticTrackActivity.MPID, mOrderDetailsEntity.getOrder_no());
            JumpUtil.overlay(this, LogisticTrackActivity.class, bundle);
            return;
        }
        if (view.getId() == R.id.tv_store_name) {
//            if (!TextUtils.isEmpty(mOrderDetailsEntity.getMainOrders().get(0).getShop_id())) {
//                JumpUtil.overlay(getContext(), StoreDetailActivity.class, STORE_ID, mOrderDetailsEntity.getMainOrders().get(0).getShop_id());
//            }
        }
        if (view.getId() == R.id.tv_copy_order_num) { //复制订单编号
            KeyboardUtils.putTextIntoClip(this, mBinding.tvOrderNum.getText().toString());
        }
    }

    private NormalDialog mLogoutConfirmDialog;

    //显示提醒订单在退款中
    private void remindDialog() {

        if (mLogoutConfirmDialog == null) {
            mLogoutConfirmDialog = new NormalDialog(getContext());
        }

        if (!mLogoutConfirmDialog.isShowing()) {
            mLogoutConfirmDialog.show();
        }
        mLogoutConfirmDialog.setMessageInfo(getString(R.string.remind_text), true, 16, true);
        mLogoutConfirmDialog.setTitleInfo("", false);
        mLogoutConfirmDialog.showLineTitleWithMessage(false);
        mLogoutConfirmDialog.setCancelInfo(getString(R.string.cancel), true);
        mLogoutConfirmDialog.setConfirmInfo(getString(R.string.confirm));

    }

    //EventBus如果退款退货成功，就刷新数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderRefresh(OrderRefreshEvent event) {
        Logs.i("退款/退款退货成功，订单详情页面刷新数据");
        doGetOrderDetails();
    }


    //设置数据
    private void showData(LineOrderDetailEntity entity) {
        this.mOrderDetailsEntity = entity;
        mBinding.setDetailsEntity(entity);
        goodAdapter.setData(entity.getGoods());
        //设置留言信息
        if (entity.getMainOrders() != null && entity.getMainOrders().size() > 0 && !Strings.isEmpty(entity.getMainOrders().get(0).getRemark())) {
            mBinding.setRemark(entity.getMainOrders().get(0).getRemark());
        }
        if (entity.getStatus() == Constant.ORDER_WAIT_RECEIVE || entity.getStatus() == Constant.ORDER_WAIT_COMMENT || entity.getStatus() == Constant.ORDER_SUCCESS) {
            //待收货，待评价，已完成订单会显示物流信息
            //请求物流信息
            order_no = entity.getOrder_no();
            doGetLogistic(entity.getOrder_no(), null, entity.getLogistics_no(), entity.getLogistics_company_code());
        }

        SpannableString sumSpan = new SpannableString(FloatUtils.formatMoney(entity.getGoods_amount()));
        //设置小计的字体颜色
        sumSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black_1d1e1f)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置前四个字符的字体大小
        sumSpan.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(getContext(), 14), false), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置价格的字体大小和颜色
        sumSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.sharemall_red_c40d00)), 3, sumSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sumSpan.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(getContext(), 18), false), 4, sumSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.tvGoodSum.setText(sumSpan);
    }


    private void doGetOrderDetails() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .getLineOrderDetail(mOrderId)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<LineOrderDetailEntity>>(this) {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        finish();
                    }

                    @Override
                    public void onSuccess(BaseData<LineOrderDetailEntity> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            if (data.getData() != null) {
                                showData(data.getData());
                                if(type==1){
                                    mBinding.tvOrderFunction2.setVisibility(View.GONE);
                                }else {
                                    if(data.getData().getRightButtonStr().isEmpty()){
                                        mBinding.tvOrderFunction2.setVisibility(View.GONE);
                                    }else {
                                        mBinding.tvOrderFunction2.setVisibility(View.VISIBLE);
                                    }
                                }
                                status = data.getData().getStatus();
                                doGetCountdown();
                                mBinding.tvOrderNum.setText(data.getData().getMainOrders().get(0).getOrder_no());
                            } else {
                                showError(getString(R.string.sharemall_lack_info));
                                finish();
                            }
                        } else {
                            showError(data.getErrmsg());
                            finish();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    //请求物流详情
    private void doGetLogistic(String orderNo, String refundNo, String code, String companyCode) {
        RetrofitApiFactory.createApi(OrderApi.class)
                .getLogistic(orderNo)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<List<com.liemi.basemall.data.entity.order.LogisticEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
//                        showError(ex.getMessage());
                        mBinding.logisticsInformation2.setVisibility(View.GONE);
                        mBinding.logisticsInformation.logisticsInformation3.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(@NonNull BaseData<List<com.liemi.basemall.data.entity.order.LogisticEntity>> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            if (data.getData() != null && data.getData().get(0).getList() != null && data.getData().get(0).getList().size() > 0) {
                                mLogisticsInfo = data.getData().get(0).getList().get(0).getContent();
                                if (mLogisticsViewStub.getParent() != null) {
                                    mLogisticsViewStub.inflate();
                                }
                            }
                        } else {
//                            showError(data.getErrmsg());
                            mBinding.logisticsInformation2.setVisibility(View.GONE);
                            mBinding.logisticsInformation.logisticsInformation3.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    //获取收货倒计时，评价倒计时
    private void doGetCountdown() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .getPlatformInfo("")
                .compose(this.<BaseData<PlatformEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<PlatformEntity>>compose())
                .subscribe(new XObserver<BaseData<PlatformEntity>>() {
                    @Override
                    public void onSuccess(BaseData<PlatformEntity> data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            if (data.getData() != null) {
                                if (status == 2) {         //2-待收货
                                    if (!DateUtil.getRemainTime(mBinding.getDetailsEntity().getMainOrders().get(0).getDeliver_time(), Integer.valueOf(data.getData().getAuto_confirm())).equals("")) {
                                        mBinding.tvCountdown.setText("还剩"
                                                + DateUtil.getRemainTime(mBinding.getDetailsEntity().getMainOrders().get(0).getDeliver_time(), Integer.valueOf(data.getData().getAuto_confirm()))
                                                + "自动确认");
                                    } else {
                                        mBinding.tvCountdown.setVisibility(View.GONE);
                                    }

                                } else if (status == 3) {  //3-待评价
                                    if (!DateUtil.getRemainTime(mBinding.getDetailsEntity().getMainOrders().get(0).getUpdate_time(), Integer.valueOf(data.getData().getAuto_evaluate())).equals("")) {
                                        mBinding.tvCountdown.setText("还剩"
                                                + DateUtil.getRemainTime(mBinding.getDetailsEntity().getMainOrders().get(0).getUpdate_time(), Integer.valueOf(data.getData().getAuto_evaluate()))
                                                + "自动好评"
                                        );
                                    } else {
                                        mBinding.tvCountdown.setVisibility(View.GONE);
                                    }
                                }
                            }
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
