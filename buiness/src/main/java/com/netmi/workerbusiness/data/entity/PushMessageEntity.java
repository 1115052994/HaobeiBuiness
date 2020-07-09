package com.netmi.workerbusiness.data.entity;

/**
 * 推送消息
 */
public class PushMessageEntity {
    //普通消息
    public static final int APP_NORMAL_MESSAGE = 111;
    //app更新的消息
    public static final int APP_UPDATE_MESSAGE = 2;
    //订单信息
    public static final int APP_ORDER_MESSAGE = 100;

    //消息类型
    private int type;
    //消息内容
    private PushContent data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PushContent getData() {
        return data;
    }

    public void setData(PushContent data) {
        this.data = data;
    }

    public static class PushContent{
        private String id;
        private String title;
        private String content;

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
    }
}
