package com.netmi.workerbusiness.data.entity.mine;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/18
 * 修改备注：
 */
public class WalletEntity extends BaseEntity {
    /**
     * balance : 0.00
     */
    private String balance;


    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
