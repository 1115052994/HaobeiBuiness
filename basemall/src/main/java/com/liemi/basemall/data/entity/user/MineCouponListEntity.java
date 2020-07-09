package com.liemi.basemall.data.entity.user;

import java.util.List;

/*
* 我的优惠券列表
* */
public class MineCouponListEntity {

    private String total_pages;
    private String unused;
    private List<MineCouponEntity> list;

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public String getUnused() {
        return unused;
    }

    public void setUnused(String unused) {
        this.unused = unused;
    }

    public List<MineCouponEntity> getList() {
        return list;
    }

    public void setList(List<MineCouponEntity> list) {
        this.list = list;
    }




    //优惠券
    public static class MineCouponEntity{
        private String id;
        private String status;
        private String end_time;
        private String limit;
        private String reduce;
        private String coupon_id;
        private String name;
        //是否打开规则提示
        private boolean isOpenRule = false;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getLimit() {
            return limit;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }

        public String getReduce() {
            return reduce;
        }

        public void setReduce(String reduce) {
            this.reduce = reduce;
        }

        public String getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(String coupon_id) {
            this.coupon_id = coupon_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isOpenRule() {
            return isOpenRule;
        }

        public void setOpenRule(boolean openRule) {
            isOpenRule = openRule;
        }
    }
}
