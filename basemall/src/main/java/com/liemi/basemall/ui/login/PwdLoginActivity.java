package com.liemi.basemall.ui.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.adapters.TextViewBindingAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.LoginApi;
import com.liemi.basemall.databinding.ModloginActivityPwdLoginBinding;
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
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;


import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.login.BindPhoneActivity.USER_BIND_PHONE_TOKEN;
import static com.liemi.basemall.ui.login.BindPhoneActivity.USER_WECHAT_OPEN_ID;
import static com.liemi.basemall.ui.login.SmsLoginActivity.START_FROM;
import static com.netmi.baselibrary.router.BaseRouter.ModLogin_ThirdLoginActivity;

/**
 * 类描述：账号密码登录页
 * 创建人：Simple
 * 创建时间：2017/12/4 20:33
 * 修改备注：
 */
public class PwdLoginActivity extends BaseActivity implements PlatformActionListener ,TextViewBindingAdapter.AfterTextChanged {

    public static final String START_FROM = "startFrom";
    private ModloginActivityPwdLoginBinding mPwdLoginBinding;
    protected int from = 0;
    private BindPhoneDialog mBindPhoneDialog;

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(LoginInfoCache.get().getLogin()) && mPwdLoginBinding != null)
            mPwdLoginBinding.etMobile.setText(LoginInfoCache.get().getLogin());
    }

    @Override
    protected int getContentView() {
        return R.layout.modlogin_activity_pwd_login;
    }

    @Override
    protected void initUI() {
        mPwdLoginBinding = DataBindingUtil.setContentView(this, getContentView());
        mPwdLoginBinding.setTextChange(this);
    }

    @Override
    protected void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            from = getIntent().getExtras().getInt(START_FROM, 0);
        }
    }


    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.iv_close) {
            //点击返回
            finish();

        } else if (view.getId() == R.id.tv_sms_login) {
            //点击短信登录
            if (from == 0) {
                Bundle bundle = new Bundle();
                bundle.putInt(START_FROM, 1);
                JumpUtil.startForResult(this, SmsLoginActivity.class, 1000, bundle);
            } else {
                finish();
            }

        } else if (view.getId() == R.id.tv_forget_password) {
            //点击忘记密码
            JumpUtil.overlay(this, ForgetPassActivity.class);
        } else if (view.getId() == R.id.tv_phone_register) {

            //点击快捷注册
            JumpUtil.startForResult(this, RegisterActivity.class, 1000, null);

        } else if (view.getId() == R.id.bt_login) {
            String login = mPwdLoginBinding.etMobile.getText().toString();
            String password = mPwdLoginBinding.etPassword.getText().toString();
            if (TextUtils.isEmpty(login)) {
                ToastUtils.showShort("请先输入手机号码");
            } else if (TextUtils.isEmpty(password)) {
                ToastUtils.showShort("请先输入密码");
            } else if (!Strings.isPhone(login)) {
                ToastUtils.showShort("请输入正确的手机号码");
            } else {
                //手机号密码登录
                doLogin(null, password, login, null, null,
                        Constant.LOGIN_PHONE);
            }
        } else if (view.getId() == R.id.ll_login_wechat) {
            Platform platform = ShareSDK.getPlatform(Wechat.NAME);
            if (!platform.isClientValid()) {
                showError("请先安装微信客户端");
                return;
            }
            showProgress("");
            platform.setPlatformActionListener(this);
            platform.authorize();
        }
    }

    @Override
    public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(platform.getDb().getUserId())) {
                    //微信登录
                    doLogin(null, null, null, null,
                            platform.getDb().getUserId(), Constant.LOGIN_WECHAT);
                } else {
                    showError("获取授权信息失败，请重试");
                }
            }
        });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        hideProgress();
        Logs.i("获取授权信息失败："+throwable.getMessage());
        showError("获取授权信息失败：" + throwable.getMessage());

    }

    @Override
    public void onCancel(Platform platform, int i) {
        hideProgress();
        showError("您取消了授权");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            loginSuccessFinish();
        }
    }

    //显示让用户绑定手机号的dialog
    private void showBindPhoneDialog(final String openId, final String token){
        if(mBindPhoneDialog == null){
            mBindPhoneDialog = new BindPhoneDialog(this);
        }
        if(!mBindPhoneDialog.isShowing()){
            mBindPhoneDialog.show();
        }
        mBindPhoneDialog.setClickBindPhoneListener(new BindPhoneDialog.ClickBindPhoneListener() {
            @Override
            public void clickBindPhone() {
                Bundle bundle = new Bundle();
                bundle.putString(USER_WECHAT_OPEN_ID,openId);
                bundle.putString(USER_BIND_PHONE_TOKEN,token);
                JumpUtil.overlay(PwdLoginActivity.this,BindPhoneActivity.class,bundle);
            }
        });
    }

    //登录成功结束页面
    protected void loginSuccessFinish() {
        //将之前的页面结束掉
        AppManager.getInstance().finishActivity(LoginHomeActivity.class);
        AppManager.getInstance().finishActivity(SmsLoginActivity.class);
        finish();
    }

    //执行登录操作
    protected void doLogin(final String userName, final String password, final String phone,
                           String code, final String openId, final String scenario) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doLogin(userName, MD5.GetMD5Code(password, true), phone, code, openId, scenario)
                .compose(this.<BaseData<UserInfoEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<UserInfoEntity>>compose())
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        //用户没有注册微信
                        if(data.getErrcode() == 50001){
                            doWechatRegister(openId);
                            return;
                        }
                        showError("登录成功");
                        if (scenario.equals(Constant.LOGIN_DEFAULT)) {
                            LoginInfoCache.put(new LoginInfoEntity(userName, password));
                        } else if (scenario.equals(Constant.LOGIN_PHONE)) {
                            LoginInfoCache.put(new LoginInfoEntity(phone, password));
                        }else if(scenario.equals(Constant.LOGIN_WECHAT)){
                            LoginInfoCache.put(new LoginInfoEntity(openId));
                        }
                        //查看是否绑定手机号
                        if(data.getData().getIs_bind_phone() != UserInfoEntity.BIND){
                            showBindPhoneDialog(openId,data.getData().getToken().getToken());
                            return;
                        }
                        //保存Token
                        AccessTokenCache.put(data.getData().getToken());
                        //保存用户信息
                        UserInfoCache.put(data.getData());
                        //查看是否需要注册新歌推送
                        if(SPs.isOpenPushStatus()){
                            PushUtils.registerPush();
                        }
                        loginSuccessFinish();
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

    //微信注册
    private void doWechatRegister(final String openId){
        RetrofitApiFactory.createApi(LoginApi.class)
                .doRegister(null,
                        null,null,
                        null,null,openId,"register_wechat")
                .compose(this.<BaseData<UserInfoEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<UserInfoEntity>>compose())
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        //查看是否绑定手机号
                        if(data.getData().getIs_bind_phone() != UserInfoEntity.BIND){
                            showBindPhoneDialog(openId,data.getData().getToken().getToken());
                            return;
                        }
                        LoginInfoCache.put(new LoginInfoEntity(openId));
                        //保存token信息
                        AccessTokenCache.put(data.getData().getToken());
                        //保存用户信息
                        UserInfoCache.put(data.getData());
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
        if(!TextUtils.isEmpty(mPwdLoginBinding.etMobile.getText().toString())
                && !TextUtils.isEmpty(mPwdLoginBinding.etPassword.getText().toString())){
            mPwdLoginBinding.btLogin.setEnabled(true);
        }else {
            mPwdLoginBinding.btLogin.setEnabled(false);
        }
    }
}
