package com.liemi.basemall.data.entity.order;

import com.liemi.basemall.R;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.Strings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/9/9 20:50
 * 修改备注：
 */
public class RefundDetailedEntity implements Serializable {

    /**
     * "remark": "222",//退款原因
     "price_total": "11.00",//退款金额
     "status": "1",//退款状态 0: 已取消退款退货 1：发起退款 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
     "refund_num": "1",//退款件数
     "create_time": "2018-11-19 13:01:27",
     "refund_no": "2018111955555210",//退款编号
     "refund_status": "1",0.取消退款申请 1.发起退款退货申请    2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
     "imgs": "https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15401245979433.jpg",
     "type": "1",//1：未发货 2：已发货
     'agree_time',//卖家同意时间
     'sucess_time',//退款成功时间
     'bec_type',//退款原因
     'refuse_remark'//拒绝备注
     */

    private String id;
    private String order_id;
    private String remark;
    private String price_total;
    private int status;
    private String refund_num;
    private String create_time;
    private String update_time;     //申请时间
    private String refund_no;
    private int refund_status;
    private String imgs;
    private int type;
    private String agree_time;
    private String success_time; //退款成功时间
    private String no_pass_time; //拒绝退款时间
    private String failed_time; //退款失败时间
    private String bec_type;
    private String refuse_remark;
    private List<RefundImgsBean> meRefundImgs;
    private List<LTOrderDetailedEntity.OrderSkusBean> orderSku;
    private RefundListEntity.ShopTelBean tel;

    private String mail_no; //快递单号
    private String mail_name; //快递名称
    private String mail_code; //快递公司编号

    private List<OrderDetailsEntity.MeOrdersBean> meOrders;
    private List<String> hints;
    private String hintTitle;

    private PayOrderBean order;

    public String getStatusToString() {
        if (type==1){   //1：未发货 2：已发货
            switch (status) {        //退款状态 0: 已取消退款退货 1：发起退款
                // 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）

                case 0:
                    return "退款已取消";
                case 1:
                    return "等待商家处理";
                case 2:
                    return "退款成功";
                case 3:
                    return "商家拒绝退款";
                case 4:
                    return "退款已取消";
                case 5:
                    return "退款失败";
                default:
                    return "";
            }
        }else{
            switch (refund_status) {        //0.取消退款申请 1.发起退款退货申请
                // 2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
                case 0:
                    return "退款已取消";
                case 1:
                    return "等待商家处理";
                case 2:
                    return "填写物流单号";
                case 3:
                    return "等待商家处理";
                case 4:
                    return "商家拒绝退款";
                case 5:
                    return "退款成功";
                default:
                    return "";
            }
        }
    }

    public String getShowHintTitle(){
        if (Strings.isEmpty(hintTitle)){
            if (type==1){   //1：未发货 2：已发货
                switch (status) {        //退款状态 0: 已取消退款退货 1：发起退款
                    // 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
                    case 1:
                        hints=new ArrayList<>();
                        hints.add("·商家同意，系统将退款给您");
                        hints.add("· 如果商家拒绝，您可以修改退款申请后再次发起，商家会重新处理");
                        hintTitle="您已成功发起退款申请，请耐心等待商家处理";
                        break;
                    case 3:
                        hints=null;
                        hintTitle="拒绝原因："+refuse_remark;
                        break;
                    case 5:
                        hints=null;
                        hintTitle="退款失败：因微信功能受限，为确保您的退款金额，请您与卖家线下沟通";
                    default:
                        hints=null;
                        hintTitle="";
                        break;
                }
            }else{
                switch (refund_status) {        //0.取消退款申请 1.发起退款退货申请
                    // 2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
                    case 1:
                        hints=new ArrayList<>();
                        hints.add("·商家同意，系统将退款给您");
                        hints.add("· 如果商家拒绝，您可以修改退款申请后再次发起，商家会重新处理");
                        hintTitle="您已成功发起退款申请，请耐心等待商家处理";
                        break;
                    case 2:
                        hintTitle="商家已同意您的退货退款申请，请填写物流单号";
                        hints=null;
                        break;
                    case 3:
                        hints=new ArrayList<>();
                        hints.add("·商家同意，系统将退款给您");
                        hints.add("· 如果商家拒绝，您可以修改退款申请后再次发起，商家会重新处理");
                        hintTitle="您已填写物流信息，请耐心等待商家处理";
                        break;
                    case 4:
                        hints=null;
                        hintTitle="拒绝原因："+refuse_remark;
                        break;
                    default:
                        hints=null;
                        hintTitle="";
                        break;
                }
            }
        }
        return hintTitle;
    }

