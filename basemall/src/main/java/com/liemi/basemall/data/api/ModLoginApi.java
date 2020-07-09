package com.liemi.basemall.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 10:58
 * 修改备注：
 */
public interface ModLoginApi {

    /**
     * 注册
     * scenario： 1：手机号+验证码+密码注册注册场景： register_phone 2：手机号+验证码+密码注册注册场景： register_default
     * 3：手机号+验证码+密码注册注册场景： register_wechat
     */
    @FormUrlEncoded
    @POST("usermember/user-api/register")
    Observable<BaseData> doRegister(@Field("phone") String phone,
                                    @Field("password") String password,
                                    @Field("code") String code,
                                    @Field("invitation_code") String invitation_code,
                                    @Field("openid") String openid,
                                    @Field("scenario") String scenario);

    /**
     * 重置密码
     */
    @FormUrlEncoded
    @POST("usermember/user-api/reset-password")
    Observable<BaseData> doForget(@Field("phone") String phone,
                                  @Field("password") String password,
                                  @Field("code") String code);

    /**
     * 用户登录
     * scenario：1：用户名+密码登录场景： login_default 2：手机号+密码登录场景：login_phone
     * 3：手机号+验证码登录场景：login_code 4: 微信登录场景：login_wechat
     */
    @FormUrlEncoded
    @POST("usermember/user-api/login")
    Observable<BaseData<UserInfoEntity>> doLogin(@Field("phone") String phone,
                                                 @Field("password") String password,
                                                 @Field("code") String code,
                                                 @Field("username") String username,
                                                 @Field("openid") String openid,
                                                 @Field("scenario") String scenario);


}
