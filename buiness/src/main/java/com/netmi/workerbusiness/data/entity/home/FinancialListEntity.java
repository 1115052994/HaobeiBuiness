package com.netmi.workerbusiness.data.entity.home;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/25
 * 修改备注：
 */
public class FinancialListEntity extends BaseEntity {

    private List<OnlineListBean> online_list;
    private List<OutlineListBean> outline_list;

    public List<OnlineListBean> getOnline_list() {
        return online_list;
    }

    public void setOnline_list(List<OnlineListBean> online_list) {
        this.online_list = online_list;
    }

    public List<OutlineListBean> getOutline_list() {
        return outline_list;
    }

    public void setOutline_list(List<OutlineListBean> outline_list) {
        this.outline_list = outline_list;
    }

    public static class OnlineListBean {
        /**
         * date : ["2019-08-31"]
         * amount : 0
         * count : 0
         */

        private double amount;
        private double count;
        private List<String> date;
        private String dataStr;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public List<String> getDate() {
            return date;
        }

        public void setDate(List<String> date) {
            this.date = date;
        }

        public String getDataStr() {
            return monthDay(date.get(0));
        }
        public void setDataStr(String dataStr) {
            this.dataStr = dataStr;
        }
    }

    public static class OutlineListBean {
        /**
         * date : ["2019-08-31"]
         * amount : 0
         * count : 0
         */

        private double amount;
        private double count;
        private List<String> date;
        private String dataStr;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public List<String> getDate() {
            return date;
        }

        public void setDate(List<String> date) {
            this.date = date;
        }
        public String getDataStr() {
            return monthDay(date.get(0));
        }
        public void setDataStr(String dataStr) {
            this.dataStr = dataStr;
        }
    }

    private static String monthDay(String date) {
        String[] strs = date.split("-");
        if (strs.length > 2)
            return strs[1] + "-" + strs[2];
        return date;
    }


}
