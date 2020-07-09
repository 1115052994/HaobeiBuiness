package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.workerbusiness.data.entity.home.linecommodity.createcommofity.CreateGoodCommand;
import com.netmi.workerbusiness.data.entity.home.offlinecommodity.OfflineGoodDetailEntity;
import com.netmi.workerbusiness.data.entity.home.offlineorder.OfflineOrderDetailEntity;
import com.netmi.workerbusiness.data.entity.home.offlineorder.OfflineOrderEvaluationEntity;
import com.netmi.workerbusiness.data.entity.home.offlineorder.OfflineOrderListEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OfflineGoodApi {

    /**
     * 创建商品接口
     */
    @FormUrlEncoded
    @POST("offline/offline-item-api/create")
    Observable<BaseData> getCreateGood(@Field("title") String title,
                                       @Field("img_url[]") List<String> img_url,
                                       @Field("price") String price,
                                       @Field("old_price") String old_price,
                                       @Field("stock") String stock,
                                       @Field("deal_num_false") String deal_num_false,
                                       @Field("sort") String sort,
                                       @Field("status") String status,
                                       @Field("rich_text") String rich_text,
                                       @Field("purchase_note") String purchase_note,
                                       @Field("start_date") String start_date,
                                       @Field("end_date") String end_date);

    /**
     * 商品详情
     */
    @FormUrlEncoded
    @POST("offline/offline-item-api/detail")
    Observable<BaseData<OfflineGoodDetailEntity>> getGoodDetail(@Field("item_id") String item_id);


    /**
     * 编辑保存商品
     */
    @FormUrlEncoded
    @POST("offline/offline-item-api/update")
    Observable<BaseData> getUpdateGood(@Field("item_id") String item_id,
                                       @Field("title") String title,
                                       @Field("img_url[]") List<String> img_url,
                                       @Field("price") String price,
                                       @Field("old_price") String old_price,
                                       @Field("stock") String stock,
                                       @Field("deal_num_false") String deal_num_false,
                                       @Field("sort") String sort,
                                       @Field("status") String status,
                                       @Field("rich_text") String rich_text,
                                       @Field("purchase_note") String purchase_note,
                                       @Field("start_date") String start_date,
                                       @Field("end_date") String end_date);


    /**
     * 删除商品
     */
    @FormUrlEncoded
    @POST("offline/offline-item-api/del")
    Observable<BaseData> deleteGood(@Field("item_id") int item_id);


    /**
     * 商品上下架
     */
    @FormUrlEncoded
    @POST("offline/offline-item-api/change-status")
    Observable<BaseData> shelf(@Field("item_id") int item_id);


    /**
     * 线下订单列表
     */
    @FormUrlEncoded
    @POST("offline/shop-local-order-api/list")
    Observable<BaseData<PageEntity<OfflineOrderListEntity>>> getList(@Field("order_no") String order_no,
                                                                     @Field("status") String status,
                                                                     @Field("start_page") int start_page,
                                                                     @Field("pages") int pages);

    /**
     * 线下订单详情
     */
    @FormUrlEncoded
    @POST("offline/local-order-api/info")
    Observable<BaseData<OfflineOrderDetailEntity>> getDetail(@Field("order_id") String order_id);


    /**
     * 线下订单核销
     */
    @FormUrlEncoded
    @POST("offline/local-order-api/used")
    Observable<BaseData> check(@Field("code") String code);


    /**
     * 线下订单 获取评论列表
     */
    @FormUrlEncoded
    @POST("offline/local-item-api/get-evaluate")
    Observable<BaseData<PageEntity<OfflineOrderEvaluationEntity>>> getEvaluation(@Field("order_id") String order_id,
                                                                                 @Field("start_page") String start_page,
                                                                                 @Field("pages") String pages,
                                                                                 @Field("flag") String flag);

    /**
     * 线下回复评论
     */
    @FormUrlEncoded
    @POST("offline/shop-local-order-api/reply")
    Observable<BaseData> comment(@Field("content") String content,
                                 @Field("comment_id") String comment_id);


    /**
     * 线下订单拒绝退款申请
     */
    @FormUrlEncoded
    @POST("item/me-voucher-refund-api/refuse")
    Observable<BaseData> refuse(@Field("order_id") String order_id,
                                @Field("reason_remark") String reason_remark);


    /**
     * 线下订单同意退款申请
     */
    @FormUrlEncoded
    @POST("item/me-voucher-refund-api/refund")
    Observable<BaseData> agree(@Field("order_id") String order_id);


}
