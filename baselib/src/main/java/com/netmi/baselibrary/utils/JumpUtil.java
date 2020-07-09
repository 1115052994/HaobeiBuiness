package com.netmi.baselibrary.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.View;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.util.ImageItemUtil;

import java.util.List;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;

/**
 * 类描述：Activity 界面跳转
 * 创建人：Simple
 * 创建时间：2017/9/6 10:12
 * 修改备注：
 */
public class JumpUtil {
    public static final int SUCCESS = 1;
    public static final String CODE = "code";
    public static final String TYPE = "type";
    public static final String VALUE = "value";
    public static final String RESULT = "result";
    public static final String ID = "id";
    public static final String UID = "uid";
    public static final String FLAG = "flag";

    /**
     * 不带参数的跳转
     *
     * @param context
     * @param targetClazz
     */
    public static void overlay(Context context, Class<? extends Activity> targetClazz) {
        Intent mIntent = new Intent(context, targetClazz);
        context.startActivity(mIntent);
    }

    /**
     * 带参数的跳转
     *
     * @param context
     * @param targetClazz
     * @param bundle
     */
    public static void overlay(Context context, Class<? extends Activity> targetClazz, Bundle bundle) {
        Intent mIntent = new Intent(context, targetClazz);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        context.startActivity(mIntent);
    }


    /**
     * 带字符串参数的跳转
     *
     * @param context
     * @param targetClazz
     * @param param
     */
    public static void overlay(Context context, Class<? extends Activity> targetClazz, String... param) {
        Intent mIntent = new Intent(context, targetClazz);
        if (param != null
                && param.length > 1) {
            for (int i = 0; i < param.length / 2; i++) {
                mIntent.putExtra(param[i * 2], param[i * 2 + 1]);
            }
        }
        context.startActivity(mIntent);
    }

    /**
     * @param context
     * @param targetClazz
     * @param bundle
     * @param flags
     */
    public static void overlay(Context context, Class<? extends Activity> targetClazz, Bundle bundle, Integer flags) {
        Intent mIntent = new Intent(context, targetClazz);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        if (flags != null) {
            mIntent.setFlags(flags);
        }
        context.startActivity(mIntent);
    }

    /**
     * @param context
     * @param targetClazz
     * @param requestCode
     * @param bundle
     */
    public static void startForResult(Activity context, Class<? extends Activity> targetClazz, int requestCode, Bundle bundle) {
        Intent mIntent = new Intent(context, targetClazz);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        context.startActivityForResult(mIntent, requestCode);
    }

    /**
     * @param fragment
     * @param targetClazz
     * @param requestCode
     * @param bundle
     */
    public static void startForResult(Fragment fragment, Class<? extends Activity> targetClazz, int requestCode, Bundle bundle) {
        Intent mIntent = new Intent(fragment.getActivity(), targetClazz);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        fragment.startActivityForResult(mIntent, requestCode);
    }

    @SafeVarargs
    public static void startSceneTransition(Activity context, Class<? extends Activity> targetClazz, Bundle bundle, Pair<View, String>... sharedElements) {
        Intent mIntent = new Intent(context, targetClazz);
        if (bundle != null) {
            mIntent.putExtras(bundle);
        }
        if (Build.VERSION.SDK_INT >= LOLLIPOP
                && sharedElements != null) {
            context.startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(context, sharedElements).toBundle());
        } else {
            context.startActivity(mIntent);
        }
    }

    /**
     * 图片预览界面
     */
    public static void overlayImagePreview(Activity context, List<String> previewImgs, int currentImg) {
        //打开预览
        Intent intentPreview = new Intent(context, ImagePreviewDelActivity.class);
        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, ImageItemUtil.String2ImageItem(previewImgs));
        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, currentImg);
        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
        intentPreview.putExtra(ImagePicker.EXTRA_PREVIEW_HIDE_DEL, true);
        context.startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
    }

}
