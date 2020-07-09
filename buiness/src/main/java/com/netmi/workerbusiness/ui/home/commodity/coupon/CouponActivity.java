package com.netmi.workerbusiness.ui.home.commodity.coupon;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.liemi.basemall.databinding.ActivityXrecyclerviewBinding;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.CouponApi;
import com.netmi.workerbusiness.data.entity.home.coupon.CouponListEntity;
import com.netmi.workerbusiness.databinding.ItemCouponBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

public class CouponActivity extends BaseXRecyclerActivity<ActivityXrecyclerviewBinding, CouponListEntity> {


    @Override
    protected int getContentView() {
        return R.layout.activity_xrecyclerview;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("优惠券");
        getRightSetting().setText("新增");
        getRightSetting().setTextColor(getResources().getColor(R.color.color_84929E));
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new BaseRViewAdapter<CouponListEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_coupon;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<CouponListEntity>(binding) {
                    @Override
                    public void bindData(CouponListEntity item) {
                        super.bindData(item);//不能删
                        ItemCouponBinding itemCouponBinding = (ItemCouponBinding) binding;
                        CouponListEntity entity = getItem(position);
                        //   * is_expire	int	是否已过期 0:未过期 1:已过期
                        if (entity.getIs_expire() == 1) {
                            itemCouponBinding.tvDel.setVisibility(View.VISIBLE);
                            itemCouponBinding.tvStop.setVisibility(View.GONE);
                            itemCouponBinding.llDownShelf.setVisibility(View.GONE);
                        } else {
                            //  * sw	发放状态 0:已下架或禁用 1:发放中或启用
                            if (entity.getSw().equals("0")) {
                                itemCouponBinding.tvDel.setVisibility(View.GONE);
                                itemCouponBinding.tvStop.setVisibility(View.GONE);
                                itemCouponBinding.llDownShelf.setVisibility(View.VISIBLE);
                            } else if (entity.getSw().equals("1")) {
                                itemCouponBinding.tvDel.setVisibility(View.GONE);
                                itemCouponBinding.tvStop.setVisibility(View.VISIBLE);
                                itemCouponBinding.llDownShelf.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        CouponListEntity entity = getItem(position);
                        super.doClick(view);
                        int id = view.getId();
                        if (id == R.id.tv_stop) {  //禁用
                            stop(entity.getId());
                        } else if (id == R.id.tv_del) {  //删除
                            delete(entity.getId());
                        } else if (id == R.id.tv_start) {  // 启用（下架状态）
                            start(entity.getId());
                        } else if (id == R.id.tv_del_red) {  // 删除（下架状态）
                            delete(entity.getId());
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

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_setting) {
            JumpUtil.overlay(getContext(), AddCouponActivity.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        xRecyclerView.refresh();
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(CouponApi.class)
                .getCouponList("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<CouponListEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<List<CouponListEntity>> data) {
                        showData(data.getData());
                    }
                });
    }

    //禁用
    protected void stop(String id) {
        RetrofitApiFactory.createApi(CouponApi.class)
                .stopCoupon(id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        xRecyclerView.refresh();
                    }
                });
    }

    //删除
    protected void delete(String id) {
        RetrofitApiFactory.createApi(CouponApi.class)
                .deleteCoupon(id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        xRecyclerView.refresh();
                    }
                });
    }

    //启用
    protected void start(String id) {
        RetrofitApiFactory.createApi(CouponApi.class)
                .startCoupon(id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        xRecyclerView.refresh();
                    }
                });
    }

}
