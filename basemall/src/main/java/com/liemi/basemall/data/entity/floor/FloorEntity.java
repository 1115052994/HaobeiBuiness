package com.liemi.basemall.data.entity.floor;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/6 16:51
 * 修改备注：
 */
public class FloorEntity extends BaseEntity {

    /**
     * 楼层列表编号
     */
    private String list_id;

    /**
     * 所属模块编号
     */
    private String block_id;

    /**
     * 分类标题
     */
    private String title;

    /**
     * 楼层信息
     */
    private List<FloorInfoEntity> floor_info;

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FloorInfoEntity> getFloor_info() {
        return floor_info;
    }

    public void setFloor_info(List<FloorInfoEntity> floor_info) {
        this.floor_info = floor_info;
    }
}
