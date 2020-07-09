package com.liemi.basemall.data.entity;

import com.liemi.basemall.data.entity.good.GoodsListEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/18 11:03
 * 修改备注：
 */
public class StoreEntity extends BaseEntity implements Serializable {


    /**
     * 我的收藏中是否被选中
     */
    private boolean checked;

    /**
     * id": "164",//主键
     "logo_url": "http://antbt.oss-cn-hangzhou.aliyuncs.com/backend_img/BFWZNTHK01235689_1502970143.jpg",//店铺logo
     "img_url": "http://antbt.oss-cn-hangzhou.aliyuncs.com/backend_img/BFWZNTHK01235689_1502970143.jpg",//店铺展示图片
     "name": "忘记唱情歌",//店铺名称
     "remark": "fdd",//店铺简介
     "full_name": null,//店铺详细地址
     "content": null,//店铺富文本
     "shop_tel": "3123123",//店铺电话
     "checkin_time": "2018-01-03",//入住时间
     "qrcode": null,//
     "sum_item": 1,//商品数
     "sum_shop": 0,//店铺的收藏数
     "itemList": []//推荐商品集合
     */
    private String id;
    private String logo_url;
    private String img_url;
    private String name;
    private String remark;
    private String full_name;
    private String content;
    private String shop_tel;
    private String checkin_time;
    private String qrcode;
    private String rccode;
    private String wxcode;
    private int sum_item;
    private int is_collection;
    private int sum_collection;
    private List<GoodsListEntity> itemList;



    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShop_tel() {
        return shop_tel;
    }

    public void setShop_tel(String shop_tel) {
        this.shop_tel = shop_tel;
    }

    public String getCheckin_time() {
        return checkin_time;
    }

    public void setCheckin_time(String checkin_time) {
        this.checkin_time = checkin_time;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getRccode() {
        return rccode;
    }

    public void setRccode(String rccode) {
        this.rccode = rccode;
    }

    public String getWxcode() {
        return wxcode;
    }

    public void setWxcode(String wxcode) {
        this.wxcode = wxcode;
    }

    public int getSum_item() {
        return sum_item;
    }

    public void setSum_item(int sum_item) {
        this.sum_item = sum_item;
    }

    public int getIs_collection() {
        return is_collection;
    }

    public void setIs_collection(int is_collection) {
        this.is_collection = is_collection;
    }

    public int getSum_collection() {
        return sum_collection;
    }

    public void setSum_collection(int sum_collection) {
        this.sum_collection = sum_collection;
    }

    public List<GoodsListEntity> getItemList() {
        return itemList;
    }

    public void setItemList(List<GoodsListEntity> itemList) {
        this.itemList = itemList;
    }
}
