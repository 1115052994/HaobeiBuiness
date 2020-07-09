package com.liemi.basemall.ui.personal.setting;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ActivitySettingBinding;
import com.liemi.basemall.ui.NormalDialog;
import com.liemi.basemall.ui.personal.address.AddressManageActivity;
import com.liemi.basemall.utils.PushUtils;
import com.netmi.baselibrary.data.event.SwitchTabEvent;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.CacheUtils;
import com.netmi.baselibrary.utils.CleanUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.SPs;
import com.tencent.android.tpush.XGIOperateCallback;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
public class SettingActivity extends BaseActivity<ActivitySettingBinding> {
    //用户退出登录的提示
    private NormalDialog mLogoutConfirmDialog;
    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }
    @Override
    protected void initUI() {
        getTvTitle().setText("设置");
        mBinding.switchButton.setChecked(SPs.isOpenPushStatus());
        mBinding.switchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        showProgress("");
                        if (mBinding.switchButton.isChecked()) {
                            Logs.i("取消注册");
                            //取消注册
                            PushUtils.unRegisterPush(new XGIOperateCallback() {
                                @Override
                                public void onSuccess(Object o, int i) {
                                    Logs.i("PUSH_MESSAGE   取消注册成功");
                                    SPs.setPushStatus(false);
                                    mBinding.switchButton.setChecked(false);
                                    hideProgress();
                                }

                                @Override
                                public void onFail(Object o, int i, String s) {
                                    Logs.i("PUSH_MESSAGE   取消注册失败:" + s);
                                    SPs.setPushStatus(true);
                                    mBinding.switchButton.setChecked(true);
                                    hideProgress();
                                }
                            });
                        } else {
                            Logs.i("开始注册");
                            //开始注册：
                            PushUtils.registerPush(new XGIOperateCallback() {
                                @Override
                                public void onSuccess(Object o, int i) {
                                    Logs.i("PUSH_MESSAGE   注册成功");
                                    SPs.setPushStatus(true);
                                    mBinding.switchButton.setChecked(true);
                                    hideProgress();
                                }

                                @Override
                                public void onFail(Object o, int i, String s) {
                                    Logs.i("PUSH_MESSAGE   注册失败:" + i + "," + s);
                                    SPs.setPushStatus(false);
                                    mBinding.switchButton.setChecked(false);
                                    hideProgress();
                                }
                            });

                        }
                        break;

                }
                return true;
            }
        });
    }
    @Override
    protected void initData() {
        //设置APP版本号
        mBinding.tvAppVersion.setText("V "+AppUtils.getAppVersionName());
        //设置缓存大小
        setCacheSize();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.ll_trade_password_manager) {
            //点击交易密码管理
            JumpUtil.overlay(this, PasswordManagerActivity.class);
            return;
        }
        if (view.getId() == R.id.ll_common_question) {
            //点击常见问题
            JumpUtil.overlay(this, CommonQuestionActivity.class);
            return;
        }
        if (view.getId() == R.id.ll_suggestion_feedback){
            //点击意见反馈
            JumpUtil.overlay(this, SuggestionFeedbackActivity.class);
            return;
        }
        if (view.getId() == R.id.ll_about_us) {
            //点击关于我们
            JumpUtil.overlay(this, AboutUsWebActivity.class);
            return;
        }
        if (view.getId() == R.id.ll_clear_cache) {
            //点击清除缓存
            if (CleanUtils.cleanInternalCache() && CleanUtils.cleanExternalCache()) {
                showError("缓存清除成功");
                mBinding.setCacheNum("0B");
            } else {
                showError("缓存清理失败，请重试");
            }
            return;
        }
        if (view.getId() == R.id.tv_exit_account) {
            //点击退出账号
            showLogoutDialog();
            return;
        }
        if(view.getId() == R.id.ll_address_manager){
            JumpUtil.overlay(getActivity(),AddressManageActivity.class);
            return;
        }

    }
    private void setCacheSize() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> ex) {
                try {
                    ex.onNext(CacheUtils.getInstance().getTotalCacheSize(getContext()));
                    ex.onComplete();
                } catch (Exception e) {

                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Logs.i("获取到缓存大小：" + s);
                        if (!TextUtils.isEmpty(s)) {
                            mBinding.setCacheNum(s);
                        } else {
                            mBinding.setCacheNum("0B");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    //显示询问用户是否退出登录
    private void showLogoutDialog() {
        if (mLogoutConfirmDialog == null) {
            mLogoutConfirmDialog = new NormalDialog(this);
        }
        if (!mLogoutConfirmDialog.isShowing()) {
            mLogoutConfirmDialog.show();
        }

        mLogoutConfirmDialog.setMessageInfo(getString(R.string.confirm_logout), true, 16, true);
        mLogoutConfirmDialog.setTitleInfo("", false);
        mLogoutConfirmDialog.showLineTitleWithMessage(false);
        mLogoutConfirmDialog.setCancelInfo("取消", true);
        mLogoutConfirmDialog.setConfirmInfo("确定");
        mLogoutConfirmDialog.setClickConfirmListener(new NormalDialog.ClickConfirmListener() {
            @Override
            public void clickConfirm() {
                MApplication.getInstance().gotoLogin();
                mLogoutConfirmDialog.dismiss();
                EventBus.getDefault().post(new SwitchTabEvent(R.id.rb_home));
                finish();
            }
        });

    }

}
