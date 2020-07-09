package com.netmi.workerbusiness.ui.home.commodity.online;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.event.LineCommoditySearchEvent;
import com.netmi.workerbusiness.databinding.ActivityLineCommodityListBinding;
import com.netmi.workerbusiness.ui.utils.MyFragmentPageAdapterWithTabs;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.CREATE_GOOD_FROM;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.SHOP_ID;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.STORE_RESULT;

public class LineCommodityListActivity extends BaseActivity<ActivityLineCommodityListBinding> implements ViewPager.OnPageChangeListener {
    /**
     * 商品状态： 5:已上架  2:待审核  7:已下架
     */
    public static final int ON_SHELF = 5;
    public static final int WAIT_CHECK = 2;
    public static final int DOWN_SHELF = 7;

    private int type = 1;
    private int currentFragment;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabs = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_line_commodity_list;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("线上商品");
        getRightSetting().setText("创建商品");
        getRightSetting().setTextColor(getResources().getColor(R.color.ff333333));

        mBinding.etCommodity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 发送广播
                EventBus.getDefault().post(new LineCommoditySearchEvent(mBinding.etCommodity.getText().toString()));
            }
        });
        initViewPager();
    }

    private void initViewPager() {
        fragments.add(LineCommodityListFragment.newInstance(ON_SHELF));
        fragments.add(LineCommodityListFragment.newInstance(WAIT_CHECK));
        fragments.add(LineCommodityListFragment.newInstance(DOWN_SHELF));
        tabs.add("已上架");
        tabs.add("待审核");
        tabs.add("已下架");
        mBinding.vpContent.setAdapter(new MyFragmentPageAdapterWithTabs(getSupportFragmentManager(), fragments, tabs));
        mBinding.tab.setViewPager(mBinding.vpContent);
        mBinding.vpContent.addOnPageChangeListener(this);
        mBinding.vpContent.setOffscreenPageLimit(fragments.size());
    }

    @Override
    protected void initData() {
        switch (type) {
            case ON_SHELF:
                mBinding.vpContent.setCurrentItem(0);
                break;
            case WAIT_CHECK:
                mBinding.vpContent.setCurrentItem(1);
                break;
            case DOWN_SHELF:
                mBinding.vpContent.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_setting) {
            Bundle bundle = new Bundle();
            //TYPE表示从哪个页面进入 1表示创建商品 2表示编辑商品
            bundle.putInt(JumpUtil.TYPE, 1);
            bundle.putString(CREATE_GOOD_FROM, STORE_RESULT);
            bundle.putString(SHOP_ID, getIntent().getStringExtra(SHOP_ID));
            JumpUtil.overlay(getContext(), CreateCommodityActivity.class, bundle);
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
