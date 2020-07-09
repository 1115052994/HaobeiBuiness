package com.liemi.basemall.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.liemi.basemall.R;


/**
 * Created by Bingo on 2018/7/7.
 */

public class MyBaseDialog extends Dialog {
    public MyBaseDialog(Context context) {
        super(context, R.style.my_dialog_style);
    }

    public MyBaseDialog(Context context, int themeId){
        super(context,themeId);
    }

    public static MyBaseDialog getDialog(Context content, View view){
        final MyBaseDialog cardDialog = new MyBaseDialog(content);
        cardDialog.setContentView(view);
        return cardDialog;
    }
    public static MyBaseDialog getDialog(Context content, int resId){
        final MyBaseDialog cardDialog = new MyBaseDialog(content);
        cardDialog.setContentView(View.inflate(content,resId,null));
        return cardDialog;
    }

    public static MyBaseDialog getDialog(Context content, View view, int themeId){
        final MyBaseDialog cardDialog = new MyBaseDialog(content,themeId);
        cardDialog.setContentView(view);
        return cardDialog;
    }
    public static MyBaseDialog getDialog(Context content, int resId, int themeId){
        final MyBaseDialog cardDialog = new MyBaseDialog(content,themeId);
        cardDialog.setContentView(View.inflate(content,resId,null));
        return cardDialog;
    }

    public void showBottom(){
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogWindow.setAttributes(lp);
            dialogWindow.setWindowAnimations(com.netmi.baselibrary.R.style.dialog_bottom_anim_style);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
        show();
    }
}
