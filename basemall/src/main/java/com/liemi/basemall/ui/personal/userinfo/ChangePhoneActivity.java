package com.liemi.basemall.ui.personal.userinfo;

import android.content.Intent;
import android.databinding.adapters.TextViewBindingAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.LoginApi;
import com.liemi.basemall.databinding.ActivityChangePhoneBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class ChangePhoneActivity extends BaseActivity<ActivityChangePhoneBinding> implements TextViewBindingAdapter.AfterTextChanged {
    //是否发送验证码
    private boolean mSendCode = true;
    //倒计时
    private CountDownTimer mCountDownTimer;
    @Override
    protected int getContentView() {
        return R.layout.activity_change_phone;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("修改手机号");
        mBinding.setTextAfter(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if(view.getId() == R.id.tv_get_verification_code){
            if(TextUtils.isEmpty(mBinding.etInputNewPhone.getText().toString())){
                showError("请输入手机号");
                return;
            }
            if(Strings.isPhone(mBinding.etInputNewPhone.getText().toString())){
                doVerificationCode();
            }else{
                showError("请输入正确的手机号");
            }
            return;
        }
        if(view.getId() == R.id.btn_save){
            if(TextUtils.isEmpty(mBinding.etInputNewPhone.getText().toString())){
                showError("请输入手机号");
                return;
            }
            if(!Strings.isPhone(mBinding.etInputNewPhone.getText().toString())){
                showError("请输入正确的手机号");
                return;
            }
            if(TextUtils.isEmpty(mBinding.etInputVerificationCode.getText().toString())){
                showError("请输入验证码");
                return;
            }
            doChangePhone();
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        Logs.i("s:"+s.toString());
        if(mSendCode &&
                !TextUtils.isEmpty(mBinding.etInputNewPhone.getText().toString()) &&
                !TextUtils.isEmpty(mBinding.etInputVerificationCode.getText().toString())){
            mBinding.btnSave.setEnabled(true);
        }else{
            mBinding.btnSave.setEnabled(false)  ;
        }
    }

    //开启倒计时
    private void startTimer(){
        mBinding.tvGetVerificationCode.setEnabled(false);
        mCountDownTimer = new CountDownTimer(Constant.COUNT_DOWN_TIME_DEFAULT,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.tvGetVerificationCode.setText(millisUntilFinished / 1000 + "秒后重新获取");
            }

            @Override
            public void onFinish() {
                mBinding.tvGetVerificationCode.setEnabled(true);
            }
        };
        mCountDownTimer.start();
    }

    //获取验证码
    //请求验证码
    private void doVerificationCode() {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doAuthCode(mBinding.etInputNewPhone.getText().toString(), "changePhone")
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError(data.getErrmsg());
                        mSendCode = true;
                        startTimer();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });
    }

    private void doChangePhone(){
        RetrofitApiFactory.createApi(LoginApi.class)
                .doChangePhone(mBinding.etInputNewPhone.getText().toString(),
                        mBinding.etInputVerificationCode.getText().toString())
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("更换手机成功");
                        Intent intent = new Intent();
                        intent.putExtra("isChangeSuccess", true);
                        setResult(10001, intent);
                        finish();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    protected void onError(ApiException ex) {

                    }
                });
    }
}
