package com.liemi.basemall.ui.login;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import com.liemi.basemall.R;
import com.liemi.basemall.databinding.DialogBindPhoneBinding;
/**
 * 类描述：提醒用户绑定手机号的dialog
 * 创建人：张一凡
 * 创建时间：2018/9/25 16:03
 * 修改备注：
 */
public class BindPhoneDialog extends Dialog implements View.OnClickListener {

    private ClickBindPhoneListener clickBindPhoneListener;

    public BindPhoneDialog(@NonNull Context context) {
        super(context);
    }

    public BindPhoneDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BindPhoneDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setClickBindPhoneListener(ClickBindPhoneListener clickBindPhoneListener) {
        this.clickBindPhoneListener = clickBindPhoneListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogBindPhoneBinding dialogBindPhoneBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_bind_phone, null, false);
        setContentView(dialogBindPhoneBinding.getRoot());
        dialogBindPhoneBinding.setClick(this);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_i_know) {
            //跳转到绑定手机号页面
            if (clickBindPhoneListener != null) {
                clickBindPhoneListener.clickBindPhone();
            }
        }

    }


    //用户点击绑定手机号的接口
    public interface ClickBindPhoneListener {
        void clickBindPhone();
    }
}
