package com.netmi.workerbusiness.data.entity.home.category;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 19:39
 * 修改备注：
 */
public class GoodsOneCateEntity extends BaseEntity {

    /**
     * mcid : 228
     * name : 家居
     * pid : 0
     * level : 1
     * sequence : 5
     * img_url : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15390711043052.jpg
     * is_home : 0
     * is_total : 0
     * shop_id : 0
     * second_category : [{"mcid":"229","name":"全部","pid":"228","level":"1","sequence":"0","img_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15204995973056.jpg","is_home":"0","is_total":"1","shop_id":"0"},{"mcid":"232","name":"衣柜","pid":"228","level":"1","sequence":null,"img_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15390713662002.jpg","is_home":"1","is_total":"0","shop_id":"0"},{"mcid":"230","name":"沙发","pid":"228","level":"1","sequence":null,"img_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15390711466254.jpg","is_home":"1","is_total":"0","shop_id":"0"}]
     */

    private String mcid;
    private String name;
    private String pid;
    private String level;
    private String sequence;
    private String img_url;
    private String is_home;
    private String is_total;
    private String shop_id;
    private List<SecondCategoryBean> second_category;
    private boolean check;

    public String getMcid() {
        return mcid;
    }

    public void setMcid(String mcid) {
        this.mcid = mcid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getIs_home() {
        return is_home;
    }

    public void setIs_home(String is_home) {
        this.is_home = is_home;
    }

    public String getIs_total() {
        return is_total;
    }

    public void setIs_total(String is_total) {
        this.is_total = is_total;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public List<SecondCategoryBean> getSecond_category() {
        return second_category;
    }

    public void setSecond_category(List<SecondCategoryBean> second_category) {
        this.second_category = second_category;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public static class SecondCategoryBean extends BaseEntity{
        /**
         * mcid : 229
         * name : 全部
         * pid : 228
         * level : 1
         * sequence : 0
         * img_url : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15204995973056.jpg
         * is_home : 0
         * is_total : 1
         * shop_id : 0
         */

        private String mcid;
        private String name;
        private String pid;
        private String level;
        private String sequence;
        private String img_url;
        private String is_home;
        private String is_total;
        private String shop_id;
        private boolean check;

        public String getMcid() {
            return mcid;
        }

        public void setMcid(String mcid) {
            this.mcid = mcid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getIs_home() {
            return is_home;
        }

        public void setIs_home(String is_home) {
            this.is_home = is_home;
        }

        public String getIs_total() {
            return is_total;
        }

        public void setIs_total(String is_total) {
            this.is_total = is_total;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
    }
}
