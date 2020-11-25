package com.netmi.workerbusiness.ui.mine.wallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.ui.personal.digitalasset.PropertyTakeOutConfirmDialog;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.cache.WithdrawCache;
import com.netmi.workerbusiness.data.entity.mine.BankListEntity;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.data.entity.mine.WithdrawMessEntity;
import com.netmi.workerbusiness.data.entity.walllet.AliWechatEntity;
import com.netmi.workerbusiness.databinding.ActivityMineWithdrawDetailBinding;
import com.netmi.workerbusiness.ui.mine.BankListActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import static com.netmi.workerbusiness.ui.mine.BankListActivity.BANK_MESS;
import static com.netmi.workerbusiness.ui.mine.BankListActivity.FROM_WITHDRAW;
import static com.netmi.workerbusiness.ui.mine.wallet.InputAliWechatActivity.ACCOUNT_WITHDRAW;
import static com.netmi.workerbusiness.ui.mine.wallet.InputAliWechatActivity.NAME_WITHDRAW;

public class MineWithdrawDetailActivity extends BaseActivity<ActivityMineWithdrawDetailBinding> {
    private int type; //30 支付宝 ； 31  微信 ； 32 银行卡
    private int id;
    private double amount;
    public static final int ALI_WITHDRAE = 1000;
    public static final int WECHAT_WITHDRAE = 1001;
    private String account;
    private String name;

