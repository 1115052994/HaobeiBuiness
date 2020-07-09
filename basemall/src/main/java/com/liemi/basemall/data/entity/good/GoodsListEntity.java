package com.liemi.basemall.data.entity.good;

import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.Strings;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/15 18:20
 * 修改备注：
 */
public class GoodsListEntity extends BaseEntity implements Serializable {

    /**
     * "item_id": "1206",//商品主键
     "shop_id": "170",//商铺主键
     "title": "植绒A005-296（花布）",//商品名称
     "remark": "植绒（花布），宽幅145CM±3CM",//商品二级名称
     "img_url": "http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_1522647229947.jpg",//展示图片
     "sequence": "0",//默认排序值
     "stock": "1000",//剩余总库存
     "deal_num": "0",//总卖出量
     "rich_text": "......",//富文本介绍
     "status": "5",//状态：1上传中 2上架待审核 3待定价 4待上架 5已上架 6 下架待审核 7已下架'
     "is_new": "1",//是否新品 0 否 1 是'
     "is_hot": "0",//是否热卖 0：否 1：是'
     "is_sales": "1",//是否促销 0：否 1：是'
     "param": "/item/me-item/info?id=1206",//app使用的富文本链接（相对路劲）
     "shop_tel": "400-880-8012",
     "is_collection": "0",//是否收藏 0：否 1：是
     "old_price": "0.00",//商品原价 （备用字段）
     "price": "28.00",//现价（米单价）
     "item_code": "",
     "item_type": "0",//商品类型 默认0:普通商品 1:纯积分商品 2:现金+积分商品
     "postage": "0.00",//邮费 为0时包邮

     *  //商品下的对应图片列表 itemImgs : ["http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_1522647229947.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15226472293140.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15226472305473.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15226472318525.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15226472321840.jpg"]
     * meNatures : []
     * meLabels : [{"name":"哈哈标签","item_id":"1272"},{"name":"热门商品122","item_id":"1272"}]
     * meCommet : {"item_id":null,"uid":null,"content":null,"flag":null,"create_time":null,"to_commet_id":null,"commet_id":null,"sum_commet":null,"meCommetImgs":[],"u":null}
     * shop : {"id":"170","logo_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15209237777317.jpg","img_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15209239077242.jpg","name":"嗨布商城","remark":"嗨布商城","discount":"0.1000", //店铺分销比例 "full_name":"浙江省-嘉兴市-海宁市 许村镇市场路118号1幢1708室","content":null,"shop_tel":"400-880-8012","qrcode":null,"sum_item":"93","rccode":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15215402113211.png","wxcode":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15215402227337.png"}
     */

    private String item_id;
    private String shop_id;
    private String title;
    private String remark;
    private String img_url;
    private String sequence;
    private String stock;
    private String deal_num;
    private String rich_text;
    private String status;
    private String is_new;
    private String is_hot;
    private String is_sales;
    private String param;
    private String shop_tel;
    private String is_collection;
    private String old_price;
    private String price;
    private String item_code;
    private int item_type;
    private String postage;
    private MeCommetBean meCommet;
    private ShopBean shop;
    private List<String> itemImgs;
    private List<?> meNatures;
    private List<MeLabelsBean> meLabels;

    public String getShowPrice() {
        switch (item_type) {
            default:
                return FloatUtils.formatDouble(price);

        }
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getIs_collection() {
        return is_collection;
    }

    public void setIs_collection(String is_collection) {
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

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public MeCommetBean getMeCommet() {
        return meCommet;
    }

    public void setMeCommet(MeCommetBean meCommet) {
        this.meCommet = meCommet;
    }

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public List<String> getItemImgs() {
        return itemImgs;
    }

    public void setItemImgs(List<String> itemImgs) {
        this.itemImgs = itemImgs;
    }

    public List<?> getMeNatures() {
        return meNatures;
    }

    public void setMeNatures(List<?> meNatures) {
        this.meNatures = meNatures;
    }

    public List<MeLabelsBean> getMeLabels() {
        return meLabels;
    }

    public void setMeLabels(List<MeLabelsBean> meLabels) {
        this.meLabels = meLabels;
    }

    public static class MeCommetBean {
        /**
         * item_id : null
         * uid : null
         * content : null
         * flag : null
         * create_time : null
         * to_commet_id : null
         * commet_id : null
         * sum_commet : null
         * meCommetImgs : []
         * u : null
         */

        private Object item_id;
        private Object uid;
        private Object content;
        private Object flag;
        private Object create_time;
        private Object to_commet_id;
        private Object commet_id;
        private Object sum_commet;
        private Object u;
        private List<?> meCommetImgs;

        public Object getItem_id() {
            return item_id;
        }

        public void setItem_id(Object item_id) {
            this.item_id = item_id;
        }

        public Object getUid() {
            return uid;
        }

        public void setUid(Object uid) {
            this.uid = uid;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public Object getFlag() {
            return flag;
        }

        public void setFlag(Object flag) {
            this.flag = flag;
        }

        public Object getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Object create_time) {
            this.create_time = create_time;
        }

        public Object getTo_commet_id() {
            return to_commet_id;
        }

        public void setTo_commet_id(Object to_commet_id) {
            this.to_commet_id = to_commet_id;
        }

        public Object getCommet_id() {
            return commet_id;
        }

        public void setCommet_id(Object commet_id) {
            this.commet_id = commet_id;
        }

        public Object getSum_commet() {
            return sum_commet;
        }

        public void setSum_commet(Object sum_commet) {
            this.sum_commet = sum_commet;
        }

        public Object getU() {
            return u;
        }

        public void setU(Object u) {
            this.u = u;
        }

        public List<?> getMeCommetImgs() {
            return meCommetImgs;
        }

        public void setMeCommetImgs(List<?> meCommetImgs) {
            this.meCommetImgs = meCommetImgs;
        }
    }

    public static class ShopBean {
        /**
         * id : 170
         * logo_url : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15209237777317.jpg
         * img_url : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15209239077242.jpg
         * name : 嗨布商城
         * remark : 嗨布商城
         * discount : 0.1000
         * full_name : 浙江省-嘉兴市-海宁市 许村镇市场路118号1幢1708室
         * content : null
         * shop_tel : 400-880-8012
         * qrcode : null
         * sum_item : 93
         * rccode : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15215402113211.png
         * wxcode : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15215402227337.png
         */

        private String id;
        private String logo_url;
        private String img_url;
        private String name;
        private String remark;
        private String discount;
        private String full_name;
        private Object content;
        private String shop_tel;
        private Object qrcode;
        private String sum_item;
        private String rccode;
        private String wxcode;

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

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public String getShop_tel() {
            return shop_tel;
        }

        public void setShop_tel(String shop_tel) {
            this.shop_tel = shop_tel;
        }

        public Object getQrcode() {
            return qrcode;
        }

        public void setQrcode(Object qrcode) {
            this.qrcode = qrcode;
        }

        public String getSum_item() {
            return sum_item;
        }

        public void setSum_item(String sum_item) {
            this.sum_item = sum_item;
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
    }

    public static class MeLabelsBean {
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
}
