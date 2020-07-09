package com.liemi.basemall.data.api;

import com.liemi.basemall.data.entity.AliPayData;
import com.liemi.basemall.data.entity.WXPayData;
import com.liemi.basemall.data.entity.category.GoodsOneCateEntity;
import com.liemi.basemall.data.entity.category.GoodsTwoCateEntity;
import com.liemi.basemall.data.entity.comment.CommentEntity;
import com.liemi.basemall.data.entity.floor.FloorEntity;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.liemi.basemall.data.entity.good.GoodsListEntity;
import com.liemi.basemall.data.entity.order.FillOrderEntity;
import com.liemi.basemall.data.entity.order.OrderIdEntity;
import com.liemi.basemall.data.entity.shopcar.AddShopCartEntity;
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
 * 创建时间：2018/8/15 15:39
 * 修改备注：
 */
public interface CategoryApi {


    /**
     * 商品分类列表获取
     */
    @FormUrlEncoded
    @POST("item/me-category-api/get-category")
    Observable<BaseData<List<GoodsOneCateEntity>>> listTotalCategory(@Field("param") String param);

    /**
     * 获取二级分类
     */
    @FormUrlEncoded
    @POST("item/me-category-api/get-category-list")
    Observable<BaseData<List<GoodsTwoCateEntity>>> getSecondCategory(@Field("shop_id") String shopId,
                                                                     @Field("pid") String pid);


    /**
     * 获取一二级分类
     */
    @FormUrlEncoded
    @POST("item/me-category-api/get-category")
    Observable<BaseData<List<GoodsOneCateEntity>>> getAllCategory(@Field("param") String param);

    /**
     * 商品列表获取
     * type     0-普通商品1-VIP商品2-拼团购商品3-秒杀商品4-尾货商品
     * sell_out 销量1：升序2：降序 默认2
     * pricesort 价格1：升序2：降序 默认1
     * screen_type 0：普通商品1：近期上新2：热卖推荐 默认0
     */
    @FormUrlEncoded
    @POST("shop/item-api/index")
    Observable<BaseData<PageEntity<GoodsListEntity>>> listGoods(@Field("start_page") int start_page,
                                                                @Field("pages") int pages,
                                                                @Field("type") String type,
                                                                @Field("category_id") String category_id,
                                                                @Field("sell_out") String sell_out,
                                                                @Field("pricesort") String pricesort,
                                                                @Field("screen_type") String screen_type);


    /**
     * 商品列表获取
     * mcid	是	int	分类主键
     *
     * @param sort_name 排序字段名称 is_new：新品排序,price：价格排序,deal_num：人气排序 不填为综合排序;
     * @param sort_type 排序规则 SORT_DESC：逆序（从大到小） SORT_ASC：正序（从小到大）
     * @param item_type 商品类型 默认0:普通商品 1:纯积分商品 2:现金+积分商品
     */
    @FormUrlEncoded
    @POST("item/me-item-api/index")
    Observable<BaseData<PageEntity<GoodsListEntity>>> listGoods(@Field("start_page") int start_page,
                                                                @Field("pages") int pages,
                                                                @Field("item_type") String item_type,
                                                                @Field("mcid") String mcid,
                                                                @Field("key_word") String key_word,
                                                                @Field("min_price") String min_price,
                                                                @Field("max_price") String max_price,
                                                                @Field("min_stock") String min_stock,
                                                                @Field("max_stock") String max_stock,
                                                                @Field("shop_id") String shop_id,
                                                                @Field("is_hot") String is_hot,
                                                                @Field("is_new") String is_new,
                                                                @Field("sort_name") String sort_name,
                                                                @Field("sort_type") String sort_type);

    /**
     * 商品列表获取
     * keywords 搜索关键字
     */
    @FormUrlEncoded
    @POST("shop/item-api/index")
    Observable<BaseData<PageEntity<GoodsListEntity>>> listGoods(@Field("start_page") int start_page,
                                                                @Field("pages") int pages,
                                                                @Field("keywords") String keywords);

    /**
     * 直播商品列表获取
     * keywords 搜索关键字
     */
    @FormUrlEncoded
    @POST("live/live-api/choose-list")
    Observable<BaseData<PageEntity<GoodsListEntity>>> listLiveGoods(@Field("start_page") int start_page,
                                                                    @Field("pages") int pages,
                                                                    @Field("category_id") String category_id,
                                                                    @Field("keywords") String keywords);


