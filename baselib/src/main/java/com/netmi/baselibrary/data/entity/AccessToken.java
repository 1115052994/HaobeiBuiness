package com.netmi.baselibrary.data.entity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 14:10
 * 修改备注：
 */
public class AccessToken {

    /**
     * API凭证 校验用户登录时效
     */
    private String token;

    /**
     * 云信token
     */
    private String yunxin_token;

    /**
     * 云信通信id
     */
    private String accid;

    /**
     * access_token 有效开始时间 ：2017-03-09 09:49:55
     */
    private String start_time;

    /**
     * access_token 有效结束时间 ：2017-03-09 11:49:55
     */
    private String end_time;

    /**
     * access_token 用户uid
     */
    private String uid;

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AccessToken(){}

    public AccessToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String access_token) {
        this.token = access_token;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getYunxin_token() {
        return yunxin_token;
    }

    public void setYunxin_token(String yunxin_token) {
        this.yunxin_token = yunxin_token;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "token='" + token + '\'' +
                ", yunxin_token='" + yunxin_token + '\'' +
                ", accid='" + accid + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", uid='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
