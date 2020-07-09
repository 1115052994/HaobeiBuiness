package com.netmi.baselibrary.utils.glide;

import android.content.Context;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/3/2 15:23
 * 修改备注：
 */
public class GlideCacheUtils {

    public static File getPhotoCacheDir(Context context) {
        return Glide.getPhotoCacheDir(context);
    }

    public static void clearDiskCache(Context context){
        Glide.get(context).clearDiskCache();
    }

}
