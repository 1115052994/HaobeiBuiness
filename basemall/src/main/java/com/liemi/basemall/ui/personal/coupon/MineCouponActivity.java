package com.liemi.basemall.ui.personal.coupon;


import android.support.v4.app.Fragment;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ActivityMineCouponBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import java.util.ArrayList;

public class MineCouponActivity extends BaseActivity<ActivityMineCouponBinding> {


    private String[] mTitles = new String[3];
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_coupon;
    }

    @Override
    protected void initUI() {
        mTitles[0] = "未使用";
        mTitles[1] = "已使用";
        mTitles[2] = "已失效";
        fragmentList.add(MineCouponFragment.newInstance(MineCouponFragment.COUPON_STATUS_NOT_USED));
        fragmentList.add(MineCouponFragment.newInstance(MineCouponFragment.COUPON_STATUS_TIMED));
        fragmentList.add(MineCouponFragment.newInstance(MineCouponFragment.COUPON_STATUS_TIMED));

        mBinding.tlTitle.setViewPager(mBinding.vpContent, mTitles, getActivity(), fragmentList);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);

        if (view.getId() == R.id.iv_close) {
            onBackPressed();
            return;
        }

    }
}
