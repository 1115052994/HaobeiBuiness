package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.workerbusiness.data.entity.walllet.BankBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BankThreeApi {
    /**
     *
     */
    @FormUrlEncoded
    @POST("shop/ips-api/get-bank-list")
    Observable<BaseData<PageEntity<BankBean>>> getBankThreeConvert(@Field("server_bank") String server_bank);


}