    /**
     * 商品详情
     */
    @FormUrlEncoded
    @POST("item/me-item-api/view")
    Observable<BaseData<GoodsDetailedEntity>> getGoodsDetailed(@Field("item_id") String goodId);

    /**
     * 收藏商品
     */
    @FormUrlEncoded
    @POST("item/me-item-collection-api/collection")
    Observable<BaseData> goodCollection(@Field("item_id") String goodId);

    /**
     * 删除商品收藏
     */
    @FormUrlEncoded
    @POST("item/me-item-collection-api/del-collection")
    Observable<BaseData> goodCollectionDel(@Field("item_id[]") String[] goodId);


    /**
     * 加入购物车
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("shop/cart-api/add")
    Observable<BaseData> addShopCart(@Body AddShopCartEntity shopCartEntity);

    /**
     * 删除购物车
     */
    @FormUrlEncoded
    @POST("shop/cart-api/batch-delete")
    Observable<BaseData> deleteShopCart(@Field("id[]") List<String> ids);

    /**
     * 购物车列表
     */
    @FormUrlEncoded
    @POST("shop/cart-api/index")
    Observable<BaseData<PageEntity<ShopCartEntity>>> listShopCart(@Field("start_page") int start_page,
                                                                  @Field("pages") int pages);

    /**
     * 编辑购物车商品
     */
    @FormUrlEncoded
    @POST("shop/cart-api/modify")
    Observable<BaseData> shopCartUpdate(@Field("id") String id,
                                        @Field("num") int num);

    /**
     * 立即购买，订单下单数据
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("order/order-api/order-data")
    Observable<BaseData<FillOrderEntity>> buyNow(@Body AddShopCartEntity shopCartEntity);

    /**
     * 创建订单
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("order/order-api/create")
    Observable<BaseData<OrderIdEntity>> createOrder(@Body FillOrderEntity orderEntity);


    /**
     * 订单支付 微信
     * pay_type     支付类型:1-全款支付2-首款支付3-尾款支付
     * pay_channel  支付渠道:0-微信1-支付宝
     */
    @FormUrlEncoded
    @POST("pay/pay-api/app-pay")
    Observable<BaseData<WXPayData>> payOrderWechat(@Field("pay_type") int pay_type,
                                                   @Field("order_id") String order_id,
                                                   @Field("pay_channel") int pay_channel);

    /**
     * 订单支付 支付宝支付
     * pay_type     支付类型:1-全款支付2-首款支付3-尾款支付
     * pay_channel  支付渠道:0-微信1-支付宝
     */
    @FormUrlEncoded
    @POST("pay/pay-api/app-pay")
    Observable<BaseData<AliPayData>> payOrderAli(@Field("pay_type") int pay_type,
                                                 @Field("order_id") String order_id,
                                                 @Field("pay_channel") int pay_channel);

    /**
     * 申请线下支付
     * pay_type     支付类型:1-全款支付2-首款支付
     */
    @FormUrlEncoded
    @POST("order/me-order-api/underline")
    Observable<BaseData<AliPayData>> payOffline(@Field("order_id") String order_id,
                                                @Field("pay_type") int pay_type);


    /**
     * 商品评论列表获取
     *
     * @param flag 是否有图 0：否 1：是
     */
    @FormUrlEncoded
    @POST("usermember/user-data-api/comment-list")
    Observable<BaseData<PageEntity<CommentEntity>>> listComment(@Field("start_page") int start_page,
                                                                @Field("pages") int pages,
                                                                @Field("item_id") String item_id,
                                                                @Field("flag") String flag);

    /**
     * 分类推荐下的楼层页面
     */
    @FormUrlEncoded
    @POST("floor/floor-block-api/index")
    Observable<BaseData<PageEntity<FloorEntity>>> getStoreFloor(@Field("start_page") int start_page,
                                                                @Field("pages") int pages,
                                                                @Field("block_id") String id);

    /**
     * 获取热门搜索信息
     */
    @FormUrlEncoded
    @POST("item/me-item-api/hot-label")
    Observable<BaseData<List<String>>> getHotSearchList(@Field("shop_id") String shopId);
}
