package com.netmi.workerbusiness.data.entity.home.offlineorder;

import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.workerbusiness.R;

import java.util.List;

public class OfflineOrderDetailEntity extends BaseEntity {
    /**
     * order_no : 366615441721792385
     * id : 10738
     * type : 11
     * create_time : 2019-10-09 15:58:52
     * pay_time : 2019-10-09 16:04:51
     * amount : 11.00
     * status : 1
     * sku_info : {"item_id":"11","item_type":0,"ivid":11,"num":1,"price":"11.00"}
     * code : 1967156865
     * num : 1
     * title : 测试商品1号
     * price : 11.00
     * img_url : https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/AFWXZMTHK0124789_1569206663.jpeg
     * start_date : 2019-10-09
     * end_date : 2019-10-31
     * purchase_note : 111111111
     * total_price : 11.00
     * used_time :
     * earn_score : 0.0000
     * refund_time : 2019-10-13 21:37:15
     * user : {"nickname":"Near","head_url":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK9BHsYJRKhYWGicDqKWukWYics7mQohWXK2ia95RoGKMHrtxB2wNJtiac5qbiaL7kum1OXjkibRfTUx3ibg/132","phone":"18856855449"}
     */

    private String order_no;
    private String id;
    private String type;
    private String create_time;
    private String pay_time;
    private String amount;
    private int status;
    private String status_name;
    private String sku_info;
    private String code;
    private String num;
    private String title;
    private String price;
    private String img_url;
    private String start_date;
    private String end_date;
    private String purchase_note;
    private String total_price;
    private String used_time;
    private String earn_score;
    private String refund_time;
    private RefundBean refundMsg;
    private UserBean user;
    private String useTime;
    private int statusImage;//状态图片

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSku_info() {
        return sku_info;
    }

    public void setSku_info(String sku_info) {
        this.sku_info = sku_info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPurchase_note() {
        return purchase_note;
    }

    public void setPurchase_note(String purchase_note) {
        this.purchase_note = purchase_note;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getUsed_time() {
        return used_time;
    }

    public void setUsed_time(String used_time) {
        this.used_time = used_time;
    }

    public String getEarn_score() {
        return earn_score;
    }

    public void setEarn_score(String earn_score) {
        this.earn_score = earn_score;
    }

    public String getRefund_time() {
        return refund_time;
    }

    public void setRefund_time(String refund_time) {
        this.refund_time = refund_time;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getStatus_name() {
        //状态 0待付款；1待核销；3待评价；9完成；7已退款  4：申请退款中  5： 拒绝退款  7： 退款成功
//        if (status == 0) {
//            return "待付款";
//        } else
        if (status == 1) {
            return "待核销";
        } else {
            return "已使用";
        }
//        } else if (status == 3) {
//            return "待评价";
//        } else if (status == 9) {
//            return "完成";
//        } else if (status == 7) {
//            return "已退款";
//        } else if (status == 4) {
//            return "申请退款中";
//        } else if (status == 5) {
//            return "退款已拒绝";
//        }
//        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public int getStatusImage() {
        Logs.i("状态图片：" + status);
        switch (status) {    //状态 0待付款；1待核销；3待评价；9完成；7已退款

            case 0:
                return R.mipmap.sharemall_ic_order_wait_receive;
            case 1:
                return R.mipmap.sharemall_ic_order_wait_pay;
//            return R.mipmap.sharemall_ic_order_wait_send;
            case 3:
                return R.mipmap.sharemall_ic_order_finish;
            case 9:
                return R.mipmap.sharemall_ic_order_finish;
            case 7:
                return R.mipmap.ic_code;
            default:
                return R.mipmap.sharemall_ic_order_finish;
        }
    }

    public void setStatusImage(int statusImage) {
        this.statusImage = statusImage;
    }

    public String getUseTime() {
        return "有效期:" + start_date + "~" + end_date;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public RefundBean getRefundMsg() {
        return refundMsg;
    }

    public void setRefundMsg(RefundBean refundMsg) {
        this.refundMsg = refundMsg;
    }

    public static class UserBean {
        /**
         * nickname : Near
         * head_url : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK9BHsYJRKhYWGicDqKWukWYics7mQohWXK2ia95RoGKMHrtxB2wNJtiac5qbiaL7kum1OXjkibRfTUx3ibg/132
         * phone : 18856855449
         */

        private String nickname;
        private String head_url;
        private String phone;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static class RefundBean {

        /**
         * img_urls : ["https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/ABFWXZNTHK012457_1555565197.png"]
         * refund_remark : 买多了
         * refund_status : 1
         * "approval_time": "2019-10-13 21:37:15",        //执行审批时间
         * "apply_time": "2019-10-13 21:37:15",           //发起退款时间
         */

        private String refund_remark;
        private String reason_remark;
        private String refund_status;
        private String approval_time;
        private String apply_time;
        private List<String> img_urls;


        public String getRefund_remark() {
            return refund_remark;
        }

        public void setRefund_remark(String refund_remark) {
            this.refund_remark = refund_remark;
        }

        public String getRefund_status() {
            return refund_status;
        }

        public void setRefund_status(String refund_status) {
            this.refund_status = refund_status;
        }

        public List<String> getImg_urls() {
            return img_urls;
        }

        public void setImg_urls(List<String> img_urls) {
            this.img_urls = img_urls;
        }

        public String getReason_remark() {
            return reason_remark;
        }

        public void setReason_remark(String reason_remark) {
            this.reason_remark = reason_remark;
        }

        public String getApproval_time() {
            return approval_time;
        }

        public void setApproval_time(String approval_time) {
            this.approval_time = approval_time;
        }

        public String getApply_time() {
            return apply_time;
        }

        public void setApply_time(String apply_time) {
            this.apply_time = apply_time;
        }
    }
}
