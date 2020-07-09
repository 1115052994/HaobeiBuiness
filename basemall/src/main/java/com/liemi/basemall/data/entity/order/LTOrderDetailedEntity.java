package com.liemi.basemall.data.entity.order;

import com.liemi.basemall.data.entity.StoreEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/10/17 11:09
 * 修改备注：
 */
public class LTOrderDetailedEntity extends BaseEntity implements Serializable {

    /**
     * "id": "78",//订单id
     "order_no": "2018101615543922855200",//订单编码
     "shop_id": null,//商城编号
     "address_id": null,//收货人地址ID
     "product_count": "2",//SKU 数量
     "amount": "18.00",//订单总价
     "logistics_freight": "0.00",//运费金额
     "logistics_no": "",//物流编号
     "logistics_company_code": "",//物流公司编号
     "pay_score": "3",//抵用积分
     "earn_score": "20",//获赠的积分
     "pay_time": null,//支付时间
     "deliver_time": null,//发货时间
     "remark": "",//备注
     "status": "0",//0-未付款1-待发货2-待收货3-待评价4-退货申请5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
     "type": "1",//订单类型:\r\n0:主商城\r\n1:积分商城
     "seller_message": null,//卖家留言
     "is_remind": "0",//是否提醒卖家；1已提醒；0未提醒
     "to_name": "蓬蓬",//收货人姓名
     "to_tel": "13858169288",//收货人电话
     "to_address": "广东省-深圳市-宝安区 反反复复",//收货地址
     "create_time": "2018-10-16 15:54:39",//创建时间
     "update_time": "2018-10-16 15:54:39",//更新时间
     "orderSkus": [//订单商品数组
         {
         "id": "116",//订单详情ID
         "order_id": "78",//所属订单ID
         "spu_name": "积分商品第一件",//商品名称
         "sku_info": "{\"item_id\":\"1240\",\"ivid\":5315}",//商品详情(JSON)
         "sku_score": "1",//商品折扣积分
         "spu_earn_score": "10",//获赠的积分
         "sku_price": "1.00",//商品价格
         "num": "12",//购买数量
         "spu_type": "1",//商品类型(等同于item_type）
         "sub_total": "12.00",//商品小计金额
         "remark": "",//商品备注
         "status": "0",//0-未付款1-待发货2-待收货3-待评价4-退货申请5-退货申请驳回6-退款退货中7-已退货8-取消交易9-交易完成10-支付失败
         "create_time": "2018-10-16 15:54:39",//创建时间
         "update_time": "2018-10-16 15:54:39",//更新时间
         "del_flag": "0",//删除标记:0-正常1-删除
         "img_url": "https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15387956016558.jpg",//商品图片
         "value_names": "黄色 l"//商品规格名称
         "item_type": "0",//商品类型\r\n0:普通商品\r\n1:纯积分商品\r\n2:现金+积分商品
         "item_id": "1240"
         },
     * "shop": null,//店铺信息，若为空，为积分商城。根据type字段判断是否为积分商城
     "status_name": "未付款"//订单状态中文
     */

    private String id;
    private String order_no;
    private String shop_id;
    private String address_id;
    private String product_count;
    private String amount;
    private String logistics_freight;
    private String logistics_no;
    private String logistics_company_code;
    private String pay_score;
    private String earn_score;
    private String pay_time;
    private String deliver_time;
    private String remark;
    private int status;
    //订单类型:0:主商城    1:积分商城
    private int type;
    private String seller_message;
    private String is_remind;
    private String to_name;
    private String to_tel;
    private String to_address;
    private String create_time;
    private String update_time;
    private String status_name;
    private List<OrderSkusBean> orderSkus;
    private StoreEntity shop;

    public String getShowPrice() {
        return type == 1 ? FloatUtils.formatResult(pay_score, getAmount()) : FloatUtils.formatMoney(getAmount());
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public StoreEntity getShop() {
        return shop;
    }

    public void setShop(StoreEntity shop) {
        this.shop = shop;
    }

    public static class OrderSkusBean implements Serializable {
        /**
         * id : 107
         * order_id : 72
         * spu_name : 积分商品第一件
         * sku_info : {"item_id":"1240","ivid":5315}
         * sku_score : 1
         * spu_earn_score : 10
         * sku_price : 1.00
         * num : 12
         * spu_type : 1
         * sub_total : 12.00
         * remark :
         * replace_status : 0
         * create_time : 2018-10-14 11:51:59
         * update_time : 2018-10-14 11:51:59
         * del_flag : 0
         * img_url : https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15387956016558.jpg
         * value_names : 黄色 l
         */

        private String id;
        private String order_id;
        private String spu_name;
        private String sku_info;
        private String sku_score;
        private String spu_earn_score;
        private String sku_price;
        private String num;
        private String spu_type;
        private String sub_total;
        private String remark;
        private String replace_status;
        private String create_time;
        private String update_time;
        private String del_flag;
        private String img_url;
        private String value_names;
        private int item_type;
        private String item_id;
        private int status;
        private String type;    //退款详情接口中代替value_names;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getShowPrice() {
            switch (item_type) {
                case 1:
                    return FloatUtils.formatScore(sku_score);
                case 2:
                    return FloatUtils.formatMoney(sku_price) + "+" + FloatUtils.formatScore(sku_score);
                default:
                    return FloatUtils.formatMoney(sku_price);
            }
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public int getItem_type() {
            return item_type;
        }

        public void setItem_type(int item_type) {
            this.item_type = item_type;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getReplace_status() {
            return replace_status;
        }

        public void setReplace_status(String replace_status) {
            this.replace_status = replace_status;
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
    }
}
