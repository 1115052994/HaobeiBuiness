package com.netmi.baselibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.netmi.baselibrary.R;
import com.netmi.baselibrary.utils.Densitys;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/19 17:05
 * 修改备注：
 */
public class ConfirmDialog extends Dialog {

    public ConfirmDialog(Context context) {
        super(context, R.style.transparentDialog);
        initUI();
    }

    public ConfirmDialog(Context context, int themeResId) {
        super(context, themeResId);
        initUI();
    }

    private TextView tvContent;
    private TextView tvCancel;
    private TextView tvConfirm;

    /**
     * 对话框布局初始化
     */
    private void initUI() {
        setContentView(R.layout.baselib_dialog_confirm);
        tvContent = findViewById(R.id.tv_content);
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (cancelListener != null) {
                    cancelListener.onClick(view);
                }
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (confirmListener != null) {
                    confirmListener.onClick(v);
                }
            }
        });

        setDialogPosition();
    }

    private void setDialogPosition() {
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.y = -Densitys.dp2px(getContext(), 80);
            dialogWindow.setAttributes(lp);
        }
    }

    public ConfirmDialog setContentText(String content) {
        tvContent.setText(content);
        return this;
    }

    public ConfirmDialog setCancelText(String content) {
        tvCancel.setText(content);
        return this;
    }

    public ConfirmDialog setConfirmText(String content) {
        tvConfirm.setText(content);
        return this;
    }

    private View.OnClickListener confirmListener;
    private View.OnClickListener cancelListener;

    public ConfirmDialog setConfirmListener(View.OnClickListener confirmListener) {
        this.confirmListener = confirmListener;
        return this;
    }

    public ConfirmDialog setCancelListener(View.OnClickListener cancelListener) {
        this.cancelListener = cancelListener;
        return this;
    }
}
