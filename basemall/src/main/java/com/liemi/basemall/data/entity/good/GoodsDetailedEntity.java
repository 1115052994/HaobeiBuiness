package com.liemi.basemall.data.entity.good;

import android.databinding.Bindable;

import com.liemi.basemall.data.entity.StoreEntity;
import com.liemi.basemall.data.entity.comment.CommentEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/16 16:57
 * 修改备注：
 */
public class GoodsDetailedEntity extends BaseEntity implements Serializable {

    public static final String GOODS_ENTITY = "goodsEntity";

    /**
     * "item_id": "1025",//商品主键
     "shop_id": "151",//商铺主键
     "title": "红色沙发布料",//商品名称
     "remark": "23",//商品二级名称
     "img_url": "http://www.zhida.cc/images/201706/goods_img/4038_G_1497305343591.jpg",//展示图片
     "sequence": "0",//默认排序值
     "stock": "100",//剩余总库存
     "deal_num": "5",//总卖出量
     "rich_text": "<p><img title=\"ABFXZMNTHK123489_1507863202.jpg\" alt=\"ABFXZMNTHK123489_1507863202.jpg\" src=\"http://antbt.oss-cn-hangzhou.aliyuncs.com/backend_img%2FABFXZMNTHK123489_1507863202.jpg\"/><img title=\"ABFWZNTH01345679_1507863091.jpg\" alt=\"ABFWZNTH01345679_1507863091.jpg\" src=\"http://antbt.oss-cn-hangzhou.aliyuncs.com/backend_img%2FABFWZNTH01345679_1507863091.jpg\"/></p>",//富文本介绍
     "status": "5",//状态：1上传中 2上架待审核 3待定价 4待上架 5已上架 6 下架待审核 7已下架'
     "is_new": "1",//是否新品 0 否 1 是'
     "is_hot": "1",//是否热卖 0：否 1：是'
     "is_sales": "1",//是否促销 0：否 1：是'
     "param": null,
     "shop_tel": "15067124172",
     "is_collection": "0",//是否收藏 0：否 1：是
     "old_price": "10.00",//备用字段
     "price": "199.00",//单价
     "item_code": null,//商品编号
     "earn_score": "0",//该商品第一个规格可获取的积分数量
     "postage": "0.00",//邮费
     "item_type": "0",//商品类型\r\n0:普通商品\r\n1:纯积分商品\r\n2:现金+积分商品
     * itemImgs : ["http://www.zhida.cc/images/201706/goods_img/4038_P_1497467001445.jpg","http://www.zhida.cc/images/201706/goods_img/4038_P_1497305343602.jpg","http://www.zhida.cc/images/201706/goods_img/4038_P_1497467001924.jpg","http://antbt.oss-cn-hangzhou.aliyuncs.com/backend_img%2FABFXZMNTHK123489_1507863202.jpg"]
     * meNatures : [{ //商品下的服务说明
     * "name":"材质：",  //服务描述
     * "remark":"涤纶、胶贴", //废弃
     * "item_id":"1025"}, //废弃
     * {"name":"成分： ","remark":"15%涤纶","item_id":"1025"},{"name":"柔软：","remark":"100%","item_id":"1025"}]
     * meLabels : [{"name":"哈哈标签","item_id":"1272"},{"name":"热门商品122","item_id":"1272"}]
     * meCommet : {"item_id":"1025",
     * "uid":"4",
     * "content":"test",
     * "flag":"1",
     * "create_time":null,
     * "to_commet_id":"0", //回复评论主键
     * "commet_id":"110", //评论主键
     * "sum_commet":"3", //总评论数
     //评论下的对应图片列表
     * "meCommetImgs":[{"id":"195","commet_id":"110","img_url":"http://hangwt-file.palcomm.com.cn/head_default.png"}],"meItmeValue":{"ivid":"5311","value_names":"l"},"nickname":null,"head_url":"http://hangwt-file.palcomm.com.cn/head_default.png"}
     //商铺信息
     * shop : {"id":"151","logo_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15201560561117.jpg","img_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15201560616651.jpg","name":"衣服店",
     * "discount":"0.1000", //店铺分销比例
     * "remark":"水果店","full_name":"北京市-北京市- 浙江省杭州市上城区六号大街","content":null,"shop_tel":"15067124172","qrcode":null,"sum_item":"5","rccode":"","wxcode":""}
     * color_list : [] //商品下的对应颜色列表
     */
    /**
     * 我的收藏中是否被选中
     */
    private boolean checked;
    private String item_id;
    private String shop_id;
    private String title;
    private String remark;
    private String img_url;
    private String sequence;
    private String stock;
    private String deal_num;
    private String rich_text;
    private int status;
    private String is_new;
    private String is_hot;
    private String is_sales;
    private String param;
    private String shop_tel;
    private int is_collection;
    private String old_price;
    private String price;
    private String item_code;
    private String earn_score;
    private String postage;
    private int item_type;
    private CommentEntity meCommet;
    private StoreEntity shop;
    private List<String> itemImgs;
    private List<MeNaturesBean> meNatures;
    private List<MeLabelsBean> meLabels;
    private List<ColorListBean> color_list;
    private String total_level;     //商品好评度


