package com.netmi.workerbusiness.data.event;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/29
 * 修改备注：
 */
public class ChartChangeEvent {
    private String type; // 1 2 3 分别为 日 周 月
    private int popNum; // 1 2  分别为（表） 成交额  订单数

    public ChartChangeEvent(String type,int popNum) {
        this.type = type;
        this.popNum = popNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPopNum() {
        return popNum;
    }

    public void setPopNum(int popNum) {
        this.popNum = popNum;
    }
}
