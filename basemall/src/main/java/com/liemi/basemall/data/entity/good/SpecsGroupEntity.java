package com.liemi.basemall.data.entity.good;

import com.netmi.baselibrary.utils.FloatUtils;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/10/16 19:50
 * 修改备注：
 */
public class SpecsGroupEntity {

    /**
     * ivid : 5344
     * item_id : 1250
     * value_names : 8G i7
     * value_ids : 2004,2007
     * stock : 100
     * price : 8000.00
     * score : 0
     */

    private String ivid;
    private String item_id;
    private String value_names;
    private String value_ids;
    private long stock;
    private String price;
    private String score;
    private int item_type;
    private String img_url;

    public String getShowPrice() {
        switch (item_type) {
            case 1:
                return FloatUtils.formatScore(score);
            case 2:
                return FloatUtils.formatMoney(price) + "+" + FloatUtils.formatScore(score);
            default:
                return FloatUtils.formatMoney(price);
        }
    }

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public String getIvid() {
        return ivid;
    }

    public void setIvid(String ivid) {
        this.ivid = ivid;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getValue_names() {
        return value_names;
    }

    public void setValue_names(String value_names) {
        this.value_names = value_names;
    }

    public String getValue_ids() {
        return value_ids;
    }

    public void setValue_ids(String value_ids) {
        this.value_ids = value_ids;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
