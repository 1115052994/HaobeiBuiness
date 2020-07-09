package com.liemi.basemall.ui.personal.digitalasset;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.entity.MinePropertyEntity;
import com.liemi.basemall.data.entity.user.CoinEntity;
import com.liemi.basemall.databinding.ActivityTradePropertyBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * 我的钱包页面
 */
public class TradePropertyActivity extends BaseActivity<ActivityTradePropertyBinding> {
    public static final String PREVIEW_COIN_INFO = "coinInfo";
    //private MinePropertyEntity minePropertyEntity;
    private CoinEntity mCoinEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_trade_property;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("我的钱包");
    }

    @Override
    protected void initData() {
        //doTradePropertyInfo();
        if(getIntent() != null && getIntent().getExtras() != null){
            mCoinEntity = (CoinEntity) getIntent().getExtras().getSerializable(PREVIEW_COIN_INFO);
            if(mCoinEntity == null){
                showError("错误的区块链信息");
                finish();
            }
        }else{
            showError("没有区块链信息");
            finish();
        }
        mBinding.setCoinEntity(mCoinEntity);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if(view.getId() == R.id.iv_back){
            finish();
        }
        if (view.getId() == R.id.ll_watch_details) {
            JumpUtil.overlay(this, WalletDetailsActivity.class);
        }

        if (view.getId() == R.id.tv_recharge) {
            JumpUtil.overlay(this,RechargeActivity.class);
        }
        if (view.getId() == R.id.tv_receive) {
            Bundle bundle = new Bundle();
            if(mCoinEntity != null) {
                bundle.putSerializable(PropertyTakeOutActivity.MINE_PROPERTY_INFO, mCoinEntity);
            }
            JumpUtil.startForResult(this, PropertyTakeOutActivity.class, 1001, bundle);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && data != null) {
            if (data.getBooleanExtra("takeOutSuccess", true)) {
                doTradePropertyInfo();
            }
        }
    }

    //请求交易资产信息
    private void doTradePropertyInfo() {
        /*
        RetrofitApiFactory.createApi(MineApi.class)
                .doMineProperty("default")
                .compose(this.<BaseData<MinePropertyEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<MinePropertyEntity>>compose())
                .subscribe(new BaseObserver<BaseData<MinePropertyEntity>>() {
                    @Override
                    public void onNext(BaseData<MinePropertyEntity> minePropertyEntityBaseData) {
                        if (minePropertyEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {

                            minePropertyEntity = minePropertyEntityBaseData.getData();
                            mBinding.setPropertyTotal(minePropertyEntityBaseData.getData().getAmount());
                        } else {
                            showError(minePropertyEntityBaseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });
                */
    }


    //请求资产说明
    private void doAgreement(int type) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .getAgreement(type)
                .compose(this.<BaseData<AgreementEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<AgreementEntity>>compose())
                .subscribe(new BaseObserver<BaseData<AgreementEntity>>() {
                    @Override
                    public void onNext(BaseData<AgreementEntity> agreementEntityBaseData) {
                        if (agreementEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent(TradePropertyActivity.this, BaseWebviewActivity.class);
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_TITLE, agreementEntityBaseData.getData().getTitle());
                            if (agreementEntityBaseData.getData().getLink_type().equals("2")) {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_URL);
                            } else {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_CONTENT);
                            }
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_CONTENT, agreementEntityBaseData.getData().getContent());
                            startActivity(intent);
                        } else {
                            showError(agreementEntityBaseData.getErrmsg());
                        }
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
