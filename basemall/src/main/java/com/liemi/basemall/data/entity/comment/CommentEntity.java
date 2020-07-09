package com.liemi.basemall.data.entity.comment;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/24 15:43
 * 修改备注：
 */
public class CommentEntity extends BaseEntity {

    private String commet_id;
    private String item_id;
    private String uid;
    private String content;
    private String flag;
    private String create_time;
    private String to_commet_id;
    private UBean u;
    private ToCommetBean to_commet;
    private List<String> meCommetImgs;
    private String value_names;
    private int sum_commet;
    private int level;
    private String total_level; //商品好评度

    public String getTotal_level() {
        return total_level;
    }

    public void setTotal_level(String total_level) {
        this.total_level = total_level;
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

    public UBean getU() {
        return u;
    }

    public void setU(UBean u) {
        this.u = u;
    }

    public ToCommetBean getTo_commet() {
        return to_commet;
    }

    public void setTo_commet(ToCommetBean to_commet) {
        this.to_commet = to_commet;
    }

    public List<String> getMeCommetImgs() {
        return meCommetImgs;
    }

    public void setMeCommetImgs(List<String> meCommetImgs) {
        this.meCommetImgs = meCommetImgs;
    }

    public String getValue_names() {
        return value_names;
    }

    public void setValue_names(String value_names) {
        this.value_names = value_names;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSum_commet() {
        return sum_commet;
    }

    public void setSum_commet(int sum_commet) {
        this.sum_commet = sum_commet;
    }

    public static class UBean implements Serializable{

        private String uid;
        private String nickname;
        private String head_url;
        private String level;
        private String sex;
        private String sign_name;

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

        public String getSign_name() {
            return sign_name;
        }

        public void setSign_name(String sign_name) {
            this.sign_name = sign_name;
        }
    }

    public static class ToCommetBean implements Serializable{

        private String commet_id;
        private String item_id;
        private String uid;
        private String content;
        private String flag;
        private String create_time;
        private String to_commet_id;
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

        public List<String> getMeCommetImgs() {
            return meCommetImgs;
        }

        public void setMeCommetImgs(List<String> meCommetImgs) {
            this.meCommetImgs = meCommetImgs;
        }
    }
}
