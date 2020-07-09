package com.netmi.workerbusiness.ui.mine.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ImageUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.ScreenUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.api.WalletApi;
import com.netmi.workerbusiness.data.cache.HeadUrlCache;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.ActivityHaibeiRechargeBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class HaibeiRechargeActivity extends BaseActivity<ActivityHaibeiRechargeBinding> {

    private int type;// 20 海贝充值 ; 21 海贝收款

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_haibei_recharge;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {

        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        if (type == 20) {
            getTvTitle().setText("充入");
        } else if (type == 21) {
            getTvTitle().setText("接收");
        }

    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        doGetShopInfo();
        getWalletInfo();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_copy) {  //复制地址
            KeyboardUtils.putTextIntoClip(getContext(), mBinding.tvAddress.getText().toString());
            showError(ResourceUtil.getString(R.string.copy_success));
        } else if (view.getId() == R.id.tv_save) { //保存至相册
            //保存图片到本地
            ImageUtils.saveBmp2Gallery(ScreenUtils.screenShot(getActivity()), getResources().getString(R.string.app_name), getContext());
        }
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
                        mBinding.setName(data.getData().getName());
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
                        if (type == 20) { // 20 海贝充值 ; 21 海贝收款
                            mBinding.setCode(data.getData().getAddress_qrcode());
                            mBinding.setAddress(data.getData().getAddress());
                            mBinding.tvContent.setText(data.getData().getRecharge_remark());
                        } else if (type == 21) {
                            mBinding.setCode(data.getData().getShare_code_qrcode());
                            mBinding.setAddress("ID:" + data.getData().getShare_code());
                            mBinding.tvContent.setText(data.getData().getReceive_remark());
                        }

                    }
                });
    }


}
