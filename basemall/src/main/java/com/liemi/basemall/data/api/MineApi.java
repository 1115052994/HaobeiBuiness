package com.liemi.basemall.data.api;

import com.liemi.basemall.data.entity.AddressEntity;
import com.liemi.basemall.data.entity.AliPayData;
import com.liemi.basemall.data.entity.OrderCountEntity;
import com.liemi.basemall.data.entity.UserNoticeEntity;
import com.liemi.basemall.data.entity.WXPayData;
import com.liemi.basemall.data.entity.good.ShareImgEntity;
import com.liemi.basemall.data.entity.user.HandlingChargeEntity;
import com.liemi.basemall.data.entity.user.HelperServiceEntity;
import com.liemi.basemall.data.entity.user.MineCollectionGoodsEntity;
import com.liemi.basemall.data.entity.user.MineCollectionStoreEntity;
import com.liemi.basemall.data.entity.user.PublicWalletEntity;
import com.liemi.basemall.data.entity.user.WalletDetailsEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.data.entity.UserInfoEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/8 16:19
 * 修改备注：
 */
public interface MineApi {


    /**
     * 用户反馈
     */
    @FormUrlEncoded
    @POST("usermember/user-data-api/suggestion")
    Observable<BaseData> feedBack(@Field("remark") String remark,
                                  @Field("tel") String tel,
                                  @Field("imgs[]") List<String> imgs);




    /**
     * 修改个人资料
     */
    @FormUrlEncoded
    @POST("usermember/user-data-api/update-info")
    Observable<BaseData> updateUserInfo(@Field("head_url") String head_url,
                                        @Field("nickname") String nickname,
                                        @Field("sex") int sex,
                                        @Field("birthday") String birthday);


    /**
     * 修改支付密码
     *
     * @param old_pass
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/change-pay-password")
    Observable<BaseData> doChangePayPassword(@Field("old_pass") String old_pass,
                                             @Field("password") String password);

    /**
     * 修改登录密码
     *
     * @param old_pass
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/change-password")
    Observable<BaseData> doChangeLoginPassword(@Field("old_pass") String old_pass,
                                               @Field("password") String password);

    /**
     * 忘记支付密码
     *
     * @param phone
     * @param code
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/reset-pay-password")
    Observable<BaseData> doForgetPayPassword(@Field("phone") String phone,
                                             @Field("code") String code,
                                             @Field("password") String password);


    /**
     * 忘记登录密码
     *
     * @param phone
     * @param code
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/reset-password")
    Observable<BaseData> doForgetPassword(@Field("phone") String phone,
                                          @Field("code") String code,
                                          @Field("password") String password);

    /**
     //     * 我的优惠券列表
     //     */
//    @FormUrlEncoded
//    @POST("coupon/coupon-user-api/index")
//    Observable<BaseData<PageEntity<CouponEntity>>> listMyCoupon(@Field("start_page") int start_page,
//                                                                @Field("pages") int pages,
//                                                                @Field("type") int type);
//
//    /**
//     * 发放优惠券
//     */
//    @FormUrlEncoded
//    @POST("coupon/coupon-api/index")
//    Observable<BaseData<PageEntity<CouponEntity>>> getMyCoupon(@Field("position") String position);

    /**
     * 用户个人信息
     */
    @FormUrlEncoded
    @POST("/member/user-api/info")
    Observable<BaseData<UserInfoEntity>> getUserInfo(@Field("defaultParam") int param);

    /**
     * 修改设计师登录密码
     * old_pass	string
     * 之前的密码（md5加密）
     * <p>
     * password	string
     * 修改后的密码（md5加密）
     */
    @FormUrlEncoded
    @POST("usermember/user-api/reset-pass-old")
    Observable<BaseData> changePassword(@Field("old_pass") String oldPwd, @Field("password") String newPwd);

    /**
     * 订单支付 微信
     * pay_type     支付类型:1-全款支付2-首款支付3-尾款支付
     * pay_channel  支付渠道:0-微信1-支付宝
     */
    @FormUrlEncoded
    @POST("usermember/user-vip-api/buy-vip")
    Observable<BaseData<WXPayData>> payOrderWechat(@Field("pay_channel") int pay_channel);

