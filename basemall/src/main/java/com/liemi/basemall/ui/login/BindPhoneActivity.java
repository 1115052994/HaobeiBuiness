package com.liemi.basemall.ui.login;

import android.content.Intent;
import android.databinding.adapters.TextViewBindingAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.LoginApi;
import com.liemi.basemall.databinding.ActivityBindPhoneBinding;
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
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.LoginInfoEntity;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.data.event.SwitchTabEvent;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 绑定手机号页面
 */
public class BindPhoneActivity extends BaseActivity<ActivityBindPhoneBinding> implements TextViewBindingAdapter.OnTextChanged {
    //需要上个页面传递过来用户的openiD，微信昵称和微信头像地址
    public static final String USER_WECHAT_OPEN_ID = "userWechatOpenId";
    //需要上个页面传递过来用户token
    public static final String USER_BIND_PHONE_TOKEN = "userBindPhoneToken";
    //用户token
    private String mToken;
    //用户微信标识
    private String mOpenId;

    //是否发送验证码
    private boolean isSendCode = false;
    //获取验证码的倒计时
    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mBinding.tvGetVerificationCode.setText((millisUntilFinished / 1000) + "s后重新获取");
        }

        @Override
        public void onFinish() {
            mBinding.tvGetVerificationCode.setEnabled(true);
            mBinding.tvGetVerificationCode.setText(getString(R.string.re_get_verification_code));
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initUI() {
        mBinding.setTextChange(this);
        //获取上个页面传递的用户微信信息
        if (getIntent() != null && getIntent().getExtras() != null) {
            mOpenId = getIntent().getExtras().getString(USER_WECHAT_OPEN_ID);
            mToken = getIntent().getExtras().getString(USER_BIND_PHONE_TOKEN);
            if (TextUtils.isEmpty(mOpenId) || TextUtils.isEmpty(mToken)) {
                showError("登录信息出错，请重试");
                finish();
            }

        } else {
            showError("登录信息出错，请重试");
            finish();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.iv_close) {
            onBackPressed();
        }
        if (view.getId() == R.id.tv_get_verification_code) {
            if (!Strings.isPhone(mBinding.etInputPhone.getText().toString())) {
                showError(this.getString(R.string.please_input_right_phone));
                return;
            }
            doVerificationCode();
        }

        if (view.getId() == R.id.btn_bind_phone_finish) {
            if (!Strings.isPhone(mBinding.etInputPhone.getText().toString())) {
                showError(this.getString(R.string.please_input_right_phone));
                return;
            }
            if (TextUtils.isEmpty(mBinding.etInputVerificationCode.getText().toString())) {
                showError(this.getString(R.string.please_input_verification_code));
                return;
            }
            doBindPhone();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.onFinish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            //设置资产密码成功
            Intent intent = new Intent();
            intent.putExtra("register", "success");
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    //请求验证码
    private void doVerificationCode() {
        showProgress("");
        RetrofitApiFactory.createApi(com.liemi.basemall.data.api.LoginApi.class)
                .doAuthCode(mBinding.etInputPhone.getText().toString(), "bindPhone")
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError(data.getErrmsg());
                        isSendCode = true;
                        mBinding.tvGetVerificationCode.setEnabled(false);
                        countDownTimer.start();
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

    //请求绑定手机号
    private void doBindPhone() {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doBindPhone(mBinding.etInputPhone.getText().toString(),
                        mBinding.etInputVerificationCode.getText().toString(),mToken)
                .compose(this.<BaseData<UserInfoEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<UserInfoEntity>>compose())
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {

                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        //保存登录信息
                        LoginInfoCache.put(new LoginInfoEntity(mOpenId));
                        //保存token信息
                        AccessTokenCache.put(data.getData().getToken());
                        //保存用户信息
                        UserInfoCache.put(data.getData());
                        //发送登录成功事件
                        EventBus.getDefault().post(new SwitchTabEvent(R.id.rb_home));
                        //查看是否需要注册新歌推送
                        if(SPs.isOpenPushStatus()){
                            PushUtils.registerPush();
                        }
                        //绑定手机号成功
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

    //请求注册
    private void doRegister() {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doRegister(mBinding.etInputPhone.getText().toString(),
                        mBinding.etInputVerificationCode.getText().toString(), null,
                        null, null, mOpenId, "register_wechat")
                .compose(this.<BaseData<UserInfoEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<UserInfoEntity>>compose())
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        //保存token信息
                        AccessTokenCache.put(data.getData().getToken());
                        //保存用户信息
                        UserInfoCache.put(data.getData());
                        //发送登录成功事件
                        EventBus.getDefault().post(new SwitchTabEvent(R.id.rb_home));
                        Intent intent = new Intent();
                        intent.putExtra("register", "success");
                        setResult(RESULT_OK, intent);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(mBinding.etInputPhone.getText().toString())
                && !TextUtils.isEmpty(mBinding.etInputVerificationCode.getText().toString())
                && isSendCode) {
            mBinding.btnBindPhoneFinish.setEnabled(true);
        } else {
            mBinding.btnBindPhoneFinish.setEnabled(false);
        }
    }
}
