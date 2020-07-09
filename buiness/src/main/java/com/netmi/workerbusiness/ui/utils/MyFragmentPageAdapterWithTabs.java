package com.netmi.workerbusiness.ui.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/8/30
 * 修改备注：
 */
public class MyFragmentPageAdapterWithTabs extends FragmentPagerAdapter {
    private List<Fragment> list;
    private List<String> tabs;

    public MyFragmentPageAdapterWithTabs(FragmentManager fm, List<Fragment> list, List<String> tabs) {
        super(fm);
        this.list = list;
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    //TabLayout的标题的显示
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position % tabs.size());
    }
}
