package com.netmi.workerbusiness.data.entity.home.postage;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class FreightListEntity extends BaseEntity {
    private String id;
    private String shop_id;
    private String template_group_name;
    private String create_time;
    private String update_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTemplate_group_name() {
        return template_group_name;
    }

    public void setTemplate_group_name(String template_group_name) {
        this.template_group_name = template_group_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
