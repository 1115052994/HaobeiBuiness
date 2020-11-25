package com.netmi.workerbusiness.data.entity.home;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/3
 * 修改备注：
 */
public class BusinessOverviewEntity extends BaseEntity {

    /**
     * on_line_data	array	线上数据
     * out_line_data	array	线下数据
     * yesterday	array	昨天数据
     * today	array	今天数据
     * count_order	array	订单数量
     * order_money	array	订单成交金额
     * pay_money	array	订单支付金额
     * on_sale_item	array	已上架商品数量
     * out_sale_item	array	已下架商品数量
     * total_sale_item	array	总商品数量
     */


    /**
     * on_line_data : {"count_order":{"yesterday":"0","today":"0"},"user_num":{"yesterday":"0","today":"0"},"order_money":{"yesterday":"0.00","today":"0.00"},"pay_money":{"yesterday":"0.00","today":"0.00"},"on_sale_item":"20","out_sale_item":"0","total_sale_item":"20","page_view":{"today":"0","yesterday":"0","thirty_days":"2"}}
     * out_line_data : {"count_order":{"yesterday":"0","today":"0"},"user_num":{"yesterday":"0","today":"0"},"order_money":{"yesterday":"0.00","today":"0.00"},"pay_money":{"yesterday":"0.00","today":"0.00"},"on_sale_item":"0","out_sale_item":"0","total_sale_item":"0","page_view":{"today":"0","yesterday":"0","thirty_days":"0"}}
     */

    private OnLineDataBean on_line_data;
    private OutLineDataBean out_line_data;

    public OnLineDataBean getOn_line_data() {
        return on_line_data;
    }

    public void setOn_line_data(OnLineDataBean on_line_data) {
        this.on_line_data = on_line_data;
    }

    public OutLineDataBean getOut_line_data() {
        return out_line_data;
    }

    public void setOut_line_data(OutLineDataBean out_line_data) {
        this.out_line_data = out_line_data;
    }

    public static class OnLineDataBean {
        /**
         * count_order : {"yesterday":"0","today":"0"}
         * user_num : {"yesterday":"0","today":"0"}
         * order_money : {"yesterday":"0.00","today":"0.00"}
         * pay_money : {"yesterday":"0.00","today":"0.00"}
         * on_sale_item : 20
         * out_sale_item : 0
         * total_sale_item : 20
         * page_view : {"today":"0","yesterday":"0","thirty_days":"2"}
         */

        private CountOrderBean count_order;
        private UserNumBean user_num;
        private OrderMoneyBean order_money;
        private PayMoneyBean pay_money;
        private String on_sale_item;
        private String out_sale_item;
        private String total_sale_item;
        private PageViewBean page_view;

        public CountOrderBean getCount_order() {
            return count_order;
        }

        public void setCount_order(CountOrderBean count_order) {
            this.count_order = count_order;
        }

        public UserNumBean getUser_num() {
            return user_num;
        }

        public void setUser_num(UserNumBean user_num) {
            this.user_num = user_num;
        }

        public OrderMoneyBean getOrder_money() {
            return order_money;
        }

        public void setOrder_money(OrderMoneyBean order_money) {
            this.order_money = order_money;
        }

        public PayMoneyBean getPay_money() {
            return pay_money;
        }

        public void setPay_money(PayMoneyBean pay_money) {
            this.pay_money = pay_money;
        }

        public String getOn_sale_item() {
            return on_sale_item;
        }

        public void setOn_sale_item(String on_sale_item) {
            this.on_sale_item = on_sale_item;
        }

        public String getOut_sale_item() {
            return out_sale_item;
        }

        public void setOut_sale_item(String out_sale_item) {
            this.out_sale_item = out_sale_item;
        }

        public String getTotal_sale_item() {
            return total_sale_item;
        }

        public void setTotal_sale_item(String total_sale_item) {
            this.total_sale_item = total_sale_item;
        }

        public PageViewBean getPage_view() {
            return page_view;
        }

        public void setPage_view(PageViewBean page_view) {
            this.page_view = page_view;
        }

