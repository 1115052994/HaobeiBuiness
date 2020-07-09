package com.netmi.workerbusiness.ui.home.online;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.liemi.basemall.ui.NormalDialog;
import com.liemi.basemall.ui.personal.order.LogisticTrackActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.OrderApi;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.netmi.workerbusiness.data.entity.home.lineorder.LineOrderListEntity;
import com.netmi.workerbusiness.data.event.LineCommoditySearchEvent;
import com.netmi.workerbusiness.databinding.FragmentListBinding;
import com.netmi.workerbusiness.databinding.ItemOrderGoodBinding;
import com.netmi.workerbusiness.databinding.ItemOrderViewBinding;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.netmi.workerbusiness.ui.home.online.LineOrderActivity.ALL;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/8/30
 * 修改备注：
 */
public class LineOrderViewFragment extends BaseXRecyclerFragment<FragmentListBinding, LineOrderListEntity> {
    private static final String TAG = LineOrderViewFragment.class.getName();
    public static final String TYPE = "type";
    private int type;
    private String keyword;

    //实例化
    public static LineOrderViewFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        LineOrderViewFragment fragment = new LineOrderViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshOrderStatus(LineCommoditySearchEvent event) {
        keyword = event.getContent();
        xRecyclerView.refresh();
    }

    private int status;

    @Override
    protected void doListData() {
        if (type != ALL) {
            status = type;
            RetrofitApiFactory.createApi(OrderApi.class)
                    .getLineOrderList(String.valueOf(status), keyword, PageUtil.toPage(startPage, 10), Constant.PAGE_ROWS)
                    .compose(RxSchedulers.compose())
                    .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new XObserver<BaseData<PageEntity<LineOrderListEntity>>>(this) {
                        @Override
                        public void onSuccess(BaseData<PageEntity<LineOrderListEntity>> data) {
                            showData(data.getData());
                        }
                    });
        } else {
            RetrofitApiFactory.createApi(OrderApi.class)
                    .getLineOrderList("", keyword, PageUtil.toPage(startPage, 10), Constant.PAGE_ROWS)
                    .compose(RxSchedulers.compose())
                    .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new XObserver<BaseData<PageEntity<LineOrderListEntity>>>(this) {
                        @Override
                        public void onSuccess(BaseData<PageEntity<LineOrderListEntity>> data) {
                            showData(data.getData());
                        }
                    });
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initUI() {
        type = getArguments().getInt(TYPE);
        initRecyclerView();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        xRecyclerView.refresh();
    }

