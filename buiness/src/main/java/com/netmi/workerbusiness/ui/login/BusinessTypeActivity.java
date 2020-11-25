package com.netmi.workerbusiness.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioGroup;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.LoginApi;
import com.netmi.workerbusiness.databinding.ActivityBusinessTypeBinding;
import com.netmi.workerbusiness.ui.MainActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class BusinessTypeActivity extends BaseIMLoginActivity<ActivityBusinessTypeBinding> {

    private String type = "2";
    private int shop_user_type;//用户选择商户类型 0:未选择类型 1:线上 2:线下 3:线上+线下
    @Override
    protected int getContentView() {
        return R.layout.activity_business_type;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("选择入住类型");
        mBinding.tvConfirm.setText("下一步");

        mBinding.rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_line) {
                    type = "2";
                } else if (i == R.id.rb_out_line) {
                    type = "1";
                }
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_confirm) {
            chooseType(type);
        }
    }


    //选择店铺类型
    private void chooseType(String type) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .chooseType(type)
                .compose(RxSchedulers.compose())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(@NonNull BaseData data) {
                        if (UserInfoCache.get().getShop_apply_status().equals("0")) {
                            Bundle args = new Bundle();
                            args.putString(JumpUtil.VALUE, type);
                           if (type.equals("1")) {//线上：线上商家
                                JumpUtil.overlay(getContext(),PersonalOfflineInfoActivity .class,args);
                            } else if (type.equals("2")) {//线下:实体商家
                               JumpUtil.overlay(getContext(), PersonalOfflineInfoRestrictionsActivity.class,args);
                            }
                        } else {
//                            loginYunXin(UserInfoCache.get().getToken());
                            JumpUtil.overlay(getContext(), MainActivity.class);
                            AppManager.getInstance().finishAllActivity(MainActivity.class);
                        }
                    }
                });
    }

}

