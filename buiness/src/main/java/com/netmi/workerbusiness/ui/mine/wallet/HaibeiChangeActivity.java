package com.netmi.workerbusiness.ui.mine.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.data.entity.mine.WalletEntity;
import com.netmi.workerbusiness.databinding.ActivityHaibeiChangeBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class HaibeiChangeActivity extends BaseActivity<ActivityHaibeiChangeBinding> {
    private String ratio;

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_haibei_change;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("转换");
        ratio = getIntent().getExtras().getString(JumpUtil.VALUE);

        mBinding.etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mBinding.tvRmb.setText("≈" + String.valueOf(Strings.toDouble(mBinding.etNum.getText().toString()) * Strings.toDouble(ratio)) + "元");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
            if (mBinding.etNum.getText().toString().isEmpty()) {
                showError("请输入需要转换的海贝额度");
            } else {
                doGetShopInfo();
            }
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
                        haibeiChange(data.getData().getId());
                    }
                });
    }

    private void haibeiChange(String id) {
        RetrofitApiFactory.createApi(MineApi.class)
                .haibeiChange(mBinding.etNum.getText().toString(), id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        finish();
                    }
                });
    }


}
