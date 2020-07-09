package com.liemi.basemall.ui.personal.order;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.data.event.OrderRefreshEvent;
import com.liemi.basemall.data.event.OrderUpdateEvent;
import com.liemi.basemall.data.event.OrderUpdateReplaceEvent;
import com.liemi.basemall.databinding.FragmentXrecyclerviewBinding;
import com.liemi.basemall.databinding.ItemOrderBinding;
import com.liemi.basemall.ui.shopcar.OrderPayOnlineActivity;
import com.liemi.basemall.ui.store.StoreDetailActivity;
import com.liemi.basemall.widget.MyRecyclerView;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.ConfirmDialog;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.personal.order.LogisticTrackActivity.MPID;
import static com.liemi.basemall.ui.personal.order.OrderDetailActivity.ORDER_ENTITY;
import static com.liemi.basemall.ui.personal.order.OrderDetailActivity.PARENT_POSITON;
import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;
import static com.netmi.baselibrary.data.Constant.ORDER_CANCEL;
import static com.netmi.baselibrary.data.Constant.ORDER_DO_ACCEPT;
import static com.netmi.baselibrary.data.Constant.ORDER_DO_CANCEL;
import static com.netmi.baselibrary.data.Constant.ORDER_DO_DELETE;
import static com.netmi.baselibrary.data.Constant.ORDER_DO_REMIND;
import static com.netmi.baselibrary.data.Constant.ORDER_MPID;
import static com.netmi.baselibrary.data.Constant.ORDER_STATE;
import static com.netmi.baselibrary.data.Constant.ORDER_WAIT_COMMENT;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/25 9:55
 * 修改备注：
 */
public class OrderModuleFragment extends BaseFragment<FragmentXrecyclerviewBinding> implements XRecyclerView.LoadingListener {


    /**
     * 页数
     */
    private int startPage = 0;

    /**
     * 总条数
     */
    private int totalCount;

    /**
     * 列表加载数据方式
     */
    private int LOADING_TYPE = -1;

    private BaseRViewAdapter<OrderDetailsEntity, BaseViewHolder> adapter;

