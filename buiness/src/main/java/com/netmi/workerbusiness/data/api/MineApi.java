package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageBonusEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.workerbusiness.data.entity.VIPShareImgEntity;
import com.netmi.workerbusiness.data.entity.haibei.HaiBeiConfidenceEntity;
import com.netmi.workerbusiness.data.entity.haibei.HaibeiBonus;
import com.netmi.workerbusiness.data.entity.mine.BankListEntity;
import com.netmi.workerbusiness.data.entity.mine.BountyEntity;
import com.netmi.workerbusiness.data.entity.mine.ChooseBankEntity;
import com.netmi.workerbusiness.data.entity.mine.ContentEntity;
import com.netmi.workerbusiness.data.entity.mine.GetApplyInfo;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.data.entity.mine.ShopPayRecordEntity;
import com.netmi.workerbusiness.data.entity.mine.ShopScoreEntity;
import com.netmi.workerbusiness.data.entity.mine.TransactionEntity;
import com.netmi.workerbusiness.data.entity.mine.WalletEntity;
import com.netmi.workerbusiness.data.entity.mine.WithdrawMessEntity;
import com.netmi.workerbusiness.data.entity.walllet.AliWechatEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/8/29
 * 修改备注：
 */
public interface MineApi {

    /**
     * 获取商家信息
     */
    @FormUrlEncoded
    @POST("shop/shop-api/view")
    Observable<BaseData<ShopInfoEntity>> shopInfo(@Field("parm") String parm);

    /**
     * 兑换二维码
     */
    @FormUrlEncoded
    @POST("haibei/haibei-api/haibei-qr-code")
    Observable<BaseData<HaibeiBonus>> haibeiQrCode(@Field("parm") String parm);

    /**
     * 获取商家信息
     */
    @FormUrlEncoded
    @POST("/shop/shop-apply-record-api/get-apply-info")
    Observable<BaseData<GetApplyInfo>> getApplyInfo(@Field("parm") String parm);



    /**
     * 商家缴费详情
     */
    @FormUrlEncoded
    @POST("shop/shop-pay-record-api/view")
    Observable<BaseData<ShopPayRecordEntity>> shopPayRecord(@Field("parm") String parm);

    /**
     * 商家缴费提交
     */
    @FormUrlEncoded
    @POST("shop/shop-pay-record-api/create")
    Observable<BaseData> commitShopPay(@Field("service_prove") String service_prove,
                                       @Field("deposit_prove") String deposit_prove,
                                       @Field("service_account") String service_account,
                                       @Field("deposit_account") String deposit_account);

    /**
     * 商家重新缴费提交
     */
    @FormUrlEncoded
    @POST("shop/shop-pay-record-api/update")
    Observable<BaseData> updateShopPay(@Field("service_prove") String service_prove,
                                       @Field("deposit_prove") String deposit_prove);


    /**
     * 修改商家信息
     */
    @FormUrlEncoded
    @POST("shop/shop-api/update")
    Observable<BaseData> doUpdateShopInfo(@Field("id") int id,
                                          @Field("logo_url") String logo_url,
                                          @Field("name") String name,
                                          @Field("remark") String remark,
                                          @Field("opening_hours") String opening_hours,
                                          @Field("longitude") String longitude,
                                          @Field("latitude") String latitude,
                                          @Field("p_name") String p_name,
                                          @Field("c_name") String c_name,
                                          @Field("d_name") String d_name,
                                          @Field("address") String address,
                                          @Field("img_url") String img_url,
                                          @Field("p_id") String p_id,
                                          @Field("c_id") String c_id,
                                          @Field("d_id") String d_id);

    /**
     * 我的余额
     */
    @FormUrlEncoded
    @POST("adminUser/user-api/banlance")
    Observable<BaseData<WalletEntity>> getWallet(@Field("parm") String parm);


    /**
     * 我的银行卡列表
     */
    @FormUrlEncoded
    @POST("shop/shop-account-api/index")
    Observable<BaseData<List<BankListEntity>>> getBankList(@Field("parm") String parm);

    /**
     * 添加银行卡
     */
    @FormUrlEncoded
    @POST("shop/shop-account-api/create")
    Observable<BaseData> addBankCard(@Field("subbranch_name") String subbranch_name,
                                     @Field("bank_card") String bank_card,
                                     @Field("bank_name") String bank_name,
                                     @Field("name") String name);

    /**
     * 交易明细-交易列表
     */
    @FormUrlEncoded
    @POST("shop/shop-income-log-api/index")
    Observable<BaseData<PageEntity<TransactionEntity>>> transactionDetails(@Field("time") String time,
                                                                           @Field("start_page") int start_page,
                                                                           @Field("pages") int pages);
    /**
     * 奖励金明细
     */
    @FormUrlEncoded
    @POST("active/bonus-api/get-bonus-list")
    Observable<BaseData<PageBonusEntity<BountyEntity>>> getBounsList(@Field("time") String time,
                                                                @Field("start_page") int start_page,
                                                                @Field("pages") int pages);
    /**
     * 奖励金额度明细
     */
    @FormUrlEncoded
    @POST("active/bonus-api/get-bonus-quota-log-list")
    Observable<BaseData<PageBonusEntity<BountyEntity>>> getBounsQuotaList(@Field("time") String time,
                                                                          @Field("start_page") int start_page,
                                                                          @Field("pages") int pages);


    /**
     * /**
     * 银行列表
     */
    @FormUrlEncoded
    @POST("member/withdraw-api/bank-index")
    Observable<BaseData<PageEntity<ChooseBankEntity>>> chooseBank(@Field("start_page") int start_page,
                                                                  @Field("pages") int pages);

