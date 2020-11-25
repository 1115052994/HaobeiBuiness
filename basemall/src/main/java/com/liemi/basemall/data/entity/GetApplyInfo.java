package com.liemi.basemall.data.entity;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/18
 * 修改备注：
 */
public class GetApplyInfo extends BaseEntity {


    /**
     * name : 串意十足烧烤
     * real_name : 谢志国
     * idcard : 320323198508217713
     * front_card_url : https://panjinren.oss-cn-hangzhou.aliyuncs.com/liemi/A6942BD0-7273-4924-B1D2-C94A84A10068.jpg
     * back_card_url : https://panjinren.oss-cn-hangzhou.aliyuncs.com/liemi/F566C5A4-959F-4FA7-BDB8-80B834D48496.jpg
     * hold_card_url :
     * license_num :
     * license_url : https://panjinren.oss-cn-hangzhou.aliyuncs.com/liemi/BC32FE26-DA64-4874-901F-2715508631E2.jpg
     */

    private String name;
    private String real_name;
    private String idcard;
    private String front_card_url;
    private String back_card_url;
    private String hold_card_url;
    private String license_num;
    private String license_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getFront_card_url() {
        return front_card_url;
    }

    public void setFront_card_url(String front_card_url) {
        this.front_card_url = front_card_url;
    }

    public String getBack_card_url() {
        return back_card_url;
    }

    public void setBack_card_url(String back_card_url) {
        this.back_card_url = back_card_url;
    }

    public String getHold_card_url() {
        return hold_card_url;
    }

    public void setHold_card_url(String hold_card_url) {
        this.hold_card_url = hold_card_url;
    }

    public String getLicense_num() {
        return license_num;
    }

    public void setLicense_num(String license_num) {
        this.license_num = license_num;
    }

    public String getLicense_url() {
        return license_url;
    }

    public void setLicense_url(String license_url) {
        this.license_url = license_url;
    }
}
