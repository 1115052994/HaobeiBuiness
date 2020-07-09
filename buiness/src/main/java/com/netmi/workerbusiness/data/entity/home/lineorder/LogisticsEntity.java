package com.netmi.workerbusiness.data.entity.home.lineorder;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：发货时使用
 * 创建人：Leo
 * 创建时间：2020/5/13
 * 修改备注：
 */
public class LogisticsEntity extends BaseEntity {

    /**
     * logistics_company_code : ZJS
     * logistics_no : d65555
     * logistics_remark : dsadasdas
     */

    private String logistics_company_code;
    private String logistics_no;
    private String logistics_remark;

    public String getLogistics_company_code() {
        return logistics_company_code;
    }

    public void setLogistics_company_code(String logistics_company_code) {
        this.logistics_company_code = logistics_company_code;
    }

    public String getLogistics_no() {
        return logistics_no;
    }

    public void setLogistics_no(String logistics_no) {
        this.logistics_no = logistics_no;
    }

    public String getLogistics_remark() {
        return logistics_remark;
    }

    public void setLogistics_remark(String logistics_remark) {
        this.logistics_remark = logistics_remark;
    }
}
