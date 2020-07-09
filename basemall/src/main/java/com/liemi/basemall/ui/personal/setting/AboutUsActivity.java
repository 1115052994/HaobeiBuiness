package com.liemi.basemall.ui.personal.setting;

import android.content.Intent;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ActivityLayoutAboutUsBinding;
import com.liemi.basemall.widget.ImageFullWebViewClient;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.tencent.bugly.beta.Beta;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity<ActivityLayoutAboutUsBinding> {
    @Override
    protected int getContentView() {
        return R.layout.activity_layout_about_us;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("关于我们");
        //设置版本号
        mBinding.setVersionName(AppUtils.getAppVersionName());
        mBinding.wvAboutUs.setWebViewClient(new ImageFullWebViewClient());
        ImageFullWebViewClient.setWebSettings(mBinding.wvAboutUs.getSettings());
        doAgreement(2);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.ll_service_agreement) {
            //点击服务协议
            doAgreement(2);
            return;
        }
        if (view.getId() == R.id.ll_link_us) {
            //点击联系我们
            return;
        }
        if (view.getId() == R.id.ll_check_update) {
            //点击检查更新
            Beta.checkUpgrade();
            return;
        }
        if (view.getId() == R.id.ll_feedback) {
            //点击意见反馈
            JumpUtil.overlay(this, SuggestionFeedbackActivity.class);
            return;
        }


    }

    //查看是否拥有打电话的权限


    //请求服务协议
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
                            if (agreementEntityBaseData.getData().getLink_type().equals("2")) {
                                mBinding.wvAboutUs.loadUrl(Constant.BASE_API + agreementEntityBaseData.getData().getParam());
                            } else {
                                mBinding.wvAboutUs.loadData(agreementEntityBaseData.getData().getContent(), "text/html;charset=utf-8", "utf-8");
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
