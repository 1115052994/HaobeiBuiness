package com.liemi.basemall.ui.shopcar;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.liemi.basemall.R;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.liemi.basemall.data.entity.shopcar.ShopCartAdapterEntity;
import com.liemi.basemall.databinding.ItemShopCartBinding;
import com.liemi.basemall.ui.store.StoreDetailActivity;
import com.liemi.basemall.widget.ChildCheckListener;
import com.liemi.basemall.widget.MyRecyclerView;
import com.liemi.basemall.widget.ShopCartCallback;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;

import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;

/**
 * Created by Bingo on 2018/11/27.
 */

public class ShopCartAdapter extends BaseRViewAdapter<ShopCartAdapterEntity, BaseViewHolder> {

    private ShopCartCallback shopCartCallback;

    public ShopCartAdapter(Context context,@NonNull ShopCartCallback shopCartCallback) {
        super(context);
        this.shopCartCallback=shopCartCallback;
    }

    @Override
    public int layoutResId(int position) {
        return R.layout.item_shop_cart;
    }

    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {
        return new BaseViewHolder(binding) {
            @Override
            public void bindData(Object item) {
                super.bindData(item);
                getBinding().setIsEdit(true);
                MyRecyclerView rvGoods = getBinding().rvGoods;
                BaseRViewAdapter<GoodsDetailedEntity, BaseViewHolder> goodAdapter;
                if (rvGoods.getAdapter() == null) {
                    rvGoods.setLayoutManager(new LinearLayoutManager(context));
                    rvGoods.setNestedScrollingEnabled(false);
                    goodAdapter = getGoodAdapter(shopCartCallback, new ChildCheckListener() {
                        @Override
                        public void childCheck() {
                            if (getItem(position).getList() != null) {
                                boolean isAllSelect = true;
                                for (GoodsDetailedEntity goodsDetailedEntity : getItem(position).getList()) {
                                    if (!goodsDetailedEntity.isChecked()){
                                        isAllSelect=false;
                                    }
                                }
                                getBinding().cbStore.setChecked(isAllSelect);
                                getItem(position).setChecked(isAllSelect);
                            }
                        }
                    });
                    rvGoods.setAdapter(goodAdapter);
                } else {
                    goodAdapter = (BaseRViewAdapter<GoodsDetailedEntity, BaseViewHolder>) rvGoods.getAdapter();
                }

                goodAdapter.setData(getItem(position).getList());
            }

            @Override
            public ItemShopCartBinding getBinding() {
                return (ItemShopCartBinding) super.getBinding();
            }

            @Override
            public void doClick(View view) {
                super.doClick(view);
                int i = view.getId();
                if (i == R.id.tv_store_name) {
                    JumpUtil.overlay(context, StoreDetailActivity.class, new FastBundle().putString(STORE_ID, getItem(position).getShop().getId()));
                } else if (i == R.id.cb_store) {
                    CheckBox checkBox = (CheckBox) view;
                    getItem(position).setChecked(checkBox.isChecked());
                    for (GoodsDetailedEntity entity : getItem(position).getList()) {
                        entity.setChecked(checkBox.isChecked());
                    }
                    shopCartCallback.calcuCount();
                    notifyDataSetChanged();
                }
            }
        };
    }

    private BaseRViewAdapter<GoodsDetailedEntity, BaseViewHolder> getGoodAdapter(ShopCartCallback shopCartCallback,ChildCheckListener checkListener) {
        return new ShopGoodAdapter(context,shopCartCallback,checkListener);
    }
}
