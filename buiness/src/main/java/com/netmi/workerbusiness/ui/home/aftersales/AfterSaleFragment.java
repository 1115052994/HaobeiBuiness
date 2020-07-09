package com.netmi.workerbusiness.ui.home.aftersales;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

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
import com.netmi.workerbusiness.data.api.AfterSaleApi;
import com.netmi.workerbusiness.data.entity.home.aftersale.AfterSaleEntity;
import com.netmi.workerbusiness.data.event.LineCommoditySearchEvent;
import com.netmi.workerbusiness.databinding.FragmentListBinding;
import com.netmi.workerbusiness.databinding.ItemAfterSaleBinding;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.netmi.workerbusiness.ui.home.aftersales.AfterSalesActivity.ALL;


/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/22
 * 修改备注：
 */
public class AfterSaleFragment extends BaseXRecyclerFragment<FragmentListBinding, AfterSaleEntity> {
    private static final String TAG = AfterSaleFragment.class.getName();
    public static final String TYPE = "type";
    private int type;
    private String keyword = "";

    //实例化
    public static AfterSaleFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        AfterSaleFragment fragment = new AfterSaleFragment();
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
            RetrofitApiFactory.createApi(AfterSaleApi.class)
                    .getAfterSaleList(String.valueOf(status), keyword, PageUtil.toPage(startPage, 10), Constant.PAGE_ROWS)
                    .compose(RxSchedulers.compose())
                    .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new XObserver<BaseData<PageEntity<AfterSaleEntity>>>(this) {
                        @Override
                        public void onSuccess(BaseData<PageEntity<AfterSaleEntity>> data) {
                            showData(data.getData());
                        }
                    });
        } else {
            RetrofitApiFactory.createApi(AfterSaleApi.class)
                    .getAfterSaleList("", keyword, PageUtil.toPage(startPage, 10), Constant.PAGE_ROWS)
                    .compose(RxSchedulers.compose())
                    .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new XObserver<BaseData<PageEntity<AfterSaleEntity>>>(this) {
                        @Override
                        public void onSuccess(BaseData<PageEntity<AfterSaleEntity>> data) {
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

    private void initRecyclerView() {
        adapter = new BaseRViewAdapter<AfterSaleEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_after_sale;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<AfterSaleEntity>(binding) {
                    @Override
                    public void bindData(AfterSaleEntity item) {
                        super.bindData(item);//不能删
                        ItemAfterSaleBinding itemAfterSaleBinding = (ItemAfterSaleBinding) binding;
                        AfterSaleEntity entity = items.get(position);
                        if (entity.getRefund().getType().equals("1")) {
                            itemAfterSaleBinding.tvStatus.setText(entity.getRefund().getStatus());
                        } else if (entity.getRefund().getType().equals("2")) {
                            itemAfterSaleBinding.tvStatus.setText(entity.getRefund().getRefund_status());
                        } else {
                            itemAfterSaleBinding.tvStatus.setText(entity.getRefund().getStatus());
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        AfterSaleEntity entity = items.get(position);
                        Bundle args = new Bundle();
                        args.putString(JumpUtil.ID, entity.getRefund().getOrder_sku_id());
                        JumpUtil.overlay(getContext(), AfterSaleDetailActivity.class, args);
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
}
