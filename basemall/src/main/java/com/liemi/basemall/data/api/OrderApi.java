package com.liemi.basemall.data.api;

import com.liemi.basemall.data.entity.AliPayData;
import com.liemi.basemall.data.entity.WXPayData;
import com.liemi.basemall.data.entity.comment.CommentEntity;
import com.liemi.basemall.data.entity.comment.MuchCommentEntity;
import com.liemi.basemall.data.entity.eth.YMSEntity;
import com.liemi.basemall.data.entity.order.LogisticCompanyEntity;
import com.liemi.basemall.data.entity.order.LogisticEntity;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.data.entity.order.OrderPayEntity;
import com.liemi.basemall.data.entity.order.OrderRefundEntity;
import com.liemi.basemall.data.entity.order.RefundDetailedEntity;
import com.liemi.basemall.data.entity.order.RefundListEntity;
import com.liemi.basemall.data.entity.order.RefundPriceEntity;
import com.liemi.basemall.data.entity.order.RefundReasonEntity;
import com.liemi.basemall.data.entity.order.ReplaceInfoEntity;
import com.liemi.basemall.data.entity.order.ReplaceListEntity;
import com.liemi.basemall.vo.OrderCommentVo2;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Bingo on 2018/8/23.
 */

public interface OrderApi {
    /**
     * 删除订单
     */
    @FormUrlEncoded
    @POST("order/order-api/delete")
    Observable<BaseData> delOrder(@Field("order_id") String orderId);

    /**
     * 提醒发货
     */
    @FormUrlEncoded
    @POST("order/order-api/remind")
    Observable<BaseData> remindOrder(@Field("order_id") String orderId);

    /**
     * 订单取消
     */
    @FormUrlEncoded
    @POST("order/order-api/cancel")
    Observable<BaseData> cancelOrder(@Field("order_id") String orderId);

    /**
     * 确认收货
     */
    @FormUrlEncoded
    @POST("order/order-api/confirm")
    Observable<BaseData> confirmReceipt(@Field("order_id") String orderId);

    /**
     * 评论订单
     */
    @POST("item/me-commet-api/create")
    Observable<BaseData> orderComment(@Body OrderCommentVo2 vo);

    /**
     * 订单列表
     */
    @FormUrlEncoded
    @POST("order/order-api/order-list")
    Observable<BaseData<PageEntity<OrderDetailsEntity>>> listOrder(@Field("start_page") int start_page,
                                                                   @Field("pages") int pages,
                                                                   @Field("status") String status);

    /**
     * 物流数据获取
     */
    @FormUrlEncoded
    @POST("order/order-api/logistics-list")
    Observable<BaseData<List<LogisticEntity>>> getLogistic(@Field("order_no") String order_no);


    /**
     * 订单详情获取
     */
    @FormUrlEncoded
    @POST("order/order-api/order-info")
    Observable<BaseData<OrderDetailsEntity>> getOrderDetail(@Field("order_id") String order_id);

    /**
     * 订单批量评论
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("item/me-commet-api/create")
    Observable<BaseData> submitComment(@Body MuchCommentEntity muchCommentEntity);

    /**
     * 订单申请退款(未发货状态时退款)
     * id	是	int	子订单id
     * name	是	string	退款原因
     * remark	否	string	退款备注
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/refund-back")
    Observable<BaseData> applyRefund(@Field("id") String orderId,
                                     @Field("name") String refundReason,
                                     @Field("remark") String refundRemark,
                                     @Field("price_total") String priceTotal);

    /**
     * 订单申请退款(已发货状态时退款)
     * id	是	int	子订单id
     * name	是	string	退款原因
     * remark	否	string	退款备注
     * img_url	否	string	退款图片数组
     * price_total	是	int	退款价格
     * refund_status	是	string	1等待商家审核2商家已审核，用户填写快递单号
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/goods-back")
    Observable<BaseData> applyRefundGood(@Field("id") String orderId,
                                         @Field("name") String refundReason,
                                         @Field("remark") String refundRemark,
                                         @Field("price_total") String priceTotal,
                                         @Field("img_url[]") List<String> imgUrls,
                                         @Field("refund_status") int refundStatus,
                                         @Field("state") int state);

    /**
     * 获取退款金额和邮费
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/postage")
    Observable<BaseData<RefundPriceEntity>> getRefundPrice(@Field("id") String orderId);

    /**
     * 订单申请退款(未发货状态时退款)
     * id	是	int	子订单id
     * name	是	string	退款原因
     * remark	否	string	退款备注
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/add")
    Observable<BaseData> updateApplyRefund(@Field("id") String orderId,
                                           @Field("name") String refundReason,
                                           @Field("remark") String refundRemark,
                                           @Field("price_total") String priceTotal,
                                           @Field("type") int type);

    /**
     * 订单申请退款(已发货状态时退款)
     * id	是	int	子订单id
     * name	是	string	退款原因
     * remark	否	string	退款备注
     * img_url	否	string	退款图片数组
     * price_total	是	int	退款价格
     * refund_status	是	string	1等待商家审核2商家已审核，用户填写快递单号
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/add")
    Observable<BaseData> updateApplyRefundGood(@Field("id") String orderId,
                                               @Field("name") String refundReason,
                                               @Field("remark") String refundRemark,
                                               @Field("price_total") String priceTotal,
                                               @Field("img_urls[]") List<String> imgUrls,
                                               @Field("type") int type);

    /**
     * 填写退货物流
     * mail_no	否	string	快递单号(refund_status为2时传)
     * mail_name	否	string	快递名称(refund_status为2时传)
     * mail_code	否	string	快递编号(refund_status为2时传)
     * refund_status	是	string	1等待商家审核2商家已审核，用户填写快递单号
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/goods-back")
    Observable<BaseData> applyRefundLogistic(@Field("id") String id,
                                             @Field("mail_no") String logisticsNo,
                                             @Field("mail_name") String logisticsCompany,
                                             @Field("mail_code") String companyCode,
                                             @Field("img_url[]") List<String> imgs,
                                             @Field("refund_status") int refundStatus);

    /**
     * 填写退货物流
     * mail_no	否	string	快递单号(refund_status为2时传)
     * mail_name	否	string	快递名称(refund_status为2时传)
     * mail_code	否	string	快递编号(refund_status为2时传)
     * refund_status	是	string	1等待商家审核2商家已审核，用户填写快递单号
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/agree")
    Observable<BaseData> updateApplyLogistic(@Field("id") String id,
                                             @Field("mail_no") String logisticsNo,
                                             @Field("mail_name") String logisticsCompany,
                                             @Field("mail_code") String companyCode,
                                             @Field("img_url[]") List<String> imgs,
                                             @Field("type") int refundStatus);

    /**
     * 获取物流公司列表
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/delivery")
    Observable<BaseData<List<LogisticCompanyEntity>>> listLogisticCompany(@Field("param") int param);

    /**
     * 获取退款原因列表
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/run")
    Observable<BaseData<List<RefundReasonEntity>>> listRefundReason(@Field("param") int param);


    /**
     * 退款列表
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/refund-list")
    Observable<BaseData<PageEntity<RefundListEntity>>> listOrderRefund(@Field("start_page") int startPage,
                                                                       @Field("pages") int pages);

    /**
     * 换货列表
     */
    @FormUrlEncoded
    @POST("order/order-change-api/index")
    Observable<BaseData<PageEntity<ReplaceListEntity>>> listGoodChange(@Field("start_page") int startPage,
                                                                       @Field("pages") int pages);

