package com.liemi.basemall.widget;

import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;

/**
 * Created by Bingo on 2018/11/27.
 */

public interface ShopCartCallback {
    public void calcuCount();
    public void doUpdateCartNum(final GoodsDetailedEntity goodEntity, final float num);
}
