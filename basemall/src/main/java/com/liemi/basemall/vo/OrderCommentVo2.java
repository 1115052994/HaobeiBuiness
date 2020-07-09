package com.liemi.basemall.vo;

import java.util.List;

/**
 * Created by Bingo on 2018/6/14.
 * 商品评论使用
 */

public class OrderCommentVo2 {
    private List<OrderCommentVo> list;
    private String order_id;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public List<OrderCommentVo> getList() {
        return list;
    }

    public void setList(List<OrderCommentVo> list) {
        this.list = list;
    }
}
