package com.netmi.baselibrary.data.api;

import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.CityChoiceEntity;
import com.netmi.baselibrary.data.entity.OssConfigureEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.data.entity.PlatformEntity;
import com.netmi.baselibrary.data.entity.UpFilesEntity;
import com.netmi.baselibrary.data.entity.UserInfoEntity;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/30 17:43
 * 修改备注：
 */
public interface CommonApi {

    /**
     * 获取省、市、区信息列表
     * data_type:
     * 0:获取单级数据 1： 获取所有数据
     */
    @FormUrlEncoded
    @POST("base/district-api/index")
    Observable<BaseData<List<CityChoiceEntity>>> listCity(@Field("data_type") int data_type);


    /**
     * 获取省、市、区信息列表
     * data_type:
     * 0:获取单级数据 1： 获取所有数据
     */
    @FormUrlEncoded
    @POST("base/district-api/index")
    Observable<BaseData<List<CityChoiceEntity>>> listCityPostage(@Field("upid") int upid,
                                                                 @Field("data_type") int data_type);

    /**
     * /**
     * 列表信息获取接口
     * seat_id ： 展示位置 1： 首页 2：商城 3：新闻 （依据项目自己定义）
     */
    @FormUrlEncoded
    @POST("banner/banner-api/index")
    Observable<BaseData<PageEntity<BannerEntity>>> listBanner(@Field("seat_id") int seat_id);

    /**
     * oss基础配置信息获取
     */
    @FormUrlEncoded
    @POST("base/oss-api/info")
    Observable<BaseData<OssConfigureEntity>> getOssConfigure(@Field("param") int param);

    /**
     * 获取用户基本信息
     */
    @FormUrlEncoded
    @POST("usermember/user-data-api/index")
    Observable<BaseData<UserInfoEntity>> getUserInfo(@Field("param") int param);

    /**
     * 平台相关基本信息获取
     */
    @FormUrlEncoded
    @POST("base/intel-api/info")
    Observable<BaseData<PlatformEntity>> getPlatformInfo(@Field("param") String param);


    /**
     * 批量文件上传
     *
     * @param
     * @return
     */
    @Multipart
    @POST("material/index/aws-upload")
//    Observable<BaseData<UpFilesEntity>> uploadFiles(@Part List<MultipartBody.Part> multiparts);
//    Observable<BaseData<UpFilesEntity>> uploadFiles(@Part("file\"; filename=\"image5.png\"") RequestBody file);
    Observable<BaseData<UpFilesEntity>> uploadFiles(@Part MultipartBody.Part file);

    /**
     * 开机广告页
     */
    @FormUrlEncoded
    @POST("banner/banner-api/open-ad")
    Observable<BaseData<BannerEntity>> getAdvertising(@Field("type") String type);


}