    /**
     * 购物车的字段
     */
    private String cart_id;
    private String ivid;
    private String num;
    private String value_names;//商品规格名称

    private String eth_cny;
    private String eth_price;

    public String getEth_cny() {
        return eth_cny;
    }

    public void setEth_cny(String eth_cny) {
        this.eth_cny = eth_cny;
    }

    public String getEth_price() {
        return eth_price;
    }

    public void setEth_price(String eth_price) {
        this.eth_price = eth_price;
    }

    public String getTotal_level() {
        return total_level;
    }

    public void setTotal_level(String total_level) {
        this.total_level = total_level;
    }

    public String getValue_names() {
        return value_names;
    }

    public void setValue_names(String value_names) {
        this.value_names = value_names;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getIvid() {
        return ivid;
    }

    public void setIvid(String ivid) {
        this.ivid = ivid;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getShowPrice() {
        switch (item_type) {
            default:
                return FloatUtils.formatMoney(price);
        }
    }

    public String getShowPriceGoodDetailPrice(){
        switch (item_type) {
            default:
                return FloatUtils.formatMoneyGoodDetailPrice(price);
        }
    }

    public String getRealPrice() {
        return price;
    }


    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTitle() {
        return title;
    }

    public StoreEntity getShop() {
        return shop;
    }

    public void setShop(StoreEntity shop) {
        this.shop = shop;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDeal_num() {
        return deal_num;
    }

    public void setDeal_num(String deal_num) {
        this.deal_num = deal_num;
    }

    public String getRich_text() {
        return rich_text;
    }

    public void setRich_text(String rich_text) {
        this.rich_text = rich_text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getIs_sales() {
        return is_sales;
    }

    public void setIs_sales(String is_sales) {
        this.is_sales = is_sales;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getShop_tel() {
        return shop_tel;
    }

    public void setShop_tel(String shop_tel) {
        this.shop_tel = shop_tel;
    }

    public int getIs_collection() {
        return is_collection;
    }

    public void setIs_collection(int is_collection) {
        this.is_collection = is_collection;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getEarn_score() {
        return earn_score;
    }

    public void setEarn_score(String earn_score) {
        this.earn_score = earn_score;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public CommentEntity getMeCommet() {
        return meCommet;
    }

    public void setMeCommet(CommentEntity meCommet) {
        this.meCommet = meCommet;
    }

    public List<String> getItemImgs() {
        return itemImgs;
    }

    public void setItemImgs(List<String> itemImgs) {
        this.itemImgs = itemImgs;
    }

    public List<MeNaturesBean> getMeNatures() {
        return meNatures;
    }

    public void setMeNatures(List<MeNaturesBean> meNatures) {
        this.meNatures = meNatures;
    }

    public List<MeLabelsBean> getMeLabels() {
        return meLabels;
    }

    public void setMeLabels(List<MeLabelsBean> meLabels) {
        this.meLabels = meLabels;
    }

    public List<ColorListBean> getColor_list() {
        return color_list;
    }

    public void setColor_list(List<ColorListBean> color_list) {
        this.color_list = color_list;
    }

    public static class MeNaturesBean implements Serializable {
        /**
         * name : 材质：
         * remark : 涤纶、胶贴
         * item_id : 1025
         */

        private String name;
        private String remark;
        private String item_id;

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

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }
    }

    public static class MeLabelsBean implements Serializable {
        /**
         * name : 哈哈标签
         * item_id : 1272
         */

        private String name;
        private String item_id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }
    }

    public static class ColorListBean implements Serializable {

        private String name;
        private String pid;
        private String mcid;
        private String img_url;
        private String item_id;
        private int is_select;
        private boolean checked;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getMcid() {
            return mcid;
        }

        public void setMcid(String mcid) {
            this.mcid = mcid;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public int getIs_select() {
            return is_select;
        }

        public void setIs_select(int is_select) {
            this.is_select = is_select;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
