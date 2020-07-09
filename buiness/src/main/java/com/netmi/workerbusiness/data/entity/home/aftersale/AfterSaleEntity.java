package com.netmi.workerbusiness.data.entity.home.aftersale;


import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.workerbusiness.R;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/22
 * 修改备注：
 */
public class AfterSaleEntity extends BaseEntity {


    /**
     * "id": "461",                        ## 订单详情ID
     * "order_id": "377",                        ## 所属订单ID
     * "spu_name": "Elegance雅莉格丝",                            ## 商品名称
     * "sku_info": "{\"item_id\":\"1270\",\"ivid\":\"5485\",\"item_type\":\"0\",\"num\":1}",  ## 商品详情（JSON）
     * "sku_score": "0",                        ## 商品折扣积分
     * "spu_earn_score": "1000",                    ## 获赠的积分
     * "sku_price": "0.01",                        ## 商品价格
     * "num": "1",                            ## 购买数量
     * "spu_type": "0",                ## 商品类型
     * "sub_total": "0.01",                        ## 退款金额
     * "status": "6",                        ## 0-未付款1-待发货2-待收货3-待评价4-退货申请5-退货申请驳回6-退货中7-已退货8-取消交易9-交易完成10-支付失败
     * "remark": "",                        ## 商品备注
     * "create_time": "2018-11-19 10:27:19",                        ## 创建时间
     * "update_time": "2018-11-19 13:28:56",                        ## 更新时间
     * "del_flag": "0",
     * "uid": "462",                        ## 用户主键
     * "item_img": "https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15401245979433.jpg"                    ## 商品图片
     * "value_name": "黄色 l"                    ## 规格名称
     * ## 退款数据
     * refund : {"id":"26","order_id":"461","shop_id":"182","price_total":"0.01","remark":"","status":"1","refund_status":"1","create_time":"2018-11-19 13:28:56","update_time":null,"uid":"462","mpid":"377","type":"1","price_total_type":"1","edit_shop_id":"0","refuse_remark":null,"order_no":"2018111910271946803445","back_status":"2","mail_no":null,"mail_name":null,"mail_code":null,"bec_type":"请选择","refund_no":"2018111956569949","agree_time":null,"sucess_time":null,"pay_remark":"","oms_confirm":"0","oms_remark":"","refund_num":"1","shop":{"id":"180","name":"云南连心服装店","shop_tel":155151221252,"accid":null}}
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
    private String status;
    private String remark;
    private String create_time;
    private String update_time;
    private String del_flag;
    private String uid;
    private String item_img;
    private String value_name;
    private RefundBean refund;
    private String param;
    private int statusImage;

    //左边按钮显示的文字
    protected String leftButtonStr;
    //右边按钮显示的文字
    protected String rightButtonStr;

    //    ## 0-未付款1-待发货2-待收货3-待评价4-退货申请5-退货申请驳回6-退货中7-已退货8-取消交易9-交易完成10-支付失败
    public String getLeftButtonStr() {
        switch (Integer.valueOf(status)) {
            case Constant.ORDER_WAIT_PAY:
                return ResourceUtil.getString(R.string.sharemall_order_cancel);
            case Constant.ORDER_WAIT_SEND:
                return "";
//            case Constant.ORDER_WAIT_RECEIVE:
//                return ResourceUtil.getString(R.string.sharemall_order_logistics);
        }
        return "";
    }

    public String getRightButtonStr() {
        switch (Integer.valueOf(status)) { //0-未付款1-待发货2-待收货3-待评价4-退货申请
            // 5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
            case Constant.ORDER_WAIT_PAY:
                return ResourceUtil.getString(R.string.sharemall_order_go_pay);
            case Constant.ORDER_WAIT_SEND:
                return ResourceUtil.getString(R.string.sharemall_order_remind);
            case Constant.ORDER_WAIT_RECEIVE:
                return ResourceUtil.getString(R.string.sharemall_order_confirm_accept);
            case Constant.ORDER_WAIT_COMMENT:
                return ResourceUtil.getString(R.string.sharemall_order_go_comment);
            case Constant.ORDER_CANCEL:
            case Constant.ORDER_SUCCESS:
                return ResourceUtil.getString(R.string.sharemall_order_delete);
            default:
                return "";
        }
    }

    public int getStatusImage() {

        Logs.i("状态图片：" + status);
        switch (Integer.valueOf(status)) { //0-未付款1-待发货2-待收货3-待评价4-退货申请
            // 5-退货申请驳回6-退款退货中7-已退货8-取消交易9-交易完成10-支付失败
            case Constant.ORDER_WAIT_PAY:
                return R.mipmap.sharemall_ic_order_wait_pay;
            case Constant.ORDER_WAIT_SEND:
                return R.mipmap.sharemall_ic_order_wait_send;
            case Constant.ORDER_WAIT_RECEIVE:
                return R.mipmap.sharemall_ic_order_wait_receive;
            case Constant.ORDER_WAIT_COMMENT:
                return R.mipmap.sharemall_ic_order_wait_comment;
            case Constant.ORDER_CANCEL:
                return R.mipmap.sharemall_ic_order_cancel;
            default:
                return R.mipmap.sharemall_ic_order_finish;
        }

    }

    public void setStatusImage(int statusImage) {
        this.statusImage = statusImage;
    }


    public static class SkuInfoBean {
        //  {\"item_id\":\"1597\",\"ivid\":\"6380\",\"item_type\":\"0\",\"num\":1,\"price\":\"0.01\"}
        private String item_id;
        private String ivid;
        private String item_type;
        private String num;
        private String price;

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
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

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public String getValue_name() {
        return value_name;
    }

    public void setValue_name(String value_name) {
        this.value_name = value_name;
    }

    public RefundBean getRefund() {
        return refund;
    }

    public void setRefund(RefundBean refund) {
        this.refund = refund;
    }

    public static class RefundBean {
        /**
         * "id": "26",                            ## 退款主键
         * "order_id": "461",                    ## 子订单id
         * "shop_id": "182",                    ## 店铺主键
         * "price_total": "0.01",                    ## 退款金额
         * "remark": "",                    ## 退款说明
         * "status": "1",                ## 状态：0: 已取消退款退货 1：发起退款 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
         * "refund_status": "1",                    ## 退货时候状态 0.取消退款申请 1.发起退款退货申请    2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
         * "create_time": "2018-11-19 13:28:56",                    ## 发起时间
         * "update_time": null,                    ## 最后一次操作时间
         * "uid": "462",                    ## 退款用户
         * "mpid": "377",                    ## 主订单id
         * "type": "1",                    ## 退款类型 1：未发货 2：已发货
         * "price_total_type": "1",                ## 退款金额类型 1微信原路退款 2线下退款 3打到零钱
         * "edit_shop_id": "0",                    ## 操作人
         * "refuse_remark": null,                        ## 拒绝备注
         * "order_no": "2018111910271946803445",                    ## 退款订单编号
         * "back_status": "2",                            ## 退款时订单状态
         * "mail_no": null,                    ## 快递单号
         * "mail_name": null,                        ## 快递名称
         * "mail_code": null,                    ## 快递公司编号
         * "bec_type": "请选择",                ## 退款原因1
         * "refund_no": "2018111956569949",                    ## 退款编号
         * "agree_time": null,                        ## 卖家同意时间
         * "sucess_time": null,                ## 退款成功时间
         * "pay_remark": "",                    ## 退款到账说明
         * "oms_confirm": "0",                    ## OMS通知状态
         * "oms_remark": "",                    ## oms通知备注
         * "refund_num": "1",                    ## 退货数量
         * ## 店铺信息
         * shop : {"id":"180","name":"云南连心服装店","shop_tel":155151221252,"accid":null}
         */

