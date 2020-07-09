package com.netmi.workerbusiness.data.entity.home.linecommodity.createcommofity;

import android.text.TextUtils;

import java.io.Serializable;

public class CreateGoodShopDetailEntity implements Serializable {
    private String id;
    private String on_shelf;
    private String ready_Shelf;
    private String up_shelf;
    private String count_item;
    private String logo_url;
    private String create_time;
    private String flowwater;
    private String billtotal;
    private String name;
    private String shop_tel;
    private String shop_remind_tel;
    private String p_id;
    private String c_id;
    private String d_id;
    private String address;
    private String full_name;
    private String admin_name;
    private String sc_id;
    private String category_name;
    private String facecode;


    public String getCategoryString(){
        if (TextUtils.isEmpty(getCategory_name())){
            return "请选择类目";
        }else {
            return getCategory_name();
        }
    }

    public String getSc_id() {
        return sc_id;
    }

    public void setSc_id(String sc_id) {
        this.sc_id = sc_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getFacecode() {
        return facecode;
    }

    public void setFacecode(String facecode) {
        this.facecode = facecode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getOn_shelf() {
        return on_shelf;
    }

    public void setOn_shelf(String on_shelf) {
        this.on_shelf = on_shelf;
    }

    public String getReady_Shelf() {
        return ready_Shelf;
    }

    public void setReady_Shelf(String ready_Shelf) {
        this.ready_Shelf = ready_Shelf;
    }

    public String getUp_shelf() {
        return up_shelf;
    }

    public void setUp_shelf(String up_shelf) {
        this.up_shelf = up_shelf;
    }

    public String getCount_item() {
        return count_item;
    }

    public void setCount_item(String count_item) {
        this.count_item = count_item;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFlowwater() {
        return flowwater;
    }

    public void setFlowwater(String flowwater) {
        this.flowwater = flowwater;
    }

    public String getBilltotal() {
        return billtotal;
    }

    public void setBilltotal(String billtotal) {
        this.billtotal = billtotal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShop_tel() {
        return shop_tel;
    }

    public void setShop_tel(String shop_tel) {
        this.shop_tel = shop_tel;
    }

    public String getShop_remind_tel() {
        return shop_remind_tel;
    }

    public void setShop_remind_tel(String shop_remind_tel) {
        this.shop_remind_tel = shop_remind_tel;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
