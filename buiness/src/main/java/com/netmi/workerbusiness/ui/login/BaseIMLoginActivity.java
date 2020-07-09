package com.netmi.workerbusiness.ui.login;

import android.databinding.ViewDataBinding;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.entity.AccessToken;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.im.DemoCache;
import com.netmi.workerbusiness.im.config.preference.Preferences;
import com.netmi.workerbusiness.ui.MainActivity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/9/24
 * 修改备注：
 */
public abstract class BaseIMLoginActivity<T extends ViewDataBinding> extends BaseActivity<T> {

    //登录网易云信
    protected void loginYunXin(final AccessToken token) {
        NimUIKit.login(new com.netease.nimlib.sdk.auth.LoginInfo(token.getAccid(), token.getYunxin_token()), new RequestCallback<LoginInfo>() {

            @Override
            public void onSuccess(com.netease.nimlib.sdk.auth.LoginInfo param) {
                hideProgress();
                DemoCache.setAccount(param.getAccount());
                AccessTokenCache.put(token);
                Preferences.saveUserAccount(param.getAccount());
                Preferences.saveUserToken(param.getToken());
                JumpUtil.overlay(getContext(), MainActivity.class);
                AppManager.getInstance().finishAllActivity(MainActivity.class);
            }

            @Override
            public void onFailed(int code) {
                if (code == 302 || code == 404) {
                    ToastUtils.showShort("帐号或密码错误");
                } else {
                    ToastUtils.showShort("登录失败");
                }
                hideProgress();
            }

            @Override
            public void onException(Throwable exception) {
                ToastUtils.showShort("无效输入");
                hideProgress();
            }
        });
    }

}
