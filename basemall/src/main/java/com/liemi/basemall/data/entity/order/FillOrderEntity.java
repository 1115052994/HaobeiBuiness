package com.liemi.basemall.data.entity.order;

import com.netmi.baselibrary.utils.FloatUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/6 9:51
 * 修改备注：
 */
public class FillOrderEntity implements Serializable {

    /**
     * 地址主键
     */
    private String address_id;

    /**
     * 实付价格
     */
    private String amount;

    /**
     * 积分
     */
    private double pay_score;

    private List<Good> item_data;

    public static class Good implements Serializable{

        private String cart_id;

        private int num;

        private String ivid;

        private int item_type;

        private String remark;

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getIvid() {
            return ivid;
        }

        public void setIvid(String ivid) {
            this.ivid = ivid;
        }

        public int getItem_type() {
            return item_type;
        }

        public void setItem_type(int item_type) {
            this.item_type = item_type;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @Override
        public String toString() {
            return "{" +
                    "cart_id=" +
                    ", num=" + num +
                    ", ivid=" + ivid +
                    ", item_type=" + item_type +
                    ", remark=" + remark +
                    '}';
        }
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public double getPay_score() {
        return pay_score;
    }

    public void setPay_score(double pay_score) {
        this.pay_score = pay_score;
    }

    public List<Good> getItem_data() {
        return item_data;
    }

    public void setItem_data(List<Good> item_data) {
        this.item_data = item_data;
    }


}
