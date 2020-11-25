package com.netmi.baselibrary.ui;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.listener.LogoutListener;
import com.netmi.baselibrary.utils.AppManager;
import com.tencent.bugly.Bugly;

import java.util.ArrayList;
import java.util.List;

import static com.netmi.baselibrary.router.BaseRouter.ModLogin_ThirdLoginActivity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/4 14:33
 * 修改备注：
 */
public class MApplication extends Application {

    private static MApplication instance;

    /**
     * App Activity 自定义栈管理
     */
    public AppManager appManager;

    /**
     * 退出的监听
     */
    public LogoutListener logoutListener;

    public MApplication() {
        super();
        instance = this;
    }

    public static MApplication getInstance() {
        if (instance == null)
            throw new IllegalStateException();
        return instance;
    }

    public static Context getAppContext() {
        if (instance == null)
            throw new IllegalStateException();
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appManager = AppManager.getInstance();
        appManager.init(this);

        ARouter.openLog();
        ARouter.openDebug();
        //路由初始化
        ARouter.init(this);

        //前往bugly平台申请APP_ID，并配置
        Bugly.init(getApplicationContext(), "b4c085ce50", false);

        //云信IM初始化
        initIM();
    }

    /**
     * 云信IM初始化
     */
    public void initIM() {

    }

    /**
     * 检查用户是否登录
     */
    public boolean checkUserIsLogin() {
//        Logs.e(""+TextUtils.isEmpty(AccessTokenCache.get().getToken()+"   :  "+UserInfoCache.get().getWallet_status()));
        if (TextUtils.isEmpty(AccessTokenCache.get().getToken()) || UserInfoCache.get().getShop_apply_status().equals("0")) {
            gotoLogin();
            return false;
        }
        return true;
    }

    /**
     * 检查用户是否通过审核
     */
    public boolean checkUserIsReview() { // "shop_apply_status": "1",审核结果 3：失败审核  1：未审核2：审核成功
        if (!UserInfoCache.get().getShop_apply_status().equals("2")) {
            return false;
        } else {
            return true;
        }

    }


    /**
     * 退出当前登录，跳转到登录界面
     */
    public void gotoLogin() {
        if (logoutListener != null) {
            logoutListener.logout();
        } else {
            //清除本地用户数据
            LoginInfoCache.clear();
            AccessTokenCache.clear();
            UserInfoCache.clear();
            appManager.finishAllActivity();
            //转到登录界面
            ARouter.getInstance()
                    .build(ModLogin_ThirdLoginActivity)
                    .navigation();
        }

    }


    /**
     * 退出当前登录，跳转到登录界面
     */
    public void gotoLogin(String name) {
        if (logoutListener != null) {
            logoutListener.logout();
        } else {
            //清除本地用户数据
            LoginInfoCache.clear();
            AccessTokenCache.clear();
            UserInfoCache.clear();
//            appManager.finishAllActivity();
            //转到登录界面
//            ARouter.getInstance()
//                    .build(ModLogin_ThirdLoginActivity)
//                    .navigation();
        }

    }

    /**
     * 判断是否提交个人信息(支付宝，微信或银行卡)
     */
    public boolean checkCommitInfo() {
        return false;
    }

    /**
     * 判断是否实名认证(身份证)
     */
    public boolean checkVerified() {
        return false;
    }


    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

}