package com.netmi.workerbusiness.data.entity.haibei;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class HaibeiOrderList extends BaseEntity {


    /**
     * id : 2
     * order_no : 5456456786786786786
     * shop_id : 558
     * user_id : 3978
     * amount : 30.00
     * haibei_amount : 5.00
     * pay_type : 微信
     * status : 已付款
     * add_time : 2020-07-22
     * pay_time : 2020-07-22
     * nickname : 155****7788
     * head_url : https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15397899666447.png
     */

    private String id;
    private String order_no;
    private String shop_id;
    private String user_id;
    private String amount;
    private int haibei_amount;
    private String pay_type;
    private String status;
    private String add_time;
    private String pay_time;
    private String nickname;
    private String head_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getHaibei_amount() {
        return haibei_amount;
    }

    public void setHaibei_amount(int haibei_amount) {
        this.haibei_amount = haibei_amount;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }
}
