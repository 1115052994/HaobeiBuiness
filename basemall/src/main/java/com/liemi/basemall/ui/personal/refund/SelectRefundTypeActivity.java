package com.liemi.basemall.ui.personal.refund;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.databinding.ActivitySelectRefundTypeBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;

import static com.liemi.basemall.ui.personal.order.OrderDetailActivity.ORDER_ENTITY;
import static com.liemi.basemall.ui.personal.refund.ApplyRefundActivity.REFUND_POSITION;

public class SelectRefundTypeActivity extends BaseActivity<ActivitySelectRefundTypeBinding> {

    private int position;
    private OrderDetailsEntity orderDetailsEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_select_refund_type;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("选择服务类型");
    }

    @Override
    protected void initData() {
        position = getIntent().getIntExtra(REFUND_POSITION, 0);
        orderDetailsEntity = (OrderDetailsEntity) getIntent().getSerializableExtra(ORDER_ENTITY);

        if (orderDetailsEntity == null) {
            ToastUtils.showShort("没有订单数据");
            finish();
            return;
        }
        mBinding.setItem(orderDetailsEntity.getMeOrders().get(position));
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();

        Bundle bundle = new Bundle();
        if (i == R.id.rl_refund) {
            orderDetailsEntity.setRefundStatus(1); //1:退款退货-仅退款 2:退款退货-退款退货
            orderDetailsEntity.setStatus(1);
            for (OrderDetailsEntity.MeOrdersBean meOrdersBean : orderDetailsEntity.getMeOrders()) {
                meOrdersBean.setStatus(1);
            }
        } else if (i == R.id.rl_returnable) { //1:退款退货-仅退款 2:退款退货-退款退货
            orderDetailsEntity.setRefundStatus(2);
            orderDetailsEntity.setStatus(2);
            for (OrderDetailsEntity.MeOrdersBean meOrdersBean : orderDetailsEntity.getMeOrders()) {
                meOrdersBean.setStatus(2);
            }
        }

        bundle.putSerializable(ORDER_ENTITY, orderDetailsEntity);
        bundle.putInt(REFUND_POSITION, position);
        JumpUtil.overlay(getContext(), ApplyRefundActivity.class, bundle);
    }
}
