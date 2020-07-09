package com.netmi.workerbusiness.ui.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.ActivityChangePassBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

public class ChangePassActivity extends BaseActivity<ActivityChangePassBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_change_pass;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("修改密码");
        new InputListenView(mBinding.tvConfirm, mBinding.etNewPass) {
        };
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_confirm) {
            changePassword(mBinding.etNewPass.getText().toString());
        }
    }


    private void changePassword(String password) {
        RetrofitApiFactory.createApi(MineApi.class)
                .changePassword(password)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        finish();
                    }
                });
    }


}
