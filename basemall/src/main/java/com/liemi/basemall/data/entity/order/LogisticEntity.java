package com.liemi.basemall.data.entity.order;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/9 17:31
 * 修改备注：
 */
public class LogisticEntity {


    private String code;
    private String no = "";
    private String type;
    private int state;
    private String name = "";
    private String site;
    private String phone;
    private String company;
    private String logo;
    private List<ListBean> info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<ListBean> getList() {
        return info;
    }

    public void setList(List<ListBean> list) {
        this.info = list;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public static class ListBean {
        /**
         * content : 【广州市】  快件离开 【广州中心】 发往 【杭州中转部】
         * time : 2018-10-12 05:34:59
         */

        private String status;
        private String time;

        public String getContent() {
            return status;
        }

        public void setContent(String content) {
            this.status = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
