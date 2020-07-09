package com.liemi.basemall.data.event;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/23 18:45
 * 修改备注：
 */
public class ShopCartEvent {

    public ShopCartEvent() {

    }

    public ShopCartEvent(List<String> deleteList) {
        this.deleteList = deleteList;
    }

    private List<String> deleteList;

    public List<String> getDeleteList() {
        return deleteList;
    }

    public void setDeleteList(List<String> deleteList) {
        this.deleteList = deleteList;
    }
}
