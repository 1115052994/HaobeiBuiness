package com.netmi.workerbusiness.ui.mine;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.Densitys;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.CouponApi;
import com.netmi.workerbusiness.data.api.LoginApi;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.api.WalletApi;
import com.netmi.workerbusiness.data.cache.TelCache;
import com.netmi.workerbusiness.data.entity.home.HaibeiConfidenceEntity;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.data.entity.mine.WalletEntity;
import com.netmi.workerbusiness.databinding.DialogMineTipsBinding;
import com.netmi.workerbusiness.databinding.FragmentMineBinding;
import com.netmi.workerbusiness.ui.login.BindingThreeActivity;
import com.netmi.workerbusiness.ui.login.ContractOfflineListActivity;
import com.netmi.workerbusiness.ui.login.PersonalOfflineInfoRestrictionsActivity;
import com.netmi.workerbusiness.ui.mine.wallet.SetPayPassActivity;
import com.netmi.workerbusiness.ui.utils.SobotApiUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/8/29
 * 修改备注：
 */
public class MineFragment extends BaseFragment<FragmentMineBinding> {
    public static final String TAG = MineFragment.class.getName();
    private static final int WAIT = 1000;
    private ShopInfoEntity shopInfoEntity;

    //    private String paySuccess = "您的商家认证已经通过，请缴纳服务费";
//    private String payWait = "您已经缴纳服务费，请等待审核通过";
//    private String payDefeat = "您的缴纳服务费审核失败，请重新提交";
    private String pay_status = "你的店铺已经审核通过\n祝商家生意兴隆";
    private String pay_defeat = "您的店铺审核失败，请重新审核";
    private int credit_score;
    private int code = 0;
    public static final String NEED_PAY = "need_pay";

    private ClipboardManager cm;
    private ClipData mClipData;
    private String synthesize;
    private String login;
    private String type;

    @Override
    protected int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initUI() {
        ((TextView) mBinding.getRoot().findViewById(R.id.tv_title)).setText("我的");
        mBinding.setDoClick(this);
        mBinding.refreshView.setOnRefreshListener(this::onRefresh);
    }

    @Override
    protected void initData() {

    }

    public void onRefresh() {
        doGetUserInfo();
        getWallet();
        mBinding.refreshView.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (code != 20001) {
            onRefresh();
        }
    }

