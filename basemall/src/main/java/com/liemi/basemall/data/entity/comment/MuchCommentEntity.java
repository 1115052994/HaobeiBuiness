package com.liemi.basemall.data.entity.comment;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/10 10:37
 * 修改备注：
 */
public class MuchCommentEntity {

    private String order_id;
    private List<CommentItemEntity> list = new ArrayList<>();

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }


    public List<CommentItemEntity> getList() {
        return list;
    }

    public void setList(List<CommentItemEntity> list) {
        this.list = list;
    }
}
