package com.netmi.workerbusiness.data.entity.walllet;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class BankBean extends BaseEntity {


    /**
     * errcode : 0
     * errmsg : success.
     * data : {"list":[{"id":"1","name":"工商银行","code":"1100"},{"id":"2","name":"农业银行","code":"1101"},{"id":"3","name":"招商银行","code":"1102"},{"id":"4","name":"兴业银行","code":"1103"},{"id":"5","name":"中信银行","code":"1104"},{"id":"6","name":"中国银行","code":"1107"},{"id":"7","name":"交通银行","code":"1108"},{"id":"8","name":"浦发银行","code":"1109"},{"id":"9","name":"民生银行","code":"1110"},{"id":"10","name":"华夏银行","code":"1111"},{"id":"11","name":"光大银行","code":"1112"},{"id":"12","name":"北京银行","code":"1113"},{"id":"13","name":"广发银行","code":"1114"},{"id":"14","name":"南京银行","code":"1115"},{"id":"15","name":"上海银行","code":"1116"},{"id":"16","name":"杭州银行","code":"1117"},{"id":"17","name":"宁波银行","code":"1118"},{"id":"18","name":"邮储银行","code":"1119"},{"id":"19","name":"浙商银行","code":"1120"},{"id":"20","name":"平安银行","code":"1121"},{"id":"21","name":"东亚银行","code":"1122"},{"id":"22","name":"渤海银行","code":"1123"},{"id":"23","name":"北京农商行","code":"1124"},{"id":"24","name":"浙江泰隆商业银行","code":"1127"},{"id":"25","name":"中国建设银行","code":"1106"}]}
     */
    /**
     * id : 1
     * name : 工商银行
     * code : 1100
     */

    private String id;
    private String name;
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

