package com.netmi.workerbusiness.ui.mine.wallet;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.WalletApi;
import com.netmi.workerbusiness.data.entity.walllet.EthDetailTwoEntity;
import com.netmi.workerbusiness.databinding.ActivityEthwalletDetailBinding;
import com.netmi.workerbusiness.databinding.ItemDetailEthWalletBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class ETHWalletDetailActivity extends BaseXRecyclerActivity<ActivityEthwalletDetailBinding, EthDetailTwoEntity> {

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_ethwallet_detail;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("交易明细");
        initRecycleView();
    }

    private void initRecycleView() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<EthDetailTwoEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_detail_eth_wallet;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        ItemDetailEthWalletBinding item1 = (ItemDetailEthWalletBinding) binding;
//                        item1.tvTitle.setText(TextUtils.equals(items.get(position).getIs_haibei(),"0")? items.get(position).getTitle():"海贝兑换奖励");
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        switch (view.getId()) {

                        }
                    }
                };
            }
        });
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        showProgress("");
        RetrofitApiFactory.createApi(WalletApi.class)
                .ETHDetail(PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<EthDetailTwoEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<EthDetailTwoEntity>> data) {
                        showData(data.getData());
                        hideProgress();

                    }
                });

    }
}
