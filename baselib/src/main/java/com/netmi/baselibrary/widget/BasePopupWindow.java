package com.netmi.baselibrary.widget;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by Bingo on 2018/7/2.
 */

public class BasePopupWindow extends PopupWindow {

    private Activity activity;

    public BasePopupWindow setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    @Override
    public void dismiss() {
        resetBg();
        super.dismiss();
    }

    @Override
    public void showAsDropDown(View anchor) {
        setBg();
        super.showAsDropDown(anchor);
    }

    private void setBg() {
        if (activity == null) return;
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.5f;//取值范围0.0f-1.0f 值越小越暗
        activity.getWindow().setAttributes(params);
    }

    private void resetBg() {
        if (activity == null) return;
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        if (params.alpha != 1f) {
            params.alpha = 1f;//取值范围0.0f-1.0f 值越小越暗
            activity.getWindow().setAttributes(params);
        }
    }


    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        setBg();
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        setBg();
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setBg();
        super.showAtLocation(parent, gravity, x, y);
    }
}
