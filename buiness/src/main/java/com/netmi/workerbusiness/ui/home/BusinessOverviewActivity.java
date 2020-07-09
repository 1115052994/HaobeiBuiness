package com.netmi.workerbusiness.ui.home;

import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.LoginApi;
import com.netmi.workerbusiness.data.entity.home.BusinessOverviewEntity;
import com.netmi.workerbusiness.databinding.ActivityBusinessOverviewBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class BusinessOverviewActivity extends BaseActivity<ActivityBusinessOverviewBinding> {
    //用户选择商户类型  1:线上 2:线下 3:线上+线下
    private int shop_user_type;

    @Override
    protected int getContentView() {
        return R.layout.activity_business_overview;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("经营概况");
        shop_user_type = getIntent().getExtras().getInt(JumpUtil.TYPE);
    }

    @Override
    protected void initData() {
        doGetBusiness();
    }

    private void doGetBusiness() {
        RetrofitApiFactory.createApi(LoginApi.class)
                .businessOverview("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<BusinessOverviewEntity>>() {
                    @Override
                    public void onSuccess(BaseData<BusinessOverviewEntity> data) {
                        mBinding.setModel(data.getData());
                        if (shop_user_type == 1) {
                            mBinding.llOutlineOrder.setVisibility(View.GONE);
                            mBinding.llOutlineCommodity.setVisibility(View.GONE);
                        } else if (shop_user_type == 2) {
                            mBinding.llLineOrder.setVisibility(View.GONE);
                            mBinding.llLineCommodity.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
