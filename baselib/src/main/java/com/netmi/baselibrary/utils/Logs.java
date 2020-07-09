package com.netmi.baselibrary.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 14:04
 * 修改备注：
 */
public class Logs {

    private static String TAG = "Logs";

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化

    private Logs() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void setLOGTAG(String logTAG) {
        TAG = logTAG;
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug && !TextUtils.isEmpty(msg))
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug && !TextUtils.isEmpty(msg))
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug && !TextUtils.isEmpty(msg))
            Log.e(TAG, msg);
    }
    public static void e(Exception e) {
        if (isDebug && e!=null)
            Log.e("AndroidRuntime","--->Exception:",e);
    }

    public static void v(String msg) {
        if (isDebug && !TextUtils.isEmpty(msg))
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug && !TextUtils.isEmpty(msg))
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug && !TextUtils.isEmpty(msg))
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug && !TextUtils.isEmpty(msg))
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug && !TextUtils.isEmpty(msg))
            Log.i(tag, msg);
    }
}
