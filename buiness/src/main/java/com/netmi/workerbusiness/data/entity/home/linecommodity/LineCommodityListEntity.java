package com.netmi.workerbusiness.data.entity.home.linecommodity;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/2
 * 修改备注：
 */
public class LineCommodityListEntity extends BaseEntity {


    /**
     * item_id : 1684
     * update_time : 2019-09-02 10:06:31
     * sort : 20
     * price : 35.00
     * stock : 50
     * deal_num : 0
     * img_url : https://goecnetcn.oss-cn-hongkong.aliyuncs.com/2019/08/29/14/36/34/6b0e1c083a5642c79435a3d5df618838.png
     * status : 1
     */

    private String item_id;
    private String title;
    private String update_time;
    private int sort;
    private String price;
    private String stock;
    private String deal_num;
    private String img_url;
    private int status;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDeal_num() {
        return deal_num;
    }

    public void setDeal_num(String deal_num) {
        this.deal_num = deal_num;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
