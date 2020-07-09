package com.netmi.workerbusiness.data.entity.home.store;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/19
 * 修改备注：
 */
public class CateOneEntity extends BaseEntity {
    /**
     * prop_id : 1000164
     * shop_id : 213
     * prop_name : 风格
     * prop_code : null
     * remark : null
     * create_time : 2019-09-10 15:52:25
     * update_time : 2019-09-10 15:52:25
     * sort : 9999
     * del_flag : 0
     * is_show : 1
     */

    private String prop_id;
    private String shop_id;
    private String prop_name;
    private Object prop_code;
    private Object remark;
    private String create_time;
    private String update_time;
    private int sort;
    private int del_flag;
    private int is_show;

    public String getProp_id() {
        return prop_id;
    }

    public void setProp_id(String prop_id) {
        this.prop_id = prop_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getProp_name() {
        return prop_name;
    }

    public void setProp_name(String prop_name) {
        this.prop_name = prop_name;
    }

    public Object getProp_code() {
        return prop_code;
    }

    public void setProp_code(Object prop_code) {
        this.prop_code = prop_code;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(int del_flag) {
        this.del_flag = del_flag;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }
}
