package com.netmi.workerbusiness.ui;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.request.target.ViewTarget;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.language.MultiLanguageUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.im.DemoCache;
import com.netmi.workerbusiness.im.NIMInitManager;
import com.netmi.workerbusiness.im.NimDemoLocationProvider;
import com.netmi.workerbusiness.im.NimSDKOptionConfig;
import com.netmi.workerbusiness.im.chatroom.ChatRoomSessionHelper;
import com.netmi.workerbusiness.im.config.preference.Preferences;
import com.netmi.workerbusiness.im.config.preference.UserPreferences;
import com.netmi.workerbusiness.im.mixpush.DemoMixPushMessageHandler;
import com.netmi.workerbusiness.im.mixpush.DemoPushContentProvider;
import com.netmi.workerbusiness.im.session.SessionHelper;
import com.netmi.workerbusiness.ui.login.LoginActivity;
import com.netmi.workerbusiness.ui.login.PersonalInfoActivity;
import com.sobot.chat.SobotApi;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import static com.netmi.baselibrary.data.Constant.PUSH_PREFIX;

public class MyApplication extends MApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //多语言初始化
        MultiLanguageUtil.init(this);

//        //信鸽推送相关配置
//        XGPushConfig.enableDebug(this, true);
//
        //智齿客服初始化
        SobotApi.initSobotSDK(this, "f96f28f095e64d589d5438571d9c272b", UserInfoCache.get().getUid());

        //百度地图初始化
        SDKInitializer.initialize(getApplicationContext());
        //友盟初始化
        UMShareAPI.get(this);//初始化sdk
        UMConfigure.setLogEnabled(true);//查看友盟运行日志   在log里面看  方便定位错误
        //友盟申请的appKey
        UMConfigure.init(this, "5f927efa8a5de91db33ea51d", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");


        //监听退出登录
        logoutListener = () -> {
            XGPushManager.delAccount(getApplicationContext(), PUSH_PREFIX + AccessTokenCache.get().getUid());
            //清除本地用户数据
//                LoginInfoCache.clear();//保存登陆信息
            AccessTokenCache.clear();
            UserInfoCache.clear();
            // 清理缓存&注销监听&清除状态
            Preferences.saveUserToken("");
            NimUIKit.logout();
            DemoCache.clear();

            //跳转到登陆页面
            JumpUtil.overlay(appManager.currentActivity(), LoginActivity.class);
            MyApplication.getInstance().appManager.finishAllActivity(LoginActivity.class);
        };

        ViewTarget.setTagId(R.id.tag_parent);
    }

    //各个平台的配置
    {
        //微信
        PlatformConfig.setWeixin("wx79e05e452118fc21", "715623e68f0a92f27a64ce73578b933c");
//        //新浪微博(第三个参数为回调地址)
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com/sina2/callback");
//        //QQ
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    //判断是否提交个人信息(支付宝，微信或银行卡)
    //该认证仅在进入门票理财之判断  -1未申请 0 申请中 1申请通过 2申请拒绝
    @Override
    public boolean checkCommitInfo() {

        if (UserInfoCache.get().getIs_auth() == 1) {
            return true;
        } else if (UserInfoCache.get().getIs_auth() == 0) {
            //   ToastUtils.showShort("身份资料审核中，请耐心等待");
            return true;
        } else {
            ToastUtils.showShort("请先进行实名认证");
            JumpUtil.overlay(appManager.currentActivity(), PersonalInfoActivity.class);
            return false;
        }
    }

    //判断是否实名认证(身份证)
    // -1：未申请过会员资料 0：后台审核中 1审核通过 2审核拒绝\
    @Override
    public boolean checkVerified() {
        if (UserInfoCache.get().getIs_auth_vip() == 1) {
            return true;
        } else if (UserInfoCache.get().getIs_auth_vip() == 0) {
            //  ToastUtils.showShort("身份资料审核中，请耐心等待");
            return true;
        } else {
            //  JumpUtil.overlay(appManager.currentActivity(), VerifiedActivity.class);
            return false;
        }
    }


    /**
     * 云信IM初始化
     */
    @Override
    public void initIM() {

        DemoCache.setContext(this);

        // 4.6.0 开始，第三方推送配置入口改为 SDKOption#mixPushConfig，旧版配置方式依旧支持。
        NIMClient.init(this, getLoginInfo(), NimSDKOptionConfig.getSDKOptions(this));

        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(this)) {
            // 注册自定义推送消息处理，这个是可选项
            NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());
            // init pinyin
            PinYin.init(this);
            PinYin.validate();
            // 初始化UIKit模块
            initUIKit();
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            //关闭撤回消息提醒
//            NIMClient.toggleRevokeMessageNotification(false);
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true);
        }
    }

    private void initUIKit() {
        // 初始化
        NimUIKit.init(this, buildUIKitOptions());

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // IM 会话窗口的定制初始化。
        SessionHelper.init();

        // 聊天室聊天窗口的定制初始化。
        ChatRoomSessionHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());
        //不需要显示在线
//        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";
        return options;
    }
}
