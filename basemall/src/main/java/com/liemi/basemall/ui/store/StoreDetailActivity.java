package com.liemi.basemall.ui.store;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.StoreApi;
import com.liemi.basemall.data.entity.StoreEntity;
import com.liemi.basemall.databinding.ActivityStoreDetailBinding;
import com.liemi.basemall.ui.store.good.StoreGoodsFragment;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/24 10:24
 * 修改备注：
 */
public class StoreDetailActivity extends BaseActivity<ActivityStoreDetailBinding> {

    public static final String STORE_ID = "store_id";

    private StoreEntity storeEntity;

    private String storeId;

    @Override
    protected int getContentView() {
        return R.layout.activity_store_detail;
    }

    @Override
    protected void initUI() {
        storeId = getIntent().getStringExtra(STORE_ID);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(StoreHomeFragment.newInstance(storeId));
        fragmentList.add(StoreGoodsFragment.newInstance(storeId, null));
        mBinding.tlGroup.setViewPager(mBinding.vpGroup,
                new String[]{"店铺首页", "全部商品"}, this, fragmentList);
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(storeId)) {
            doGetStoreDetail();
        } else {
            ToastUtils.showShort("请先传入店铺参数");
            finish();
        }
    }

    private void showData(StoreEntity storeEntity) {
        this.storeEntity = storeEntity;
        mBinding.setItem(storeEntity);
        mBinding.tvFollow.setSelected(storeEntity.getIs_collection() == 1);
    }

    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_follow) {
            if (!MApplication.getInstance().checkUserIsLogin()) {
                ToastUtils.showShort("请先登录");
                return;
            }

            if (mBinding.tvFollow.isSelected()) {
                doCollectionDel();
            } else {
                doCollection();
            }

        } else if (i == R.id.tv_search) {
            JumpUtil.startSceneTransition(this, StoreSearchActivity.class,
                    new FastBundle().putString(STORE_ID, storeId),
                    new Pair<>(view, getString(R.string.transition_name)));
        } else if (i == R.id.tv_store_category) {
            Bundle bundle = new Bundle();
            bundle.putString(STORE_ID, storeId);
            JumpUtil.overlay(this, StoreCategoryActivity.class, bundle);

        } else {
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    private void doGetStoreDetail() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .getStoreDetail(storeId)
                .compose(RxSchedulers.<BaseData<StoreEntity>>compose())
                .compose((this).<BaseData<StoreEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<StoreEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<StoreEntity> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doCollection() {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .shopCollection(storeId)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        storeEntity.setSum_collection(storeEntity.getSum_collection() + 1);
                        mBinding.tvFollow.setText(String.format(getString(R.string.format_follow), storeEntity.getSum_collection()));
                        mBinding.tvFollow.setSelected(true);
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doCollectionDel() {
        showProgress("");
        List<String> list = new ArrayList<>();
        list.add(storeId);
        RetrofitApiFactory.createApi(StoreApi.class)
                .shopCollectionDel(list)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        storeEntity.setSum_collection(storeEntity.getSum_collection() - 1);
                        mBinding.tvFollow.setText(String.format(getString(R.string.format_follow), storeEntity.getSum_collection()));
                        mBinding.tvFollow.setSelected(false);
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }
}
