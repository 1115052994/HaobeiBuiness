package com.netmi.baselibrary.data.entity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/8/11
 * 修改备注：
 */
public class AnnouncementEntity extends BaseEntity {
    /**
     * //         * id : 28
     * //         * notice_id : 5
     * //         * is_read : 1
     * //         * send_id : 9
     * //         * type : 1
     * //         * title : 系统升级2
     * //         * content :
     * //         * create_time : 2017-12-06 23:40:23
     * //         * update_time : 2017-12-06 17:07:12
     * //         * remark : 123456
     * //         * link_type : 2
     * //         * param : http://sys-admin.dev/#
     * //
     */

    private String id;
    private String notice_id;
    private String is_read;
    private String send_id;
    private String type;
    private String title;
    private String content;
    private String create_time;
    private String update_time;
    private String remark;
    private String link_type;
    private String param;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
//    /**
//     * total_pages : 1
//     * is_next : 0
//     * list : [{"id":"28","notice_id":"5","is_read":"1","send_id":"9","type":"1","title":"系统升级2","content":"","create_time":"2017-12-06 23:40:23","update_time":"2017-12-06 17:07:12","remark":"123456","link_type":"2","param":"http://sys-admin.dev/#"}]
//     * read_data : {"all_no_readnum":2,"no_readnum1":"2","no_readnum2":0}
//     */
//
//    private int total_pages;
//    private int is_next;
//    private ReadDataBean read_data;
//    private List<ListBean> list;
//
//    public int getTotal_pages() {
//        return total_pages;
//    }
//
//    public void setTotal_pages(int total_pages) {
//        this.total_pages = total_pages;
//    }
//
//    public int getIs_next() {
//        return is_next;
//    }
//
//    public void setIs_next(int is_next) {
//        this.is_next = is_next;
//    }
//
//    public ReadDataBean getRead_data() {
//        return read_data;
//    }
//
//    public void setRead_data(ReadDataBean read_data) {
//        this.read_data = read_data;
//    }
//
//    public List<ListBean> getList() {
//        return list;
//    }
//
//    public void setList(List<ListBean> list) {
//        this.list = list;
//    }
//
//    public static class ReadDataBean {
//        /**
//         * all_no_readnum : 2
//         * no_readnum1 : 2
//         * no_readnum2 : 0
//         */
//
//        private int all_no_readnum;
//        private String no_readnum1;
//        private int no_readnum2;
//
//        public int getAll_no_readnum() {
//            return all_no_readnum;
//        }
//
//        public void setAll_no_readnum(int all_no_readnum) {
//            this.all_no_readnum = all_no_readnum;
//        }
//
//        public String getNo_readnum1() {
//            return no_readnum1;
//        }
//
//        public void setNo_readnum1(String no_readnum1) {
//            this.no_readnum1 = no_readnum1;
//        }
//
//        public int getNo_readnum2() {
//            return no_readnum2;
//        }
//
//        public void setNo_readnum2(int no_readnum2) {
//            this.no_readnum2 = no_readnum2;
//        }
//    }
//
//    public static class ListBean extends BaseEntity{
//        /**
//         * id : 28
//         * notice_id : 5
//         * is_read : 1
//         * send_id : 9
//         * type : 1
//         * title : 系统升级2
//         * content :
//         * create_time : 2017-12-06 23:40:23
//         * update_time : 2017-12-06 17:07:12
//         * remark : 123456
//         * link_type : 2
//         * param : http://sys-admin.dev/#
//         */
//
//        private String id;
//        private String notice_id;
//        private String is_read;
//        private String send_id;
//        private String type;
//        private String title;
//        private String content;
//        private String create_time;
//        private String update_time;
//        private String remark;
//        private String link_type;
//        private String param;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getNotice_id() {
//            return notice_id;
//        }
//
//        public void setNotice_id(String notice_id) {
//            this.notice_id = notice_id;
//        }
//
//        public String getIs_read() {
//            return is_read;
//        }
//
//        public void setIs_read(String is_read) {
//            this.is_read = is_read;
//        }
//
//        public String getSend_id() {
//            return send_id;
//        }
//
//        public void setSend_id(String send_id) {
//            this.send_id = send_id;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public String getContent() {
//            return content;
//        }
//
//        public void setContent(String content) {
//            this.content = content;
//        }
//
//        public String getCreate_time() {
//            return create_time;
//        }
//
//        public void setCreate_time(String create_time) {
//            this.create_time = create_time;
//        }
//
//        public String getUpdate_time() {
//            return update_time;
//        }
//
//        public void setUpdate_time(String update_time) {
//            this.update_time = update_time;
//        }
//
//        public String getRemark() {
//            return remark;
//        }
//
//        public void setRemark(String remark) {
//            this.remark = remark;
//        }
//
//        public String getLink_type() {
//            return link_type;
//        }
//
//        public void setLink_type(String link_type) {
//            this.link_type = link_type;
//        }
//
//        public String getParam() {
//            return param;
//        }
//
//        public void setParam(String param) {
//            this.param = param;
//        }
//    }

