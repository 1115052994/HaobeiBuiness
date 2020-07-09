package com.netmi.baselibrary.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.netmi.baselibrary.R;
import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.language.MultiLanguageUtil;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.netmi.baselibrary.widget.MLoadingDialog;
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
    protected
    MyXRecyclerView xRecyclerView;

    protected BaseActivity instance;

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
        if (view.getId() == R.id.ll_back) {
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
}
