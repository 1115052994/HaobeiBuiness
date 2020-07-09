package com.netmi.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;
import com.netmi.baselibrary.utils.Densitys;

/**
 * 类描述：圆角矩形图片，默认圆角4dp
 * 创建人：Simple
 * 创建时间：2018/10/23 11:05
 * 修改备注：
 */
public class RoundImageView extends RoundedImageView {

    public RoundImageView(Context context) {
        this(context, null, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCornerRadius(Densitys.dp2px(context, 4));
    }

}
