package com.liemi.basemall.ui.store;

import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.CategoryApi;
import com.liemi.basemall.data.entity.category.GoodsOneCateEntity;
import com.liemi.basemall.data.entity.category.GoodsTwoCateEntity;
import com.liemi.basemall.databinding.ActivityStoreCategoryBinding;
import com.liemi.basemall.ui.category.CategoryGoodsActivity;
import com.liemi.basemall.ui.home.SearchActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.category.CategoryGoodsActivity.MC_ID;
import static com.liemi.basemall.ui.category.CategoryGoodsActivity.MC_NAME;
import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;

public class StoreCategoryActivity extends BaseXRecyclerActivity<ActivityStoreCategoryBinding,GoodsTwoCateEntity> {

    private String storeId;
    @Override
    protected int getContentView() {
        return R.layout.activity_store_category;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("商品分类");
        xRecyclerView = mBinding.xrvData;
        ImageView ivRight = findViewById(R.id.iv_setting);
        ivRight.setImageResource(R.mipmap.baselib_ic_search);
        ivRight.setVisibility(View.VISIBLE);

        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<GoodsTwoCateEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_store_category;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);

                        Bundle bundle = new Bundle();
                        bundle.putString(MC_ID, getItem(position).getMcid());
                        bundle.putString(MC_NAME, getItem(position).getName());
                        if (!TextUtils.isEmpty(storeId)) {
                            bundle.putString(STORE_ID, storeId);
                        }
                        JumpUtil.overlay(getActivity(), CategoryGoodsActivity.class, bundle);
                    }
                };
            }
        });
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.iv_setting) {
            if (TextUtils.isEmpty(storeId)) {
                JumpUtil.startSceneTransition(this, SearchActivity.class, null);
            } else {
                    JumpUtil.startSceneTransition(this, StoreSearchActivity.class,
                            new FastBundle().putString(STORE_ID, storeId));
            }

        }else if (i==R.id.tv_all_good){     //全部商品
            Bundle bundle = new Bundle();
            bundle.putString(MC_NAME, mBinding.tvAllGood.getText().toString());
            if (!TextUtils.isEmpty(storeId)) {
                bundle.putString(STORE_ID, storeId);
            }
            JumpUtil.overlay(getActivity(), CategoryGoodsActivity.class, bundle);
        }
    }

    @Override
    protected void initData() {
        storeId=getIntent().getStringExtra(STORE_ID);
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        doListCategory();
    }

    private void doListCategory() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .getSecondCategory(storeId,null)
                .compose(RxSchedulers.<BaseData<List<GoodsTwoCateEntity>>>compose())
                .compose((this).<BaseData<List<GoodsTwoCateEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<GoodsTwoCateEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<List<GoodsTwoCateEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }
}
