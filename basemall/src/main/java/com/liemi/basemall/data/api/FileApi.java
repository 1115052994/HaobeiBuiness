package com.liemi.basemall.data.api;
import com.liemi.basemall.data.entity.FileEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UpFilesEntity;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Bingo on 2018/6/1.
 */

public interface FileApi {
    /**
     * 图片上传
     */
    @Multipart
    @POST("material/index/aws-upload")
    Observable<BaseData<UpFilesEntity>> fileUp(@Part MultipartBody.Part multiparts);
}
