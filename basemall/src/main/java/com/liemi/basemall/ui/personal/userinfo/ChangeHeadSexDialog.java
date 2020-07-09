package com.liemi.basemall.ui.personal.userinfo;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.DialogChangeHeadSexBinding;
import com.netmi.baselibrary.utils.Densitys;


public class ChangeHeadSexDialog extends Dialog {

    private String firstItemStr;//第一个item显示的文字
    private String secondItemStr;//第二个item显示的文字
    private String cancelStr = "取消";//取消按钮显示的文字

    private ClickFirstItemListener clickFirstItemListener;
    private ClickSecondItemListener clickSecondItemListener;
    private ClickCancelListener clickCancelListener;

    public ChangeHeadSexDialog(@NonNull Context context) {
        this(context, com.netmi.baselibrary.R.style.showDialog);
    }

    public ChangeHeadSexDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ChangeHeadSexDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    //设置按钮文字
    public void setButtonStr(String firstItemStr, String secondItemStr) {
        this.firstItemStr = firstItemStr;
        this.secondItemStr = secondItemStr;
    }

    public void setButtonStr(String firstItemStr, String secondItemStr, String cancelStr) {
        this.setButtonStr(firstItemStr, secondItemStr);
        this.cancelStr = cancelStr;
    }

    //设置接口
    public void setClickFirstItemListener(ClickFirstItemListener clickFirstItemListener) {
        this.clickFirstItemListener = clickFirstItemListener;
    }

    public void setClickSecondItemListener(ClickSecondItemListener clickSecondItemListener) {
        this.clickSecondItemListener = clickSecondItemListener;
    }

    public void setClickCancelListener(ClickCancelListener clickCancelListener) {
        this.clickCancelListener = clickCancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogChangeHeadSexBinding dialogChangeHeadSexBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_change_head_sex, null, false);
        setContentView(dialogChangeHeadSexBinding.getRoot());
        DialogChangeHeadSexClickListener clickListener = new DialogChangeHeadSexClickListener();
        dialogChangeHeadSexBinding.setClick(clickListener);
        dialogChangeHeadSexBinding.setCancelStr(cancelStr);
        dialogChangeHeadSexBinding.setFirstItem(firstItemStr);
        dialogChangeHeadSexBinding.setSecondItem(secondItemStr);
    }

    //dialog显示在底部
    public void showBottom() {
        show();
        //获取当前dialog显示的window
        Window mDialogWindow = getWindow();
        if (mDialogWindow != null) {
            WindowManager.LayoutParams params = mDialogWindow.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.BOTTOM;
            mDialogWindow.setAttributes(params);
            mDialogWindow.setWindowAnimations(com.netmi.baselibrary.R.style.dialog_bottom_anim_style);
            mDialogWindow.getDecorView().setPadding(Densitys.dp2px(getContext(), 15), Densitys.dp2px(getContext(), 15), Densitys.dp2px(getContext(), 15), Densitys.dp2px(getContext(), 15));
        }
    }

    //点击第一个item的接口
    public interface ClickFirstItemListener {
        void clickFirstItem(String string);
    }

    //点击第二个item的接口
    public interface ClickSecondItemListener {
        void clickSecondItem(String string);
    }

    //点击取消按钮
    public interface ClickCancelListener {
        void clickCancel();
    }

    //点击事件
    public class DialogChangeHeadSexClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dismiss();

            if (v.getId() == R.id.tv_first_item) {
                if (clickFirstItemListener != null) {
                    clickFirstItemListener.clickFirstItem(firstItemStr);
                }
                return;
            }

            if (v.getId() == R.id.tv_second_item) {
                if (clickSecondItemListener != null) {
                    clickSecondItemListener.clickSecondItem(secondItemStr);
                }
                return;
            }

            if (v.getId() == R.id.btn_cancel) {
                if (clickCancelListener != null) {
                    clickCancelListener.clickCancel();
                }
            }
        }
    }
}
