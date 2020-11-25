package com.netmi.workerbusiness.ui.login;

import android.text.TextUtils;
import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.ActivityVerificationThreeBinding;
import com.netmi.workerbusiness.ui.utils.SobotApiUtils;

public class VerificationActivity extends BaseActivity<ActivityVerificationThreeBinding> {

    private ShopInfoEntity shopInfoEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_verification_three;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("邀请码");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_login_three) {
            String code = mBinding.etSmsCode.getText().toString();
            if (TextUtils.isEmpty(code)) {
                ToastUtils.showShort("请输入推荐码");
                return;
            }
            JumpUtil.overlay(getContext(), RegisterActivity.class, "code", code);
        }
        if (view.getId() == R.id.tv_service_three) {
            kefu();
        }
    }

    private void kefu() {
        SobotApiUtils.getInstance().toCustomServicePage(getContext(), shopInfoEntity, null);
    }

}
