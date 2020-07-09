package com.netmi.workerbusiness.ui.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.AfterSaleApi;
import com.netmi.workerbusiness.data.api.CouponApi;
import com.netmi.workerbusiness.data.entity.home.AfterSaleDataEntity;
import com.netmi.workerbusiness.databinding.ActivityMerchantLoanBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

public class MerchantLoanActivity extends BaseActivity<ActivityMerchantLoanBinding> {

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_merchant_loan;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("商家贷");
        new InputListenView(mBinding.tvConfirm, mBinding.etShopName, mBinding.etShopId, mBinding.etApplyNum, mBinding.etMonth, mBinding.etPhone) {

        };
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
            loan(mBinding.etShopName.getText().toString(), "", mBinding.etApplyNum.getText().toString()
                    , mBinding.etMonth.getText().toString(), mBinding.etPhone.getText().toString(), mBinding.etShopId.getText().toString());
        }
    }

    private void loan(String shop_name, String shop_username, String hai_num, String pledge_time, String phone, String shop_id) {
        RetrofitApiFactory.createApi(CouponApi.class)
                .loan(shop_name, shop_username, hai_num, pledge_time, phone, shop_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("提交成功");
                        finish();
                    }
                });
    }
}
