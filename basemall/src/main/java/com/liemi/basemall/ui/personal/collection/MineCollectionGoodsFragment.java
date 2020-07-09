package com.liemi.basemall.ui.personal.collection;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.user.MineCollectionGoodsEntity;
import com.liemi.basemall.databinding.FragmentMineCollectionGoodsBinding;
import com.liemi.basemall.databinding.ItemMineCollectionGoodsBinding;
import com.liemi.basemall.ui.store.good.GoodDetailActivity;
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

import static com.liemi.basemall.ui.store.good.BaseGoodsDetailedActivity.ITEM_ID;

/**
 * 我的收藏----商品
 */
public class MineCollectionGoodsFragment extends BaseXRecyclerFragment<FragmentMineCollectionGoodsBinding, MineCollectionGoodsEntity> {

    //记录当前是否是编辑状态
    private boolean mIsEdit = false;
    //全选或者取消全选
    private boolean mIsSelect = false;
    private CollcetionSelectAllListener mListener;

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
        xRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new BaseRViewAdapter<MineCollectionGoodsEntity, BaseViewHolder>(getActivity()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_mine_collection_goods;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<MineCollectionGoodsEntity>(binding) {
                    @Override
                    public void bindData(MineCollectionGoodsEntity item) {
                        ItemMineCollectionGoodsBinding goodsBinding = (ItemMineCollectionGoodsBinding) getBinding();
                        goodsBinding.setIsEditStatus(mIsEdit);
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (mIsEdit) {
                            getItem(position).setClickSelect(!getItem(position).isClickSelect());
                            //notifyItemChanged(getAdapterPosition());
                            notifyDataSetChanged();
                            for (MineCollectionGoodsEntity entity : items) {
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
                            JumpUtil.overlay(context, GoodDetailActivity.class,
                                    new FastBundle().putString(ITEM_ID, String.valueOf(getItem(position).getItem_id())));
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
    public void setListener(CollcetionSelectAllListener listener) {
        this.mListener = listener;
    }

    //设置全选或者取消全选
    public void setSelectStatus(boolean selectStatus) {
        if (this.mIsSelect != selectStatus) {
            this.mIsSelect = selectStatus;
            for (MineCollectionGoodsEntity goodsEntity : adapter.getItems()) {
                goodsEntity.setClickSelect(mIsSelect);
            }
        }
        adapter.notifyDataSetChanged();
    }

    //设置是否为编辑状态
    public void setEditStatus(boolean editStatus) {
        this.mIsEdit = editStatus;
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doMineCollectionGoods(PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(this.<BaseData<PageEntity<MineCollectionGoodsEntity>>>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<PageEntity<MineCollectionGoodsEntity>>>compose())
                .subscribe(new XObserver<BaseData<PageEntity<MineCollectionGoodsEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<MineCollectionGoodsEntity>> data) {
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

    //获取所有的选中的商品
    public String[] getDeleteGoods() {
        List<String> entityList = new ArrayList<>();
        for (MineCollectionGoodsEntity entity : adapter.getItems()) {
            if (entity.isClickSelect()) {
                entityList.add(String.valueOf(entity.getItem_id()));
            }
        }
        String[] goodsArray = new String[entityList.size()];
        entityList.toArray(goodsArray);
        return goodsArray;
    }


    //定义一个接口，用于全选之后改变activity的状态
    public interface CollcetionSelectAllListener {
        void collectionSelectAll(boolean selectAll);
    }
}
