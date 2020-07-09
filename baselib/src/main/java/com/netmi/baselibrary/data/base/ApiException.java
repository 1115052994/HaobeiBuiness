package com.netmi.baselibrary.data.base;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 14:01
 * 修改备注：
 */
public class ApiException {

    private int statuCode;

    private String message;

    public ApiException(int statuCode, String message) {
        this.statuCode = statuCode;
        this.message = message;
    }

    public int getStatuCode() {
        return statuCode;
    }

    public void setStatuCode(int statuCode) {
        this.statuCode = statuCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
