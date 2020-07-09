package com.netmi.workerbusiness.data.entity.walllet;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/3/17
 * 修改备注：
 */
public class WalletEntity extends BaseEntity {


    /**
     * hand_balance : 0.00
     * address : 0x7583F7626B383a480e70a602a611639c574DDa00
     * address_qrcode : http://haibeimaster.oss-cn-hangzhou.aliyuncs.com/image/walletpublic2020031304014697535497.png
     * share_code : 9898ae
     * share_code_qrcode : http://haibeimaster.oss-cn-hangzhou.aliyuncs.com/image/wallet2020031304291155101511.png
     * recharge_remark : 充值备注
     * extract_remark : 提取备注
     * transfer_remark : 转账备注
     * receive_remark_ : 接收备注
     * extract_lowest : 1
     * extract_highest : 10
     * extract_fee_rate : 0.1
     * transfer_fee_rate : 0.1
     * "hand_yugu": 0,
     */

    private String hand_balance;
    private String address;
    private String address_qrcode;
    private String share_code;
    private String share_code_qrcode;
    private String recharge_remark;
    private String extract_remark;
    private String transfer_remark;
    private String receive_remark;
    private String extract_lowest;
    private String extract_highest;
    private String extract_fee_rate;
    private String transfer_fee_rate;
    private String money;
    private String hand_yugu;
    //待结算
    private String wait_settle;
    //       "freeze_price": "0.00",        //商家未结算金额
//               "shop_freeze_price": "0.00",        //海贝未结算节分
    private String freeze_price;
    private String shop_freeze_price;
    private String conversion;
    private String conversion_price;
    private String ratio;


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

    public String getRecharge_remark() {
        return recharge_remark;
    }

    public void setRecharge_remark(String recharge_remark) {
        this.recharge_remark = recharge_remark;
    }

    public String getExtract_remark() {
        return extract_remark;
    }

    public void setExtract_remark(String extract_remark) {
        this.extract_remark = extract_remark;
    }

    public String getTransfer_remark() {
        return transfer_remark;
    }

    public void setTransfer_remark(String transfer_remark) {
        this.transfer_remark = transfer_remark;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getWait_settle() {
        return wait_settle;
    }

    public void setWait_settle(String wait_settle) {
        this.wait_settle = wait_settle;
    }

    public String getFreeze_price() {
        return freeze_price;
    }

    public void setFreeze_price(String freeze_price) {
        this.freeze_price = freeze_price;
    }

    public String getShop_freeze_price() {
        return shop_freeze_price;
    }

    public void setShop_freeze_price(String shop_freeze_price) {
        this.shop_freeze_price = shop_freeze_price;
    }

    public String getConversion() {
        return conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    public String getConversion_price() {
        return conversion_price;
    }

    public void setConversion_price(String conversion_price) {
        this.conversion_price = conversion_price;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getReceive_remark() {
        return receive_remark;
    }

    public void setReceive_remark(String receive_remark) {
        this.receive_remark = receive_remark;
    }

    public String getHand_yugu() {
        return hand_yugu;
    }

    public void setHand_yugu(String hand_yugu) {
        this.hand_yugu = hand_yugu;
    }
}
