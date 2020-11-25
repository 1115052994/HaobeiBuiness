package com.netmi.workerbusiness.data.entity.mine;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/18
 * 修改备注：
 */
public class ShopInfoEntity extends BaseEntity {

    /**
     * "id": "278",
     * logo_url	string	店铺LOGO
     * img_url	string	店铺图片
     * name	string	店铺名称
     * remark	string	店铺简介
     * opening_hours	string	营业时间
     * longitude	string	经度
     * latitude	string	维度
     * score	int	商户信用分
     * shop_user_type	int	用户选择商户类型 0:未选择类型 1:线上 2:线下 3:线上+线下
     * shop_apply_status	int	用户申请入驻审核状态 0:未申请 1:审核中 2:审核成功 3:审核失败
     * shop_pay_status	int	用户缴费审核状态 0:未申请 1:审核中 2:审核成功 3:审核失败
     * shop_admin_tel	string	商家管理员电话
     * expire_notice	int 过期提醒天数 如果大于 30 则不提醒 如果为负数 则为过期多少天
     * "is_popup": 1///是否第一次弹窗0否1是
     */

    private String id;
    private String logo_url;
    private String img_url;
    private String name;
    private String remark;
    private String full_name;
    private String sum_item;
    private String content;
    private String shop_tel;
    private String checkin_time;
    private Object qrcode;
    private String rccode;
    private int score;
    private String collection;
    private String opening_hours;
    private String longitude;
    private String latitude;
    private int is_collection;
    private String sum_collection;
    private int shop_user_type;
    private int shop_apply_status;
    private String shop_apply_status_str;
    private int shop_pay_status;
    private int expire_notice;
    private String expire_notice_str;
    private String shop_admin_tel;
    private String expire_time;
    private String end_time;
    private String p_name;
    private String c_name;
    private String d_name;
    private String p_id;
    private String c_id;
    private String d_id;
    private String address;
    private String service_prove;   //服务费证明图片
    private String deposit_prove;   //押金证明图片
    private String reason;   //押金证明图片
    private String is_popup;   //押金证明图片

    private String is_sign_contract;//判断是否跳转合同列表
    private String is_improve_info;//显示弹框  店铺信息待完善，请注意查看补充
    private String is_bind_bank;
    private String district;

    private String real_name;
    private String idcard;
    private String is_apply;
    private String phone;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getIs_apply() {
        return is_apply;
    }

    public void setIs_apply(String is_apply) {
        this.is_apply = is_apply;
    }
    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }
    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIs_improve_info() {
        return is_improve_info;
    }

    public void setIs_improve_info(String is_improve_info) {
        this.is_improve_info = is_improve_info;
    }
    public String getIs_bind_bank() {
        return is_bind_bank;
    }

    public void setIs_bind_bank(String is_bind_bank) {
        this.is_bind_bank = is_bind_bank;
    }

    public String getIs_sign_contract() {
        return is_sign_contract;
    }

    public void setIs_sign_contract(String is_sign_contract) {
        this.is_sign_contract = is_sign_contract;
    }

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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    public String getDistrictistrict() {
        return district;
    }

    public void setDistrictistrict(String district) {
        this.district = district;
    }

    public String getSum_item() {
        return sum_item;
    }

    public void setSum_item(String sum_item) {
        this.sum_item = sum_item;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShop_tel() {
        return shop_tel;
    }

    public void setShop_tel(String shop_tel) {
        this.shop_tel = shop_tel;
    }

    public String getCheckin_time() {
        return checkin_time;
    }

    public void setCheckin_time(String checkin_time) {
        this.checkin_time = checkin_time;
    }

    public Object getQrcode() {
        return qrcode;
    }

    public void setQrcode(Object qrcode) {
        this.qrcode = qrcode;
    }

    public String getRccode() {
        return rccode;
    }

    public void setRccode(String rccode) {
        this.rccode = rccode;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(String opening_hours) {
        this.opening_hours = opening_hours;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getIs_collection() {
        return is_collection;
    }

    public void setIs_collection(int is_collection) {
        this.is_collection = is_collection;
    }

    public String getSum_collection() {
        return sum_collection;
    }

    public void setSum_collection(String sum_collection) {
        this.sum_collection = sum_collection;
    }

    public int getShop_user_type() {
        return shop_user_type;
    }

    public void setShop_user_type(int shop_user_type) {
        this.shop_user_type = shop_user_type;
    }


    public int getShop_apply_status() {
        return shop_apply_status;
    }

    public void setShop_apply_status(int shop_apply_status) {
        this.shop_apply_status = shop_apply_status;
    }

    public int getShop_pay_status() {
        return shop_pay_status;
    }

    public void setShop_pay_status(int shop_pay_status) {
        this.shop_pay_status = shop_pay_status;
    }


    public int getExpire_notice() {
        return expire_notice;
    }

    public void setExpire_notice(int expire_notice) {
        this.expire_notice = expire_notice;
    }

    public String getExpire_notice_str() {
        if (expire_notice > 0) {
            return "将于" + expire_notice + "天后过期，立即续费";
        } else {
            return "您的账号已过期，请立即续费";
        }
    }

    public void setExpire_notice_str(String expire_notice_str) {
        this.expire_notice_str = expire_notice_str;
    }

    public String getShop_admin_tel() {
        return shop_admin_tel;
    }

    public void setShop_admin_tel(String shop_admin_tel) {
        this.shop_admin_tel = shop_admin_tel;
    }

    /**
     * int	用户申请入驻审核状态 0:未申请 1:审核中 2:审核成功 3:审核失败
     */

    public String getShop_apply_status_str() {
        if (shop_apply_status == 0) {
            return "未申请";
        } else if (shop_apply_status == 1) {
            return "认证中";
        } else if (shop_apply_status == 2) {
            return "认证通过";
        } else {
            return "认证失败";
        }
    }

    public void setShop_apply_status_str(String shop_apply_status_str) {
        this.shop_apply_status_str = shop_apply_status_str;
    }


    public String getExpire_time() {
        return "身份到期:" + expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getD_name() {
        return d_name;
    }

    public void setD_name(String d_name) {
        this.d_name = d_name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getService_prove() {
        return service_prove;
    }

    public void setService_prove(String service_prove) {
        this.service_prove = service_prove;
    }

    public String getDeposit_prove() {
        return deposit_prove;
    }

    public void setDeposit_prove(String deposit_prove) {
        this.deposit_prove = deposit_prove;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIs_popup() {
        return is_popup;
    }

    public void setIs_popup(String is_popup) {
        this.is_popup = is_popup;
    }
}
