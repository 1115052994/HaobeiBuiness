package com.netmi.workerbusiness.ui.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityPaymentBinding;

import cn.iwgang.countdownview.CountdownView;

public class PaymentActivity extends BaseActivity<ActivityPaymentBinding> {


    @Override
    protected int getContentView() {
        return R.layout.activity_payment;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("确认支付");
    }

    @Override
    protected void initData() {
        CountdownView mCvCountdownView = mBinding.tvTimeLimit;
        mCvCountdownView.start(500000); // Millisecond
        mCvCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                finish();
            }
        });
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.cb_wechat) {
            showError("微信");
            mBinding.wechat.setChecked(true);
            mBinding.alipay.setChecked(false);
            mBinding.bank.setChecked(false);
        } else if (id == R.id.cb_alipay) {
            showError("支付宝");
            mBinding.wechat.setChecked(false);
            mBinding.alipay.setChecked(true);
            mBinding.bank.setChecked(false);
        } else if (id == R.id.cb_bank) {
            showError("银行卡");
            mBinding.wechat.setChecked(false);
            mBinding.alipay.setChecked(false);
            mBinding.bank.setChecked(true);
        }
    }
}