    @Override
    public void onClick(View view) {
        Bundle args = new Bundle();
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.ll_renewal_fee) {  //续费
            if (code != 20001 && UserInfoCache.get().getShop_pay_status().equals("2")) {
                args.putInt(JumpUtil.TYPE, 1);
                args.putInt(JumpUtil.CODE, mBinding.getModel().getShop_pay_status());
                JumpUtil.overlay(getContext(), RenewalFeeActivity.class, args);
            }
        } else if (id == R.id.iv_edit_info) {  //店铺信息修改
            if (code != 20001 && UserInfoCache.get().getShop_pay_status().equals("2")) {
                if (mBinding.getModel().getShop_apply_status() == 1) {
                    showTipsDialog("你的信息正在审核中，请通过审核后再次操作，如有问题请\n联系平台客服",
                            "确定", "提示");
                } else {
                    args.putString(JumpUtil.VALUE, mBinding.getModel().getId());
                    JumpUtil.overlay(getContext(), StoreInfoActivity.class, args);
                }
            }
        } else if (id == R.id.ll_wallet || id == R.id.lilayout_centent) {  //钱包
            if (code != 20001 && UserInfoCache.get().getShop_pay_status().equals("2")) {
                if (UserInfoCache.get().getIs_set_paypassword() != UserInfoEntity.BIND) {
                    showError("请先设置资产密码");
                    args.putInt(JumpUtil.TYPE, 2000);
                    JumpUtil.overlay(getContext(), SetPayPassActivity.class, args);
                } else {
                    doShopInfo();

                }

            }
        }
//        else if (id == R.id.ll_store_credit_score) { //商品信用分
//            if (code != 20001 && UserInfoCache.get().getShop_pay_status().equals("2")) {
//                Bundle bundle = new Bundle();
//                bundle.putInt(JumpUtil.VALUE, credit_score);
//                JumpUtil.overlay(getContext(), StoreCreditScoreActivity.class, bundle);
//            }
//        }
        else if (id == R.id.ll_rule) {  //平台规则
            if (code != 20001 && UserInfoCache.get().getShop_pay_status().equals("2")) {
                JumpUtil.overlay(getContext(), AppRuleActivity.class);
            }
        } else if (id == R.id.ll_setting) { //设置
            JumpUtil.overlay(getContext(), SettingActivity.class);
        } else if (id == R.id.tv_copy) { //拷贝邀请码
            tvCopy();
        } else if (id == R.id.ll_message_management) { //信息管理

            if (shop_user_type == 2 || shop_user_type == 3) {//线下
                JumpUtil.overlay(getContext(), MessageOfflineActivity.class,"edit","1");
            } else JumpUtil.overlay(getContext(), MessageActivity.class);

        } else if (id == R.id.tv_setting) { //客服
            kefu();
        } else if (id == R.id.ll_contract) {
            JumpUtil.overlay(getContext(), ContractExamineListActivity.class);
        }
    }

    //客服
    private void kefu() {
        SobotApiUtils.getInstance().toCustomServicePage(getContext(), shopInfoEntity, null);
    }

    private void getWallet() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getWallet("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<WalletEntity>>() {
                    @Override
                    public void onSuccess(BaseData<WalletEntity> data) {
                        mBinding.tvMineBalance.setText("¥" + data.getData().getBalance());
                    }

                    @Override
                    public void onError(Throwable e) {
                        return;
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                    }
                });

        RetrofitApiFactory.createApi(WalletApi.class)
                .getWalletInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<com.netmi.workerbusiness.data.entity.walllet.WalletEntity>>() {
                    @Override
                    public void onSuccess(BaseData<com.netmi.workerbusiness.data.entity.walllet.WalletEntity> data) {
                        synthesize = data.getData().getHand_balance();
                        mBinding.tvWaitOne.setText("待结算：" + data.getData().getShop_freeze_price());

                        mBinding.tvHaibeiBalanceEstimate.setText(data.getData().getHand_yugu() + "元");
                        doGetWalletMessage();
                    }

                    @Override
                    public void onComplete() {
                        doGetWalletMessage();
                    }
                });
    }

    private void doShopInfo() {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        Bundle args = new Bundle();
                        args.putInt("SHOP_USER_TYPE", data.getData().getShop_user_type());
                        JumpUtil.overlay(getContext(), WalletActivity.class, args);
                        hideProgress();
                    }

                    @Override
                    public void onFail(BaseData<ShopInfoEntity> data) {
                        hideProgress();
                    }
                });
    }

    private int shop_user_type;//用户选择商户类型 0:未选择类型 1:线上 2:线下 3:线上+线下

    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        shopInfoEntity = data.getData();
                        shop_user_type = data.getData().getShop_user_type();//用户选择商户类型 0:未选择类型 1:线上 2:线下 3:线上+线下
                        UserInfoCache.get().setShop_pay_status(String.valueOf(data.getData().getShop_pay_status()));
                        UserInfoCache.get().setShop_apply_status(String.valueOf(data.getData().getShop_apply_status()));
                        TelCache.put(data.getData().getShop_admin_tel());
                        code = data.getErrcode();
                        mBinding.setModel(data.getData());
                        mBinding.tvPhone.setText("店铺账号 ：" + UserInfoCache.get().getPhone());
                        mBinding.tvId.setText("邀请码:" + UserInfoCache.get().getShare_code());
                        if (data.getData().getEnd_time() == null) {
                            mBinding.tvExpireTime.setVisibility(View.GONE);
                        } else {
                            mBinding.tvExpireTime.setText("到期时间：" + data.getData().getEnd_time());
                        }
                        if (data.getData().getExpire_notice() > 30 || mBinding.getModel().getShop_pay_status() != 2) {
                            mBinding.llRenewalFee.setVisibility(View.GONE);
                            mBinding.llRenewalFee.setClickable(false);
                        }
                        if (mBinding.getModel().getShop_apply_status() == 1 || mBinding.getModel().getShop_apply_status() == 3) {
                            mBinding.llRenewalFee.setVisibility(View.GONE);
                            mBinding.llRenewalFee.setClickable(false);
                            mBinding.llWallet.setVisibility(View.GONE);
                            mBinding.llStoreCreditScore.setVisibility(View.GONE);
                        } else if (mBinding.getModel().getShop_apply_status() == 2) {
                            mBinding.llWallet.setVisibility(View.VISIBLE);
                            mBinding.llStoreCreditScore.setVisibility(View.GONE);
                        }

                        //用户申请入驻审核状态 0:未申请 1:审核中 2:审核成功 3:审核失败    //Is_apply判断新老用户
                        if (mBinding.getModel().getShop_apply_status() == 1) {
                            if (mBinding.getModel().getIs_bind_bank().equals("0")) {
                                //跳转银行卡绑定
                                showTipsDialog("未绑定银行卡", "去绑定", "提示");
                            }else{
                                //审核中
                                showTipsDialog("你的信息正在审核中，请通过审核后再次操作，如有问题请\n联系平台客服", "确定", "提示");
                            }

                        } else if (mBinding.getModel().getShop_apply_status() == 2) {
                            if (shopInfoEntity.getShop_user_type() == 2) {
                                //Is_apply 0过，1弹框签署合同
                                if (shopInfoEntity.getIs_apply().equals(0)) {
                                    return;
                                } else {
                                    if (shopInfoEntity.getIs_improve_info().equals("0") && mBinding.getModel().getIs_apply() != "0") {
                                        //跳商家入驻
                                        showTipsDialog(getString(R.string.haibei_dialog_check),
                                                "去完善", "提示");

                                    }else if (mBinding.getModel().getIs_bind_bank().equals("0")) {
                                        //跳转银行卡绑定
                                        showTipsDialog("未绑定银行卡", "去绑定", "提示");
                                    }
//                                    else if (shopInfoEntity.getIs_improve_info().equals("1") && shopInfoEntity.getIs_sign_contract().equals("0") && mBinding.getModel().getIs_apply() != "0") {
//                                        //跳合同
//                                        showTipsDialog(getString(R.string.haibei_dialog_contrac),
//                                                "去签署合同", "提示");
//                                    }
                                }
                            }
                            if (mBinding.getModel().getShop_pay_status() == 2 && mBinding.getModel().getIs_popup().equals("0")) {
                                //is_popup是否第一次弹窗 0弹1不弹
                                showTipsDialog(pay_status, "前往运营台", "审核通过");
                                commit(data.getData().getId());
                            }
                        } else if (mBinding.getModel().getShop_apply_status() == 3) {

                            showTipsDialog("商家审核失败，请按提示重新提交" + " 原因:" + data.getData().getReason() + "",
                                    "去提交", "审核结果");
                        }
                        credit_score = data.getData().getScore();
                    }

                    @Override
                    public void onFail(BaseData<ShopInfoEntity> data) {
                        if (data.getErrcode() == 20001) {
                            code = data.getErrcode();
                            showError(data.getErrmsg());
                        }
                    }
                });
    }

    private void saveLocally() {
        //本地存储数据判断
//   if (SPs.get(getActivity(), NEED_PAY, "") == null || !TextUtils.equals(SPs.get(getActivity(), NEED_PAY, "") + "", (UserInfoCache.get().getUid()))) {
//   SPs.put(getActivity(), NEED_PAY, UserInfoCache.get().getUid());
//   showTipsDialog(pay_status, "确定");
//   }
        /**
         *要求：每个账号只能显示一次弹窗
         *方法：每次新的账号添加到后面以“，”隔开
         * */
        boolean isPresence = false;
        Log.e("weng1", SPs.get(getActivity(), NEED_PAY, "") + "");
        if (SPs.get(getActivity(), NEED_PAY, "") == null) {
            SPs.put(getActivity(), NEED_PAY, UserInfoCache.get().getUid());
            showTipsDialog(pay_status, "前往运营台", "审核通过");
        } else {
            String str = (String) SPs.get(getActivity(), NEED_PAY, "");
            String[] arr = str.split(",");
            List<String> list = Arrays.asList(arr);
            for (int i = 0; i < list.size(); i++) {
                if (TextUtils.equals(UserInfoCache.get().getUid(), list.get(i))) {
                    isPresence = true;
                }
            }
            if (!isPresence) {
                SPs.put(getActivity(), NEED_PAY, SPs.get(getActivity(), NEED_PAY, "") + "," + UserInfoCache.get().getUid());
                Log.e("weng2", SPs.get(getActivity(), NEED_PAY, "") + "");
                showTipsDialog(pay_status, "前往运营台", "审核通过");
            }
            Log.e("weng3", SPs.get(getActivity(), NEED_PAY, "") + "");
        }

    }

    //提示确认弹窗
    private void showTipsDialog(String content, String confirmText, String contentTitle) {

        final DialogMineTipsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_mine_tips, null, false);

        if (!TextUtils.isEmpty(confirmText)) {
            binding.tvConfirm.setText(confirmText);
        }
        if (!TextUtils.isEmpty(contentTitle)) {
            binding.tvHintDialogTips.setText(contentTitle);
        }
        if (!TextUtils.isEmpty(content)) {
            binding.tvContent.setText(content);
        }
        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.showDialog)
                .setView(binding.getRoot())
                .show();
        //此处设置位置窗体大小
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setLayout(Densitys.dp2px(getContext(), 250), LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
        binding.tvConfirm.setOnClickListener((View v) -> {
            alertDialog.dismiss();
            Bundle args = new Bundle();

            if (content.equals(getString(R.string.haibei_dialog_check))) {//信息入住
                    JumpUtil.overlay(getContext(), PersonalOfflineInfoRestrictionsActivity.class, "edit", "1",JumpUtil.VALUE, String.valueOf(shop_user_type),"name");
                } else if (TextUtils.equals(confirmText, "去绑定")) {//银行卡绑定
                JumpUtil.overlay(getContext(), BindingThreeActivity.class, "name", mBinding.getModel().getReal_name(), "idcard", mBinding.getModel().getIdcard(), "phone", mBinding.getModel().getPhone());
            }else
//                    if (content.equals(getString(R.string.haibei_dialog_contrac))) {//合同签署
//                    doVerified();
//                } else
                    if (TextUtils.equals(confirmText, "去提交")) {
                    if (shop_user_type == 2) {//用户选择商户类型 0:未选择类型 1:线上 2:线下 3:线上+线下
                        JumpUtil.overlay(getContext(), PersonalOfflineInfoRestrictionsActivity.class, JumpUtil.VALUE, String.valueOf(shop_user_type),"edit","1");
                    }

                }
        });
    }

    private void doVerified() {
        RetrofitApiFactory.createApi(LoginApi.class)
                .checkCompany(login, type)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new XObserver<BaseData>() {

                    public void onSuccess(BaseData baseData) {
                        JumpUtil.overlay(getContext(), ContractOfflineListActivity.class, "edit", "1");
                    }

                    @Override
                    public void onFail(BaseData data) {
                        if (data.getErrmsg().equals("已存在")) {
                            JumpUtil.overlay(getContext(), ContractOfflineListActivity.class, "edit", "0");
                        }else showError(data.getErrmsg());
                    }

                });
    }


    private void doGetUserInfo() {
        RetrofitApiFactory.createApi(com.liemi.basemall.data.api.MineApi.class)
                .getUserInfo(0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        UserInfoCache.put(data.getData());
                        doGetShopInfo();
                    }
                });
    }

    private void commit(String shop_id) {
        RetrofitApiFactory.createApi(MineApi.class)
                .commit(shop_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {

                    }
                });
    }


    private void doGetWalletMessage() {
        RetrofitApiFactory.createApi(CouponApi.class)
                .doList("1")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<HaibeiConfidenceEntity>>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(BaseData<PageEntity<HaibeiConfidenceEntity>> data) {
                        if (dataExist(data)) {
                            BigDecimal b = new BigDecimal((Double.parseDouble(data.getData().getSynthesize()) * Double.parseDouble(synthesize)));
                            //保留后两位 多余直接进1
                            mBinding.tvHaibeiBalance.setText("≈" + b.setScale(2, BigDecimal.ROUND_UP).doubleValue());
                        }
                    }
                });
    }

    public void tvCopy() {
        //获取剪贴板管理器：
        cm = (ClipboardManager) AppManager.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        mClipData = ClipData.newPlainText("Label", UserInfoCache.get().getShare_code());
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        showError("复制成功");

    }


}
