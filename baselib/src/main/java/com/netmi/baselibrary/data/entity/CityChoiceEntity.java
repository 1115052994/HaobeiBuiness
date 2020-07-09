package com.netmi.baselibrary.data.entity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/30 17:46
 * 修改备注：
 */
public class CityChoiceEntity {

    private String id;
    private String name;
    private String level;
    private String upid;
    private String character;
    private List<CListBean> c_list;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUpid() {
        return upid;
    }

    public void setUpid(String upid) {
        this.upid = upid;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public List<CListBean> getC_list() {
        return c_list;
    }

    public void setC_list(List<CListBean> c_list) {
        this.c_list = c_list;
    }

    public static class CListBean {

        private String id;
        private String name;
        private String level;
        private String upid;
        private String character;
        private List<DListBean> d_list;

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

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getUpid() {
            return upid;
        }

        public void setUpid(String upid) {
            this.upid = upid;
        }

        public String getCharacter() {
            return character;
        }

        public void setCharacter(String character) {
            this.character = character;
        }

        public List<DListBean> getD_list() {
            return d_list;
        }

        public void setD_list(List<DListBean> d_list) {
            this.d_list = d_list;
        }

        public static class DListBean {

            private String id;
            private String name;
            private String level;
            private String upid;
            private String character;

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

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getUpid() {
                return upid;
            }

            public void setUpid(String upid) {
                this.upid = upid;
            }

            public String getCharacter() {
                return character;
            }

            public void setCharacter(String character) {
                this.character = character;
            }
        }
    }
}
