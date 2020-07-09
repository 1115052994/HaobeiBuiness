package com.liemi.basemall.data.entity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/10/22 11:50
 * 修改备注：
 */
public class XGNotifyEntity {

    /**
     * type : 100
     * data : {"title":"商品上新","content":{"content":"试试","status":1}}
     */

    private int type;
    private DataBean data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 商品上新
         * content : {"content":"试试","status":1}
         */

        private String title;
        private ContentBean content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean {
            /**
             * content : 试试
             * status : 1
             */

            private String content;
            private int status;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
