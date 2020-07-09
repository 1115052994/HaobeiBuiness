package com.netmi.workerbusiness.ui.home.offline;

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
import com.netmi.workerbusiness.data.api.OfflineGoodApi;
import com.netmi.workerbusiness.data.entity.home.offlineorder.OfflineOrderListEntity;
import com.netmi.workerbusiness.data.event.LineCommoditySearchEvent;
import com.netmi.workerbusiness.databinding.FragmentListBinding;
import com.netmi.workerbusiness.databinding.ItemOfflineOrderViewBinding;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.netmi.workerbusiness.ui.home.online.LineOrderActivity.ALL;


public class OfflineOrderViewFragment extends BaseXRecyclerFragment<FragmentListBinding, OfflineOrderListEntity> {
    private static final String TAG = OfflineOrderViewFragment.class.getName();
    public static final String TYPE = "type";
    private int type;
    private String keyword;


    //实例化
    public static OfflineOrderViewFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        OfflineOrderViewFragment fragment = new OfflineOrderViewFragment();
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


    @Override
    public void onResume() {
        super.onResume();
        xRecyclerView.refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshOrderStatus(LineCommoditySearchEvent event) {
        keyword = event.getContent();
        xRecyclerView.refresh();
    }


    @Override
    protected void doListData() {
        int status;
        if (type != ALL) {
            status = type;
            RetrofitApiFactory.createApi(OfflineGoodApi.class)
                    .getList(keyword, String.valueOf(status), PageUtil.toPage(startPage, 10), Constant.PAGE_ROWS)
                    .compose(RxSchedulers.compose())
                    .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new XObserver<BaseData<PageEntity<OfflineOrderListEntity>>>(this) {
                        @Override
                        public void onSuccess(BaseData<PageEntity<OfflineOrderListEntity>> data) {
                            showData(data.getData());
                        }
                    });
        } else {
            RetrofitApiFactory.createApi(OfflineGoodApi.class)
                    .getList(keyword, "", PageUtil.toPage(startPage, 10), Constant.PAGE_ROWS)
                    .compose(RxSchedulers.compose())
                    .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new XObserver<BaseData<PageEntity<OfflineOrderListEntity>>>(this) {
                        @Override
                        public void onSuccess(BaseData<PageEntity<OfflineOrderListEntity>> data) {
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
//        xRecyclerView.refresh();
    }

    private void initRecyclerView() {
        adapter = new BaseRViewAdapter<OfflineOrderListEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_offline_order_view;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<OfflineOrderListEntity>(binding) {
                    ItemOfflineOrderViewBinding itemOfflineOrderViewBinding = (ItemOfflineOrderViewBinding) binding;

                    @Override
                    public void bindData(OfflineOrderListEntity item) {
                        super.bindData(item);//不能删
                        OfflineOrderListEntity entity = items.get(position);
                        if (entity.getStatus() == 9) {
                            if (entity.getType().equals("12")) {
                                itemOfflineOrderViewBinding.tvComment.setVisibility(View.GONE);
                            } else {
                                itemOfflineOrderViewBinding.tvComment.setVisibility(View.VISIBLE);
                            }
                        } else {
                            itemOfflineOrderViewBinding.tvComment.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void doClick(View view) {
                        OfflineOrderListEntity offlineOrderListEntity = items.get(position);
                        super.doClick(view);
                        int id = view.getId();
                        if (id == R.id.ll_good) {
                            Bundle bundle = new Bundle();  //订单详情
                            bundle.putString(JumpUtil.ID, offlineOrderListEntity.getId());
                            JumpUtil.overlay(getActivity(), OfflineOrderDetailActivity.class, bundle);
                        } else if (id == R.id.tv_comment) {
                            Bundle args = new Bundle();    //订单评价
                            args.putString(JumpUtil.ID, offlineOrderListEntity.getId());
                            JumpUtil.overlay(getContext(), OfflineEvaluationActivity.class, args);
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

}
