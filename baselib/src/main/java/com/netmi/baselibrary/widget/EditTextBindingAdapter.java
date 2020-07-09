package com.netmi.baselibrary.widget;

import android.databinding.BindingAdapter;
import android.text.TextPaint;
import android.widget.EditText;
import android.widget.TextView;

import com.netmi.baselibrary.utils.MoneyValueFilter;
import com.netmi.baselibrary.utils.Strings;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/9 19:39
 * 修改备注：
 */
public class EditTextBindingAdapter {

    @BindingAdapter("setMoneyFilters")
    public static void setMoneyFilters(EditText view, String digits) {
        if (view != null) {
            view.setFilters(MoneyValueFilter.getFilters(Strings.toInt(digits)));
        }
    }

    @BindingAdapter("setTextBold")
    public static void setTextBold(TextView view, boolean isBold) {
        if (view != null) {
            TextPaint tp = view.getPaint();
            tp.setFakeBoldText(isBold);
        }
    }

}
