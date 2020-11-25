package com.netmi.workerbusiness.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.DialogPayMessageBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 类描述：提醒用户绑定手机号的dialog
 * 创建人：张一凡
 * 创建时间：2018/9/25 16:03
 * 修改备注：
 */
public class Pay_Message_Dialog extends Dialog implements View.OnClickListener {

    private ClickBindPhoneListener clickBindMessageListener;
    private DialogPayMessageBinding dialogPayMessageBinding;
    private int type;

    public Pay_Message_Dialog(@NonNull Context context) {
        super(context);
    }

    public Pay_Message_Dialog(@NonNull Context context, int type) {
        super(context);
        this.type=type;
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
        dialogPayMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_pay_message, null, false);
        setContentView(dialogPayMessageBinding.getRoot());
        dialogPayMessageBinding.setClick(this);

        dialogPayMessageBinding.etPayPhone.setHint(type==30?"请输入支付宝账号":"请输入微信账号");
        setEditTextInhibitInputSpeChat(dialogPayMessageBinding.etPayPhone);

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
                String name = dialogPayMessageBinding.etPayName.getText().toString();
                String phone = dialogPayMessageBinding.etPayPhone.getText().toString();
                //确定按钮
                if(TextUtils.isEmpty(name)){
                    ToastUtils.showLong("请输入姓名");
                    return;
                }else if(TextUtils.isEmpty(phone)){
                    ToastUtils.showLong("请输入账号");
                    return;
                }else if(phone.length()<11){
                    ToastUtils.showLong("请输入11位的手机号");
                    return;
                }
                clickBindMessageListener.clickBindMessageCon(name,phone);
            }

        }
    }


    //确定和取消事件
    public interface ClickBindPhoneListener {
        void clickBindMessageCan();
        void clickBindMessageCon(String name,String phone);
    }

    /**
     * 禁止EditText输入特殊字符
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(" "))return "";
                if(dest.toString().contains(".")){
                    if(source.toString().equals(".")){
                        return "";
                    }
                }
                if(dest.toString().contains("@")){
                    if(source.toString().equals("@")){
                        return "";
                    }
                }
                String speChat="[`~!#$%^&*()+=|{}':;',\\\\[\\\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“'。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(matcher.find())return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

}
