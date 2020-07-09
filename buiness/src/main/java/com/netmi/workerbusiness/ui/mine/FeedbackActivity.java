package com.netmi.workerbusiness.ui.mine;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MultiPictureSelectFragment;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.databinding.ActivityFeedbackBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

public class FeedbackActivity extends BaseActivity<ActivityFeedbackBinding> {
    private MultiPictureSelectFragment picFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("意见反馈");
        new InputListenView(mBinding.tvConfirm, mBinding.etContent, mBinding.etPhone) {
        };
        initPicFragment();
    }

    @Override
    protected void initData() {

    }

    private void initPicFragment() {
        //图片上传Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        picFragment = MultiPictureSelectFragment.newInstance(3, 3);
        fragmentTransaction.add(R.id.fl_content, picFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_confirm) {
            if (!picFragment.uploadOSS(urls -> doFeedBack(mBinding.etContent.getText().toString(), mBinding.etName.getText().toString(), mBinding.etPhone.getText().toString(), urls), null)) {
                doFeedBack(mBinding.etContent.getText().toString(), mBinding.etName.getText().toString(), mBinding.etPhone.getText().toString(), null);
            }
        }
    }


    private void doFeedBack(String content, String name, String phone, List<String> imgs) {
        RetrofitApiFactory.createApi(MineApi.class)
                .feedback(phone, content, name, imgs)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError(data.getErrmsg());
                        finish();
                    }
                });

    }


}
