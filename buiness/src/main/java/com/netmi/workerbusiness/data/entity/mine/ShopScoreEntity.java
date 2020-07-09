package com.netmi.workerbusiness.data.entity.mine;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/18
 * 修改备注：
 */
public class ShopScoreEntity extends BaseEntity {


    /**
     * id : 1
     * user_id : 1758
     * shop_id : 278
     * reason : 表现的很好
     * type : 1
     * score : +100
     * create_time : 2019-09-10 10:16:10
     * update_time : 2019-09-10 10:16:10
     *
     * reason	string	原因
     * score	string	分数显示
     * create_time	string	创建时间
     */

    private String id;
    private int user_id;
    private int shop_id;
    private String reason;
    private int type;
    private String score;
    private String create_time;
    private String update_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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
