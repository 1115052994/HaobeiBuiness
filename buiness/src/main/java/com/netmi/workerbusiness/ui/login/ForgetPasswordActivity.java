package com.netmi.workerbusiness.ui.login;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

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
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityForgetPasswordBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;

public class ForgetPasswordActivity extends BaseActivity<ActivityForgetPasswordBinding> {


    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_forget_password;
    }

    /**
     * 界面初始化
     */
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

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {

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
                doSendSMS(phone, "reset");
            }
        } else if (id == R.id.tv_confirm) {
            check();
        }
    }


    private void check() {
        phone = mBinding.etPhone.getText().toString();
        code = mBinding.etCode.getText().toString();
        password = mBinding.etPassword.getText().toString();
        passwordAgain = mBinding.etPasswordAgain.getText().toString();
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
        } else {
            ResetPassword(phone, code,  MD5.GetMD5Code(password, true));
//            MD5.GetMD5Code(password, true)
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

    //忘记密码
    private void ResetPassword(final String phone, final String code, String password) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .resetPassword(password, code, phone)
                .compose(RxSchedulers.compose())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(@NonNull BaseData data) {
                        finish();
                    }
                });
    }
}