    /**
     * 申请换货
     */
    @FormUrlEncoded
    @POST("order/order-change-api/change")
    Observable<BaseData> getChange(@Field("id") String id,
                                   @Field("remark") String remark,
                                   @Field("phone") String phone,
                                   @Field("img_url[]") List<String> img_url);

    /**
     * 换货详情
     */
    @FormUrlEncoded
    @POST("order/order-change-api/view")
    Observable<BaseData<ReplaceInfoEntity>> getChangeDetail(@Field("order_sku_id") int order_sku_id);


    /**
     * 取消换货申请
     */
    @FormUrlEncoded
    @POST("order/order-change-api/cancel")
    Observable<BaseData> getReplaceCancel(@Field("id") int id);

    /**
     * 修改换货订单
     */
    @FormUrlEncoded
    @POST("order/order-change-api/add")
    Observable<BaseData> getChangeUpdate(@Field("id") String id,
                                         @Field("remark") String remark,
                                         @Field("phone") String phone,
                                         @Field("img_urls[]") List<String> img_url);


    /**
     * 商品评论列表获取
     *
     * @param flag 是否有图 0：否 1：是
     */
    @FormUrlEncoded
    @POST("item/me-commet-api/index")
    Observable<BaseData<PageEntity<CommentEntity>>> listComment(@Field("start_page") int start_page,
                                                                @Field("pages") int pages,
                                                                @Field("item_id") String item_id,
                                                                @Field("flag") String flag);

    /**
     * 根据订单号获取支付信息
     */
    @FormUrlEncoded
    @POST("pay/pay-api/reset-order")
    Observable<BaseData<OrderPayEntity>> getOrderPayInfo(@Field("order_id") String order_id);

    /**
     * 支付宝支付接口
     *
     * @param pay_channel 支付渠道 支付渠道:0-微信支付1-支付宝 3-积分支付
     */
    @FormUrlEncoded
    @POST("pay/pay-api/app")
    Observable<BaseData<String>> orderPayAli(@Field("order_id") String pay_order_no,
                                             @Field("channel") String pay_channel);

    /**
     * 微信支付接口
     */
    @FormUrlEncoded
    @POST("pay/pay-api/app")
    Observable<BaseData<WXPayData>> orderPayWechat(@Field("order_id") String pay_order_no,
                                                   @Field("channel") String pay_channel);


    /**
     * Eth 支付
     */
    @FormUrlEncoded
    @POST("pay/pay-api/eth")
    Observable<BaseData> orderPayETH(@Field("order_id") String pay_order_no,
                                     @Field("channel") String pay_channel,
                                     @Field("password") String password);

    /**
     * yms支付
     */
    @FormUrlEncoded
    @POST("pay/pay-api/yms-pay")
    Observable<BaseData> orderPayYMS(@Field("order_id") String pay_order_no,
                                     @Field("channel") String pay_channel,
                                     @Field("password") String password,
                                     @Field("code") String code);

    /**
     * 获取钱包余额
     */
    @FormUrlEncoded
    @POST("yms/yms-wallet-api/get-yms")
    Observable<BaseData<YMSEntity>> getYMS(@Field("param") int param);


    /**
     * 退款详情
     * refund_id	是	int	退款id	1
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/refund-details")
    Observable<BaseData<RefundDetailedEntity>> getRefundDetail(@Field("refund_id") String refundId);


    /**
     * 退款取消申请
     * id	是	int	子订单id
     * type	是	string	1未发货状态时取消申请退款2已发货状态时取消申请退款
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/cancel")
    Observable<BaseData> cancelRefundApply(@Field("id") String orderId, @Field("type") int type);
}
