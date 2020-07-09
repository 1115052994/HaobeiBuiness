package com.netmi.workerbusiness.data.event;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/23
 * 修改备注：
 */
public class StoreRemarkEvent {

    private String remark;

    public StoreRemarkEvent(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
