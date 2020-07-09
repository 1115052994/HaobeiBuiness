package com.netmi.workerbusiness.ui.mine.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.databinding.ActivitySetPayPassBinding;
import com.netmi.workerbusiness.ui.mine.WalletActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class SetPayPassActivity extends BaseActivity<ActivitySetPayPassBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_set_pay_pass;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("设置交易密码");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_confirm) {
            check();
        }
    }

    private void check() {
        if (mBinding.etPassword.getText().toString().isEmpty()) {
            showError("请输入交易密码");
        } else if (mBinding.etPasswordAgain.getText().toString().isEmpty()) {
            showError("请确认交易密码");
        } else if (!TextUtils.equals(mBinding.etPassword.getText().toString(), mBinding.etPasswordAgain.getText().toString())) {
            showError("两次输入的密码不相同，请再次输入");
        } else {
            setPayPass(mBinding.etPassword.getText().toString());
        }
    }

    //忘记交易
    private void setPayPass(String password) {
        RetrofitApiFactory.createApi(MineApi.class)
                .setPayPass(MD5.GetMD5Code(password))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        if (getIntent() != null && getIntent().getExtras() != null) {
                            JumpUtil.overlay(getContext(), WalletActivity.class);
                        }
                        finish();
                    }
                });
    }
}
