package com.netmi.workerbusiness.data.entity.home.postage;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostageTempleItemEntity extends BaseEntity implements Serializable {
    private String region_name;
    private List<String> region = new ArrayList<>();
    private String first_item;
    private String first_money;
    private String add_item;
    private String add_money;


    public String getFirst_item() {
        return first_item;
    }

    public void setFirst_item(String first_item) {
        this.first_item = first_item;
    }

    public String getFirst_money() {
        return first_money;
    }

    public void setFirst_money(String first_money) {
        this.first_money = first_money;
    }

    public String getAdd_item() {
        return add_item;
    }

    public void setAdd_item(String add_item) {
        this.add_item = add_item;
    }

    public String getAdd_money() {
        return add_money;
    }

    public void setAdd_money(String add_money) {
        this.add_money = add_money;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public List<String> getRegion() {
        return region;
    }

    public void setRegion(List<String> region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "PostageTempleItemEntity{" +
                "first_item='" + first_item + '\'' +
                ", first_money='" + first_money + '\'' +
                ", add_item='" + add_item + '\'' +
                ", add_money='" + add_money + '\'' +
                ", region_name='" + region_name + '\'' +
                ", region=" + region +
                '}';
    }
}
