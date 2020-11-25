package com.netmi.workerbusiness.ui.home.haibei;

import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityHaiBeiShowResultBinding;
import com.netmi.workerbusiness.ui.mine.WalletActivity;

public class HaiBeiShowResultActivity extends BaseActivity<ActivityHaiBeiShowResultBinding> {


    @Override
    protected int getContentView() {
        return R.layout.activity_hai_bei_show_result;
    }

    @Override
    protected void initUI() {



    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        JumpUtil.overlay(this, WalletActivity.class);

    }


}
