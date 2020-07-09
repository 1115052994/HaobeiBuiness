package com.liemi.basemall.ui.personal.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ActivityPasswordManagerBinding;
import com.liemi.basemall.ui.login.ForgetPassActivity;
import com.liemi.basemall.ui.login.SetPayPasswordActivity;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;

import static com.netmi.baselibrary.router.BaseRouter.App_MineOrderActivity;
import static com.netmi.baselibrary.router.BaseRouter.App_SetchargePasswrodActivity;

public class PasswordManagerActivity extends BaseActivity<ActivityPasswordManagerBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_password_manager;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("密码管理");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_change_trade_password) {
            //点击修改资产密码
            //首先判断是否设置资产密码
            if(UserInfoCache.get() == null){
                MApplication.getInstance().gotoLogin();
                return;
            }
            if(UserInfoCache.get().getIs_set_paypassword() != UserInfoEntity.BIND){
                showError("请先设置资产密码");
//                JumpUtil.overlay(this,SetPayPasswordActivity.class);
                ARouter.getInstance().build(App_SetchargePasswrodActivity).navigation();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt(ChangePasswordActivity.CHANGE_PASSWORD_TYPE, ChangePasswordActivity.CHANGE_TRADE_PASSWORD);
            JumpUtil.overlay(this, ChangePasswordActivity.class, bundle);

            return;
        }
        if (view.getId() == R.id.tv_forget_trade_password) {
            if(UserInfoCache.get() == null){
                MApplication.getInstance().gotoLogin();
                return;
            }
            if(UserInfoCache.get().getIs_set_paypassword() != UserInfoEntity.BIND){
                showError("请先设置资产密码");
//                JumpUtil.overlay(this,SetPayPasswordActivity.class);
                ARouter.getInstance().build(App_SetchargePasswrodActivity).navigation();
                return;
            }
            //点击忘记交易密码
            Bundle bundle = new Bundle();
            bundle.putInt(ChangePasswordActivity.CHANGE_PASSWORD_TYPE, ChangePasswordActivity.CHANGE_TRADE_PASSWORD);
            JumpUtil.overlay(this, ForgetPassActivity.class, bundle);
            return;
        }

        if (view.getId() == R.id.tv_change_login_password) {
            //点击修改登录密码
            Bundle bundle = new Bundle();
            bundle.putInt(ChangePasswordActivity.CHANGE_PASSWORD_TYPE, ChangePasswordActivity.CHANGE_LOGIN_PASSWORD);
            JumpUtil.overlay(this, ChangePasswordActivity.class, bundle);
            return;
        }
        if (view.getId() == R.id.tv_forget_login_password) {
            //点击忘记登录密码
            Bundle bundle = new Bundle();
            bundle.putInt(ChangePasswordActivity.CHANGE_PASSWORD_TYPE, ChangePasswordActivity.CHANGE_LOGIN_PASSWORD);
            JumpUtil.overlay(this, ForgetPassActivity.class, bundle);
            return;

        }

    }
}
