package com.netmi.workerbusiness.ui.mine;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.ShopScoreEntity;
import com.netmi.workerbusiness.databinding.ActivityStoreCreditScoreBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

public class StoreCreditScoreActivity extends BaseXRecyclerActivity<ActivityStoreCreditScoreBinding, ShopScoreEntity> {
    private int credit_score;

    @Override
    protected int getContentView() {
        return R.layout.activity_store_credit_score;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("商户信用分");
        initRecyclerView();
        credit_score = getIntent().getExtras().getInt(JumpUtil.VALUE);
        mBinding.tvScore.setText(String.valueOf(credit_score));
    }

    private void initRecyclerView() {
        adapter = new BaseRViewAdapter<ShopScoreEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_credit_score;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<ShopScoreEntity>(binding) {
                    @Override
                    public void bindData(ShopScoreEntity item) {
                        super.bindData(item);//不能删

                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);

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
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .shopScore("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<ShopScoreEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<List<ShopScoreEntity>> data) {
                        adapter.setData(data.getData());
                    }
                });
    }
}
