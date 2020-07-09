package com.netmi.baselibrary.data.cache;


import com.netmi.baselibrary.data.entity.AppConfigEntity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/4/18 17:57
 * 修改备注：
 */
public class AppConfigCache {

    private static final String APP_CONFIG = "appConfig";

    private static final String APP_CONFIG_PUSH = "appConfigPush";

    /**
     * 缓存APP配置信息
     */
    private static AppConfigEntity appConfigEntity;


    public static void put(AppConfigEntity entity) {
        appConfigEntity = entity;
    }

    public static AppConfigEntity get() {
        if (appConfigEntity == null) {
            appConfigEntity = new AppConfigEntity();
            appConfigEntity.setStatus((Integer) PrefCache.getData(APP_CONFIG_PUSH, 0));
        }
        return appConfigEntity;
    }

    public static void setStatus(int status) {
        get().setStatus(status);
        PrefCache.putData(APP_CONFIG_PUSH, status);
    }

    public static void clear() {
        appConfigEntity = null;
    }

}
