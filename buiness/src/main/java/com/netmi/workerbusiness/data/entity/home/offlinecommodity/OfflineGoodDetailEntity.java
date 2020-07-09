package com.netmi.workerbusiness.data.entity.home.offlinecommodity;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

public class OfflineGoodDetailEntity extends BaseEntity {


    /**
     * item_id : 1
     * shop_id : 224
     * title : 测试商品
     * img_url : ["https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/ABWXMTHK01236789_1560147569.png"]
     * price : 0.01
     * old_price : 0.01
     * stock : 100
     * deal_num : 0
     * rich_text : wwwwwwwwwww
     * deal_num_false : 0
     * status : 1
     * sort : 1
     * purchase_note : ssss
     */

    private String item_id;
    private String shop_id;
    private String title;
    private String price;
    private String old_price;
    private String stock;
    private String deal_num;
    private String rich_text;
    private String deal_num_false;
    private String status;
    private String sort;
    private String purchase_note;
    private List<String> img_url;
    private String start_date;
    private String end_date;

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

    public String getRich_text() {
        return rich_text;
    }

    public void setRich_text(String rich_text) {
        this.rich_text = rich_text;
    }

    public String getDeal_num_false() {
        return deal_num_false;
    }

    public void setDeal_num_false(String deal_num_false) {
        this.deal_num_false = deal_num_false;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPurchase_note() {
        return purchase_note;
    }

    public void setPurchase_note(String purchase_note) {
        this.purchase_note = purchase_note;
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
