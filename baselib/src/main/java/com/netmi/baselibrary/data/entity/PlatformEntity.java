package com.netmi.baselibrary.data.entity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/3/20 20:59
 * 修改备注：
 */
public class PlatformEntity {

    private String liemi_intel_tel;
    private String liemi_intel_email;
    private String liemi_intel_work_time;
    private String auto_evaluate;
    private String auto_confirm;

    public String getLiemi_intel_work_time() {
        return liemi_intel_work_time;
    }

    public void setLiemi_intel_work_time(String liemi_intel_work_time) {
        this.liemi_intel_work_time = liemi_intel_work_time;
    }

    public String getLiemi_intel_tel() {
        return liemi_intel_tel;
    }

    public void setLiemi_intel_tel(String liemi_intel_tel) {
        this.liemi_intel_tel = liemi_intel_tel;
    }

    public String getLiemi_intel_email() {
        return liemi_intel_email;
    }

    public void setLiemi_intel_email(String liemi_intel_email) {
        this.liemi_intel_email = liemi_intel_email;
    }

    public String getAuto_evaluate() {
        return auto_evaluate;
    }

    public void setAuto_evaluate(String auto_evaluate) {
        this.auto_evaluate = auto_evaluate;
    }

    public String getAuto_confirm() {
        return auto_confirm;
    }

    public void setAuto_confirm(String auto_confirm) {
        this.auto_confirm = auto_confirm;
    }
}
