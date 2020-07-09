package com.liemi.basemall.ui.store.good;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.CategoryApi;
import com.liemi.basemall.data.entity.good.GoodsListEntity;
import com.liemi.basemall.data.event.SearchKeyWordEvent;
import com.liemi.basemall.databinding.FragmentXrecyclerviewBinding;
import com.liemi.basemall.ui.category.GoodsListAdapter;
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
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/16 15:36
 * 修改备注：
 */
public class StoreGoodsFragment extends BaseXRecyclerFragment<FragmentXrecyclerviewBinding, GoodsListEntity> {

    public static final String TAG = StoreGoodsFragment.class.getName();

    //商品类型 默认0:普通商品 1:纯积分商品 2:现金+积分商品
    public static final String GOOD_ITEM_TYPE = "goodItemId";

    public static final String GOOD_ITEM_TYPE_SCORE = "1";

    public static final String GOOD_ITEM_TYPE_MONEY_AND_SCORE = "2";

    private String storeId, itemType;

    @Override
    protected int getContentView() {
        return R.layout.fragment_xrecyclerview;
    }

    public static StoreGoodsFragment newInstance(String storeId, String itemType) {
        StoreGoodsFragment f = new StoreGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STORE_ID, storeId);
        bundle.putString(GOOD_ITEM_TYPE, itemType);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeId = getArguments().getString(STORE_ID);
        itemType = getArguments().getString(GOOD_ITEM_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initUI() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        xRecyclerView.setAdapter(adapter = new GoodsListAdapter(getContext()));

        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    protected void initData() {
        if ((!TextUtils.isEmpty(storeId)
                && !(getActivity() instanceof StoreSearchActivity))
                || !TextUtils.isEmpty(itemType) || !TextUtils.isEmpty(getKeyWord())) {
            xRecyclerView.refresh();
        }
    }

    private String getKeyWord() {
        if (getActivity() instanceof SearchActivity) {
            return ((SearchActivity) getActivity()).getEtSearchText();
        } else if (getActivity() instanceof StoreSearchActivity) {
            return ((StoreSearchActivity) getActivity()).getEtSearchText();
        }
        return "";
    }

    @Override
    public void onRefresh() {
        emptyVisible(true);
        super.onRefresh();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        emptyVisible(adapter.getItemCount() > 0);
    }

    private void emptyVisible(boolean hidden) {
        if (getActivity() instanceof SearchActivity || getActivity() instanceof StoreSearchActivity) {
            mBinding.includeEmpty.llEmpty.setVisibility(hidden ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .listGoods(PageUtil.toPage(startPage), Constant.PAGE_ROWS, itemType, null, getKeyWord(),
                        null, null, null, null, storeId,
                        null, null, null, null)
                .compose(RxSchedulers.<BaseData<PageEntity<GoodsListEntity>>>compose())
                .compose((this).<BaseData<PageEntity<GoodsListEntity>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void searchKeyWordEvent(SearchKeyWordEvent event) {
        xRecyclerView.refresh();
    }

}
