package com.netmi.workerbusiness.data.entity.mess;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/26
 * 修改备注：
 */
public class CustomerEntity extends BaseEntity {
    /**
     * id : 2
     * shop_id : 278
     * group_id : 1
     * token : d6e1ac0bafd14e35a7d20f7c9d6bda21
     * name : 平台客服
     * create_time : 2019-09-09 09:44:47
     * update_time : 2019-07-11 16:17:15
     * img : https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/ABFWXZMNTK023458_1562833029.png
     */

    private String id;
    private int shop_id;
    private int group_id;
    private String token;
    private String name;
    private String create_time;
    private String update_time;
    private String img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
