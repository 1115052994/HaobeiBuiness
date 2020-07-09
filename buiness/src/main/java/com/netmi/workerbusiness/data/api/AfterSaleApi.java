package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.workerbusiness.data.entity.home.AfterSaleDataEntity;
import com.netmi.workerbusiness.data.entity.home.LineOrderDataEntity;
import com.netmi.workerbusiness.data.entity.home.OfflineOrderDataEntity;
import com.netmi.workerbusiness.data.entity.home.aftersale.AfterSaleDetailEntity;
import com.netmi.workerbusiness.data.entity.home.aftersale.AfterSaleEntity;
import com.netmi.workerbusiness.data.entity.home.linecommodity.LineCommodityListEntity;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/22
 * 修改备注：
 */
public interface AfterSaleApi {

    /**
     * 售后列表获取
     */
    @FormUrlEncoded
    @POST("/order/shop-refund-api/refund-list")
    Observable<BaseData<PageEntity<AfterSaleEntity>>> getAfterSaleList(@Field("type") String type,
                                                                       @Field("keywords") String keywords,
                                                                       @Field("start_page") int start_page,
                                                                       @Field("pages") int pages);

    /**
     * 售后管理-详情
     */
    @FormUrlEncoded
    @POST("/order/shop-refund-api/refund-details")
    Observable<BaseData<AfterSaleDetailEntity>> getAfterSaleDetail(@Field("refund_id") String refund_id);


    /**
     * 售后管理-退货第一次审核
     * refund_id	int	退款主键	Y
     * status_ayy	number	状态 1： 卖家已审核 2： 卖家拒绝退款	Y
     * refuse_remark	string	拒绝退款说明，拒绝时必填	N
     */
    @FormUrlEncoded
    @POST("/order/shop-refund-api/toexamine")
    Observable<BaseData> firstReview(@Field("refund_id") String refund_id,
                                     @Field("status_ayy") String status_ayy,
                                     @Field("refuse_remark") String refuse_remark);


    /**
     * 售后管理-拒绝退款
     * refund_id	int	退款主键	Y
     * refuse_remark	string	拒绝退款说明	Y
     */
    @FormUrlEncoded
    @POST("/order/shop-refund-api/refuse")
    Observable<BaseData> disagree(@Field("refund_id") String refund_id,
                                  @Field("refuse_remark") String refuse_remark);

    /**
     * 售后管理 同意退款
     * refund_id	int	退款主键	Y
     * price_total	number	退款金额
     */
    @FormUrlEncoded
    @POST("/order/shop-refund-api/refund")
    Observable<BaseData> agree(@Field("refund_id") String refund_id,
                               @Field("price_total") String price_total);


    /**
     * 售后角标
     */
    @FormUrlEncoded
    @POST("/order/shop-refund-api/get-count")
    Observable<BaseData<AfterSaleDataEntity>> getAfterSaleData(@Field("pram") String pram);

    /**
     * 线上订单角标
     */
    @FormUrlEncoded
    @POST("/order/order-shop-api/get-count")
    Observable<BaseData<LineOrderDataEntity>> getLineOrderData(@Field("pram") String pram);

    /**
     * 线下订单角标
     */
    @FormUrlEncoded
    @POST("/offline/shop-local-order-api/get-count")
    Observable<BaseData<OfflineOrderDataEntity>> getOfflineOrderData(@Field("pram") String pram);


}
