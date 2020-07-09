package com.netmi.workerbusiness.data.entity.home;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class AfterSaleDataEntity extends BaseEntity {
    /**
     * refund_goods : 3
     * refund_price : 4
     * complete : 5
     */

    private String refund_goods;
    private String refund_price;
    private String complete;

    public String getRefund_goods() {
        return refund_goods;
    }

    public void setRefund_goods(String refund_goods) {
        this.refund_goods = refund_goods;
    }

    public String getRefund_price() {
        return refund_price;
    }

    public void setRefund_price(String refund_price) {
        this.refund_price = refund_price;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }
}
