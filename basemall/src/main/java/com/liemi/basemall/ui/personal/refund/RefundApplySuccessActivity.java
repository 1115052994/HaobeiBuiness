package com.liemi.basemall.ui.personal.refund;

import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.event.OrderRefreshEvent;
import com.liemi.basemall.data.event.OrderRefundEvent;
import com.liemi.basemall.databinding.ActivityRefundApplySuccessBinding;
import com.netmi.baselibrary.ui.BaseActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/9/9 16:57
 * 修改备注：
 */
public class RefundApplySuccessActivity extends BaseActivity<ActivityRefundApplySuccessBinding> {

    public static final String REFUND_TIP = "refundTip";

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_apply_success;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("提交申请");
        String tip = getIntent().getStringExtra(REFUND_TIP);
        if(!TextUtils.isEmpty(tip)) {
            mBinding.tvTip.setText(tip);
        }

        EventBus.getDefault().post(new OrderRefundEvent());
        EventBus.getDefault().post(new OrderRefreshEvent());
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_confirm) {
            finish();

        } else {
        }
    }
}
