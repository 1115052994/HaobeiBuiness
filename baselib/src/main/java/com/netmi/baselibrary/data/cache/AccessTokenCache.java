package com.netmi.baselibrary.data.cache;

import android.text.TextUtils;


import com.netmi.baselibrary.data.entity.AccessToken;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.Logs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 14:46
 * 修改备注：
 */
public class AccessTokenCache {

    private static final String TAG = "AccessTokenCache";

    private static AccessToken accessToken;

    /**
     * accesstoken
     */
    public static final String TOKEN = "access_token";

    /**
     * 云信token
     */
    public static final String YUNXIN_TOKEN = "yunxin_token";

    /**
     * 云信通信id
     */
    public static final String ACCID = "yunxin_accid";

    /**
     * 有效开始时间
     */
    public static final String START_TIME = "start_time";

    /**
     * 有效结束时间
     */
    public static final String END_TIME = "end_time";

    /**
     * 用户uid
     */
    public static final String UID = "user_uid";

    public static final Object object = new Object();

    public static void put(AccessToken accessToken) {
        PrefCache.putData(TOKEN, accessToken.getToken());
        PrefCache.putData(YUNXIN_TOKEN, accessToken.getYunxin_token());
        PrefCache.putData(ACCID, accessToken.getAccid());
        PrefCache.putData(START_TIME, accessToken.getStart_time());
        PrefCache.putData(END_TIME, accessToken.getEnd_time());
        PrefCache.putData(UID, accessToken.getUid());
        AccessTokenCache.accessToken = accessToken;
    }

    public static void clear() {
        PrefCache.removeData(TOKEN);
        PrefCache.removeData(YUNXIN_TOKEN);
        PrefCache.removeData(ACCID);
        PrefCache.removeData(START_TIME);
        PrefCache.removeData(END_TIME);
        PrefCache.removeData(UID);
        accessToken = null;
    }

    public static AccessToken get() {
        if(accessToken == null) {
            accessToken = new AccessToken();
            accessToken.setToken((String) PrefCache.getData(TOKEN, ""));
            accessToken.setYunxin_token((String) PrefCache.getData(YUNXIN_TOKEN, ""));
            accessToken.setAccid((String) PrefCache.getData(ACCID, ""));
            accessToken.setStart_time((String) PrefCache.getData(START_TIME, ""));
            accessToken.setEnd_time((String) PrefCache.getData(END_TIME, ""));
            accessToken.setUid((String) PrefCache.getData(UID, ""));
        }
        return accessToken;
    }

    /**
     * token 是否失效
     *
     * @return
     */
    public static boolean isTokenExpired() {

        if(TextUtils.isEmpty(accessToken.getStart_time()))
           return false;

        Logs.d(TAG, "into isTokenExpired()");
        if (!TextUtils.isEmpty(accessToken.getEnd_time())) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.DF_YYYY_MM_DD_HH_MM_SS);
                Date date = formatter.parse(accessToken.getEnd_time());
                int result = date.compareTo(new Date());
                Logs.d(TAG, "end_time :"+accessToken.getEnd_time());
                Logs.d(TAG, "result :"+result);
                if (result > 0) {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
