package com.netmi.workerbusiness.data.entity.home.aftersale;

import android.util.Log;

import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.workerbusiness.R;

import java.util.List;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/26
 * 修改备注：
 */
public class AfterSaleDetailEntity extends BaseEntity {
    /**
     * id : 24
     * order_id : 380
     * remark : 222
     * price_total : 11.00
     * status : 1
     * refund_num : 1
     * create_time : 2018-11-19 13:01:27
     * refund_no : 2018111955555210
     * refund_status : 1
     * imgs : https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15401245979433.jpg
     * type : 1
     * agree_time :
     * sucess_time :
     * state :
     * no_pass_time :
     * failed_time :
     * bec_type :
     * refuse_remark :
     * mail_no :
     * mail_name :
     * mail_code :
     * meRefundImgs : [{"id":"18","img_url":"111","r_id":"24"}]
     * tel : {"shop_tel":"15515121252"}
     * shop : {"id":"151","name":"衣服店","shop_tel":"15067124172","accid":null}
     * orderSku : [{"id":"464","order_id":"380","spu_name":"第九铺 2018新款彩虹蛋糕包 仙女本仙热烈推荐 小主甜一夏","sku_info":"{\"item_id\":\"1268\",\"ivid\":\"5457\",\"item_type\":\"0\",\"num\":1}","sku_score":"0","spu_earn_score":"2","sku_price":"108.00","num":"1","spu_type":"0","sub_total":"108.00","status":"1","remark":"","create_time":"2018-11-19 10:28:02","update_time":"2018-11-19 10:28:02","del_flag":"0","uid":null,"type":"L"}]
     */

    private String id;
    private String order_id;
    private String remark;
    private String price_total;
    private int status;
    private String refund_num;
    private String create_time;
    private String refund_no;
    private int refund_status;
    private String imgs;
    private String type;
    private String agree_time;
    private String sucess_time;
    private String state;
    private String no_pass_time;
    private String failed_time;
    private String bec_type;
    private String refuse_remark;
    private String mail_no;
    private String mail_name;
    private String mail_code;
    private TelBean tel;
    private ShopBean shop;
    private List<MeRefundImgsBean> meRefundImgs;

    private List<OrderSkuBean> orderSku;
    private int statusImage;
    private String title_name;
    private UserInfoBean user_info;


    public String getTitle_name() {

        if (type.equals("1")) {//type退款类型 1：未发货(退款) 2：已发货(退货退款)
//   ## 状态：0: 已取消退款退货 1：发起退款 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
            if (status == 0) {
                return "已取消退款退货";
            } else if (status == 1) {
                return "等待商家处理";
            } else if (status == 2) {
                return "完成退款";
            } else if (status == 3) {
                return "拒绝退款";
            } else if (status == 4) {
                return "取消申请";
            } else if (status == 5) {
                return "退款失败";
            }

        } else {
////0.取消退款申请 1.发起退款退货申请    2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
            if (refund_status == 0) {
                return "退款申请已取消";
            } else if (refund_status == 1) {
                return "等待商家处理";
            } else if (refund_status == 2) {
                return "填写物流单号";
            } else if (refund_status == 3) {
                return "等待商家处理";
            } else if (refund_status == 4) {
                return "商家拒绝退款";
            } else if (refund_status == 5) {
                return "退款成功";
            }
        }

        return title_name;
    }

