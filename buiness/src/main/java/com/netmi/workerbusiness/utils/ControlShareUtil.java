package com.netmi.workerbusiness.utils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Bingo on 2019/1/21.
 */
public class ControlShareUtil {

    private volatile static ControlShareUtil instance = null;

    private String wxAppId, wxAppSecret;

    private ControlShareUtil() {
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        wxAppId = platform.getDevinfo("AppId");
        wxAppSecret = platform.getDevinfo("AppSecret");
    }

    public static ControlShareUtil getInstance() {
        if (instance == null) {
            synchronized (ControlShareUtil.class) {
                if (instance == null) {
                    instance = new ControlShareUtil();
                }
            }
        }
        return instance;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public String getWxAppSecret() {
        return wxAppSecret;
    }

    /**
     * @param bypassApproval 是否绕过审核
     */
    public static void controlWechatShare(boolean bypassApproval) {
        HashMap<String, Object> optionMap = new HashMap<>();

        optionMap.put("Id", "5");
        optionMap.put("SortId", "5");
        optionMap.put("AppId", getInstance().getWxAppId());
        optionMap.put("AppSecret", getInstance().getWxAppSecret());
        optionMap.put("BypassApproval", bypassApproval);
        optionMap.put("Enable", true);
        ShareSDK.setPlatformDevInfo(Wechat.NAME, optionMap);
    }

    /**
     * @param bypassApproval 是否绕过审核
     */
    public static void controlWechatMomentsShare(boolean bypassApproval) {
        HashMap<String, Object> optionMap = new HashMap<>();

        optionMap.put("Id", "5");
        optionMap.put("SortId", "5");
        optionMap.put("AppId", getInstance().getWxAppId());
        optionMap.put("AppSecret", getInstance().getWxAppSecret());
        optionMap.put("BypassApproval", bypassApproval);
        optionMap.put("Enable", true);
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, optionMap);
    }
}
