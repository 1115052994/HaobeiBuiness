package com.netmi.baselibrary.data.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：分页实体
 * 创建人：Simple
 * 创建时间：2017/7/18 13:40
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class PageEntityForSeederProfit<T> extends BaseEntity {

    /**
     * 总页数
     */
    private int total_pages;

    private int is_next;

    private String  receive_amount;

    public String getReceive_amount() {
        return receive_amount;
    }

    public void setReceive_amount(String receive_amount) {
        this.receive_amount = receive_amount;
    }

    /**
     * 数据
     */
    private List<T> list = new ArrayList<T>();

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getIs_next() {
        return is_next;
    }

    public void setIs_next(int is_next) {
        this.is_next = is_next;
    }
}
