package com.netmi.workerbusiness.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.DialogAddGroupBinding;


public class AddGroupDialog extends Dialog {

    private ClickCancelListener mClickCancelListener;
    private ClickConfirmListener mClickConfirmListener;
    private String content;

    private DialogAddGroupBinding mBinding;

    public AddGroupDialog(@NonNull Context context) {
        super(context, R.style.showDialog);
    }

    public AddGroupDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AddGroupDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.dialog_add_group,null,false);
        setContentView(mBinding.getRoot());

        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mClickCancelListener != null){
                    mClickCancelListener.clickCancel();
                }
            }
        });

        mBinding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mClickConfirmListener != null){
                    mClickConfirmListener.clickConfirm(mBinding.etGroupName.getText().toString());
                }
            }
        });
    }

    //设置点击取消接口
    public void setClickCancelListener(AddGroupDialog.ClickCancelListener listener){
        this.mClickCancelListener = listener;
    }

    //设置点击确认接口
    public void setClickConfirmListener(AddGroupDialog.ClickConfirmListener listener){
        this.mClickConfirmListener = listener;
    }

    //点击取消的接口
    public interface ClickCancelListener{
        void clickCancel();
    }
    //点击确认的接口
    public interface ClickConfirmListener{
        void clickConfirm(String name);
    }


    public void setEditContent(String content){
        mBinding.etGroupName.setText(content);
    }

}
