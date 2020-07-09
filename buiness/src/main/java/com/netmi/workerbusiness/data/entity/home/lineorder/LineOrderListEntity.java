package com.netmi.workerbusiness.data.entity.home.lineorder;

import android.text.TextUtils;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.workerbusiness.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/23
 * 修改备注：
 */
public class LineOrderListEntity extends BaseEntity implements Serializable {
    public static final String ORDER_TYPE_NORMAL = "0";//主商城
    public static final String ORDER_TYPE_SCORE = "1";//积分商城
    public static final String ORDER_TYPE_PUSH = "4";//推手礼包
    public static final String ORDER_TYPE_MANAGER = "5";//经理礼包
    public static final String ORDER_TYPE_AGENCY = "6";//代理商礼包

    /**
     * id : 3
     * main_order_id : 3
     * order_id :
     * goods_num : 1
     * goods_amount : 58
     * order_amount : 51
     * order_pay_amount : 19
     * order_pay_score : 0
     * order_coupon_amount : 0
     * order_logistics_freight : 12
     * status_name : 未付款
     * status : 0
     * order_no :
     * pay_end_time : 2018-12-30 11:55:23
     * to_name : jimmy_nganam
     * to_tel : 17826893808
     * to_address : 北京市-北京市-西城区 qqqqqqqqqqqqqqqqqqqqqqqqqqq
     * create_time : 2019-01-03 13:12:33
     * is_remind : 0
     * logistics_no :
     * logistics_company_code :
     * pay_channel :
     * last_pay_channel : 0
     * now_time : 2019-01-02 21:26:07
     */
    private String id;
    private String main_order_id;
    private String order_id;
    private int goods_num;
    private double goods_amount;
    private double order_amount;
    private double order_pay_amount;
    private String order_pay_score;
    protected String orderPayScoreReal;//真实积分抵扣的价格
    private double order_coupon_amount;
    private String order_logistics_freight;
    private String status_name;
    protected int status;
    private String order_no;
    private String pay_end_time;
    protected String to_name;
    protected String to_tel;
    protected String to_address;
    private String create_time;
    private String is_remind;
    protected String logistics_no;
    protected String logistics_company_code;
    protected String pay_channel;
    private String last_pay_channel;
    private String now_time;

    private List<MainOrdersBean> MainOrders;
    //主订单下的所有商品，这里通过遍历二级订单的所有商品来填充
    protected List<MainOrdersBean.OrderSkusBean> goods;

    //左边按钮显示的文字
    protected String leftButtonStr;
    //右边按钮显示的文字
    protected String rightButtonStr;


    public String getLeftButtonStr() {
        switch (Integer.valueOf(status)) {
//            case Constant.ORDER_WAIT_PAY:
//                return ResourceUtil.getString(R.string.sharemall_order_cancel);
//            case Constant.ORDER_WAIT_SEND:
//                return "";
            case 9:
                return "订单评价";
//            case Constant.ORDER_WAIT_RECEIVE:
//                return ResourceUtil.getString(R.string.sharemall_order_logistics);
        }
        return "";
    }

    public String getRightButtonStr() {
        switch (Integer.valueOf(status)) { //0-未付款1-待发货2-待收货3-待评价4-退货申请
            // 5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
//            case Constant.ORDER_WAIT_PAY:
//                return ResourceUtil.getString(R.string.sharemall_order_go_pay);
            case Constant.ORDER_WAIT_SEND:
                return ResourceUtil.getString(R.string.sharemall_order_remind);
            case Constant.ORDER_WAIT_RECEIVE:
            case Constant.ORDER_WAIT_COMMENT:
            case 9:
                return ResourceUtil.getString(R.string.sharemall_order_confirm_accept);
//                return ResourceUtil.getString(R.string.sharemall_order_go_comment);
            case Constant.ORDER_CANCEL:
//            case Constant.ORDER_SUCCESS:
//                return ResourceUtil.getString(R.string.sharemall_order_delete);
            default:
                return "";
        }
    }

    public List<MainOrdersBean.OrderSkusBean> getGoods() {
        if (MainOrders != null && MainOrders.size() > 0) {
            goods = new ArrayList<>();
            for (MainOrdersBean entity : MainOrders) {
                if (entity.getOrderSkus() != null && entity.getOrderSkus().size() > 0) {
                    goods.addAll(entity.getOrderSkus());
                }
            }
        }
        return goods;
    }


