package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.workerbusiness.data.entity.home.OrderEvaluationEntity;
import com.netmi.workerbusiness.data.entity.home.lineorder.LineOrderDetailEntity;
import com.netmi.workerbusiness.data.entity.home.lineorder.LineOrderListEntity;
import com.netmi.workerbusiness.data.entity.home.lineorder.LogisticEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/21
 * 修改备注：
 */
public interface OrderApi {
    /**
     * 线上订单-列表（返回数据和用户端订单列表一致）
     */
    @FormUrlEncoded
    @POST("/order/order-shop-api/main-order-list")
    Observable<BaseData<PageEntity<LineOrderListEntity>>> getLineOrderList(@Field("status") String status,
                                                                           @Field("keywords") String keywords,
                                                                           @Field("start_page") int start_page,
                                                                           @Field("pages") int pages);

    /**
     * 订单详情(和用户端订单详情数据一致)
     */
    @FormUrlEncoded
    @POST("/order/order-shop-api/main-order-info")
    Observable<BaseData<LineOrderDetailEntity>> getLineOrderDetail(@Field("main_order_id") int main_order_id);

    /**
     * 物流数据获取
     */
    @FormUrlEncoded
    @POST("order/order-api/logistics")
    Observable<BaseData<LogisticEntity>> getLogistic(@Field("order_no") String order_no,
                                                     @Field("refund_no") String refund_no,
                                                     @Field("code") String code,
                                                     @Field("company_code") String company_code);

    /**
     * 物流数据获取
     */
    @FormUrlEncoded
    @POST("order/order-api/logistics-list")
    Observable<BaseData<List<com.liemi.basemall.data.entity.order.LogisticEntity>>> getLogistic(@Field("order_no") String order_no);

    /**
     * 线上订单-评价列表
     */
    @FormUrlEncoded
    @POST("order/order-shop-api/order-commet-list")
    Observable<BaseData<List<OrderEvaluationEntity>>> order_comment_list(@Field("order_id") String order_id);


    /**
     * 线上回复评价
     */
    @FormUrlEncoded
    @POST("order/order-shop-api/receive")
    Observable<BaseData> comment(@Field("commet_id") String commet_id,
                                 @Field("content") String content);


}
