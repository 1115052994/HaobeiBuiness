package com.netmi.workerbusiness.data.event;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/3
 * 修改备注：
 */
public class PostageListEntity extends BaseEntity {
    /**
     * t_id : 9
     * template_name : 自定义模板
     * type : 1
     */

//    private List<ListBean> list;
//
//    public List<ListBean> getSecond_category() {
//        return list;
//    }
//
//    public void setSecond_category(List<ListBean> list) {
//        this.list = list;
//    }
//
//    public static class ListBean extends BaseEntity {
//        /**
//         * id : 9
//         * template_name : 自定义模板
//         * type : 1
//         */
        private String t_id;
        private String template_name;
        private String type;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
//    }


}
