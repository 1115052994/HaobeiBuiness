package com.netmi.workerbusiness.ui.message;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.liemi.basemall.ui.NormalDialog;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MessApi;
import com.netmi.workerbusiness.data.entity.mess.PublicNoticeEntity;
import com.netmi.workerbusiness.databinding.FragmentMessageBinding;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.annotations.NonNull;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/8/29
 * 修改备注：
 */
public class MessageFragment extends BaseFragment<FragmentMessageBinding> {
    public static final String TAG = MessageFragment.class.getName();


    @Override
    protected int getContentView() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initUI() {
        initImmersionBar();
        ((TextView) mBinding.getRoot().findViewById(R.id.tv_title)).setText("消息");
        mBinding.setDoClick(this);
        mBinding.refreshView.setOnRefreshListener(this::onRefresh);
    }

    @Override
    protected void initData() {
    }

    public void onRefresh() {
        NoReadNum();
        mBinding.refreshView.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        NoReadNum();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initImmersionBar();
        }
    }

    public void initImmersionBar() {
        ImmersionBar.with(this)
                .reset()
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        // type_arr  1 平台公告(改为3)  2订单通知  5 系统通知
        Bundle args = new Bundle();
        if (view.getId() == R.id.ll_notice) { //平台公告
            args.putInt(JumpUtil.TYPE, 3);
            JumpUtil.overlay(getContext(), PublicNoticeActivity.class, args);
        } else if (view.getId() == R.id.ll_order) { //订单消息
            args.putInt(JumpUtil.TYPE, 2);
            JumpUtil.overlay(getContext(), PublicNoticeActivity.class, args);
        } else if (view.getId() == R.id.ll_system) { //系统通知
            args.putInt(JumpUtil.TYPE, 5);
            JumpUtil.overlay(getContext(), PublicNoticeActivity.class, args);
        } else if (view.getId() == R.id.tv_setting) {
            showLogoutDialog();
        }
    }


    private NormalDialog mReadConfirmDialog;

    //显示询问用户是否已读
    private void showLogoutDialog() {

        if (mReadConfirmDialog == null) {
            mReadConfirmDialog = new NormalDialog(getContext());
        }

        if (!mReadConfirmDialog.isShowing()) {
            mReadConfirmDialog.show();
        }
        mReadConfirmDialog.setMessageInfo(getString(R.string.confirm_read), true, 16, true);
        mReadConfirmDialog.setTitleInfo("", false);
        mReadConfirmDialog.showLineTitleWithMessage(false);
        mReadConfirmDialog.setCancelInfo(getString(R.string.cancel), true);
        mReadConfirmDialog.setConfirmInfo(getString(R.string.confirm));

        mReadConfirmDialog.setClickConfirmListener(new NormalDialog.ClickConfirmListener() {
            @Override
            public void clickConfirm() {
                doRead();
                mReadConfirmDialog.dismiss();
            }
        });

    }


    public void doRead() {
        RetrofitApiFactory.createApi(MessApi.class)
                .readAll("")
                .compose(RxSchedulers.compose())
                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort("全部已读");
                        onRefresh();
                    }
                });
    }

    private void NoReadNum() {
        RetrofitApiFactory.createApi(MessApi.class)
                .getNotice(new Integer[]{3}, "0", "10")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<PublicNoticeEntity>>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<PageEntity<PublicNoticeEntity>> data) {
                        if (data.getData().getRead_data().getAll_no_readnum() <= 0) {
                            mBinding.tvOne.setVisibility(View.GONE);
                        } else {
                            mBinding.setOne(String.valueOf(data.getData().getRead_data().getAll_no_readnum()));
                            mBinding.tvOne.setVisibility(View.VISIBLE);
                        }
                    }
                });
        RetrofitApiFactory.createApi(MessApi.class)
                .getNotice(new Integer[]{2}, "0", "10")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<PublicNoticeEntity>>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<PageEntity<PublicNoticeEntity>> data) {
                        if (data.getData().getRead_data().getAll_no_readnum() <= 0) {
                            mBinding.tvTwo.setVisibility(View.GONE);
                        } else {
                            mBinding.setTwo(String.valueOf(data.getData().getRead_data().getAll_no_readnum()));
                            mBinding.tvTwo.setVisibility(View.VISIBLE);
                        }
                    }
                });
        RetrofitApiFactory.createApi(MessApi.class)
                .getNotice(new Integer[]{5}, "0", "10")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<PublicNoticeEntity>>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<PageEntity<PublicNoticeEntity>> data) {
                        if (data.getData().getRead_data().getAll_no_readnum() <= 0) {
                            mBinding.tvThree.setVisibility(View.GONE);
                        } else {
                            mBinding.setThree(String.valueOf(data.getData().getRead_data().getAll_no_readnum()));
                            mBinding.tvThree.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }


}
