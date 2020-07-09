package com.liemi.basemall.data.entity.order;

import java.io.Serializable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/8 17:19
 * 修改备注：
 */
public class InvoiceEntity implements Serializable {

    private int type;
    private String com_name;
    private String duty_no;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getDuty_no() {
        return duty_no;
    }

    public void setDuty_no(String duty_no) {
        this.duty_no = duty_no;
    }
}
