package com.netmi.baselibrary.data.cache;


import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.SPs;

/**
 * 类描述：SharedPreferences 数据管理类
 * 创建人：Simple
 * 创建时间：2017/9/5 14:47
 * 修改备注：
 */
public class PrefCache {

    public static void putData(String key, Object object) {

        SPs.put(MApplication.getAppContext(), key, object);
    }

    public static Object getData(String key, Object defaultObject) {

        return SPs.get(MApplication.getAppContext(), key, defaultObject);
    }

    public static void removeData(String key) {
        SPs.remove(MApplication.getAppContext(), key);
    }

    public static void clearData() {
        SPs.clear(MApplication.getAppContext());
    }

    public boolean contains(String key) {

        return SPs.contains(MApplication.getAppContext(), key);
    }
}
