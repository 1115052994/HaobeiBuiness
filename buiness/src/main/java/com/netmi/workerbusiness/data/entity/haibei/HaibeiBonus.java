package com.netmi.workerbusiness.data.entity.haibei;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class HaibeiBonus extends BaseEntity {


    /**
     * bonus : 0.00
     */

    private String bonus;
    private String haibei_qrcode;

    public String getHaibei_qrcode() {
        return haibei_qrcode;
    }

    public void setHaibei_qrcode(String haibei_qrcode) {
        this.haibei_qrcode = haibei_qrcode;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }
}
