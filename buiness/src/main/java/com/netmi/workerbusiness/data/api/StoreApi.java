package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.workerbusiness.data.entity.home.category.GoodsOneCateEntity;
import com.netmi.workerbusiness.data.entity.home.linecommodity.createcommofity.CreateGoodCommand;
import com.netmi.workerbusiness.data.entity.home.lineorder.LogisticEntity;
import com.netmi.workerbusiness.data.entity.home.lineorder.LogisticsCommand;
import com.netmi.workerbusiness.data.entity.home.lineorder.LogisticsEntity;
import com.netmi.workerbusiness.data.entity.home.lineorder.StoreExpressEntity;
import com.netmi.workerbusiness.data.entity.home.postage.ServiceDesEntity;
import com.netmi.workerbusiness.data.entity.home.store.CateOneEntity;
import com.netmi.workerbusiness.data.entity.home.store.CateTwoEntity;
import com.netmi.workerbusiness.data.entity.home.store.StoreCateEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/19
 * 修改备注：
 */
public interface StoreApi {

    /**
     * 获取店铺规格列表
     */
    @FormUrlEncoded
    @POST("item/me-prop-api/shop-index")
    Observable<BaseData<PageEntity<StoreCateEntity>>> storeCate(@Field("param") String param);

    /**
     * 一级规格新增
     */
    @FormUrlEncoded
    @POST("/item/me-prop-api/create")
    Observable<BaseData<CateOneEntity>> createCateOne(@Field("prop_name") String prop_name,
                                                      @Field("prop_code") String prop_code);

    /**
     * 二级规格新增
     */
    @FormUrlEncoded
    @POST("/item/me-prop-value-api/create")
    Observable<BaseData<CateTwoEntity>> createCateTwo(@Field("prop_id") String prop_id,
                                                      @Field("value_name") String value_name,
                                                      @Field("value_code") String value_code,
                                                      @Field("img_url") String img_url,
                                                      @Field("is_img") String is_img);

    /**
     * 一级规格批量删除
     */
    @FormUrlEncoded
    @POST("/item/me-prop-api/delete")
    Observable<BaseData> deleteOne(@Field("prop_ids[]") List<String> prop_ids);

    /**
     * 二级规格批量删除
     */
    @FormUrlEncoded
    @POST("/item/me-prop-value-api/delete")
    Observable<BaseData> deleteTwo(@Field("value_ids[]") List<String> value_ids);


    /**
     * 修改店铺类目
     */
    @FormUrlEncoded
    @POST("shop/shop-api/update-shop-category")
    Observable<BaseData> getStoreCategoryUpdate(@Field("cid") String cid);


    /**
     * 创建商品接口
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("item/shop-me-item-api/create")
    Observable<BaseData> getCreateGood(@Body CreateGoodCommand createGoodCommand);


    /**
     * 编辑保存商品
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("item/shop-me-item-api/update")
    Observable<BaseData> getUpdateGood(@Body CreateGoodCommand createGoodCommand);

    /**
     * 删除商品
     */
    @FormUrlEncoded
    @POST("item/shop-me-item-api/delete")
    Observable<BaseData> deleteGood(@Field("id") int id);

    /**
     * 批量上架
     * ids	string	商品ID 如果为单个的话 直接传一个值 如果为多个传一个字符串
     */
    @FormUrlEncoded
    @POST("item/shop-me-item-api/batch-shelf")
    Observable<BaseData> onShelf(@Field("ids") String ids);

    /**
     * 批量下架
     */
    @FormUrlEncoded
    @POST("item/shop-me-item-api/batch-unshelf")
    Observable<BaseData> downShelf(@Field("ids") String ids);

    /**
     * 获取商品详情
     */
    @FormUrlEncoded
    @POST("item/shop-me-item-api/view")
    Observable<BaseData<CreateGoodCommand>> getGoodDetail(@Field("item_id") String item_id);


    /**
     * 商品分类列表获取
     */
    @FormUrlEncoded
    @POST("item/me-category-api/get-category")
    Observable<BaseData<List<GoodsOneCateEntity>>> listCategory(@Field("start_page") int start_page,
                                                                @Field("pages") int pages,
                                                                @Field("level") String level,
                                                                @Field("pid") String pid,
                                                                @Field("is_home") String is_home,
                                                                @Field("shop_id") String shop_id);

    /**
     * 获取服务描述
     */
    @FormUrlEncoded
    @POST("item/me-item-nature-api/index")
    Observable<BaseData<PageEntity<ServiceDesEntity>>> serviceDes(@Field("pram") String pram);

    /**
     * 快递公司列表接口
     */
    @FormUrlEncoded
    @POST("/base/intel-api/commpany")
    Observable<BaseData<List<StoreExpressEntity>>> listExpress(@Field("param") String param);

    /**
     * 线上订单-立即发货-发货提交
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/order/order-shop-api/send-out")
    Observable<BaseData> sendOut(@Body LogisticsCommand LogisticsCommand);


}
