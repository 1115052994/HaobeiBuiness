package com.liemi.basemall.ui.store;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.api.StoreApi;
import com.liemi.basemall.data.entity.StoreEntity;
import com.liemi.basemall.data.entity.UserNoticeEntity;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.liemi.basemall.data.entity.good.GoodsListEntity;
import com.liemi.basemall.data.event.BackToAppEvent;
import com.liemi.basemall.data.event.UnReadMsgEvent;
import com.liemi.basemall.databinding.FragmentStoreBinding;
import com.liemi.basemall.databinding.ItemStoreBinding;
import com.liemi.basemall.ui.MainActivity;
import com.liemi.basemall.ui.home.SearchActivity;
import com.liemi.basemall.ui.personal.MessageActivity;
import com.liemi.basemall.ui.store.good.GoodDetailActivity;
import com.liemi.basemall.widget.MyRecyclerView;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.Densitys;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.ScreenUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;
import static com.liemi.basemall.ui.store.good.BaseGoodsDetailedActivity.ITEM_ID;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/16 15:36
 * 修改备注：
 */
public class StoreFragment extends BaseFragment<FragmentStoreBinding> implements XRecyclerView.LoadingListener {

    public static final String TAG = StoreFragment.class.getName();

    /**
     * 页数
     */
    private int startPage = 0;

    /**
     * 总条数
     */
    private int totalCount;

    /**
     * 列表加载数据方式
     */
    private int LOADING_TYPE = -1;

    private BaseRViewAdapter<StoreEntity, BaseViewHolder> adapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_store;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 未读消息标识
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void setRedCircle(UnReadMsgEvent event) {
        if(event != null)
            mBinding.includeTop.tvRedCircle.setVisibility(event.unReadNum > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(AccessTokenCache.get().getToken())) {
            doNoticeData();
        }else{
            mBinding.setShowUnReadMessage(false);
        }
    }

    @Override
    protected void initUI() {
        EventBus.getDefault().register(this);
        mBinding.setDoClick(this);
        TextView tvSearch = mBinding.getRoot().findViewById(R.id.tv_search);
        tvSearch.setText("搜索商品或店铺");

        xRecyclerView = mBinding.xrvStore;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<StoreEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_store;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        MyRecyclerView rvGoods = getBinding().rvGoods;
                        rvGoods.setNestedScrollingEnabled(false);
                        rvGoods.setLayoutManager(new GridLayoutManager(context, 3));
                        BaseRViewAdapter<GoodsListEntity, BaseViewHolder> goodAdapter = new BaseRViewAdapter<GoodsListEntity, BaseViewHolder>(context) {
                            @Override
                            public int layoutResId(int position) {
                                return R.layout.item_store_good;
                            }

                            @Override
                            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                                return new BaseViewHolder(binding) {

                                    @Override
                                    public void doClick(View view) {
                                        super.doClick(view);
                                        JumpUtil.overlay(getContext(), GoodDetailActivity.class,
                                                ITEM_ID, getItem(position).getItem_id());
                                    }
                                };
                            }
                        };
                        rvGoods.setAdapter(goodAdapter);
                        if(getItem(position).getItemList() != null)
                            goodAdapter.setData(getItem(position).getItemList());
                    }


                    @Override
                    public ItemStoreBinding getBinding() {
                        return (ItemStoreBinding) super.getBinding();
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        JumpUtil.startSceneTransition(getActivity(), StoreDetailActivity.class,
                                new FastBundle().putString(STORE_ID, adapter.getItem(position).getId()),
                                new Pair<>(binding.getRoot().findViewById(R.id.iv_store_pic), getString(R.string.transition_store)),
                                new Pair<>(binding.getRoot().findViewById(R.id.tv_store_name), getString(R.string.transition_store_name)));
                    }

                };
            }
        });

        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    public void showData(PageEntity<StoreEntity> pageEntity) {
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                xRecyclerView.setLoadingMoreEnabled(true);
            }
            adapter.setData(pageEntity.getList());
        } else if (LOADING_TYPE == Constant.LOAD_MORE) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                adapter.insert(adapter.getItemCount(), pageEntity.getList());
            }
        }
        totalCount = pageEntity.getTotal_pages();
        startPage = adapter.getItemCount();
    }

    @Override
    public void onRefresh() {
        startPage = 0;
        LOADING_TYPE = Constant.PULL_REFRESH;
        xRecyclerView.setLoadingMoreEnabled(false);
        doListStore();
    }

    @Override
    public void onLoadMore() {
        LOADING_TYPE = Constant.LOAD_MORE;
        doListStore();
    }

    @Override
    public void hideProgress() {
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            xRecyclerView.refreshComplete();
        } else {
            xRecyclerView.loadMoreComplete();
        }
        if (startPage >= totalCount) {
            xRecyclerView.setNoMore(true);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.tv_search) {
            JumpUtil.startSceneTransition(getActivity(), SearchActivity.class, null,
                    new Pair<>(view, getString(R.string.transition_name)));

        } else if (i == R.id.iv_message) {
            if (MApplication.getInstance().checkUserIsLogin()) {
                JumpUtil.overlay(getContext(), MessageActivity.class);
            }
        }
    }

    private void doListStore() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .listStore(PageUtil.toPage(startPage), Constant.PAGE_ROWS, null, null)
                .compose(RxSchedulers.<BaseData<PageEntity<StoreEntity>>>compose())
                .compose((this).<BaseData<PageEntity<StoreEntity>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData<PageEntity<StoreEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<PageEntity<StoreEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doNoticeData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doUserNotices(new String[]{"1"},null,0,10)
                .compose(RxSchedulers.<BaseData<UserNoticeEntity>>compose())
                .compose((this).<BaseData<UserNoticeEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserNoticeEntity>>() {

                    @Override
                    public void onSuccess(BaseData<UserNoticeEntity> data) {
                        if(data.getData().getRead_data().getAll_no_readnum() > 0){
                            mBinding.setShowUnReadMessage(true);
                        }else{
                            mBinding.setShowUnReadMessage(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();

                    }
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());

                    }
                });

    }
}
