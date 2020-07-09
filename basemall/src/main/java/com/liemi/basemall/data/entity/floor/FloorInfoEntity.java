package com.liemi.basemall.data.entity.floor;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/6 16:52
 * 修改备注：
 */
public class FloorInfoEntity {


    /**
     * 楼层号
     */
    private String floor;

    /**
     * 楼层类型
     */
    private int type;

    /**
     * 房间信息
     */
    private List<FloorDataEntity> floor_data;

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<FloorDataEntity> getFloor_data() {
        return floor_data;
    }

    public void setFloor_data(List<FloorDataEntity> floor_data) {
        this.floor_data = floor_data;
    }

}
