package com.liemi.basemall.data.entity.floor;

import com.liemi.basemall.data.entity.StoreEntity;
import com.liemi.basemall.data.entity.good.GoodsListEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.Strings;

import java.util.List;

/**
 * Created by Bingo on 2018/11/19.
 */

public class NewFloorEntity extends BaseEntity {

    /**
     * active : false   是否选中当前楼层（后台配置时使用，前端显示接口不使用）
     * <p>
     * type :  5   ## 搜索栏：1，-- banner：2，-- 公告：3，-- 分类导航（一层5个）：4，-- 图层（一层可1-4张）：
     * 5。（公告和搜索栏，这里只控制是否显示，展示功能前端自行实现，banner图样式自行实现）
     * top : 0     楼层上间距
     * <p>
     * bottom : 0  楼层下间距
     * <p>
     * floor_data : [{   每层显示张数
     * "title": "设置标题",                    ## 分类导航需要标题时使用（占不使用）
     * "img_url": "",                            ## 显示图片
     * "type": "",  //点击效果类型 —— 商品：1，-- 分类对应列表：2，-- 店铺：3，-- 热门商品列表：4，-- 新品推荐列表：5，-- 推荐店铺列表：6，-- 富文本：7，-- 外链：8
     * "param": ""
     * }]
     */

    //搜索栏
    public static final int FLOOR_SEARCH_BAR = 1;
    //banner轮播图
    public static final int FLOOR_BANNER = 2;
    //公告
    public static final int FLOOR_NOTICE = 3;
    //分类导航
    public static final int FLOOR_NAVIGATION = 4;
    //图层
    public static final int FLOOR_IMAGE = 5;
    //热门商品
    public static final int FLOOR_GOODS_HOT = 6;
    //新品推荐
    public static final int FLOOR_GOODS_NEWS = 7;
    //推荐店铺
    public static final int FLOOR_STORE = 8;

    //点击效果：商品
    public static final int FLOOR_TYPE_GOOD = 1;
    //点击效果：分类对应列表
    public static final int FLOOR_TYPE_GOODS_CATEGORY = 2;
    //点击效果：店铺
    public static final int FLOOR_TYPE_STORE = 3;
    //点击效果：店铺
    public static final int FLOOR_TYPE_All_STORE = 9;

    //点击效果：热门商品列表
    public static final int FLOOR_TYPE_GOODS_HOT = 4;
    //点击效果：新品推荐列表
    public static final int FLOOR_TYPE_GOODS_NEWS = 5;
    //点击效果：推荐店铺列表
    public static final int FLOOR_TYPE_GOODS_RECOMMEND = 6;
    //点击效果：富文本
    public static final int FLOOR_TYPE_WEB_CONTENT = 7;
    //点击效果：外链
    public static final int FLOOR_TYPE_WEB_URL = 8;

    private String type;
    private int top;
    private int bottom;
    private int nums;
    private List<FloorDataBean> floor_data;
    private List<GoodsListEntity> goods_list;
    private List<StoreEntity> shops_list;

    public int getType() {
        return Strings.toInt(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public List<FloorDataBean> getFloor_data() {
        return floor_data;
    }

    public void setFloor_data(List<FloorDataBean> floor_data) {
        this.floor_data = floor_data;
    }

    public List<GoodsListEntity> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListEntity> goods_list) {
        this.goods_list = goods_list;
    }

    public List<StoreEntity> getShops_list() {
        return shops_list;
    }

    public void setShops_list(List<StoreEntity> shops_list) {
        this.shops_list = shops_list;
    }

    public static class FloorDataBean {
        /**
         * title : 设置标题
         * img_url :
         * type :
         * param :
         */

        private String title;
        private String img_url;
        //店铺：3，-- 热门商品列表：4，-- 新品推荐列表：5，-- 推荐店铺列表：6，-- 富文本：7，-- 外链：8，
        private String type;
        private String param;
        private String time;

        public FloorDataBean() {

        }

        public FloorDataBean(String type) {
            this.type = type;
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

        public int getType() {
            return Strings.toInt(type);
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
