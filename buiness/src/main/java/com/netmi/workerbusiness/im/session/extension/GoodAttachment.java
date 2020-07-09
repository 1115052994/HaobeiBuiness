package com.netmi.workerbusiness.im.session.extension;

import com.alibaba.fastjson.JSONObject;

public class GoodAttachment extends CustomAttachment {
    /**
     * {"title":"xx","img_url":"xx","price":"xx.xx"}
     */
    private String title;
    private String imgUrl;
    private String price;


    private static final String KEY_TITLE = "title";
    private static final String KEY_PRICE = "price";
    private static final String KEY_IMG_URL = "img_url";

    GoodAttachment(int type) {
        super(type);
    }

    public GoodAttachment(int type, String title, String imgUrl, String price) {
        super(type);
        this.title = title;
        this.imgUrl = imgUrl;
        this.price = price;
    }

    @Override
    protected void parseData(JSONObject data) {
        title = data.getString(KEY_TITLE);
        price = data.getString(KEY_PRICE);
        imgUrl = data.getString(KEY_IMG_URL);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_TITLE, title);
        data.put(KEY_PRICE, price);
        data.put(KEY_IMG_URL, imgUrl);
        return data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

