package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BankOkThreeApi {
    /**
     *
     */
    @FormUrlEncoded
    @POST("shop/shop-apply-record-api/ips-send-sms")
    Observable<BaseData> doBankOkThree( @Field("phone") String phone);

    /**
     *
     */
    @FormUrlEncoded
    @POST("shop/shop-apply-record-api/ips-user-register")
    Observable<BaseData> doBankOkVerify(@Field("bankId") String bankId,
                                        @Field("cardNo") String cardNo,
                                        @Field("code") String code,
                                        @Field("phone") String phone);
}
