package com.netmi.workerbusiness.data.entity;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/4/10
 * 修改备注：
 */
public class CateGoryVerifyEntity extends BaseEntity {

    /**
     * mcid : 294
     * name : 美食
     * pid : 0
     * level : 1
     * sequence : 1
     * sw : 1
     * create_time : 2019-10-12 11:50:32
     * update_time : 2019-10-30 17:28:06
     * is_home : 0
     * server_type : 1
     * is_total : 0
     * del_flag : 0
     * price : 1000.00
     * category : [{"mcid":"298","name":"快餐小吃","pid":"294","level":"1","sequence":"1","sw":"1","create_time":"2019-10-12 13:37:02","update_time":"2019-10-12 13:37:02","is_home":"0","server_type":"1","is_total":"0","del_flag":"0","price":"1000.00"},{"mcid":"304","name":"炸鸡","pid":"294","level":"1","sequence":"1","sw":"1","create_time":"2019-10-22 10:49:54","update_time":"2019-10-22 10:49:54","is_home":"0","server_type":"1","is_total":"0","del_flag":"0","price":"1000.00"},{"mcid":"305","name":"饺子","pid":"294","level":"1","sequence":"2","sw":"1","create_time":"2019-10-22 10:50:05","update_time":"2019-10-22 10:50:05","is_home":"0","server_type":"1","is_total":"0","del_flag":"0","price":"1000.00"},{"mcid":"307","name":"炸鸡","pid":"294","level":"1","sequence":"1","sw":"1","create_time":"2019-11-10 17:27:28","update_time":"2019-11-10 17:27:28","is_home":"0","server_type":"1","is_total":"0","del_flag":"0","price":"0.10"}]
     */

    private String mcid;
    private String name;
    private String pid;
    private String level;
    private String sequence;
    private String sw;
    private String create_time;
    private String update_time;
    private String is_home;
    private String server_type;
    private String is_total;
    private String del_flag;
    private String price;
    private List<CategoryBean> category;
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

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getIs_home() {
        return is_home;
    }

    public void setIs_home(String is_home) {
        this.is_home = is_home;
    }

    public String getServer_type() {
        return server_type;
    }

    public void setServer_type(String server_type) {
        this.server_type = server_type;
    }

    public String getIs_total() {
        return is_total;
    }

    public void setIs_total(String is_total) {
        this.is_total = is_total;
    }

    public String getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public static class CategoryBean {
        /**
         * mcid : 298
         * name : 快餐小吃
         * pid : 294
         * level : 1
         * sequence : 1
         * sw : 1
         * create_time : 2019-10-12 13:37:02
         * update_time : 2019-10-12 13:37:02
         * is_home : 0
         * server_type : 1
         * is_total : 0
         * del_flag : 0
         * price : 1000.00
         */

        private String mcid;
        private String name;
        private String pid;
        private String level;
        private String sequence;
        private String sw;
        private String create_time;
        private String update_time;
        private String is_home;
        private String server_type;
        private String is_total;
        private String del_flag;
        private String price;
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

        public String getSw() {
            return sw;
        }

        public void setSw(String sw) {
            this.sw = sw;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getIs_home() {
            return is_home;
        }

        public void setIs_home(String is_home) {
            this.is_home = is_home;
        }

        public String getServer_type() {
            return server_type;
        }

        public void setServer_type(String server_type) {
            this.server_type = server_type;
        }

        public String getIs_total() {
            return is_total;
        }

        public void setIs_total(String is_total) {
            this.is_total = is_total;
        }

        public String getDel_flag() {
            return del_flag;
        }

        public void setDel_flag(String del_flag) {
            this.del_flag = del_flag;
        }

        public String getPrice() {
            return Double.valueOf(price) * 100 + "%";
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
    }
}
