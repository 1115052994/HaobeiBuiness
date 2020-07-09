package com.netmi.workerbusiness.data.entity.mine;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class TransactionEntity extends BaseEntity {
    /**
     * id : 9
     * income_type : 2
     * uid : 1758
     * shop_id : 278
     * title : 提现
     * price : -100.00
     * type : 2
     * order_id : 0
     * order_sku_id : 0
     * status : 1
     * settlement_status : 0
     * create_time : 2019-09-10 16:16:44
     * update_time : 2019-09-10 16:16:44
     */

    private int id;
    private int income_type;
    private String uid;
    private int shop_id;
    private String title;
    private String price;
    private int type;
    private String order_id;
    private String order_sku_id;
    private int status;
    private String statuStr;
    private int settlement_status;
    private String create_time;
    private String update_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIncome_type() {
        return income_type;
    }

    public void setIncome_type(int income_type) {
        this.income_type = income_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sku_id() {
        return order_sku_id;
    }

    public void setOrder_sku_id(String order_sku_id) {
        this.order_sku_id = order_sku_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSettlement_status() {
        return settlement_status;
    }

    public void setSettlement_status(int settlement_status) {
        this.settlement_status = settlement_status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getStatuStr() {
//        // "status": 1,// 1：待审核 2：审核通过 3：审核失败； 4：提现处理中 5：提现到帐 6：提现失败
//        if (status == 1) {
//            return "待审核";
//        } else if (status == 2) {
//            return "审核通过";
//        } else if (status == 3) {
//            return "审核失败";
//        } else if (status == 4) {
//            return "提现处理中";
//        } else if (status == 5) {
//            return "提现到账";
//        } else if (status == 6) {
//            return "提现失败";
//        }
        if (settlement_status == 3) {
            return "已退款";
        }
        return statuStr;
    }

    public void setStatuStr(String statuStr) {
        this.statuStr = statuStr;
    }
}
