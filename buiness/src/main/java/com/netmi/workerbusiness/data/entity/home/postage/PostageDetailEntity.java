package com.netmi.workerbusiness.data.entity.home.postage;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/5
 * 修改备注：
 */
public class PostageDetailEntity extends BaseEntity {
    /**
     * template_name	是	string	模板名称
     * region_conf的第一个键	是	string	默认区域配置
     * region	是	string	所选地区
     * region_name	是	string	所选地区名称
     * first_item	是	string	首件
     * first_item	是	string	首件价格
     * add_item	是	string	续件
     * add_money	是	string	续件价格
     * t_id	是	string	模板ID
     * tc_id	是	string	地域配置ID
     * region_conf	是	string	地区配置
     */
    private List<ExpressInfoBean> express_info;
    private List<RegionConfBean> region_conf;

    public List<ExpressInfoBean> getExpress_info() {
        return express_info;
    }

    public void setExpress_info(List<ExpressInfoBean> express_info) {
        this.express_info = express_info;
    }

    public List<RegionConfBean> getRegion_conf() {
        return region_conf;
    }

    public void setRegion_conf(List<RegionConfBean> region_conf) {
        this.region_conf = region_conf;
    }

    public static class ExpressInfoBean {
        /**
         * t_id : 142
         * template_name : 测试模板一
         */

        private String t_id;
        private String template_name;

        public String getT_id() {
            return t_id;
        }

        public void setT_id(String id) {
            this.t_id = id;
        }

        public String getTemplate_name() {
            return template_name;
        }

        public void setTemplate_name(String template_name) {
            this.template_name = template_name;
        }
    }

    public static class RegionConfBean extends BaseEntity {
        /**
         * tc_id : 71
         * t_id : 142
         * region : ["0"]
         * region_name : 全国默认地区
         * first_item : 1
         * first_money : 2.00
         * add_item : 3
         * add_money : 4.00
         * create_time : 2019-09-04 10:24:42
         * update_time : 2019-09-04 10:35:24
         */

        private String tc_id;
        private String t_id;
        private String region_name;
        private String first_item;
        private String first_money;
        private String add_item;
        private String add_money;
        private String create_time;
        private String update_time;
        private List<String> region;

        public String getTc_id() {
            return tc_id;
        }

        public void setTc_id(String id) {
            this.tc_id = id;
        }

        public String getT_id() {
            return t_id;
        }

        public void setT_id(String t_id) {
            this.t_id = t_id;
        }

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }

        public String getFirst_item() {
            return first_item;
        }

        public void setFirst_item(String first_item) {
            this.first_item = first_item;
        }

        public String getFirst_money() {
            return first_money;
        }

        public void setFirst_money(String first_money) {
            this.first_money = first_money;
        }

        public String getAdd_item() {
            return add_item;
        }

        public void setAdd_item(String add_item) {
            this.add_item = add_item;
        }

        public String getAdd_money() {
            return add_money;
        }

        public void setAdd_money(String add_money) {
            this.add_money = add_money;
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

        public List<String> getRegion() {
            return region;
        }

        public void setRegion(List<String> region) {
            this.region = region;
        }
    }
}
