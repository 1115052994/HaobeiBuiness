package com.netmi.baselibrary.data.cache;

import android.text.TextUtils;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/12/25
 * 修改备注：
 */
public class HeadUrlCache {
    public static final String HEAD_URL = "head_url";

    public static void put(String url) {
        if (!TextUtils.isEmpty(url)) {
            PrefCache.putData(HEAD_URL, url);
        }
    }

    public static String get() {
        return (String) PrefCache.getData(HEAD_URL, "");
    }

    public static void clear() {
        PrefCache.clearData();
    }

}
