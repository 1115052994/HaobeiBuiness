package com.liemi.basemall.data.entity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/8 12:08
 * 修改备注：
 */
public class OrderCountEntity {

    //设置邮费
    private String meail_num;
    //代付款
    private String obligation_num;
    //代发货
    private String sendgoods_num;
    //待收货
    private String getgoods_num;
    //带评价
    private String assess_num;
    //退款退货
    private String refund_num;

    public String getMeail_num() {
        return meail_num;
    }

    public void setMeail_num(String meail_num) {
        this.meail_num = meail_num;
    }

    public String getObligation_num() {
        return obligation_num;
    }

    public void setObligation_num(String obligation_num) {
        this.obligation_num = obligation_num;
    }

    public String getSendgoods_num() {
        return sendgoods_num;
    }

    public void setSendgoods_num(String sendgoods_num) {
        this.sendgoods_num = sendgoods_num;
    }

    public String getGetgoods_num() {
        return getgoods_num;
    }

    public void setGetgoods_num(String getgoods_num) {
        this.getgoods_num = getgoods_num;
    }

    public String getAssess_num() {
        return assess_num;
    }

    public void setAssess_num(String assess_num) {
        this.assess_num = assess_num;
    }

    public String getRefund_num() {
        return refund_num;
    }

    public void setRefund_num(String refund_num) {
        this.refund_num = refund_num;
    }
}

