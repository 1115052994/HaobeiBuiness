package com.liemi.basemall.ui.personal.coupon;

import android.animation.ObjectAnimator;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.entity.user.MineCouponListEntity;
import com.liemi.basemall.databinding.FragmentCouponNotUsedBinding;
import com.liemi.basemall.databinding.ItemCouponNotUsedBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;

import com.trello.rxlifecycle2.android.FragmentEvent;

public class MineCouponFragment extends BaseFragment<FragmentCouponNotUsedBinding> implements XRecyclerView.LoadingListener {

    public static final String COUPON_STATUS = "couponStatus";

    public static final int COUPON_STATUS_NOT_USED = 1;//未使用
    public static final int COUPON_STATUS_USED = 2;//已使用
    public static final int COUPON_STATUS_TIMED = 3;//已过期

    private int mCouponStatus;

    private int mStartPage = 0;
    private int mPages = 10;
    private int mTotalPages = 0;
    private int mLoadItems = 0;
    private int mLoadType = Constant.PULL_REFRESH;

    private Animation translateAnimationOpen;
    //点击使用规则时候的属性动画
    private ObjectAnimator mOpenRuleAnimator;

    private BaseRViewAdapter<MineCouponListEntity.MineCouponEntity, BaseViewHolder> adapter;

    public static MineCouponFragment newInstance(int status) {
        MineCouponFragment mineCouponFragment = new MineCouponFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COUPON_STATUS, status);
        mineCouponFragment.setArguments(bundle);
        return mineCouponFragment;
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_coupon_not_used;
    }

    @Override
    protected void initUI() {
        mCouponStatus = getArguments().getInt(COUPON_STATUS);
        //加载动画
        translateAnimationOpen = AnimationUtils.loadAnimation(getContext(), R.anim.anim_coupon_rule_open);

        if (mCouponStatus == COUPON_STATUS_NOT_USED) {
            mBinding.setUseStatus("未使用");
        } else if (mCouponStatus == COUPON_STATUS_TIMED) {
            mBinding.setUseStatus("已过期");
        }
        xRecyclerView = mBinding.frContent;
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.getItemAnimator().setChangeDuration(300);
        xRecyclerView.getItemAnimator().setMoveDuration(300);
        adapter = new BaseRViewAdapter<MineCouponListEntity.MineCouponEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                if (mCouponStatus == COUPON_STATUS_NOT_USED) {
                    //未使用
                    return R.layout.item_coupon_not_used;
                } else if (mCouponStatus == COUPON_STATUS_TIMED) {
                    return R.layout.item_coupon_timed;
                }
                return 0;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                BaseViewHolder viewHolder = new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        if (mCouponStatus == COUPON_STATUS_NOT_USED) {
                            MineCouponListEntity.MineCouponEntity entity = (MineCouponListEntity.MineCouponEntity) items.get(position);
                            ItemCouponNotUsedBinding notUsedBinding = (ItemCouponNotUsedBinding) getBinding();
                            if (entity.isOpenRule()) {

                            } else {

                            }
                        }

                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (mCouponStatus == COUPON_STATUS_NOT_USED) {
                            MineCouponListEntity.MineCouponEntity entity = (MineCouponListEntity.MineCouponEntity) items.get(position);

                            if (view.getId() == R.id.ll_rule_open) {
                                entity.setOpenRule(!entity.isOpenRule());
                                notifyItemChanged(getAdapterPosition());
                            }

                        }
                    }
                };
                return viewHolder;
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }


    @Override
    public void onRefresh() {
        mStartPage = 0;
        mLoadItems = 0;
        mTotalPages = 0;
        mLoadType = Constant.PULL_REFRESH;
        doCouponList();
    }

    @Override
    public void onLoadMore() {
        mStartPage++;
        mLoadType = Constant.LOAD_MORE;
        doCouponList();
    }

    //设置数据
    private void showCoupon(MineCouponListEntity entity) {
        if (TextUtils.isEmpty(entity.getTotal_pages())) {
            mTotalPages = Integer.parseInt(entity.getTotal_pages());
        }
        mBinding.setNum(entity.getTotal_pages());
        mLoadItems += entity.getList().size();

        if (mLoadType == Constant.PULL_REFRESH) {
            adapter.setData(entity.getList());
        } else if (mLoadType == Constant.LOAD_MORE) {
            adapter.insert(adapter.getItemCount(), entity.getList());
        }

        if (mLoadItems >= mTotalPages) {
            xRecyclerView.setLoadingMoreEnabled(false);
        } else {
            xRecyclerView.setLoadingMoreEnabled(true);
        }

    }

    //结束刷新和加载
    private void finishRefreshAndLoadMore() {
        if (mLoadType == Constant.PULL_REFRESH) {
            xRecyclerView.refreshComplete();
        } else if (mLoadType == Constant.LOAD_MORE) {
            xRecyclerView.loadMoreComplete();
        }
    }

    //请求优惠券信息
    private void doCouponList() {
        /*
        RetrofitApiFactory.createApi(MineApi.class)
                .doMineCouponListEntity(mStartPage, mPages, null, mCouponStatus)
                .compose(this.<BaseData<MineCouponListEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<MineCouponListEntity>>compose())
                .subscribe(new BaseObserver<BaseData<MineCouponListEntity>>() {
                    @Override
                    public void onNext(BaseData<MineCouponListEntity> mineCouponListEntityBaseData) {
                        if (mineCouponListEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {
                            showCoupon(mineCouponListEntityBaseData.getData());
                        } else {
                            showError(mineCouponListEntityBaseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        finishRefreshAndLoadMore();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        finishRefreshAndLoadMore();
                        showError(ex.getMessage());
                    }
                });
                */
    }


}
