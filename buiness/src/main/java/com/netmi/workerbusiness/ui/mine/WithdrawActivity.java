package com.netmi.workerbusiness.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.data.entity.AddressEntity;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.BankListEntity;
import com.netmi.workerbusiness.data.entity.mine.WithdrawMessEntity;
import com.netmi.workerbusiness.databinding.ActivityWithdrawBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import static com.netmi.workerbusiness.ui.mine.BankListActivity.BANK_MESS;
import static com.netmi.workerbusiness.ui.mine.BankListActivity.FROM_WITHDRAW;

public class WithdrawActivity extends BaseActivity<ActivityWithdrawBinding> {

    private int id;
    private double amount;

    @Override
    protected int getContentView() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("提现");
    }

    @Override
    protected void initData() {
        getBankList();
        doWithdrawMess();
    }

    @Override
    public void doClick(View view) {
        Bundle args = new Bundle();

        super.doClick(view);
        int viewId = view.getId();
        if (viewId == R.id.tv_confirm) {
            if (TextUtils.isEmpty(mBinding.etAmount.getText())) {
                showError("请输入提现金额");
            } else {
                if (amount < Integer.valueOf(mBinding.etAmount.getText().toString())) {
                    showError("您的余额不足，请重新输入提现金额");
                } else if (Integer.valueOf(mBinding.etAmount.getText().toString()) < 100) {
                    showError("提现金额不能小于100");
                } else if (Integer.valueOf(mBinding.etAmount.getText().toString()) > 100 && Integer.valueOf(mBinding.etAmount.getText().toString()) % 100 != 0) {
                    showError("请输入百位整数提现");
                } else {
                    args.putString(JumpUtil.TYPE, String.valueOf(id));
                    args.putString(JumpUtil.VALUE, mBinding.etAmount.getText().toString());
                    JumpUtil.overlay(getContext(), MessCodeActivity.class, args);
                }
            }
        } else if (viewId == R.id.rl_bank_card) {
            args.putInt(JumpUtil.TYPE, FROM_WITHDRAW);
            JumpUtil.startForResult(this, BankListActivity.class, FROM_WITHDRAW, args);
        }
    }


    private void doWithdrawMess() {
        RetrofitApiFactory.createApi(MineApi.class)
                .withdrawMess("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<WithdrawMessEntity>>() {
                    @Override
                    public void onSuccess(BaseData<WithdrawMessEntity> data) {
                        mBinding.tvMess.setText(data.getData().getShop_setting());
                        mBinding.tvAmount.setText("可提现金额¥" + data.getData().getWithdraw_money());
                        amount = Double.valueOf(data.getData().getWithdraw_money());
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FROM_WITHDRAW) {
                BankListEntity bankListEntity = (BankListEntity) data.getSerializableExtra(BANK_MESS);
                id = bankListEntity.getId();
                mBinding.setModel(bankListEntity);
            }
        }
    }


    private void getBankList() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getBankList("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<BankListEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<List<BankListEntity>> data) {
                        if (data.getData() != null) {
                            id = data.getData().get(0).getId();
                            mBinding.setModel(data.getData().get(0));
                        } else {
                            mBinding.tvRemind.setVisibility(View.VISIBLE);
                            mBinding.rlBankCard.setVisibility(View.GONE);
                            mBinding.tvConfirm.setClickable(false);
                            mBinding.tvConfirm.setBackgroundColor(getResources().getColor(R.color.gray_44));
                        }
                    }
                });
    }

}
