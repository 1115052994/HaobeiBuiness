package com.liemi.basemall.data.api;

import com.liemi.basemall.data.entity.floor.FloorPageEntity;
import com.liemi.basemall.data.entity.floor.NewFloorEntity;
import com.netmi.baselibrary.data.entity.BaseData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Bingo on 2018/11/19.
 */

public interface FloorApi {
    /**
     * 楼层展示接口
     * use_position	是	int	启用标签	1
     shop_id	是	int	店铺id	1
     */
    @FormUrlEncoded
    @POST("floor/floor-api/get-floor-info")
    Observable<BaseData<FloorPageEntity<NewFloorEntity>>> doListFloors(@Field("use_position") int usePosition ,
                                                                       @Field("shop_id") String storeId);
}
