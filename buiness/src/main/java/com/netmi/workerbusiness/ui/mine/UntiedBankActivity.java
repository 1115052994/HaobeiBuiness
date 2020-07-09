package com.netmi.workerbusiness.ui.mine;

import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.BankListEntity;
import com.netmi.workerbusiness.databinding.ActivityUntiedBankBinding;
import com.netmi.workerbusiness.databinding.ItemUntiedBankBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

public class UntiedBankActivity extends BaseXRecyclerActivity<ActivityUntiedBankBinding, BankListEntity> {


    @Override
    protected int getContentView() {
        return R.layout.activity_untied_bank;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("银行卡");
        getBankList();
        initRecyclerView();
        mBinding.refreshView.setOnRefreshListener(this::onRefresh);
    }

    private void initRecyclerView() {
        adapter = new BaseRViewAdapter<BankListEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_untied_bank;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<BankListEntity>(binding) {
                    @Override
                    public void bindData(BankListEntity item) {
                        super.bindData(item);//不能删
                        ItemUntiedBankBinding itemBinding = (ItemUntiedBankBinding) binding;
                    }

                    @Override
                    public void doClick(View view) {
                        BankListEntity entity = items.get(position);
                        super.doClick(view);
                        if (view.getId() == R.id.tv_untied) {
                            Bundle args = new Bundle();
                            args.putSerializable(JumpUtil.VALUE, entity);
                            JumpUtil.overlay(getContext(), UntiedConfirmActivity.class, args);
                        }
                    }
                };
            }

        };
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
    }


    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    protected void doListData() {

    }

    private void getBankList() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getBankList("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<BankListEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<List<BankListEntity>> data) {
                        adapter.setData(data.getData());
                    }
                });
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        getBankList();
        mBinding.refreshView.setRefreshing(false);
    }


}
