package com.liemi.basemall.ui.personal.refund;

import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.data.entity.order.RefundDetailedEntity;
import com.liemi.basemall.data.event.OrderRefreshEvent;
import com.liemi.basemall.data.event.OrderRefundEvent;
import com.liemi.basemall.databinding.ActivityRefundDetailedBinding;
import com.liemi.basemall.ui.personal.order.OrderDetailActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.IntentUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.ConfirmDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.personal.refund.RefundApplyLogisticActivity.SUB_ORDER_DETAILED;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/9/9 20:56
 * 修改备注：
 */
public class RefundDetailedActivity extends BaseActivity<ActivityRefundDetailedBinding> {

    public static final String REFUND_ID = "REFUND_ID";
    private String refundId;
    private RefundDetailedEntity detailedEntity;
    private BaseRViewAdapter<String, BaseViewHolder> hintAdapter;
    private BaseRViewAdapter<OrderDetailsEntity.MeOrdersBean, BaseViewHolder> goodAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_detailed;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("退款详情");
        EventBus.getDefault().register(this);

        refundId = getIntent().getStringExtra(REFUND_ID);

        if (TextUtils.isEmpty(refundId)) {
            ToastUtils.showShort("没有订单Id");
            finish();
            return;
        }

        doGetRefundDetailed();

        hintAdapter = new BaseRViewAdapter<String, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_refund_hint;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                };
            }
        };
        mBinding.rvHint.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvHint.setNestedScrollingEnabled(false);
        mBinding.rvHint.setAdapter(hintAdapter);

        goodAdapter = new BaseRViewAdapter<OrderDetailsEntity.MeOrdersBean, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_order_good;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                };
            }
        };
        mBinding.rvRefundGoods.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvRefundGoods.setNestedScrollingEnabled(false);
        mBinding.rvRefundGoods.setAdapter(goodAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_contact_service) {
            final String servicePhone;
            if (detailedEntity.getTel() == null || Strings.isEmpty(detailedEntity.getTel().getShop_tel())) {
                ToastUtils.showShort("店家未配置电话");
                return;
            }
            servicePhone = detailedEntity.getTel().getShop_tel();
            new ConfirmDialog(this)
                    .setContentText("客服电话：" + servicePhone)
                    .setConfirmText("拨打")
                    .setConfirmListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(IntentUtils.getDialIntent(servicePhone));
                        }
                    }).show();

        } else if (i == R.id.tv_order_see || i == R.id.tv_order_function) {
            btnClick((TextView) findViewById(i));
        }
    }

    private void btnClick(TextView tvBtn) {
        switch (tvBtn.getText().toString()) {
            case "取消申请":
                new AlertDialog.Builder(this)
                        .setMessage("确定取消申请退款吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doCancelRefundApply();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case "修改申请":
                Bundle updateApplyBundle = new Bundle();
                updateApplyBundle.putSerializable(SUB_ORDER_DETAILED, detailedEntity);
                if (detailedEntity.getRefund_status() == 3) {
                    JumpUtil.overlay(this, RefundApplyLogisticActivity.class, updateApplyBundle);
                } else {
                    JumpUtil.overlay(this, UpdateApplyActivity.class, updateApplyBundle);
                }
                break;
            case "重新申请":
                Bundle reapplyBundle = new Bundle();
                reapplyBundle.putSerializable(SUB_ORDER_DETAILED, detailedEntity);
                JumpUtil.overlay(this, UpdateApplyActivity.class, reapplyBundle);
                break;
            case "填写物流":
                Bundle bundle = new Bundle();
                bundle.putSerializable(SUB_ORDER_DETAILED, detailedEntity);
                JumpUtil.overlay(this, RefundApplyLogisticActivity.class, bundle);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void updateRefundData(OrderRefundEvent event) {
        if (!TextUtils.isEmpty(refundId)
                && !TextUtils.equals(AppManager.getInstance().currentActivity().getClass().getName(), getClass().getName())) {
            doGetRefundDetailed();
        }
    }

    private void doGetRefundDetailed() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getRefundDetail(refundId)
                .compose(RxSchedulers.<BaseData<RefundDetailedEntity>>compose())
                .compose((this).<BaseData<RefundDetailedEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<RefundDetailedEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<RefundDetailedEntity> data) {
                        if (data.getData() == null) {
                            ToastUtils.showShort("订单数据不存在");
                            finish();
                        } else {
                            detailedEntity = data.getData();
                            if (!Strings.isEmpty(detailedEntity.getMeOrders())) {
                                for (OrderDetailsEntity.MeOrdersBean bean : detailedEntity.getMeOrders()) {
                                    if (TextUtils.isEmpty(bean.getImg_url())) {
                                        bean.setImg_url(detailedEntity.getImgs());
                                    }
                                }
                            }
                            mBinding.setItem(data.getData());
                            mBinding.executePendingBindings();
                            getRefundDetailSuccess(detailedEntity);
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void getRefundDetailSuccess(RefundDetailedEntity detailedEntity) {
        controlRefundStep(detailedEntity);      //控制退款的进度显示
        goodAdapter.setData(detailedEntity.getMeOrders());
        hintAdapter.setData(detailedEntity.getHints());
        if (detailedEntity.getOrder() != null && "4".equals(detailedEntity.getOrder().getPay_channel())) {
            mBinding.tvRefundPrice.setText("ETH" + detailedEntity.getPrice_total());
            mBinding.tvTopRefundPrice.setText("ETH" + detailedEntity.getPrice_total());
        } else {
            mBinding.tvRefundPrice.setText(detailedEntity.getShowPrice());
            mBinding.tvTopRefundPrice.setText(detailedEntity.getShowPrice());
        }
    }

    private void controlRefundStep(RefundDetailedEntity detailedEntity) {
        mBinding.llStepRefundSuccess.setVisibility(View.GONE);
        mBinding.llStepRefundFail.setVisibility(View.GONE);
        mBinding.llStepRefuseRefund.setVisibility(View.GONE);
        mBinding.rlRefundPrice.setVisibility(View.GONE);
        if (detailedEntity.getType() == 1) {
            switch (detailedEntity.getStatus()) {//退款状态 0: 已取消退款退货 1：发起退款
                // 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
                case 2:
                    mBinding.rlRefundPrice.setVisibility(View.VISIBLE);
                    mBinding.llStepRefundSuccess.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    mBinding.rlRefundPrice.setVisibility(View.VISIBLE);
                    mBinding.llStepRefuseRefund.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    mBinding.rlRefundPrice.setVisibility(View.VISIBLE);
                    mBinding.llStepRefundFail.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            switch (detailedEntity.getRefund_status()) {//0.取消退款申请 1.发起退款退货申请
                // 2、卖家同意退货
                // 3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
                case 4:
                    mBinding.rlRefundPrice.setVisibility(View.VISIBLE);
                    mBinding.llStepRefuseRefund.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    mBinding.rlRefundPrice.setVisibility(View.VISIBLE);
                    mBinding.llStepRefundSuccess.setVisibility(View.VISIBLE);
                    break;

            }
        }
    }


    private void doCancelRefundApply() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .cancelRefundApply(refundId, detailedEntity.getType())
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        MApplication.getInstance().appManager.finishActivity(OrderDetailActivity.class);
                        ToastUtils.showShort("取消成功");
                        EventBus.getDefault().post(new OrderRefreshEvent());
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

}
