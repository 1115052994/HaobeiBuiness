package com.netmi.baselibrary.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.MLoadingDialog;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/6 14:46
 * 修改备注：
 */
public abstract class BaseDialogFragment<T extends ViewDataBinding> extends DialogFragment implements BaseView, View.OnClickListener, LifecycleProvider<FragmentEvent> {

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
    XRecyclerView xRecyclerView;

    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
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
        if (basePresenter != null) {
            basePresenter.pause();
        }
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        if (basePresenter != null) {
            basePresenter.stop();
        }
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (basePresenter != null) {
            basePresenter.resume();
        }
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onDestroy() {
        if (basePresenter != null) {
            basePresenter.destroy();
        }
        if (xRecyclerView != null) {
            xRecyclerView.destroy();
            xRecyclerView = null;
        }
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }


    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
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

    @Override
    public void showError(String message) {
        hideProgress();
        ToastUtils.showShort(message);
    }

    @Override
    public void onClick(View view) {

    }
}
