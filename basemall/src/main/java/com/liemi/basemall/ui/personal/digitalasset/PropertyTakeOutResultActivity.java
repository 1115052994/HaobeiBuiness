package com.liemi.basemall.ui.personal.digitalasset;

import android.content.Intent;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ActivityPropertyTakeOutResultBinding;
import com.netmi.baselibrary.ui.BaseActivity;

public class PropertyTakeOutResultActivity extends BaseActivity<ActivityPropertyTakeOutResultBinding> {
    //需要上个页面传递过来类型参数
    public static final int TAKE_OUT_SUCCESS = 0;//提取成功
    public static final int SEND_SUCCESS = 1;//发送成功
    public static final int SELL_SUCCESS = 2;//出售成功
    public static final int BUY_SUCCESS = 3;//购买成功

    public static final String SUCCESS_TYPE = "SUCCESS_TYPE";

    @Override
    protected int getContentView() {
        return R.layout.activity_property_take_out_result;
    }

    @Override
    protected void initUI() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().getInt(SUCCESS_TYPE, TAKE_OUT_SUCCESS) == TAKE_OUT_SUCCESS) {
                getTvTitle().setText("提取结果");
                mBinding.setSuccessStr("提取成功");
            } else if (getIntent().getExtras().getInt(SUCCESS_TYPE, TAKE_OUT_SUCCESS) == SEND_SUCCESS) {
                getTvTitle().setText("转赠结果");
                mBinding.setSuccessStr("转赠成功");
            } else if (getIntent().getExtras().getInt(SUCCESS_TYPE, TAKE_OUT_SUCCESS) == SELL_SUCCESS) {
                getTvTitle().setText("出售结果");
                mBinding.setSuccessStr("出售成功");
            } else if (getIntent().getExtras().getInt(SUCCESS_TYPE, TAKE_OUT_SUCCESS) == BUY_SUCCESS) {
                getTvTitle().setText("购买结果");
                mBinding.setSuccessStr("购买成功");
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_finish) {
            Intent intent = new Intent();
            intent.putExtra("result", true);
            setResult(10001, intent);
            finish();
        }
    }
}
