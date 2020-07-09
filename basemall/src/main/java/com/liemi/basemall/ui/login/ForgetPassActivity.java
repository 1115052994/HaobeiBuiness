package com.liemi.basemall.ui.login;

import android.databinding.adapters.TextViewBindingAdapter;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.HomeApi;
import com.liemi.basemall.data.api.LoginApi;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.ImgCodeEntity;
import com.liemi.basemall.databinding.ActivityForgetPasswordBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.liemi.basemall.ui.personal.setting.ChangePasswordActivity.CHANGE_LOGIN_PASSWORD;
import static com.liemi.basemall.ui.personal.setting.ChangePasswordActivity.CHANGE_PASSWORD_TYPE;
import static com.liemi.basemall.ui.personal.setting.ChangePasswordActivity.CHANGE_TRADE_PASSWORD;

public class ForgetPassActivity extends BaseActivity<ActivityForgetPasswordBinding> implements TextViewBindingAdapter.OnTextChanged {

    private CountDownTimer mCountDownTimer;
    //忘记密码的类型
    private int mForgetPasswordType = CHANGE_LOGIN_PASSWORD;
    private ImgCodeEntity imgCode;

    @Override
    protected int getContentView() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initUI() {
        mBinding.setTextChange(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mForgetPasswordType = getIntent().getExtras().getInt(CHANGE_PASSWORD_TYPE);
        }
        if (mForgetPasswordType == CHANGE_LOGIN_PASSWORD) {
            getTvTitle().setText("忘记登录密码");
            doGetImgCode(Constant.AUTH_RESET_PASSWORD);
        } else if (mForgetPasswordType == CHANGE_TRADE_PASSWORD) {
            getTvTitle().setText("忘记交易密码");
            doGetImgCode(Constant.AUTH_RESET_PAY_PASSWORD);
        }
    }


    @Override
    protected void initData() {
        mBinding.etInputPhone.setText(LoginInfoCache.get().getLogin());
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.iv_get_code) {
            if (mForgetPasswordType == CHANGE_LOGIN_PASSWORD) {
                doGetImgCode(Constant.AUTH_RESET_PASSWORD);
            } else if (mForgetPasswordType == CHANGE_TRADE_PASSWORD) {
                doGetImgCode(Constant.AUTH_RESET_PAY_PASSWORD);
            } else {
                showError("未知修改类型");
            }
        }
        if (id == R.id.tv_get_auth_code) {
            if (checkPhone()) {
                if (mForgetPasswordType == CHANGE_LOGIN_PASSWORD) {
                    doAuthCode(Constant.AUTH_RESET_PASSWORD);
                } else if (mForgetPasswordType == CHANGE_TRADE_PASSWORD) {
                    doAuthCode(Constant.AUTH_RESET_PAY_PASSWORD);
                } else {
                    showError("未知修改类型");
                }
            }
            return;
        }
        if (id == R.id.btn_confirm) {
            if (checkPhone() && checkAuthCode() && checkNewPassword() &&
                    checkNewPasswordAgain() && checkPasswordRight()) {
                if (mForgetPasswordType == CHANGE_LOGIN_PASSWORD) {
                    doForgetLoginPassword();
                } else if (mForgetPasswordType == CHANGE_TRADE_PASSWORD) {
                    doForgetPayPassword();
                } else {
                    showError("未知修改类型");
                }
            }
            return;
        }
    }

    //查看用户输入的手机号是否合法
    private boolean checkPhone() {
        if (Strings.isPhone(mBinding.etInputPhone.getText().toString())) {
            return true;
        }
        showError(getString(R.string.please_input_right_phone));
        return false;
    }

    //查看用户是否输入验证码
    private boolean checkAuthCode() {
        if (TextUtils.isEmpty(mBinding.etInputAuthCode.getText().toString())) {
            showError(getString(R.string.modlogin_please_input_code));
            return false;
        }
        return true;
    }

    //查看用户是否输入新密码
    private boolean checkNewPassword() {
        if (TextUtils.isEmpty(mBinding.etInputNewPassword.getText().toString())) {
            showError(getString(R.string.please_input_new_password));
            return false;
        }
        return true;
    }

    //查看用户是否输入确认密码
    private boolean checkNewPasswordAgain() {
        if (TextUtils.isEmpty(mBinding.etInputNewPasswordAgain.getText().toString())) {
            showError(getString(R.string.please_input_new_password_again));
            return false;
        }
        return true;
    }

    //查看两次输入密码是否一致
    private boolean checkPasswordRight() {
        if (mBinding.etInputNewPasswordAgain.getText().toString().equals(
                mBinding.etInputNewPassword.getText().toString())) {
            return true;
        }
        showError("两次输入的密码不一致");
        return false;
    }

    //开启倒计时
    private void startCountDownTimer() {
        mBinding.tvGetAuthCode.setEnabled(false);
        mCountDownTimer = new CountDownTimer(Constant.COUNT_DOWN_TIME_DEFAULT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.tvGetAuthCode.setText(millisUntilFinished / 1000 + getString(R.string.reget_auth_code));
            }

            @Override
            public void onFinish() {
                mBinding.tvGetAuthCode.setEnabled(true);
                mBinding.tvGetAuthCode.setText(getString(R.string.modlogin_get_code));
            }
        };
        mCountDownTimer.start();
    }

    //获取图文验证
    private void doGetImgCode(String type) {
        showProgress("");
        RetrofitApiFactory.createApi(HomeApi.class)
                .getImgCode(type)
                .compose(this.<BaseData<ImgCodeEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<ImgCodeEntity>>compose())
                .subscribe(new XObserver<BaseData<ImgCodeEntity>>() {
                    public void onSuccess(BaseData<ImgCodeEntity> baseData) {

                        imgCode = baseData.getData();
                        mBinding.setImgCode(imgCode);
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });
    }

    //获取验证码
    private void doAuthCode(String type) {
        showProgress("");
        String sign = imgCode.getSign();
        RetrofitApiFactory.createApi(LoginApi.class)
                .doAuthCode(mBinding.etInputPhone.getText().toString(), sign, mBinding.etImgCode.getText().toString(), type)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError(data.getErrmsg());
                        startCountDownTimer();
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

    //忘记登录密码
    private void doForgetLoginPassword() {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doForgetPassword(MD5.GetMD5Code(mBinding.etInputNewPassword.getText().toString(), true),
                        mBinding.etInputAuthCode.getText().toString(),
                        mBinding.etInputPhone.getText().toString())
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        //重置成功
                        showError("重置登录密码成功");
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

    //忘记支付密码
    private void doForgetPayPassword() {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doForgetPayPassword(mBinding.etInputPhone.getText().toString(),
                        mBinding.etInputAuthCode.getText().toString(),
                        MD5.GetMD5Code(mBinding.etInputNewPassword.getText().toString(), true))
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        //重置成功
                        showError("重置支付密码成功");
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



    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(mBinding.etInputPhone.getText().toString()) &&
                !TextUtils.isEmpty(mBinding.etInputAuthCode.getText().toString()) &&
                !TextUtils.isEmpty(mBinding.etInputNewPassword.getText().toString()) &&
                !TextUtils.isEmpty(mBinding.etInputNewPasswordAgain.getText().toString())) {
            mBinding.btnConfirm.setEnabled(true);
        } else {
            mBinding.btnConfirm.setEnabled(false);
        }
    }
}
