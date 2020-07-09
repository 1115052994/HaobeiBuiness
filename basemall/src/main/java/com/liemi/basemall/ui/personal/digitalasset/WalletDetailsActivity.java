package com.liemi.basemall.ui.personal.digitalasset;

import android.os.Bundle;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ActivityWalletDetailsBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
/**
 * 钱包明细首页
 */
public class WalletDetailsActivity extends BaseActivity<ActivityWalletDetailsBinding> {
    //明细列表类型
    //充值明细
    public static final int DETAILS_TAKE_IN = 1;
    //提取明细
    public static final int DETAILS_TAKE_OUT = 2;
    //购买明细
    public static final int DETAILS_BUY = 3;
    //退款明细
    public static final int DETAILS_BACK_MONEY = 4;

    @Override
    protected int getContentView() {
        return R.layout.activity_wallet_details;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("查看明细");
        //getRightSetting().setText("总明细");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        //super.doClick(view);
        if (view.getId() == com.netmi.baselibrary.R.id.ll_back || view.getId() == com.netmi.baselibrary.R.id.iv_back) {
            onBackPressed();
            return;
        }

        int types = -1;
        String title = "";
        if (view.getId() == R.id.tv_setting) {
            //全部详情
            title = "全部明细";
        }
        if (view.getId() == R.id.ll_take_in_details) {
            //转账明细
            types = DETAILS_TAKE_IN;
            title = "充值明细";
        }
        if (view.getId() == R.id.ll_take_out_details) {
            types = DETAILS_TAKE_OUT;
            title = "提取明细";
        }

        if (view.getId() == R.id.ll_buy_details) {
            types = DETAILS_BUY;
            title = "购买明细";
        }

        if (view.getId() == R.id.ll_back_money_details) {
            types = DETAILS_BACK_MONEY;
            title = "退款明细";
        }


        Bundle bundle = new Bundle();
        bundle.putInt(TradePropertyDetailsActivity.TRADE_PROPERTY_DETAILS_TYPES, types);
        bundle.putString(TradePropertyDetailsActivity.TRADE_PROPERTY_DETAILS_TITLE, title);
        JumpUtil.overlay(this, TradePropertyDetailsActivity.class, bundle);
    }
}

