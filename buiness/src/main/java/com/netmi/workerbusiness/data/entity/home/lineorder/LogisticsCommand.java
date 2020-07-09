package com.netmi.workerbusiness.data.entity.home.lineorder;

import com.netmi.workerbusiness.data.entity.home.postage.PostageTempleItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/5/13
 * 修改备注：
 */
public class LogisticsCommand implements Serializable {
    private String order_id;
    private List<LogisticsEntity> logistics;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public List<LogisticsEntity> getLogistics() {
        return logistics;
    }

    public void setLogistics(List<LogisticsEntity> logistics) {
        this.logistics = logistics;
    }
}
