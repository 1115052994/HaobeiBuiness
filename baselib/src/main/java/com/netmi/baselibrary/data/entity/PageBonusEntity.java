package com.netmi.baselibrary.data.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：分页实体
 * 创建人：Simple
 * 创建时间：2017/7/18 13:40
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class PageBonusEntity<T> extends BaseEntity {

    /**
     * 总页数
     */
    private int total_pages;

    private int is_next;
    private String purchase;
    private readBean read_data;

    private String total_count;
    private int now_num;
    private int page_num;
    private int is_prev;

    //     "all_balance":"10769134.00",            //海贝流通总数
//             "confidence":"0.24",            //当前 权益指数
//             "synthesize":"100.00",            //当前 海贝指数
//             "max_confidence":"159.97",            //历史最高权益指数
//             "min_confidence":"0.00",            //历史最低权益指数
//             "max_synthesize":"259.97",            //历史最高海贝指数
//             "min_synthesize":"100.00"            //历史最低海贝指数
    private String all_balance;
    private String confidence;
    private String synthesize;
    private String max_confidence;
    private String min_confidence;
    private String max_synthesize;
    private String min_synthesize;



    private totalIncomeBean total_income;
    /**
     * total_data : {"income_amount":"1112.00","support_amount":"868.02"}
     */

    private TotalDataBean total_data;

    public totalIncomeBean getTotal_income() {
        return total_income;
    }

    public void setTotal_income(totalIncomeBean total_income) {
        this.total_income = total_income;
    }

    public String getAll_balance() {
        return all_balance;
    }

    public void setAll_balance(String all_balance) {
        this.all_balance = all_balance;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getSynthesize() {
        return synthesize;
    }

    public void setSynthesize(String synthesize) {
        this.synthesize = synthesize;
    }

    public String getMax_confidence() {
        return max_confidence;
    }

    public void setMax_confidence(String max_confidence) {
        this.max_confidence = max_confidence;
    }

    public String getMin_confidence() {
        return min_confidence;
    }

    public void setMin_confidence(String min_confidence) {
        this.min_confidence = min_confidence;
    }

    public String getMax_synthesize() {
        return max_synthesize;
    }

    public void setMax_synthesize(String max_synthesize) {
        this.max_synthesize = max_synthesize;
    }

    public String getMin_synthesize() {
        return min_synthesize;
    }

    public void setMin_synthesize(String min_synthesize) {
        this.min_synthesize = min_synthesize;
    }

    public TotalDataBean getTotal_data() {
        return total_data;
    }

    public void setTotal_data(TotalDataBean total_data) {
        this.total_data = total_data;
    }

    public static class totalIncomeBean extends BaseEntity {

        /**
         * enter_income : 5.00
         * out_income : 200.00
         */

        private String enter_income;
        private String out_income;

        public String getEnter_income() {
            return enter_income;
        }

        public void setEnter_income(String enter_income) {
            this.enter_income = enter_income;
        }

        public String getOut_income() {
            return out_income;
        }

        public void setOut_income(String out_income) {
            this.out_income = out_income;
        }
    }

    /**
     * 数据
     */
    private List<T> list = new ArrayList<T>();

    public PageBonusEntity() {
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<T> getList() {
        return list;
    }


    public void setList(List<T> list) {
        this.list = list;
    }

    public int getIs_next() {
        return is_next;
    }

    public void setIs_next(int is_next) {
        this.is_next = is_next;
    }


    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }


    public readBean getRead_data() {
        return read_data;
    }

    public void setRead_data(readBean read_data) {
        this.read_data = read_data;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public int getNow_num() {
        return now_num;
    }

    public void setNow_num(int now_num) {
        this.now_num = now_num;
    }

    public int getPage_num() {
        return page_num;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public int getIs_prev() {
        return is_prev;
    }

    public void setIs_prev(int is_prev) {
        this.is_prev = is_prev;
    }

    public static class TotalDataBean {
        /**
         * income_amount : 1112.00
         * support_amount : 868.02
         */

        private String income_amount;
        private String support_amount;

        public String getIncome_amount() {
            return income_amount;
        }

        public void setIncome_amount(String income_amount) {
            this.income_amount = income_amount;
        }

        public String getSupport_amount() {
            return support_amount;
        }

        public void setSupport_amount(String support_amount) {
            this.support_amount = support_amount;
        }
    }


    public class readBean extends BaseEntity {

        /**
         * all_no_readnum : 1
         */

        public int all_no_readnum;

        public int getAll_no_readnum() {
            return all_no_readnum;
        }

        public void setAll_no_readnum(int all_no_readnum) {
            this.all_no_readnum = all_no_readnum;
        }
    }

    @Override
    public String toString() {
        return "PageEntity{" +
                "total_pages=" + total_pages +
                ", is_next=" + is_next +
                ", purchase='" + purchase + '\'' +
                ", read_data=" + read_data +
                ", total_count='" + total_count + '\'' +
                ", now_num=" + now_num +
                ", page_num=" + page_num +
                ", is_prev=" + is_prev +
                ", all_balance='" + all_balance + '\'' +
                ", confidence='" + confidence + '\'' +
                ", synthesize='" + synthesize + '\'' +
                ", max_confidence='" + max_confidence + '\'' +
                ", min_confidence='" + min_confidence + '\'' +
                ", max_synthesize='" + max_synthesize + '\'' +
                ", min_synthesize='" + min_synthesize + '\'' +
                ", total_income=" + total_income +
                ", list=" + list +
                '}';
    }
}
