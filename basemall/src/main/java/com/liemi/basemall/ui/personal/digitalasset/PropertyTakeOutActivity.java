package com.liemi.basemall.ui.personal.digitalasset;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.LoginApi;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.MinePropertyEntity;
import com.liemi.basemall.data.entity.user.CoinEntity;
import com.liemi.basemall.data.entity.user.HandlingChargeEntity;
import com.liemi.basemall.databinding.ActivityPropertyTakeOutBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 *
 * 提取页面
 */
public class PropertyTakeOutActivity extends BaseActivity<ActivityPropertyTakeOutBinding> {
    //需要上个页面传递个人文旅数字通行证信息
    public static final String MINE_PROPERTY_INFO = "minePropertyInfo";

    //用户输入的信息
    private String mAddress;
    private String mTakeOutNum;
    private String mPassword;

    private CoinEntity mCoinEntity;
    private MinePropertyEntity minePropertyEntity;
    //提取文旅数字通行证确认dialog
    private PropertyTakeOutConfirmDialog confirmDialog;
    //提取文旅数字通行证输入密码dialog
    private PropertyTakeOutInputPasswordDialog inputPasswordDialog;


    @Override
    protected int getContentView() {
        return R.layout.activity_property_take_out;
    }

    @Override
    protected void initUI() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            mCoinEntity = (CoinEntity) getIntent().getExtras().getSerializable(MINE_PROPERTY_INFO);
        }
        getTvTitle().setText("提取");
        mBinding.etInputTakeOutNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String takeOutNum = s.toString();
                if (takeOutNum.contains(".")) {
                    //取出后面的数据
                    String decimals = takeOutNum.substring(takeOutNum.indexOf(".") + 1, takeOutNum.length());
                    if (!TextUtils.isEmpty(decimals) && decimals.length() > 8) {
                        takeOutNum = takeOutNum.substring(0, takeOutNum.length() - (decimals.length() - 8));
                        showError("小数点后最多八位");
                        mBinding.etInputTakeOutNum.setText(takeOutNum);
                        mBinding.etInputTakeOutNum.setSelection(takeOutNum.length());
                    }
                }
            }

        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.btn_save) {
            if (checkInputInfo()) {
                doHandlingCharge(mBinding.etInputWalletAddress.getText().toString(),
                        mBinding.etInputTakeOutNum.getText().toString());

            }
        }

        if (view.getId() == R.id.tv_all) {
            if (mCoinEntity != null) {
                mBinding.etInputTakeOutNum.setText(mCoinEntity.getUsedNum());
            }
        }


        if (view.getId() == R.id.iv_scan_qrcode) {
            //点击扫描二维码
            JumpUtil.startForResult(this, QRCodeScanActivity.class, 1002, null);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && data != null) {
            if (data.getBooleanExtra("result", true)) {
                Intent intent = new Intent();
                intent.putExtra("takeOutSuccess", true);
                setResult(10001, intent);
                finish();
            }
        } else if (requestCode == 1002 && resultCode == 10002 && data != null) {
            String scanResult = data.getStringExtra("scan_result");
            mBinding.etInputWalletAddress.setText(scanResult);
        }
    }

    //查看用户输入的数据是否合法
    private boolean checkInputInfo() {
        mAddress = mBinding.etInputWalletAddress.getText().toString();
        mTakeOutNum = mBinding.etInputTakeOutNum.getText().toString();
        if (TextUtils.isEmpty(mAddress)) {
            showError("请输入以太坊钱包地址");
            return false;
        }
        if (TextUtils.isEmpty(mTakeOutNum)) {
            showError("请输入要提取的数目");
            return false;
        }
        return true;
    }

    //显示用户确认的dialog
    private void showConfirmDialog(String count,String hand,String real) {
        if (confirmDialog == null) {
            confirmDialog = new PropertyTakeOutConfirmDialog(this);
        }
        if (!confirmDialog.isShowing()) {
            confirmDialog.showBottom();
        }
        confirmDialog.setPropertyNum(count,hand,real);
        confirmDialog.setClickConfirmListener(new PropertyTakeOutConfirmDialog.ClickConfirmListener() {
            @Override
            public void clickConfirm() {
                confirmDialog.dismiss();
                //用户点击确认，显示让用户输入密码的dialog
                showInputPasswordDialog();
            }
        });
    }

    //显示让用户输入密码的dialog
    private void showInputPasswordDialog() {
        if (inputPasswordDialog == null) {
            inputPasswordDialog = new PropertyTakeOutInputPasswordDialog(this);
        }
        if (!inputPasswordDialog.isShowing()) {
            inputPasswordDialog.showBottom();
        }
        inputPasswordDialog.setClickNextStepListener(new PropertyTakeOutInputPasswordDialog.ClickNextStepListener() {
            @Override
            public void clickNextStep(String password,String verify_code) {
                //点击下一步，请求提取的接口
                inputPasswordDialog.dismiss();
                mPassword = password;
                doTakeOut();
            }
        });
        inputPasswordDialog.setClickVerifyCodeListener(new PropertyTakeOutInputPasswordDialog.ClickVerifyCodeListener(){
            @Override
            public void clickVerifyCode() {
                //获取验证码
                doAuthCode("pay");
            }
        });
    }

    //获取验证码
    private void doAuthCode(String type){
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doAuthCode(UserInfoCache.get().getPhone(),type)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError(data.getErrmsg());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        hideProgress();
                    }
                });
    }


    //查询手续费
    private void doHandlingCharge(String address,String account){
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doHandlingCharge(address,account)
                .compose(this.<BaseData<HandlingChargeEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<HandlingChargeEntity>>compose())
                .subscribe(new XObserver<BaseData<HandlingChargeEntity>>() {
                    @Override
                    public void onSuccess(BaseData<HandlingChargeEntity> data) {
                        showConfirmDialog(data.getData().getAmount(),data.getData().getFee(),
                                data.getData().getAmount_end());
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

    //请求提取的接口
    private void doTakeOut() {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doTakeOut( mAddress, mTakeOutNum,MD5.GetMD5Code(mPassword, true))
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("提取成功");
                        //转出成功
                        Bundle bundle = new Bundle();
                        bundle.putInt(PropertyTakeOutResultActivity.SUCCESS_TYPE,PropertyTakeOutResultActivity.TAKE_OUT_SUCCESS);
                        JumpUtil.startForResult(PropertyTakeOutActivity.this,PropertyTakeOutResultActivity.class,1001,bundle);
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
