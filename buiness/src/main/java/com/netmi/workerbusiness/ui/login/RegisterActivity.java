package com.netmi.workerbusiness.ui.login;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.utils.PushUtils;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
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
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityRegisterBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initUI() {

        new InputListenView(mBinding.tvConfirm, mBinding.etPhone, mBinding.etCode, mBinding.etPassword, mBinding.etPasswordAgain) {
            @Override
            public boolean customJudge() {
                return mBinding.etCode.getText().toString().length() > 3;
//                return true;
            }
        };
    }

    @Override
    protected void initData() {
        mBinding.setIsCheck(false);
    }

    private boolean isSend = true;

    public void sendSMSOk() {
        isSend = false;
        new CountDownTimer(1000 * 60, 1000) {  // 倒计时 60 秒后可以重新获取验证码
            @Override
            public void onTick(long millisUntilFinished) {
                if (isSend) {
                    cancel();
                } else if (mBinding.tvGetCode != null) {
                    String s = (millisUntilFinished / 1000) + getString(R.string.pickerview_seconds);
                    mBinding.tvGetCode.setText(s);
                }
            }

            @Override
            public void onFinish() {
                if (isSend || mBinding.tvGetCode == null)
                    return;
                isSend = true;
                mBinding.tvGetCode.setEnabled(isSend);
                mBinding.tvGetCode.setText(R.string.obtain_check_code);
            }
        }.start();
        mBinding.tvGetCode.setEnabled(isSend);
    }

    private String phone;
    private String code;
    private String password;
    private String passwordAgain;
    private String inviteCode;


    @Override
    public void doClick(View view) {
        phone = mBinding.etPhone.getText().toString();

        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_get_code) {
            if (TextUtils.isEmpty(phone)) {
                showError("请输入手机号");
            } else if (!Strings.isPhone(phone)) {
                showError("请输入正确的手机号");
            } else {
                doSendSMS(phone, "register");
            }
        } else if (id == R.id.tv_confirm) {
            check();
        } else if (id == R.id.cb_service) {
            if (mBinding.getIsCheck()) {
                mBinding.setIsCheck(false);
            } else {
                mBinding.setIsCheck(true);
            }
        } else if (id == R.id.tv_service) {//点击服务条款
            doAgreement(111);
        } else if (id == R.id.tv_secret) {//点击隐私协议
            doAgreement(127);
        }
    }

    private void check() {
        phone = mBinding.etPhone.getText().toString();
        code = mBinding.etCode.getText().toString();
        password = mBinding.etPassword.getText().toString();
        passwordAgain = mBinding.etPasswordAgain.getText().toString();
        inviteCode = mBinding.etInviteCode.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showError("请输入手机号");
        } else if (!Strings.isPhone(phone)) {
            showError("请输入正确的手机号");
        } else if (TextUtils.isEmpty(code)) {
            showError("请输入验证码");
        } else if (TextUtils.isEmpty(password)) {
            showError("请输入密码");
        } else if (TextUtils.isEmpty(passwordAgain)) {
            showError("请再次输入密码");
        } else if (password.length() < 6) {
            showError("密码必须大于6位，请重新输入");
        } else if (!password.equals(passwordAgain)) {
            showError("两次输入的密码不同，请重新输入");
        } else if (TextUtils.isEmpty(inviteCode)) {
            showError("请填写邀请码");
        } else if (!mBinding.getIsCheck()) {
            showError("请勾选《服务条约》");
        } else {
            doRegister(phone, inviteCode, code, MD5.GetMD5Code(password, true), MD5.GetMD5Code(password, true), "register_phone", null);
        }
    }

    //请求验证码
    private void doSendSMS(String phone, String type) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .getSmsCode(phone, type)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new XObserver<BaseData>() {
                    public void onSuccess(BaseData baseData) {
                        sendSMSOk();
                        showError(baseData.getErrmsg());
                    }

                    @Override
                    public void onFail(BaseData data) {
                        super.onFail(data);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                    }
                });
    }

    //注册
    private void doRegister(final String phone, String inviteCode, final String code, String password, String repassword, String scenario, String role) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doRegisterBussiness(phone, inviteCode, code, password, repassword, scenario, role)
                .compose(RxSchedulers.compose())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<UserInfoEntity> data) {
                        ToastUtils.showShort(getString(R.string.register_success));
                        UserInfoCache.put(data.getData());
                        AccessTokenCache.put(data.getData().getToken());
                        LoginInfoCache.put(new LoginInfoEntity(phone, password));
                        JumpUtil.overlay(getContext(), BusinessTypeActivity.class);
                    }
                });
    }

    //服务协议
    private void doAgreement(int type) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .getAgreement(type)
                .compose(this.<BaseData<AgreementEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<AgreementEntity>>compose())
                .subscribe(new BaseObserver<BaseData<AgreementEntity>>() {
                    @Override
                    public void onNext(BaseData<AgreementEntity> agreementEntityBaseData) {
                        if (agreementEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {
                            if (agreementEntityBaseData.getData() != null && agreementEntityBaseData.getData().getContent() != null && agreementEntityBaseData.getData().getTitle() != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString(WEBVIEW_TITLE, agreementEntityBaseData.getData().getTitle());
                                bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_CONTENT);
                                bundle.putString(WEBVIEW_CONTENT, agreementEntityBaseData.getData().getContent());
                                JumpUtil.overlay(getContext(), BaseWebviewActivity.class, bundle);
                            }
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
}
