package com.liemi.basemall.ui.personal.digitalasset;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.DialogPropertyTakeOutConfirmBinding;
import com.netmi.baselibrary.utils.FloatUtils;

/*
 * 资产确认提取的dialog
 * */
public class PropertyTakeOutConfirmDialog extends Dialog {

    private DialogPropertyTakeOutConfirmBinding binding;
    private ClickConfirmListener clickConfirmListener;
    private String mTakeOutNum;
    private String mHandlingNum;
    private String realNum;


    public PropertyTakeOutConfirmDialog(@NonNull Context context) {
        this(context, com.netmi.baselibrary.R.style.showDialog);
    }

    public PropertyTakeOutConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PropertyTakeOutConfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public void setClickConfirmListener(ClickConfirmListener clickConfirmListener) {
        this.clickConfirmListener = clickConfirmListener;
    }

    public void setPropertyNum(String takeOutNum, String handlingNum, String realNum) {
        this.mTakeOutNum = takeOutNum;
        this.mHandlingNum = handlingNum;
        this.realNum = realNum;
        if (binding != null) {
            binding.setTakeOutNum(mTakeOutNum);
            binding.setHandlingChargeNum(mHandlingNum);
            binding.setFinishNum(realNum);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_property_take_out_confirm, null, false);
        binding.setTakeOutNum(mTakeOutNum);
        binding.setHandlingChargeNum(mHandlingNum);
        binding.setFinishNum(realNum);
        setContentView(binding.getRoot());
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickConfirmListener != null) {
                    clickConfirmListener.clickConfirm();
                }
            }
        });

        binding.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //显示在页面底部
    public void showBottom() {
        this.show();
        Window mDialogWindow = getWindow();
        if (mDialogWindow != null) {
            WindowManager.LayoutParams params = mDialogWindow.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.BOTTOM;
            mDialogWindow.setAttributes(params);
            mDialogWindow.setWindowAnimations(com.netmi.baselibrary.R.style.dialog_bottom_anim_style);
            mDialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        }
    }


    //用户点击确认的接口
    public interface ClickConfirmListener {
        void clickConfirm();
    }
}
