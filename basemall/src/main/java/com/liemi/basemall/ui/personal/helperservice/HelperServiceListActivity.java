package com.liemi.basemall.ui.personal.helperservice;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.user.HelperServiceEntity;
import com.liemi.basemall.databinding.ActivityHelperServiceListBinding;
import com.liemi.basemall.databinding.LayoutHelperPlatformBinding;
import com.liemi.basemall.ui.NormalDialog;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.liemi.basemall.ui.personal.helperservice.LinkHelperActivity.LINK_HELPER_SERVICE;

public class HelperServiceListActivity extends BaseXRecyclerActivity<ActivityHelperServiceListBinding, HelperServiceEntity> {

    private NormalDialog normalDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_helper_service_list;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("客服列表");
        xRecyclerView = mBinding.myContent;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        //设置topView
        LayoutHelperPlatformBinding mPlatformBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_helper_platform, null, false);
        xRecyclerView.addHeaderView(mPlatformBinding.getRoot());
        adapter = new BaseRViewAdapter<HelperServiceEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_helper_service;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(LINK_HELPER_SERVICE, items.get(position));
                        JumpUtil.overlay(HelperServiceListActivity.this, LinkHelperActivity.class, bundle);
                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);

        mPlatformBinding.setPlatformInfo(AppConfigCache.get().getPlatformEntity());
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.ll_call_phone) {
            if (!TextUtils.isEmpty(AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel())) {
                showCallDialog();
            } else {
                showError("平台未配置客服信息");
            }
        }
    }

    private void showCallDialog() {
        if (normalDialog == null) {
            normalDialog = new NormalDialog(this);
        }
        if (!normalDialog.isShowing()) {
            normalDialog.show();
        }

        normalDialog.setTitleInfo("温馨提示", true);
        normalDialog.setMessageInfo("是否拨打：" + AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel(), true);
        normalDialog.setCancelInfo("取消", true);
        normalDialog.setConfirmInfo("确认");

        normalDialog.setClickConfirmListener(new NormalDialog.ClickConfirmListener() {
            @Override
            public void clickConfirm() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel()));
                startActivity(intent);
                normalDialog.dismiss();
            }
        });
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doHelpeService(PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(this.<BaseData<PageEntity<HelperServiceEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<PageEntity<HelperServiceEntity>>>compose())
                .subscribe(new XObserver<BaseData<PageEntity<HelperServiceEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<HelperServiceEntity>> data) {
                        showData(data.getData());
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
