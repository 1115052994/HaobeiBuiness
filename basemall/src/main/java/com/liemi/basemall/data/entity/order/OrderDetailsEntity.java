package com.liemi.basemall.data.entity.order;

import com.liemi.basemall.R;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.utils.FloatUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/25 10:12
 * 修改备注：
 */
public class OrderDetailsEntity extends LTOrderDetailedEntity implements Serializable {

    private String uid;

    //退款金额
    private String refund_price;
    //退款路径
    private String pay_name;
    private List<MeOrdersBean> meOrders;
    private ReceiptBean receipt;
    private UBean u;
    private String pay_channel; //支付类型，ETH.
    private String amount_eth; //以太币金额
    private int refundStatus;   //1:退款退货-仅退款 2:退款退货-退款退货

    public String getStatusToString() {
        switch (getStatus()) {
            case Constant.ORDER_WAIT_PAY:
                return "待付款";
            case Constant.ORDER_WAIT_SEND:
                return "待发货";
            case Constant.ORDER_WAIT_RECEIVE:
                return "待收货";
            case Constant.ORDER_WAIT_COMMENT:
                return "待评价";
            case Constant.ORDER_CANCEL:
                return "交易取消";
            case Constant.ORDER_SUCCESS:
                return "交易完成";
            case Constant.ORDER_PAY_FAIL:
                return "支付失败";
            default:
                return "";
        }
    }

    public String getLeftBtnText() {
        switch (getStatus()) {//0-未付款1-待发货2-待收货3-待评价4-退货申请
            // 5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
            case 0:
                return "取消订单";
            case 2:
                return "查看物流";
            case 3:
                return "查看物流";
            default:
                return "";
        }
    }

    public String getRightBtnText() {
        switch (getStatus()) { //0-未付款1-待发货2-待收货3-待评价4-退货申请
            // 5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
            case 0:
                return "去付款";
            case 1:
                return "提醒发货";
            case 2:
                return "确认收货";
            case 3:
                return "去评价";
            case 8:
            case 9:
                return "删除订单";
            default:
                return "";
        }
    }


    public int getStatusResource() {
        switch (getStatus()) {
            case 0:
                return R.mipmap.ic_order_wait_pay;
            case 1:
                return R.mipmap.ic_order_wait_send;
            case 2:
                return R.mipmap.ic_order_wait_receive;
            case 3:
                return R.mipmap.ic_order_wait_comment;
            default:
                return R.mipmap.ic_order_success;
        }
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getDigitalPrice(){
        return "YMS"+amount_eth;
    }

    public String getAmount_eth() {
        return amount_eth;
    }

    public void setAmount_eth(String amount_eth) {
        this.amount_eth = amount_eth;
    }

    public String getMpid() {
        return getId();
    }

    public String getPostage() {
        return getLogistics_freight();
    }

    public String getName() {
        return getTo_name();
    }

    public void setName(String name) {
        super.setTo_name(name);
    }

    public String getTel() {
        return getTo_tel();
    }

    public void setTel(String tel) {
        super.setTo_tel(tel);
    }

    public String getAddress() {
        return "收货地址："+getTo_address();
    }

    public void setAddress(String address) {
        super.setTo_address(address);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPrice_total() {
        return getAmount();
    }

    public void setPrice_total(String price_total) {
        super.setAmount(price_total);
    }

    public String getRefund_price() {
        return refund_price;
    }

    public void setRefund_price(String refund_price) {
        this.refund_price = refund_price;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public List<MeOrdersBean> getMeOrders() {
        if (meOrders == null) {
            meOrders = new ArrayList<>();
            for (OrderSkusBean skusBean : getOrderSkus()) {
                meOrders.add(new MeOrdersBean(skusBean));
            }
        }
        return meOrders;
    }

    public void setMeOrders(List<MeOrdersBean> meOrders) {
        this.meOrders = meOrders;
    }

    public ReceiptBean getReceipt() {
        return receipt;
    }

    public void setReceipt(ReceiptBean receipt) {
        this.receipt = receipt;
    }

    public UBean getU() {
        return u;
    }

    public void setU(UBean u) {
        this.u = u;
    }

    public static class MeOrdersBean implements Serializable {

        private String id;
        private String order_id;
        private int status; //0-未付款1-待发货2-待收货3-待评价4-退货申请5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
        private String price;
        private String type;
        private String price_total;
        private String num;
        private String remark;
        private String item_id;
        private String img_url;
        private String title;
        private String create_time;
        private String color_name;
        private int item_type;

        public MeOrdersBean() {
        }

        public MeOrdersBean(OrderSkusBean skusBean) {
            id = skusBean.getId();
            item_id = skusBean.getItem_id();
            order_id = skusBean.getOrder_id();
            price = skusBean.getShowPrice();
            price_total = skusBean.getSub_total();
            num = skusBean.getNum();
            remark = skusBean.getRemark();
            img_url = skusBean.getImg_url();
            title = skusBean.getSpu_name();
            create_time = skusBean.getCreate_time();
            color_name = skusBean.getValue_names();
            status=skusBean.getStatus();
        }

        public String getShowPrice() {
            return FloatUtils.formatMoney(price_total);
        }

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice_total() {
            return price_total;
        }

        public void setPrice_total(String price_total) {
            this.price_total = price_total;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
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

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getColor_name() {
            return color_name;
        }

        public void setColor_name(String color_name) {
            this.color_name = color_name;
        }

        public int getItem_type() {
            return item_type;
        }

        public void setItem_type(int item_type) {
            this.item_type = item_type;
        }
    }

    public static class ReceiptBean implements Serializable {

        private String mpid;
        private String price;
        private int type;
        private String com_name = "";
        private String duty_no;
        private String create_time;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCom_name() {
            return com_name;
        }

        public void setCom_name(String com_name) {
            this.com_name = com_name;
        }

        public String getDuty_no() {
            return duty_no;
        }

        public void setDuty_no(String duty_no) {
            this.duty_no = duty_no;
        }

        public String getMpid() {
            return mpid;
        }

        public void setMpid(String mpid) {
            this.mpid = mpid;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }

    public static class UBean implements Serializable {
        private String uid;
        private String phone;
        private String nickname;
        private String head_url;
        private String level;
        private String sex;
        private String age;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHead_url() {
            return head_url;
        }

        public void setHead_url(String head_url) {
            this.head_url = head_url;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

    @Override
    public String toString() {
        return "OrderDetailsEntity{" +
                "uid='" + uid + '\'' +
                ", refund_price='" + refund_price + '\'' +
                ", pay_name='" + pay_name + '\'' +
                ", meOrders=" + meOrders +
                ", receipt=" + receipt +
                ", u=" + u +
                '}';
    }
}
