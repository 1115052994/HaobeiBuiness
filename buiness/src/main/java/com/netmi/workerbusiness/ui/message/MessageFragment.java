package com.netmi.workerbusiness.ui.message;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MessApi;
import com.netmi.workerbusiness.data.entity.mess.PublicNoticeEntity;
import com.netmi.workerbusiness.databinding.FragmentMessageBinding;
import com.netmi.workerbusiness.databinding.ItemOfficialPushBinding;
import com.netmi.workerbusiness.ui.home.offline.OfflineOrderDetailActivity;
import com.netmi.workerbusiness.ui.home.online.LineOrderDetailActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.annotations.NonNull;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_URL;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/8/29
 * 修改备注：
 */
public class MessageFragment extends BaseXRecyclerFragment<FragmentMessageBinding,PublicNoticeEntity> {
    public static final String TAG = MessageFragment.class.getName();
    protected MyXRecyclerView xRecyclerView;

    @Override
    protected int getContentView() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initUI() {
        initImmersionBar();
        ((TextView) mBinding.getRoot().findViewById(R.id.tv_title)).setText("消息");
        mBinding.setDoClick(this);
        initRecyclerView();
    }


    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    public void onRefresh() {
        NoReadNum();
        xRecyclerView.refreshComplete();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MessApi.class)
                .getNotice(new Integer[]{2}, String.valueOf(PageUtil.toPage(startPage)), "10")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<PublicNoticeEntity>>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<PageEntity<PublicNoticeEntity>> data) {
                        showData(data.getData());
                    }
                });
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
    private void initRecyclerView() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);
        adapter = new BaseRViewAdapter<PublicNoticeEntity, BaseViewHolder>(getContext()) {

            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_official_push;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<PublicNoticeEntity>(binding) {
                    @Override
                    public void bindData(PublicNoticeEntity item) {
                        PublicNoticeEntity entity = items.get(position);
                        super.bindData(item);//不能删
                        ItemOfficialPushBinding itemOfficialPushBinding = (ItemOfficialPushBinding) binding;
                        itemOfficialPushBinding.ivLogo.setImageResource(R.mipmap.ic_system_notice);
                    }

                    @Override
                    public void doClick(View view) {
                        PublicNoticeEntity entity = items.get(position);
                        read(entity.getNotice_id());
                        super.doClick(view);
                        if (entity.getLink_type().equals("4")) {
                            Bundle bundle = new Bundle();
                            if (entity.getOrder_type().equals("11")) {//* order_type :  "11", //0表示普通消息；11表示线下订单消息
                                bundle.putString(JumpUtil.ID, entity.getParam());
                                JumpUtil.overlay(getActivity(), OfflineOrderDetailActivity.class, bundle);
                            } else {
                                bundle.putInt(LineOrderDetailActivity.ORDER_DETAILS_ID, Integer.valueOf(entity.getParam()));
                                JumpUtil.overlay(getActivity(), LineOrderDetailActivity.class, bundle);
                            }
                        }
                        xRecyclerView.refresh();
                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    private void read(String notice_id) {
        RetrofitApiFactory.createApi(MessApi.class)
                .read(notice_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(@NonNull BaseData data) {
                        xRecyclerView.refresh();
                    }
                });
    }


}
