package com.netmi.workerbusiness.im.session.extension;

import com.alibaba.fastjson.JSONObject;

public class OrderAttachment extends CustomAttachment{
    /**
     * {"title":"xx","img_url":"xx","order_no":"xx"}
     */
    private String title;
    private String imgUrl;
    private String orderNo;

    private static final String KEY_TITLE = "title";
    private static final String KEY_ORDER_NO= "orderNo";
    private static final String KEY_IMG_URL= "imgUrl";

    OrderAttachment(int type) {
        super(type);
    }


    public OrderAttachment(int type, String title, String imgUrl, String orderNo) {
        super(type);
        this.title = title;
        this.imgUrl = imgUrl;
        this.orderNo = orderNo;
    }

    @Override
    protected void parseData(JSONObject data) {
        title=data.getString(KEY_TITLE);
        orderNo=data.getString(KEY_ORDER_NO);
        imgUrl=data.getString(KEY_IMG_URL);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_TITLE, title);
        data.put(KEY_ORDER_NO, orderNo);
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

    public String getOrderNo() {


        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
