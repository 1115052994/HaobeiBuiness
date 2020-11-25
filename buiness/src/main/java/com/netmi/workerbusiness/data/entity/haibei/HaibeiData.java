package com.netmi.workerbusiness.data.entity.haibei;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class HaibeiData extends BaseEntity {


    /**
     * id : 558
     * bonus : 0.00
     * bouns_quota : 0.00
     * total_amount : 42.00
     * sales_volume : 8.00
     */

    private String id;
    private String bonus;
    private String bonus_quota;
    private String total_amount;
    private String sales_volume;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getBonus_quota() {
        return bonus_quota;
    }

    public void setBouns_quota(String bouns_quota) {
        this.bonus_quota = bouns_quota;
    }

    public String getTotal_amount() {
        if(total_amount.isEmpty()||total_amount.equals("")){
            total_amount = "0";
        }
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(String sales_volume) {
        this.sales_volume = sales_volume;
    }
}
