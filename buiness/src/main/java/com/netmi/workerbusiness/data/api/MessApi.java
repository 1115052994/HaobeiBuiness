package com.netmi.workerbusiness.data.api;

import android.content.Intent;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.workerbusiness.data.entity.home.linecommodity.LineCommodityListEntity;
import com.netmi.workerbusiness.data.entity.mess.CustomerEntity;
import com.netmi.workerbusiness.data.entity.mess.PublicNoticeEntity;

import java.util.ArrayList;
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
public interface MessApi {
    /**
     * type_arr  1 平台公告  2订单通知  5 系统通知
     */
    @FormUrlEncoded
    @POST("notice/notice-api/index")
    Observable<BaseData<PageEntity<PublicNoticeEntity>>> getNotice(@Field("type_arr[]") Integer[] type_arr,
                                                                   @Field("start_page") String start_page,
                                                                   @Field("pages") String pages);


    /**
     * 平台客服
     */
    @FormUrlEncoded
    @POST("adminUser/index-api/customer")
    Observable<BaseData<CustomerEntity>> getMessage(@Field("pram") String pram);


    /**
     * 一键已读
     */
    @FormUrlEncoded
    @POST("notice/notice-api/shop-set-read-all")
    Observable<BaseData> readAll(@Field("pram") String pram);

    /**
     * 公告已读标志设置
     */
    @FormUrlEncoded
    @POST("notice/notice-api/set-read")
    Observable<BaseData> read(@Field("notice_id") String notice_id);


}