    public int getStatusImage() {

        Logs.i("状态图片：" + refund_status);
        switch (refund_status) {
            ////0.取消退款申请 1.发起退款退货申请    2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
            case 1:
            case 3:
                return R.mipmap.sharemall_ic_order_wait_pay;
            case 2:
                return R.mipmap.sharemall_ic_order_wait_receive;
            case 4:
                return R.mipmap.ic_cancel_red;
            case 5:
            default:
                return R.mipmap.sharemall_ic_order_finish;
        }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPrice_total() {
        return FloatUtils.formatDouble(price_total);

    }

    public void setPrice_total(String price_total) {
        this.price_total = price_total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRefund_num() {
        return refund_num;
    }

    public void setRefund_num(String refund_num) {
        this.refund_num = refund_num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRefund_no() {
        return refund_no;
    }

    public void setRefund_no(String refund_no) {
        this.refund_no = refund_no;
    }

    public int getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(int refund_status) {
        this.refund_status = refund_status;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNo_pass_time() {
        return no_pass_time;
    }

    public void setNo_pass_time(String no_pass_time) {
        this.no_pass_time = no_pass_time;
    }

    public String getFailed_time() {
        return failed_time;
    }

    public void setFailed_time(String failed_time) {
        this.failed_time = failed_time;
    }

    public String getBec_type() {
        return bec_type;
    }

    public void setBec_type(String bec_type) {
        this.bec_type = bec_type;
    }

    public String getRefuse_remark() {
        return refuse_remark;
    }

    public void setRefuse_remark(String refuse_remark) {
        this.refuse_remark = refuse_remark;
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

    public TelBean getTel() {
        return tel;
    }

    public void setTel(TelBean tel) {
        this.tel = tel;
    }

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public List<MeRefundImgsBean> getMeRefundImgs() {
        return meRefundImgs;
    }

    public void setMeRefundImgs(List<MeRefundImgsBean> meRefundImgs) {
        this.meRefundImgs = meRefundImgs;
    }

    public List<OrderSkuBean> getOrderSku() {
        return orderSku;
    }

    public void setOrderSku(List<OrderSkuBean> orderSku) {
        this.orderSku = orderSku;
    }

    public void setStatusImage(int statusImage) {
        this.statusImage = statusImage;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }


    public static class TelBean {
        /**
         * shop_tel : 15515121252
         */

        private String shop_tel;

        public String getShop_tel() {
            return shop_tel;
        }

        public void setShop_tel(String shop_tel) {
            this.shop_tel = shop_tel;
        }
    }

    public static class ShopBean {
        /**
         * id : 151
         * name : 衣服店
         * shop_tel : 15067124172
         * accid : null
         */

        private String id;
        private String name;
        private String shop_tel;
        private Object accid;

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

        public Object getAccid() {
            return accid;
        }

        public void setAccid(Object accid) {
            this.accid = accid;
        }
    }

    public static class MeRefundImgsBean {
        /**
         * id : 18
         * img_url : 111
         * r_id : 24
         */

        private String id;
        private String img_url;
        private String r_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getR_id() {
            return r_id;
        }

        public void setR_id(String r_id) {
            this.r_id = r_id;
        }
    }

    public static class OrderSkuBean {
        /**
         * id : 464
         * order_id : 380
         * spu_name : 第九铺 2018新款彩虹蛋糕包 仙女本仙热烈推荐 小主甜一夏
         * sku_info : {"item_id":"1268","ivid":"5457","item_type":"0","num":1}
         * sku_score : 0
         * spu_earn_score : 2
         * sku_price : 108.00
         * num : 1
         * spu_type : 0
         * sub_total : 108.00
         * status : 1
         * remark :
         * create_time : 2018-11-19 10:28:02
         * update_time : 2018-11-19 10:28:02
         * del_flag : 0
         * uid : null
         * type : L
         * "value_names": "胶原蛋白",//规格
         * "item_img": "https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/ABFWXZMTHK124689_1560146716.png"//商品图片
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
        private Object uid;
        private String type;
        private String value_names;
        private String item_img;

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

        public Object getUid() {
            return uid;
        }

        public void setUid(Object uid) {
            this.uid = uid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue_names() {
            return value_names;
        }

        public void setValue_names(String value_names) {
            this.value_names = value_names;
        }

        public String getItem_img() {
            return item_img;
        }

        public void setItem_img(String item_img) {
            this.item_img = item_img;
        }
    }

    public class UserInfoBean {
        private String name;
        private String tel;
        private String address;
        private String head_url;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getHead_url() {
            return head_url;
        }

        public void setHead_url(String head_url) {
            this.head_url = head_url;
        }
    }
}
