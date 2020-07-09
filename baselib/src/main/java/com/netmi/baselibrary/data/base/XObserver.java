package com.netmi.baselibrary.data.base;

import android.app.Activity;

import com.netmi.baselibrary.R;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseView;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.ToastUtils;

/**
 * 类描述：对服务器返回错误码进行统一处理
 * 创建人：Sherlock
 * 创建时间：2019/1/28
 * 修改备注：
 */
public abstract class XObserver<T extends BaseData> extends BaseObserver<T> {


    /**
     * token empty
     */
    private static final int TOKEN_EMPTY = 10000;

    /**
     * token out
     */
    private static final int TOKEN_OUT = 10001;
    /**
     *
     */
    private static final int TOKEN_END = 10002;
    /**
     * 异地登陆
     */
    private static final int TOKEN_REFRESH = 10004;

    private BaseView context;

    public XObserver(BaseView context) {
        this.context = context;
    }

    public XObserver() {
        Activity act = AppManager.getInstance().currentActivity();
        if (act instanceof BaseView) {
            context = (BaseView) act;
        }
    }

    @Override
    public void onNext(T t) {
        try {
            if (t.getErrcode() == TOKEN_OUT || t.getErrcode() == TOKEN_EMPTY) {
                ToastUtils.showShort(R.string.the_certificate_expires_please_relogin);
                MApplication.getInstance().gotoLogin();
            } else if (t.getErrcode() == TOKEN_REFRESH) {
                ToastUtils.showShort(R.string.your_account_is_logged_in_elsewhere_please_log_in_again);
                MApplication.getInstance().gotoLogin();
            } else if (t.getErrcode() == Constant.SUCCESS_CODE) {
                onSuccess(t);
            } else {
                onFail(t);
            }
        } catch (Exception e) {
            Logs.e(e);
        }
    }

    public void onFail(T data) {
        ToastUtils.showShort(data.getErrmsg());
    }

    public abstract void onSuccess(T data);

    protected boolean dataExist(T data) {
        if (data.getData() == null) {
            ToastUtils.showShort(R.string.baselib_not_data);
            return false;
        }
        return true;
    }

    @Override
    public void onComplete() {
        try {
            context.hideProgress();
        } catch (Exception e) {
            Logs.e(e);
        }
    }

    @Override
    protected void onError(ApiException ex) {
        try {
            context.showError(ex.getMessage());
            Logs.e("errcode = " + ex.getStatuCode() + "\nerrMess = " + ex.getMessage());
        } catch (Exception e) {
            Logs.e(e);
        }
    }
}
