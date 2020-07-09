package com.liemi.basemall.data.entity.order;

import java.io.Serializable;
import java.util.List;

/**
 * 换货详情
 */
public class ReplaceInfoEntity implements Serializable {
    private String id;
    private String order_id;
    private String remark;
    private String phone;
    private String status;
    private String refuse_remark;
    private List<String> meChangeImg;
    private TelBean tel;
    private OrderSku orderSku;
    private OrderBean order;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefuse_remark() {
        return refuse_remark;
    }

    public void setRefuse_remark(String refuse_remark) {
        this.refuse_remark = refuse_remark;
    }

    public List<String> getMeChangeImg() {
        return meChangeImg;
    }

    public void setMeChangeImg(List<String> meChangeImg) {
        this.meChangeImg = meChangeImg;
    }

    public TelBean getTel() {
        return tel;
    }

    public void setTel(TelBean tel) {
        this.tel = tel;
    }

    public OrderSku getOrderSku() {
        return orderSku;
    }

    public void setOrderSku(OrderSku orderSku) {
        this.orderSku = orderSku;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public static class TelBean implements Serializable{
        private String shop_tel;

        public String getShop_tel() {
            return shop_tel;
        }

        public void setShop_tel(String shop_tel) {
            this.shop_tel = shop_tel;
        }
    }
    public static class OrderSku implements Serializable{
        /**
         *             "id": "1925",
         *             "order_id": "1757",
         *             "spu_name": "第九铺  人鱼泡沫手链项链耳钉女纯银简约冷淡风学生气质森系闺蜜个性饰品",
         *             "sku_info": "{\"item_id\":\"1339\",\"ivid\":\"5868\",\"item_type\":\"0\",\"num\":1}",
         *             "sku_score": "0",
         *             "spu_earn_score": "0",
         *             "sku_price": "58.00",
         *             "old_price": "0.00",
         *             "eth_price": "0",
         *             "num": "1",
         *             "spu_type": "0",
         *             "sub_total": "58.00",
         *             "status": "13",
         *             "remark": "",
         *             "create_time": "2019-01-10 18:03:00",
         *             "update_time": "2019-01-11 15:19:57",
         *             "del_flag": "0",
         *             "uid": "901",
         *             "pay_channel": "4",
         *             "item_img": "http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpeg/BFWXZNTK12356789_1543042508.jpg"
         *             "type": "手链" //商品规格
         */

        private String id;
        private String order_id;
        private String spu_name;
//        private SkuInfoBean sku_info;
        private String sku_score;
        private String spu_earn_score;
        private String sku_price;
        private String old_price;
        private String eth_price;
        private String num;
        private String spu_type;
        private String sub_total;
        private String status;
        private String remark;
        private String create_time;
        private String update_time;
        private String del_flag;
        private String uid;
        private String pay_channel;
        private String item_img;
        private String type;

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

//        public SkuInfoBean getSku_info() {
//            return sku_info;
//        }
//
//        public void setSku_info(SkuInfoBean sku_info) {
//            this.sku_info = sku_info;
//        }

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

        public String getOld_price() {
            return old_price;
        }

        public void setOld_price(String old_price) {
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
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

        public String getItem_img() {
            return item_img;
        }

        public void setItem_img(String item_img) {
            this.item_img = item_img;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class SkuInfoBean{
            private String item_id;
            private String ivid;
            private String item_type;
            private String num;

            public String getItem_id() {
                return item_id;
            }

            public void setItem_id(String item_id) {
                this.item_id = item_id;
            }

            public String getIvid() {
                return ivid;
            }

            public void setIvid(String ivid) {
                this.ivid = ivid;
            }

            public String getItem_type() {
                return item_type;
            }

            public void setItem_type(String item_type) {
                this.item_type = item_type;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }
        }



    }

    public static class OrderBean implements Serializable{
        /**
         *         "id": "1757",
         *             "order_no": "2019011018025991188290",
         *             "shop_id": "193",
         *             "uid": "901",
         *             "address_id": null,
         *             "product_count": "1",
         *             "amount": "70.00",
         *             "amount_eth": "0",
         *             "logistics_freight": "12.00",
         *             "logistics_no": "",
         *             "logistics_company_code": "",
         *             "hand_balance_amount": "0.00",
         *             "coupon_amount": "0.00",
         *             "pay_score": "0.0000",
         *             "earn_score": "0.0000",
         *             "pay_time": "2019-01-10 19:21:36",
         *             "deliver_time": null,
         *             "remark": "",
         *             "status": "1",
         *             "create_time": "2019-01-10 18:03:00",
         *             "update_time": "2019-01-10 19:24:39",
         *             "del_flag": "0",
         *             "type": "0",
         *             "seller_message": null,
         *             "is_remind": "1",
         *             "display_price": "0",
         *             "to_name": "Peak",
         *             "to_tel": "13858169248",
         *             "to_address": "北京市-北京市-东城区 Hello",
         *             "pay_channel": "4"
         */

        private String id;
        private String order_no;
        private String shop_id;
        private String uid;
        private String address_id;
        private String product_count;
        private String amount;
        private String amount_eth;
        private String logistics_freight;
        private String logistics_no;
        private String logistics_company_code;
        private String hand_balance_amount;
        private String coupon_amount;
        private String pay_score;
        private String earn_score;
        private String pay_time;
        private String deliver_time;
        private String remark;
        private String status;
        private String create_time;
        private String update_time;
        private String del_flag;
        private String type;
        private String seller_message;
        private String is_remind;
        private String display_price;
        private String to_name;
        private String to_tel;
        private String to_address;
        private String pay_channel;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAddress_id() {
            return address_id;
        }

        public void setAddress_id(String address_id) {
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

        public String getAmount_eth() {
            return amount_eth;
        }

        public void setAmount_eth(String amount_eth) {
            this.amount_eth = amount_eth;
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

        public String getEarn_score() {
            return earn_score;
        }

        public void setEarn_score(String earn_score) {
            this.earn_score = earn_score;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSeller_message() {
            return seller_message;
        }

        public void setSeller_message(String seller_message) {
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

        public String getPay_channel() {
            return pay_channel;
        }

        public void setPay_channel(String pay_channel) {
            this.pay_channel = pay_channel;
        }
    }


}
