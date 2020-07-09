package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.workerbusiness.data.entity.home.postage.AddFreightTempleCommand;
import com.netmi.workerbusiness.data.entity.home.postage.FreightListEntity;
import com.netmi.workerbusiness.data.entity.home.postage.PostageDetailEntity;
import com.netmi.workerbusiness.data.entity.home.postage.PostageTempleDetailListEntity;
import com.netmi.workerbusiness.data.entity.home.postage.UpdateFreightTempleCommand;
import com.netmi.workerbusiness.data.event.PostageListEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/4
 * 修改备注：
 */
public interface PostageApi {
    /**
     * 邮费模版(详情)
     */
    @FormUrlEncoded
    @POST("express/express-template-api/detail")
    Observable<BaseData<PageEntity<PostageTempleDetailListEntity>>> getFreightAreaList(@Field("fg_id") String fg_id,
                                                                                       @Field("start_page") int start_page,
                                                                                       @Field("pages") int pages);


    /**
     * 邮费模版(详情)
     */
    @FormUrlEncoded
    @POST("express/express-template-api/detail")
    Observable<BaseData<PostageDetailEntity>> getPostageDetail(@Field("t_id") String t_id);

//    /**
//     * 邮费模版列表接口
//     */
//    @FormUrlEncoded
//    @POST("shop/shop-freight-api/freight-group-list")
//    Observable<BaseData<PageEntity<FreightListEntity>>> getFreightList(@Field("start_page") int start_page,
//                                                                       @Field("pages") int pages);

    /**
     * 邮费模板列表
     */
    @FormUrlEncoded
    @POST("express/express-template-api/index")
    Observable<BaseData<List<PostageListEntity>>> postageList(@Field("pram") String pram);

    /**
     * 新增邮费模版接口
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("express/express-template-api/create  ")
    Observable<BaseData> getAddFreight(@Body AddFreightTempleCommand addFreightTempleCommod);


    /**
     * 修改子邮费模版接口
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("express/express-template-api/update")
    Observable<BaseData> getUpdateFreight(@Body UpdateFreightTempleCommand updateFreightTempleCommand);

    /**
     * 删除子邮费模版接口
     */
    @FormUrlEncoded
    @POST("express/express-template-api/delete")
    Observable<BaseData> getDeleteFreight(@Field("tc_id") String tc_id);

}
