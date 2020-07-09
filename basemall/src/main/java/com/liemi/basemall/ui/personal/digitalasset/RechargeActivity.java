package com.liemi.basemall.ui.personal.digitalasset;

import android.content.Intent;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.user.PublicWalletEntity;
import com.liemi.basemall.databinding.ActivityRechargeBinding;
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
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * 充值页面
 */
public class RechargeActivity extends BaseActivity<ActivityRechargeBinding> {

    private PublicWalletEntity mEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("充值");
    }

    @Override
    protected void initData() {
        doPublicWalletInfo();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_click_copy_phone) {
            if (mEntity != null) {
                KeyboardUtils.putTextIntoClip(this, mEntity.getAddress());
            } else {
                showError("请先登录");
            }
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("takeOutSuccess", true);
        setResult(10003, intent);
        finish();
    }

    //请求公链ETH钱包信息
    private void doPublicWalletInfo(){
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doPublicWalleInfo("defaultData")
                .compose(this.<BaseData<PublicWalletEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<PublicWalletEntity>>compose())
                .subscribe(new XObserver<BaseData<PublicWalletEntity>>() {

                    @Override
                    public void onSuccess(BaseData<PublicWalletEntity> data) {
                        mEntity = data.getData();
                        mBinding.setPhone(data.getData().getAddress());
                        mBinding.setQrCodePic(data.getData().getQrcode());
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
