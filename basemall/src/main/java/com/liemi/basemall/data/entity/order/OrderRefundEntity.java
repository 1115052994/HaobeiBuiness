package com.liemi.basemall.data.entity.order;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/9/8 17:49
 * 修改备注：
 */
public class OrderRefundEntity extends BaseEntity {


    /**
     * id : 26
     * order_id : 30
     * spu_name : 折扣商品
     * sku_price : 100
     * value_names : 100
     * buy_count : 10
     * sku_id : 1
     * refund_status : 1
     * refund_way : 0
     * cover_img_url : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15341474949335.jpg
     */

    private String id;
    private String order_id;
    private String spu_name;
    private String sku_price;
    private String value_names;
    private String buy_count;
    private String sku_id;
    //退款状态0：未退款1：已申请2：已拒绝3：已退款
    private int refund_status;
    //退款方式0：仅退款1：退款退货
    private int refund_way;
    private String cover_img_url;
    private String statusStr;

    public String getStatusStr() {
        statusStr = refund_way == 0 ? "仅退款" : "退货退款";
        switch (refund_status) {
            case 0:
            case 1:
                statusStr += "，申请中";
                break;
            case 2:
                statusStr += "，拒绝退款";
                break;
            case 3:
                statusStr += "，退款成功";
                break;
        }
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSpu_name() {
        return spu_name;
    }

    public void setSpu_name(String spu_name) {
        this.spu_name = spu_name;
    }

    public String getSku_price() {
        return sku_price;
    }

    public void setSku_price(String sku_price) {
        this.sku_price = sku_price;
    }

    public String getValue_names() {
        return value_names;
    }

    public void setValue_names(String value_names) {
        this.value_names = value_names;
    }

    public String getBuy_count() {
        return buy_count;
    }

    public void setBuy_count(String buy_count) {
        this.buy_count = buy_count;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public int getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(int refund_status) {
        this.refund_status = refund_status;
    }

    public int getRefund_way() {
        return refund_way;
    }

    public void setRefund_way(int refund_way) {
        this.refund_way = refund_way;
    }

    public String getCover_img_url() {
        return cover_img_url;
    }

    public void setCover_img_url(String cover_img_url) {
        this.cover_img_url = cover_img_url;
    }
}
