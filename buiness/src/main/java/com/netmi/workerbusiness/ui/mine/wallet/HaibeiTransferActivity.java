package com.netmi.workerbusiness.ui.mine.wallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.ui.personal.digitalasset.PropertyTakeOutInputPasswordDialog;
import com.liemi.basemall.ui.personal.digitalasset.QRCodeScanActivity;
import com.liemi.basemall.widget.ConfirmBottomDialog;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.WalletApi;
import com.netmi.workerbusiness.data.entity.walllet.WalletEntity;
import com.netmi.workerbusiness.databinding.ActivityHaibeiTransferBinding;
import com.netmi.workerbusiness.ui.mine.PassChooseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.netmi.baselibrary.utils.JumpUtil.TYPE;
import static com.netmi.workerbusiness.ui.mine.AccountSecurityActivity.TRANSFER_PASS;

public class HaibeiTransferActivity extends BaseActivity<ActivityHaibeiTransferBinding> {

    private String rate;

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_haibei_transfer;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {

    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        getWalletInfo();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_confirm) {
            check();
        } else if (view.getId() == R.id.tv_setting) {
            JumpUtil.startForResult(this, QRCodeScanActivity.class, 1002, null);
        }
    }

    private void check() {
        String phone = mBinding.etId.getText().toString();
        String num = mBinding.etNum.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("请输入对方ID");
        } else if (TextUtils.isEmpty(num)) {
            ToastUtils.showShort("请输入需要发送数目");
        } else {
            aTokenReceive = String.valueOf(Double.valueOf(mBinding.etNum.getText().toString()) * Double.valueOf(rate));
            showConfirmDialog(mBinding.etNum.getText().toString(), phone);
        }
    }


    /**
     * 确认弹窗
     */
    private ConfirmBottomDialog confirmBottomDialog;
    String aTokenValue;
    String aTokenReceive;
    String aTokenReceiveSeed;

    private void showConfirmDialog(String num, String id) {
        if (confirmBottomDialog == null) {
            confirmBottomDialog = new ConfirmBottomDialog(getContext());
        }
        confirmBottomDialog.buttonsTitle(R.string.other_amout, R.string.my_fee, R.string.other_id)//每行显示的标题
                .buttonsContent(num, aTokenReceive, id)//可以不传入参数，但是该方法必须调用，否则buttonsTitle（）失效
//                .clickListener(null)//item点击事件
                .titleText(R.string.confirm_send)//标题
                .confirmText(R.string.confirm_1)//下方按钮
                .confirmClick(string -> showInputPasswordDialog(num))//下方按钮点击事件,默认会关闭dialog，不需要多此一举confirmBottomDialog.dismiss();.showBottom();
                .showBottom();

    }


    /**
     * 密码和验证码弹窗
     */
    private PropertyTakeOutInputPasswordDialog mInputPasswordDialog;

    private void showInputPasswordDialog(String num) {
        if (UserInfoCache.get().getIs_set_paypassword() != UserInfoEntity.BIND) {
            showError("请先设置资产密码");
            JumpUtil.overlay(getContext(), SetPayPassActivity.class);
            return;
        }
        if (mInputPasswordDialog == null) {
            mInputPasswordDialog = new PropertyTakeOutInputPasswordDialog(getContext());
        }
        if (!mInputPasswordDialog.isShowing()) {
            mInputPasswordDialog.showBottom();
        }
        mInputPasswordDialog.setClickNextStepListener((password, verify_code) -> {
            mInputPasswordDialog.dismiss();
            doTransfer(password, verify_code);
        });
        mInputPasswordDialog.setClickVerifyCodeListener(() -> {
            //获取验证码
//            doAuthCode("inside");
        });

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
            mBinding.etId.setText(scanResult);
            mBinding.etId.setSelection(scanResult.length());
        }
    }


    //提取
    private void doTransfer(String password, String verify_code) {
        RetrofitApiFactory.createApi(WalletApi.class)
                .transfer(mBinding.etId.getText().toString()
                        , mBinding.etNum.getText().toString(), MD5.GetMD5Code(password, true))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    public void onSuccess(BaseData data) {
                        Bundle bundle = new Bundle();
                        bundle.putString(TYPE, "转赠");
                        JumpUtil.overlay(getContext(), WalletResultActivity.class, bundle);
                        finish();
                    }

                });
    }


    private void getWalletInfo() {
        RetrofitApiFactory.createApi(WalletApi.class)
                .getWalletInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<WalletEntity>>() {
                    @Override
                    public void onSuccess(BaseData<com.netmi.workerbusiness.data.entity.walllet.WalletEntity> data) {
                        rate = data.getData().getTransfer_fee_rate();
                        mBinding.tvRemark.setText(data.getData().getTransfer_remark());
                    }
                });
    }


}
