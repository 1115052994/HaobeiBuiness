package com.liemi.basemall.data.entity.user;

import java.util.List;

/*
* 常见问题列表数据
* */
public class CommonQuestionListEntity {
    private String total_pages;
    private List<CommonQustionEntity> list;

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public List<CommonQustionEntity> getList() {
        return list;
    }

    public void setList(List<CommonQustionEntity> list) {
        this.list = list;
    }

    /*
    * 常见问题
    * */
    public static class CommonQustionEntity{
        private String id;
        private String title;
        private String type;
        private String content;
        private String url;
        private String phone;
        private String create_time;
        private String update_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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
    }
}
