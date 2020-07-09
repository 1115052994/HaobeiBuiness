package com.liemi.basemall.data.entity.comment;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/10 10:34
 * 修改备注：
 */
public class CommentItemEntity {

    private int level;
    private String order_sku_id;
    private String content;
    private List<String> imgs = new ArrayList<>();

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public CommentItemEntity(String order_id){
        this.order_sku_id = order_id;
    }

    public String getOrder_sku_id() {
        return order_sku_id;
    }

    public void setOrder_sku_id(String order_sku_id) {
        this.order_sku_id = order_sku_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
