package com.netmi.workerbusiness.utils;

import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.SPs;

import static com.netmi.baselibrary.data.Constant.IS_PUSH;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/4/8
 * 修改备注：
 */
public class PushUtils {
    //登录成功个推绑定别名
    public static void bindPush() {
        if (AccessTokenCache.get() != null && !TextUtils.isEmpty(UserInfoCache.get().getPhone())) {
            PushManager.getInstance().bindAlias(MApplication.getAppContext(),AppUtils.isAppDebug()?"haibei_test_"+ UserInfoCache.get().getPhone():"haibei_" + UserInfoCache.get().getPhone(),
                    PushManager.getInstance().getClientid(MApplication.getAppContext()));
        }
        SPs.put(MApplication.getAppContext(), IS_PUSH, false);
        Log.e("GTIntentService", "PushUtils " + "====Phone:"+ Constant.PUSH_PREFIX + AccessTokenCache.get().getUid() + UserInfoCache.get().getPhone());

    }

    //个推取消绑定别名
    public static void unbindPush() {
        if (AccessTokenCache.get() != null && !TextUtils.isEmpty(UserInfoCache.get().getPhone())) {
            PushManager.getInstance().unBindAlias(MApplication.getAppContext(), AppUtils.isAppDebug()?"haibei_test_"+ UserInfoCache.get().getPhone():"haibei_" + UserInfoCache.get().getPhone(), true,
                    PushManager.getInstance().getClientid(MApplication.getAppContext()));
        }
        SPs.put(MApplication.getAppContext(), IS_PUSH, true);
    }
}
