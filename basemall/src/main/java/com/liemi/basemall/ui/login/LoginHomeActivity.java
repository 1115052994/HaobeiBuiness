package com.liemi.basemall.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.LoginApi;
import com.liemi.basemall.databinding.ModloginActivityLoginBinding;
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
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.SPs;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import static com.liemi.basemall.ui.login.BindPhoneActivity.USER_BIND_PHONE_TOKEN;
import static com.liemi.basemall.ui.login.BindPhoneActivity.USER_WECHAT_OPEN_ID;
import static com.netmi.baselibrary.router.BaseRouter.ModLogin_ThirdLoginActivity;

//@Route(path = ModLogin_ThirdLoginActivity)
/**
 * 登录注册首页
 */
public class LoginHomeActivity extends BaseActivity<ModloginActivityLoginBinding> implements PlatformActionListener {
    //让用户绑定手机号
    private BindPhoneDialog mBindPhoneDialog;
    @Override
    protected int getContentView() {
        return R.layout.modlogin_activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if(id == R.id.bt_sms_login){
            //点击短信登录
            JumpUtil.overlay(this,SmsLoginActivity.class);
            return;
        }
        if(id == R.id.bt_pwd_login){
            //点击密码登录
            JumpUtil.overlay(this,PwdLoginActivity.class);
            return;
        }
        if(id == R.id.tv_register_quick){
            //点击快捷注册
            JumpUtil.overlay(this,RegisterActivity.class);
            return;
        }
        if(view.getId() == R.id.ll_login_wechat){
            Platform platform = ShareSDK.getPlatform(Wechat.NAME);
            if (platform.isClientValid()) {
                showProgress("");
                platform.setPlatformActionListener(this);
                platform.authorize();
            } else {
                //没有安装微信客户端
                showError("请先安装微信客户端");
            }
        }
    }

    @Override
    public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!TextUtils.isEmpty(platform.getDb().getUserId())){
                    Logs.i("微信openId:"+platform.getDb().getUserId());
                    //微信登录
                    doLogin(platform.getDb().getUserId());
                }else{
                    showError("获取授权信息失败，请重试");
                }
            }
        });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        hideProgress();
        showError("获取授权信息失败："+throwable.getMessage());

    }

    @Override
    public void onCancel(Platform platform, int i) {
        hideProgress();
        showError("您取消了授权");

    }

    //查看是否绑定了手机号
    private void checkBindPhone(UserInfoEntity entity){

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
                JumpUtil.overlay(LoginHomeActivity.this,BindPhoneActivity.class,bundle);
            }
        });
    }

    //微信登录
    protected void doLogin(final String openId){
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doLoginWechat(openId,Constant.LOGIN_WECHAT)
                .compose(this.<BaseData<UserInfoEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<UserInfoEntity>>compose())
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        //微信没有注册，进行微信注册
                        if(data.getErrcode() == 50001){
                            doWechatRegister(openId);
                            return;
                        }
                        //用户没有绑定手机号，首先去绑定手机号
                        if(data.getData().getIs_bind_phone() != UserInfoEntity.BIND){
                            showBindPhoneDialog(openId,data.getData().getToken().getToken());
                            return;
                        }
                        LoginInfoCache.put(new LoginInfoEntity(openId));
                        //微信用户登录成功
                        //用户token信息
                        AccessTokenCache.put(data.getData().getToken());
                        //保存用户信息
                        UserInfoCache.put(data.getData());
                        //发送登录成功事件
                        EventBus.getDefault().post(new SwitchTabEvent(R.id.rb_home));
                        //查看是否需要注册新歌推送
                        if(SPs.isOpenPushStatus()){
                            PushUtils.registerPush();
                        }
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
                    public void onNext(BaseData<UserInfoEntity> baseData) {
                        if (baseData.getErrcode() == Constant.SUCCESS_CODE) {


                        } else {
                            showError(baseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        //查看用户是否绑定手机号
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
}
