package com.netmi.workerbusiness.data.entity.walllet;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/3/23
 * 修改备注：
 */
public class ETHDetailEntity extends BaseEntity {

    /**
     * id : 15
     * uid : 652
     * wallet_eth_id : 92
     * type : 2
     * address : 0x45b9DD7f4569FfF05e2C26e4D13eae01885d7947
     * remark : 提取到0x45b9DD7f4569FfF05e2C26e4D13eae01885d7947钱包
     * pay_order_id : null
     * order_time : null
     * pay_order_no :
     * order_amount :
     * coin : 0.001
     * symbol : 2
     * fee : 0.00001
     * hash_value : 0x732d5d756964691d71df2d3ad3588f74160b74622a313bac18af6cc037a19681
     * status : 0  任务状态1：投递中；2：异常终止；3：交易回单；4：25遍确认中；5：成功
     * create_time : 2018-11-27 19:36:19
     */

    private String id;
    private String uid;
    private String wallet_eth_id;
    private String type;
    private String address;
    private String remark;
    private Object pay_order_id;
    private Object order_time;
    private String pay_order_no;
    private String order_amount;
    private String coin;
    private String symbol;
    private String fee;
    private String hash_value;
    private String status;
    private String create_time;
    private String title;
    private String statuStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWallet_eth_id() {
        return wallet_eth_id;
    }

    public void setWallet_eth_id(String wallet_eth_id) {
        this.wallet_eth_id = wallet_eth_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object getPay_order_id() {
        return pay_order_id;
    }

    public void setPay_order_id(Object pay_order_id) {
        this.pay_order_id = pay_order_id;
    }

    public Object getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Object order_time) {
        this.order_time = order_time;
    }

    public String getPay_order_no() {
        return pay_order_no;
    }

    public void setPay_order_no(String pay_order_no) {
        this.pay_order_no = pay_order_no;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getHash_value() {
        return hash_value;
    }

    public void setHash_value(String hash_value) {
        this.hash_value = hash_value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTitle() {
        //	明细类型1：充值明细2 提取明细3购买明细4退款明细
        if (type.equals("1")) {
            return "充值";
        } else if (type.equals("2")) {
            return "提取";
        } else if (type.equals("3")) {
            return "购买";
        } else if (type.equals("4")) {
            return "退款";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatuStr() {
        //任务状态1：投递中；2：异常终止；3：交易回单；4：25遍确认中；5：成功
        if (status.equals("1")) {
            return "投递中";
        } else if (status.equals("2")) {
            return "异常终止";
        } else if (status.equals("3")) {
            return "交易回单";
        } else if (status.equals("4")) {
            return "确认中";
        } else if (status.equals("5")) {
            return "成功";
        }
        return statuStr;
    }

    public void setStatuStr(String statuStr) {
        this.statuStr = statuStr;
    }
}