    private int orderState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_xrecyclerview;
    }

    public static OrderModuleFragment newInstance(int state) {
        OrderModuleFragment f = new OrderModuleFragment();
        Bundle bundle = new Bundle();
        if (state > -1) {
            bundle.putString(ORDER_STATE, String.valueOf(state));
        }
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && xRecyclerView != null) {
            xRecyclerView.refresh();
        }
    }

    @Override
    protected void initUI() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<OrderDetailsEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_order;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    private int topPosition;

                    @Override
                    public void bindData(Object item) {
                        topPosition = position;
                        MyRecyclerView rvGoods = getBinding().rvGoods;
                        rvGoods.setNestedScrollingEnabled(false);
                        rvGoods.setLayoutManager(new LinearLayoutManager(context));
                        BaseRViewAdapter<OrderDetailsEntity.MeOrdersBean, BaseViewHolder> goodAdapter = new BaseRViewAdapter<OrderDetailsEntity.MeOrdersBean, BaseViewHolder>(getContext()) {
                            @Override
                            public int layoutResId(int position) {
                                return R.layout.item_order_good;
                            }

                            @Override
                            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                                return new BaseViewHolder(binding) {
                                    @Override
                                    public void doClick(View view) {
                                        super.doClick(view);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable(ORDER_ENTITY, adapter.getItem(topPosition));
                                        bundle.putString(PARENT_POSITON,topPosition+"");
                                        JumpUtil.overlay(getContext(), OrderDetailActivity.class, bundle);
                                    }
                                };
                            }

                        };

                        rvGoods.setAdapter(goodAdapter);
                        goodAdapter.setData(getItem(position).getMeOrders());
                        super.bindData(item);
                        getBinding().tvStatus.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public ItemOrderBinding getBinding() {
                        return (ItemOrderBinding) super.getBinding();
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (i == R.id.tv_store_name) {
                            JumpUtil.overlay(getContext(), StoreDetailActivity.class, new FastBundle().putString(STORE_ID, adapter.getItem(position).getShop().getId()));
                        } else if (i == R.id.tv_order_see || i == R.id.tv_order_function) {
                            final String functionText = ((TextView) view).getText().toString();
                            if (functionText.equals(getString(R.string.order_delete))
                                    || functionText.equals(getString(R.string.order_cancel))) {
                                new ConfirmDialog(getContext())
                                        .setContentText("确认" + functionText + "吗？")
                                        .setConfirmListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                doOrderUpdate(adapter.getItem(position), functionText.equals(getString(R.string.order_delete)) ? ORDER_DO_DELETE : ORDER_DO_CANCEL);
                                            }
                                        }).show();
                            } else if (functionText.equals(getString(R.string.order_logistics))) {
                                JumpUtil.overlay(getContext(), LogisticTrackActivity.class,
                                        new FastBundle().putString(MPID, adapter.getItem(position).getOrder_no()));
                            } else if (functionText.equals(getString(R.string.order_remind))) {
                                doOrderUpdate(adapter.getItem(position), ORDER_DO_REMIND);
                            } else if (functionText.equals(getString(R.string.order_go_pay))) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ORDER_MPID, adapter.getItem(position).getId());
                                JumpUtil.overlay(getContext(), OrderPayOnlineActivity.class, bundle);
                            } else if (functionText.equals(getString(R.string.order_confirm_accept))) {
                                new ConfirmDialog(getContext())
                                        .setContentText("确认收货吗？")
                                        .setConfirmListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                doOrderUpdate(adapter.getItem(position), ORDER_DO_ACCEPT);
                                            }
                                        }).show();
                            } else if (functionText.equals(getString(R.string.order_read_detail))) {
                                goDetail(adapter.getItem(position));
                            } else if (functionText.equals(getString(R.string.order_go_comment))) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ORDER_ENTITY, adapter.getItem(position));
                                JumpUtil.overlay(getContext(), MineCommentActivity.class, bundle);
                            }

                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(ORDER_ENTITY, adapter.getItem(position));
                            JumpUtil.overlay(getContext(), OrderDetailActivity.class, bundle);
                        }
                    }

                    private void goDetail(OrderDetailsEntity detailsEntity) {
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable(ORDER_ENTITY, detailsEntity);
//                        if (detailsEntity.getStatus() == Constant.ORDER_REFUND
//                                || detailsEntity.getStatus() == Constant.ORDER_REFUND_SUCCESS) {
//                            JumpUtil.overlay(getContext(), RefundDetailsActivity.class, bundle);
//                        } else {
//                            JumpUtil.overlay(getContext(), OrderDetailActivity.class, bundle);
//                        }
                    }
                };
            }
        });

        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    protected void initData() {
        orderState = Strings.toInt(getArguments().getString(ORDER_STATE), -1);
        xRecyclerView.refresh();
    }

    public void showData(PageEntity<OrderDetailsEntity> pageEntity) {
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                xRecyclerView.setLoadingMoreEnabled(true);
            }
            adapter.setData(pageEntity.getList());
        } else if (LOADING_TYPE == Constant.LOAD_MORE) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                adapter.insert(adapter.getItemCount(), pageEntity.getList());
            }
        }
        totalCount = pageEntity.getTotal_pages();
        startPage = adapter.getItemCount();
    }

    @Override
    public void onRefresh() {
        startPage = 0;
        LOADING_TYPE = Constant.PULL_REFRESH;
        xRecyclerView.setLoadingMoreEnabled(false);
        doListOrder();
    }

    @Override
    public void onLoadMore() {
        LOADING_TYPE = Constant.LOAD_MORE;
        doListOrder();
    }

    @Override
    public void hideProgress() {
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            xRecyclerView.refreshComplete();
        } else {
            xRecyclerView.loadMoreComplete();
        }
        if (startPage >= totalCount) {
            xRecyclerView.setNoMore(true);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void orderUpdate(OrderUpdateEvent event) {
        if (adapter == null) return;

        for (OrderDetailsEntity entity : adapter.getItems()) {
            if (TextUtils.equals(entity.getMpid(), event.getMpid())) {
                if (event.getStatus() == -1
                        || (orderState != -1
                        && event.getStatus() == ORDER_WAIT_COMMENT)) {
                    adapter.remove(entity);
                } else {
                    entity.setStatus(event.getStatus());
                    adapter.notifyDataSetChanged();
                }
                break;
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void orderUpdateReplace(OrderUpdateReplaceEvent event) {
//        if (adapter == null) return;
//        for (OrderDetailsEntity entity : adapter.getItems()) {
//            if (TextUtils.equals(entity.getMpid(), event.getMpid())) {
//                entity.setStatus(event.getStatus());
//                adapter.notifyDataSetChanged();
//                break;
//            }
//        }

        xRecyclerView.refresh();

    }





    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderRefresh(OrderRefreshEvent event) {
        mBinding.xrvData.refresh();
    }


    private void doListOrder() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listOrder(PageUtil.toPage(startPage), Constant.PAGE_ROWS, getArguments().getString(ORDER_STATE))
                .compose(RxSchedulers.<BaseData<PageEntity<OrderDetailsEntity>>>compose())
                .compose((this).<BaseData<PageEntity<OrderDetailsEntity>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData<PageEntity<OrderDetailsEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<PageEntity<OrderDetailsEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doOrderUpdate(final OrderDetailsEntity entity, final String scenario) {

        Observable<BaseData> observable = RetrofitApiFactory.createApi(OrderApi.class).cancelOrder(entity.getId());

        switch (scenario) {
            case ORDER_DO_REMIND:
                observable = RetrofitApiFactory.createApi(OrderApi.class).remindOrder(entity.getId());
                break;
            case ORDER_DO_ACCEPT:
                observable = RetrofitApiFactory.createApi(OrderApi.class).confirmReceipt(entity.getId());
                break;
            case ORDER_DO_DELETE:
                observable = RetrofitApiFactory.createApi(OrderApi.class).delOrder(entity.getId());
                break;
        }

        observable.compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        switch (scenario) {
                            case ORDER_DO_DELETE:
                                EventBus.getDefault().post(new OrderUpdateEvent(entity.getId(), -1));
                                break;
                            case ORDER_DO_CANCEL:
                                EventBus.getDefault().post(new OrderUpdateEvent(entity.getId(), ORDER_CANCEL));
                                break;
                            case ORDER_DO_REMIND:
                                ToastUtils.showShort("提醒成功！");
                                break;
                            case ORDER_DO_ACCEPT:
                                entity.setStatus(ORDER_WAIT_COMMENT);
                                if (orderState == -1) {
                                    adapter.notifyDataSetChanged();
                                } else {
                                    adapter.remove(entity);
                                }
                                EventBus.getDefault().post(new OrderUpdateEvent(entity.getMpid(), entity.getStatus()));
                                break;
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }


}
