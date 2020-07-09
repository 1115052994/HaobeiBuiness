package com.liemi.basemall.ui.personal.digitalasset;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.user.WalletDetailsEntity;
import com.liemi.basemall.databinding.ActivityTradePropertyDetailsBinding;
import com.liemi.basemall.databinding.ItemTradePropertyDetailsBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class TradePropertyDetailsActivity extends BaseXRecyclerActivity<ActivityTradePropertyDetailsBinding,WalletDetailsEntity>{

    public static final String TRADE_PROPERTY_DETAILS_TITLE = "tradePropertyDetailsTitle";
    public static final String TRADE_PROPERTY_DETAILS_TYPES = "tradePropertyDetailsTypes";

    private int mLoadType = Constant.PULL_REFRESH;

    private int mTypes;
    private String mTitle;

    @Override
    protected int getContentView() {
        return R.layout.activity_trade_property_details;
    }

    @Override
    protected void initUI() {
        //从上个页面传递过来的title和类型
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mTitle = bundle.getString(TRADE_PROPERTY_DETAILS_TITLE);
            getTvTitle().setText(mTitle);
            mTypes = bundle.getInt(TRADE_PROPERTY_DETAILS_TYPES);
        }
        xRecyclerView = mBinding.frContent;
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseRViewAdapter<WalletDetailsEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_trade_property_details;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                BaseViewHolder viewHolder = new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        ItemTradePropertyDetailsBinding detailsBinding =(ItemTradePropertyDetailsBinding)getBinding();
                        if(items.get(position).getSymbol().equals("1")){
                            detailsBinding.setNum("+"+items.get(position).getCoin());
                        }else if(items.get(position).getSymbol().equals("2")){
                            detailsBinding.setNum("-"+items.get(position).getCoin());
                        }else{
                            detailsBinding.setNum(items.get(position).getCoin());
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
    protected void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doWalletDetails(String.valueOf(mTypes), PageUtil.toPage(startPage), Constant.PAGE_ROWS)
               .compose(this.<BaseData<PageEntity<WalletDetailsEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
               .compose(RxSchedulers.<BaseData<PageEntity<WalletDetailsEntity>>>compose())
                .subscribe(new XObserver<BaseData<PageEntity<WalletDetailsEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<WalletDetailsEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });
    }


}
