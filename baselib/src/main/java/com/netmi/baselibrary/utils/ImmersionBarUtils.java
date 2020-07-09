package com.netmi.baselibrary.utils;

import android.app.Activity;

import com.gyf.barlibrary.ImmersionBar;
import com.netmi.baselibrary.R;

/**
 * 类描述：使用示例可移步http://doc.netmi.com.cn/web/#/32?page_id=3297
 * 创建人：Simple
 * 创建时间：2019/2/16
 * 修改备注：
 */
public class ImmersionBarUtils {

    /**
     * 设置白色的状态栏
     *
     * @param fits 解决布局与状态栏重叠问题，true基于状态栏开始显示，false基于顶部开始显示
     */
    public static void whiteStatusBar(Activity activity, boolean fits) {
        ImmersionBar immersionBar = ImmersionBar
                .with(activity)
                .statusBarColor(R.color.white)
                .fitsSystemWindows(fits);
        if (ImmersionBar.isSupportStatusBarDarkFont()) {
            immersionBar.statusBarDarkFont(true);
        } else {
            immersionBar.statusBarDarkFont(true, 0.2f);
        }
        immersionBar.init();
    }
    public static void colorStatusBar(Activity activity, boolean fits, int color) {
        ImmersionBar immersionBar = ImmersionBar
                .with(activity)
                .statusBarColor(color)
                .fitsSystemWindows(fits);
        if (ImmersionBar.isSupportStatusBarDarkFont()) {
            immersionBar.statusBarDarkFont(true);
        } else {
            immersionBar.statusBarDarkFont(true, 0.2f);
        }
        immersionBar.init();
    }

    //透明状态栏
    public static void tranStatusBar(Activity activity, boolean fits) {
        ImmersionBar immersionBar = ImmersionBar
                .with(activity)
                .statusBarColor(android.R.color.transparent)
                .fitsSystemWindows(fits);
//        if (ImmersionBar.isSupportStatusBarDarkFont()) {
//            immersionBar.statusBarDarkFont(true);
//        } else {
//            immersionBar.statusBarDarkFont(true, 0.2f);
//        }
        immersionBar.init();
    }

}
