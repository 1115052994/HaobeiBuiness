package com.netmi.workerbusiness.data.entity.mine;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class BountyEntity extends BaseEntity {

    /**
     * title : 奖励金提现
     * bonus : -0.01
     * order_id : 0
     * bonus_type : 2
     * add_time : 2020-08-14 16:30:55
     */

    private String title;
    private String bonus;
    private String order_id;
    private String bonus_type;
    private String add_time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getBonus_type() {
        return bonus_type;
    }

    public void setBonus_type(String bonus_type) {
        this.bonus_type = bonus_type;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
