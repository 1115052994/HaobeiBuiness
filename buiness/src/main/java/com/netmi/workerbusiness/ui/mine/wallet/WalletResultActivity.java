package com.netmi.workerbusiness.ui.mine.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityWalletResultBinding;

import org.greenrobot.eventbus.EventBus;

public class WalletResultActivity extends BaseActivity<ActivityWalletResultBinding> {

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_wallet_result;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {

        getTvTitle().setText(getIntent().getStringExtra(JumpUtil.TYPE) + "成功");
        mBinding.tvResult.setText(getIntent().getStringExtra(JumpUtil.TYPE) + "成功");
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
            if (getIntent().getStringExtra(JumpUtil.TYPE).equals("购买")) {  //回到生态商城首页

                Bundle args = new Bundle();
                args.putInt(JumpUtil.TYPE, 2);
//                JumpUtil.overlay(getContext(), EcologicalMallActivity.class,args);
                finish();
            } else if (getIntent().getStringExtra(JumpUtil.TYPE).equals("参与")) {
                finish();
//                JumpUtil.overlay(getContext(), WelfareGameActivity.class);
            } else {
                finish();
                // 发送广播
//                EventBus.getDefault().post(new WalletSuccessEvent(1));
//                JumpUtil.overlay(getContext(), MainActivity.class);
            }
        }
    }

}
