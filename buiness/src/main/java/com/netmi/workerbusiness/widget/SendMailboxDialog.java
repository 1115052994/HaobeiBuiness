package com.netmi.workerbusiness.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.netmi.baselibrary.R;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/4 17:53
 * 修改备注：
 */
public class SendMailboxDialog {
    private static Dialog mDialog;
    private static Context conte;

    /**
     * 显示加载框
     */
    public static void show(Activity context) {
        conte =context;
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
            mDialog = new Dialog(context, R.style.transparentDialog);
            mDialog.setContentView(context.getLayoutInflater().inflate(R.layout.contract_dialog_mailbox, null));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(cancelable);
            mDialog.setOnCancelListener(listener);
            mDialog.findViewById(R.id.img_doc).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            mDialog.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mailbox = ((EditText) mDialog.findViewById(R.id.ed_mailbox)).getText().toString().trim();
                    Toast.makeText(context,"发送成功，请前往邮箱查看",Toast.LENGTH_LONG).show();
                }
            });

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

