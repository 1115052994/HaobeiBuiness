package com.netmi.workerbusiness.data.entity.mine;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/18
 * 修改备注：
 */
public class BankListEntity extends BaseEntity {


    /**
     * id	int	银行卡ID
     * bank_name	string	银行名称
     * bank_card	string	银行卡号
     */

    private int id;
    private String bank_name;
    private String bank_card;
    private String bank_logo;


    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_logo() {
        return bank_logo;
    }

    public void setBank_logo(String bank_logo) {
        this.bank_logo = bank_logo;
    }
}
