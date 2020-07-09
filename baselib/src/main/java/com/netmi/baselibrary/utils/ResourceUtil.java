package com.netmi.baselibrary.utils;

import android.app.Activity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/1/2
 * 修改备注：
 */
public class ResourceUtil {

    public static String getString(int resId) {
        Activity activity = AppManager.getInstance().currentActivity();
        if (activity != null) {
            return activity.getString(resId);
        }
        return "";
    }

}
