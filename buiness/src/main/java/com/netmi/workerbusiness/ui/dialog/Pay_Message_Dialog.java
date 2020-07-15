package com.netmi.workerbusiness.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;


import com.netmi.baselibrary.databinding.DialogMessageHintBinding;
import com.netmi.workerbusiness.R;


/**
 * 类描述：提醒用户绑定手机号的dialog
 * 创建人：张一凡
 * 创建时间：2018/9/25 16:03
 * 修改备注：
 */
public class Pay_Message_Dialog extends Dialog implements View.OnClickListener {

    private ClickBindPhoneListener clickBindMessageListener;
    private String message="数据为空";
    private DialogMessageHintBinding dialogBindMessageBinding;

    public Pay_Message_Dialog(@NonNull Context context, String message) {
        super(context);
        this.message=message;
    }

    public Pay_Message_Dialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected Pay_Message_Dialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setClickBindMessageListener(ClickBindPhoneListener clickBindPhoneListener) {
        this.clickBindMessageListener = clickBindPhoneListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogBindMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_pay_message, null, false);
        setContentView(dialogBindMessageBinding.getRoot());
        showMessage(message);
    }
    public void showMessage(String message){
        dialogBindMessageBinding.textLMessage.setText(message);
        dialogBindMessageBinding.setClick(this);
    }


    @Override
    public void onClick(View v) {
        if(clickBindMessageListener==null){
            dismiss();
            return;
        }else{
            if (v.getId() == R.id.text_i_can) {
                //取消按钮
                clickBindMessageListener.clickBindMessageCan();
            }else if (v.getId() == R.id.text_i_con) {
                //确定按钮
                clickBindMessageListener.clickBindMessageCon();
            }
        }
    }


    //确定和取消事件
    public interface ClickBindPhoneListener {
        void clickBindMessageCan();
        void clickBindMessageCon();
    }
}
