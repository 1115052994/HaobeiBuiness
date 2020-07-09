package com.liemi.basemall.data;

import android.os.Environment;

/**
 * 类描述：
 * 创建人：Jacky
 * 创建时间：2019/1/16
 * 修改备注：
 */
public class AppConfig {
    /**
     * 缓存文件夹
     */
    public static final String SAVEDFILE_LOCATION = Environment.getExternalStorageDirectory().getPath() + "/yms/data";
    public static final String SAVEDFILE_LOCATION_BUFFER_BIT = SAVEDFILE_LOCATION + "/bit";
}
