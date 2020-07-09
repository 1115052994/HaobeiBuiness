package com.netmi.workerbusiness.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.entity.BannerJumpEntity;
import com.netmi.workerbusiness.databinding.ActivitySplashBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    private boolean isIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initUI() {
        // 后台返回时可能启动这个页面 http://blog.csdn.net/jianiuqi/article/details/54091181
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
    }

    @Override
    protected void initData() {
        doGetAdvertising();
        Log.e("weng1", "splashActivity");
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_jump) {
            disposable();
            toMainActivity();
        }
    }

    private void disposable() {
        if (countdownDisposable != null) {
            countdownDisposable.dispose();
            countdownDisposable = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        disposable();
    }


    private void toMainActivity() {
        new Handler().postDelayed(() -> {
            if (isIn) {
                return;
            }
            if (MApplication.getInstance().checkUserIsLogin()) {
                JumpUtil.overlay(getContext(), MainActivity.class);
                Log.e("weng2", "splashActivity");
            }
            Log.e("weng3", "splashActivity");
            finish();
            isIn = true;
        }, 1000L);
    }

    private Disposable countdownDisposable;

    private void countdown(int time) {
        if (time <= 0) {
            toMainActivity();
            return;
        }
        mBinding.tvJump.setVisibility(View.VISIBLE);
        setTextTimeCount(time);

        final int countTime = time;
        Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .map((aLong) -> countTime - aLong.intValue())
                .take(countTime + 1)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        countdownDisposable = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        setTextTimeCount(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        toMainActivity();
                    }
                });
    }

    private void setTextTimeCount(int time) {
        mBinding.tvJump.setText((time + "跳过"));
    }

    private void doGetAdvertising() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .getAdvertising("2")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<BannerEntity>>() {
                    @Override
                    public void onSuccess(BaseData<BannerEntity> data) {
                        if (dataExist(data)) {
                            GlideShowImageUtils.displayNetImage(getContext(), data.getData().getImg_url(), mBinding.ivBg);
                            mBinding.ivLogo.setVisibility(View.GONE);

                            int time = Strings.toInt(data.getData().getRemark());
                            countdown(time);
                            mBinding.ivBg.setOnClickListener(v -> {
                                toMainActivity();
                                new BannerJumpEntity().toJump(getContext(), data.getData());
                            });
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mBinding.tvJump.getVisibility() == View.GONE) {
                            toMainActivity();
                        }
                    }
                });
    }

}
