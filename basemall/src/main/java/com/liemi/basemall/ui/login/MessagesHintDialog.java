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
import com.liemi.basemall.databinding.DialogMessageHintBinding;

/**
 * 类描述：提醒用户绑定手机号的dialog
 * 创建人：张一凡
 * 创建时间：2018/9/25 16:03
 * 修改备注：
 */
public class MessagesHintDialog extends Dialog implements View.OnClickListener {

    //是否直接拦截关闭
    public boolean isIntercept;
    private ClickBindPhoneListener clickBindMessageListener;
    private String message="数据异常";

    public MessagesHintDialog(@NonNull Context context,String message) {
        super(context);
        this.message=message;
    }

    public MessagesHintDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MessagesHintDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setClickBindMessageListener(ClickBindPhoneListener clickBindPhoneListener) {
        this.clickBindMessageListener = clickBindPhoneListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogMessageHintBinding dialogBindMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_message_hint, null, false);
        setContentView(dialogBindMessageBinding.getRoot());
        dialogBindMessageBinding.textLMessage.setText(message);
        dialogBindMessageBinding.setClick(this);
    }


    @Override
    public void onClick(View v) {
        if(isIntercept){
            dismiss();
            return;
        }
        if (clickBindMessageListener != null) {
            if (v.getId() == R.id.text_i_can) {
                //取消按钮
                clickBindMessageListener.clickBindMessageCan();
            }else if (v.getId() == R.id.text_i_con) {
                //确定按钮
                clickBindMessageListener.clickBindMessageCon();
            }
        }
    }


    //用户点击绑定手机号的接口
    public interface ClickBindPhoneListener {
        void clickBindMessageCan();
        void clickBindMessageCon();
    }
}
