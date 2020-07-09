package com.liemi.basemall.data.entity.user;
/*
* 手续费
* */
public class HandlingChargeEntity {
    //金额
    private String amount;
    //手续费
    private String fee;
    //实际到账金额
    private String amount_end;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAmount_end() {
        return amount_end;
    }

    public void setAmount_end(String amount_end) {
        this.amount_end = amount_end;
    }
}
