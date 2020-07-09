package com.netmi.workerbusiness.data.event;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/3/19
 * 修改备注：
 */
public class TeamEvent {
    private String uid;

    public TeamEvent(String uid) {
        this.uid = uid;

    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
