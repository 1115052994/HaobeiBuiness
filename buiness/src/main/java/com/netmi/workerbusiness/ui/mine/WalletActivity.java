package com.netmi.workerbusiness.ui.mine;

import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.HaibeiConvertApi;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.api.WalletApi;
import com.netmi.workerbusiness.data.entity.haibei.HaibeiData;
import com.netmi.workerbusiness.data.entity.mine.WalletEntity;
import com.netmi.workerbusiness.databinding.ActivityWalletBinding;
import com.netmi.workerbusiness.ui.home.haibei.HaiBeiWithdrawDepositActivity;
import com.netmi.workerbusiness.ui.mine.wallet.ETHWalletDetailActivity;
import com.netmi.workerbusiness.ui.mine.wallet.HaibeiChangeActivity;
import com.netmi.workerbusiness.ui.mine.wallet.HaibeiRechargeActivity;
import com.netmi.workerbusiness.ui.mine.wallet.HaibeiTransferActivity;
import com.netmi.workerbusiness.ui.mine.wallet.HaibeiWithdrawActivity;
import com.netmi.workerbusiness.ui.mine.wallet.MineCollectionActivity;
import com.netmi.workerbusiness.ui.mine.wallet.MineWithdrawActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.annotations.NonNull;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;

public class WalletActivity extends BaseActivity<ActivityWalletBinding> {

    private String ratio;

    @Override
    protected int getContentView() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("钱包");
        getRightImage().setImageResource(R.mipmap.say_help);
        getRightImage().setVisibility(View.VISIBLE);
        int shop_user_type = getIntent().getIntExtra("SHOP_USER_TYPE", -1);
        if(shop_user_type==1||shop_user_type==4){
            mBinding.layoutRight.setVisibility(View.GONE);
            mBinding.llMineWithdrawSeckill.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initData() {
        initdata();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        Bundle args = new Bundle();
        int id = view.getId();
        if (id == R.id.ll_haibei_recharge) {   //海贝充值
            args.putInt(JumpUtil.TYPE, 20);
            JumpUtil.overlay(getContext(), HaibeiRechargeActivity.class, args);
        } else if (id == R.id.ll_haibei_withdraw) {//海贝提取
            JumpUtil.overlay(getContext(), HaibeiWithdrawActivity.class);
        } else if (id == R.id.ll_haibei_transfer) {   //海贝转账
            JumpUtil.overlay(getContext(), HaibeiTransferActivity.class);
        } else if (id == R.id.ll_haibei_collection) { //海贝收款
            args.putInt(JumpUtil.TYPE, 21);
            JumpUtil.overlay(getContext(), HaibeiRechargeActivity.class, args);
        } else if (id == R.id.tv_change) { //海贝转换（新增）
            args.putString(JumpUtil.VALUE, ratio);
            JumpUtil.overlay(getContext(), HaibeiChangeActivity.class, args);
        } else if (id == R.id.ll_mine_withdraw) {  //我的提取
            args.putInt(JumpUtil.TYPE, 0);//0 提现 ；1 提现信息管理
            JumpUtil.overlay(getContext(), MineWithdrawActivity.class, args);
        } else if (id == R.id.ll_mine_withdraw_info) { //我的提取信息'
            args.putInt(JumpUtil.TYPE, 1);//0 提现 ；1 提现信息管理
            JumpUtil.overlay(getContext(), MineWithdrawActivity.class, args);
        } else if (id == R.id.ll_mine_collection) { //我的收款
            JumpUtil.overlay(getContext(), MineCollectionActivity.class);
        } else if (id == R.id.tv_haibei_detail) { //海贝明细
            JumpUtil.overlay(getContext(), ETHWalletDetailActivity.class);
        } else if (id == R.id.tv_mine_detail) { //我的余额
            JumpUtil.overlay(getContext(), TransactionDetailsActivity.class);
        } else if (id == R.id.layout_right) { //奖励金
            JumpUtil.overlay(getContext(), TransactionBountyActivity.class);
        }else if (id == R.id.ll_mine_withdraw_seckill) { //奖励金提现
            JumpUtil.overlay(getContext(), HaiBeiWithdrawDepositActivity.class);
        }else if(id == R.id.iv_setting){
            doAgreement(52);
        }
    }

    private void getWallet() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getWallet("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<WalletEntity>>() {
                    @Override
                    public void onSuccess(BaseData<WalletEntity> data) {
                        mBinding.tvMineBalance.setText("¥" + data.getData().getBalance());
                    }
                });
    }

    private void getWalletInfo() {
        RetrofitApiFactory.createApi(WalletApi.class)
                .getWalletInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<com.netmi.workerbusiness.data.entity.walllet.WalletEntity>>() {
                    @Override
                    public void onSuccess(BaseData<com.netmi.workerbusiness.data.entity.walllet.WalletEntity> data) {
                        mBinding.tvHaibeiBalance.setText(data.getData().getHand_balance());
                        mBinding.tvHaibeiBalanceMoney.setText("≈" + data.getData().getMoney() + "元");
                        mBinding.tvWaitOne.setText("待结算：" + data.getData().getShop_freeze_price());
                        mBinding.tvWaitTwo.setText("待结算：" + data.getData().getFreeze_price() );
                        mBinding.tvEarn.setText("海贝兑换预估：" + data.getData().getHand_yugu()+ "元");
                        mBinding.tvCanChange.setText("可转换：" + data.getData().getConversion() + "（≈" + data.getData().getConversion_price() + "元）");

                        ratio = data.getData().getRatio();
                    }
                });
    }

    private void initdata() {
        RetrofitApiFactory.createApi(HaibeiConvertApi.class)
                .getHaibeiConvert("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<HaibeiData>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<HaibeiData> data) {
                        mBinding.setData1(data.getData());
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getWallet();  //我的钱包信息
        getWalletInfo(); //区块链钱包信息
    }

    //我的钱包问号
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
                            if (agreementEntityBaseData.getData() != null && agreementEntityBaseData.getData().getContent() != null && agreementEntityBaseData.getData().getTitle() != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString(WEBVIEW_TITLE, agreementEntityBaseData.getData().getTitle());
                                bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_CONTENT);
                                bundle.putString(WEBVIEW_CONTENT, agreementEntityBaseData.getData().getContent());
                                JumpUtil.overlay(getContext(), BaseWebviewActivity.class, bundle);
                            }
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
