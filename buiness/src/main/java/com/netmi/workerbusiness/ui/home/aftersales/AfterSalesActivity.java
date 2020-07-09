package com.netmi.workerbusiness.ui.home.aftersales;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.event.LineCommoditySearchEvent;
import com.netmi.workerbusiness.databinding.ActivityLineOrderBinding;
import com.netmi.workerbusiness.ui.home.online.LineOrderViewFragment;
import com.netmi.workerbusiness.ui.utils.MyFragmentPageAdapterWithTabs;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class AfterSalesActivity extends BaseActivity<ActivityLineOrderBinding> implements ViewPager.OnPageChangeListener {

    /**
     * 类型 不填 全部 1：退款 2：退款退货   订单状态 默认全部
     */

    public static final int ALL = 100;
    public static final int REFUND = 1;
    public static final int RETURN = 2;
    public static final int FINISH = 100;


    private int type;
    private int currentFragment;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabs = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_line_order;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("售后订单");
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        mBinding.etOrderNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 发送广播
                EventBus.getDefault().post(new LineCommoditySearchEvent(mBinding.etOrderNum.getText().toString()));
            }
        });

        initViewPager();
    }

    private void initViewPager() {
        fragments.add(AfterSaleFragment.newInstance(ALL));
        fragments.add(AfterSaleFragment.newInstance(REFUND));
        fragments.add(AfterSaleFragment.newInstance(RETURN));

//        fragments.add(LineOrderViewFragment.newInstance(FINISH));

        tabs.add("全部");
        tabs.add("退款");
        tabs.add("退货");
//        tabs.add("已完成");

        mBinding.vpContent.setAdapter(new MyFragmentPageAdapterWithTabs(getSupportFragmentManager(), fragments, tabs));
        mBinding.tab.setViewPager(mBinding.vpContent);
        mBinding.vpContent.addOnPageChangeListener(this);
        mBinding.vpContent.setOffscreenPageLimit(fragments.size());
    }

    @Override
    protected void initDefault() {
        fullScreen(true);
    }

    @Override
    protected void initData() {
        switch (type) {
            case ALL:
                mBinding.vpContent.setCurrentItem(0);
                break;
            case REFUND:
                mBinding.vpContent.setCurrentItem(1);
                break;
            case RETURN:
                mBinding.vpContent.setCurrentItem(2);
                break;
//            case FINISH:
//                mBinding.vpContent.setCurrentItem(3);
//                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentFragment = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
