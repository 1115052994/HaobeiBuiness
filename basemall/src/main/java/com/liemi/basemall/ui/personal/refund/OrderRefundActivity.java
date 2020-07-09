package com.liemi.basemall.ui.personal.refund;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.data.entity.order.RefundListEntity;
import com.liemi.basemall.data.entity.order.ReplaceListEntity;
import com.liemi.basemall.data.event.OrderRefundEvent;
import com.liemi.basemall.databinding.ActivityOrderRefundListBinding;
import com.liemi.basemall.databinding.ItemRefundOrderBinding;
import com.liemi.basemall.ui.personal.order.OrderDetailActivity;
import com.liemi.basemall.ui.store.StoreDetailActivity;
import com.liemi.basemall.widget.MyRecyclerView;
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
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.IntentUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.ConfirmDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.personal.refund.RefundDetailedActivity.REFUND_ID;
import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;
import static com.netmi.baselibrary.router.BaseRouter.App_MineOrderRefundActivity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/15 18:19
 * 修改备注：
 */
@Route(path = App_MineOrderRefundActivity)
public class OrderRefundActivity extends BaseXRecyclerActivity<ActivityOrderRefundListBinding, ReplaceListEntity> {

    @Override
    protected int getContentView() {
        return R.layout.activity_order_refund_list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("商品换货");
        EventBus.getDefault().register(this);

        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<ReplaceListEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_refund_order;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (i == R.id.tv_contact_service) {
                            final String servicePhone;
                            if (getItem(position).getChange() == null || getItem(position).getChange().getShop() == null || Strings.isEmpty(getItem(position).getChange().getShop().getShop_tel())) {
                                ToastUtils.showShort("店家未配置电话");
                                return;
                            }
                            servicePhone = getItem(position).getChange().getShop().getShop_tel();
                            new ConfirmDialog(OrderRefundActivity.this)
                                    .setContentText("客服电话：" + servicePhone)
                                    .setConfirmText("拨打")
                                    .setConfirmListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(IntentUtils.getDialIntent(servicePhone));
                                        }
                                    }).show();
                        } else if (i == R.id.tv_order_see) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(RequestReplaceDetailActivity.POSITION, position);
                            bundle.putInt(RequestReplaceDetailActivity.REPLACE_ID,Strings.toInt(getItem(position).getChange().getOrder_sku_id()));
                            bundle.putString(RequestReplaceDetailActivity.REPLACE_FROM,RequestReplaceDetailActivity.REPLACE_FROM_LIST);
                            JumpUtil.overlay(getContext(), RequestReplaceDetailActivity.class, bundle);
                        } else if (i == R.id.tv_store_name) {
                            JumpUtil.overlay(getContext(), StoreDetailActivity.class, new FastBundle().putString(STORE_ID,
                                    adapter.getItem(position).getChange().getShop_id()));
                        } else {
                            JumpUtil.overlay(getContext(), StoreDetailActivity.class, new FastBundle().putString(STORE_ID,
                                    adapter.getItem(position).getChange().getShop_id()));
                        }

                    }

                    @Override
                    public ItemRefundOrderBinding getBinding() {
                        return (ItemRefundOrderBinding) super.getBinding();
                    }

                    @Override
                    public void bindData(Object item) {
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
                                    }
                                };
                            }

                        };

                        List<OrderDetailsEntity.MeOrdersBean> meOrders = new ArrayList<>();
                        OrderDetailsEntity.MeOrdersBean meOrdersBean = new OrderDetailsEntity.MeOrdersBean();
                        meOrdersBean.setImg_url(getItem(position).getItem_img());
                        meOrdersBean.setTitle(getItem(position).getSpu_name());
                        meOrdersBean.setColor_name(getItem(position).getValue_name());
                        meOrdersBean.setNum("1");
                        meOrders.add(meOrdersBean);

                        rvGoods.setAdapter(goodAdapter);
                        goodAdapter.setData(meOrders);
                        super.bindData(item);
                    }
                };
            }
        });
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);

    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_confirm) {
            new ConfirmDialog(this)
                    .setContentText("客服电话：" + AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel())
                    .setConfirmText("拨打")
                    .setConfirmListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(IntentUtils.getDialIntent(AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel()));
                        }
                    }).show();

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void updateRefundData(OrderRefundEvent event) {
        if (xRecyclerView != null) {
            xRecyclerView.refresh();
        }
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listGoodChange(PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(RxSchedulers.<BaseData<PageEntity<ReplaceListEntity>>>compose())
                .compose((this).<BaseData<PageEntity<ReplaceListEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<ReplaceListEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<PageEntity<ReplaceListEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

}
