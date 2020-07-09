package com.netmi.workerbusiness.ui.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.event.LocationEvent;
import com.netmi.workerbusiness.data.event.TeamEvent;
import com.netmi.workerbusiness.databinding.ActivityTeamBinding;
import com.netmi.workerbusiness.ui.home.offline.OfflineOrderViewFragment;
import com.netmi.workerbusiness.ui.utils.MyFragmentPageAdapterWithTabs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class TeamActivity extends BaseActivity<ActivityTeamBinding> implements ViewPager.OnPageChangeListener {
    private static final String UID = "uid";
    private String uid;
    /**
     * 推广用户 1 ，推广店铺 2
     */
    public static final int USER = 1;
    public static final int SHOP = 2;
    private int type;
    private int currentFragment;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabs = new ArrayList<>();

    public static void start(Context context, String uid) {
        if (!TextUtils.isEmpty(uid)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(UID, uid);
            JumpUtil.overlay(context, TeamActivity.class, bundle);
        } else {
            ToastUtils.showShort("没有数据");
        }
    }

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_team;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("团队管理");
        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable(UID) != null) {
            uid = (String) getIntent().getExtras().getSerializable(UID);
        }
        initViewPager();
    }

    private void initViewPager() {
        fragments.add(TeamFragment.newInstance(USER, uid));
        fragments.add(TeamFragment.newInstance(SHOP, uid));

        tabs.add("推广用户");
        tabs.add("推广店铺");

        mBinding.vpContent.setAdapter(new MyFragmentPageAdapterWithTabs(getSupportFragmentManager(), fragments, tabs));
        mBinding.tab.setViewPager(mBinding.vpContent);
        mBinding.vpContent.addOnPageChangeListener(this);
        mBinding.vpContent.setOffscreenPageLimit(fragments.size());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void location(TeamEvent event) {
        uid = event.getUid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        switch (type) {
            case USER:
                mBinding.vpContent.setCurrentItem(0);
                break;
            case SHOP:
                mBinding.vpContent.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        currentFragment = position;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
