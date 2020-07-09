package com.liemi.basemall.data.api;

import com.liemi.basemall.data.entity.ImgCodeEntity;
import com.netmi.baselibrary.data.entity.BaseData;



import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HomeApi {


    /**
     * 获取图文验证码
     * 请求类型 register：注册，
     * login：登陆，
     * resetPassword：重置密码，
     * changePassword：修改密码，
     * changePayPassword：修改支付密码，
     * resetPayPassword：找回支付密码，
     * forward：转账，pickUp：提取，OTC：OTC交易
     */
    @FormUrlEncoded
    @POST("/member/user-api/get-img-code")
    Observable<BaseData<ImgCodeEntity>> getImgCode(@Field("type") String type);



    /*
     * 更新个人信息
     * */
    @FormUrlEncoded
    @POST("member/user-api/update")
    Observable<BaseData> doUpdateUserInfo(@Field("nickname") String nickname,
                                          @Field("head_url") String head_url/*,
                                          @Field("sex") String sex,
                                          @Field("date_birth") String date_birth*/);


    /*
     * 实名认证
     * */
    @FormUrlEncoded
    @POST("yms/yms-realname-authentication-api/create")
    Observable<BaseData> verified(@Field("real_name") String real_name,
                                  @Field("id_card") String id_card,
                                  @Field("phone") String phone,
                                  @Field("alipay") String alipay,
                                  @Field("alipay_qrcode") String alipay_qrcode,
                                  @Field("wechat") String wechat,
                                  @Field("wechat_qrcode") String wechat_qrcode,
                                  @Field("bank") String bank,
                                  @Field("bank_card_number") String bank_card_number);


    /**
     * 店铺入驻申请(实体店)
     */

    @FormUrlEncoded
    @POST("shop/shop-apply-record-api/create")
    Observable<BaseData> getCreateStore(@Field("username") String username,
                                        @Field("password") String password,
                                        @Field("name") String name,
                                        @Field("type") int type,
                                        @Field("logo_url") String logo_url,
                                        @Field("license_url") String license_url,
                                        @Field("business_permits") String business_permits,
                                        @Field("p_id") int p_id,
                                        @Field("c_id") int c_id,
                                        @Field("d_id") int d_id,
                                        @Field("address") String address);

    /**
     * 店铺入驻申请(非实体店)
     */

    @FormUrlEncoded
    @POST("shop/shop-apply-record-api/create")
    Observable<BaseData> getCreateStore(@Field("username") String username,
                                        @Field("password") String password,
                                        @Field("name") String name,
                                        @Field("type") int type,
                                        @Field("logo_url") String logo_url,
                                        @Field("p_id") int p_id,
                                        @Field("c_id") int c_id,
                                        @Field("d_id") int d_id,
                                        @Field("address") String address);

    /**
     * 申请信用卡还款
     */
    @FormUrlEncoded
    @POST("credit/user-epayment-apply-api/create")
    Observable<BaseData> getRepaymentCreate(@Field("name") String name,
                                            @Field("credit_card") String credit_card,
                                            @Field("bank_name") String bank_name,
                                            @Field("phone") String phone,
                                            @Field("price") String price,
                                            @Field("amount") String amount,
                                            @Field("password") String password,
                                            @Field("code") String code,
                                            @Field("poundage") String poundage);


    /**
     * 删除信用卡
     */
    @FormUrlEncoded
    @POST("credit/user-credit-card-api/delete")
    Observable<BaseData> getDeletCredit(@Field("card_id") String card_id);

    /**
     * 添加信用卡
     */
    @FormUrlEncoded
    @POST("credit/user-credit-card-api/create")
    Observable<BaseData> getAddCredit(@Field("name") String name,
                                      @Field("credit_card") String credit_card,
                                      @Field("bank_name") String bank_name,
                                      @Field("phone") String phone);

    /**
     * 修改信用卡信息
     */
    @FormUrlEncoded
    @POST("credit/user-credit-card-api/update")
    Observable<BaseData>getUpdateCredit(@Field("name") String name,
                                        @Field("credit_card") String credit_card,
                                        @Field("bank_name") String bank_name,
                                        @Field("phone") String phone,
                                        @Field("card_id") String card_id);



}
