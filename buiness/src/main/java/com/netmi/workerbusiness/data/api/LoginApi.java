package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.workerbusiness.data.entity.CateGoryVerifyEntity;
import com.netmi.workerbusiness.data.entity.home.BusinessOverviewEntity;
import com.netmi.workerbusiness.data.entity.home.category.GoodsOneCateEntity;
import com.netmi.workerbusiness.data.event.PostageListEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/8/29
 * 修改备注：
 */
public interface LoginApi {

    /**
     * 商务类型选择
     */
    @FormUrlEncoded
    @POST("adminUser/user-api/select-type")
    Observable<BaseData> chooseType(@Field("type") String type);


    /**
     * 商家店铺入驻
     * token		string	token
     * name		String	店铺名
     * logo_url		array	门店图片
     * content		String	商家简介
     * longitude		String	地理位置经度
     * latitude		String	地理位置纬度
     * do_time		String	营业时间
     * img_url		array	商铺展示图片
     * shop_url		array	商铺店内图片
     * real_name		String	法人姓名
     * idcard		String	身份证号
     * front_card_url		String	身份证正面照
     * back_card_url		String	身份证背面照
     * license_url		String	营业执照
     * license_img_url		String	经营许可证图片
     */
    @FormUrlEncoded
    @POST("shop/shop-apply-record-api/create")
    Observable<BaseData> shopSettled(@Field("name") String name,
                                     @Field("logo_url") String logo_url,
                                     @Field("content") String content,
                                     @Field("longitude") String longitude,
                                     @Field("latitude") String latitude,
                                     @Field("do_time") String do_time,
                                     @Field("shop_url[]") List<String> shop_url,
                                     @Field("img_url") String img_url,
                                     @Field("front_card_url") String front_card_url,
                                     @Field("back_card_url") String back_card_url,
                                     @Field("license_url") String license_url,
                                     @Field("real_name") String real_name,
                                     @Field("idcard") String idcard,
                                     @Field("operation_license_url") String operation_license_url,
                                     @Field("p_id") String p_id,
                                     @Field("c_id") String c_id,
                                     @Field("d_id") String d_id,
                                     @Field("address") String address,
                                     @Field("category_id") String category_id,
                                     @Field("license_num") String license_num,
                                     @Field("hold_card_url") String hold_card_url);


    /**
     * 首页数据
     */
    @FormUrlEncoded
    @POST("adminUser/index-api/index")
    Observable<BaseData<BusinessOverviewEntity>> businessOverview(@Field("pram") String pram);

    /**
     * 获取商家分类
     */
    @FormUrlEncoded
    @POST("order/order-api/category")
    Observable<BaseData<PageEntity<CateGoryVerifyEntity>>> getCategory(@Field("server_type") String server_type);


}
