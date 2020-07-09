package com.liemi.basemall.data.event;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/4/24 14:35
 * 修改备注：
 */
public class WXPayResultEvent {

    public int errorCode = -1;

    public WXPayResultEvent(int errorCode) {
        this.errorCode = errorCode;
    }

}
