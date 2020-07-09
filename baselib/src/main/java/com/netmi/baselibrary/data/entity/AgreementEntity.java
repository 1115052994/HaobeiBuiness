package com.netmi.baselibrary.data.entity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 10:13
 * 修改备注：
 */
public class AgreementEntity {

    private String id;
    private String title;
    private String remark;
    //展现方式 1：无点击效果 2：点击跳转链接 3：点击跳转富文本
    private String link_type;
    //跳转链接
    private String param;
    //内容（富文本）
    private String content;
    //协议类型：1：登录注册协议
    private String type;
    private String action_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction_id() {
        return action_id;
    }

    public void setAction_id(String action_id) {
        this.action_id = action_id;
    }
}
