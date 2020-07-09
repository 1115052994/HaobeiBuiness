package com.liemi.basemall.ui.category;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.CategoryApi;
import com.liemi.basemall.data.entity.good.GoodsListEntity;
import com.liemi.basemall.databinding.ActivityCategoryGoodsBinding;
import com.liemi.basemall.databinding.PopGoodFilterMenuBinding;
import com.liemi.basemall.databinding.SpinnerGoodFilterItemBinding;
import com.liemi.basemall.ui.home.SearchActivity;
import com.liemi.basemall.ui.store.StoreSearchActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.widget.BasePopupWindow;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;

public class CategoryGoodsActivity extends BaseXRecyclerActivity<ActivityCategoryGoodsBinding, GoodsListEntity> implements XRecyclerView.LoadingListener, View.OnClickListener {
    public static final String MC_ID = "category_mc_id";

    public static final String MC_NAME = "category_mc_name";

    public static final String MC_HOT_GOODS = "category_mc_hot_goods";

    public static final String MC_NEW_GOODS = "category_mc_new_goods";

    private String mcid, isHot, isNew, sort_name, sort_type, storeId;
    private BaseRViewAdapter<String, BaseViewHolder> menuAdapter;
    private PopupWindow menuPop;

    @Override
    protected int getContentView() {
        return R.layout.activity_category_goods;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getIntent().getStringExtra(MC_NAME));
        ImageView ivRight = findViewById(R.id.iv_setting);
        ivRight.setImageResource(R.mipmap.baselib_ic_search);
        ivRight.setVisibility(View.VISIBLE);

        adapter = new GoodsListAdapter(getContext());

        xRecyclerView = mBinding.xrvGoods;
        xRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }


    private void showMenuPop() {
        if (menuPop == null) {
            menuPop = new BasePopupWindow().setActivity(null);
            menuPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            menuPop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            menuPop.setFocusable(true);
            menuPop.setOutsideTouchable(true);
            PopGoodFilterMenuBinding binding = DataBindingUtil.inflate(
                    getLayoutInflater(), R.layout.pop_good_filter_menu, null, false);
            initMenuAdapter();
            binding.rvMenu.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvMenu.setAdapter(menuAdapter);


            binding.setListener(this);
            menuPop.setContentView(binding.getRoot());
        }
        if (Build.VERSION.SDK_INT < 24) {
            menuPop.showAsDropDown(mBinding.tvSort);
        } else {
            Rect visibleFrame = new Rect();
            mBinding.tvSort.getGlobalVisibleRect(visibleFrame);
            int height = mBinding.tvSort.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            menuPop.setHeight(height);
            menuPop.showAsDropDown(mBinding.tvSort);
        }
    }

    private void initMenuAdapter() {
        List<String> list = new ArrayList<>();
        list.add("综合排序");
        list.add("价格从高到低");
        list.add("价格从低到高");
        list.add("销量从高到低");
        list.add("销量从低到高");

        menuAdapter = new BaseRViewAdapter<String, BaseViewHolder>(getContext()) {
            private int selectIndex;

            @Override
            public int layoutResId(int viewType) {
                return R.layout.spinner_good_filter_item;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<String>(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (menuPop != null) {
                            menuPop.dismiss();
                        }
                        selectIndex = position;
                        switch (position) {
                            case 1:
                                sort_name = "price";
                                sort_type = "SORT_DESC";
                                break;
                            case 2:
                                sort_name = "price";
                                sort_type = "SORT_ASC";
                                break;
                            case 3:
                                sort_name = "deal_num";
                                sort_type = "SORT_DESC";
                                break;
                            case 4:
                                sort_name = "deal_num";
                                sort_type = "SORT_ASC";
                                break;
                            default:
                                sort_name = null;
                                sort_type = null;
                                break;
                        }
                        mBinding.tvSort.setText(getItem(position));
                        notifyDataSetChanged();
                        mBinding.xrvGoods.refresh();
                    }

                    @Override
                    public SpinnerGoodFilterItemBinding getBinding() {
                        return (SpinnerGoodFilterItemBinding) super.getBinding();
                    }

                    @Override
                    public void bindData(String item) {
                        super.bindData(item);
                        if (selectIndex == position) {
                            getBinding().ivSelected.setVisibility(View.VISIBLE);
                            getBinding().tvFilter.setChecked(true);
                        } else {
                            getBinding().ivSelected.setVisibility(View.GONE);
                            getBinding().tvFilter.setChecked(false);
                        }
                    }
                };
            }
        };
        menuAdapter.setData(list);
    }

    @Override
    protected void initData() {
        mcid = getIntent().getStringExtra(MC_ID);
        isHot = getIntent().getStringExtra(MC_HOT_GOODS);
        isNew = getIntent().getStringExtra(MC_NEW_GOODS);
        storeId = getIntent().getStringExtra(STORE_ID);
        xRecyclerView.refresh();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_sort) {
            showMenuPop();

        } else if (i == R.id.iv_setting) {
            if (TextUtils.isEmpty(storeId)) {
                JumpUtil.startSceneTransition(this, SearchActivity.class, null);
            } else {
                JumpUtil.startSceneTransition(this, StoreSearchActivity.class,
                        new FastBundle().putString(STORE_ID, storeId));
            }

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

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .listGoods(PageUtil.toPage(startPage), Constant.PAGE_ROWS, null, mcid, null,
                        null, null, null, null, storeId,
                        isHot, isNew, sort_name, sort_type)
                .compose(RxSchedulers.<BaseData<PageEntity<GoodsListEntity>>>compose())
                .compose((this).<BaseData<PageEntity<GoodsListEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<GoodsListEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<PageEntity<GoodsListEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_content_pop) {
            if (menuPop != null) {
                menuPop.dismiss();
            }
        }
    }
}
