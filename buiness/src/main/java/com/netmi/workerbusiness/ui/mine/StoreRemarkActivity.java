package com.netmi.workerbusiness.ui.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.data.event.StoreRemarkEvent;
import com.netmi.workerbusiness.databinding.ActivityStoreRemarkBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import static com.liemi.basemall.ui.personal.address.AddressManageActivity.ADDRESS_ENTITY;
import static com.netmi.baselibrary.data.Constant.SUCCESS_CODE;
import static com.netmi.workerbusiness.ui.mine.StoreInfoActivity.REQUEST_CHANGE_REMARK;

public class StoreRemarkActivity extends BaseActivity<ActivityStoreRemarkBinding> {

    public static final String STORE_REMARK = "store_remark";
    private ShopInfoEntity entity;

    @Override
    protected int getContentView() {
        return R.layout.activity_store_remark;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("商家简介");
        getRightSetting().setText("完成");
        getRightSetting().setTextColor(getResources().getColor(R.color.black));

        entity = (ShopInfoEntity) getIntent().getExtras().getSerializable(JumpUtil.VALUE);
        if (entity != null) {
            mBinding.etRemark.setText(entity.getRemark());
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_setting) {
            EventBus.getDefault().post(new StoreRemarkEvent(mBinding.etRemark.getText().toString()));
            finish();
//            doUpdateShopInfo(Integer.valueOf(entity.getId()), entity.getLogo_url(), entity.getName(), mBinding.etRemark.getText().toString(), entity.getOpening_hours(), entity.getLongitude(), entity.getLatitude()
//                    , entity.getP_name(), entity.getC_name(), entity.getD_name(), entity.getAddress(), entity.getImg_url());
        }
    }

    /**
     * 更新用户信息
     */
    private void doUpdateShopInfo(Integer id, String logo_url, String name, String remark, String opening_hours, String longitude, String latitude,
                                  String p_name, String c_name, String d_name, String address, String img_url
    ) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUpdateShopInfo(id, logo_url, name, remark, opening_hours, longitude, latitude, p_name, c_name, d_name, address, img_url)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData baseData) {
                        EventBus.getDefault().post(new StoreRemarkEvent(mBinding.etRemark.getText().toString()));
                        finish();
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onFail(BaseData data) {
                        super.onFail(data);
                        hideProgress();
                    }
                });
    }

}
