package com.liemi.basemall.data.entity.good;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/10/16 19:52
 * 修改备注：
 */
public class SpecsEntity {

    private String prop_name;
    private String prop_id;
    private List<ChildrenBean> children;

    public String getProp_name() {
        return prop_name;
    }

    public void setProp_name(String prop_name) {
        this.prop_name = prop_name;
    }

    public String getProp_id() {
        return prop_id;
    }

    public void setProp_id(String prop_id) {
        this.prop_id = prop_id;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public static class ChildrenBean {
        /**
         * value_id : 1900
         * prop_id : 1145
         * value_name : S
         */

        private String value_id;
        private String prop_id;
        private String value_name;
        private int option = 1;
        private boolean checked;

        public int getOption() {
            return option;
        }

        public void setOption(int option) {
            this.option = option;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getValue_id() {
            return value_id;
        }

        public void setValue_id(String value_id) {
            this.value_id = value_id;
        }

        public String getProp_id() {
            return prop_id;
        }

        public void setProp_id(String prop_id) {
            this.prop_id = prop_id;
        }

        public String getValue_name() {
            return value_name;
        }

        public void setValue_name(String value_name) {
            this.value_name = value_name;
        }
    }
}
