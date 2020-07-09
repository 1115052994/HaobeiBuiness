package com.liemi.basemall.data.api;

import com.liemi.basemall.data.entity.StoreEntity;
import com.liemi.basemall.data.entity.floor.FloorEntity;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.liemi.basemall.data.entity.good.GoodsListEntity;
import com.liemi.basemall.data.entity.good.SpecsEntity;
import com.liemi.basemall.data.entity.good.SpecsGroupEntity;
import com.liemi.basemall.data.entity.order.FillOrderEntity;
import com.liemi.basemall.data.entity.order.OrderPayEntity;
import com.liemi.basemall.data.entity.shopcar.ShopCartAdapterEntity;
import com.liemi.basemall.data.entity.shopcar.ShopCartEntity;
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
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 15:11
 * 修改备注：
 */
public interface StoreApi {




    //hot search
    @FormUrlEncoded
    @POST("item/me-item-api/hot-label")
    Observable<BaseData<List<String>>> getHotSearch(@Field("data") String data);


    /**
     * 店铺列表获取
     * is_recommend	是	int	是否为推荐店铺 0：否 1：是 不填则为全部
     */
    @FormUrlEncoded
    @POST("shop/shop-api/index")
    Observable<BaseData<PageEntity<StoreEntity>>> listStore(@Field("start_page") int start_page,
                                                            @Field("pages") int pages,
                                                            @Field("is_recommend") String is_recommend,
                                                            @Field("key_word") String key_word);

    /**
     * 收藏店铺
     */
    @FormUrlEncoded
    @POST("shop/shop-api/shop-collection")
    Observable<BaseData> shopCollection(@Field("shop_id") String shop_id);

    /**
     * 删除店铺
     */
    @FormUrlEncoded
    @POST("shop/shop-api/delete-collection")
    Observable<BaseData> shopCollectionDel(@Field("shop_id[]") List<String> shop_ids);

    /**
     * 收藏商品
     */
    @FormUrlEncoded
    @POST("item/me-item-collection-api/collection")
    Observable<BaseData> goodCollection(@Field("item_id") String shop_id);

    /**
     * 删除商品收藏
     */
    @FormUrlEncoded
    @POST("item/me-item-collection-api/del-collection")
    Observable<BaseData> goodCollectionDel(@Field("item_id[]") List<String> item_ids);

    /**
     * 收藏的店铺列表
     */
    @FormUrlEncoded
    @POST("shop/shop-collection-api/index")
    Observable<BaseData<PageEntity<StoreEntity>>> listCollectionStore(@Field("start_page") int start_page,
                                                                      @Field("pages") int pages);

    /**
     * 商品收藏列表
     */
    @FormUrlEncoded
    @POST("item/me-item-collection-api/index")
    Observable<BaseData<PageEntity<GoodsListEntity>>> listCollectionGood(@Field("start_page") int start_page,
                                                                         @Field("pages") int pages);

    /**
     * 添加购物车
     */
    @FormUrlEncoded
    @POST("item/me-cart-api/create")
    Observable<BaseData> shopCartAdd(@Field("item_id") String item_id,
                                     @Field("item_type") int item_type,
                                     @Field("num") int num);

    /**
     * 购物车列表
     */
    @FormUrlEncoded
    @POST("item/me-cart-api/index")
    Observable<BaseData<PageEntity<ShopCartAdapterEntity>>> listShopCart(@Field("start_page") int start_page,
                                                                         @Field("pages") int pages);

    /**
     * 删除购物车
     */
    @FormUrlEncoded
    @POST("item/me-cart-api/delete")
    Observable<BaseData> shopCartDel(@Field("cart_ids[]") List<String> cart_ids);

    /**
     * 更新购物车数量
     */
    @FormUrlEncoded
    @POST("item/me-cart-api/update")
    Observable<BaseData> shopCartUpdate(@Field("cart_id") String cart_id,
                                        @Field("num") String num);

    /**
     * 店铺详情获取
     */
    @FormUrlEncoded
    @POST("shop/shop-api/view")
    Observable<BaseData<StoreEntity>> getStoreDetail(@Field("shop_id") String id);

    /**
     * 店铺首页楼层
     */
    @FormUrlEncoded
    @POST("floor/index/index")
    Observable<BaseData<PageEntity<FloorEntity>>> getStoreFloor(@Field("start_page") int start_page,
                                                                @Field("pages") int pages,
                                                                @Field("type") int type,
                                                                @Field("shop_id") String id);


//    /**
//     * 创建订单
//     */
//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST("order/order-api/create")
//    Observable<BaseData<OrderPayEntity>> createOrder(@Body OrderCommand orderCommand);


    /**
     * 商品规格列表接口
     */
    @FormUrlEncoded
    @POST("item/me-item-api/get-property")
    Observable<BaseData<List<SpecsEntity>>> listGoodsSpecs(@Field("item_id") String item_id);

    /**
     * 获取商品全部规格组合
     */
    @FormUrlEncoded
    @POST("item/me-item-api/get-all-property")
    Observable<BaseData<List<SpecsGroupEntity>>> listSpecsGroup(@Field("item_id") String item_id);


    /**
     * 加入购物车
     */
    @FormUrlEncoded
    @POST("item/me-cart-api/create")
    Observable<BaseData> addShopCart(@Field("ivid") String ivid,
                                     @Field("num") String num);

    /**
     * 创建订单
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("order/order-api/create")
    Observable<BaseData<OrderPayEntity>> createOrder(@Body FillOrderEntity orderCommand);


}
