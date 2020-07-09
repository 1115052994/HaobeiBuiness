package com.liemi.basemall.ui.personal.digitalasset;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.DialogInputPasswordTakeOutBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.utils.ToastUtils;

/*
 * 提取文旅数字通行证输入密码
 * */
public class PropertyTakeOutInputPasswordDialog extends Dialog {

    private DialogInputPasswordTakeOutBinding binding;
    private ClickNextStepListener clickNextStepListener;
    private ClickVerifyCodeListener clickVerifyCodeListener;

    private CountDownTimer mCountDownTimer;
    //记录用户是否获取了验证码
    private boolean mGetAuthCode = false;

    public String code;
    public String password;


    public PropertyTakeOutInputPasswordDialog(@NonNull Context context) {
        this(context, com.netmi.baselibrary.R.style.showDialog);
    }

    public PropertyTakeOutInputPasswordDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PropertyTakeOutInputPasswordDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setClickNextStepListener(ClickNextStepListener clickNextStepListener) {
        this.clickNextStepListener = clickNextStepListener;
    }

    public void setClickVerifyCodeListener(ClickVerifyCodeListener clickVerifyCodeListener) {
        this.clickVerifyCodeListener = clickVerifyCodeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_input_password_take_out, null, false);
        setContentView(binding.getRoot());
        code = binding.etVerifyCode.getText().toString();
        password = binding.etInputPassword.getText().toString();
        binding.etInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() >= 6) {
                    binding.tvNextStep.setTextColor(Color.parseColor("#108EE9"));
                } else {
                    binding.tvNextStep.setTextColor(Color.parseColor("#999999"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.tvNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.etInputPassword.getText().toString())) {
                    ToastUtils.showShort("请输入交易密码");

//                } else if (TextUtils.isEmpty(binding.etVerifyCode.getText().toString())) {
//                    ToastUtils.showShort("请输入验证码");
                } else {
                    if (clickNextStepListener != null) {
                        clickNextStepListener.clickNextStep(binding.etInputPassword.getText().toString(), binding.etVerifyCode.getText().toString());
                    }
                }
            }
        });
        binding.tvVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickVerifyCodeListener != null) {
                    startCountDownTimer();
                    clickVerifyCodeListener.clickVerifyCode();
                }
            }
        });
    }

    //开始倒计时
    private void startCountDownTimer() {
        binding.tvVerify.setEnabled(false);
        mCountDownTimer = new CountDownTimer(Constant.COUNT_DOWN_TIME_DEFAULT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvVerify.setText(millisUntilFinished / 1000 + "秒后重新获取");
            }

            @Override
            public void onFinish() {
                binding.tvVerify.setEnabled(true);
                binding.tvVerify.setText("重新获取验证码");
            }
        };
        mCountDownTimer.start();
    }


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

    @Override
    public void dismiss() {
        super.dismiss();
        binding.etInputPassword.setText("");
        binding.etVerifyCode.setText("");
    }

    //用户点击下一步的接口
    public interface ClickNextStepListener {
        void clickNextStep(String password, String verify_code);
    }

    //点击获取验证码的接口
    public interface ClickVerifyCodeListener {
        void clickVerifyCode();
    }
}
