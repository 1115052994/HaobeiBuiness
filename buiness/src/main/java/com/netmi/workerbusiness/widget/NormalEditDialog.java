package com.netmi.workerbusiness.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.netmi.baselibrary.utils.Densitys;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.DialogNormalEditBinding;

/**
 * 类描述：
 * 创建人：Sherlock
 * 创建时间：2019/4/22
 * 修改备注：
 */
public class NormalEditDialog extends Dialog {
    //标题
    private String title;
    //副标题
    private String subTitle;
    //编辑框
    private String content;
    //取消按钮文字
    private String cancel;
    //确定按钮文字
    private String confirm;
    //编辑框提示文字
    private String hint;
    //是否需要编辑框
    private boolean isNeedEditText = true;

    private ClickCancelListener mClickCancelListener;
    private ClickConfirmListener mClickConfirmListener;

    private DialogNormalEditBinding mBinding;
    private boolean isPassword = false;
    private boolean isNumber = false;

    public NormalEditDialog(@NonNull Context context) {
        super(context, com.netmi.baselibrary.R.style.showDialog);
    }

    public NormalEditDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected NormalEditDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public NormalEditDialog isNeedEditText(boolean isNeedEditText) {
        this.isNeedEditText = isNeedEditText;
        return this;
    }

    public NormalEditDialog isPassword(boolean isPassword) {
        this.isPassword = isPassword;
        return this;
    }

    public NormalEditDialog isNumber(boolean isNumber) {
        this.isNumber = isNumber;
        return this;
    }

    public NormalEditDialog text(@NonNull String title, String subTitle, String hint, String confirm, String cancel) {

        if (TextUtils.isEmpty(confirm)) {
            this.confirm = getContext().getString(com.netmi.baselibrary.R.string.confirm);
        } else {
            this.confirm = confirm;
        }
        if (TextUtils.isEmpty(cancel)) {
            this.cancel = getContext().getString(com.netmi.baselibrary.R.string.cancel);
        } else {
            this.cancel = cancel;
        }
        if (TextUtils.isEmpty(hint)) {
            this.hint = getContext().getString(com.netmi.baselibrary.R.string.please_input);
        } else {
            this.hint = hint;
        }
        this.subTitle = subTitle;
        this.title = title;
        return this;
    }

    //设置点击取消接口
    public NormalEditDialog setClickCancelListener(ClickCancelListener listener) {
        this.mClickCancelListener = listener;
        return this;
    }

    //设置点击确认接口
    public NormalEditDialog setClickConfirmListener(ClickConfirmListener listener) {
        this.mClickConfirmListener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_normal_edit, null, false);
        setContentView(mBinding.getRoot());
//        mBinding.setCancel(getContext().getString(cancel));
        if (TextUtils.isEmpty(subTitle)) {
            mBinding.tvContent.setVisibility(View.GONE);
        }
        if (isNumber && isPassword) {
//            mBinding.etContent.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        } else if (isPassword)
            mBinding.etContent.setInputType(129);
        else if (isNumber)
            mBinding.etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
        else
            mBinding.etContent.setInputType(InputType.TYPE_CLASS_TEXT);
        mBinding.etContent.setVisibility(isNeedEditText ? View.VISIBLE : View.GONE);

        mBinding.setCancel(cancel);
        mBinding.setConfirm(confirm);
        mBinding.setContent(content);
        mBinding.setSubTitle(subTitle);
        mBinding.setTitle(title);
        mBinding.etContent.setHint(hint);
        mBinding.tvCancel.setOnClickListener(v -> {
            dismiss();
            if (mClickCancelListener != null) {
                mClickCancelListener.clickCancel();
            }
        });
        if (isNeedEditText)
        mBinding.tvConfirm.setOnClickListener(v -> {
            dismiss();
//            ToastUtils.showShort(mBinding.getContent());
            if (mClickConfirmListener != null) {
                mClickConfirmListener.clickConfirm((mBinding.etContent.getText().toString()));
            }
        });
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
//            mDialogWindow.setWindowAnimations(com.netmi.baselibrary.R.style.dialog_bottom_anim_style);
            mDialogWindow.getDecorView().setPadding(Densitys.dp2px(getContext(), 15), Densitys.dp2px(getContext(), 15), Densitys.dp2px(getContext(), 15), Densitys.dp2px(getContext(), 15));
        }
    }

    //dialog显示在中间
    public void showCenter() {
        show();
        //获取当前dialog显示的window
        Window mDialogWindow = getWindow();
        if (mDialogWindow != null) {
            WindowManager.LayoutParams params = mDialogWindow.getAttributes();
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            mDialogWindow.setAttributes(params);
//            mDialogWindow.setWindowAnimations(com.netmi.baselibrary.R.style.dialog_bottom_anim_style);
            mDialogWindow.getDecorView().setPadding(Densitys.dp2px(getContext(), 15), Densitys.dp2px(getContext(), 15), Densitys.dp2px(getContext(), 15), Densitys.dp2px(getContext(), 15));
        }
    }

    @Override
    public void dismiss() {
        try {
            KeyboardUtils.hideKeyboard(mBinding.getRoot());
        } catch (Exception e) {
            Logs.e(e);
        }
        super.dismiss();
    }

    //点击取消的接口
    public interface ClickCancelListener {
        void clickCancel();
    }

    //点击确认的接口
    public interface ClickConfirmListener {
        void clickConfirm(String name);
    }
}
