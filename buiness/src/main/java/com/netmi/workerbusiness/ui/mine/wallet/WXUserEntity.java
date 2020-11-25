package com.netmi.workerbusiness.ui.mine.wallet;

import java.io.Serializable;

public class WXUserEntity implements Serializable {

    /**
     * unionid : oCTTuwCcz7m7nR5q_RcFfIPno8xk
     * userID : o_UcP0-rBLSlF6-7fiiMTWIG0Kvc
     * icon : http://thirdwx.qlogo.cn/mmopen/vi_32
     * expiresTime : 1551254080292
     * nickname : xxxxxx
     * token : 19_aBmod6aAUnHyqW95nyAimzMqmBTeJt9-bJFatpqrA76vTA8b3REiakL_vj4Wpz0PK-bHl870-Mx7ou_jmnjJ1GxMj8xTz-3HiNsb7hVsDOQ
     * city :
     * gender : 0
     * province :
     * refresh_token : 19_SN7w5REpR3AkHrAnM-7W2Ff8GwsOcJZQKyMx3jLbd__m2tT7TVEWDeqkH0y5R09VGVzMLtHGMoa5N0qA9o3qwrSOQg7KVbAt9wMMSAuil48
     * openid : o_UcP0-rBLSlF6-7fiiMTWIG0Kvc
     * country : 希腊
     * expiresIn : 7200
     */

    private String unionid;
    private String userID;
    private String icon;
    private String expiresTime;
    private String nickname;
    private String token;
    private String city;
    private String gender;
    private String province;
    private String refresh_token;
    private String openid;
    private String country;
    private String expiresIn;

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(String expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
