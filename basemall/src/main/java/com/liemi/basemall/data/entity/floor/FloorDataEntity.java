package com.liemi.basemall.data.entity.floor;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/6 16:52
 * 修改备注：
 */
public class FloorDataEntity {


    /**
     * 楼层类型
     */
    private int type;


    /**
     * 房间编号
     */
    private String room_id;

    /**
     * 房间名称
     */
    private String title;

    /**
     * 房间图片
     */
    private String img_url;

    /**
     * 房间排序值
     */
    private String sequence;

    /**
     * 房间二级信息
     */
    private List<FloorItmEntity> floorItems;

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public List<FloorItmEntity> getFloorItems() {
        return floorItems;
    }

    public void setFloorItems(List<FloorItmEntity> floorItems) {
        this.floorItems = floorItems;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
