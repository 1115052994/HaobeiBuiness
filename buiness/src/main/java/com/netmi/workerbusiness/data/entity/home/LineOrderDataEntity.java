package com.netmi.workerbusiness.data.entity.home;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class LineOrderDataEntity extends BaseEntity {
    /**
     * obligation_num : 0
     * sendgoods_num : 0
     * getgoods_num : 0
     * assess_num : 19
     */

    private String obligation_num;
    private String sendgoods_num;
    private String getgoods_num;
    private String assess_num;

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
}
