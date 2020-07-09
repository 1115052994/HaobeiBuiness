package com.netmi.workerbusiness.data.entity.home.coupon;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/21
 * 修改备注：
 */
public class CouponListEntity extends BaseEntity {
    /**
     * id	int	优惠券ID
     * name	string	优惠券名称
     * rule	object	规则
     * m	int	满多少
     * j	int	减多少
     * total_num	int	库存
     * used_coupon	int	已使用
     * create_time	string	开始时间
     * update_time	string	截止时间
     * sw	int	发放状态 0:已下架或禁用 1:发放中或启用
     * is_expire	int	是否已过期 0:未过期 1:已过期
     */

    private String id;
    private String cf_id;
    private String coupon_type;
    private String use_type;
    private String item_type;
    private String name;
    private String remark;
    private RuleBean rule;
    private String give_num;
    private String expire_hour;
    private String shop_id;
    private String total_num;
    private String del_flag;
    private String create_time;
    private String update_time;
    private String sw;
    private String price;
    private String used_coupon;
    private int is_expire;
    private String swStr;
    private String useTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCf_id() {
        return cf_id;
    }

    public void setCf_id(String cf_id) {
        this.cf_id = cf_id;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public String getUse_type() {
        return use_type;
    }

    public void setUse_type(String use_type) {
        this.use_type = use_type;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
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

    public RuleBean getRule() {
        return rule;
    }

    public void setRule(RuleBean rule) {
        this.rule = rule;
    }

    public String getGive_num() {
        return give_num;
    }

    public void setGive_num(String give_num) {
        this.give_num = give_num;
    }

    public String getExpire_hour() {
        return expire_hour;
    }

    public void setExpire_hour(String expire_hour) {
        this.expire_hour = expire_hour;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public String getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
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

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUsed_coupon() {
        return used_coupon;
    }

    public void setUsed_coupon(String used_coupon) {
        this.used_coupon = used_coupon;
    }

    public int getIs_expire() {
        return is_expire;
    }

    public void setIs_expire(int is_expire) {
        this.is_expire = is_expire;
    }

    public String getSwStr() {
        //  * sw	int	发放状态 0:已下架或禁用 1:发放中或启用
        //   * is_expire	int	是否已过期 0:未过期 1:已过期
        if (is_expire == 0) {
            if (sw.equals("0")) {
                return "已下架";
            }
            return "发放中";
        } else {
            return "已过期";
        }

    }

    public void setSwStr(String swStr) {
        this.swStr = swStr;
    }

    public String getUseTime() {

        return "有效期 " + create_time + "-" + update_time;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public static class RuleBean {
        /**
         * m : 500
         * j : 50
         */

        private String m;
        private String j;

        public String getM() {
            return m;
        }

        public void setM(String m) {
            this.m = m;
        }

        public String getJ() {
            return j;
        }

        public void setJ(String j) {
            this.j = j;
        }
    }
}
