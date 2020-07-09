package com.liemi.basemall.data.entity.floor;

import java.io.Serializable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/6 16:52
 * 修改备注：
 */
public class FloorItmEntity implements Serializable {

    /**
     * 元素编号
     */
    private String fid;

    /**
     * 房间编号
     */
    private String room_id;

    /**
     * 元素标题
     */
    private String title;

    /**
     * 图片地址
     */
    private String img_url;

    /**
     * 点击效果 0:点击无效果,1:文章，2:商品详情,3:链接,4:商城主页,5:投资主页
     */
    private Integer type;

    /**
     * 点击参数(配合type使用) type为1:文章编号,type为2:商品编号,type为3:跳转链接
     */
    private String param;

    /**
     * 房间排序值
     */
    private String sequence;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 更新时间
     */
    private String update_time;

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

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
