package com.netmi.baselibrary.data.api;

import com.netmi.baselibrary.data.entity.AccessToken;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/4 15:05
 * 修改备注：
 */
public interface LoginApi {

    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST("member/user-api/login")
    Call<BaseData<UserInfoEntity>> callLogin(@Field("phone") String username,
                                             @Field("password") String password,
                                             @Field("scenario") String scenario);


    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST("member/user-api/login")
    Observable<BaseData<UserInfoEntity>> doLogin(@Field("username") String username,
                                                 @Field("password") String password);


    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST("adminUser/user-api/login")
    Observable<BaseData<UserInfoEntity>> doLoginCode(@Field("phone") String phone,
                                                     @Field("code") String code,
                                                     @Field("scenario") String scenario,
                                                     @Field("role") String role);


    /**
     * 协议相关API， type：
     * 1：登录注册协议,
     * 2：关于我们
     * 3:红贝说明
     * 4：蓝贝说明
     */
    @FormUrlEncoded
    @POST("content/content-api/view")
    Observable<BaseData<AgreementEntity>> getAgreement(@Field("type") int type);

    /**
     * 获取验证码
     * type:请求类型 验证码获取类型
     * register ： 注册验证码
     * login：登录验证码
     * reset：忘记密码验证码
     * setphone：更换手机号验证码
     * payPassword:设置支付密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/send-sms")
    Observable<BaseData> getSmsCode(@Field("phone") String phone,
                                    @Field("type") String type);


    /**
     * 获取验证码
     * type:请求类型 验证码获取类型
     * register ： 注册验证码
     * login：登录验证码
     * reset：忘记密码验证码
     * setphone：更换手机号验证码
     * payPassword:设置支付密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/send-sms")
    Observable<BaseData> getSmsCode(@Field("obj") String phone,
                                    @Field("mode") String mode,
                                    @Field("type") String type);


    /**
     * 类型：
     * register:注册
     * reset：修改登录密码
     * resetwallet:修改资产密码
     * acquire：获取密钥
     * bind:绑定
     * inside:挂单出售/转账/提取/商城支付
     */
    @FormUrlEncoded
    @POST("/member/user-api/send-sms")
    Observable<BaseData> getSmsCode(@Field("obj") String obj,
                                    @Field("type") String type,
                                    @Field("mode") String mode,
                                    @Field("url") String url,
                                    @Field("img_code") String img_code);

    /**
     * 用户注册
     */
    @FormUrlEncoded
    @POST("member/user-api/register")
    Observable<BaseData<UserInfoEntity>> doRegister(@Field("phone") String phone,
                                                    @Field("code") String code,
                                                    @Field("password") String password,
                                                    @Field("username") String username,
                                                    @Field("invitation_code") String invite_code,
                                                    @Field("openid") String openid,
                                                    @Field("scenario") String scenario,
                                                    @Field("unionid") String unionid,
                                                    @Field("jscode") String jscode
    );

    /**
     * 用户注册
     */
    @FormUrlEncoded
    @POST("member/user-api/register")
    Observable<BaseData<UserInfoEntity>> doRegisterEdc(@Field("obj") String obj,
                                                       @Field("mode") String mode,
                                                       @Field("code") String code,
                                                       @Field("password") String password,
                                                       @Field("invite_code") String invite_code

    );

    /**
     * 用户注册
     * scenario  1：手机号+验证码+密码注册注册场景： register_phone
     * role  默认传10 代表商家注册
     */
    @FormUrlEncoded
    @POST("/adminUser/user-api/register")
    Observable<BaseData<UserInfoEntity>> doRegisterBussiness(@Field("phone") String phone,
                                                             @Field("invitation_code") String invitation_code,
                                                             @Field("code") String code,
                                                             @Field("password") String password,
                                                             @Field("repassword") String repassword,
                                                             @Field("scenario") String scenario,
                                                             @Field("role") String role

    );

    /**
     * 忘记登录密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/reset-password")
    Observable<BaseData> resetPassword(@Field("password") String password,
                                       @Field("code") String code,
                                       @Field("phone") String phone);

    /**
     * 忘记资产密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/reset-pay-password")
    Observable<BaseData> forgetResetPass(@Field("obj") String obj,
                                         @Field("mode") String mode,
                                         @Field("code") String code,
                                         @Field("password") String password);


    /**
     * 设置交易密码
     *
     * @param password 交易密码
     * @return
     */
    @FormUrlEncoded
    @POST("member/user-api/set-pay-password")
    Observable<BaseData> doSetChargePassword(@Field("password") String password);
}