    /**
     * 订单支付 支付宝支付
     * pay_type     支付类型:1-全款支付2-首款支付3-尾款支付
     * pay_channel  支付渠道:0-微信1-支付宝
     */
    @FormUrlEncoded
    @POST("usermember/user-vip-api/buy-vip")
    Observable<BaseData<AliPayData>> payOrderAli(@Field("pay_channel") int pay_channel);


//    /**
//     * 用户VIP信息
//     */
//    @FormUrlEncoded
//    @POST("usermember/user-vip-api/detail")
//    Observable<BaseData<UserVipInfoEntity>> getUserVipInfo(@Field("param") String param);

    /**
     * 收藏的商品列表
     *
     * @param start_page
     * @param pages
     * @return
     */
    @FormUrlEncoded
    @POST("/item/me-item-collection-api/index")
    Observable<BaseData<PageEntity<MineCollectionGoodsEntity>>> doMineCollectionGoods(@Field("start_page") int start_page,
                                                                                      @Field("pages") int pages);

    /**
     * 删除商品收藏
     *
     * @param item_ids 收藏的商品id
     * @return
     */
    @FormUrlEncoded
    @POST("/item/me-item-collection-api/del-collection")
    Observable<BaseData<BaseData>> doDeleteCollectionGoods(@Field("item_id[]") String item_ids);

    /**
     * 取消收藏店铺
     *
     * @param shop_ids 店铺id列表
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/shop-api/delete-collection")
    Observable<BaseData> doDeleteCollectionStore(@Field("shop_id") String shop_ids);

    /**
     * 收藏的店铺列表
     *
     * @param start_page
     * @param pages
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/shop-collection-api/index")
    Observable<BaseData<PageEntity<MineCollectionStoreEntity>>> doMineCollectionStore(@Field("start_page") int start_page,
                                                                                      @Field("pages") int pages);

    /**
     * 收货地址列表
     *
     * @param start_page 起始页
     * @param pages      每页树木
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/me-address-api/index")
    Observable<BaseData<PageEntity<AddressEntity>>> doAddressList(@Field("start_page") int start_page,
                                                                  @Field("pages") int pages);

    /**
     * 新增收货地址
     *
     * @param name    收货人姓名
     * @param p_id    省id
     * @param c_id    市id
     * @param d_id    区id
     * @param tel     联系电话
     * @param address 详细地址
     * @param is_top  是否设置默认地址 0：否，1：是
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/me-address-api/create")
    Observable<BaseData<AddressEntity>> doAddNewAddress(@Field("name") String name,
                                                        @Field("p_id") String p_id,
                                                        @Field("c_id") String c_id,
                                                        @Field("d_id") String d_id,
                                                        @Field("tel") String tel,
                                                        @Field("address") String address,
                                                        @Field("is_top") int is_top);

    /**
     * 修改收货地址
     *
     * @param name
     * @param p_id
     * @param c_id
     * @param d_id
     * @param tel
     * @param address
     * @param is_top
     * @param maid      地址id
     * @param post_code 邮编
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/me-address-api/update")
    Observable<BaseData> doUpdateAddress(@Field("maid") String maid,
                                         @Field("name") String name,
                                         @Field("p_id") String p_id,
                                         @Field("c_id") String c_id,
                                         @Field("d_id") String d_id,
                                         @Field("tel") String tel,
                                         @Field("address") String address,
                                         @Field("is_top") int is_top,
                                         @Field("post_code") String post_code);

    /**
     * 删除收货地址
     *
     * @param maid 地址id
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/me-address-api/delete")
    Observable<BaseData> doDeleteAddress(@Field("maid") int maid);


    //share image
    @FormUrlEncoded
    @POST("item/me-item-api/merger-img")
    Observable<BaseData<ShareImgEntity>> getShareImg(@Field("item_id") String item_id);

    /**
     * 用户公告信息
     *
     * @param type_arr   需要获取的公告类型
     * @param key_word   关键字
     * @param start_page 起始页
     * @param pages      每一页的条数
     * @return
     */
    @FormUrlEncoded
    @POST("/notice/notice-api/index")
    Observable<BaseData<UserNoticeEntity>> doUserNotices(@Field("type_arr[]") String[] type_arr,
                                                         @Field("key_word") String key_word,
                                                         @Field("start_page") int start_page,
                                                         @Field("pages") int pages);


