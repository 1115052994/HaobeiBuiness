package com.liemi.basemall.data.entity.user;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/11/23
 * 修改备注：
 */
public class MineCollectionStoreEntity extends BaseEntity {


    private int id;
    private String logo_url;
    private String img_url;
    private String name;
    private String remark;
    private String full_name;
    private String content;
    private String shop_tel;
    private String sum_item;
    private boolean clickSelect = false;

    public boolean isClickSelect() {
        return clickSelect;
    }

    public void setClickSelect(boolean clickSelect) {
        this.clickSelect = clickSelect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShop_tel() {
        return shop_tel;
    }

    public void setShop_tel(String shop_tel) {
        this.shop_tel = shop_tel;
    }

    public String getSum_item() {
        return sum_item;
    }

    public void setSum_item(String sum_item) {
        this.sum_item = sum_item;
    }
}
