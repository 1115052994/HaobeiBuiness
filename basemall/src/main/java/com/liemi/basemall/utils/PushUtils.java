package com.liemi.basemall.utils;


import android.text.TextUtils;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.SPs;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import static com.netmi.baselibrary.data.Constant.IS_PUSH;

/*
 * 信鸽推送注册和取消注册
 * */
public class PushUtils {

    /**
     * 普通注册信鸽推送,首页和登录成功之后可以调用这个方法进行设置，不需要自定义回调
     */
    public static void registerPush() {
        registerPush(new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                //信鸽推送注册成功
                Logs.i("PUSH_MESSAGE", "信鸽推送注册成功：" + o.toString());
                SPs.put(MApplication.getAppContext(), IS_PUSH, true);
            }

            @Override
            public void onFail(Object o, int i, String s) {

            }
        });
    }

    /**
     * 自定义回调注册信鸽推送，主要用于用户修改推送状态，防止由于用于操作太快多次进行设置
     *
     * @param listener 自定义回调
     */
    public static void registerPush(XGIOperateCallback listener) {
        Logs.i("PUSH_MESSAGE：绑定用户：" + AccessTokenCache.get().getUid());
        if (AccessTokenCache.get() != null && !TextUtils.isEmpty(AccessTokenCache.get().getUid())) {
            XGPushManager.bindAccount(MApplication.getAppContext(), Constant.PUSH_PREFIX + AccessTokenCache.get().getUid(), listener);
        } else {
            XGPushManager.registerPush(MApplication.getAppContext(), listener);
        }
    }


    //解绑信鸽推送
    public static void unRegisterPush() {
        unRegisterPush(new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Logs.i("PUSH_MESSAGE 信鸽推送解绑成功：" + i + "," + o.toString());
                SPs.put(MApplication.getAppContext(), IS_PUSH, false);
            }

            @Override
            public void onFail(Object o, int i, String s) {

            }
        });

    }

    /**
     * 自定义解绑接口
     */
    public static void unRegisterPush(XGIOperateCallback listener) {

        if (AccessTokenCache.get() != null && !TextUtils.isEmpty(AccessTokenCache.get().getUid())) {
            XGPushManager.delAccount(MApplication.getAppContext(), Constant.PUSH_PREFIX + AccessTokenCache.get().getUid(), listener);
        } else {
            XGPushManager.unregisterPush(MApplication.getAppContext(), listener);
        }
    }


}
