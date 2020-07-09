package com.netmi.workerbusiness.data.api;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.data.entity.walllet.ETHDetailEntity;
import com.netmi.workerbusiness.data.entity.walllet.EthDetailTwoEntity;
import com.netmi.workerbusiness.data.entity.walllet.WalletEntity;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/3/17
 * 修改备注：
 */
public interface WalletApi {

    /**
     * 钱包信息
     */
    @FormUrlEncoded
    @POST("wallet/index/info")
    Observable<BaseData<WalletEntity>> getWalletInfo(@Field("parm") String parm);

    /**
     * 提取
     */
    @FormUrlEncoded
    @POST("wallet/index/extract")
    Observable<BaseData> withdraw(@Field("address") String address,
                                  @Field("amount") String amount,
                                  @Field("password") String password);

    /**
     * 提转账
     */
    @FormUrlEncoded
    @POST("wallet/index/transfer")
    Observable<BaseData> transfer(@Field("share_code") String share_code,
                                  @Field("amount") String amount,
                                  @Field("password") String password);


    /**
     * 公链ETH钱包明细列表
     */
    @FormUrlEncoded
    @POST("/hand/hand-api/income-list")
    Observable<BaseData<PageEntity<EthDetailTwoEntity>>> ETHDetail(@Field("start_page") int start_page,
                                                                   @Field("pages") int pages);

    /**
     * 收款二维码
     */
    @FormUrlEncoded
    @POST("/wallet/index/img")
    Observable<BaseData<String>> getCode(@Field("shop_id") String shop_id);

}
