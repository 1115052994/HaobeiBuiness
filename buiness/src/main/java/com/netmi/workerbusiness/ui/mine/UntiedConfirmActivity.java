package com.netmi.workerbusiness.ui.mine;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.BankListEntity;
import com.netmi.workerbusiness.databinding.ActivityUntiedConfirmBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class UntiedConfirmActivity extends BaseActivity<ActivityUntiedConfirmBinding> {
    private BankListEntity entity;
    private String phone;

    @Override
    protected int getContentView() {
        return R.layout.activity_untied_confirm;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("解绑验证");
        new InputListenView(mBinding.tvConfirm, mBinding.etPhone, mBinding.etCode) {

        };
        entity = (BankListEntity) getIntent().getExtras().getSerializable(JumpUtil.VALUE);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        phone = mBinding.etPhone.getText().toString();
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_get_code) {
            if (TextUtils.isEmpty(phone)) {
                showError("请输入银行卡预留手机号");
            } else if (!Strings.isPhone(phone)) {
                showError("请输入正确的手机号");
            } else {
                doSendSMS(phone, "bindBank");
            }
        } else if (id == R.id.tv_confirm) {
            delBank(String.valueOf(entity.getId()), phone, mBinding.etCode.getText().toString());
        }
    }

    //请求验证码
    private void doSendSMS(String phone, String type) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .getSmsCode(phone, type)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new XObserver<BaseData>() {
                    public void onSuccess(BaseData baseData) {
                        sendSMSOk();
                        showError(baseData.getErrmsg());
                    }

                    @Override
                    public void onFail(BaseData data) {
                        super.onFail(data);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                    }
                });
    }

    private boolean isSend = true;

    public void sendSMSOk() {
        isSend = false;
        new CountDownTimer(1000 * 60, 1000) {  // 倒计时 60 秒后可以重新获取验证码
            @Override
            public void onTick(long millisUntilFinished) {
                if (isSend) {
                    cancel();
                } else if (mBinding.tvGetCode != null) {
                    String s = (millisUntilFinished / 1000) + getString(R.string.pickerview_seconds);
                    mBinding.tvGetCode.setText(s);
                }
            }

            @Override
            public void onFinish() {
                if (isSend || mBinding.tvGetCode == null)
                    return;
                isSend = true;
                mBinding.tvGetCode.setEnabled(isSend);
                mBinding.tvGetCode.setText(R.string.obtain_check_code);
            }
        }.start();
        mBinding.tvGetCode.setEnabled(isSend);
    }


    //解除绑定
    private void delBank(String id, String tel, String code) {
        RetrofitApiFactory.createApi(MineApi.class)
                .delete_bank(id, tel, code)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        finish();
                    }
                });
    }
}
