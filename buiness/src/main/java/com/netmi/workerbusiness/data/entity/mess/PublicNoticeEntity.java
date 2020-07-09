package com.netmi.workerbusiness.data.entity.mess;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/24
 * 修改备注：
 */
public class PublicNoticeEntity extends BaseEntity {

    /**
     * id : 18226
     * notice_id : 63798
     * is_read : 0
     * send_id : 1758
     * type : 1
     * title : 恭喜您商户信用分上涨10分
     * content : <p>测试</p>
     * create_time : 2019-08-23 15:38:08
     * update_time : 2019-09-06 14:30:09
     * remark : 恭喜您商户信用分上涨10分
     * order_type :  "11", //0表示普通消息；11表示线下订单消息
     * link_type : 2
     * param : /notice/notice/info?id=63798
     * notice : {"id":"63798","title":"恭喜您商户信用分上涨10分","content":"<p>测试<\/p>","create_time":"2019-08-23 15:38:08","update_time":"2019-09-06 14:30:09","send_status":"2","type":"1","remark":"恭喜您商户信用分上涨10分","link_type":"2","status":"1","send_num":"0","read_num":"0","param":"/notice/notice/info?id=63798","show_img":"","push_type":"2"}
     * read_num : 0
     */

    private String id;
    private String notice_id;
    private String is_read;
    private String send_id;
    private int type;
    private String title;
    private String content;
    private String create_time;
    private String update_time;
    private String remark;
    private String link_type;
    private String order_type;
    private String param;
    private NoticeBean notice;
    private int read_num;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public NoticeBean getNotice() {
        return notice;
    }

    public void setNotice(NoticeBean notice) {
        this.notice = notice;
    }

    public int getRead_num() {
        return read_num;
    }

    public void setRead_num(int read_num) {
        this.read_num = read_num;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public static class NoticeBean {
        /**
         * id : 63798
         * title : 恭喜您商户信用分上涨10分
         * content : <p>测试</p>
         * create_time : 2019-08-23 15:38:08
         * update_time : 2019-09-06 14:30:09
         * send_status : 2
         * type : 1
         * remark : 恭喜您商户信用分上涨10分
         * link_type : 2
         * status : 1
         * send_num : 0
         * read_num : 0
         * param : /notice/notice/info?id=63798
         * show_img :
         * push_type : 2
         */

        private String id;
        private String title;
        private String content;
        private String create_time;
        private String update_time;
        private String send_status;
        private String type;
        private String remark;
        private String link_type;
        private String status;
        private String send_num;
        private String read_num;
        private String param;
        private String show_img;
        private String push_type;

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

        public String getSend_status() {
            return send_status;
        }

        public void setSend_status(String send_status) {
            this.send_status = send_status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSend_num() {
            return send_num;
        }

        public void setSend_num(String send_num) {
            this.send_num = send_num;
        }

        public String getRead_num() {
            return read_num;
        }

        public void setRead_num(String read_num) {
            this.read_num = read_num;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String getShow_img() {
            return show_img;
        }

        public void setShow_img(String show_img) {
            this.show_img = show_img;
        }

        public String getPush_type() {
            return push_type;
        }

        public void setPush_type(String push_type) {
            this.push_type = push_type;
        }
    }
}
