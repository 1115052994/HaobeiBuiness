package com.netmi.workerbusiness.data.entity.home.offlineorder;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

public class OfflineOrderEvaluationEntity extends BaseEntity {

    /**
     * "commet_id": "12",
     * "item_id": "11",
     * "uid": "1623",
     * "level": "2",//星级
     * "content": "不错哦",//评论内容
     * "flag": "0",//是否有图：0没有；1有
     * "create_time": "2019-10-11 11:43:54",//添加时间
     * "to_commet_id": "0",//被回复评论编号，没有则不填
     * "u": {
     * "uid": "1623",
     * "nickname": "Near",//昵称
     * "head_url": "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK9BHsYJRKhYWGicDqKWukWYics7mQohWXK2ia95RoGKMHrtxB2wNJtiac5qbiaL7kum1OXjkibRfTUx3ibg/132",//头像
     * "level": "0",
     * "sex": "3",
     * "sign_name": null,
     * "phone": "18856855449"
     * },
     * "to_commet": null,//回复内容
     * "meCommetImgs": []//图片
     */

    private String commet_id;
    private String item_id;
    private String uid;
    private int level;
    private String content;
    private String flag;
    private String create_time;
    private String to_commet_id;
    private String video_url;
    private UBean u;
    private toCommentBean to_commet;
    private String item_url;
    private List<String> meCommetImgs;

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

    public String getTo_commet_id() {
        return to_commet_id;
    }

    public void setTo_commet_id(String to_commet_id) {
        this.to_commet_id = to_commet_id;
    }

    public UBean getU() {
        return u;
    }

    public void setU(UBean u) {
        this.u = u;
    }

    public toCommentBean getTo_commet() {
        return to_commet;
    }

    public void setTo_commet(toCommentBean to_commet) {
        this.to_commet = to_commet;
    }

    public List<String> getMeCommetImgs() {
        return meCommetImgs;
    }

    public void setMeCommetImgs(List<String> meCommetImgs) {
        this.meCommetImgs = meCommetImgs;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getItem_url() {
        return item_url;
    }

    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }

    public static class toCommentBean{

        /**
         * commet_id : 50
         * item_id : 47
         * uid : 1718
         * content : 125
         * flag : 0
         * create_time : 2019-10-25 10:03:47
         * to_commet_id : 40
         */

        private String commet_id;
        private String item_id;
        private String uid;
        private String content;
        private String flag;
        private String create_time;
        private String to_commet_id;

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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getTo_commet_id() {
            return to_commet_id;
        }

        public void setTo_commet_id(String to_commet_id) {
            this.to_commet_id = to_commet_id;
        }
    }

    public static class UBean {
        /**
         * uid : 1623
         * nickname : Near
         * head_url : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK9BHsYJRKhYWGicDqKWukWYics7mQohWXK2ia95RoGKMHrtxB2wNJtiac5qbiaL7kum1OXjkibRfTUx3ibg/132
         * level : 0
         * sex : 3
         * sign_name : null
         * phone : 18856855449
         */

        private String uid;
        private String nickname;
        private String head_url;
        private String level;
        private String sex;
        private Object sign_name;
        private String phone;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHead_url() {
            return head_url;
        }

        public void setHead_url(String head_url) {
            this.head_url = head_url;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Object getSign_name() {
            return sign_name;
        }

        public void setSign_name(Object sign_name) {
            this.sign_name = sign_name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
