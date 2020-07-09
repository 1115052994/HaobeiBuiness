package com.netmi.baselibrary.data.cache;


import com.netmi.baselibrary.data.entity.LoginInfoEntity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 14:53
 * 修改备注：
 */
public class LoginInfoCache {

    /**
     * 缓存用户信息
     */
    private static LoginInfoEntity loginInfo;
    /**
     * 用户账号
     */
    public static final String LOGIN = "login";

    /**
     * 用户密码
     */
    public static final String PASSWORD = "password";

    /**
     * 微信登录
     */
    public static final String OPENID = "openid";

    /**
     * 退出登录时的状态
     */
    private static final String STATE = "logoutState";

    /**
     * 保存登陆用户信息
     *
     * @param loginInfo
     */
    public static void put(LoginInfoEntity loginInfo) {

        PrefCache.putData(LOGIN, loginInfo.getLogin());
        PrefCache.putData(PASSWORD, loginInfo.getPassword());
        PrefCache.putData(OPENID, loginInfo.getOpenid());
        LoginInfoCache.loginInfo = loginInfo;
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public static LoginInfoEntity get() {
        if(loginInfo == null) {
            loginInfo = new LoginInfoEntity();
            loginInfo.setLogin((String) PrefCache.getData(LOGIN, ""));
            loginInfo.setPassword((String) PrefCache.getData(PASSWORD, ""));
            loginInfo.setOpenid((String) PrefCache.getData(OPENID, ""));
        }
        return loginInfo;

    }

    public static void resetLogoutState() {
        get().setLogoutState(0);
        PrefCache.removeData(STATE);
    }

    public static void setLogoutState(int state) {
        get().setLogoutState(state);
        PrefCache.putData(STATE, state);
    }

    public static void clear() {

        PrefCache.removeData(PASSWORD);
        PrefCache.removeData(OPENID);
        loginInfo = null;
    }
}
