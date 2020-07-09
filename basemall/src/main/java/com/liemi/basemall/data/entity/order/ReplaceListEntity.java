package com.liemi.basemall.data.entity.order;



import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;

/**
 * 换货列表
 */

public class ReplaceListEntity extends BaseEntity implements Serializable {
    /**
     *                 "id": "461",                        ## 订单详情ID
     *                 "order_id": "377",                        ## 所属订单ID
     *                 "spu_name": "Elegance雅莉格丝",                            ## 商品名称
     *                 "sku_info": "{\"item_id\":\"1270\",\"ivid\":\"5485\",\"item_type\":\"0\",\"num\":1}",
     *                             ## 商品详情（JSON）
     *                 "sku_score": "0",                        ## 商品折扣积分
     *                 "spu_earn_score": "1000",                    ## 获赠的积分
     *                 "sku_price": "0.01",                        ## 商品价格
     *                 "num": "1",                            ## 购买数量
     *                 "spu_type": "0",                ## 商品类型
     *                 "sub_total": "0.01",                        ## 退款金额
     *                 "status": "6",                        ## 0-未付款1-待发货2-待收货3-待评价4-退货申请5-退货申请驳回6-退货中7-已退货8-取消交易9-交易完成10-支付失败-11申请换货-12拒绝换-13换货中-14换货完成
     *                 "remark": "",                        ## 商品备注
     *                 "create_time": "2018-11-19 10:27:19",                        ## 创建时间
     *                 "update_time": "2018-11-19 13:28:56",                        ## 更新时间
     *                 "del_flag": "0",
     *                 "uid": "462",                        ## 用户主键
     *                 "item_img": "https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15401245979433.jpg"                    ## 商品图片
     *                 "value_name": "黄色 l"                    ## 规格名称
     *                 "change": {                    ## 退款数据
     *                     "id": "26",                    ## 退款主键
     *                     "order_id": "461",            ## 子订单id
     *                     "shop_id": "182",            ## 店铺主键
     *                     "remark": "",            ## 退款原因
     *                     "status": "1",                ## 状态：0: 已取消换货 1：发起换货 2：完成换货 3、拒绝换货
     *                     "create_time": "2018-11-19 13:28:56",        ## 发起时间
     *                     "update_time": null,            ## 最后一次操作时间
     *                     "uid": "462",            ## 退款用户
     *                     "edit_shop_id": "0",                    ## 操作人
     *                     "refuse_remark": null,            ## 拒绝备注
     *                     "agree_time": null,                ## 卖家同意时间
     *                     "sucess_time": null,                ## 退款成功时间
     *                     "phone": "",        联系电话
     *                     "shop": {                    ## 店铺信息
     *                         "id": "180",
     *                         "name": "云南连心服装店"            ## 店铺名称
     *                         "shop_tel":155151221252
     *                     }
     *                 },
     */

    private String id;
    private String order_id;
    private String spu_name;
//    private SkuInfoBean sku_info;
    private String sku_score;
    private String spu_earn_score;
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
    private String item_img;
    private String value_name;
    private String pay_channel;
    private ChangeBean change;

    public String getStatusToString(){
        switch (getChange().getStatus()){
            case "0":
                return "已取消换货";
            case "1":
                return "换货中";
            case "2":
                return "换货成功";
            case "3":
                return "换货失败";
            default:
                return "";
        }
    }

    public String getLeftBtnText() {
        switch (getChange().getStatus()) {//退货时候状态 0.取消退款申请 1.发起退款退货申请    2、卖家同意退货
            // 3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
//            case "2":
//                return "查看物流";
            default:
                return "联系客服";
        }
    }

    public String getRightBtnText() {
        switch (getChange().getStatus()) { //退货时候状态 0.取消退款申请 1.发起退款退货申请    2、卖家同意退货
            // 3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
            default:
                return "查看详情";
        }
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

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
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

//    public SkuInfoBean getSku_info() {
//        return sku_info;
//    }
//
//    public void setSku_info(SkuInfoBean sku_info) {
//        this.sku_info = sku_info;
//    }

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

    public ChangeBean getChange() {
        return change;
    }

    public void setChange(ChangeBean change) {
        this.change = change;
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

    public static class ChangeBean{
        /**
         "change": {                    ## 退款数据
         *                     "id": "26",                    ## 退款主键
         *                     "order_id": "461",            ## 子订单id
         *                     "shop_id": "182",            ## 店铺主键
         *                     "remark": "",            ## 退款原因
         *                     "status": "1",                ## 状态：0: 已取消换货 1：发起换货 2：完成换货 3、拒绝换货
         *                     "create_time": "2018-11-19 13:28:56",        ## 发起时间
         *                     "update_time": null,            ## 最后一次操作时间
         *                     "uid": "462",            ## 退款用户
         *                     "edit_shop_id": "0",                    ## 操作人
         *                     "refuse_remark": null,            ## 拒绝备注
         *                     "agree_time": null,                ## 卖家同意时间
         *                     "sucess_time": null,                ## 退款成功时间
         *                     "phone": "",        联系电话
         *                     "shop": {                    ## 店铺信息
         *                         "id": "180",
         *                         "name": "云南连心服装店"            ## 店铺名称
         *                         "shop_tel":155151221252
         *                     }
         *                 },
         */

        private String id;
        private String order_sku_id;
        private String shop_id;
        private String remark;
        private String status;
        private String create_time;
        private String update_time;
        private String uid;
        private String edit_shop_id;
        private String refuse_remark;
        private String agree_time;
        private String success_time;
        private String no_pass_time;
        private String phone;
        private ShopBean shop;
        private String item_img;
        private String value_name;

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

        public String getOrder_sku_id() {
            return order_sku_id;
        }

        public void setOrder_sku_id(String order_sku_id) {
            this.order_sku_id = order_sku_id;
        }

        public String getSuccess_time() {
            return success_time;
        }

        public void setSuccess_time(String success_time) {
            this.success_time = success_time;
        }

        public String getNo_pass_time() {
            return no_pass_time;
        }

        public void setNo_pass_time(String no_pass_time) {
            this.no_pass_time = no_pass_time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }



        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getAgree_time() {
            return agree_time;
        }

        public void setAgree_time(String agree_time) {
            this.agree_time = agree_time;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public ShopBean getShop() {
            return shop;
        }

        public void setShop(ShopBean shop) {
            this.shop = shop;
        }

        public static class ShopBean{
            private String id;
            private String name;
            private String shop_tel;

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
        }
    }
}
