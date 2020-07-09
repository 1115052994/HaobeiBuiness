package com.netmi.baselibrary.utils.yangmu;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 类描述：
 * 创建人：Jacky
 * 创建时间：2019/1/16
 * 修改备注：
 */
public class FileUtils {
    public static RequestBody createRequestBody(File file) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), file);
    }

    public static MultipartBody.Part createMutipartPng(File file) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.hashCode() + ".png", createRequestBody(file));
        return filePart;
    }

}
