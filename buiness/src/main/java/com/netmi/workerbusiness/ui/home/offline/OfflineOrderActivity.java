package com.netmi.workerbusiness.ui.home.offline;

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
import com.netmi.workerbusiness.databinding.ActivityOfflineOrderBinding;
import com.netmi.workerbusiness.ui.home.online.LineOrderViewFragment;
import com.netmi.workerbusiness.ui.utils.MyFragmentPageAdapterWithTabs;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class OfflineOrderActivity extends BaseActivity<ActivityOfflineOrderBinding> implements ViewPager.OnPageChangeListener {
    /**
     * 订单状态：0待付款；1待核销；9交易完成；7已经退款 3待评价 4：申请退款中 5： 拒绝退款
     */
    public static final int ALL = 100;
    public static final int PAYMENT = 0;
    public static final int WRITE_OFF = 1;
    public static final int FINISH = 9;
    public static final int REFUND = 7;
    public static final int COMMENT = 3;
    public static final int APPLY_REFUND = 4;
    public static final int REFUSE_REFUND = 5;

    private int type;
    private int currentFragment;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabs = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_offline_order;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("线下订单");
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
        fragments.add(OfflineOrderViewFragment.newInstance(ALL));
        fragments.add(OfflineOrderViewFragment.newInstance(WRITE_OFF));
        fragments.add(OfflineOrderViewFragment.newInstance(COMMENT));
//        fragments.add(OfflineOrderViewFragment.newInstance(APPLY_REFUND));
//        fragments.add(OfflineOrderViewFragment.newInstance(REFUND));
        tabs.add("全部");
        tabs.add("待核销");
        tabs.add("待评价");
//        tabs.add("待退款");
//        tabs.add("已退款");
        mBinding.vpContent.setAdapter(new MyFragmentPageAdapterWithTabs(getSupportFragmentManager(), fragments, tabs));
        mBinding.tab.setViewPager(mBinding.vpContent);
        mBinding.vpContent.addOnPageChangeListener(this);
        mBinding.vpContent.setOffscreenPageLimit(fragments.size());
    }

    @Override
    protected void initData() {
        switch (type) {
            case ALL:
                mBinding.vpContent.setCurrentItem(0);
                break;
            case WRITE_OFF:
                mBinding.vpContent.setCurrentItem(1);
                break;
            case COMMENT:
                mBinding.vpContent.setCurrentItem(2);
                break;
            case REFUND:
                mBinding.vpContent.setCurrentItem(3);
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
