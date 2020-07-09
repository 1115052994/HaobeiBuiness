package com.netmi.workerbusiness.data.entity.home.lineorder;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/23
 * 修改备注：
 */
public class LogisticEntity extends BaseEntity {
    /**
     * company : 顺丰
     * code : 251618952261
     * info : [{"status":"[临沂市]已签收,感谢使用顺丰,期待再次为您服务","time":"2018-11-27 11:04:14"},{"status":"[临沂市]快件派送不成功(因电话无人接听/关机/无信号，暂无法联系到收方客户),正在处理中,待再次派送","time":"2018-11-27 10:58:47"},{"status":"[临沂市]快件交给陈士超，正在派送途中（联系电话：15753921686）","time":"2018-11-27 08:41:05"},{"status":"快件到达 【临沂市兰陵营业点 】","time":"2018-11-27 08:12:42"},{"status":"[临沂市]快件已发车","time":"2018-11-27 06:18:41"},{"status":"快件在【临沂批发城集散中心】已装车,准备发往 【临沂市兰陵营业点 】","time":"2018-11-27 06:02:55"},{"status":"[临沂市]快件到达 【临沂批发城集散中心】","time":"2018-11-27 03:51:14"},{"status":"[潍坊市]快件已发车","time":"2018-11-26 23:34:28"},{"status":"[潍坊市]快件在【潍坊坊子新区集散中心】已装车,准备发往 【临沂批发城集散中心】","time":"2018-11-26 23:30:04"},{"status":"[潍坊市]快件到达 【潍坊坊子新区集散中心】","time":"2018-11-26 22:35:15"},{"status":"[东莞市]快件已发车","time":"2018-11-25 18:38:41"},{"status":"[东莞市]快件在【东莞沙田集散中心】已装车,准备发往 【潍坊坊子新区集散中心】","time":"2018-11-25 15:53:13"},{"status":"[东莞市]快件到达 【东莞沙田集散中心】","time":"2018-11-25 15:31:24"},{"status":"[广州市]快件已发车","time":"2018-11-25 14:12:59"},{"status":"[广州市]快件在【广州新塘集散中心】已装车,准备发往 【东莞沙田集散中心】","time":"2018-11-25 14:00:29"},{"status":"[广州市]快件到达 【广州新塘集散中心】","time":"2018-11-25 13:24:19"},{"status":"[广州市]快件已发车","time":"2018-11-25 12:54:18"},{"status":"[广州市]快件在【广州简村速运营业部】已装车,准备发往 【广州新塘集散中心】","time":"2018-11-25 11:52:42"},{"status":"[广州市]顺丰速运 已收取快件","time":"2018-11-25 10:50:27"}]
     */

    private String company;
    private String code;
    private List<InfoBean> info;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * status : [临沂市]已签收,感谢使用顺丰,期待再次为您服务
         * time : 2018-11-27 11:04:14
         */

        private String status;
        private String time;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
