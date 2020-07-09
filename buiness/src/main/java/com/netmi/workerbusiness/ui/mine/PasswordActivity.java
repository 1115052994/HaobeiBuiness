package com.netmi.workerbusiness.ui.mine;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.databinding.ActivityPassowordBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.CHANGE_LOGIN;
import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.CHANGE_TRANSFER;
import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.FORGET_LOGIN;
import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.FORGET_TRANSFER;

public class PasswordActivity extends BaseActivity<ActivityPassowordBinding> {
    private int type;

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_passoword;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        if (type == CHANGE_LOGIN) {
            getTvTitle().setText("修改登录密码");
            mBinding.tvGetCode.setVisibility(View.GONE);
            mBinding.etCode.setHint("请输入原密码");
            mBinding.tvFirstTitle.setText("原密码");
        } else if (type == FORGET_LOGIN) {
            getTvTitle().setText("忘记登录密码");
        } else if (type == CHANGE_TRANSFER) {
            getTvTitle().setText("修改钱包密码");
            mBinding.tvGetCode.setVisibility(View.GONE);
            mBinding.etCode.setHint("请输入原密码");
            mBinding.tvFirstTitle.setText("原密码");
        } else if (type == FORGET_TRANSFER) {
            getTvTitle().setText("忘记钱包密码");
        }
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_get_code) {
            if (type == FORGET_LOGIN) {
                doSendSMS(UserInfoCache.get().getPhone(), "reset");
            } else if (type == FORGET_TRANSFER) {
                doSendSMS(UserInfoCache.get().getPhone(), "payPassword");
            }
        } else if (view.getId() == R.id.tv_confirm) {
            check();
        }
    }

    private void check() {
        if (!mBinding.etPassword.getText().toString().equals(mBinding.etPasswordAgain.getText().toString())) {
            showError("两次输入的密码不一样，请重新输入");
        } else if (type == FORGET_LOGIN) {  //忘记登录
            forgetLogin(MD5.GetMD5Code(mBinding.etPassword.getText().toString()), mBinding.etCode.getText().toString(), UserInfoCache.get().getPhone());
        } else if (type == FORGET_TRANSFER) {  //忘记交易
            forgetTransfer(MD5.GetMD5Code(mBinding.etPassword.getText().toString()), mBinding.etCode.getText().toString(), UserInfoCache.get().getPhone());
        } else if (type == CHANGE_LOGIN) {  //修改登录
            changeLogin(MD5.GetMD5Code(mBinding.etPassword.getText().toString()), MD5.GetMD5Code(mBinding.etCode.getText().toString()));
        } else if (type == CHANGE_TRANSFER) {  //修改交易
            changeTransfer(MD5.GetMD5Code(mBinding.etPassword.getText().toString()), MD5.GetMD5Code(mBinding.etCode.getText().toString()));
        }
    }


    //忘记登录
    private void forgetLogin(String password, String code, String phone) {
        RetrofitApiFactory.createApi(MineApi.class)
                .forgetLogin(password, code, phone)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        finish();
                    }
                });
    }


    //忘记交易
    private void forgetTransfer(String password, String code, String phone) {
        RetrofitApiFactory.createApi(MineApi.class)
                .forgetTransfer(phone, code, password)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        finish();
                    }
                });
    }

    //修改登录
    private void changeLogin(String password, String oldpass) {
        RetrofitApiFactory.createApi(MineApi.class)
                .changeLogin(password, oldpass)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        finish();
                    }
                });
    }

    //修改交易
    private void changeTransfer(String password, String oldpass) {
        RetrofitApiFactory.createApi(MineApi.class)
                .changeTransfer(password, oldpass)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        finish();
                    }
                });
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
}
