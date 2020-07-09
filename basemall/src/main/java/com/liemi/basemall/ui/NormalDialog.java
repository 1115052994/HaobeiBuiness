package com.liemi.basemall.ui;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.DialogNormalBinding;

public class NormalDialog extends Dialog implements View.OnClickListener {

    private DialogNormalBinding normalBinding;

    private ClickCancelListener clickCancelListener;
    private ClickConfirmListener clickConfirmListener;

    public NormalDialog(@NonNull Context context) {
        this(context, com.netmi.baselibrary.R.style.showDialog);
    }

    public NormalDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected NormalDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    //设置title信息
    public void setTitleInfo(String title, boolean showTitle) {
        setTitleInfo(title, showTitle, false);
    }

    //title是否加粗
    public void setTitleInfo(String title, boolean showTitle, boolean bold) {
        if (normalBinding != null) {
            if (showTitle) {
                normalBinding.setIsShowTitle(showTitle);
                normalBinding.setTitle(title);
            } else {
                normalBinding.setIsShowTitle(false);
            }
            if (bold) {
                normalBinding.tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
            }
        }
    }

    //是否显示分割线
    public void showLineTitleWithMessage(boolean isShow) {
        if (normalBinding != null) {
            if (isShow) {
                normalBinding.viewLineTitle.setVisibility(View.VISIBLE);
            } else {
                normalBinding.viewLineTitle.setVisibility(View.GONE);
            }
        }
    }


    //设置message信息
    public void setMessageInfo(String message, boolean showMessage) {
        setMessageInfo(message, showMessage, 18, false);
    }

    public void setMessageInfo(String message, boolean showMessage, boolean bold) {
        setMessageInfo(message, showMessage, 18, bold);
    }

    //设置Message信息
    public void setMessageInfo(String message, boolean showMessage, float size, boolean bold) {
        if (normalBinding != null) {
            if (showMessage) {
                normalBinding.setIsShowMessage(true);
                normalBinding.setMessage(message);
                normalBinding.tvMessage.setTextSize(Dimension.SP, size);
                if (bold) {
                    //字体加粗
                    normalBinding.tvMessage.setTypeface(Typeface.DEFAULT_BOLD);
                }
            } else {
                normalBinding.setIsShowMessage(false);
            }
        }
    }

    //设置取消按钮
    public void setCancelInfo(String cancelStr, boolean showCancel) {
        if (normalBinding != null) {
            if (showCancel) {
                normalBinding.setIsShowCancel(true);
                normalBinding.setCancelStr(cancelStr);
            } else {
                normalBinding.setIsShowCancel(false);
            }
        }
    }

    //设置确认按钮
    public void setConfirmInfo(String confirmStr) {
        if (normalBinding != null) {
            normalBinding.setConfirmStr(confirmStr);
        }
    }

    //设置接口
    public void setClickCancelListener(ClickCancelListener clickCancelListener) {
        this.clickCancelListener = clickCancelListener;
    }

    public void setClickConfirmListener(ClickConfirmListener clickConfirmListener) {
        this.clickConfirmListener = clickConfirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        normalBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_normal, null, false);
        setContentView(normalBinding.getRoot());
        normalBinding.setClick(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tv_cancel) {
            if (clickCancelListener != null) {
                clickCancelListener.clickCancel();
            } else {
                dismiss();
            }
            return;
        }

        if (v.getId() == R.id.tv_confirm) {
            if (clickConfirmListener != null) {
                clickConfirmListener.clickConfirm();
            } else {
                dismiss();
            }
            return;
        }


    }


    //点击取消的接口
    public interface ClickCancelListener {
        void clickCancel();
    }

    //点击确认的接口
    public interface ClickConfirmListener {
        void clickConfirm();
    }

    @Override
    public void show() {
        super.show();
    }
}
