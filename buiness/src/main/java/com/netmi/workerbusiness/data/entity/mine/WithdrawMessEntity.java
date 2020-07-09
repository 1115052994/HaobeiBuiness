package com.netmi.workerbusiness.data.entity.mine;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/18
 * 修改备注：
 */
public class WithdrawMessEntity extends BaseEntity {
    /**
     * shop_setting : 1.啊啊啊啊
     2.收拾收拾
     3.顶顶顶顶
     * withdraw_money : 25.03
     */

    private String shop_setting;
    private String withdraw_money;


    public String getShop_setting() {
        return shop_setting;
    }

    public void setShop_setting(String shop_setting) {
        this.shop_setting = shop_setting;
    }

    public String getWithdraw_money() {
        return withdraw_money;
    }

    public void setWithdraw_money(String withdraw_money) {
        this.withdraw_money = withdraw_money;
    }
}
