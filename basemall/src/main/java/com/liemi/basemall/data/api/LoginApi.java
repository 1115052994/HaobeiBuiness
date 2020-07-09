package com.liemi.basemall.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 用户登录注册相关
 */
public interface LoginApi {
    /**
     * 获取验证码
     *
     * @param phone 手机号码   必选字段
     * @param type  请求类型   必选字段
     * @return type 取值：
     * register:注册验证码
     * login:登录验证码
     * reset:忘记密码验证码
     * payPassword:设置支付密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/send-sms")
    Observable<BaseData> doAuthCode(@Field("phone") String phone,
                                    @Field("type") String type);


    /**
     * 请求类型 register：注册，
     * login：登陆，
     * resetPassword：重置密码，
     * changePassword：修改密码，
     * changePayPassword：修改支付密码，
     * resetPayPassword：找回支付密码，
     * 以下三个可不填sign以及code:
     * forward：转账，pickUp：提取，OTC：OTC交易
     */
    @FormUrlEncoded
    @POST("/member/user-api/send-sms")
    Observable<BaseData> doAuthCode(@Field("phone") String phone,
                                    @Field("sign") String sign,
                                    @Field("code") String code,
                                    @Field("type") String type);


    /**
     * 用户注册
     *
     * @param phone           注册手机号  可选字段
     * @param code            注册验证码   可选字段
     * @param password        注册验证码   可选字段
     * @param username        用户名 可选字段
     * @param invitation_code 邀请码 可选字段
     * @param openid          微信openId    可选字段
     * @param scenario        注册类型
     * @return scenario取值：
     * register_phone 手机号+验证码+密码注册场景
     * register_default    用户名+手机号+密码注册场景
     * register_wechat     微信openId注册场景
     * register_wechat_phone   微信openId+手机号+验证码
     */
    @FormUrlEncoded
    @POST("/member/user-api/register")
    Observable<BaseData<UserInfoEntity>> doRegister(@Field("phone") String phone,
                                                    @Field("code") String code,
                                                    @Field("password") String password,
                                                    @Field("username") String username,
                                                    @Field("invitation_code") String invitation_code,
                                                    @Field("openid") String openid,
                                                    @Field("scenario") String scenario);

    /**
     * 登录通用方法
     *
     * @param username 用户名   用于与用户名+密码登录方式
     * @param password 密码     用于用户名+密码  手机号+密码登录方式
     * @param phone    手机号     用于手机号+密码   手机号+验证码登录方式
     * @param code     验证码     用于手机号+验证码登录方式
     * @param openid   微信用户id  用于微信登陆方式
     * @param scenario 登录方式    用于标识用户的登录方式
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/login")
    Observable<BaseData<UserInfoEntity>> doLogin(@Field("username") String username,
                                                 @Field("password") String password,
                                                 @Field("phone") String phone,
                                                 @Field("code") String code,
                                                 @Field("openid") String openid,
                                                 @Field("scenario") String scenario);

    /**
     * 用户名 + 密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @param scenario 固定值“login_default”
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/login")
    Observable<BaseData<UserInfoEntity>> doLoginUserNamePassword(@Field("username") String username,
                                                                 @Field("password") String password,
                                                                 @Field("scenario") String scenario);

    /**
     * 手机号（邮箱）+密码登录
     *
     * @param obj      手机号或者邮箱
     * @param password 密码
     * @param scenario 固定取值“login_phone”
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/login")
    Observable<BaseData<UserInfoEntity>> doLoginPhonePassword(@Field("obj") String obj,
                                                              @Field("password") String password,
                                                              @Field("scenario") String scenario);

    /**
     * 手机号+验证码登录
     *
     * @param phone    手机号
     * @param code     验证码
     * @param scenario 固定取值“login_code”
     * @return
     */
    @FormUrlEncoded
    @POST("adminUser/user-api/login")
    Observable<BaseData<UserInfoEntity>> doLoginPhoneCode(@Field("phone") String phone,
                                                          @Field("code") String code,
                                                          @Field("password") String password,
                                                          @Field("scenario") String scenario,
                                                          @Field("role") String role);

    /**
     * 微信登录
     *
     * @param openid   微信openId
     * @param scenario 固定取值“login_wechat”
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/login")
    Observable<BaseData<UserInfoEntity>> doLoginWechat(@Field("openid") String openid,
                                                       @Field("scenario") String scenario);

    /**
     * 忘记密码/找回密码接口
     *
     * @param password 新密码
     * @param code     验证码
     * @param phone    手机号
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/reset-password")
    Observable<BaseData> doForgetPassword(@Field("password") String password,
                                          @Field("code") String code,
                                          @Field("phone") String phone);

    @FormUrlEncoded
    @POST("/member/user-api/change-phone")
    Observable<BaseData> doChangePhone(@Field("phone") String phone,
                                       @Field("code") String code);

    @FormUrlEncoded
    @POST("member/user-api/bind-phone")
    Observable<BaseData<UserInfoEntity>> doBindPhone(@Field("phone") String phone,
                                                     @Field("code") String code,
                                                     @Field("token") String token);


    /**
     * 设置支付密码
     *
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/set-pay-password")
    Observable<BaseData> doSetPayPassword(@Field("password") String password);
}
