package com.liemi.basemall.data.entity.order;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/9/9 16:21
 * 修改备注：
 */
public class RefundReasonEntity {


    /**
     * name : 卖家发错货
     * code : 3
     */

    private String name;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
