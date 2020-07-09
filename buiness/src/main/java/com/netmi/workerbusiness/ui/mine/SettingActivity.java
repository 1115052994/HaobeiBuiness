package com.netmi.workerbusiness.ui.mine;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.liemi.basemall.ui.NormalDialog;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.cache.TelCache;
import com.netmi.workerbusiness.data.cache.WithdrawCache;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.utils.PushUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;

public class SettingActivity extends BaseActivity {
    private ShopInfoEntity shopInfoEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("设置");
    }

    @Override
    protected void initData() {
        doGetShopInfo();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_pass_change) {   //修改密码
//            JumpUtil.overlay(getContext(), ChangePassActivity.class);
            JumpUtil.overlay(getContext(), AccountSecurityActivity.class);
        } else if (id == R.id.tv_customer_service) {  //联系客服
//            /**
//             * 智齿客服
//             * */
//            showProgress("");
//            RetrofitApiFactory.createApi(MessApi.class)
//                    .getMessage("")
//                    .compose(RxSchedulers.compose())
//                    .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
//                    .subscribe(new XObserver<BaseData<CustomerEntity>>() {
//                        @Override
//                        public void onSuccess(BaseData<CustomerEntity> data) {
//                            hideProgress();
//                            //    IM       NimUIKit.startP2PSession(getContext(), data.getData().getName());
//                            SobotApiUtils.getInstance().toCustomServicePage(getContext(), shopInfoEntity, null, data.getData());
//                        }
//
//                        @Override
//                        public void onFail(BaseData<CustomerEntity> data) {
//                            showError(data.getErrmsg());
//                        }
//                    });
            showError(TelCache.get());
        } else if (id == R.id.tv_feedback) {    //意见反馈
            JumpUtil.overlay(getContext(), FeedbackActivity.class);
        } else if (id == R.id.tv_exit) {
            showLogoutDialog();
        } else if (id == R.id.tv_secret) {
            doAgreement(127);
        } else if (id == R.id.tv_service_two) {//点击服务条款
            doAgreement(111);
        } else if (id == R.id.tv_secret_two) {//点击隐私协议
            doAgreement(127);
        }
    }

    private NormalDialog mLogoutConfirmDialog;

    //显示询问用户是否退出登录
    private void showLogoutDialog() {

        if (mLogoutConfirmDialog == null) {
            mLogoutConfirmDialog = new NormalDialog(getContext());
        }

        if (!mLogoutConfirmDialog.isShowing()) {
            mLogoutConfirmDialog.show();
        }
        mLogoutConfirmDialog.setMessageInfo(getString(R.string.confirm_logout), true, 16, true);
        mLogoutConfirmDialog.setTitleInfo("", false);
        mLogoutConfirmDialog.showLineTitleWithMessage(false);
        mLogoutConfirmDialog.setCancelInfo(getString(R.string.cancel), true);
        mLogoutConfirmDialog.setConfirmInfo(getString(R.string.confirm));

        mLogoutConfirmDialog.setClickConfirmListener(() -> {
            //个推解绑
            PushUtils.unbindPush();
            WithdrawCache.clear();
            MApplication.getInstance().gotoLogin();
        });

    }

    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(com.netmi.workerbusiness.data.api.MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        shopInfoEntity = data.getData();
                    }
                });
    }


    //服务协议
    private void doAgreement(int type) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .getAgreement(type)
                .compose(this.<BaseData<AgreementEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<AgreementEntity>>compose())
                .subscribe(new BaseObserver<BaseData<AgreementEntity>>() {
                    @Override
                    public void onNext(BaseData<AgreementEntity> agreementEntityBaseData) {
                        if (agreementEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {
                            if (agreementEntityBaseData.getData() != null && agreementEntityBaseData.getData().getContent() != null && agreementEntityBaseData.getData().getTitle() != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString(WEBVIEW_TITLE, agreementEntityBaseData.getData().getTitle());
                                bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_CONTENT);
                                bundle.putString(WEBVIEW_CONTENT, agreementEntityBaseData.getData().getContent());
                                JumpUtil.overlay(getContext(), BaseWebviewActivity.class, bundle);
                            }
                        } else {
                            showError(agreementEntityBaseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });

    }


}
