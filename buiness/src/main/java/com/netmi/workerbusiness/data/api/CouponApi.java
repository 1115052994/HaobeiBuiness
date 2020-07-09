package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.workerbusiness.data.entity.home.HaibeiConfidenceEntity;
import com.netmi.workerbusiness.data.entity.home.TeamEntity;
import com.netmi.workerbusiness.data.entity.home.coupon.CouponListEntity;

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
public interface CouponApi {
    /**
     * 优惠券列表
     */
    @FormUrlEncoded
    @POST("coupon/shop-coupon-templet-api/index")
    Observable<BaseData<List<CouponListEntity>>> getCouponList(@Field("pram") String pram);

    /**
     * 禁用
     */
    @FormUrlEncoded
    @POST("coupon/shop-coupon-templet-api/off")
    Observable<BaseData> stopCoupon(@Field("id") String id);

    /**
     * 启用
     */
    @FormUrlEncoded
    @POST("coupon/shop-coupon-templet-api/on")
    Observable<BaseData> startCoupon(@Field("id") String id);


    /**
     * 删除
     */
    @FormUrlEncoded
    @POST("coupon/shop-coupon-templet-api/delete")
    Observable<BaseData> deleteCoupon(@Field("id") String id);


    /**
     * 新增
     */
    @FormUrlEncoded
    @POST("coupon/shop-coupon-templet-api/create")
    Observable<BaseData> addCoupon(@Field("total_num") String total_num,
                                   @Field("create_time") String create_time,
                                   @Field("update_time") String update_time,
                                   @Field("sw") int sw,
                                   @Field("remark") String remark,
                                   @Field("condition_num") int condition_num,
                                   @Field("discount_num") int discount_num);

    /**
     * 团队管理
     * fans_uid 二级用户列表传一级用户的uid
     * type 1是个人 2是店铺
     */
    @FormUrlEncoded
    @POST("/hand/fans-api/fans")
    Observable<BaseData<PageEntity<TeamEntity>>> doTeam(@Field("start_page") int start_page,
                                                        @Field("pages") int pages,
                                                        @Field("fans_uid") String fans_uid,
                                                        @Field("type") String type);

    /**
     * 海贝信心指数
     */
    @FormUrlEncoded
    @POST("/order/order-api/hai")
    Observable<BaseData<PageEntity<HaibeiConfidenceEntity>>> doList(@Field("type") String type);


    /**
     * 商家贷款
     * "shop_name": "jjjjjj"//店铺名称
     * "shop_username": "jjjjjj"//商家账号
     * "hai_num": "jjjjjj"//申请海贝数量
     * "pledge_time": "1"/预计抵押时长（月）
     * "phone": "13015540623"//电话
     * "shop_id":1//店铺id
     */
    @FormUrlEncoded
    @POST("/loan/loan-api/create")
    Observable<BaseData> loan(@Field("shop_name") String shop_name,
                              @Field("shop_username") String shop_username,
                              @Field("hai_num") String hai_num,
                              @Field("pledge_time") String pledge_time,
                              @Field("phone") String phone,
                              @Field("shop_id") String shop_id);


}
