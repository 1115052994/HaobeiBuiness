package com.netmi.workerbusiness.ui.mine.wallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.cache.WithdrawCache;
import com.netmi.workerbusiness.data.entity.mine.WithdrawMessEntity;
import com.netmi.workerbusiness.databinding.ActivityInputAliWechatBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class InputAliWechatActivity extends BaseActivity<ActivityInputAliWechatBinding> {
    private int type; //30 支付宝 ； 31  微信

    public static final String NAME_WITHDRAW = "name_withdraw";
    public static final String ACCOUNT_WITHDRAW = "account_withdraw";

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_input_ali_wechat;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("提现信息");
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        new InputListenView(mBinding.tvConfirm, mBinding.etAccount, mBinding.etName) {
            @Override
            public boolean customJudge() {
                return true;
            }
        };
        if (type == 30) {
            mBinding.etName.setText(WithdrawCache.getAliName());
            mBinding.etAccount.setText(WithdrawCache.getAliAccount());
        } else if (type == 31) {
            mBinding.etAccount.setHint("请输入微信绑定的手机号");
            mBinding.etName.setText(WithdrawCache.getWechatName());
            mBinding.etAccount.setText(WithdrawCache.getWechatAccount());
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
            String name = mBinding.etName.getText().toString();
            String account = mBinding.etAccount.getText().toString();
            if (type == 30) {//支付宝
                addInfo("0", account, name);
            } else if (type == 31) { //微信
                addInfo("1", account, name);
            }
        }
    }

    private void addInfo(String typeUp, String phone, String name) {//获取提现手续费 0支付宝1微信
        RetrofitApiFactory.createApi(MineApi.class)
                .addAliInfo(typeUp, phone, name)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        Intent intent = new Intent();
                        intent.putExtra(NAME_WITHDRAW, name);
                        intent.putExtra(ACCOUNT_WITHDRAW, phone);
                        setResult(RESULT_OK, intent);
                        if (typeUp.equals("0")) {
                            WithdrawCache.putAliname(name);
                            WithdrawCache.putAliaccount(phone);
                        } else if (typeUp.equals("1")) {
                            WithdrawCache.putWechatname(name);
                            WithdrawCache.putWechatAccount(phone);
                        }
                        finish();
                    }
                });
    }


//    Intent intent = new Intent();
//                intent.putExtra(NAME_WITHDRAW, name);
//                intent.putExtra(ACCOUNT_WITHDRAW, account);
//    setResult(RESULT_OK, intent);
//                WithdrawCache.putAliname(name);
//                WithdrawCache.putAliaccount(account);
//    finish();

}


