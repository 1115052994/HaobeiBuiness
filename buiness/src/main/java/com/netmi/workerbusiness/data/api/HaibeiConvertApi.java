package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.workerbusiness.data.entity.haibei.HaibeiBonus;
import com.netmi.workerbusiness.data.entity.haibei.HaibeiData;
import com.netmi.workerbusiness.data.entity.haibei.HaibeiOrderList;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/24
 * 修改备注：
 */
public interface HaibeiConvertApi {
    /**
     *
     */
    @FormUrlEncoded
    @POST("haibei/haibei-api/index")
    Observable<BaseData<HaibeiData>> getHaibeiConvert(@Field("pram") String pages);
    /**
     *
     */
    @FormUrlEncoded
    @POST("haibei/haibei-api/haibei-order")
    Observable<BaseData<List<HaibeiOrderList>>> getHaibeiOrder(@Field("pram") String pages);
    /**
     *
     */
    @FormUrlEncoded
    @POST("haibei/haibei-api/get-haibei-bonus")
    Observable<BaseData<HaibeiBonus>> getHaibeiBouns(@Field("pram") String pages);
    /**
     *
     */
    @FormUrlEncoded
    @POST("haibei/haibei-api/haibei-withdrawal")
    Observable<BaseData> haibeiWithdrawal(@Field("bonus") String pages);




}
