package com.liemi.basemall.ui.login;

import android.databinding.adapters.TextViewBindingAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.LoginApi;
import com.liemi.basemall.data.entity.user.CoinEntity;
import com.liemi.basemall.databinding.ActivitySetPayPasswordBinding;
import com.liemi.basemall.ui.personal.digitalasset.TradePropertyActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class SetPayPasswordActivity extends BaseActivity<ActivitySetPayPasswordBinding> implements TextViewBindingAdapter.AfterTextChanged {
    public static final String PREVIEW_TRADE_INFO = "previewTradeInfo";
    //是否需要直接跳转资产页面
    private CoinEntity mCoinEntity = null;
    @Override
    protected int getContentView() {
        return R.layout.activity_set_pay_password;
    }
    @Override
    protected void initUI() {
        getTvTitle().setText("设置支付密码");
        mBinding.setAfterText(this);
    }

    @Override
    protected void initData() {
        mCoinEntity = (CoinEntity) getIntent().getSerializableExtra(PREVIEW_TRADE_INFO);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if(view.getId() == R.id.bt_confirm){
            String password = mBinding.etInputPayPassword.getText().toString();
            String passwordAgain = mBinding.etInputPayPasswordAgain.getText().toString();
            if(TextUtils.isEmpty(password)){
                showError("请输入支付密码");
                return;
            }
            if(TextUtils.isEmpty(passwordAgain)){
                showError("请输入确认密码");
                return;
            }
            if(password.length() < 6){
                showError("密码长度最小为6位");
                return;
            }
            if(!password.equals(passwordAgain)){
                showError("两次输入的密码不一致，请检查");
                return;
            }
            doSetPayPassword();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!TextUtils.isEmpty(mBinding.etInputPayPassword.getText().toString())
                && !TextUtils.isEmpty(mBinding.etInputPayPasswordAgain.getText().toString())){
            mBinding.btConfirm.setEnabled(true);
        }else{
            mBinding.btConfirm.setEnabled(false);
        }
    }

    //请求设置支付密码
    private void doSetPayPassword(){
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doSetPayPassword(MD5.GetMD5Code(mBinding.etInputPayPassword.getText().toString(),true))
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("设置支付密码成功");
                        //用户已经设置了交易密码，手动进行绑定，主要用于用户在密码管理页面跳转过来之后无法立即更新数据
                        if(UserInfoCache.get() != null){
                            UserInfoEntity en = UserInfoCache.get();
                            en.setIs_set_paypassword(UserInfoEntity.BIND);
                            UserInfoCache.put(en);
                        }
                        if(mCoinEntity != null){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(TradePropertyActivity.PREVIEW_COIN_INFO,mCoinEntity);
                            JumpUtil.overlay(SetPayPasswordActivity.this,TradePropertyActivity.class,bundle);
                        }
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });
    }
}
