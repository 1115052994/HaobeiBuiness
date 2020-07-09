package com.liemi.basemall.vo;

import java.util.List;

/**
 * Created by Bingo on 2018/6/14.
 */

public class OrderCommentVo {
    private String sku_id;
    private float star;
    private String content;
    private List<String> img_url;

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }
}
