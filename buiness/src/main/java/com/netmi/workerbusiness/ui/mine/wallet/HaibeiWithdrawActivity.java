package com.netmi.workerbusiness.ui.mine.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.ui.personal.digitalasset.PropertyTakeOutConfirmDialog;
import com.liemi.basemall.ui.personal.digitalasset.PropertyTakeOutInputPasswordDialog;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.WalletApi;
import com.netmi.workerbusiness.data.entity.walllet.WalletEntity;
import com.netmi.workerbusiness.databinding.ActivityHaibeiWithdrawBinding;
import com.netmi.workerbusiness.ui.mine.PassChooseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import static com.netmi.baselibrary.utils.JumpUtil.TYPE;
import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.TRANSFER_PASS;

public class HaibeiWithdrawActivity extends BaseActivity<ActivityHaibeiWithdrawBinding> {

    //确定对话框
    private String mTakeOutNum;   //输入的提现金额
    private String mHandlingNum;   //手续费
    private String mRealNum;       //实际提取
    //全部
    private String allAmount;


    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_haibei_withdraw;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("提取");
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_confirm) {
            check();
        } else if (view.getId() == R.id.tv_all) {
            mBinding.etNum.setText(allAmount);
        }

    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        getWalletInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        doGetUserInfo();
    }

    private void check() {
        String wallet_address = mBinding.etWalletAddress.getText().toString();
        String input_suggestion = mBinding.etNum.getText().toString();
        if (TextUtils.isEmpty(wallet_address)) {
            ToastUtils.showShort("请输入钱包地址");
        } else if (TextUtils.isEmpty(input_suggestion)) {
            ToastUtils.showShort("请输入提取额度");
        } else {
            getWithDrawFee();
        }
    }

    /**
     * 确认弹窗
     */
    private PropertyTakeOutConfirmDialog mCondirmDialog;

    private void showConfirmDialog() {

        if (mCondirmDialog == null) {
            mCondirmDialog = new PropertyTakeOutConfirmDialog(getContext());
        }
        mCondirmDialog.setPropertyNum(mTakeOutNum, mHandlingNum, mRealNum);
        mCondirmDialog.setClickConfirmListener(() -> showInputPasswordDialog());
        if (!mCondirmDialog.isShowing()) {
            mCondirmDialog.showBottom();
        }
    }

    /**
     * 密码和验证码弹窗
     */
    private PropertyTakeOutInputPasswordDialog mInputPasswordDialog;

    //提醒用户输入支付密码的弹出框
    private void showInputPasswordDialog() {
        if (UserInfoCache.get().getIs_set_paypassword() != UserInfoEntity.BIND) {
            showError("请先设置资产密码");
            JumpUtil.overlay(getContext(), SetPayPassActivity.class);
            return;
        }
        if (mInputPasswordDialog == null) {
            mInputPasswordDialog = new PropertyTakeOutInputPasswordDialog(getContext());
        }
        if (!mInputPasswordDialog.isShowing()) {
            mInputPasswordDialog.showBottom();
        }
        mInputPasswordDialog.setClickNextStepListener((password, verify_code) -> {
            mInputPasswordDialog.dismiss();
            doWithDrawal(password, verify_code);
        });
        mInputPasswordDialog.setClickVerifyCodeListener(() -> {
            //获取验证码
        });

    }

    private WalletEntity feeData;

    //获取提取手续费
    private void getWithDrawFee() {
        showProgress("");
        RetrofitApiFactory.createApi(WalletApi.class)
                .getWalletInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<com.netmi.workerbusiness.data.entity.walllet.WalletEntity>>() {
                    @Override
                    public void onSuccess(BaseData<com.netmi.workerbusiness.data.entity.walllet.WalletEntity> data) {
                        feeData = data.getData();
                        mTakeOutNum = mBinding.etNum.getText().toString();
                        mHandlingNum = String.valueOf(Double.valueOf(feeData.getExtract_fee_rate()) * Double.valueOf(mBinding.etNum.getText().toString()));
                        showConfirmDialog();
                    }
                });
    }

    //提取
    private void doWithDrawal(String password, String verify_code) {
        RetrofitApiFactory.createApi(WalletApi.class)
                .withdraw(mBinding.etWalletAddress.getText().toString()
                        , mBinding.etNum.getText().toString(), MD5.GetMD5Code(password, true))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    public void onSuccess(BaseData data) {
                        Bundle bundle = new Bundle();
                        bundle.putString(TYPE, "提取");
                        JumpUtil.overlay(getContext(), WalletResultActivity.class, bundle);
                        finish();
                    }

                });
    }

    private void getWalletInfo() {
        RetrofitApiFactory.createApi(WalletApi.class)
                .getWalletInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<com.netmi.workerbusiness.data.entity.walllet.WalletEntity>>() {
                    @Override
                    public void onSuccess(BaseData<com.netmi.workerbusiness.data.entity.walllet.WalletEntity> data) {
                        allAmount = data.getData().getHand_balance();
                        mBinding.tvRemark.setText(data.getData().getExtract_remark());
                    }
                });
    }


    private void doGetUserInfo() {
        RetrofitApiFactory.createApi(com.liemi.basemall.data.api.MineApi.class)
                .getUserInfo(0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        UserInfoCache.get().setIs_set_paypassword(data.getData().getIs_set_paypassword());
                    }
                });
    }


}
