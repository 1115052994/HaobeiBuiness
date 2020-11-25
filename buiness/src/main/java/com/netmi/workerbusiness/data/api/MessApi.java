package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.CreateContractEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.data.entity.TemplateListEntity;
import com.netmi.workerbusiness.data.entity.mess.CustomerEntity;
import com.netmi.workerbusiness.data.entity.mess.PublicNoticeEntity;

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
     * 合同列表确认提交
     */
    @FormUrlEncoded
    @POST("shop/shop-apply-record-api/confirm-contract")
    Observable<BaseData<String>> confirmContract(@Field("pram") String pram);


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

    /**
     * 绑定/解绑设备  audio_device_id  绑定时不得为空，解绑时为空  type  1 - 绑定 2 - 解绑
     */
    @FormUrlEncoded
    @POST("shop/shop-api/bind-audio-system")
    Observable<BaseData> bindAudioSystem(@Field("audio_device_id") String audio_device_id,
                              @Field("type") String type);
    /**
     * 获取合同模板列表
     */
    @FormUrlEncoded
    @POST("shop/shop-apply-record-api/template-list")
    Observable<BaseData<List<TemplateListEntity>>> templateList(@Field("pram") String pram);

    /**
     * 生成合同文件 并跳转到签署合同
     */
    @FormUrlEncoded
    @POST("shop/shop-apply-record-api/create-contract")
    Observable<BaseData<CreateContractEntity>> createContract(@Field("tid") String tid);

    /**
     * 获取预览合同URL
     */
    @FormUrlEncoded
    @POST("shop/shop-apply-record-api/get-preview-url")
    Observable<BaseData<CreateContractEntity>> getPreviewUrl(@Field("contract_id") String contract_id);

    /**
     * 获取下载合同链接
     */
    @FormUrlEncoded
    @POST("shop/shop-apply-record-api/download-contract")
    Observable<BaseData<CreateContractEntity>> downloadContract(@Field("contract_id") String contract_id);


}
