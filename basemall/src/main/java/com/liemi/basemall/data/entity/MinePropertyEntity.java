package com.liemi.basemall.data.entity;

import java.io.Serializable;

/*
* 资产信息
* */
public class MinePropertyEntity implements Serializable{
    private String amount;
    private String spend_msg;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSpend_msg() {
        return spend_msg;
    }

    public void setSpend_msg(String spend_msg) {
        this.spend_msg = spend_msg;
    }
}
