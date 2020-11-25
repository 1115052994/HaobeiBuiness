package com.netmi.baselibrary.data.entity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/24
 * 修改备注：
 */
public class TemplateListEntity extends BaseEntity {


    /**
     * id : 2
     * uid : 4341
     * title : 商户分账协议
     * tid : 15991221170
     * contract_id :
     * status : 0
     * add_time : 2020-09-17 11:24:17
     * update_time : 0000-00-00 00:00:00
     */

    private String id;
    private String uid;
    private String title;
    private String tid;
    private String contract_id;
    private int status;
    private String add_time;
    private String update_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
