package com.netease.nim.uikit.api;

import com.netease.nim.uikit.api.model.team.TeamInfoEntity;
import com.netmi.baselibrary.data.entity.BaseData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/27
 * 修改备注：
 */
public interface IMGroupApi {


    /**
     * 创建群
     */
    @FormUrlEncoded
    @POST("member/user-group-api/create-group")
    Observable<BaseData<TeamInfoEntity>> createGroup(@Field("tname") String tname,
                                                     @Field("members[]") List<String> members,
                                                     @Field("msg") String msg);


    /**
     * 编辑群
     */
    @FormUrlEncoded
    @POST("member/user-group-api/update-group")
    Observable<BaseData> updateGroup(@Field("tid") String tid,
                                     @Field("tname") String tname);


    /**
     * 解散群
     */
    @FormUrlEncoded
    @POST("member/user-group-api/delete-group")
    Observable<BaseData> deleteGroup(@Field("tid") String tid);

    /**
     * 移交群主
     *
     * @param newowner 新群主的accid
     * @param leave    1:群主转让群主后离开群，2：群主转让群主后成为普通成员
     */
    @FormUrlEncoded
    @POST("member/user-group-api/transfer-group")
    Observable<BaseData> transferGroup(@Field("tid") String tid,
                                       @Field("newowner") String newowner,
                                       @Field("leave") String leave);
}
