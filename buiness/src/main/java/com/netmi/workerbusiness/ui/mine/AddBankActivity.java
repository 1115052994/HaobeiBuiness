package com.netmi.workerbusiness.ui.mine;

import android.databinding.ViewDataBinding;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.ui.NormalDialog;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.ChooseBankEntity;
import com.netmi.workerbusiness.databinding.ActivityAddBankBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class AddBankActivity extends BaseActivity<ActivityAddBankBinding> {
    private String phone;
    private boolean shoeBankList = false;
    private String bank_name;
    private String bank_id;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_bank;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("添加银行卡");
        new InputListenView(mBinding.tvConfirm, mBinding.etName, mBinding.etCardNum, mBinding.etBankName, mBinding.etBankName2) {
            @Override
            public boolean customJudge() {
//                return mBinding.etCode.getText().toString().length() > 3;
                return true;
            }
        };
    }


    @Override
    protected void initData() {

    }


    @Override
    public void doClick(View view) {
        super.doClick(view);
        phone = mBinding.etPhone.getText().toString();
        int id = view.getId();
        if (id == R.id.tv_get_code) {
            if (TextUtils.isEmpty(phone)) {
                showError("请输入银行卡预留手机号");
            } else if (!Strings.isPhone(phone)) {
                showError("请输入正确的手机号");
            } else {
//                doSendSMS(phone, "bindBank");
            }
        } else if (id == R.id.tv_confirm) {
            addBankCard(mBinding.etBankName2.getText().toString(), mBinding.etCardNum.getText().toString()
                    , mBinding.etBankName.getText().toString(), mBinding.etName.getText().toString());
        }
    }

    private void addBankCard(String subbranch_name, String bank_card, String bank_name, String name) {
        RetrofitApiFactory.createApi(MineApi.class)
                .addBankCard(subbranch_name, bank_card, bank_name, name)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        finish();
                    }
                });
    }

}
