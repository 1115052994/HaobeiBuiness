package com.netmi.workerbusiness.data.entity.home.postage;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class PostageTempleDetailListEntity extends BaseEntity {

    private String id;
    private String shop_id;
    private String fg_id;
    private String template_name;
    private String first_piece;
    private String first_freight;
    private String continue_piece;
    private String continue_freight;

    private String region_name;
    private List<String> region_list = new ArrayList<>();

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

    public String getFg_id() {
        return fg_id;
    }

    public void setFg_id(String fg_id) {
        this.fg_id = fg_id;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getFirst_piece() {
        return first_piece;
    }

    public void setFirst_piece(String first_piece) {
        this.first_piece = first_piece;
    }

    public String getFirst_freight() {
        return first_freight;
    }

    public void setFirst_freight(String first_freight) {
        this.first_freight = first_freight;
    }

    public String getContinue_piece() {
        return continue_piece;
    }

    public void setContinue_piece(String continue_piece) {
        this.continue_piece = continue_piece;
    }

    public String getContinue_freight() {
        return continue_freight;
    }

    public void setContinue_freight(String continue_freight) {
        this.continue_freight = continue_freight;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public List<String> getRegion_list() {
        return region_list;
    }

    public void setRegion_list(List<String> region_list) {
        this.region_list = region_list;
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
