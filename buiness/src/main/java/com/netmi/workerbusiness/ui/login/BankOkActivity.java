package com.netmi.workerbusiness.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.ShareEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.BankOkThreeApi;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.ActivityBankokThreeBinding;
import com.netmi.workerbusiness.ui.MainActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.internal.observers.BlockingBaseObserver;

public class BankOkActivity extends BaseActivity<ActivityBankokThreeBinding> {

    private String  type;
    private String login;
    private String  name;
    private String  idcard;
    private String  binding;
    private String  bank;
    private String  code;
    private String tvVerify;
    private String tvPhone;
    private ShareEntity entity;

    @Override
    protected int getContentView() {
        return R.layout.activity_bankok_three;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("绑定结算银行卡");
        name = getIntent().getStringExtra("name");
        mBinding.etBankokThree.setText(name);

        idcard = getIntent().getStringExtra("idcard");
        mBinding.etBankokIdentity.setText(idcard);

        binding = getIntent().getStringExtra("binding");
        mBinding.etBankokCard.setText(binding);

        bank = getIntent().getStringExtra("bank");
        mBinding.etBankokBank.setText(bank);

        code = getIntent().getStringExtra("code");

        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        Bundle args = new Bundle();
        super.doClick(view);
        if (view.getId() == R.id.tv_bankok_verify) {
//            showError("短信验证");
            tvPhone = mBinding.etBankokPhone.getText().toString();
            if (TextUtils.isEmpty(tvPhone)){
                showError("请输入手机号");
                return;
            }
            mBinding.tvThreeCode.setVisibility(View.VISIBLE);
            mBinding.tvBankokVerify.setVisibility(View.GONE);
            doAuthCode();
            startCountDownTimer();
        } else if (view.getId() == R.id.tv_three_code) {
            //获取验证码
            doAuthCode();
            return;
        }
        if (view.getId() == R.id.tv_bank_ok) {
             tvVerify = mBinding.etBankokVerify.getText().toString();
            if (TextUtils.isEmpty(tvVerify)){
                showError("请输入验证码");
                return;
            }

            doVerify();
        }
    }

    //信息确认
    private void doVerify() {
        showProgress("");
        RetrofitApiFactory.createApi(BankOkThreeApi.class)
                .doBankOkVerify(code, binding, tvVerify,tvPhone)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        Bundle bundle = new Bundle();
                        bundle.putString("bankId", code);
                        bundle.putString("cardNo", binding);
                        bundle.putString("code", tvVerify);
                        bundle.putString("phone", tvPhone);
                        hideProgress();

                        doGetShopInfo();

                    }
                    @Override
                    public void onError(Throwable e) {
                        showError(e.getMessage());
                    }
                });
    }

    //短信验证码
    private void doAuthCode() {
        showProgress("");
        RetrofitApiFactory.createApi(BankOkThreeApi.class)
                .doBankOkThree(tvPhone)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new BlockingBaseObserver<BaseData>() {
                    @Override
                    public void onNext(BaseData baseData) {
                        showError("验证码已发送");
                        if (baseData.getErrcode() == Constant.SUCCESS_CODE) {
                            Bundle bundle = new Bundle();
                            bundle.putString("login", login);
                            bundle.putString("type", type);
                            bundle.putString("phone", tvPhone);
                            startCountDownTimer();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e.getMessage());
                    }
                });
    }

    //开启倒计时
    private void startCountDownTimer() {
        mBinding.tvThreeCode.setEnabled(false);
        CountDownTimer mCountDownTimer = new CountDownTimer(Constant.COUNT_DOWN_TIME_DEFAULT, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.tvThreeCode.setText("重新发送(" + millisUntilFinished / 1000 + "s)");
//                mBinding.tvThreeCode.setText("millisUntilFinished / 1000 " );
            }

            @Override
            public void onFinish() {
                mBinding.tvThreeCode.setEnabled(true);
                mBinding.tvThreeCode.setText("重新获取验证码");
            }
        };
        mCountDownTimer.start();
    }

    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(com.netmi.workerbusiness.data.api.MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        finish();
                        UserInfoCache.get().setShop_pay_status(String.valueOf(data.getData().getShop_pay_status()));
                        UserInfoCache.get().setShop_apply_status(String.valueOf(data.getData().getShop_apply_status()));
//                        JumpUtil.overlay(PersonalOfflineInfoActivity.this, ContractOfflineListActivity.class);
                        toMain();//提交确定，跳转首页

                    }
                });
    }

    public void toMain() {
        JumpUtil.overlay(getContext(), MainActivity.class);//跳转首页

    }

}