        private String id;
        private String order_sku_id;
        private String shop_id;
        private String price_total;
        private String remark;
        private String status;
        private String refund_status;
        private String create_time;
        private String update_time;
        private String uid;
        private String mpid;
        private String type;
        private String price_total_type;
        private String edit_shop_id;
        private String refuse_remark;
        private String order_no;
        private String back_status;
        private String mail_no;
        private String mail_name;
        private String mail_code;
        private String bec_type;
        private String refund_no;
        private String agree_time;
        private String sucess_time;
        private String pay_remark;
        private String oms_confirm;
        private String oms_remark;
        private String refund_num;
        private ShopBean shop;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_sku_id() {
            return order_sku_id;
        }

        public void setOrder_sku_id(String order_id) {
            this.order_sku_id = order_id;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getPrice_total() {
            return price_total;
        }

        public void setPrice_total(String price_total) {
            this.price_total = price_total;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        /**
         * ## 状态：0: 已取消退款退货 1：发起退款 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
         */
        public String getStatus() {
            if (status.equals("0")) {
                return "已取消退款退货";
            } else if (status.equals("1")) {
                return "发起退款";
            } else if (status.equals("2")) {
                return "完成退款";
            } else if (status.equals("3")) {
                return "拒绝退款";
            } else if (status.equals("4")) {
                return "取消申请";
            } else if (status.equals("5")) {
                return "退款失败";
            }
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * ## 退货时候状态 0.取消退款申请 1.发起退款退货申请    2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
         */
        public String getRefund_status() {
            if (refund_status.equals("0")) {
                return "取消退款申请";
            } else if (refund_status.equals("1")) {
                return "发起退款退货申请";
            } else if (refund_status.equals("2")) {
                return "卖家同意退货";
            } else if (refund_status.equals("3")) {
                return "等待卖家审核 ";
            } else if (refund_status.equals("4")) {
                return "卖家拒绝";
            } else if (refund_status.equals("5")) {
                return "退款完成";
            }
            return refund_status;
        }

        public void setRefund_status(String refund_status) {
            this.refund_status = refund_status;
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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getMpid() {
            return mpid;
        }

        public void setMpid(String mpid) {
            this.mpid = mpid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice_total_type() {
            return price_total_type;
        }

        public void setPrice_total_type(String price_total_type) {
            this.price_total_type = price_total_type;
        }

        public String getEdit_shop_id() {
            return edit_shop_id;
        }

        public void setEdit_shop_id(String edit_shop_id) {
            this.edit_shop_id = edit_shop_id;
        }

        public String getRefuse_remark() {
            return refuse_remark;
        }

        public void setRefuse_remark(String refuse_remark) {
            this.refuse_remark = refuse_remark;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getBack_status() {
            return back_status;
        }

        public void setBack_status(String back_status) {
            this.back_status = back_status;
        }

        public String getMail_no() {
            return mail_no;
        }

        public void setMail_no(String mail_no) {
            this.mail_no = mail_no;
        }

        public String getMail_name() {
            return mail_name;
        }

        public void setMail_name(String mail_name) {
            this.mail_name = mail_name;
        }

        public String getMail_code() {
            return mail_code;
        }

        public void setMail_code(String mail_code) {
            this.mail_code = mail_code;
        }

        public String getBec_type() {
            return bec_type;
        }

        public void setBec_type(String bec_type) {
            this.bec_type = bec_type;
        }

        public String getRefund_no() {
            return refund_no;
        }

        public void setRefund_no(String refund_no) {
            this.refund_no = refund_no;
        }

        public String getAgree_time() {
            return agree_time;
        }

        public void setAgree_time(String agree_time) {
            this.agree_time = agree_time;
        }

        public String getSucess_time() {
            return sucess_time;
        }

        public void setSucess_time(String sucess_time) {
            this.sucess_time = sucess_time;
        }

        public String getPay_remark() {
            return pay_remark;
        }

        public void setPay_remark(String pay_remark) {
            this.pay_remark = pay_remark;
        }

        public String getOms_confirm() {
            return oms_confirm;
        }

        public void setOms_confirm(String oms_confirm) {
            this.oms_confirm = oms_confirm;
        }

        public String getOms_remark() {
            return oms_remark;
        }

        public void setOms_remark(String oms_remark) {
            this.oms_remark = oms_remark;
        }

        public String getRefund_num() {
            return refund_num;
        }

        public void setRefund_num(String refund_num) {
            this.refund_num = refund_num;
        }

        public ShopBean getShop() {
            return shop;
        }

        public void setShop(ShopBean shop) {
            this.shop = shop;
        }

        public static class ShopBean {
            /**
             * id : 180
             * name : 云南连心服装店
             * shop_tel : 155151221252
             * accid : null
             */

            private String id;
            private String name;
            private String shop_tel;
            private String accid;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getShop_tel() {
                return shop_tel;
            }

            public void setShop_tel(String shop_tel) {
                this.shop_tel = shop_tel;
            }

            public String getAccid() {
                return accid;
            }

            public void setAccid(String accid) {
                this.accid = accid;
            }
        }
    }
}
