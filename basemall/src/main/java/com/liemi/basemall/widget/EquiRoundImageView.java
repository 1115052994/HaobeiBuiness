package com.liemi.basemall.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;
import com.netmi.baselibrary.utils.Densitys;

/**
 * 类描述：圆角图片显示、用于宽度撑满时，高度1:1显示
 * 创建人：Simple
 * 创建时间：2018/12/11
 * 修改备注：
 */
public class EquiRoundImageView extends RoundedImageView {

    public EquiRoundImageView(Context context) {
        this(context, null, 0);
    }

    public EquiRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EquiRoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCornerRadius(Densitys.dp2px(context, 4));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();

        if (d != null) {
            // ceil not round - avoid thin vertical gaps along the left/right edges
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //根据宽度设置等比高度
            setMeasuredDimension(width, width);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }



}
