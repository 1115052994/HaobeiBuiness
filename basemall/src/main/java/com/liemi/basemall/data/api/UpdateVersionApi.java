package com.liemi.basemall.data.api;


import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UpdateVersionDto;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Bingo on 2018/2/27.
 */

public interface UpdateVersionApi {
    @FormUrlEncoded
    @POST("/base/edition/edition")
    Observable<BaseData<UpdateVersionDto>> updateVersion(@Field("param") String param);
}
