package com.liemi.basemall.ui.personal.order;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.AddressEntity;
import com.liemi.basemall.data.entity.order.LogisticEntity;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.data.event.OrderUpdateEvent;
import com.liemi.basemall.data.event.OrderUpdateReplaceEvent;
import com.liemi.basemall.databinding.ActivityOrderDetailBinding;
import com.liemi.basemall.databinding.ItemOrderBinding;
import com.liemi.basemall.databinding.ItemOrderDetailAddressBinding;
import com.liemi.basemall.databinding.ItemOrderDetailInfoBinding;
import com.liemi.basemall.databinding.ItemOrderGoodBinding;
import com.liemi.basemall.ui.personal.refund.OrderRefundActivity;
import com.liemi.basemall.ui.personal.refund.RefundDetailedActivity;
import com.liemi.basemall.ui.personal.refund.RequestReplaceActivty;
import com.liemi.basemall.ui.personal.refund.RequestReplaceDetailActivity;
import com.liemi.basemall.ui.shopcar.OrderPayOnlineActivity;
import com.liemi.basemall.ui.store.good.GoodDetailActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.IntentUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.ConfirmDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.liemi.basemall.ui.personal.order.LogisticTrackActivity.MPID;
import static com.liemi.basemall.ui.store.good.BaseGoodsDetailedActivity.ITEM_ID;
import static com.netmi.baselibrary.data.Constant.ORDER_CANCEL;
import static com.netmi.baselibrary.data.Constant.ORDER_DO_ACCEPT;
import static com.netmi.baselibrary.data.Constant.ORDER_DO_CANCEL;
import static com.netmi.baselibrary.data.Constant.ORDER_DO_DELETE;
import static com.netmi.baselibrary.data.Constant.ORDER_DO_REMIND;
import static com.netmi.baselibrary.data.Constant.ORDER_MPID;
import static com.netmi.baselibrary.data.Constant.ORDER_WAIT_COMMENT;
import static com.netmi.baselibrary.router.BaseRouter.App_OrderDetailActivity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/25 11:17
 * 修改备注：
 */
@Route(path = App_OrderDetailActivity)
public class OrderDetailActivity extends BaseActivity<ActivityOrderDetailBinding> {

    public static final String ORDER_ENTITY = "order_entity";

    public static final String HIDDEN_OPERATE = "hidden_operate";

    public static final String PARENT_POSITON = "parent_position";

    private BaseRViewAdapter<BaseEntity, BaseViewHolder> adapter;

    BaseRViewAdapter<OrderDetailsEntity.MeOrdersBean, BaseViewHolder> goodAdapter;

    List<BaseEntity> baseEntities = new ArrayList<>();

    private OrderDetailsEntity orderDetails;

    private LogisticEntity logisticEntity;

    private boolean hiddenOperate;

    private String parent_position;

    @Override
    protected int getContentView() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("订单详情");
        if (getIntent().getStringExtra(PARENT_POSITON) != null) {
            parent_position = getIntent().getStringExtra(PARENT_POSITON);
        }
        hiddenOperate = getIntent().getBooleanExtra(HIDDEN_OPERATE, false);
        if (hiddenOperate) {
            mBinding.llFooter.setVisibility(GONE);
        }

