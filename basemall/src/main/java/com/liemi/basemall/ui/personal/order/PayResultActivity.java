package com.liemi.basemall.ui.personal.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.databinding.ActivityPayResultBinding;
import com.liemi.basemall.ui.store.good.GoodDetailActivity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;

import static com.liemi.basemall.ui.personal.order.OrderDetailActivity.ORDER_ENTITY;

public class PayResultActivity extends BaseActivity<ActivityPayResultBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("下单成功");
        MApplication.getInstance().appManager.finishActivity(GoodDetailActivity.class);
    }

    @Override
    protected void initData() {
        OrderDetailsEntity detailsEntity=(OrderDetailsEntity) getIntent().getSerializableExtra(ORDER_ENTITY);
        mBinding.setItem(detailsEntity);
        String ethPrice=getIntent().getStringExtra("ethPrice");
        mBinding.tvPrice.setText(detailsEntity.getPrice_total());

    }

    public void doClick(View view){
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_read_order) {
            MApplication.getInstance().appManager.finishActivity(MineOrderActivity.class);
            JumpUtil.overlay(this, MineOrderActivity.class);
            finish();

        } else if (i == R.id.tv_back_home) {
            finish();

        }
    }
}