    public String getOrderPayScoreReal() {
        if (Strings.isEmpty(order_pay_score)) {
            return FloatUtils.formatMoney("0.00");
        }
        try {
            double d = Double.parseDouble(order_pay_score);
            return FloatUtils.formatMoney(d / 1000);
        } catch (NumberFormatException e) {
            return FloatUtils.formatMoney("0.00");
        }
    }

    public void setOrderPayScoreReal(String orderPayScoreReal) {
        this.orderPayScoreReal = orderPayScoreReal;
    }


    public List<MainOrdersBean> getMainOrders() {
        return MainOrders;
    }

    public void setMainOrders(List<MainOrdersBean> MainOrders) {
        this.MainOrders = MainOrders;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain_order_id() {
        return main_order_id;
    }

    public void setMain_order_id(String main_order_id) {
        this.main_order_id = main_order_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public double getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(double goods_amount) {
        this.goods_amount = goods_amount;
    }

    public double getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(double order_amount) {
        this.order_amount = order_amount;
    }

    public double getOrder_pay_amount() {
        return order_pay_amount;
    }

    public void setOrder_pay_amount(double order_pay_amount) {
        this.order_pay_amount = order_pay_amount;
    }

    public String getOrder_pay_score() {
        return order_pay_score;
    }

    public void setOrder_pay_score(String order_pay_score) {
        this.order_pay_score = order_pay_score;
    }

    public double getOrder_coupon_amount() {
        return order_coupon_amount;
    }

    public void setOrder_coupon_amount(double order_coupon_amount) {
        this.order_coupon_amount = order_coupon_amount;
    }

    public String getOrder_logistics_freight() {
        return order_logistics_freight;
    }

    public void setOrder_logistics_freight(String order_logistics_freight) {
        this.order_logistics_freight = order_logistics_freight;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getPay_end_time() {
        return pay_end_time;
    }

    public void setPay_end_time(String pay_end_time) {
        this.pay_end_time = pay_end_time;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getTo_tel() {
        return to_tel;
    }

    public void setTo_tel(String to_tel) {
        this.to_tel = to_tel;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIs_remind() {
        return is_remind;
    }

    public void setIs_remind(String is_remind) {
        this.is_remind = is_remind;
    }

    public String getLogistics_no() {
        return logistics_no;
    }

    public void setLogistics_no(String logistics_no) {
        this.logistics_no = logistics_no;
    }

    public String getLogistics_company_code() {
        return logistics_company_code;
    }

    public void setLogistics_company_code(String logistics_company_code) {
        this.logistics_company_code = logistics_company_code;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getLast_pay_channel() {
        return last_pay_channel;
    }

    public void setLast_pay_channel(String last_pay_channel) {
        this.last_pay_channel = last_pay_channel;
    }

    public String getNow_time() {
        return now_time;
    }

    public void setNow_time(String now_time) {
        this.now_time = now_time;
    }

    public static class MainOrdersBean extends BaseEntity implements Serializable {
        /**
         * id : 1558
         * order_id : 1558
         * order_no : 2018122810511495820774
         * shop_id : 193
         * address_id : null
         * product_count : 1
         * amount : 53.00
         * hand_balance_amount : 17.00
         * coupon_amount : 0.00
         * pay_score : 0.0000
         * logistics_freight : 12.00
         * logistics_no :
         * logistics_company_code :
         * earn_score : 0.0000
         * pay_time : null
         * deliver_time : null
         * remark : 留言
         * status : 0
         * type : 0
         * seller_message : null
         * is_remind : 0
         * display_price : 1
         * to_name : jimmy_nganam
         * to_tel : 17826893808
         * to_address : 北京市-北京市-西城区 qqqqqqqqqqqqqqqqqqqqqqqqqqq
         * create_time : 2018-12-28 10:51:14
         * update_time : 2018-12-28 10:51:14
         * pay_channel :
         * amount_eth : 0
         * orderSkus : [{"id":"1740","order_id":"1558","spu_name":"第九铺  人鱼泡沫手链项链耳钉女纯银简约冷淡风学生气质森系闺蜜个性饰品","sku_info":"{\"item_id\":\"1339\",\"ivid\":\"5868\",\"item_type\":\"0\",\"num\":1}","sku_score":"0","spu_earn_score":"0","sku_price":"58.00","old_price":null,"eth_price":"0","num":"1","spu_type":"0","sub_total":"58.00","status":"0","remark":"","create_time":"2018-12-28 10:51:14","update_time":"2018-12-28 10:51:14","del_flag":"0","uid":"913","pay_channel":"","img_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpeg/BFWXZNTK12356789_1543042508.jpg","value_names":"手链","item_type":"0","item_id":"1339"}]
         * shop : {"id":"193","logo_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpeg/ABFXZTHK23456789_1542873510.jpg","img_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/png/ABXZMK0123456789_1542873914.png","name":"第九铺","scid":null,"opening_hours":"早8.00 -- 下午6.00","remark":"","longitude":null,"latitude":null,"p_id":"330000","c_id":"330100","d_id":"330104","full_name":"浙江省-杭州市-江干区 无","address":"无","sort":"1","content":null,"discount":"0.0000","shop_tel":"18758246616","shop_remind_tel":"18758246616","create_time":"2018-11-22 16:01:31","update_time":"2018-12-31 15:17:06","is_recommend":"2","serviceid":"1","qrcode":null,"freeze":"0.00","billtotal":"0.00","get_price":"0.00","flowwater":"0.00","status":"1","sum_item":"11","del_flag":"0","rccode":"","wxcode":"","collection":"11","discount_type":"0"}
         * status_name : 未付款
         */

        private String id;
        private String order_id;
        private String order_no;
        private String shop_id;
        private Object address_id;
        private String product_count;
        private String amount;
        private String hand_balance_amount;
        private String coupon_amount;
        private String pay_score;
        private String logistics_freight;
        private String logistics_no;
        private String logistics_company_code;
        private String earn_score;
        private Object pay_time;
        private String deliver_time;
        private String remark;
        private String status;
        private String type;
        private Object seller_message;
        private String is_remind;
        private String display_price;
        private String to_name;
        private String to_tel;
        private String to_address;
        private String create_time;
        private String update_time;
        private String pay_channel;
        private String amount_eth;
        private ShopBean shop;
        private String status_name;
        private List<OrderSkusBean> orderSkus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public Object getAddress_id() {
            return address_id;
        }

        public void setAddress_id(Object address_id) {
            this.address_id = address_id;
        }

        public String getProduct_count() {
            return product_count;
        }

        public void setProduct_count(String product_count) {
            this.product_count = product_count;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getHand_balance_amount() {
            return hand_balance_amount;
        }

        public void setHand_balance_amount(String hand_balance_amount) {
            this.hand_balance_amount = hand_balance_amount;
        }

        public String getCoupon_amount() {
            return coupon_amount;
        }

        public void setCoupon_amount(String coupon_amount) {
            this.coupon_amount = coupon_amount;
        }

        public String getPay_score() {
            return pay_score;
        }

        public void setPay_score(String pay_score) {
            this.pay_score = pay_score;
        }

        public String getLogistics_freight() {
            return logistics_freight;
        }

        public void setLogistics_freight(String logistics_freight) {
            this.logistics_freight = logistics_freight;
        }

        public String getLogistics_no() {
            return logistics_no;
        }

        public void setLogistics_no(String logistics_no) {
            this.logistics_no = logistics_no;
        }

        public String getLogistics_company_code() {
            return logistics_company_code;
        }

        public void setLogistics_company_code(String logistics_company_code) {
            this.logistics_company_code = logistics_company_code;
        }

        public String getEarn_score() {
            return earn_score;
        }

        public void setEarn_score(String earn_score) {
            this.earn_score = earn_score;
        }

        public Object getPay_time() {
            return pay_time;
        }

        public void setPay_time(Object pay_time) {
            this.pay_time = pay_time;
        }

        public String getDeliver_time() {
            return deliver_time;
        }

        public void setDeliver_time(String deliver_time) {
            this.deliver_time = deliver_time;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getSeller_message() {
            return seller_message;
        }

        public void setSeller_message(Object seller_message) {
            this.seller_message = seller_message;
        }

        public String getIs_remind() {
            return is_remind;
        }

        public void setIs_remind(String is_remind) {
            this.is_remind = is_remind;
        }

        public String getDisplay_price() {
            return display_price;
        }

        public void setDisplay_price(String display_price) {
            this.display_price = display_price;
        }

        public String getTo_name() {
            return to_name;
        }

        public void setTo_name(String to_name) {
            this.to_name = to_name;
        }

        public String getTo_tel() {
            return to_tel;
        }

        public void setTo_tel(String to_tel) {
            this.to_tel = to_tel;
        }

        public String getTo_address() {
            return to_address;
        }

        public void setTo_address(String to_address) {
            this.to_address = to_address;
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

        public String getPay_channel() {
            return pay_channel;
        }

        public void setPay_channel(String pay_channel) {
            this.pay_channel = pay_channel;
        }

        public String getAmount_eth() {
            return amount_eth;
        }

        public void setAmount_eth(String amount_eth) {
            this.amount_eth = amount_eth;
        }

        public ShopBean getShop() {
            return shop;
        }

        public void setShop(ShopBean shop) {
            this.shop = shop;
        }

        public String getStatus_name() {
            return status_name;
        }

        public void setStatus_name(String status_name) {
            this.status_name = status_name;
        }

        public List<OrderSkusBean> getOrderSkus() {
            return orderSkus;
        }

        public void setOrderSkus(List<OrderSkusBean> orderSkus) {
            this.orderSkus = orderSkus;
        }

        public static class ShopBean extends BaseEntity implements Serializable {
            /**
             * id : 193
             * logo_url : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpeg/ABFXZTHK23456789_1542873510.jpg
             * img_url : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/png/ABXZMK0123456789_1542873914.png
             * name : 第九铺
             * scid : null
             * opening_hours : 早8.00 -- 下午6.00
             * remark :
             * longitude : null
             * latitude : null
             * p_id : 330000
             * c_id : 330100
             * d_id : 330104
             * full_name : 浙江省-杭州市-江干区 无
             * address : 无
             * sort : 1
             * content : null
             * discount : 0.0000
             * shop_tel : 18758246616
             * shop_remind_tel : 18758246616
             * create_time : 2018-11-22 16:01:31
             * update_time : 2018-12-31 15:17:06
             * is_recommend : 2
             * serviceid : 1
             * qrcode : null
             * freeze : 0.00
             * billtotal : 0.00
             * get_price : 0.00
             * flowwater : 0.00
             * status : 1
             * sum_item : 11
             * del_flag : 0
             * rccode :
             * wxcode :
             * collection : 11
             * discount_type : 0
             */

            private String id;
            private String logo_url;
            private String img_url;
            private String name;
            private Object scid;
            private String opening_hours;
            private String remark;
            private Object longitude;
            private Object latitude;
            private String p_id;
            private String c_id;
            private String d_id;
            private String full_name;
            private String address;
            private String sort;
            private Object content;
            private String discount;
            private String shop_tel;
            private String shop_remind_tel;
            private String create_time;
            private String update_time;
            private String is_recommend;
            private String serviceid;
            private Object qrcode;
            private String freeze;
            private String billtotal;
            private String get_price;
            private String flowwater;
            private String status;
            private String sum_item;
            private String del_flag;
            private String rccode;
            private String wxcode;
            private String collection;
            private String discount_type;

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

            public Object getScid() {
                return scid;
            }

            public void setScid(Object scid) {
                this.scid = scid;
            }

            public String getOpening_hours() {
                return opening_hours;
            }

            public void setOpening_hours(String opening_hours) {
                this.opening_hours = opening_hours;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public Object getLongitude() {
                return longitude;
            }

            public void setLongitude(Object longitude) {
                this.longitude = longitude;
            }

            public Object getLatitude() {
                return latitude;
            }

            public void setLatitude(Object latitude) {
                this.latitude = latitude;
            }

            public String getP_id() {
                return p_id;
            }

            public void setP_id(String p_id) {
                this.p_id = p_id;
            }

            public String getC_id() {
                return c_id;
            }

            public void setC_id(String c_id) {
                this.c_id = c_id;
            }

            public String getD_id() {
                return d_id;
            }

            public void setD_id(String d_id) {
                this.d_id = d_id;
            }

            public String getFull_name() {
                return full_name;
            }

            public void setFull_name(String full_name) {
                this.full_name = full_name;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public Object getContent() {
                return content;
            }

            public void setContent(Object content) {
                this.content = content;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getShop_tel() {
                return shop_tel;
            }

            public void setShop_tel(String shop_tel) {
                this.shop_tel = shop_tel;
            }

            public String getShop_remind_tel() {
                return shop_remind_tel;
            }

            public void setShop_remind_tel(String shop_remind_tel) {
                this.shop_remind_tel = shop_remind_tel;
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

            public String getIs_recommend() {
                return is_recommend;
            }

            public void setIs_recommend(String is_recommend) {
                this.is_recommend = is_recommend;
            }

            public String getServiceid() {
                return serviceid;
            }

            public void setServiceid(String serviceid) {
                this.serviceid = serviceid;
            }

            public Object getQrcode() {
                return qrcode;
            }

            public void setQrcode(Object qrcode) {
                this.qrcode = qrcode;
            }

            public String getFreeze() {
                return freeze;
            }

            public void setFreeze(String freeze) {
                this.freeze = freeze;
            }

            public String getBilltotal() {
                return billtotal;
            }

            public void setBilltotal(String billtotal) {
                this.billtotal = billtotal;
            }

            public String getGet_price() {
                return get_price;
            }

            public void setGet_price(String get_price) {
                this.get_price = get_price;
            }

            public String getFlowwater() {
                return flowwater;
            }

            public void setFlowwater(String flowwater) {
                this.flowwater = flowwater;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getSum_item() {
                return sum_item;
            }

            public void setSum_item(String sum_item) {
                this.sum_item = sum_item;
            }

            public String getDel_flag() {
                return del_flag;
            }

            public void setDel_flag(String del_flag) {
                this.del_flag = del_flag;
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

            public String getCollection() {
                return collection;
            }

            public void setCollection(String collection) {
                this.collection = collection;
            }

            public String getDiscount_type() {
                return discount_type;
            }

            public void setDiscount_type(String discount_type) {
                this.discount_type = discount_type;
            }
        }

        public static class OrderSkusBean extends BaseEntity implements Serializable {
            /**
             * id : 1740
             * order_id : 1558
             * spu_name : 第九铺  人鱼泡沫手链项链耳钉女纯银简约冷淡风学生气质森系闺蜜个性饰品
             * sku_info : {"item_id":"1339","ivid":"5868","item_type":"0","num":1}
             * sku_score : 0
             * spu_earn_score : 0
             * sku_price : 58.00
             * old_price : null
             * eth_price : 0
             * num : 1
             * spu_type : 0
             * sub_total : 58.00
             * status : 0
             * remark :
             * create_time : 2018-12-28 10:51:14
             * update_time : 2018-12-28 10:51:14
             * del_flag : 0
             * uid : 913
             * pay_channel :
             * img_url : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpeg/BFWXZNTK12356789_1543042508.jpg
             * value_names : 手链
             * item_type : 0
             * item_id : 1339
             */

            private String id;
            private String order_id;
            private String spu_name;
            private String sku_info;
            private String sku_score;
            private String spu_earn_score;
            private String sku_price;
            private Object old_price;
            private String eth_price;
            private String num;
            private String spu_type;
            private String sub_total;
            private int status;
            private String remark;
            private String create_time;
            private String update_time;
            private String del_flag;
            private String uid;
            private String pay_channel;
            private String img_url;
            private String value_names;
            private String item_type;
            private String item_id;
            private String statusFormat;

            //是否可申请退款
            private boolean canApplyRefund;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getSpu_name() {
                return spu_name;
            }

            public void setSpu_name(String spu_name) {
                this.spu_name = spu_name;
            }

            public String getSku_info() {
                return sku_info;
            }

            public void setSku_info(String sku_info) {
                this.sku_info = sku_info;
            }

            public String getSku_score() {
                return sku_score;
            }

            public void setSku_score(String sku_score) {
                this.sku_score = sku_score;
            }

            public String getSpu_earn_score() {
                return spu_earn_score;
            }

            public void setSpu_earn_score(String spu_earn_score) {
                this.spu_earn_score = spu_earn_score;
            }

            public String getSku_price() {
                return sku_price;
            }

            public void setSku_price(String sku_price) {
                this.sku_price = sku_price;
            }

            public Object getOld_price() {
                return old_price;
            }

            public void setOld_price(Object old_price) {
                this.old_price = old_price;
            }

            public String getEth_price() {
                return eth_price;
            }

            public void setEth_price(String eth_price) {
                this.eth_price = eth_price;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getSpu_type() {
                return spu_type;
            }

            public void setSpu_type(String spu_type) {
                this.spu_type = spu_type;
            }

            public String getSub_total() {
                return sub_total;
            }

            public void setSub_total(String sub_total) {
                this.sub_total = sub_total;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
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

            public String getDel_flag() {
                return del_flag;
            }

            public void setDel_flag(String del_flag) {
                this.del_flag = del_flag;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getPay_channel() {
                return pay_channel;
            }

            public void setPay_channel(String pay_channel) {
                this.pay_channel = pay_channel;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getValue_names() {
                return value_names;
            }

            public void setValue_names(String value_names) {
                this.value_names = value_names;
            }

            public String getItem_type() {
                return item_type;
            }

            public void setItem_type(String item_type) {
                this.item_type = item_type;
            }

            public String getItem_id() {
                return item_id;
            }

            public void setItem_id(String item_id) {
                this.item_id = item_id;
            }

            public String getStatusFormat() {
                switch (status) {
                    //,//0-未付款1-待发货2-待收货3-待评价4-退货申请5-退货申请驳回6-退货中7-已退货8-取消交易9-交易完成10-支付失败
                    case Constant.ORDER_WAIT_PAY:
                        return ResourceUtil.getString(R.string.sharemall_order_wait_pay);
                    case Constant.ORDER_WAIT_SEND:
                        return ResourceUtil.getString(R.string.sharemall_order_wait_send);
                    case Constant.ORDER_WAIT_RECEIVE:
                        return ResourceUtil.getString(R.string.sharemall_order_wait_receive);
                    case Constant.ORDER_WAIT_COMMENT:
                        return ResourceUtil.getString(R.string.sharemall_order_wait_appraise);
                    case Constant.ORDER_REFUND_ASK:
                        return ResourceUtil.getString(R.string.sharemall_order_refund_apply);
                    case Constant.ORDER_REFUND_NOT_ALLOW:
                        return ResourceUtil.getString(R.string.sharemall_order_refund_apply_fail);
                    case Constant.ORDER_REFUND_NOW:
                        return ResourceUtil.getString(R.string.sharemall_order_refund_ing);
                    case Constant.ORDER_REFUND_SUCCESS:
                        return ResourceUtil.getString(R.string.sharemall_order_refund_complete);
                    case Constant.ORDER_CANCEL:
                        return ResourceUtil.getString(R.string.sharemall_cancel_transaction);
                    case Constant.ORDER_SUCCESS:
                        return ResourceUtil.getString(R.string.sharemall_transaction_complete);
                    case Constant.ORDER_PAY_FAIL:
                        return ResourceUtil.getString(R.string.sharemall_pay_failure);
                }
                return statusFormat;
            }

            public void setStatusFormat(String statusFormat) {
                this.statusFormat = statusFormat;
            }


            public boolean isCanApplyRefund() {
                //待收货，待发货，4-退货申请5-退货申请驳回6-退货中7-已退货 均会显示这个按钮
                if (status == Constant.ORDER_WAIT_RECEIVE || status == Constant.ORDER_WAIT_SEND || status == Constant.ORDER_REFUND_ASK
                        || status == Constant.ORDER_REFUND_NOT_ALLOW || status == Constant.ORDER_REFUND_NOW || status == Constant.ORDER_REFUND_SUCCESS) {
                    if (!TextUtils.equals(item_type, ORDER_TYPE_PUSH) && !TextUtils.equals(item_type, ORDER_TYPE_MANAGER) && !TextUtils.equals(item_type, ORDER_TYPE_AGENCY)) {
                        return true;
                    }
                }
                return false;
            }

            public void setCanApplyRefund(boolean canApplyRefund) {
                this.canApplyRefund = canApplyRefund;
            }
        }
    }
}
