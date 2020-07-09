package com.liemi.basemall.data.entity.user;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;


public class HelperServiceEntity extends BaseEntity {
    private int id;
    private String name;
    private String shop_tel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}

