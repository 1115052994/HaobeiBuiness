package com.netmi.workerbusiness.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netmi.baselibrary.R;
import com.netmi.baselibrary.data.cache.UserInfoCache;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/4 17:53
 * 修改备注：
 */
public class ContractVerifyDialog {
    private static Dialog mDialog;
    private static Context conte;
    private static MyListener myListener; //声明接口

    /**
     * 显示加载框
     */
    public static void show(Activity context) {
        conte = context;
        show(context, "", true);
        sendSMSOk();
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
            mDialog.setContentView(context.getLayoutInflater().inflate(R.layout.contract_dialog_verify, null));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(cancelable);
            mDialog.setOnCancelListener(listener);
            mDialog.findViewById(R.id.right_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendSMSOk();
                }
            });
            mDialog.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myListener != null) {
                        EditText viewById = mDialog.findViewById(R.id.ed_code);
                        String string = viewById.getText().toString();
                        if (string.isEmpty() || string.equals("")) {
                            Toast.makeText(context, "请输入正确的验证码", Toast.LENGTH_LONG).show();
                            return;
                        }
                        myListener.getData(string);
                    }

                }
            });
            ((TextView) mDialog.findViewById(R.id.send_phone)).setText("点击“获取验证码”后将发送短信验证码至" + UserInfoCache.get().getPhone());

            mDialog.show();
        } catch (Exception e) {
//            isShow();
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

    private static boolean isSend = true;

    public static void sendSMSOk() {
        isSend = false;
        new CountDownTimer(1000 * 60, 1000) {  // 倒计时 60 秒后可以重新获取验证码
            @Override
            public void onTick(long millisUntilFinished) {
                if (isSend) {
                    cancel();
                } else if (mDialog.findViewById(R.id.right_text) != null) {
                    String s = (millisUntilFinished / 1000) + conte.getString(R.string.pickerview_seconds);
                    ((TextView) mDialog.findViewById(R.id.right_text)).setText(s);
                }
            }

            @Override
            public void onFinish() {
                if (isSend || mDialog.findViewById(R.id.right_text) == null)
                    return;
                isSend = true;
                (mDialog.findViewById(R.id.right_text)).setEnabled(isSend);
                ((TextView) mDialog.findViewById(R.id.right_text)).setText("获取验证码");
            }
        }.start();
        (mDialog.findViewById(R.id.right_text)).setEnabled(isSend);
    }

    public interface MyListener {
        //通过抽象方法的参数传递数据的
        void getData(String res);
    }

    public static void setlict(MyListener my) {
        myListener = my;
    }

}

