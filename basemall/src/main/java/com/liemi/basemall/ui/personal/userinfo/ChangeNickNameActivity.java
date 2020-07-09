package com.liemi.basemall.ui.personal.userinfo;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.databinding.ActivityChangeNickNameBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class ChangeNickNameActivity extends BaseActivity<ActivityChangeNickNameBinding> {

    //上个页面传递过来用户昵称的标志
    public static final String USER_NICK_NAME = "userNickName";

    private String mNickName;
    //记录是否修改成功
    private boolean isChangeSuccess = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_change_nick_name;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("修改昵称");
        //获取上个页面传递过来的昵称信息
        if (getIntent() != null && getIntent().getExtras() != null) {
            mNickName = getIntent().getExtras().getString(USER_NICK_NAME);
            mBinding.etInputNewNickName.setHint(mNickName);
        }
    }

    @Override
    protected void initData() {

    }


    @Override
    public void doClick(View view) {
        super.doClick(view);

        if (view.getId() == R.id.tv_save) {
            if (TextUtils.isEmpty(mBinding.etInputNewNickName.getText().toString())) {
                showError(getString(R.string.input_new_nick_name));
                return;
            }
            if (mBinding.etInputNewNickName.getText().toString().equals(mNickName)) {
                showError(getString(R.string.input_new_nick_name));
                return;
            }
            if (TextUtils.isEmpty(mBinding.etInputNewNickName.getText().toString().trim())) {
                showError(getString(R.string.nick_name_not_all_space));
                return;
            }
            if (mBinding.etInputNewNickName.getText().toString().startsWith(" ") || mBinding.etInputNewNickName.getText().toString().endsWith(" ")) {
                showError(getString(R.string.nick_name_not_with_space));
                return;
            }
            doUpdateUserInfo(mBinding.etInputNewNickName.getText().toString(), null, null, null);
            return;
        }
    }

    @Override
    public void onBackPressed() {
        if (isChangeSuccess) {
            Intent intent = new Intent();
            intent.putExtra("isChangeSuccess", isChangeSuccess);
            setResult(10001, intent);
        }
        finish();
    }

    private void doUpdateUserInfo(String nickName, String headImage, String sex, String birthday) {
        showProgress("");

        RetrofitApiFactory.createApi(MineApi.class)
                .doUserInfoUpdate( headImage,nickName, sex, birthday)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("修改成功");
                        isChangeSuccess = true;
                        onBackPressed();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });
    }
}
