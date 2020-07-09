package com.liemi.basemall.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/23 13:59
 * 修改备注：
 */
public class MoneyTextView extends TextView {

    public MoneyTextView(Context context) {
        super(context);
    }

    public MoneyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text) && text.length() > 1) {
            int moneyIndex = text.toString().indexOf("YMS");
            int pointIndex = text.toString().lastIndexOf(".");
            int scoreIndex = text.toString().indexOf("贝");
            SpannableString spannableString = new SpannableString(text);
            if (moneyIndex > -1 && scoreIndex > -1) {
                spannableString.setSpan(new RelativeSizeSpan(0.9f), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                if (moneyIndex > -1) {
                    spannableString.setSpan(new RelativeSizeSpan(0.7f), moneyIndex, moneyIndex + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (pointIndex > -1) {
                    spannableString.setSpan(new RelativeSizeSpan(0.7f), pointIndex, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (scoreIndex > -1) {
                    spannableString.setSpan(new RelativeSizeSpan(0.7f), scoreIndex, scoreIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            super.setText(spannableString, BufferType.SPANNABLE);
        } else {
            super.setText(text, type);
        }
    }
}
