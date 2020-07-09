package com.netmi.baselibrary.widget;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netmi.baselibrary.R;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/7 9:34
 * 修改备注：
 */
public class ImageViewBindingGlide {

    @BindingAdapter("ivPath")
    public static void imageLoad(ImageView view, String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if (view.getTag(R.id.tag_data) == null
                || !view.getTag(R.id.tag_data).equals(path)) {
            GlideShowImageUtils.displayNetImage(view.getContext(), path, view, R.drawable.baselib_bg_default_pic, 4);
            view.setTag(R.id.tag_data, path);
        }
    }

    @BindingAdapter("civPath")
    public static void imageCircleLoad(ImageView view, String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if (view.getTag(R.id.tag_data) == null
                || !view.getTag(R.id.tag_data).equals(path)) {
            GlideShowImageUtils.displayCircleNetImage(view.getContext(), path, view, R.drawable.baselib_bg_default_circle_pic);
            view.setTag(R.id.tag_data, path);
        }
    }

    @BindingAdapter("ivPathN")
    public static void imageLoadNormal(ImageView view, String path) {
        if (TextUtils.isEmpty(path)) {
            view.setImageResource(R.drawable.baselib_bg_default_pic);
            return;
        }
        if (view.getTag(R.id.tag_data) == null
                || !view.getTag(R.id.tag_data).equals(path)) {
            GlideShowImageUtils.displayNetImage(view.getContext(), path, view, R.drawable.baselib_bg_default_pic);
            view.setTag(R.id.tag_data, path);
        }
    }


    @BindingAdapter("civPathBorder")
    public static void imageCircleBorderLoad(ImageView view, String path) {
//        if (TextUtils.isEmpty(path)) {
//            return;
//        }
        if (view.getTag(R.id.tag_data) == null
                || !view.getTag(R.id.tag_data).equals(path)) {
            GlideShowImageUtils.displayCircleBorderNetImage(view.getContext(), path, view, 1, Color.WHITE, R.drawable.baselib_bg_default_circle_pic);
            view.setTag(R.id.tag_data, path);
        }
    }

    @BindingAdapter("ivPathResource")
    public static void imageLoadResource(ImageView view, int res) {
        if (res != 0) {
            view.setVisibility(View.VISIBLE);
            view.setImageResource(res);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("setTextColor")
    public static void setTextColor(TextView view, int res) {
        if (res != 0) {
            view.setTextColor(res);
        }
    }

}
