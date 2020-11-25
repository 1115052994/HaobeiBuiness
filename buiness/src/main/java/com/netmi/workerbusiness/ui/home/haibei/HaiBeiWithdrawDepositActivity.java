package com.netmi.workerbusiness.ui.home.haibei;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.HaibeiConvertApi;
import com.netmi.workerbusiness.data.entity.haibei.HaibeiData;
import com.netmi.workerbusiness.databinding.ActivityHaiBeiWithdrawDepositBinding;
import com.netmi.workerbusiness.ui.mine.wallet.InputAliWechatActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.annotations.NonNull;

import static com.netmi.workerbusiness.ui.mine.wallet.InputAliWechatActivity.ACCOUNT_WITHDRAW;
import static com.netmi.workerbusiness.ui.mine.wallet.InputAliWechatActivity.NAME_WITHDRAW;
import static com.netmi.workerbusiness.ui.mine.wallet.MineWithdrawDetailActivity.ALI_WITHDRAE;

public class HaiBeiWithdrawDepositActivity extends BaseActivity<ActivityHaiBeiWithdrawDepositBinding> {

    private String account;
    private String name;
    private String bonus;
    @Override
    protected int getContentView() {
        return R.layout.activity_hai_bei_withdraw_deposit;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("转换");
        mBinding.payLeftLayout.setSelected(true);
        initda();
    }

    private void initda() {
        RetrofitApiFactory.createApi(HaibeiConvertApi.class)
                .getHaibeiConvert("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<HaibeiData>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<HaibeiData> data) {
                        bonus = data.getData().getBonus_quota();
                        mBinding.setHint(data.getData().getBonus_quota());
                    }
                });
    }

    @Override
    protected void initData() {
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if(id == R.id.pay_left_layout||id == R.id.pay_righ_layout){
            mBinding.payLeftLayout.setSelected(id == R.id.pay_left_layout);
            mBinding.payRighLayout.setSelected(id == R.id.pay_righ_layout);
            mBinding.bottomView1.setVisibility(id == R.id.pay_left_layout ? View.VISIBLE : View.INVISIBLE);
            mBinding.bottomView2.setVisibility(id == R.id.pay_righ_layout ? View.VISIBLE : View.INVISIBLE);
            mBinding.payLeftLayoutMoney.setVisibility(id == R.id.pay_left_layout ? View.VISIBLE : View.GONE);
            mBinding.payRightLayoutTop.setVisibility(id == R.id.pay_righ_layout ? View.VISIBLE : View.GONE);
            mBinding.payRightLayoutBottom.setVisibility(id == R.id.pay_righ_layout ? View.VISIBLE : View.GONE);
        }else if(id==R.id.tv_add_alipay){
            Bundle args = new Bundle();
            args.putInt(JumpUtil.TYPE, 30);
            JumpUtil.startForResult(getActivity(), InputAliWechatActivity.class, ALI_WITHDRAE, args);
        }else if(id == R.id.tv_up_apy){
            if(TextUtils.isEmpty(mBinding.edWithdrawDepositMoney.getText().toString())){
                showError("请输入提现金额");
                return;
            }
            Double text = Double.parseDouble(mBinding.edWithdrawDepositMoney.getText().toString().trim());
            //提现
            if(text<1){
                showError("最小金额为1元");
            }else if((Double.parseDouble(bonus)-text)<0){
                showError("请输入正确的金额");
            }else {
                RetrofitApiFactory.createApi(HaibeiConvertApi.class)
                        .haibeiWithdrawal(mBinding.edWithdrawDepositMoney.getText().toString().trim())
                        .compose(RxSchedulers.compose())
                        .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new XObserver<BaseData>() {
                            @Override
                            public void onSuccess(@NonNull BaseData data) {
                                JumpUtil.overlay(getContext(),HaiBeiShowResultActivity.class);
                            }
                        });

            }


        }else if(id == R.id.tv_all_money){
            //全部
            mBinding.edWithdrawDepositMoney.setText(bonus);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ALI_WITHDRAE) { //支付宝
                Bundle pay = data.getBundleExtra("pay");
                name = pay.getString(NAME_WITHDRAW);
                account = pay.getString(ACCOUNT_WITHDRAW);
                mBinding.tvAddAlipay.setText("支付宝账号："+account);
            }
        }
    }
}