    //确定对话框
    private String mTakeOutNum;   //输入的提现金额
    private String mHandlingNum;   //手续费
    private String mRealNum;       //实际提取

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_mine_withdraw_detail;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("提现");
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
    }

    /**
     * 数据初始化
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        doWithdrawMess();
        getAliInfo();
        if (type == 30) {
            mBinding.tvBankName.setText("请填写支付宝账户信息");
            getTvTitle().setText("支付宝提现");
            account = WithdrawCache.getAliAccount();
            name = WithdrawCache.getAliName();
            //为了获取头像
            doGetShopInfo("0");
            if (!WithdrawCache.getAliAccount().isEmpty()) {
                mBinding.tvBankNameLeft.setText(WithdrawCache.getAliName()+"      "+WithdrawCache.getAliAccount());
                mBinding.tvBankName.setText("");
            }
        } else if (type == 31) {
            mBinding.tvBankName.setText("请填写微信信息");
            getTvTitle().setText("微信提现");
            account = WithdrawCache.getWechatAccount();
            name = WithdrawCache.getWechatName();
            //为了获取头像
            doGetShopInfo("0");
            if (!WithdrawCache.getWechatAccount().isEmpty()) {
                mBinding.tvBankNameLeft.setText(WithdrawCache.getAliName()+"      "+WithdrawCache.getAliAccount());
                mBinding.tvBankName.setText("");
            }
        } else if (type == 32) {
            getBankList();
        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int viewId = view.getId();
        Bundle args = new Bundle();
        if (viewId == R.id.tv_all_withdraw) { //全部提现
            mBinding.etNum.setText(String.valueOf(amount));
        } else if (viewId == R.id.tv_confirm) {  //提现
            if (TextUtils.isEmpty(mBinding.etNum.getText())) {
                showError("请输入提现金额");
            } else if (mBinding.tvBankName.getText().toString().equals("请填写支付宝账户信息")) {
                showError("请填写支付宝账户信息");
            } else if (mBinding.tvBankName.getText().toString().equals("请填写微信信息")) {
                showError("请填写微信信息");
            } else {
                getFee();
            }
        } else if (viewId == R.id.ll_bank_card) {
            if (type == 30) {
                args.putInt(JumpUtil.TYPE, type);
                JumpUtil.startForResult(getActivity(), InputAliWechatActivity.class, ALI_WITHDRAE, args);
            } else if (type == 31) {
                args.putInt(JumpUtil.TYPE, type);
                JumpUtil.startForResult(getActivity(), InputAliWechatActivity.class, WECHAT_WITHDRAE, args);
            } else if (type == 32) {
                args.putInt(JumpUtil.TYPE, FROM_WITHDRAW);
                JumpUtil.startForResult(this, BankListActivity.class, FROM_WITHDRAW, args);
            }
        }
    }


    /**
     * 确认弹窗
     */
    private PropertyTakeOutConfirmDialog mCondirmDialog;

    private void showConfirmDialog() {
        if (mCondirmDialog == null) {
            mCondirmDialog = new PropertyTakeOutConfirmDialog(getContext());
        }
        mCondirmDialog.setPropertyNum(mTakeOutNum, mHandlingNum, mRealNum);
        mCondirmDialog.setClickConfirmListener(new PropertyTakeOutConfirmDialog.ClickConfirmListener() {
            @Override
            public void clickConfirm() {
                if (type == 30) {//获取提现手续费
                    doGetShopInfo("2");
                } else if (type == 31) {
                    doGetShopInfo("1");
                } else if (type == 32) {
                    withdraw();
                }
            }
        });
        if (!mCondirmDialog.isShowing()) {
            mCondirmDialog.showBottom();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FROM_WITHDRAW) {
                BankListEntity bankListEntity = (BankListEntity) data.getSerializableExtra(BANK_MESS);
                id = bankListEntity.getId();
                mBinding.tvBankName.setText(bankListEntity.getBank_card());
                mBinding.tvConfirm.setClickable(true);
                mBinding.tvConfirm.setBackgroundColor(getResources().getColor(R.color.red_D81E06));
            } else if (requestCode == ALI_WITHDRAE) { //支付宝
                Bundle pay = data.getBundleExtra("pay");
                name = pay.getString(NAME_WITHDRAW);
                account = pay.getString(ACCOUNT_WITHDRAW);
                mBinding.tvBankNameLeft.setText(name+"    "+account);
                mBinding.tvBankName.setText("");
            } else if (requestCode == WECHAT_WITHDRAE) {//微信
                Bundle pay = data.getBundleExtra("pay");
                name = pay.getString(NAME_WITHDRAW);
                account = pay.getString(ACCOUNT_WITHDRAW);
                mBinding.tvBankNameLeft.setText(name+"    "+account);
                mBinding.tvBankName.setText("");
            }
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
                        mBinding.tvBalance.setText(data.getData().getWithdraw_money());
                        amount = Double.valueOf(data.getData().getWithdraw_money());
//                        mBinding.tvMess.setText(data.getData().getShop_setting());
                        mBinding.tvMess.setText("1.当前提现没有手续费\n2.微信提现：需店主实名认证的微信关注“客商e宝”公众号\n3.银行分账系统上线前，9：00—16：00  提现当天到账  16：00后提现第二天到账");
                    }
                });
    }

    private void withdraw() { //银行卡提现
        RetrofitApiFactory.createApi(MineApi.class)
                .withdraw(String.valueOf(id), mBinding.etNum.getText().toString(), "", "")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("提现成功");
                        finish();
                        AppManager.getInstance().finishActivity(MineWithdrawActivity.class);
                    }
                });
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
                            mBinding.tvBankName.setText(data.getData().get(0).getBank_card());
                        } else {
                            showError("请先添加银行卡");
                            mBinding.tvConfirm.setClickable(false);
                            mBinding.tvConfirm.setBackgroundColor(getResources().getColor(R.color.gray_44));
                        }
                    }
                });
    }

    private void doWithdraw(String id, String type) {//支付宝 微信 提现
        RetrofitApiFactory.createApi(MineApi.class)
                .withdrawAliWechat(id, mBinding.etNum.getText().toString(), type, account, name)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("提现成功");
                        finish();
                        AppManager.getInstance().finishActivity(MineWithdrawActivity.class);
                    }
                });
    }

    private void getFee() {//获取提现手续费
        RetrofitApiFactory.createApi(MineApi.class)
                .getFee("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        mTakeOutNum = mBinding.etNum.getText().toString();
                        mHandlingNum = String.valueOf(Double.valueOf(data.getData() + "") * Double.valueOf(mBinding.etNum.getText().toString()));
                        showConfirmDialog();
                    }
                });
    }

    private void doGetShopInfo(String type) {
        RetrofitApiFactory.createApi(MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        mBinding.setModel(data.getData());
                        if(!type.equals("0")){
                            doWithdraw(data.getData().getId(), type);
                        }
                    }
                });
    }

    private void getAliInfo() {//获取支付宝和微信信息
        RetrofitApiFactory.createApi(MineApi.class)
                .getAliInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<AliWechatEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<List<AliWechatEntity>> data) {
                        // type 30 支付宝 ； 31  微信 ； 32 银行卡
                        for (int i = 0; i < data.getData().size(); i++) {
                            if (type == 30) {
                                if (data.getData().get(i).getType().equals("0")) {
                                    name = data.getData().get(i).getName();
                                    account = data.getData().get(i).getPhone();
                                    mBinding.tvBankNameLeft.setText(name+"    "+account);
                                    mBinding.tvBankName.setText("");
                                }
                            } else if (type == 31) {
                                if (data.getData().get(i).getType().equals("1")) {
                                    name = data.getData().get(i).getName();
                                    account = data.getData().get(i).getPhone();
                                    mBinding.tvBankNameLeft.setText(name+"    "+account);
                                    mBinding.tvBankName.setText("");
                                }
                            }
                        }
                    }
                });
    }
}
