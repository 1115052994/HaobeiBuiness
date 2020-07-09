package com.liemi.basemall.ui.personal.setting;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.HomeApi;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.ImgCodeEntity;
import com.liemi.basemall.databinding.ActivityChangePasswordBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.MD5;
import com.trello.rxlifecycle2.android.ActivityEvent;

/*
 * 通过验证之前的密码设置登录密码
 * */
public class ChangePasswordActivity extends BaseActivity<ActivityChangePasswordBinding> {

    //查看是登录密码还是资产密码
    public static final String CHANGE_PASSWORD_TYPE = "changePasswordType";

    //登陆密码
    public static final int CHANGE_LOGIN_PASSWORD = 1;
    //资产密码
    public static final int CHANGE_TRADE_PASSWORD = 2;

    //修改密码类型，默认为登录密码
    private int mChangePasswordType = CHANGE_LOGIN_PASSWORD;

    @Override
    protected int getContentView() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initUI() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            mChangePasswordType = getIntent().getExtras().getInt(CHANGE_PASSWORD_TYPE);
        }

        if (mChangePasswordType == CHANGE_LOGIN_PASSWORD) {
            getTvTitle().setText("修改登录密码");
        } else if (mChangePasswordType == CHANGE_TRADE_PASSWORD) {
            getTvTitle().setText("修改资产密码");
        }
        setRemindInfo();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_confirm) {
            doClickSave();
            return;

        }

    }

    //设置提醒用户的信息
    protected void setRemindInfo() {
        mBinding.setInputInfo("请输入原密码");
        mBinding.setRemindInfo("忘记密码?");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && data != null) {
            if (data.getIntExtra("changePasswordResult", -1) == Constant.SUCCESS_CODE) {
                finish();
            }
        }
    }

    //点击保存
    protected void doClickSave() {
        if (TextUtils.isEmpty(mBinding.etInputCode.getText())) {
            showError("请输入原密码");
            return;
        }

        if (mBinding.etInputCode.getText().toString().length() < Constant.MIN_PASSWORD_LENGTH || mBinding.etInputCode.getText().toString().length() > Constant.MAX_PASSWORD_LENGTH) {
            showError("原密码格式不正确");
            return;
        }

        if (checkNewPassword()) {
            //请求修改密码的接口
            String md5OldPassword = MD5.GetMD5Code(mBinding.etInputCode.getText().toString(), true);
            String md5Password = MD5.GetMD5Code(mBinding.etInputNewPassword.getText().toString(), true);
            if (mChangePasswordType == CHANGE_LOGIN_PASSWORD) {
                doChangeLoginPassword(md5OldPassword, md5Password);
            } else if (mChangePasswordType == CHANGE_TRADE_PASSWORD) {
                doChangePayPassword(md5OldPassword, md5Password);
            } else {
                showError("未知修改类型");
            }
        }
    }

    //查看输入的新密码是否合法
    protected boolean checkNewPassword() {
        if (TextUtils.isEmpty(mBinding.etInputNewPassword.getText().toString())) {
            showError(getString(R.string.please_input_new_password));
            return false;
        }
        if (TextUtils.isEmpty(mBinding.etInputNewPasswordAgain.getText().toString())) {
            showError(getString(R.string.please_input_new_password_again));
            return false;
        }
        if (mBinding.etInputNewPassword.getText().toString().length() < Constant.MIN_PASSWORD_LENGTH || mBinding.etInputNewPassword.getText().toString().length() > Constant.MAX_PASSWORD_LENGTH) {
            showError("密码长度最小为6位");
            return false;
        }
        if (!mBinding.etInputNewPassword.getText().toString().equals(mBinding.etInputNewPasswordAgain.getText().toString())) {
            showError("两次输入的密码不匹配，请检查");
            return false;
        }
        return true;
    }


    //请求修改支付密码
    private void doChangePayPassword(String oldPassword, String password) {
        showProgress("");

        RetrofitApiFactory.createApi(MineApi.class)
                .doChangePayPassword(oldPassword, password)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("支付密码修改成功");
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });

    }

    //修改登录密码
    private void doChangeLoginPassword(String oldPassword, String password) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doChangeLoginPassword(oldPassword, password)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("登录密码修改成功");
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });
    }

}
