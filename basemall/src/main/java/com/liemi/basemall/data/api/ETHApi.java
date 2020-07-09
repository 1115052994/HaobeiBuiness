package com.liemi.basemall.data.api;

import com.liemi.basemall.data.entity.eth.ETHRateEntity;
import com.netmi.baselibrary.data.entity.BaseData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by Bingo on 2018/12/1.
 */

public interface ETHApi {
    @FormUrlEncoded
    @POST("wallet/wallet-eth-api/rate")
    Observable<BaseData<ETHRateEntity>> getETHRate(@Field("param") String param);
}
