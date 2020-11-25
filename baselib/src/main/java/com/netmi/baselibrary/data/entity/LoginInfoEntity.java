package com.netmi.baselibrary.data.entity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/4 18:11
 * 修改备注：
 */
public class LoginInfoEntity {
    public LoginInfoEntity() {

    }

    public LoginInfoEntity(String login) {
        this.login = login;
    }

    public LoginInfoEntity(String login, String password) {
        this.login = login;
        this.password = password;
    }

//    public LoginInfoEntity(String openId){
//        this.openid = openId;
//    }

    /**
     * 用户名
     */
    private String login;
    /**
     * 密码
     */
    private String password;
    /**
     * 平台类型
     */
    private String type = "2"; //平台类型（1:IOS,2:安卓,3:PC,4:移动H5）

    /**
     * 微信登录
     */
    private String openid;
    /**
     * 是否开启语音播报
     */
    private boolean isOpen = true;



    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    /**
     * 登录状态
     */
    private int logoutState;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getLogoutState() {
        return logoutState;
    }

    public void setLogoutState(int logoutState) {
        this.logoutState = logoutState;
    }
}
