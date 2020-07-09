package com.liemi.basemall.ui.login;

import android.databinding.DataBindingUtil;
import android.databinding.adapters.TextViewBindingAdapter;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.LoginApi;
import com.liemi.basemall.databinding.ModloginActivitySmsLoginBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class SmsLoginActivity extends PwdLoginActivity {

    private ModloginActivitySmsLoginBinding mSmsLoginBinding;
    private CountDownTimer mCountDownTimer;
    //是否获取验证码
    private boolean isGetCode = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(LoginInfoCache.get().getLogin()) && mSmsLoginBinding != null)
            mSmsLoginBinding.etMobile.setText(LoginInfoCache.get().getLogin());
    }

    @Override
    protected int getContentView() {
        return R.layout.modlogin_activity_sms_login;
    }

    @Override
    protected void initUI() {
        mSmsLoginBinding = DataBindingUtil.setContentView(this,getContentView());
        mSmsLoginBinding.setTextAfter(this);
        mSmsLoginBinding.etPassword.setInputType(EditorInfo.TYPE_CLASS_TEXT);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void doClick(View view) {
        int id = view.getId();
        if (view.getId() == R.id.iv_close) {
            //点击返回
            finish();
            return;
        }
        if (id == R.id.tv_get_auth_code) {
            //获取验证码
            if (checkPhone()) {
                doAuthCode();
            }
            return;
        }
        if (view.getId() == R.id.tv_phone_register) {
            //点击快捷注册
            JumpUtil.startForResult(this, RegisterActivity.class,1000,null);
            return;
        }
        //点击登录
        if (id == R.id.bt_login) {
            if (checkPhone() && checkAuthCode()) {
                doLogin(null, null, mSmsLoginBinding.etMobile.getText().toString(),
                        mSmsLoginBinding.etPassword.getText().toString(), null, Constant.LOGIN_CODE);
            }
            return;
        }
        //点击手机号密码登录
        if(id == R.id.tv_pwd_login){
            if(from == 0){
               Bundle bundle = new Bundle();
               bundle.putInt(START_FROM,1);
               JumpUtil.startForResult(this,PwdLoginActivity.class,1000,bundle);
            }else{
                finish();
            }
        }
        //点击微信登录
        if(view.getId() == R.id.ll_login_wechat){
            Platform platform = ShareSDK.getPlatform(Wechat.NAME);
            if(!platform.isClientValid()){
                showError("请先安装微信客户端");
                return;
            }
            showProgress("");
            platform.setPlatformActionListener(this);
            platform.authorize();
        }
    }

    //查看用户输入的手机号是否合法
    private boolean checkPhone() {
        Logs.i("用户输入的手机号码："+mSmsLoginBinding.etMobile.getText().toString());
        if (Strings.isPhone(mSmsLoginBinding.etMobile.getText().toString())) {
            return true;
        }
        showError(getString(R.string.please_input_right_phone));
        return false;
    }

    //查看用户是否输入验证码
    private boolean checkAuthCode() {
        if (TextUtils.isEmpty(mSmsLoginBinding.etPassword.getText().toString())) {
            showError(this.getString(R.string.please_input_auth_code));
            return false;
        }
        return true;
    }

    //开启倒计时
    private void startCountDownTimer() {
        mSmsLoginBinding.tvGetAuthCode.setEnabled(false);
        mCountDownTimer = new CountDownTimer(Constant.COUNT_DOWN_TIME_DEFAULT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mSmsLoginBinding.tvGetAuthCode.setText(millisUntilFinished / 1000 + getString(R.string.reget_auth_code));
            }

            @Override
            public void onFinish() {
                mSmsLoginBinding.tvGetAuthCode.setEnabled(true);
                mSmsLoginBinding.tvGetAuthCode.setText("获取验证码");
            }
        };
        mCountDownTimer.start();
    }



    //请求获取注册验证码
    private void doAuthCode() {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doAuthCode(mSmsLoginBinding.etMobile.getText().toString(), Constant.AUTH_CODE_LOGIN)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError(data.getErrmsg());
                        isGetCode = true;
                        startCountDownTimer();
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
    public void afterTextChanged(Editable s) {
        if(!TextUtils.isEmpty(mSmsLoginBinding.etMobile.getText().toString())
                && !TextUtils.isEmpty(mSmsLoginBinding.etPassword.getText().toString())
                && isGetCode){
            mSmsLoginBinding.btLogin.setEnabled(true);
        }else{
            mSmsLoginBinding.btLogin.setEnabled(false);
        }
    }
}
