package com.netmi.workerbusiness.ui.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityAccountSecurityBinding;

public class AccountSecurityActivity extends BaseActivity<ActivityAccountSecurityBinding> {

    public static final int LOGIN_PASS = 1001;
    public static final int TRANSFER_PASS = 1002;
    public static final int CHANGE_LOGIN = 2001;
    public static final int FORGET_LOGIN = 2002;
    public static final int CHANGE_TRANSFER = 2003;
    public static final int FORGET_TRANSFER = 2004;


    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_account_security;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("账号安全");
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
        Bundle args = new Bundle();
        if (view.getId() == R.id.tv_login) {   //登录密码
            args.putInt(JumpUtil.TYPE, LOGIN_PASS);
            JumpUtil.overlay(getContext(), PassChooseActivity.class, args);
        } else if (view.getId() == R.id.tv_transfer) { //交易密码
            args.putInt(JumpUtil.TYPE, TRANSFER_PASS);
            JumpUtil.overlay(getContext(), PassChooseActivity.class, args);
        }
    }
}
