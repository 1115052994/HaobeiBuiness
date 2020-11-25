package com.netmi.workerbusiness.ui.home.haibei;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.haibei.HaibeiBonus;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.ActivityHaiBeiRedeemCodeBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class HaiBeiRedeemCodeActivity extends BaseActivity<ActivityHaiBeiRedeemCodeBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_hai_bei_redeem_code;
    }

    @Override
    protected void initUI() {
        mBinding.tvTitle.setText("海贝兑换码");
        TextView rightSetting = getRightSetting();
        rightSetting.setText("订单明细");
        rightSetting.setTextColor(Color.parseColor("#108EE9"));
        rightSetting.setTextSize(14);
        doGetShopInfo();
        doGetUserInfo();
        doGetCode();

    }



    @Override
    protected void initData() {
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        finish();

    }
    private void doGetCode() {
        RetrofitApiFactory.createApi(MineApi.class)
                .haibeiQrCode("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<HaibeiBonus>>() {
                    @Override
                    public void onSuccess(BaseData<HaibeiBonus> data) {
                        mBinding.setCode(data.getData().getHaibei_qrcode());
                    }
                });
    }

    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        mBinding.setLogoUrl(data.getData().getLogo_url());
                    }
                });
    }

    private void doGetUserInfo() {
        RetrofitApiFactory.createApi(com.liemi.basemall.data.api.MineApi.class)
                .getUserInfo(0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        mBinding.setId("ID:" + data.getData().getShare_code());
                    }
                });
    }




}
