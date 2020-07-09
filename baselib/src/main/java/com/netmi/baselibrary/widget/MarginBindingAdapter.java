package com.netmi.baselibrary.widget;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/7 9:34
 * 修改备注：
 */
public class MarginBindingAdapter {

    @BindingAdapter("setMarginTop")
    public static void setTopMargin(View view, float topMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, (int) topMargin,
                layoutParams.rightMargin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }


    @BindingAdapter("setMarginLeft")
    public static void setMarginLeft(View view, float leftMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins((int) leftMargin, layoutParams.topMargin,
                layoutParams.rightMargin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }


    @BindingAdapter("setMarginRight")
    public static void setMarginRight(View view, float rightMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                (int) rightMargin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }
}
