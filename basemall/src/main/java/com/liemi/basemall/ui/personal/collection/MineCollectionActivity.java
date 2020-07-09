package com.liemi.basemall.ui.personal.collection;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.databinding.ActivityMineCollectionBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

public class MineCollectionActivity extends BaseActivity<ActivityMineCollectionBinding> implements MineCollectionGoodsFragment.CollcetionSelectAllListener {

    private MineCollectionGoodsFragment mGoodsFragment;
    private MineCollectionStoreFragment mStoreFragment;
    private String[] mTitleList;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_collection;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("我的收藏");
        getRightSetting().setText("编辑");
        getRightSetting().setTextColor(Color.parseColor("#84929e"));
        getRightSetting().setTypeface(Typeface.DEFAULT_BOLD);
        mBinding.setClickEdit(false);
        mBinding.setClickSelectAll(false);

        mTitleList = new String[]{"商品", "店铺"};

        mFragmentList.clear();
        mGoodsFragment = new MineCollectionGoodsFragment();
        mStoreFragment = new MineCollectionStoreFragment();
        mGoodsFragment.setListener(this);
        mStoreFragment.setListener(this);
        mFragmentList.add(mGoodsFragment);
        mFragmentList.add(mStoreFragment);

        mBinding.vpContent.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
        mBinding.tlContent.setViewPager(mBinding.vpContent, mTitleList);
        mBinding.vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getRightSetting().setText("编辑");
                mBinding.setClickEdit(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_setting) {
            if (getRightSetting().getText().equals("编辑")) {
                getRightSetting().setText("完成");
                mBinding.setClickEdit(true);
                mBinding.setClickSelectAll(false);
                if (mBinding.vpContent.getCurrentItem() == 0) {
                    mGoodsFragment.setEditStatus(true);
                } else if (mBinding.vpContent.getCurrentItem() == 1) {
                    mStoreFragment.setEditStatus(true);
                }
            } else {
                getRightSetting().setText("编辑");
                mBinding.setClickEdit(false);
                if (mBinding.vpContent.getCurrentItem() == 0) {
                    mGoodsFragment.setEditStatus(false);
                } else if (mBinding.vpContent.getCurrentItem() == 1) {
                    mStoreFragment.setEditStatus(false);
                }
            }
            return;
        }
        if (id == R.id.ll_select_status) {
            mBinding.setClickSelectAll(!mBinding.getClickSelectAll());
            if (mBinding.vpContent.getCurrentItem() == 0) {
                mGoodsFragment.setSelectStatus(mBinding.getClickSelectAll());
            } else if (mBinding.vpContent.getCurrentItem() == 1) {
                mStoreFragment.setSelectStatus(mBinding.getClickSelectAll());
            }
            return;
        }
        if (id == R.id.tv_delete) {
            //点击删除
            if (mBinding.getClickSelectAll()) {
                //全选
                if (mBinding.vpContent.getCurrentItem() == 0) {
                    doClearCollectionGoods();
                } else if (mBinding.vpContent.getCurrentItem() == 1) {
                    doClearCollectionStore();
                }
            } else {
                if (mBinding.vpContent.getCurrentItem() == 0) {
                    if (mGoodsFragment.getDeleteGoods().length > 0) {
                        doUCollectionGood(mGoodsFragment.getDeleteGoods());
                    } else {
                        showError("请选择的要删除的商品");
                    }
                } else if (mBinding.vpContent.getCurrentItem() == 1) {
                    if (mStoreFragment.getDeleteStores().length > 0) {
                        doUCollectionStore(mStoreFragment.getDeleteStores());
                    } else {
                        showError("请选择要删除的店铺");
                    }
                }
            }

        }
    }

    @Override
    public void collectionSelectAll(boolean selectAll) {
        mBinding.setClickSelectAll(selectAll);
    }

    //请求删除收藏的商品
    private void doUCollectionGood(String[] items) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUnCollectionGood(items)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort("删除商品收藏成功");
                        if (mBinding.vpContent.getCurrentItem() == 0) {
                            mGoodsFragment.onRefresh();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError("");
                    }
                });
    }

    //请求清空收藏的商品
    private void doClearCollectionGoods() {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doClearCollectionGoods("default")
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort("清空商品收藏成功");
                        getRightSetting().setText("编辑");
                        mBinding.setClickEdit(false);
                        if (mBinding.vpContent.getCurrentItem() == 0) {
                            mGoodsFragment.setEditStatus(false);
                            mGoodsFragment.onRefresh();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError("");
                    }
                });
    }


    //请求删除收藏的商品
    private void doUCollectionStore(String[] items) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUnCollectionStore(items)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort("删除店铺收藏成功");
                        if (mBinding.vpContent.getCurrentItem() == 1) {
                            mStoreFragment.onRefresh();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError("");
                    }
                });
    }

    //请求清空收藏的店铺
    private void doClearCollectionStore() {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doClearCollectionStore("default")
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort("清空店铺收藏成功");
                        //将页面还原为初始状态
                        getRightSetting().setText("编辑");
                        mBinding.setClickEdit(false);
                        if (mBinding.vpContent.getCurrentItem() == 1) {
                            mStoreFragment.setEditStatus(false);
                            mStoreFragment.onRefresh();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError("");
                    }
                });
    }
}
