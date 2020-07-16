package com.netmi.workerbusiness.ui.home.haibei;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityHaiBeiExchangeBinding;

public class HaiBeiExchangeActivity extends BaseActivity<ActivityHaiBeiExchangeBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_hai_bei_exchange;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getIntent().getStringExtra("海贝兑换"));
    }

    @Override
    protected void initData() {

    }
}
