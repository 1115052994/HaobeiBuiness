package com.netmi.workerbusiness.ui.home.online;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.event.LineCommoditySearchEvent;
import com.netmi.workerbusiness.databinding.ActivityLineOrderBinding;
import com.netmi.workerbusiness.ui.utils.MyFragmentPageAdapterWithTabs;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class LineOrderActivity extends BaseActivity<ActivityLineOrderBinding> implements ViewPager.OnPageChangeListener {
    /**
     * 订单状态 默认全部 ，0 待付款，1待发货，2待收货，3待评价
     */
    public static final int ALL = 100;
    public static final int PAYMENT = 0;
    public static final int SHIP = 1;
    public static final int RECEIPT = 2;
    public static final int EVALUATE = 3;

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
        getTvTitle().setText("线上订单");
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
        fragments.add(LineOrderViewFragment.newInstance(ALL));
        fragments.add(LineOrderViewFragment.newInstance(PAYMENT));
        fragments.add(LineOrderViewFragment.newInstance(SHIP));
        fragments.add(LineOrderViewFragment.newInstance(RECEIPT));
        fragments.add(LineOrderViewFragment.newInstance(EVALUATE));
        tabs.add("全部");
        tabs.add("待付款");
        tabs.add("待发货");
        tabs.add("待收货");
        tabs.add("待评价");
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
            case PAYMENT:
                mBinding.vpContent.setCurrentItem(1);
                break;
            case SHIP:
                mBinding.vpContent.setCurrentItem(2);
                break;
            case RECEIPT:
                mBinding.vpContent.setCurrentItem(3);
                break;
            case EVALUATE:
                mBinding.vpContent.setCurrentItem(4);
                break;
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
