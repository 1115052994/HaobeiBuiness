package com.netmi.workerbusiness.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import com.netmi.workerbusiness.R;


/**
 * 音响绑定
 */
public class SoundBindDialog {
    private static Dialog mDialog;
    private static Activity conte;


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
    public static void show(Activity context,String text) {
        conte =context;
        show(context, text, true);
    }

//    /**
//     * 显示加载框
//     */
//    public static void show(Activity context, String info) {
//        show(context, info, true);
//    }

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
            View inflate = context.getLayoutInflater().inflate(R.layout.sound_dialog_bind, null);
            mDialog.setContentView(inflate);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(cancelable);
            mDialog.setOnCancelListener(listener);
            if(!info.equals("")){
                EditText editText = inflate.findViewById(R.id.ed_sound_id);
                editText.setText(info);
            }
            mDialog.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sound_id = ((EditText) mDialog.findViewById(R.id.ed_sound_id)).getText().toString().trim();
                    if(myListener!=null){
                        myListener.getData(sound_id);
                    }

                }
            });
            mDialog.findViewById(R.id.tv_dismis).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
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
    public static MyListener myListener; //声明接口
    public interface MyListener {
        //通过抽象方法的参数传递数据的
        void getData(String res);
    }
    public static void setlict(MyListener my){
        myListener=my;
    }


}

