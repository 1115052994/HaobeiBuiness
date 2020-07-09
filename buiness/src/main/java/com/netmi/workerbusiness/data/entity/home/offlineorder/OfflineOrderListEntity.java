package com.netmi.workerbusiness.data.entity.home.offlineorder;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class OfflineOrderListEntity extends BaseEntity {
    /**
     * id : 10737
     * order_no : 366596952919494785
     * create_time : 2019-10-09 14:45:24
     * status : 0
     * amount : 11.00
     * type : 11
     * spu_name : 测试商品1号
     * img_url : https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/AFWXZMTHK0124789_1569206663.jpeg
     */

    private String id;
    private String order_no;
    private String create_time;
    private int status;
    private String amount;
    private String type;
    private String spu_name;
    private String img_url;

    private String statusStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpu_name() {
        return spu_name;
    }

    public void setSpu_name(String spu_name) {
        this.spu_name = spu_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getStatusStr() {
        //状态：0待付款；1待核销；9交易完成；7已经退款 3待评价 4：申请退款中 5： 拒绝退款
        if (status == 0) {
            return "待付款";
        } else if (status == 1) {
            return "待核销";
        } else if (status == 9) {
            return "交易完成";
        } else if (status == 7) {
            return "已经退款";
        } else if (status == 3) {
            return "待评价";
        } else if (status == 4) {
            return "申请退款中";
        } else if (status == 5) {
            return "拒绝退款";
        }
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
}