    /**
     * 设置公告已读
     *
     * @param notice_id 公告id
     * @return
     */
    @FormUrlEncoded
    @POST("/notice/notice-api/set-read")
    Observable<BaseData> doSetNoticeRead(@Field("notice_id") int notice_id);

    /**
     * 更新用户信息
     *
     * @param head_url   头像
     * @param nickname   昵称
     * @param sex        性别
     * @param date_birth 生日
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/update")
    Observable<BaseData> doUserInfoUpdate(@Field("head_url") String head_url,
                                          @Field("nickname") String nickname,
                                          @Field("sex") String sex,
                                          @Field("date_birth") String date_birth);

    /**
     * 删除收货地址
     *
     * @param maid 地址id
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/me-address-api/delete")
    Observable<BaseData> doDeleteAddress(@Field("maid") String maid);

    /**
     * 意见反馈接口
     *
     * @param tel
     * @param remark
     * @param imgs
     * @return
     */
    @FormUrlEncoded
    @POST("/feedback/feedback-api/create")
    Observable<BaseData> doSuggestionBack(@Field("tel") String tel,
                                          @Field("remark") String remark,
                                          @Field("imgs[]") List<String> imgs);

    /**
     * 取消收藏店铺
     *
     * @param shop_id 要取消收藏的id
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/shop-api/delete-collection")
    Observable<BaseData> doUnCollectionStore(@Field("shop_id[]") String[] shop_id);

    /**
     * 清空收藏的店铺
     *
     * @param defaultData
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/shop-api/delete-all-collection")
    Observable<BaseData> doClearCollectionStore(@Field("defaultDta") String defaultData);

    /**
     * 删除收藏的商品
     *
     * @param item_id
     * @return
     */
    @FormUrlEncoded
    @POST("/item/me-item-collection-api/del-collection")
    Observable<BaseData> doUnCollectionGood(@Field("item_id[]") String[] item_id);

    /**
     * 清空收藏列表
     *
     * @param defaultData
     * @return
     */
    @FormUrlEncoded
    @POST("/item/me-item-collection-api/del-all-collection")
    Observable<BaseData> doClearCollectionGoods(@Field("defaultData") String defaultData);

    /**
     * 设置全部已读
     *
     * @param defaultData
     * @return
     */
    @FormUrlEncoded
    @POST("/notice/notice-api/set-read-all")
    Observable<BaseData> doNoticeAllRead(@Field("defaultData") String defaultData);

    /**
     * 订单数量统计
     */
    @FormUrlEncoded
    @POST("order/order-api/get-count")
    Observable<BaseData<OrderCountEntity>> getOrderCount(@Field("param") int param);

    @FormUrlEncoded
    @POST("/wallet/wallet-eth-api/log-list")
    Observable<BaseData<PageEntity<WalletDetailsEntity>>> doWalletDetails(@Field("type") String type,
                                                                          @Field("start_page") int start_page,
                                                                          @Field("pages") int pages);

    @FormUrlEncoded
    @POST("/wallet/wallet-eth-api/info")
    Observable<BaseData<PublicWalletEntity>> doPublicWalleInfo(@Field("defaultData") String defaultData);

    /**
     * 请求手续费信息
     *
     * @param address
     * @param amount
     * @return
     */
    @FormUrlEncoded
    @POST("/wallet/wallet-eth-api/query-fee")
    Observable<BaseData<HandlingChargeEntity>> doHandlingCharge(@Field("address") String address,
                                                                @Field("amount") String amount);

    /**
     * 提取以太币
     *
     * @param address
     * @param amount
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/wallet/wallet-eth-api/transfer-eth/")
    Observable<BaseData> doTakeOut(@Field("address") String address,
                                   @Field("amount") String amount,
                                   @Field("password") String password);

    @FormUrlEncoded
    @POST("/shop/shop-api/tel-list")
    Observable<BaseData<PageEntity<HelperServiceEntity>>> doHelpeService(@Field("start_page") int start_page,
                                                                         @Field("pages") int pages);

}
