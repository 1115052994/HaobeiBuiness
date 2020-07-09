package com.liemi.basemall.ui.personal.order;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ActivityMineOrderBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.ui.BaseActivity;

import java.util.ArrayList;

import static com.netmi.baselibrary.data.Constant.ORDER_STATE;
import static com.netmi.baselibrary.router.BaseRouter.App_MineOrderActivity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/25 9:52
 * 修改备注：
 */
@Route(path = App_MineOrderActivity)
public class MineOrderActivity extends BaseActivity<ActivityMineOrderBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_order;
    }


    @Override
    protected void initUI() {
        getTvTitle().setText("我的订单");

        final ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(OrderModuleFragment.newInstance(-1));
        fragmentList.add(OrderModuleFragment.newInstance(Constant.ORDER_WAIT_PAY));
        fragmentList.add(OrderModuleFragment.newInstance(Constant.ORDER_WAIT_SEND));
        fragmentList.add(OrderModuleFragment.newInstance(Constant.ORDER_WAIT_RECEIVE));
        fragmentList.add(OrderModuleFragment.newInstance(Constant.ORDER_WAIT_COMMENT));
        mBinding.tlGroup.setViewPager(mBinding.vpGroup,
                new String[]{"全部", "待付款", "待发货", "待收货", "待评价"}, this, fragmentList);
        mBinding.tlGroup.setCurrentTab(getIntent().getIntExtra(ORDER_STATE, -1) + 1);
        mBinding.vpGroup.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //解决Fragment中使用EventBus的通知RecyclerView的adapter.notifyDataSetChanged只有拖动才刷新的问题
                View view = fragmentList.get(position).getView();
                if (view != null) {
                    view.requestLayout();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void initData() {

    }
}
