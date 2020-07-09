package com.netmi.workerbusiness.ui.mine.wallet.payway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityAddAlipayBinding;

public class AddAlipayActivity extends BaseActivity<ActivityAddAlipayBinding> {
    private int type; //10 支付宝 ； 11微信

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_add_alipay;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        if (type == 10) {
            getTvTitle().setText("支付宝账户");
        } else if (type == 11) {
            getTvTitle().setText("微信账户");
            mBinding.tvConfirm.setText("绑定微信账户");
            mBinding.llWechatInfo.setVisibility(View.VISIBLE);
            mBinding.llAliInfo.setVisibility(View.GONE);
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
        if (view.getId() == R.id.tv_confirm) {
            if (type == 10) {

            } else if (type == 11) {

            }
        }
    }
}
