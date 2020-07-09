package com.netmi.workerbusiness.ui.mine.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityMineWithdrawBinding;
import com.netmi.workerbusiness.ui.mine.BankListActivity;
import com.netmi.workerbusiness.ui.mine.wallet.payway.AddAlipayActivity;

import static com.netmi.workerbusiness.ui.mine.BankListActivity.FROM_WALLET;

public class MineWithdrawActivity extends BaseActivity<ActivityMineWithdrawBinding> {
    private int type; //0 提现 ；1 提现信息管理

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_mine_withdraw;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        if (type == 0) {
            getTvTitle().setText("提现");
        } else if (type == 1) {
            getTvTitle().setText("提现信息管理");
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
        if (type == 1) { //提现信息管理
            if (view.getId() == R.id.ll_alipay) {
                args.putInt(JumpUtil.TYPE, 30);
                JumpUtil.overlay(getActivity(), InputAliWechatActivity.class, args);
            } else if (view.getId() == R.id.ll_wechat) {
                args.putInt(JumpUtil.TYPE, 31);
                JumpUtil.overlay(getActivity(), InputAliWechatActivity.class, args);
            } else if (view.getId() == R.id.ll_bank) {
                args.putInt(JumpUtil.TYPE, FROM_WALLET);
                JumpUtil.overlay(getContext(), BankListActivity.class, args);
            }
        } else if (type == 0) { //0 提现操作
            if (view.getId() == R.id.ll_alipay) {
                args.putInt(JumpUtil.TYPE, 30);
                JumpUtil.overlay(getContext(), MineWithdrawDetailActivity.class, args);
            } else if (view.getId() == R.id.ll_wechat) {
                args.putInt(JumpUtil.TYPE, 31);
                JumpUtil.overlay(getContext(), MineWithdrawDetailActivity.class, args);
            } else if (view.getId() == R.id.ll_bank) {
                args.putInt(JumpUtil.TYPE, 32);
                JumpUtil.overlay(getContext(), MineWithdrawDetailActivity.class, args);
            }
        }
    }
}