    /**
     * 解绑银行卡
     */
    @FormUrlEncoded
    @POST("shop/shop-account-api/delete")
    Observable<BaseData> delete_bank(@Field("id") String id,
                                     @Field("tel") String tel,
                                     @Field("code") String code);

    /**
     * 提现-提示信息
     */
    @FormUrlEncoded
    @POST("member/withdraw-api/shop-setting")
    Observable<BaseData<WithdrawMessEntity>> withdrawMess(@Field("pram") String pram);


    /**
     * 提现手续费
     */
    @FormUrlEncoded
    @POST("/cash/shop-cash-api/cash")
    Observable<BaseData> getFee(@Field("pram") String pram);


    /**
     * 提现-申请提现(银行卡)
     */
    @FormUrlEncoded
    @POST("cash/shop-cash-api/create")
    Observable<BaseData> withdraw(@Field("id") String id,
                                  @Field("amount") String amount,
                                  @Field("phone") String phone,
                                  @Field("code") String code);


    /**
     * 提现-申请提现(微信和支付宝)
     */
    @FormUrlEncoded
    @POST("advance/advance-api/create")
    Observable<BaseData> withdrawAliWechat(@Field("shop_id") String shop_id,
                                           @Field("price") String price,
                                           @Field("type") String type,
                                           @Field("phone") String phone,
                                           @Field("name") String name);


    /**
     * 提交支付宝微信账号信息
     */
    @FormUrlEncoded
    @POST("contact/contact-api/add")
    Observable<BaseData> addAliInfo(@Field("type") String type,
                                    @Field("name") String phone,
                                    @Field("phone") String name);

    /**
     * 获取支付宝微信账号信息
     */
    @FormUrlEncoded
    @POST("contact/contact-api/view")
    Observable<BaseData<List<AliWechatEntity>>> getAliInfo(@Field("param") String param);

    /**
     * 获取支付宝微信账号信息
     */
    @FormUrlEncoded
    @POST("contact/contact-api/get-list")
    Observable<BaseData<List<AliWechatEntity>>> getAliInfoList(@Field("type") String type);

    /**
     * 编辑支付宝微信账号信息
     */
    @FormUrlEncoded
    @POST("contact/contact-api/edit-info")
    Observable<BaseData<String>> setEditInfo(@Field("id") String id,
                                             @Field("name") String name,
                                             @Field("phone") String phone);

    /**
     * 删除支付宝微信账号信息
     */
    @FormUrlEncoded
    @POST("contact/contact-api/delete-info")
    Observable<BaseData<String>> deleteInfo(@Field("id") String id);



    /**
     * 海贝转化现金
     */
    @FormUrlEncoded
    @POST("wallet/index/conversion")
    Observable<BaseData> haibeiChange(@Field("amount") String amount,
                                      @Field("shop_id") String shop_id);


    /**
     * 商户信用分
     */
    @FormUrlEncoded
    @POST("shop/shop-score-api/index")
    Observable<BaseData<List<ShopScoreEntity>>> shopScore(@Field("pram") String pram);

    /**
     * 商家平台协议
     */
    @FormUrlEncoded
    @POST("content/content-api/view")
    Observable<BaseData<ContentEntity>> content(@Field("type") int type);


    /**
     * 商家端平台规则协议列表获取
     */
    @FormUrlEncoded
    @POST("/content/content-api/index")
    Observable<BaseData<PageEntity<ContentEntity>>> contentNew(@Field("pram") String pram);


    /**
     * 设置支付密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/set-pay-password")
    Observable<BaseData> setPayPass(@Field("password") String password);


    /**
     * 忘记登录密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/reset-password")
    Observable<BaseData> forgetLogin(@Field("password") String password,
                                     @Field("code") String code,
                                     @Field("phone") String phone);

    /**
     * 忘记交易密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/reset-pay-password")
    Observable<BaseData> forgetTransfer(@Field("phone") String phone,
                                        @Field("code") String code,
                                        @Field("password") String password);

    /**
     * 修改登录密码
     */
    @FormUrlEncoded
    @POST("adminUser/index-api/update")
    Observable<BaseData> changeLogin(@Field("password") String password,
                                     @Field("old_password") String old_password);

    /**
     * 修改交易密码
     */
    @FormUrlEncoded
    @POST("adminUser/index-api/update-pay")
    Observable<BaseData> changeTransfer(@Field("password") String password,
                                        @Field("old_password") String old_password);


    /**
     * 修改密码
     */
    @FormUrlEncoded
    @POST("adminUser/index-api/update")
    Observable<BaseData> changePassword(@Field("password") String password);


    /**
     * 意见反馈
     */
    @FormUrlEncoded
    @POST("feedback/feedback-api/create")
    Observable<BaseData> feedback(@Field("tel") String tel,
                                  @Field("remark") String remark,
                                  @Field("name") String name,
                                  @Field("imgs[]") List<String> imgs);

    /**
     * 邀请好友海报接口
     */
    @FormUrlEncoded
    @POST("member/user-api/inviting-posters")
    Observable<BaseData<VIPShareImgEntity>> VIPInviteFriend(@Field("param") String param);

    /**
     * 获取店铺分享海报
     */
    @FormUrlEncoded
    @POST("member/user-api/get-vip-share-poster")
    Observable<BaseData<VIPShareImgEntity>> getStoreSharePoster(@Field("param") String param);

    /**
     * 个人页面是否弹窗
     */
    @FormUrlEncoded
    @POST("shop/shop-api/tan")
    Observable<BaseData> commit(@Field("shop_id") String shop_id);


    /**
     * 我的海贝信心指数
     */
    @FormUrlEncoded
    @POST("order/order-api/hai")
    Observable<BaseData<HaiBeiConfidenceEntity>> getMinHai(@Field("type") int type);

}