    public String getLeftBtnText() {
        if (type==1) {   //1：未发货 2：已发货
            switch (status) {        //退款状态 0: 已取消退款退货 1：发起退款
                // 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
                case 1:
                    return "取消申请";
                case 3:
                    return "取消申请";
                default:
                    return "";
            }
        }else {
            switch (refund_status) {//0.取消退款申请 1.发起退款退货申请
                // 2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
                case 1:
                    return "取消申请";
                case 2:
                    return "取消申请";
                case 3:
                    return "取消申请";
                case 4:
                    return "取消申请";
                default:
                    return "";
            }
        }
    }

    public String getRightBtnText() {
        if (type==1) {   //1：未发货 2：已发货
            switch (status) {        //退款状态 0: 已取消退款退货 1：发起退款
                // 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
                case 1:
                    return "修改申请";
                case 3:
                    return "重新申请";
                default:
                    return "";
            }
        }else {
            switch (refund_status) { //0.取消退款申请 1.发起退款退货申请
                // 2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
                case 1:
                    return "修改申请";
                case 2:
                    return "填写物流";
                case 3:
                    return "修改申请";
                case 4:
                    return "重新申请";
                default:
                    return "";
            }
        }
    }


    public int getStatusResource() {

        if (type==1) {   //1：未发货 2：已发货
            switch (status) {        //退款状态 0: 已取消退款退货 1：发起退款
                // 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
                case 1:
                    return R.mipmap.ic_refund_wait;
                case 2:
                    return R.mipmap.ic_order_success;
                case 3:
                    return R.mipmap.ic_refund_fail;
                case 5:
                    return R.mipmap.ic_refund_fail;
                default:
                    return R.mipmap.ic_refund_fail;
            }
        }else {
            switch (refund_status) {//0.取消退款申请 1.发起退款退货申请
                // 2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
                case 1:
                    return R.mipmap.ic_refund_wait;
                case 2:
                    return R.mipmap.ic_order_wait_receive;
                case 3:
                    return R.mipmap.ic_refund_wait;
                case 4:
                    return R.mipmap.ic_refund_fail;
                case 5:
                    return R.mipmap.ic_order_success;
                default:
                    return R.mipmap.ic_refund_fail;
            }
        }
    }

    public PayOrderBean getOrder() {
        return order;
    }

    public void setOrder(PayOrderBean order) {
        this.order = order;
    }

    public RefundListEntity.ShopTelBean getTel() {
        return tel;
    }

    public void setTel(RefundListEntity.ShopTelBean tel) {
        this.tel = tel;
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

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getShowPrice(){
        if (Strings.isEmpty(price_total))return "";
        return FloatUtils.formatMoney(price_total);
    }

    public List<String> getHints() {
        return hints;
    }

    public void setHints(List<String> hints) {
        this.hints = hints;
    }

    public String getHintTitle() {
        return hintTitle;
    }

    public void setHintTitle(String hintTitle) {
        this.hintTitle = hintTitle;
    }

    public String getAgree_time() {
        return agree_time;
    }

    public void setAgree_time(String agree_time) {
        this.agree_time = agree_time;
    }

    public String getSuccess_time() {
        return success_time;
    }

    public void setSuccess_time(String success_time) {
        this.success_time = success_time;
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

    public List<LTOrderDetailedEntity.OrderSkusBean> getOrderSku() {
        return orderSku;
    }

    public void setOrderSku(List<LTOrderDetailedEntity.OrderSkusBean> orderSku) {
        this.orderSku = orderSku;
    }

    public List<OrderDetailsEntity.MeOrdersBean> getMeOrders() {
        if (meOrders == null) {
            meOrders = new ArrayList<>();
            for (LTOrderDetailedEntity.OrderSkusBean skusBean : getOrderSku()) {
                skusBean.setValue_names(skusBean.getType());
                meOrders.add(new OrderDetailsEntity.MeOrdersBean(skusBean));
            }
        }
        return meOrders;
    }

    public void setMeOrders(List<OrderDetailsEntity.MeOrdersBean> meOrders) {
        this.meOrders = meOrders;
    }

    public List<RefundImgsBean> getMeRefundImgs() {
        return meRefundImgs;
    }

    public void setMeRefundImgs(List<RefundImgsBean> meRefundImgs) {
        this.meRefundImgs = meRefundImgs;
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
        return price_total;
    }

    public void setPrice_total(String price_total) {
        this.price_total = price_total;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class RefundImgsBean implements Serializable{
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

    public static class PayOrderBean implements Serializable{
        private String pay_channel; //支付类型4以太币
        private String amount_eth; //以太币金额

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
    }
}
