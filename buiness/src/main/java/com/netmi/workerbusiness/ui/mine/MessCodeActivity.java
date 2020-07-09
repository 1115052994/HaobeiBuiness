package com.netmi.workerbusiness.ui.mine;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.jyn.vcview.VerificationCodeView;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.BankListEntity;
import com.netmi.workerbusiness.databinding.ActivityMessCodeBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

public class MessCodeActivity extends BaseActivity<ActivityMessCodeBinding> {
    private String id;
    private String amount;
    private String phone;
    private String code;

    @Override
    protected int getContentView() {
        return R.layout.activity_mess_code;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("立即提现");
        id = getIntent().getExtras().getString(JumpUtil.TYPE);
        amount = getIntent().getExtras().getString(JumpUtil.VALUE);
        phone = UserInfoCache.get().getPhone();
        mBinding.tvPhone.setText(phone);
        doSendSMS(phone, "applyCash");
        mBinding.vcCode.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onTextChange(View view, String content) {

            }

            @Override
            public void onComplete(View view, String content) {
                code = content;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {

        super.doClick(view);
        int viewId = view.getId();
        if (viewId == R.id.tv_confirm) {
            withdraw(code);
        } else if (viewId == R.id.tv_get_code) {
            doSendSMS(phone, "applyCash");
        }
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
                    String s = (millisUntilFinished / 1000) + getString(R.string.pickerview_seconds_more);
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

    private void withdraw(String code) {
        RetrofitApiFactory.createApi(MineApi.class)
                .withdraw(id, amount, phone, code)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        AppManager.getInstance().finishActivity(WithdrawActivity.class);
                        AppManager.getInstance().finishActivity(MessCodeActivity.class);
                    }
                });
    }

}