    private void initRecyclerView() {
        adapter = new BaseRViewAdapter<LineOrderListEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_order_view;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<LineOrderListEntity>(binding) {
                    private int topPosition;

                    @Override
                    public void bindData(LineOrderListEntity item) {
                        topPosition = position;
                        super.bindData(item);//不能删
                        ItemOrderViewBinding itemOrderViewBinding = (ItemOrderViewBinding) binding;
                        RecyclerView rvGoods = itemOrderViewBinding.rvGoods;
                        rvGoods.setNestedScrollingEnabled(false);
                        rvGoods.setLayoutManager(new LinearLayoutManager(context));
                        BaseRViewAdapter<LineOrderListEntity.MainOrdersBean.OrderSkusBean, BaseViewHolder> goodAdapter = initGoodsAdapter(getItem(position).getGoods(), topPosition);
                        rvGoods.setAdapter(goodAdapter);
                        goodAdapter.setData(getItem(position).getGoods());
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        boolean canJump = true;
                        LineOrderListEntity entity = items.get(position);
                        Bundle args = new Bundle();
                        args.putString(LogisticTrackActivity.MPID, entity.getOrder_no());
                        int id = view.getId();
                        if (id == R.id.tv_order_function2) {
                            for (int i = 0; i < entity.getMainOrders().get(0).getOrderSkus().size(); i++) {
                                if (entity.getMainOrders().get(0).getOrderSkus().get(i).getStatus() != 1 && entity.getMainOrders().get(0).getOrderSkus().get(i).getStatus() != 7) {
                                    canJump = false;
                                }
                            }
                            if (status == 1) {
//                                    showError("立即发货");
                                //TYPE 1  表示是在订单列表页面进入
                                if (canJump) {
                                    args.putInt(JumpUtil.TYPE, 1);
                                    args.putSerializable(JumpUtil.VALUE, items.get(position));
                                    JumpUtil.overlay(getContext(), SendOutActivity.class, args);
                                } else {
                                    remindDialog();
                                }
                            } else if (status == 2) {
//                                    showError("查看物流");
                                JumpUtil.overlay(getContext(), LogisticTrackActivity.class, args);
                            } else if (status == 3) {
//                                    showError("查看物流");
                                JumpUtil.overlay(getContext(), LogisticTrackActivity.class, args);
                            } else {// 全部订单 重新判断    ·
                                if (entity.getStatus() == 1) {  //立即发货
                                    args.putInt(JumpUtil.TYPE, 1);
                                    args.putSerializable(JumpUtil.VALUE, items.get(position));
                                    if (canJump) {
                                        JumpUtil.overlay(getContext(), SendOutActivity.class, args);
                                    } else {
                                        remindDialog();
                                    }
                                } else {
//                                    showError("查看物流");
                                    JumpUtil.overlay(getContext(), LogisticTrackActivity.class, args);
                                }
                            }
                        } else if (id == R.id.tv_order_function1) {
                            if (entity.getStatus() == 9) { //交易成功 评价列表
                                args.putString(JumpUtil.ID, entity.getOrder_id());
                                JumpUtil.overlay(getContext(), OrderEvaluationActivity.class, args);
                            }
                        }
                    }
                };
            }
        };
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);
    }

    //初始化商品adapter
    protected BaseRViewAdapter<LineOrderListEntity.MainOrdersBean.OrderSkusBean, BaseViewHolder> initGoodsAdapter(List<LineOrderListEntity.MainOrdersBean.OrderSkusBean> beans, final int topPosition) {
        BaseRViewAdapter<LineOrderListEntity.MainOrdersBean.OrderSkusBean, BaseViewHolder> goodAdapter = new BaseRViewAdapter<LineOrderListEntity.MainOrdersBean.OrderSkusBean, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_order_good;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        //设置原价的删除线
                        ItemOrderGoodBinding itemOrderGoodBinding = (ItemOrderGoodBinding) binding;
                        itemOrderGoodBinding.setShowPriceType(true);
//                        SharemallItemOrderGoodBinding goodBinding = (SharemallItemOrderGoodBinding) getBinding();
//                        SpannableString oldPriceSpan = new SpannableString(goodBinding.tvGoodPriceOld.getText().toString());
//                        oldPriceSpan.setSpan(new StrikethroughSpan(), 0, goodBinding.tvGoodPriceOld.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        goodBinding.tvGoodPriceOld.setText(oldPriceSpan);

                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        //跳转到订单详情页
                        toOrderDetails(topPosition);
                    }
                };
            }
        };
        return goodAdapter;
    }

    //跳转到订单详情页
    protected void toOrderDetails(int topPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt(LineOrderDetailActivity.ORDER_DETAILS_ID, Integer.valueOf(adapter.getItem(topPosition).getId()));
        JumpUtil.overlay(getActivity(), LineOrderDetailActivity.class, bundle);
    }


    private NormalDialog mLogoutConfirmDialog;

    //显示提醒订单在退款中
    private void remindDialog() {

        if (mLogoutConfirmDialog == null) {
            mLogoutConfirmDialog = new NormalDialog(getContext());
        }

        if (!mLogoutConfirmDialog.isShowing()) {
            mLogoutConfirmDialog.show();
        }
        mLogoutConfirmDialog.setMessageInfo(getString(R.string.remind_text), true, 16, true);
        mLogoutConfirmDialog.setTitleInfo("", false);
        mLogoutConfirmDialog.showLineTitleWithMessage(false);
        mLogoutConfirmDialog.setCancelInfo(getString(R.string.cancel), true);
        mLogoutConfirmDialog.setConfirmInfo(getString(R.string.confirm));

    }

}
