package com.netmi.workerbusiness.data.entity.home.postage;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/20
 * 修改备注：
 */
public class ServiceDesEntity  extends BaseEntity {

    /**
     * id : 79
     * item_id : null
     * name : 正品保障
     * remark : 100%原装正品：所有商品均属正品生产或销售；因此，所售商品一律为100%原装正品。
     * sort : 1
     * create_time : 2019-01-12 14:07:20
     * update_time : 2019-07-30 15:30:39
     * icon : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/png/ABWXMNTH01235679_1547273235.png
     */

    private String id;
    private Object item_id;
    private String name;
    private String remark;
    private int sort;
    private String create_time;
    private String update_time;
    private String icon;

    private boolean isRed =false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getItem_id() {
        return item_id;
    }

    public void setItem_id(Object item_id) {
        this.item_id = item_id;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean red) {
        this.isRed = red;
    }
}
