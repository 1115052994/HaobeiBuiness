package com.liemi.basemall.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.liemi.basemall.R;
import com.liemi.basemall.data.MallConstant;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.XGCustomEntity;
import com.liemi.basemall.data.entity.XGNotifyEntity;
import com.liemi.basemall.data.event.BackToAppEvent;
import com.liemi.basemall.data.event.LoginEvent;
import com.liemi.basemall.databinding.ActivityMallMainBinding;
import com.liemi.basemall.ui.category.CategoryFragment;
import com.liemi.basemall.ui.home.HomePageFragment;
import com.liemi.basemall.ui.personal.MessageActivity;
import com.liemi.basemall.ui.personal.MineFragment;
import com.liemi.basemall.ui.shopcar.ShoppingFragment;
import com.liemi.basemall.ui.store.StoreFragment;
import com.liemi.basemall.utils.PushUtils;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.OssConfigureEntity;
import com.netmi.baselibrary.data.entity.PlatformEntity;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.data.event.SwitchTabEvent;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.StatusBarUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.tencent.bugly.beta.Beta;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;

import static com.netmi.baselibrary.router.BaseRouter.App_MainActivity;

@Route(path = App_MainActivity)
public class MainActivity extends BaseActivity<ActivityMallMainBinding> implements CompoundButton.OnCheckedChangeListener {

    private CompoundButton selectView;

    @Override
    public void setBarColor() {
        //由Fragment去控制状态栏
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_mall_main;
    }

    @Override
    protected void initUI() {
        mBinding.setCheckListener(this);
        mBinding.rbHome.setTag(HomePageFragment.TAG);
        mBinding.rbCategory.setTag(CategoryFragment.TAG);
        mBinding.rbStore.setTag(StoreFragment.TAG);
        mBinding.rbShop.setTag(ShoppingFragment.TAG);
        mBinding.rbMine.setTag(MineFragment.TAG);
        mBinding.executePendingBindings();
        mBinding.rbHome.setChecked(true);

        doInitOssConfigure();

        doPlatformHelper();

        //检查热更新； @param isManual  用户手动点击检查，非用户点击操作请传false，isSilence true为不弹窗提示
        Beta.checkUpgrade(false, true);
        XGPushManager.setNotifactionCallback(new XGPushNotifactionCallback() {
            @Override
            public void handleNotify(XGNotifaction xgNotifaction) {
                Logs.i("PUSH_MESSAGE:" + xgNotifaction.toString() + "," + xgNotifaction.getContent() + "," + xgNotifaction.getTitle());
                int status = 0;
                try {
                    XGCustomEntity customEntity = new Gson().fromJson(xgNotifaction.getCustomContent(), XGCustomEntity.class);
                    XGNotifyEntity notifyEntity = new Gson().fromJson(customEntity.getWrap(), XGNotifyEntity.class);
                    status = notifyEntity.getData().getContent().getStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Logs.e("handleNotify", xgNotifaction.getCustomContent() + "，" + status);

                doNotify(xgNotifaction, status);

            }
        });

        initXgPush();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        int status = AppConfigCache.get().getStatus();
        if (status > 0) {
            switch (status) {
                default:
                    JumpUtil.overlay(this, getNotifyClass(status));
                    break;
            }
            AppConfigCache.setStatus(0);
        }
    }

    private Class<? extends Activity> getNotifyClass(int status) {
        switch (status) {
            default:
                return MessageActivity.class;
        }
    }

    private void doNotify(XGNotifaction xgNotifaction, int status) {
        //如果APP在前台显示，意图跳转到对应Activity，
        //否则先打开MainActivity， 再通过缓存状态，在onResume中取值打开对应Activity
        boolean isAppForeground = AppUtils.isAppForeground();

        Intent intent = new Intent(this, isAppForeground ? getNotifyClass(status) : MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this
                , (int) SystemClock.uptimeMillis()
                , intent
                , PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            ToastUtils.showShort("没有通知栏权限");
            return;
        }

        //重点：先创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(getString(R.string.app_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription(xgNotifaction.getContent());
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }
        //再创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.app_name));

        //设置通知栏大图标，上图中右边的大图
        builder.setLargeIcon(BitmapFactory.decodeResource(
                getResources(), R.mipmap.app_logo))
                // 设置状态栏和通知栏小图标
                .setSmallIcon(R.mipmap.app_logo)
                // 设置通知栏应用名称
                .setTicker(getString(R.string.app_name))
                // 设置通知栏显示时间
                .setWhen(System.currentTimeMillis())
                // 设置通知栏标题
                .setContentTitle(xgNotifaction.getTitle())
                // 设置通知栏内容
                .setContentText(xgNotifaction.getContent())
                // 设置通知栏点击后是否清除，设置为true，当点击此通知栏后，它会自动消失
                .setAutoCancel(true)
                // 将Ongoing设为true 那么左滑右滑将不能删除通知栏
                .setOngoing(false)
                // 设置通知栏点击意图
                .setContentIntent(pendingIntent)
                // 铃声、闪光、震动均系统默认
                .setDefaults(Notification.DEFAULT_ALL)
                // 设置为public后，通知栏将在锁屏界面显示
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                // 从Android4.1开始，可以通过以下方法，设置通知栏的优先级，优先级越高的通知排的越靠前，
                // 优先级低的，不会在手机最顶部的状态栏显示图标
                // 设置优先级为PRIORITY_MAX，将会在手机顶部显示通知栏
                .setPriority(NotificationCompat.PRIORITY_MIN);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void initXgPush() {
        //注册信鸽推送
        PushUtils.registerPush();

        if (!Strings.isEmpty(AccessTokenCache.get().getToken())) {  //主动获取用户状态
            doGetUserInfo();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.rb_shop || buttonView.getId() == R.id.rb_mine) {
                if (!MApplication.getInstance().checkUserIsLogin()) {
                    buttonView.setChecked(false);
                    return;
                }
            }
            showFragment(buttonView);
        } else {
            hideFragment(buttonView);
        }
    }