        public static class CountOrderBean {
            /**
             * yesterday : 0
             * today : 0
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return yesterday;
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return today;
            }

            public void setToday(String today) {
                this.today = today;
            }
        }

        public static class UserNumBean {
            /**
             * yesterday : 0
             * today : 0
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return yesterday;
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return today;
            }

            public void setToday(String today) {
                this.today = today;
            }
        }

        public static class OrderMoneyBean {
            /**
             * yesterday : 0.00
             * today : 0.00
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return yesterday;
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return today;
            }

            public void setToday(String today) {
                this.today = today;
            }
        }

        public static class PayMoneyBean {
            /**
             * yesterday : 0.00
             * today : 0.00
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return yesterday;
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return today;
            }

            public void setToday(String today) {
                this.today = today;
            }
        }

        public static class PageViewBean {
            /**
             * today : 0
             * yesterday : 0
             * thirty_days : 2
             */

            private String today;
            private String yesterday;
            private String thirty_days;

            public String getToday() {
                return today;
            }

            public void setToday(String today) {
                this.today = today;
            }

            public String getYesterday() {
                return yesterday;
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getThirty_days() {
                return thirty_days;
            }

            public void setThirty_days(String thirty_days) {
                this.thirty_days = thirty_days;
            }
        }
    }

    public static class OutLineDataBean {
        /**
         * count_order : {"yesterday":"0","today":"0"}
         * user_num : {"yesterday":"0","today":"0"}
         * order_money : {"yesterday":"0.00","today":"0.00"}
         * pay_money : {"yesterday":"0.00","today":"0.00"}
         * on_sale_item : 0
         * out_sale_item : 0
         * total_sale_item : 0
         * page_view : {"today":"0","yesterday":"0","thirty_days":"0"}
         */

        private CountOrderBeanX count_order;
        private UserNumBeanX user_num;
        private OrderMoneyBeanX order_money;
        private PayMoneyBeanX pay_money;
        private String on_sale_item;
        private String out_sale_item;
        private String total_sale_item;
        private PageViewBeanX page_view;

        public CountOrderBeanX getCount_order() {
            return count_order;
        }

        public void setCount_order(CountOrderBeanX count_order) {
            this.count_order = count_order;
        }

        public UserNumBeanX getUser_num() {
            return user_num;
        }

        public void setUser_num(UserNumBeanX user_num) {
            this.user_num = user_num;
        }

        public OrderMoneyBeanX getOrder_money() {
            return order_money;
        }

        public void setOrder_money(OrderMoneyBeanX order_money) {
            this.order_money = order_money;
        }

        public PayMoneyBeanX getPay_money() {
            return pay_money;
        }

        public void setPay_money(PayMoneyBeanX pay_money) {
            this.pay_money = pay_money;
        }

        public String getOn_sale_item() {
            return on_sale_item;
        }

        public void setOn_sale_item(String on_sale_item) {
            this.on_sale_item = on_sale_item;
        }

        public String getOut_sale_item() {
            return out_sale_item;
        }

        public void setOut_sale_item(String out_sale_item) {
            this.out_sale_item = out_sale_item;
        }

        public String getTotal_sale_item() {
            return total_sale_item;
        }

        public void setTotal_sale_item(String total_sale_item) {
            this.total_sale_item = total_sale_item;
        }

        public PageViewBeanX getPage_view() {
            return page_view;
        }

        public void setPage_view(PageViewBeanX page_view) {
            this.page_view = page_view;
        }

        public static class CountOrderBeanX {
            /**
             * yesterday : 0
             * today : 0
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return yesterday;
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return today;
            }

            public void setToday(String today) {
                this.today = today;
            }
        }

        public static class UserNumBeanX {
            /**
             * yesterday : 0
             * today : 0
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return yesterday;
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return today;
            }

            public void setToday(String today) {
                this.today = today;
            }
        }

        public static class OrderMoneyBeanX {
            /**
             * yesterday : 0.00
             * today : 0.00
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return yesterday;
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return today;
            }

            public void setToday(String today) {
                this.today = today;
            }
        }

        public static class PayMoneyBeanX {
            /**
             * yesterday : 0.00
             * today : 0.00
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return yesterday;
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return today;
            }

            public void setToday(String today) {
                this.today = today;
            }
        }

        public static class PageViewBeanX {
            /**
             * today : 0
             * yesterday : 0
             * thirty_days : 0
             */

            private String today;
            private String yesterday;
            private String thirty_days;

            public String getToday() {
                return today;
            }

            public void setToday(String today) {
                this.today = today;
            }

            public String getYesterday() {
                return yesterday;
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getThirty_days() {
                return thirty_days;
            }

            public void setThirty_days(String thirty_days) {
                this.thirty_days = thirty_days;
            }
        }
    }





}
