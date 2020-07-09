package com.liemi.basemall.ui.login;

import android.content.Intent;
import android.databinding.adapters.TextViewBindingAdapter;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.LoginApi;
import com.liemi.basemall.databinding.ModloginActivityRegisterBinding;
import com.liemi.basemall.ui.personal.setting.AboutUsActivity;
import com.liemi.basemall.utils.PushUtils;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.LoginInfoEntity;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;


public class RegisterActivity extends BaseActivity<ModloginActivityRegisterBinding> implements TextViewBindingAdapter.OnTextChanged {

    private CountDownTimer mCountDownTimer;
    //记录用户是否获取了验证码
    private boolean mGetAuthCode = false;

    @Override
    protected int getContentView() {
        return R.layout.modlogin_activity_register;
    }

    @Override
    protected void initUI() {
        mBinding.setTextChange(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();

        if (id == R.id.tv_agreement) {
            //点击注册协议
            doAgreement(1);
        }

        if (id == R.id.tv_get_auth_code) {
            //点击获取验证码
            if (checkPhone()) {
                doAuthCode();
            }
            return;
        }
        if (id == R.id.ll_service_agreement) {
            mBinding.cbCheck.setChecked(!mBinding.cbCheck.isChecked());
            if(checkPhone() && checkAuthCode() && checkPassword() && checkPasswordAgain() &&
                    mBinding.cbCheck.isChecked()){
                mBinding.btRegister.setEnabled(true);
            }else{
                mBinding.btRegister.setEnabled(false);
            }
            return;
        }
        if (id == R.id.bt_register) {
            //点击注册
            if (!mGetAuthCode) {
                showError("请先获取验证码");
                return;
            }
            if (mBinding.cbCheck.isChecked() && checkCanRegister() && checkPasswordRight()) {
                //判断用户两次输入的密码是否一致
                //开始注册
                doRegister();
            }
        }

    }

    //查看用户是否可以开始注册
    private boolean checkCanRegister() {
        if (!checkPhone()) {
            return false;
        }
        if (!checkAuthCode()) {
            return false;
        }
        if (!checkPassword()) {
            return false;
        }
        if (!checkPasswordAgain()) {
            return false;
        }
        return true;
    }

    //查看用户输入的手机号是否正确
    private boolean checkPhone() {
        if (Strings.isPhone(mBinding.etMobile.getText())) {
            return true;
        }
        showError(this.getString(R.string.please_input_right_phone));
        return false;
    }

    //查看用户是否输入验证码
    private boolean checkAuthCode() {
        if (TextUtils.isEmpty(mBinding.etAuthCode.getText().toString())) {
            showError(getString(R.string.please_input_auth_code));
            return false;
        }
        return true;
    }

    //查看用户是否输入密码
    private boolean checkPassword() {
        if (TextUtils.isEmpty(mBinding.etPassword.getText().toString())) {
            showError(getString(R.string.modlogin_please_input_password));
            return false;
        }
        if (mBinding.etPassword.getText().toString().length() < 6) {
            showError(getString(R.string.password_min_six));
            return false;
        }
        return true;
    }

    //查看用户是否再次输入密码
    private boolean checkPasswordAgain() {
        if (TextUtils.isEmpty(mBinding.etAgainPwd.getText().toString())) {
            showError(getString(R.string.modlogin_please_input_password_again));
            return false;
        }
        return true;
    }

    //查看用户两次输入的密码是否一致
    private boolean checkPasswordRight() {
        if (!mBinding.etAgainPwd.getText().toString().equals(mBinding.etPassword.getText().toString())) {
            showError("两次输入的密码不一致");
            return false;
        }
        return true;
    }

    //开始倒计时
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

    //获取验证码
    private void doAuthCode() {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doAuthCode(mBinding.etMobile.getText().toString(), Constant.AUTH_CODE_REGISTER)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError(data.getErrmsg());
                        mGetAuthCode = true;
                        mBinding.etAuthCode.setText("");
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

    //执行注册
    private void doRegister() {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doRegister(mBinding.etMobile.getText().toString(),
                        mBinding.etAuthCode.getText().toString(),
                        MD5.GetMD5Code(mBinding.etPassword.getText().toString(), true),
                        null,
                        null,
                        null,
                        "register_phone")
                .compose(this.<BaseData<UserInfoEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<UserInfoEntity>>compose())
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        showError("注册成功");
                        //保存登录信息
                        LoginInfoCache.put(new LoginInfoEntity(mBinding.etMobile.getText().toString(),
                                mBinding.etPassword.getText().toString()));
                        //保存token信息
                        AccessTokenCache.put(data.getData().getToken());
                        //保存用户信息
                        UserInfoCache.put(data.getData());
                        //查看是否需要注册新歌推送
                        if(SPs.isOpenPushStatus()){
                            PushUtils.registerPush();
                        }
                        AppManager.getInstance().finishActivity(LoginHomeActivity.class);
                        AppManager.getInstance().finishActivity(PwdLoginActivity.class);
                        AppManager.getInstance().finishActivity(SmsLoginActivity.class);
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


    //请求服务协议
    private void doAgreement(int type) {
        showProgress("");
        RetrofitApiFactory.createApi(com.netmi.baselibrary.data.api.LoginApi.class)
                .getAgreement(type)
                .compose(this.<BaseData<AgreementEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<AgreementEntity>>compose())
                .subscribe(new BaseObserver<BaseData<AgreementEntity>>() {
                    @Override
                    public void onNext(BaseData<AgreementEntity> agreementEntityBaseData) {
                        if (agreementEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent(RegisterActivity.this, BaseWebviewActivity.class);
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_TITLE, agreementEntityBaseData.getData().getTitle());
                            if (agreementEntityBaseData.getData().getLink_type().equals("2")) {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_URL);
                            } else {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_CONTENT);
                            }
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_CONTENT, agreementEntityBaseData.getData().getContent());
                            startActivity(intent);
                        } else {
                            showError(agreementEntityBaseData.getErrmsg());
                        }
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
        if (!TextUtils.isEmpty(mBinding.etMobile.getText().toString())
                && !TextUtils.isEmpty(mBinding.etAuthCode.getText().toString())
                && !TextUtils.isEmpty(mBinding.etPassword.getText().toString())
                && !TextUtils.isEmpty(mBinding.etAgainPwd.getText().toString())
                && mBinding.cbCheck.isChecked()
                && mGetAuthCode) {
            mBinding.btRegister.setEnabled(true);
        } else {
            mBinding.btRegister.setEnabled(false);
        }
    }
}