    private void showFragment(CompoundButton curView) {
        if (selectView != null) {
            selectView.setChecked(false);
        }
        selectView = curView;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String tag = (String) curView.getTag();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = Fragment.instantiate(this, tag);
            fragmentTransaction.add(R.id.fl_content, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            boolean isHide = fragment.isHidden();
            if (isHide) {
                fragmentTransaction.show(fragment);
                fragmentTransaction.commitAllowingStateLoss();
                fragment.onResume(); //切换Fragment，实时刷新数据
            }
        }

        if (fragment instanceof HomePageFragment) {

        } else if (fragment instanceof MineFragment) {
            if (MallConstant.systemType > 0) {
                StatusBarUtil.StatusBarDarkMode(getActivity(), MallConstant.systemType);
            }
        } else {
            MallConstant.systemType = StatusBarUtil.StatusBarLightMode(getActivity());
        }
    }

    private void hideFragment(CompoundButton lastView) {
        if (lastView != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            String tag = (String) lastView.getTag();
            if (fragmentManager.findFragmentByTag(tag) != null) {
                boolean isHide = fragmentManager.findFragmentByTag(tag).isHidden();
                if (!isHide) {
                    fragmentTransaction.hide(fragmentManager.findFragmentByTag(tag));
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }
        }
    }


    /**
     * 切换Tab
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchTab(SwitchTabEvent event) {
        if (event.rbId == R.id.rb_home
                || event.rbId == R.id.rb_category
                || event.rbId == R.id.rb_store
                || event.rbId == R.id.rb_shop
                || event.rbId == R.id.rb_mine) {
            ((RadioButton) findViewById(event.rbId)).setChecked(true);
        }
    }

    /**
     * 登录初始化
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        initXgPush();
    }

    private void doInitOssConfigure() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .getOssConfigure(0)
                .compose(RxSchedulers.<BaseData<OssConfigureEntity>>compose())
                .compose((this).<BaseData<OssConfigureEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<OssConfigureEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<OssConfigureEntity> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            if (data.getData() != null) {
                                OssUtils.initConfigure(data.getData());
                            } else {
                                ToastUtils.showShort("no data");
                            }
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    //请求平台客服
    private void doPlatformHelper() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .getPlatformInfo("")
                .compose(this.<BaseData<PlatformEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<PlatformEntity>>compose())
                .subscribe(new BaseObserver<BaseData<PlatformEntity>>() {
                    @Override
                    public void onNext(BaseData<PlatformEntity> data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            if (data.getData() != null) {
                                AppConfigCache.get().setPlatformEntity(data.getData());
                            }
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });
    }

    private void doGetUserInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getUserInfo(0)
                .compose(RxSchedulers.<BaseData<UserInfoEntity>>compose())
                .compose((this).<BaseData<UserInfoEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        UserInfoCache.put(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new BackToAppEvent());
        finish();
    }
}