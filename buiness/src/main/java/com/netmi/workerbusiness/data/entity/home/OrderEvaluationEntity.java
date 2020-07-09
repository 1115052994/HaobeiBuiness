package com.netmi.workerbusiness.data.entity.home;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

public class OrderEvaluationEntity extends BaseEntity {

    /**
     * commet_id : 429
     * item_id : 1597
     * order_id : 3495
     * ivid : 6380
     * uid : 1613
     * level : 2
     * content : hhhh
     * flag : 1
     * create_time : 2019-09-17 15:30:38
     * update_time : 2019-09-17 15:30:38
     * to_commet_id : 0
     * verify_status : 1
     * status : 1
     * meCommetImgs : [{"id":"428","commet_id":"429","img_url":"https://liemimofang.oss-cn-hangzhou.aliyuncs.com/2018/03/21/16/17/45/90ba55edab8744d79806f2dfa02b922b.png"}]
     * img_url : https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/ABFWXZMTHK124689_1560146716.png
     * to_commet : {"commet_id":"431","item_id":"1597","order_id":"3495","ivid":"6380","uid":null,"level":"1","content":"谢谢","flag":"0","create_time":null,"update_time":null,"to_commet_id":"429","verify_status":"1","status":"1","meCommetImgs":[]}
     */

    /**
     * commet_id	int	评论ID
     * img_url	int	商品缩略图
     * level	int	1-5的五个星星等级
     * content	int	评论内容
     * meCommetImgs.img_url	int	评论图片
     * to_commet.content	int	商家回复内容
     */

    private String commet_id;
    private String item_id;
    private String order_id;
    private String ivid;
    private String uid;
    private int level;
    private String content;
    private String flag;
    private String create_time;
    private String update_time;
    private String to_commet_id;
    private String verify_status;
    private String status;
    private String img_url;
    private String video_url;
    private ReplayBean to_commet;
    private List<String> meCommetImgs;


    public List<String> getMeCommetImgs() {
        return meCommetImgs;
    }

    public void setMeCommetImgs(List<String> meCommetImgs) {
        this.meCommetImgs = meCommetImgs;
    }


    public String getCommet_id() {
        return commet_id;
    }

    public void setCommet_id(String commet_id) {
        this.commet_id = commet_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getIvid() {
        return ivid;
    }

    public void setIvid(String ivid) {
        this.ivid = ivid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    public String getTo_commet_id() {
        return to_commet_id;
    }

    public void setTo_commet_id(String to_commet_id) {
        this.to_commet_id = to_commet_id;
    }

    public String getVerify_status() {
        return verify_status;
    }

    public void setVerify_status(String verify_status) {
        this.verify_status = verify_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public ReplayBean getTo_commet() {
        return to_commet;
    }

    public void setTo_commet(ReplayBean to_commet) {
        this.to_commet = to_commet;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }


    public static class ReplayBean {
        /**
         * commet_id : 431
         * item_id : 1597
         * order_id : 3495
         * ivid : 6380
         * uid : null
         * level : 1
         * content : 谢谢
         * flag : 0
         * create_time : null
         * update_time : null
         * to_commet_id : 429
         * verify_status : 1
         * status : 1
         * meCommetImgs : []
         */

        private String commet_id;
        private String item_id;
        private String order_id;
        private String ivid;
        private Object uid;
        private String level;
        private String content;
        private String flag;
        private Object create_time;
        private Object update_time;
        private String to_commet_id;
        private String verify_status;
        private String status;
        private List<?> meCommetImgs;

        public String getCommet_id() {
            return commet_id;
        }

        public void setCommet_id(String commet_id) {
            this.commet_id = commet_id;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getIvid() {
            return ivid;
        }

        public void setIvid(String ivid) {
            this.ivid = ivid;
        }

        public Object getUid() {
            return uid;
        }

        public void setUid(Object uid) {
            this.uid = uid;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public Object getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Object create_time) {
            this.create_time = create_time;
        }

        public Object getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(Object update_time) {
            this.update_time = update_time;
        }

        public String getTo_commet_id() {
            return to_commet_id;
        }

        public void setTo_commet_id(String to_commet_id) {
            this.to_commet_id = to_commet_id;
        }

        public String getVerify_status() {
            return verify_status;
        }

        public void setVerify_status(String verify_status) {
            this.verify_status = verify_status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<?> getMeCommetImgs() {
            return meCommetImgs;
        }

        public void setMeCommetImgs(List<?> meCommetImgs) {
            this.meCommetImgs = meCommetImgs;
        }
    }

    public static class MeCommetImgsBean {
        /**
         * id : 428
         * commet_id : 429
         * img_url : https://liemimofang.oss-cn-hangzhou.aliyuncs.com/2018/03/21/16/17/45/90ba55edab8744d79806f2dfa02b922b.png
         */

        private String id;
        private String commet_id;
        private String img_url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCommet_id() {
            return commet_id;
        }

        public void setCommet_id(String commet_id) {
            this.commet_id = commet_id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}
