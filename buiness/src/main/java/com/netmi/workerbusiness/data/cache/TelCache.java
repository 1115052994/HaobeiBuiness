package com.netmi.workerbusiness.data.cache;

import android.text.TextUtils;

import com.netmi.baselibrary.data.cache.PrefCache;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/12/17
 * 修改备注：
 */
public class TelCache {

    public static final String ADMIN_TEL = "admin_tel";

    public static void put(String tel) {
        if (!TextUtils.isEmpty(tel)) {
            PrefCache.putData(ADMIN_TEL, tel);
        }
    }

    public static String get() {
        return (String) PrefCache.getData(ADMIN_TEL, "");
    }

    public static void clear() {
        PrefCache.clearData();
    }

}
