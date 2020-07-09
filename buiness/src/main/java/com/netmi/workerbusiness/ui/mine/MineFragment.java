package com.netmi.workerbusiness.ui.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.utils.Densitys;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.cache.TelCache;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.DialogMineTipsBinding;
import com.netmi.workerbusiness.databinding.FragmentMineBinding;
import com.netmi.workerbusiness.ui.login.PersonalInfoActivity;
import com.netmi.workerbusiness.ui.mine.wallet.SetPayPassActivity;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
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

    private String paySuccess = "您的商家认证已经通过，请缴纳服务费";
    private String payWait = "您已经缴纳服务费，请等待审核通过";
    private String payDefeat = "您的缴纳服务费审核失败，请重新提交";
    private String pay_status = "恭喜您！您的商家入驻申请已通过，当前活动期间免费入驻。祝您生意兴隆！";
    private int credit_score;
    private int code = 0;
    public static final String NEED_PAY = "need_pay";

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
                    showTipsDialog("您的信息正在认证中，请通过认证后再次操作，如有问题请联系管理员" + mBinding.getModel().getShop_admin_tel(),
                            "确定");
                } else {
                    args.putString(JumpUtil.VALUE, mBinding.getModel().getId());
                    JumpUtil.overlay(getContext(), StoreInfoActivity.class, args);
                }
            }
        } else if (id == R.id.ll_wallet) {  //钱包
            if (code != 20001 && UserInfoCache.get().getShop_pay_status().equals("2")) {
                if (UserInfoCache.get().getIs_set_paypassword() != UserInfoEntity.BIND) {
                    showError("请先设置资产密码");
                    args.putInt(JumpUtil.TYPE, 2000);
                    JumpUtil.overlay(getContext(), SetPayPassActivity.class, args);
                } else {
                    JumpUtil.overlay(getContext(), WalletActivity.class);
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
        }
    }


    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
//                        int num = 0;
                        UserInfoCache.get().setShop_pay_status(String.valueOf(data.getData().getShop_pay_status()));
                        UserInfoCache.get().setShop_apply_status(String.valueOf(data.getData().getShop_apply_status()));
                        TelCache.put(data.getData().getShop_admin_tel());
                        code = data.getErrcode();
                        mBinding.setModel(data.getData());
                        mBinding.tvPhone.setText("绑定手机号" + UserInfoCache.get().getPhone());
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
                        //用户申请入驻审核状态 0:未申请 1:审核中 2:审核成功 3:审核失败
                        if (mBinding.getModel().getShop_apply_status() == 1) {
                            showTipsDialog("您的信息正在认证中，请通过认证后再次操作，如有问题请联系管理员" + mBinding.getModel().getShop_admin_tel(), "确定");
                        } else if (mBinding.getModel().getShop_apply_status() == 2) {
//                            //用户缴费审核状态 0:未申请 1:审核中 2:审核成功 3:审核失败
                            /**
                             * 暂时隐藏，有缴费时显示
                             * */
//                            if (mBinding.getModel().getShop_pay_status() == 0) {
//                                showTipsDialog(paySuccess, "确定");
//                            } else if (mBinding.getModel().getShop_pay_status() == 1) {
//                                showTipsDialog(payWait, "确定");
//                            } else if (mBinding.getModel().getShop_pay_status() == 3) {
//                                showTipsDialog(payDefeat, "确定");
//                            } else
                            if (mBinding.getModel().getShop_pay_status() == 2 && mBinding.getModel().getIs_popup().equals("0")) {
                                //is_popup是否第一次弹窗 0弹1不弹
                                showTipsDialog(pay_status, "确定");
                                commit(data.getData().getId());
//                                将数据保存在本地，用来判断是否显示弹窗
//                                saveLocally();
                            }
                        } else if (mBinding.getModel().getShop_apply_status() == 3) {
                            showTipsDialog("商家认证失败，请重新认证" + " 失败原因（" + data.getData().getReason() + ")",
                                    "去认证");
//                            Log.e("weng", data.getData().getReason());
//                            + mBinding.getModel().getShop_admin_tel()
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
            showTipsDialog(pay_status, "确定");
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
                showTipsDialog(pay_status, "确定");
            }
            Log.e("weng3", SPs.get(getActivity(), NEED_PAY, "") + "");
        }

    }

    //提示确认弹窗
    private void showTipsDialog(String content, String confirmText) {
        final DialogMineTipsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_mine_tips, null, false);
        if (!TextUtils.isEmpty(confirmText)) {
            binding.tvConfirm.setText(confirmText);
        }
        if (!TextUtils.isEmpty(content)) {
            binding.tvContent.setText(content);
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.showDialog)
                .setView(binding.getRoot())
                .show();

        //此处设置位置窗体大小
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setLayout(Densitys.dp2px(getContext(), 250), LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        binding.tvConfirm.setOnClickListener((View v) -> {
            alertDialog.dismiss();
            Bundle args = new Bundle();
            if (TextUtils.equals(content, paySuccess) || TextUtils.equals(content, payDefeat)) {
                args.putInt(JumpUtil.TYPE, 1);
                args.putInt(JumpUtil.CODE, mBinding.getModel().getShop_pay_status());
                JumpUtil.overlay(getContext(), RenewalFeeActivity.class, args);
            }
            if (TextUtils.equals(content, payWait)) {
//                args.putInt(JumpUtil.TYPE, WAIT);
//                args.putSerializable(JumpUtil.VALUE, mBinding.getModel());
//                JumpUtil.overlay(getContext(), RenewalFeeActivity.class, args);
            }
            if (TextUtils.equals(confirmText, "去认证")) {
                JumpUtil.overlay(getContext(), PersonalInfoActivity.class);
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


}
