package com.netmi.workerbusiness.data.entity.mine;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/18
 * 修改备注：
 */
public class ShopPayRecordEntity extends BaseEntity {

    /**
     * service_time	int	服务时间 单位是年
     * service_money	float	服务费
     * deposit	float	押金
     * service_account	string	服务费账户
     * deposit_account	string	押金账户
     * service_prove	string	服务证明
     * deposit_prove	string	押金证明
     * name	string	店铺名称
     * real_name	string	法人姓名
     * idcard	string	法人身份证
     */
    private int service_time;
    private String service_money;
    private String deposit;
    private String service_account;
    private String deposit_account;
    private String service_prove;
    private String deposit_prove;
    private String name;
    private String real_name;
    private String idcard;

    public int getService_time() {
        return service_time;
    }

    public void setService_time(int service_time) {
        this.service_time = service_time;
    }

    public String getService_money() {
        return service_money;
    }

    public void setService_money(String service_money) {
        this.service_money = service_money;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getService_account() {
        return service_account;
    }

    public void setService_account(String service_account) {
        this.service_account = service_account;
    }

    public String getDeposit_account() {
        return deposit_account;
    }

    public void setDeposit_account(String deposit_account) {
        this.deposit_account = deposit_account;
    }

    public String getService_prove() {
        return service_prove;
    }

    public void setService_prove(String service_prove) {
        this.service_prove = service_prove;
    }

    public String getDeposit_prove() {
        return deposit_prove;
    }

    public void setDeposit_prove(String deposit_prove) {
        this.deposit_prove = deposit_prove;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }


}
