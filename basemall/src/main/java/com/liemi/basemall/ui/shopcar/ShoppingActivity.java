package com.liemi.basemall.ui.shopcar;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ActivityShoppingBinding;
import com.netmi.baselibrary.ui.BaseActivity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/25 10:57
 * 修改备注：
 */
public class ShoppingActivity extends BaseActivity<ActivityShoppingBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_shopping;
    }

    @Override
    protected void initUI() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.ll_fragment_shopping, new ShoppingFragment());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }

}
