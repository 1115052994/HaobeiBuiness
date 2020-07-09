package com.netmi.workerbusiness.data.entity.home;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class OfflineOrderDataEntity extends BaseEntity {
    /**
     * waiting_used : 0
     * waiting_evaluate : 0
     * complete : 3
     */
    public static final String ZERO = "0";

    private String waiting_used;
    private String waiting_evaluate;
    private String complete;

    public String getWaiting_used() {
        return waiting_used;
    }

    public void setWaiting_used(String waiting_used) {
        this.waiting_used = waiting_used;
    }

    public String getWaiting_evaluate() {
        return waiting_evaluate;
    }

    public void setWaiting_evaluate(String waiting_evaluate) {
        this.waiting_evaluate = waiting_evaluate;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }
}
