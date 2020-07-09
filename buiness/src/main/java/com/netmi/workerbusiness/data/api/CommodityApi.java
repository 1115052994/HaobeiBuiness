package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.workerbusiness.data.entity.home.FinancialListEntity;
import com.netmi.workerbusiness.data.entity.home.linecommodity.LineCommodityListEntity;
import com.netmi.workerbusiness.data.entity.home.offlinecommodity.OfflineCommodityListEntity;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/2
 * 修改备注：
 */
public interface CommodityApi {


    /**
     * 线上商品列表
     */
    @FormUrlEncoded
    @POST("item/shop-me-item-api/index")
    Observable<BaseData<PageEntity<LineCommodityListEntity>>> onlineCommodity(@Field("start_page") int start_page,
                                                                              @Field("pages") int pages,
                                                                              @Field("key_word") String key_word,
                                                                              @Field("status") int status);

    /**
     * 线下商品列表
     */

    @FormUrlEncoded
    @POST(" offline/offline-item-api/list")
    Observable<BaseData<PageEntity<OfflineCommodityListEntity>>> offlineCommodity(@Field("title") String title,
                                                                                  @Field("status") int status,
                                                                                  @Field("start_page") int start_page,
                                                                                  @Field("pages") int pages);

    /**
     * 财务报表
     */
    @FormUrlEncoded
    @POST("order/order-shop-api/statistics")
    Observable<BaseData<FinancialListEntity>> financialList(@Field("type") String type);


}
