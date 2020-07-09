package com.liemi.basemall.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/24 0:00
 * 修改备注：
 */
public class WXPayData {

    /**
     * appid : wx4d7a001c23e65457
     * partnerid : 1516478391
     * prepayid : wx150951180022534fae904c113906911199
     * package : Sign=WXPay
     * noncestr : E2aDIMcluJnrS9Xjmz
     * timestamp : 1539568279
     * sign : 1FB902785C35C628778EEA4142C2F9BD
     */

    private String appid;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private String timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
