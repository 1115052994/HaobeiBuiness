package com.netmi.baselibrary.data.cache;

import android.text.TextUtils;

import com.netmi.baselibrary.data.entity.UserInfoEntity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/1 10:05
 * 修改备注：
 */
public class UserInfoCache {

    /**
     * 缓存用户信息
     */
    private static UserInfoEntity userInfoEntity;


    private static final String UID = "uid";


    private static final String NICKNAME = "nickname";


    private static final String HEAD_URL = "headUrl";
    /**
     * 邀请码
     */
    private static final String INVITE_CODE = "invite_code";
    /***
     *审核状态
     */
    private static final String SHOP_APPLY_STATUS = "shop_apply_status";

    /***
     *缴费状态
     */
    private static final String SHOP_PAY_STATUS = "shop_pay_status";


    /**
     * 保存登陆用户信息
     *
     * @param entity
     */
    public static void put(UserInfoEntity entity) {
        if (!TextUtils.isEmpty(entity.getUid()))
            PrefCache.putData(UID, entity.getUid());
        if (!TextUtils.isEmpty(entity.getNickname()))
            PrefCache.putData(NICKNAME, entity.getNickname());
        if (!TextUtils.isEmpty(entity.getHead_url()))
            PrefCache.putData(HEAD_URL, entity.getHead_url());
        if (!TextUtils.isEmpty(entity.getInvite_code()))
            PrefCache.putData(INVITE_CODE, entity.getInvite_code());
        if (!TextUtils.isEmpty(entity.getShop_apply_status()))
            PrefCache.putData(SHOP_APPLY_STATUS, entity.getShop_apply_status());
        if (!TextUtils.isEmpty(entity.getShop_pay_status()))
            PrefCache.putData(SHOP_PAY_STATUS, entity.getShop_pay_status());
        UserInfoCache.userInfoEntity = entity;
        setBankInfo();
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public static UserInfoEntity get() {
        if (userInfoEntity == null) {
            userInfoEntity = new UserInfoEntity();
            setBankInfo();
        }
        return userInfoEntity;
    }

    private static void setBankInfo() {
        userInfoEntity.setUid((String) PrefCache.getData(UID, ""));
        userInfoEntity.setNickname((String) PrefCache.getData(NICKNAME, ""));
        userInfoEntity.setHead_url((String) PrefCache.getData(HEAD_URL, ""));
        userInfoEntity.setInvite_code((String) PrefCache.getData(INVITE_CODE, ""));
        userInfoEntity.setShop_apply_status((String) PrefCache.getData(SHOP_APPLY_STATUS, ""));
        userInfoEntity.setShop_pay_status((String) PrefCache.getData(SHOP_PAY_STATUS, ""));
    }

    public static void clear() {
        PrefCache.removeData(UID);
        PrefCache.removeData(NICKNAME);
        PrefCache.removeData(HEAD_URL);
        PrefCache.removeData(INVITE_CODE);
        PrefCache.removeData(SHOP_APPLY_STATUS);
        PrefCache.removeData(SHOP_PAY_STATUS);
        userInfoEntity = null;
    }
}
