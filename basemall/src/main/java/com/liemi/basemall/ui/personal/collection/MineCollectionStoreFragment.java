package com.liemi.basemall.ui.personal.collection;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.user.MineCollectionStoreEntity;
import com.liemi.basemall.databinding.FragmentMineCollectionGoodsBinding;
import com.liemi.basemall.databinding.ItemMineCollectionStoreBinding;
import com.liemi.basemall.ui.store.StoreDetailActivity;
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

import java.util.ArrayList;
import java.util.List;

import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;

public class MineCollectionStoreFragment extends BaseXRecyclerFragment<FragmentMineCollectionGoodsBinding, MineCollectionStoreEntity> {

    //记录当前是否是编辑状态
    private boolean mIsEdit = false;
    //全选或者取消全选
    private boolean mIsSelect = false;
    private MineCollectionGoodsFragment.CollcetionSelectAllListener mListener;

    @Override
    protected int getContentView() {
        return R.layout.fragment_mine_collection_goods;
    }

    @Override
    protected void initUI() {

        xRecyclerView = mBinding.xrContent;
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BaseRViewAdapter<MineCollectionStoreEntity, BaseViewHolder>(getActivity(), xRecyclerView) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_mine_collection_store;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<MineCollectionStoreEntity>(binding) {
                    @Override
                    public void bindData(MineCollectionStoreEntity item) {
                        ItemMineCollectionStoreBinding storeBinding = (ItemMineCollectionStoreBinding) getBinding();
                        storeBinding.setIsEdit(mIsEdit);
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (mIsEdit) {
                            getItem(position).setClickSelect(!getItem(position).isClickSelect());
                            notifyItemChanged(getAdapterPosition());
                            for (MineCollectionStoreEntity entity : items) {
                                if (!entity.isClickSelect()) {
                                    if (mListener != null) {
                                        mIsSelect = false;
                                        mListener.collectionSelectAll(false);
                                    }
                                    return;
                                }
                            }
                            if (mListener != null) {
                                mIsSelect = true;
                                mListener.collectionSelectAll(true);
                            }
                        } else {
                            JumpUtil.overlay(context, StoreDetailActivity.class,
                                    new FastBundle().putString(STORE_ID, String.valueOf(getItem(position).getId())));
                        }
                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }


    //设置接口
    public void setListener(MineCollectionGoodsFragment.CollcetionSelectAllListener listener) {
        this.mListener = listener;
    }

    //设置全选或者取消全选
    public void setSelectStatus(boolean selectStatus) {
        if (this.mIsSelect != selectStatus) {
            this.mIsSelect = selectStatus;
            for (MineCollectionStoreEntity storeEntity : adapter.getItems()) {
                storeEntity.setClickSelect(mIsSelect);
            }
        }
        adapter.notifyDataSetChanged();
    }

    //设置是否为编辑状态
    public void setEditStatus(boolean editStatus) {
        this.mIsEdit = editStatus;
        adapter.notifyDataSetChanged();
    }

    //获取所有的选中的商品
    public String[] getDeleteStores() {
        List<String> entityList = new ArrayList<>();
        for (MineCollectionStoreEntity entity : adapter.getItems()) {
            if (entity.isClickSelect()) {
                entityList.add(String.valueOf(entity.getId()));
            }
        }
        String[] storeArray = new String[entityList.size()];
        entityList.toArray(storeArray);
        return storeArray;
    }


    //请求收藏的商品列表
    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doMineCollectionStore(PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(this.<BaseData<PageEntity<MineCollectionStoreEntity>>>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<PageEntity<MineCollectionStoreEntity>>>compose())
                .subscribe(new XObserver<BaseData<PageEntity<MineCollectionStoreEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<MineCollectionStoreEntity>> data) {
                        showData(data.getData());
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
