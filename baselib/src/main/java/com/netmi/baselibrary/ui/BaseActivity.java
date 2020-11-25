package com.netmi.baselibrary.ui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.netmi.baselibrary.R;
import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.dialog.MessagesHintDialog;
import com.netmi.baselibrary.utils.language.MultiLanguageUtil;
import com.netmi.baselibrary.widget.MLoadingDialog;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * 类描述：Activity 基类
 * 创建人：Simple
 * 创建时间：2017/9/4 17:47
 * 修改备注：
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends RxAppCompatActivity implements BaseView {

    /**
     * 日志使用TAG
     */
    protected String TAG;

    /**
     * 业务基类
     */
    protected BasePresenter basePresenter;

    /**
     * dataBinding
     */
    protected T mBinding;

    /**
     * 需要在子类中初始化
     */
    protected MyXRecyclerView xRecyclerView;

    protected BaseActivity instance;

    protected MessagesHintDialog mMessageHintDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mBinding = DataBindingUtil.setContentView(this, getContentView());

        setBarColor();
        initContent();
    }

    //国际化
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    public void initContent() {
        initDefault();
        initUI();
        initData();
        initTAG(this);
    }

    //默认初始化
    protected void initDefault() {
        fullScreen();
    }


    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(this, true);
    }

    //设置标题栏颜色
    public void setBarColor(int color) {
        ImmersionBarUtils.colorStatusBar(this, true, color);
    }

    //布局置顶，状态栏字体显示浅色
    public void fullScreen() {
//        ImmersionBar.with(this).reset().init();
        fullScreen(true);
    }

    //布局置顶，状态栏字体显示深色
    public void fullScreen(boolean isDark) {
        ImmersionBar.with(this).reset().statusBarDarkFont(isDark).init();
    }
//    public void setBarColor() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
//        StatusBarUtil.StatusBarLightMode(this);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (basePresenter != null) {
            basePresenter.destroy();
        }
        if (xRecyclerView != null) {
            xRecyclerView.destroy();
            xRecyclerView = null;
        }
        ImmersionBar.with(this).destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (basePresenter != null) {
            basePresenter.resume();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            checkNotificationEnabled();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (basePresenter != null) {
            basePresenter.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (basePresenter != null) {
            basePresenter.stop();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkNotificationEnabled() {
        boolean isEnabled = isNotificationEnabled(this);
        Log.i(TAG, "is notification enabled: " + isEnabled);
        if (!isEnabled) {
            showMissingPermissionDialog(1);
        }
    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }



    //显示对话框
    protected void showMessageHintDialog(final String message){
        hideProgress();
        if(mMessageHintDialog == null){
            mMessageHintDialog = new MessagesHintDialog(this,message);
        }else {
            mMessageHintDialog.showMessage(message);
        }
        if(!mMessageHintDialog.isShowing()){
            mMessageHintDialog.show();
        }
    }

    /**
     * 日志TAG初始化
     *
     * @param context 上下文对象
     */
    protected void initTAG(Context context) {
        TAG = context.getClass().getSimpleName();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * 获取布局
     */
    protected abstract int getContentView();

    /**
     * 界面初始化
     */
    protected abstract void initUI();

    /**
     * 数据初始化
     */
    protected abstract void initData();

    public Context getContext() {
        return instance;
    }

    public BaseActivity getActivity() {
        return instance;
    }
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;
    private static final int PERMISSON_REQUESTCODE = 0;
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog(0);
                isNeedCheck = false;
            }
        }
    }

    /**
     * 检测是否所有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     * type:0.普通权限 1.消息通知权限
     */
    private void showMissingPermissionDialog(int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(type == 0 ? "当前应用缺少必要权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限。"
                : "检测到您的通知权限已关闭，暂无法接收通知消息，请前往开启");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(type);
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }


    @Override
    public void showProgress(String message) {
        MLoadingDialog.show(this, message);
    }

    public void showProgress(@StringRes int message) {
        showProgress(getString(message));
    }

    @Override
    public void hideProgress() {
        MLoadingDialog.dismiss();
    }

    @Override
    public void showError(String message) {
        hideProgress();
        ToastUtils.showShort(message);

    }

    public void showError(@StringRes int message) {
        hideProgress();
        ToastUtils.showShort(message);
    }

    public void showError(@StringRes int message, boolean hideProgress) {
        if (hideProgress)
            hideProgress();
        ToastUtils.showShort(message);
    }

    public void doClick(View view) {
        if (view.getId() == R.id.ll_back ) {
            onBackPressed();
        }
    }

    /**
     * 获取标题控件
     **/
    public TextView getTvTitle() {
        return findViewById(R.id.tv_title);
    }

    public TextView getRightSetting() {
        return findViewById(R.id.tv_setting);
    }

    public ImageView getRightImage() {
        return findViewById(R.id.iv_setting);
    }

    public TextView getLeftSetting() {
        return findViewById(R.id.iv_back);
    }

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings(int type) {
        if (type == 0) {
            Intent intent = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= 26) {
                // android 8.0引导
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
            } else if (Build.VERSION.SDK_INT >= 21) {
                // android 5.0-7.0
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
            } else {
                // 其他
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }



}
