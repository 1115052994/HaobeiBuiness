package com.netease.nim.uikit.business.team;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.netease.nim.uikit.R;

/**
 * Created by Bingo on 2018/7/7.
 */

public class NimBaseDialog extends Dialog {
    public NimBaseDialog(Context context) {
        super(context, R.style.sharemall_my_dialog_style);
    }

    public NimBaseDialog(Context context, int themeId){
        super(context,themeId);
    }

    public static NimBaseDialog getDialog(Context content, View view){
        final NimBaseDialog cardDialog = new NimBaseDialog(content);
        cardDialog.setContentView(view);
        return cardDialog;
    }
    public static NimBaseDialog getDialog(Context content, int resId){
        final NimBaseDialog cardDialog = new NimBaseDialog(content);
        cardDialog.setContentView(View.inflate(content,resId,null));
        return cardDialog;
    }

    public static NimBaseDialog getDialog(Context content, View view, int themeId){
        final NimBaseDialog cardDialog = new NimBaseDialog(content,themeId);
        cardDialog.setContentView(view);
        return cardDialog;
    }
    public static NimBaseDialog getDialog(Context content, int resId, int themeId){
        final NimBaseDialog cardDialog = new NimBaseDialog(content,themeId);
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
