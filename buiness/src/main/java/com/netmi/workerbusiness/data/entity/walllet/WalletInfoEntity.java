package com.netmi.workerbusiness.data.entity.walllet;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class WalletInfoEntity extends BaseEntity {
    /*
    * "hand_balance": "0.00",
        "address": "0x7583F7626B383a480e70a602a611639c574DDa00",
        "address_qrcode": "http://haibeimaster.oss-cn-hangzhou.aliyuncs.com/image/walletpublic2020031304014697535497.png",
        "share_code": "9898ae",
        "share_code_qrcode": "http://haibeimaster.oss-cn-hangzhou.aliyuncs.com/image/wallet2020031304291155101511.png",
        "recharge_remark": "充值备注",
        "extract_remark": "提取备注",
        "transfer_remark": "转账备注",
        "extract_lowest": "1",
        "extract_highest": "10",
        "extract_fee_rate": "0.1"
    *
    * */
    private String hand_balance;
    private String address;
    private String address_qrcode;
    private String share_code;
    private String share_code_qrcode;
    private String recharge_remark_user;
    private String extract_remark_user;
    private String transfer_remark_user;
    private String receive_remark_user;
    private String extract_lowest;
    private String extract_highest;
    private String extract_fee_rate;
    private String transfer_fee_rate; //转账手续费比例
    private String money;
    private String freeze_price;
    private String freeze_money;//待结算金额（人民币）
    private String hand_yugu;//预估收益

    public String getFreeze_money() {
        return freeze_money;
    }

    public void setFreeze_money(String freeze_money) {
        this.freeze_money = freeze_money;
    }

    public String getReceive_remark_user() {
        return receive_remark_user;
    }

    public void setReceive_remark_user(String receive_remark_user) {
        this.receive_remark_user = receive_remark_user;
    }

    public String getFreeze_price() {
        return freeze_price;
    }

    public void setFreeze_price(String freeze_price) {
        this.freeze_price = freeze_price;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getHand_balance() {
        return hand_balance;
    }

    public void setHand_balance(String hand_balance) {
        this.hand_balance = hand_balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_qrcode() {
        return address_qrcode;
    }

    public void setAddress_qrcode(String address_qrcode) {
        this.address_qrcode = address_qrcode;
    }

    public String getShare_code() {
        return share_code;
    }

    public void setShare_code(String share_code) {
        this.share_code = share_code;
    }

    public String getShare_code_qrcode() {
        return share_code_qrcode;
    }

    public void setShare_code_qrcode(String share_code_qrcode) {
        this.share_code_qrcode = share_code_qrcode;
    }

    public String getRecharge_remark_user() {
        return recharge_remark_user;
    }

    public void setRecharge_remark_user(String recharge_remark_user) {
        this.recharge_remark_user = recharge_remark_user;
    }

    public String getExtract_remark_user() {
        return extract_remark_user;
    }

    public void setExtract_remark_user(String extract_remark_user) {
        this.extract_remark_user = extract_remark_user;
    }

    public String getTransfer_remark_user() {
        return transfer_remark_user;
    }

    public void setTransfer_remark_user(String transfer_remark_user) {
        this.transfer_remark_user = transfer_remark_user;
    }

    public String getExtract_lowest() {
        return extract_lowest;
    }

    public void setExtract_lowest(String extract_lowest) {
        this.extract_lowest = extract_lowest;
    }

    public String getExtract_highest() {
        return extract_highest;
    }

    public void setExtract_highest(String extract_highest) {
        this.extract_highest = extract_highest;
    }

    public String getExtract_fee_rate() {
        return extract_fee_rate;
    }

    public void setExtract_fee_rate(String extract_fee_rate) {
        this.extract_fee_rate = extract_fee_rate;
    }

    public String getTransfer_fee_rate() {
        return transfer_fee_rate;
    }

    public void setTransfer_fee_rate(String transfer_fee_rate) {
        this.transfer_fee_rate = transfer_fee_rate;
    }

    public String getHand_yugu() {
        return hand_yugu;
    }

    public void setHand_yugu(String hand_yugu) {
        this.hand_yugu = hand_yugu;
    }
}
