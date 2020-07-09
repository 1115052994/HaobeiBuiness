package com.netmi.baselibrary.utils;

import android.os.Bundle;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/27 11:13
 * 修改备注：
 */
public class FastBundle {

    private Bundle bundle;

    public FastBundle() {
        bundle = new Bundle();
    }

    public Bundle putInt(String key, int value) {
        bundle.putInt(key, value);
        return bundle;
    }

    public Bundle putString(String key, String value) {
        bundle.putString(key, value);
        return bundle;
    }

    public FastBundle put(String key, String value) {
        bundle.putString(key, value);
        return this;
    }

    public FastBundle put(String key, int value) {
        bundle.putInt(key, value);
        return this;
    }
}
