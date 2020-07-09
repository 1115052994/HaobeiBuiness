package com.liemi.basemall.ui.store;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.StoreApi;
import com.liemi.basemall.data.entity.StoreEntity;
import com.liemi.basemall.data.event.SearchKeyWordEvent;
import com.liemi.basemall.databinding.FragmentXrecyclerviewBinding;
import com.liemi.basemall.ui.home.SearchActivity;
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
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
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
public class SearchStoreFragment extends BaseXRecyclerFragment<FragmentXrecyclerviewBinding, StoreEntity> {

    public static final String TAG = SearchStoreFragment.class.getName();

    @Override
    protected int getContentView() {
        return R.layout.fragment_xrecyclerview;
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
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<StoreEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.mall_item_search_store;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
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
        if (!TextUtils.isEmpty(getKeyWord())) {
            xRecyclerView.refresh();
        }
    }

    private String getKeyWord() {
        if (getActivity() instanceof SearchActivity) {
            return ((SearchActivity) getActivity()).getEtSearchText();
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
        if (getActivity() instanceof SearchActivity) {
            mBinding.includeEmpty.llEmpty.setVisibility(hidden ? View.GONE : View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void searchKeyWordEvent(SearchKeyWordEvent event) {
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .listStore(PageUtil.toPage(startPage), Constant.PAGE_ROWS, null, getKeyWord())
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
}
