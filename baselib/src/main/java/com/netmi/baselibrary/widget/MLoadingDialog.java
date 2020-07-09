package com.netmi.baselibrary.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.TextView;

import com.netmi.baselibrary.R;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/4 17:53
 * 修改备注：
 */
public class MLoadingDialog {
    private static Dialog mDialog;

    /**
     * 显示加载框
     */
    public static void show(Activity context) {
        show(context, "", true);
    }

    /**
     * 显示加载框
     */
    public static void show(Activity context, String info) {
        show(context, info, true);
    }

    /**
     * @param context
     * @param info
     * @param listener
     */
    public static void show(Activity context, String info, DialogInterface.OnCancelListener listener) {
        show(context, info, true, listener);
    }

    /**
     * 显示加载框
     */
    public static void show(Activity context, String info, boolean cancelable) {
        show(context, info, cancelable, null);
    }

    /**
     * @param context
     * @param info
     * @param cancelable
     * @param listener
     */
    public static void show(Activity context, String info, boolean cancelable, DialogInterface.OnCancelListener listener) {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                return;
            }
            mDialog = new Dialog(context, R.style.DialogTranslucent);
            mDialog.setContentView(context.getLayoutInflater().inflate(R.layout.baselib_dialog_progress, null));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(cancelable);
            mDialog.setOnCancelListener(listener);
            TextView mInfo = mDialog.findViewById(R.id.tv_msg);
            if (!TextUtils.isEmpty(info)) {
                mInfo.setText(info);
            }
            mDialog.show();
        } catch (Exception e) {

        }
    }

    /**
     * 对话框是否显示
     *
     * @return
     */
    public static boolean isShow() {
        if (mDialog != null && mDialog.isShowing()) {
            return true;
        }
        return false;
    }

    /**
     * 隐藏加载框
     */
    public static void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
    }

}

