package com.netmi.workerbusiness.ui.utils;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/7/19
 * 修改备注：
 */
public class AlertDialogButtonUtil {


    public static void setAlertButtonColor(AlertDialog alertDialog) {
        if (alertDialog != null) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#33B3FF"));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#666666"));
        }
    }

}
