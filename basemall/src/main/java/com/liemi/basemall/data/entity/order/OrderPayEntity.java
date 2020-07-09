package com.liemi.basemall.data.entity.order;

import com.netmi.baselibrary.utils.FloatUtils;

import java.io.Serializable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/10/17 14:10
 * 修改备注：
 */
public class OrderPayEntity implements Serializable {
    /**
     * pay_order_no	string	支付订单号
     end_time	timestamp	订单截止时间
     pay_amount	float	支付总金额
     pay_score	int	支付总积分
     create_time	timestamp	订单创建时间
     update_time	timestamp	订单更新时间
     status	int	支付订单状态 （0:待支付,1:支付成功,2:支付失败3:关闭）
     */
    private String pay_order_no;
    private String end_time;
    private String pay_amount;
    private double pay_score;
    private double earn_score;
    private String create_time;
    private String update_time;
    private int status;

    public String getShowPrice(){
        return FloatUtils.formatResult(String.valueOf(pay_score), pay_amount);
    }

    public String getPay_order_no() {
        return pay_order_no;
    }

    public void setPay_order_no(String pay_order_no) {
        this.pay_order_no = pay_order_no;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public double getPay_score() {
        return pay_score;
    }

    public void setPay_score(double pay_score) {
        this.pay_score = pay_score;
    }

    public double getEarn_score() {
        return earn_score;
    }

    public void setEarn_score(double earn_score) {
        this.earn_score = earn_score;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
