package com.netmi.workerbusiness.data.entity.walllet;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/4/16
 * 修改备注：
 */
public class EthDetailTwoEntity extends BaseEntity {


    /**
     * id : 8020
     * title : 培训奖
     * create_time : 2019-03-01 14:19:35
     * price : +48.00
     */

    private String id;
    private String title;
    private String create_time;
    private String price;
    //提现状态 1：待审核 2：审核通过 3：审核失败； 4：提现处理中 5：提现到帐 6：提现失败
    private String status;
    private String statuStr;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //提现状态 1：待审核 2：审核通过 3：审核失败； 4：提现处理中 5：提现到帐 6：提现失败
    public String getStatuStr() {
        if (status.equals("1")) {
            return "待审核";
        } else if (status.equals("2")) {
            return "审核通过";
        } else if (status.equals("3")) {
            return "审核失败";
        } else if (status.equals("4")) {
            return "提现处理中";
        } else if (status.equals("5")) {
            return "提现到帐";
        } else if (status.equals("6")) {
            return "提现失败";
        } else {
            return "";
        }
    }

    public void setStatuStr(String statuStr) {
        this.statuStr = statuStr;
    }
}
