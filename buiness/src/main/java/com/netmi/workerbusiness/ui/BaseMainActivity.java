package com.netmi.workerbusiness.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.CompoundButton;

import com.example.voicebroadcast.UtilsVoice;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.igexin.sdk.PushManager;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.service.XyPushIntentService;
import com.netmi.workerbusiness.service.XyPushService;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.netmi.baselibrary.data.Constant.IS_PUSH;
import static com.netmi.baselibrary.data.Constant.PUSH_PREFIX;

/**
 * 类描述：
 * 创建人：Sherlock
 * 创建时间：2019/3/6
 * 修改备注：
 */
public abstract class BaseMainActivity<T extends ViewDataBinding> extends BaseActivity<T> implements CompoundButton.OnCheckedChangeListener {
    protected static CompoundButton selectView;


    protected boolean canRegisterEventBus() {
        return false;
    }

    @Override
    public void initContent() {
        initDefault();
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        initUI();
        initTAG(this);
        if (MApplication.getInstance().checkUserIsLogin()) {
//            //初始化信鸽配置
//            XGPushManager.setNotifactionCallback(xgNotifaction -> {
//                doNotify(xgNotifaction);
//                onNotify(xgNotifaction);
//            });
//            initXgPush();
            //个推初始化
            PushManager.getInstance().initialize(getApplicationContext(), XyPushService.class);
            // XyPushIntentService 为第三方自定义的推送服务事件接收类
            PushManager.getInstance().registerPushIntentService(getApplicationContext(), XyPushIntentService.class);


            //讯飞初始化
            SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5f51a4d2");
            UtilsVoice.init(BaseMainActivity.this);

            initData();
        }
        //检查更新
        Beta.checkUpgrade(false, false);
    }

    protected abstract void onNotify(XGNotifaction xgNotifaction);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (canRegisterEventBus())
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (canRegisterEventBus())
            EventBus.getDefault().unregister(this);
        if (appexitTask != null)
            appexitTask.dispose();

        UtilsVoice.dis();

    }



    /**
     * 信鸽推送注册
     */
    protected void initXgPush() {
        Logs.e("initXgPush", AccessTokenCache.get().toString());
        if (SPs.getBoolean(this, IS_PUSH, true)) {
            if (!TextUtils.isEmpty(AccessTokenCache.get().getToken())) {
                XGPushManager.bindAccount(getApplicationContext(), Constant.getPushPrefix() + AccessTokenCache.get().getUid(), xgiOperateCallback);
                Logs.e("TPush", "注册uid：" + PUSH_PREFIX + AccessTokenCache.get().getUid());
            } else {
                XGPushManager.registerPush(getApplicationContext(), xgiOperateCallback);
            }
        }
    }

    XGIOperateCallback xgiOperateCallback = new XGIOperateCallback() {
        @Override
        public void onSuccess(Object data, int flag) {
            //token在设备卸载重装的时候有可能会变
            Logs.e("TPush", "注册成功，设备token为：" + data);
        }

        @Override
        public void onFail(Object data, int errCode, String msg) {
            Logs.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
        }
    };

    protected void doNotify(XGNotifaction xgNotifaction) {
        //如果APP在前台显示，意图跳转到对应Activity，
        //否则先打开MainActivity， 再通过缓存状态，在onResume中取值打开对应Activity
        boolean isAppForeground = AppUtils.isAppForeground();

        Intent intent = new Intent(this, isAppForeground ? MainActivity.class : SplashActivity.class);

        AppConfigCache.setStatus(isAppForeground ? 0 : 1);

        PendingIntent pendingIntent = PendingIntent.getActivity(this
                , (int) SystemClock.uptimeMillis()
                , intent
                , PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            ToastUtils.showShort(getString(R.string.not_granted_permission_notification));
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

    /**
     * 防退出误操作
     */
    boolean canFinish = false;
    protected Disposable appexitTask;

    @Override
    public void onBackPressed() {
        if (canFinish) {
            MApplication.getInstance().appManager.AppExit(this);
        } else {
            canFinish = true;
            showError(R.string.quickly_click_twice_to_exit_the_app);

            appexitTask = Observable.timer(3, TimeUnit.SECONDS).subscribe((aLong) -> {
                        canFinish = false;
                        if (canFinish)
                            showError(canFinish + "");
                    }
            );
        }
    }

    /**
     * 切换fragment部分
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            showFragment(buttonView);
        } else {
            hideFragment(buttonView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = (String) selectView.getTag();
        setStatusBarOnFragmentChange(tag, fragmentManager.findFragmentByTag(tag));
    }

    protected void showFragment(CompoundButton curView) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String tag = (String) curView.getTag();
        if (setStatusBarOnFragmentChange(tag, fragmentManager.findFragmentByTag(tag))) {
            curView.setChecked(false);
            return;
        }
        if (selectView != null) {
            selectView.setChecked(false);
        }
        selectView = curView;
        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentTransaction.add(R.id.fl_content, Fragment.instantiate(this, tag), tag);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            boolean isHide = fragmentManager.findFragmentByTag(tag).isHidden();
            if (isHide) {
                fragmentTransaction.show(fragmentManager.findFragmentByTag(tag));
                fragmentTransaction.commitAllowingStateLoss();
                fragmentManager.findFragmentByTag(tag).onResume(); //切换Fragment，实时刷新数据
            }
        }
    }

    /**
     * 切换fragment时，修改statusBar
     * 返回true拦截，不执行切换fragment动作
     */
    protected abstract boolean setStatusBarOnFragmentChange(String tag, Fragment fragment);

    protected void hideFragment(CompoundButton lastView) {
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


}
