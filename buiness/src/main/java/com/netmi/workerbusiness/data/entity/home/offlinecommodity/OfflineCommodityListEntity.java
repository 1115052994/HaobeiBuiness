package com.netmi.workerbusiness.data.entity.home.offlinecommodity;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/29
 * 修改备注：
 */
public class OfflineCommodityListEntity extends BaseEntity {
    /**
     * item_id : 2
     * shop_id : 224
     * title : 测试商品
     * img_url : https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/ABWXMTHK01236789_1560147569.png
     * price : 0.01
     * old_price : 0.01
     * stock : 100
     * deal_num : 0
     * status : 2
     * end_date : 2019-09-30
     * start_date : 2019-09-30
     */

    private String item_id;
    private String shop_id;
    private String title;
    private String img_url;
    private String price;
    private String old_price;
    private String stock;
    private String deal_num;
    private String status;
    private String end_date;
    private String start_date;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDeal_num() {
        return deal_num;
    }

    public void setDeal_num(String deal_num) {
        this.deal_num = deal_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
}
