package com.netmi.workerbusiness.data.entity.home.lineorder;

import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.workerbusiness.R;

import java.io.Serializable;

/**
 * 订单发票信息
 */
public class MineOrderInvoiceEntity implements Serializable {
    private int id;
    private int order_id;
    private String invoice_content;//发票内容
    private String invoice_type;//发票类型
    private int type;//发票抬头
    private String company_name;//单位名称
    private String company_code;//纳税人识别码
    private String phone;//收票人手机号
    private String mail;//收票人邮箱

    public String getFormatType() {
        switch (type){
            case 1:
                return ResourceUtil.getString(R.string.sharemall_personal);
            case 2:
                return ResourceUtil.getString(R.string.sharemall_company);
            default:
                return ResourceUtil.getString(R.string.sharemall_unknown_type);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getInvoice_content() {
        return invoice_content;
    }

    public void setInvoice_content(String invoice_content) {
        this.invoice_content = invoice_content;
    }

    public String getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(String invoice_type) {
        this.invoice_type = invoice_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
