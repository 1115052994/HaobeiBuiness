package com.netmi.workerbusiness.data.cache;

import android.text.TextUtils;

import com.netmi.baselibrary.data.cache.PrefCache;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/4/14
 * 修改备注：
 */
public class WithdrawCache {

    public static final String ALI_NAME = "ali_name";
    public static final String ALI_ACCOUNT = "ali_account";
    public static final String WECHAT_NAME = "wechat_name";
    public static final String WECHAT_ACCOUNT = "wechat_account";

    public static void putAliname(String name) {
        if (!TextUtils.isEmpty(name)) {
            PrefCache.putData(ALI_NAME, name);
        }
    }

    public static String getAliName() {
        return (String) PrefCache.getData(ALI_NAME, "");
    }

    public static void putAliaccount(String account) {
        if (!TextUtils.isEmpty(account)) {
            PrefCache.putData(ALI_ACCOUNT, account);
        }
    }

    public static String getAliAccount() {
        return (String) PrefCache.getData(ALI_ACCOUNT, "");
    }

    public static void putWechatname(String name) {
        if (!TextUtils.isEmpty(name)) {
            PrefCache.putData(WECHAT_NAME, name);
        }
    }

    public static String getWechatName() {
        return (String) PrefCache.getData(WECHAT_NAME, "");
    }

    public static void putWechatAccount(String account) {
        if (!TextUtils.isEmpty(account)) {
            PrefCache.putData(WECHAT_ACCOUNT, account);
        }
    }

    public static String getWechatAccount() {
        return (String) PrefCache.getData(WECHAT_ACCOUNT, "");
    }

    public static void clear() {
        PrefCache.removeData(ALI_NAME);
        PrefCache.removeData(ALI_ACCOUNT);
        PrefCache.removeData(WECHAT_NAME);
        PrefCache.removeData(WECHAT_ACCOUNT);
    }


}
