package com.netmi.workerbusiness.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.entity.mine.GetApplyInfo;
import com.netmi.workerbusiness.databinding.ActivityBingingThreeBinding;

import static com.netmi.workerbusiness.ui.login.BankThreeActivity.BANKTHREE_CODE;
import static com.netmi.workerbusiness.ui.login.BankThreeActivity.BANKTHREE_NAME;


public class BindingThreeActivity extends BaseActivity<ActivityBingingThreeBinding> {

    private String name;
    private String idcard;
    private GetApplyInfo getApplyInfo;
    private String code;

    @Override
    protected int getContentView() {
        return R.layout.activity_binging_three;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("绑定结算银行卡");
        name = getIntent().getStringExtra("name");
        idcard = getIntent().getStringExtra("idcard");

        SpannableString s = new SpannableString("点击输入：" + name + "的银行卡号");
        mBinding.tvBindingThree.setHint(s);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        Bundle args = new Bundle();
        super.doClick(view);
        //银行列表
        if (view.getId() == R.id.tv_bank_three) {
            JumpUtil.startForResult(this, BankThreeActivity.class, 1000, args);
        } else if (view.getId() == R.id.tv_submit_three) {//确认信息
            String tv_bindingthree = mBinding.tvBindingThree.getText().toString();
            if (TextUtils.isEmpty(tv_bindingthree)) {
                showError("请输入银行卡号");
                return;
            }
            String tv_bankthree = mBinding.tvBankThree.getText().toString();
            if (TextUtils.isEmpty(tv_bankthree)) {
                showError("请选择开户行");
                return;
            }
            JumpUtil.overlay(this, BankOkActivity.class, "name", name, "idcard", idcard, "binding", tv_bindingthree, "bank", tv_bankthree, "code", code);
        }else if (view.getId() == R.id.card_number_three){

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 2000) {
            String name = data.getStringExtra(BANKTHREE_NAME);
            code = data.getStringExtra(BANKTHREE_CODE);
            mBinding.tvBankThree.setText(name);

        }

    }
}