        RecyclerView recyclerView = mBinding.rvGoods;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new BaseRViewAdapter<BaseEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType == 1) {
                    return R.layout.item_order_detail_address;
                } else if (viewType == 2) {
                    return R.layout.item_order;
                }
                return R.layout.item_order_detail_info;
            }

            @Override
            public int getItemViewType(int position) {
                if (adapter.getItem(position) instanceof AddressEntity) {
                    return 1;
                } else if (adapter.getItem(position) instanceof OrderDetailsEntity) {
                    return 2;
                }
                return super.getItemViewType(position);
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(orderDetails);

                        if (getBinding() instanceof ItemOrderDetailAddressBinding) {
                            ItemOrderDetailAddressBinding addressBinding = (ItemOrderDetailAddressBinding) getBinding();
                            addressBinding.tvName.setText(orderDetails.getName());
                            if (TextUtils.isEmpty(orderDetails.getRemark())) {
                                addressBinding.tvSellMessage.setText("无");
                            } else {
                                addressBinding.tvSellMessage.setText(orderDetails.getRemark());
                            }
                            if (logisticEntity != null
                                    && logisticEntity.getList() != null
                                    && !logisticEntity.getList().isEmpty()) {
                                addressBinding.tvReceiveInfo.setText(logisticEntity.getList().get(0).getContent());
                                addressBinding.tvLogisticTime.setText(logisticEntity.getList().get(0).getTime());
                                addressBinding.rlLogistic.setVisibility(VISIBLE);
                            } else {
                                addressBinding.rlLogistic.setVisibility(GONE);
                            }
                            if (orderDetails.getStatus() == Constant.ORDER_WAIT_SEND) {
                                addressBinding.tvStatus.setText("等待卖家发货");
                            } else {
                                addressBinding.tvStatus.setText(orderDetails.getStatusToString());
                            }

                        } else if (getBinding() instanceof ItemOrderBinding) {
                            ItemOrderBinding itemOrderBinding = (ItemOrderBinding) getBinding();
                            itemOrderBinding.setHiddenFooter(true);
                            RecyclerView rvGoods = itemOrderBinding.rvGoods;
                            rvGoods.setLayoutManager(new LinearLayoutManager(context));
                            goodAdapter = new BaseRViewAdapter<OrderDetailsEntity.MeOrdersBean, BaseViewHolder>(getContext()) {
                                @Override
                                public int layoutResId(int position) {
                                    return R.layout.item_order_good;
                                }

                                @Override
                                public BaseViewHolder holderInstance(ViewDataBinding binding) {
                                    return new BaseViewHolder(binding) {
                                        @Override
                                        public void bindData(Object item) {
                                            ItemOrderGoodBinding goodBinding = (ItemOrderGoodBinding) getBinding();
                                            if (hiddenOperate) {
                                                goodBinding.setStatus(0);
                                            } else {
                                                goodBinding.setStatus(orderDetails.getStatus());
                                            }
                                            OrderDetailsEntity.MeOrdersBean bean = getItem(position);
                                            //0-未付款1-待发货2-待收货3-待评价4-退货申请
                                            // 5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
                                            switch (bean.getStatus()) {
                                                case 1:
                                                    goodBinding.llSales.setVisibility(View.VISIBLE);
                                                    goodBinding.tvAfterSales.setText("申请换货");
                                                    break;
                                                case 2:
                                                case 3:
                                                    goodBinding.llSales.setVisibility(View.VISIBLE);
                                                    goodBinding.tvAfterSales.setText("申请换货");
                                                    break;
                                                case 4:
                                                case 5:
                                                case 6:
                                                    goodBinding.llSales.setVisibility(View.VISIBLE);
                                                    goodBinding.tvAfterSales.setText("换货中");
                                                    break;
                                                default:
                                                    goodBinding.llSales.setVisibility(View.GONE);
                                                    break;
                                            }

                                            OrderDetailsEntity.MeOrdersBean ordersBean = getItem(position);

                                            super.bindData(item);

                                            if (goodBinding.llSales.getVisibility() == VISIBLE) {
                                                goodBinding.tvNum.setVisibility(View.INVISIBLE);
//                                                ordersBean.setColor_name(ordersBean.getColor_name()+"x"+ordersBean.getNum());
                                                goodBinding.tvFormat.setText(Strings.formatStr(getString(R.string.format_goods_specs_tip), ordersBean.getColor_name()) + "\t x" + ordersBean.getNum());

                                            } else {
//                                                ordersBean.setColor_name(ordersBean.getColor_name());
                                                goodBinding.tvFormat.setText(Strings.formatStr(getString(R.string.format_goods_specs_tip), ordersBean.getColor_name()));
                                            }

                                            goodBinding.tvPrice.setTextColor(Color.parseColor("#FF3700"));
                                        }

                                        @Override
                                        public void doClick(View view) {
                                            super.doClick(view);
                                            if (view.getId() == R.id.tv_after_sales) {
                                                ItemOrderGoodBinding goodBinding = (ItemOrderGoodBinding) getBinding();
                                                switch (goodBinding.tvAfterSales.getText().toString()) {
                                                    case "申请换货":
                                                        new ConfirmDialog(getContext()).setContentText("请确认是否要进行换货申请，申请后您不能再次发起申请").setConfirmListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Bundle refundBundle = new Bundle();
                                                                refundBundle.putSerializable(ORDER_ENTITY, orderDetails);
                                                                refundBundle.putInt(RequestReplaceActivty.REPLACE_POSITION, position);
                                                                JumpUtil.startForResult(getActivity(), RequestReplaceActivty.class, RequestReplaceActivty.REQUEST_CODE, refundBundle);
                                                            }
                                                        }).show();
                                                        break;
                                                    case "换货中":
                                                        Bundle bundle = new Bundle();
                                                        bundle.putSerializable(ORDER_ENTITY, orderDetails);
                                                        bundle.putInt(RequestReplaceDetailActivity.POSITION, position);
                                                        bundle.putInt(RequestReplaceDetailActivity.REPLACE_ID, Strings.toInt(orderDetails.getOrderSkus().get(position).getId()));
                                                        JumpUtil.overlay(getContext(), RequestReplaceDetailActivity.class, bundle);
                                                        break;
                                                    default:
                                                        Bundle detailBundle = new Bundle();
                                                        detailBundle.putString(RefundDetailedActivity.REFUND_ID, orderDetails.getOrderSkus().get(position).getId());
                                                        JumpUtil.overlay(getContext(), RefundDetailedActivity.class, detailBundle);
                                                        break;
                                                }
                                            } else if (view.getId() == R.id.ll_good) {
                                                JumpUtil.overlay(getContext(), GoodDetailActivity.class,
                                                        ITEM_ID, getItem(position).getItem_id());
                                            }
                                        }
                                    };
                                }
                            };
                            rvGoods.setAdapter(goodAdapter);
                            goodAdapter.setData(orderDetails.getMeOrders());
                        } else if (getBinding() instanceof ItemOrderDetailInfoBinding) {
                            ItemOrderDetailInfoBinding infoBinding = (ItemOrderDetailInfoBinding) getBinding();
                            if (orderDetails.getStatus() == 0) {
                                infoBinding.tvPriceTitle.setText("需付款");
                            } else {
                                infoBinding.tvPriceTitle.setText(Strings.isEmpty(orderDetails.getPay_channel()) ?
                                        getString(R.string.mall_title_real_price) : getString(R.string.mall_title_digital_price));
                            }
                        }
                    }


                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (i == R.id.rl_logistic) {
                            JumpUtil.overlay(getContext(), LogisticTrackActivity.class,
                                    new FastBundle().putString(MPID, orderDetails.getOrder_no()));
                        }
                    }
                };
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RequestReplaceActivty.RESULT_CODE) {
            if (requestCode == RequestReplaceActivty.REQUEST_CODE) {
                orderDetails.getMeOrders().get(data.getIntExtra(RequestReplaceActivty.POSITION, -1)).setStatus(Strings.toInt(data.getStringExtra(RequestReplaceActivty.REPLACE_STATUS)));
                goodAdapter.setData(orderDetails.getMeOrders());
                goodAdapter.notifyDataSetChanged();
                EventBus.getDefault().post(new OrderUpdateReplaceEvent(orderDetails.getMpid(), 6));
            } else if (requestCode == RequestReplaceDetailActivity.REQUEST_CODE) {
                orderDetails.getMeOrders().get(data.getIntExtra(RequestReplaceDetailActivity.POSITION, -1)).setStatus(Strings.toInt(data.getStringExtra(RequestReplaceDetailActivity.REPLACE_STATUS)));
                goodAdapter.setData(orderDetails.getMeOrders());
                goodAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    protected void initData() {
        orderDetails = (OrderDetailsEntity) getIntent().getSerializableExtra(ORDER_ENTITY);
        if (orderDetails == null) {
            String mpid = getIntent().getStringExtra(ORDER_MPID);

            if (TextUtils.isEmpty(mpid)) {
                ToastUtils.showShort("无效的数据！");
                finish();
            } else {
                doGetOrderDetail(mpid);
            }
        } else {
            showData(orderDetails);
        }
    }

    private void showData(OrderDetailsEntity entity) {
        mBinding.llFooter.setVisibility(VISIBLE);
        orderDetails = entity;
        if (orderDetails.getStatus() == 7) { //7-已退货
            mBinding.rlRefundOrder.setVisibility(VISIBLE);
        }

        mBinding.setItem(orderDetails);
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setFull_name(orderDetails.getAddress());
        addressEntity.setName(orderDetails.getName());
        addressEntity.setTel(orderDetails.getTel());
        baseEntities.add(addressEntity);
        baseEntities.add(orderDetails);
        baseEntities.add(new BaseEntity());
        adapter.setData(baseEntities);
        if (!TextUtils.isEmpty(entity.getLogistics_no())) {
            doGetLogistic(entity.getOrder_no());
        }
    }

    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_order_see || i == R.id.tv_order_function) {
            final String functionText = ((TextView) view).getText().toString();
            if (functionText.equals(getString(R.string.order_delete))
                    || functionText.equals(getString(R.string.order_cancel))) {
                new ConfirmDialog(getContext())
                        .setContentText("确认" + functionText + "吗？")
                        .setConfirmListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doOrderUpdate(orderDetails, functionText.equals(getString(R.string.order_delete)) ? ORDER_DO_DELETE : ORDER_DO_CANCEL);
                            }
                        }).show();
            } else if (functionText.equals(getString(R.string.order_logistics))) {
                JumpUtil.overlay(getContext(), LogisticTrackActivity.class,
                        new FastBundle().putString(MPID, orderDetails.getOrder_no()));
            } else if (functionText.equals(getString(R.string.order_remind))) {
                doOrderUpdate(orderDetails, ORDER_DO_REMIND);
            } else if (functionText.equals(getString(R.string.order_go_pay))) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ORDER_MPID, orderDetails.getId());
                JumpUtil.overlay(getContext(), OrderPayOnlineActivity.class, bundle);
            } else if (functionText.equals(getString(R.string.order_confirm_accept))) {
                new ConfirmDialog(getContext())
                        .setContentText("确认收货吗？")
                        .setConfirmListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doOrderUpdate(orderDetails, ORDER_DO_ACCEPT);
                            }
                        }).show();
            } else if (functionText.equals(getString(R.string.order_read_detail))) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(ORDER_ENTITY, orderDetails);
