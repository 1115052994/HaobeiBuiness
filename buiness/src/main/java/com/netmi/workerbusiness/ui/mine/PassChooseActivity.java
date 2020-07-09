package com.netmi.workerbusiness.ui.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityPassChooseBinding;


import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.CHANGE_LOGIN;
import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.CHANGE_TRANSFER;

import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.FORGET_LOGIN;
import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.FORGET_TRANSFER;
import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.LOGIN_PASS;
import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.TRANSFER_PASS;

public class PassChooseActivity extends BaseActivity<ActivityPassChooseBinding> {
    private int type;

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_pass_choose;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        if (type == LOGIN_PASS) {  //登录密码
            getTvTitle().setText("登录密码");
            mBinding.tvChange.setText("修改登录密码");
            mBinding.tvForget.setText("忘记登录密码");
        } else if (type == TRANSFER_PASS) {  //交易密码
            getTvTitle().setText("交易密码");
            mBinding.tvChange.setText("修改交易密码");
            mBinding.tvForget.setText("忘记交易密码");
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
        Bundle args = new Bundle();
        if (type == LOGIN_PASS) { //登录密码
            if (view.getId() == R.id.tv_change) {  //修改
                args.putInt(JumpUtil.TYPE, CHANGE_LOGIN);
            } else if (view.getId() == R.id.tv_forget) { //忘记
                args.putInt(JumpUtil.TYPE, FORGET_LOGIN);
            }
        } else if (type == TRANSFER_PASS) { //交易密码
            if (view.getId() == R.id.tv_change) {  //修改
                args.putInt(JumpUtil.TYPE, CHANGE_TRANSFER);
            } else if (view.getId() == R.id.tv_forget) { //忘记
                args.putInt(JumpUtil.TYPE, FORGET_TRANSFER);
            }
        }
        JumpUtil.overlay(getContext(), PasswordActivity.class, args);
    }
}
