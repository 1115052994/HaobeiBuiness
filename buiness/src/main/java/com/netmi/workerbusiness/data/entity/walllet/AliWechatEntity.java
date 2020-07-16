package com.netmi.workerbusiness.data.entity.walllet;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/5/20
 * 修改备注：
 */
public class AliWechatEntity extends BaseEntity {

    /**
     * uid : 545   用户的id
     * phone : 111111
     * type : 1
     * name : 111
     * id : 这条数据的ID
     */

    private int uid;
    private String phone;
    private String type;
    private String name;
    private int id;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
