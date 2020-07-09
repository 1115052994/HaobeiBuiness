package com.netmi.baselibrary.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.netmi.baselibrary.widget.MLoadingDialog;
import com.trello.rxlifecycle2.components.support.RxFragment;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/6 14:46
 * 修改备注：
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends RxFragment implements BaseView, View.OnClickListener {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTAG(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mBinding == null)
            mBinding = DataBindingUtil.inflate(inflater, getContentView(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (basePresenter != null) {
            basePresenter.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (basePresenter != null) {
            basePresenter.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (basePresenter != null) {
            basePresenter.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (basePresenter != null) {
            basePresenter.destroy();
        }
        if (xRecyclerView != null) {
            xRecyclerView.destroy();
            xRecyclerView = null;
        }
    }

    /**
     * 日志TAG初始化
     *
     * @param fragment UI
     */
    protected void initTAG(Fragment fragment) {
        TAG = fragment.getClass().getSimpleName();
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


    @Override
    public void showProgress(String message) {
        MLoadingDialog.show(getActivity(), message);
    }

    @Override
    public void hideProgress() {
        MLoadingDialog.dismiss();
    }

    public void showProgress(@StringRes int message) {
        showProgress(getString(message));
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

    @Override
    public void onClick(View view) {

    }

}
