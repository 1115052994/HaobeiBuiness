package com.liemi.basemall.data.entity;

import java.io.Serializable;

public class ExchangeGoodEntity implements Serializable {
    private String specifications;
    private String img_url;
    private String price;
    private String size;
    private String title;


    public String getPriceString(){
        return price;
    }

    public String getSpecificationsString(){
        return specifications;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
