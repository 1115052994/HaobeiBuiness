package com.netmi.baselibrary.data.entity;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.ui.MApplication;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 14:11
 * 修改备注：
 */
public class BaseData<T> {

    /**
     * 数据泛型
     */
    private T data;

    /**
     * 0为正常，其他为异常
     */
    private int errcode;

    /**
     * errcode非0时，会有错误详细信息
     */
    private String errmsg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrcode() {
//        if(errcode == Constant.TOKEN_FAIL) {
//            errmsg = "登录超时，请重新登录！";
//            MApplication.getInstance().gotoLogin();
//        }
        return errcode;
    }

    /**
     * 用于部分接口判断token错误时，需要重新登录
     */
    public int getErrcodeJugde() {
//        if(errcode == Constant.TOKEN_FAIL) {
//            errmsg = "登录超时，请重新登录！";
//            MApplication.getInstance().gotoLogin();
//        }
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "BaseData{" +
                "data=" + data +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
