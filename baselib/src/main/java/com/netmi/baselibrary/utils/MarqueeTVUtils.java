package com.netmi.baselibrary.utils;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/3/26 16:13
 * 修改备注：
 */
public class MarqueeTVUtils {
    /**
     * 跑马灯效果
     */
    public static TextView setMarqueeTextView(TextView textView) {
        if (textView == null)
            return null;
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setSelected(true);
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
        return textView;
    }

}
