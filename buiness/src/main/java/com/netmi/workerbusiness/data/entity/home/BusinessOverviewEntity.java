package com.netmi.workerbusiness.data.entity.home;

import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;

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
         * order_money : {"yesterday":"0","today":"0"}
         * pay_money : {"yesterday":"0","today":"0"}
         * on_sale_item : 0
         * out_sale_item : 0
         * total_sale_item : 0
         */

        private CountOrderBean count_order;
        private OrderMoneyBean order_money;
        private PayMoneyBean pay_money;
        private String on_sale_item;
        private String out_sale_item;
        private String total_sale_item;

        public CountOrderBean getCount_order() {
            return count_order;
        }

        public void setCount_order(CountOrderBean count_order) {
            this.count_order = count_order;
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

        public static class OrderMoneyBean {
            /**
             * yesterday : 0
             * today : 0
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return String.valueOf(FloatUtils.formatDouble(yesterday));
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return String.valueOf(FloatUtils.formatDouble(today));
            }

            public void setToday(String today) {
                this.today = today;
            }
        }

        public static class PayMoneyBean {
            /**
             * yesterday : 0
             * today : 0
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return String.valueOf(FloatUtils.formatDouble(yesterday));
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return String.valueOf(FloatUtils.formatDouble(today));
            }

            public void setToday(String today) {
                this.today = today;
            }
        }
    }

    public static class OutLineDataBean {
        /**
         * count_order : {"yesterday":"0","today":"0"}
         * order_money : {"yesterday":"0","today":"0"}
         * pay_money : {"yesterday":"0","today":"0"}
         * on_sale_item : 0
         * out_sale_item : 0
         * total_sale_item : 0
         */

        private CountOrderBeanX count_order;
        private OrderMoneyBeanX order_money;
        private PayMoneyBeanX pay_money;
        private String on_sale_item;
        private String out_sale_item;
        private String total_sale_item;

        public CountOrderBeanX getCount_order() {
            return count_order;
        }

        public void setCount_order(CountOrderBeanX count_order) {
            this.count_order = count_order;
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

        public static class OrderMoneyBeanX {
            /**
             * yesterday : 0
             * today : 0
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return String.valueOf(FloatUtils.formatDouble(yesterday));
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return String.valueOf(FloatUtils.formatDouble(today));
            }

            public void setToday(String today) {
                this.today = today;
            }
        }

        public static class PayMoneyBeanX {
            /**
             * yesterday : 0
             * today : 0
             */

            private String yesterday;
            private String today;

            public String getYesterday() {
                return String.valueOf(FloatUtils.formatDouble(yesterday));
            }

            public void setYesterday(String yesterday) {
                this.yesterday = yesterday;
            }

            public String getToday() {
                return String.valueOf(FloatUtils.formatDouble(today));
            }

            public void setToday(String today) {
                this.today = today;
            }
        }
    }
}
