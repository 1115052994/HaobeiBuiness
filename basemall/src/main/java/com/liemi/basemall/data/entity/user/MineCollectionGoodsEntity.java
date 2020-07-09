package com.liemi.basemall.data.entity.user;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/11/23
 * 修改备注：
 */
public class MineCollectionGoodsEntity extends BaseEntity {

    private int micid;
    private int item_id;
    private int uid;
    private String create_time;
    private String title;
    private float price;
    private int status;
    private String img_url;
    private String color_name;
    private int deal_num;
    private boolean clickSelect = false;//选中

    public boolean isClickSelect() {
        return clickSelect;
    }

    public void setClickSelect(boolean clickSelect) {
        this.clickSelect = clickSelect;
    }

    public int getDeal_num() {
        return deal_num;
    }

    public void setDeal_num(int deal_num) {
        this.deal_num = deal_num;
    }

    public int getMicid() {
        return micid;
    }

    public void setMicid(int micid) {
        this.micid = micid;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }
}
