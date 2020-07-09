package com.liemi.basemall.ui.personal.digitalasset;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.liemi.basemall.R;


import com.liemi.basemall.databinding.DialogInputPasswordTakeOutWithoutCodeBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;

/*
 * 提取文旅数字通行证输入密码
 * */
public class PropertyTakeOutInputPasswordDialogWithoutCode extends Dialog {
    private DialogInputPasswordTakeOutWithoutCodeBinding binding;
    private ClickNextStepListener clickNextStepListener;
    private boolean isPhone;
    private String account;
    private boolean haveCode;
    private String id;

    public PropertyTakeOutInputPasswordDialogWithoutCode(@NonNull Context context, boolean haveCode, String id) {
        super(context, com.netmi.baselibrary.R.style.showDialog);
        this.haveCode = haveCode;
        this.id = id;
    }

//    public PropertyTakeOutInputPasswordDialogWithoutCode(@NonNull Context context, int themeResId) {
//        super(context, themeResId);
//    }
//
//    protected PropertyTakeOutInputPasswordDialogWithoutCode(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }

    public void setClickNextStepListener(ClickNextStepListener clickNextStepListener) {
        this.clickNextStepListener = clickNextStepListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        isPhone = Strings.isPhone(LoginInfoCache.get().getLogin());
        isPhone = TextUtils.isEmpty(UserInfoCache.get().getPhone());
        account = LoginInfoCache.get().getLogin();

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_input_password_take_out_without_code, null, false);
        setContentView(binding.getRoot());
        //
        binding.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.llCode.setVisibility(haveCode ? View.VISIBLE : View.GONE);
        binding.tvNextStep1.setVisibility(haveCode ? View.GONE : View.VISIBLE);
        //变色
        binding.etInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (haveCode) {
                    if (charSequence.toString().length() >= 6 && (binding.etAuthCode.getText().toString().length() >= 4)) {
                        binding.tvNextStep2.setTextColor(Color.parseColor("#108EE9"));
                    } else {
                        binding.tvNextStep2.setTextColor(Color.parseColor("#999999"));
                    }
                } else {
                    if (charSequence.toString().length() >= 6) {
                        binding.tvNextStep1.setTextColor(Color.parseColor("#108EE9"));
                    } else {
                        binding.tvNextStep1.setTextColor(Color.parseColor("#999999"));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (haveCode) {
            binding.etAuthCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().length() >= 4 && binding.etInputPassword.getText().toString().length() >= 6) {
                        binding.tvNextStep2.setTextColor(Color.parseColor("#108EE9"));
                    } else {
                        binding.tvNextStep2.setTextColor(Color.parseColor("#999999"));
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }


        binding.tvNextStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.etInputPassword.getText().toString())) {
                    ToastUtils.showShort(ResourceUtil.getString(R.string.modlogin_please_input_password));
                    return;
                }
                if (TextUtils.isEmpty(binding.etAuthCode.getText().toString())) {
                    ToastUtils.showShort(ResourceUtil.getString(R.string.modlogin_please_input_code));
                    return;
                }
                if (clickNextStepListener != null) {
                    clickNextStepListener.clickNextStep(binding.etInputPassword.getText().toString(), binding.etAuthCode.getText().toString());
                }
            }
        });
        binding.tvNextStep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.etInputPassword.getText().toString())) {
                    ToastUtils.showShort(ResourceUtil.getString(R.string.modlogin_please_input_password));
                    return;
                }
                if (clickNextStepListener != null) {
                    clickNextStepListener.clickNextStep(binding.etInputPassword.getText().toString(), null);
                }
            }
        });
        binding.tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAuthCode();
            }

        });
    }

    private void getAuthCode() {
        if (!isSend)
            return;
        if (!isPhone && Strings.isPhone(account))
            doSendSMS(account);
        else if (isPhone && Strings.isEmail(account))
            doSendSMS(account);
        else
            ToastUtils.showShort(isPhone ? R.string.please_input_right_phone : R.string.mall_please_input_right_email);
    }

    private boolean isSend = true;
    private android.os.CountDownTimer CountDownTimer;

    public void sendSMSOk() {
        isSend = false;
        CountDownTimer = new CountDownTimer(1000 * 60, 1000) {  // 倒计时 60 秒后可以重新获取验证码
            @Override
            public void onTick(long millisUntilFinished) {
                if (isSend) {
                    cancel();
                } else if (binding.tvGetCode != null) {
                    String s = (millisUntilFinished / 1000) + getContext().getString(R.string.pickerview_seconds);
                    binding.tvGetCode.setText(s);
                }
            }

            @Override
            public void onFinish() {
                if (isSend || binding.tvGetCode == null)
                    return;
                isSend = true;
                binding.tvGetCode.setEnabled(isSend);
                binding.tvGetCode.setText(R.string.modlogin_get_code);
            }
        }.start();

        binding.tvGetCode.setEnabled(isSend);

    }

    //请求验证码
    private void doSendSMS(String phone) {
        isSend = true;
        if (MApplication.getInstance().appManager.currentActivity() instanceof BaseActivity) {
            ((BaseActivity) MApplication.getInstance().appManager.currentActivity()).showProgress("");
        }
//        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .getSmsCode(phone, isPhone ? "email" : "phone", Constant.AUTH_CODE_PAY)
//                .compose(this.<BaseData>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    public void onSuccess(BaseData baseData) {
                        sendSMSOk();
//                        ToastUtils.showShort(R.string.obtain_checkcode_success);
                    }

                    @Override
                    public void onFail(BaseData data) {
                        super.onFail(data);
                        isSend = false;
                    }

                    @Override
                    public void onComplete() {
                        if (MApplication.getInstance().appManager.currentActivity() instanceof BaseActivity) {
                            ((BaseActivity) MApplication.getInstance().appManager.currentActivity()).hideProgress();
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ToastUtils.showShort(ex.getMessage());
                        isSend = false;
                        if (MApplication.getInstance().appManager.currentActivity() instanceof BaseActivity) {
                            ((BaseActivity) MApplication.getInstance().appManager.currentActivity()).hideProgress();
                        }
                    }
                });
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
    }

    //用户点击下一步的接口
    public interface ClickNextStepListener {
        void clickNextStep(String password, String code);
    }
}