//                if (orderDetails.getStatus() == Constant.ORDER_REFUND
//                        || orderDetails.getStatus() == Constant.ORDER_REFUND_SUCCESS) {
//                    JumpUtil.overlay(getContext(), RefundDetailsActivity.class, bundle);
//                }
            } else if (functionText.equals(getString(R.string.order_go_comment))) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ORDER_ENTITY, orderDetails);
                JumpUtil.overlay(getContext(), MineCommentActivity.class, bundle);
            }

        } else if (i == R.id.tv_contact_service) {
            if (orderDetails == null
                    || orderDetails.getShop() == null
                    || TextUtils.isEmpty(orderDetails.getShop().getShop_tel())) {
                ToastUtils.showShort("未配置客服电话！");
                return;
            }
            new ConfirmDialog(getContext())
                    .setContentText("客服电话：" + orderDetails.getShop().getShop_tel())
                    .setConfirmText("拨打")
                    .setConfirmListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(IntentUtils.getDialIntent(orderDetails.getShop().getShop_tel()));
                        }
                    }).show();
        } else if (i == R.id.tv_refund_function) {   //去看看
            if (orderDetails.getOrderSkus().size() > 1) {
                JumpUtil.overlay(getContext(), OrderRefundActivity.class);
            } else {
                Bundle detailBundle = new Bundle();
                detailBundle.putString(RefundDetailedActivity.REFUND_ID, orderDetails.getOrderSkus().get(0).getId());
                JumpUtil.overlay(getContext(), RefundDetailedActivity.class, detailBundle);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void orderUpdate(OrderUpdateEvent event) {
        if (event != null && orderDetails != null
                && TextUtils.equals(event.getMpid(), orderDetails.getMpid())) {
            orderDetails.setStatus(event.getStatus());
            mBinding.setItem(orderDetails);
            mBinding.executePendingBindings();
            adapter.notifyDataSetChanged();
        }
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
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
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
                                finish();
                                break;
                            case ORDER_DO_CANCEL:
                                EventBus.getDefault().post(new OrderUpdateEvent(entity.getId(), ORDER_CANCEL));
                                finish();
                                break;
                            case ORDER_DO_REMIND:
                                ToastUtils.showShort("提醒成功！");
                                break;
                            case ORDER_DO_ACCEPT:
                                entity.setStatus(ORDER_WAIT_COMMENT);
                                mBinding.setItem(entity);
                                mBinding.executePendingBindings();
                                EventBus.getDefault().post(new OrderUpdateEvent(entity.getId(), entity.getStatus()));
                                break;
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }


    private void doGetLogistic(final String order_no) {
        RetrofitApiFactory.createApi(OrderApi.class)
                .getLogistic(order_no)
                .compose(RxSchedulers.<BaseData<List<LogisticEntity>>>compose())
                .compose((this).<BaseData<List<LogisticEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<LogisticEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        adapter.notifyItemChanged(0);
                    }

                    @Override
                    public void onSuccess(BaseData<List<LogisticEntity>> data) {
                        logisticEntity = data.getData().get(0);
                    }

                    @Override
                    public void onComplete() {
                        adapter.notifyItemChanged(0);
                    }
                });
    }


    private void doGetOrderDetail(String mpid) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getOrderDetail(mpid)
                .compose(RxSchedulers.<BaseData<OrderDetailsEntity>>compose())
                .compose((this).<BaseData<OrderDetailsEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<OrderDetailsEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        finish();
                    }

                    @Override
                    public void onSuccess(BaseData<OrderDetailsEntity> data) {
                        if (data.getData() != null) {
                            showData(data.getData());
                        } else {
                            showError("无效的数据！");
                            finish();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


}
