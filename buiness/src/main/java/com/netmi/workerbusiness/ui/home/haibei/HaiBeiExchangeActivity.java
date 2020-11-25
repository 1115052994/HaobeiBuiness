package com.netmi.workerbusiness.ui.home.haibei;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.HaibeiConvertApi;
import com.netmi.workerbusiness.data.entity.haibei.HaibeiData;
import com.netmi.workerbusiness.data.entity.haibei.HaibeiOrderList;
import com.netmi.workerbusiness.databinding.ActivityHaiBeiExchangeBinding;
import com.netmi.workerbusiness.databinding.ItemOrderListBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;

public class HaiBeiExchangeActivity extends BaseXRecyclerActivity<ActivityHaiBeiExchangeBinding,HaibeiOrderList> {

    @Override
    protected int getContentView() {
        return R.layout.activity_hai_bei_exchange;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("海贝兑换");
        getRightImage().setImageResource(R.mipmap.say_help);
        getRightImage().setVisibility(View.VISIBLE);
        initRecyclerView();
        mBinding.setDoClick(this::doClick);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
        initdata();
    }

    private void initdata() {
        RetrofitApiFactory.createApi(HaibeiConvertApi.class)
                .getHaibeiConvert("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<HaibeiData>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<HaibeiData> data) {
                        if(data.getData().getSales_volume() !=null && !data.getData().getSales_volume() .equals("")){
                            String[] split = data.getData().getSales_volume().split("[.]");
                            mBinding.tvHaveSell.setText(split[0]);
                        }else {
                            mBinding.tvHaveSell.setText("0");
                        }
                        mBinding.setData1(data.getData());

                    }
                });
    }


    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        //提现
        if(id==R.id.withdraw_deposit){
            JumpUtil.overlay(getContext(),HaiBeiWithdrawDepositActivity.class);
        }else if(id==R.id.haibei_code_conversion){
            //海贝兑换
            JumpUtil.overlay(getContext(),HaiBeiRedeemCodeActivity.class);
        }else if(id == R.id.iv_setting){
            doAgreement(51);
        }
    }

    @Override
    protected void doListData() {

        RetrofitApiFactory.createApi(HaibeiConvertApi.class)
                .getHaibeiOrder("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<HaibeiOrderList>>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<List<HaibeiOrderList>> data) {
                        showData(data.getData());

                    }
                });
    }
    @Override
    public void onRefresh() {
        super.onRefresh();
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            xRecyclerView.refreshComplete();
        } else {
            xRecyclerView.loadMoreComplete();
        }
    }

    private void initRecyclerView() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);
        adapter = new BaseRViewAdapter<HaibeiOrderList, BaseViewHolder>(getContext()) {

            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_order_list;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<HaibeiOrderList>(binding) {
                    @Override
                    public void bindData(HaibeiOrderList item) {
                        HaibeiOrderList entity = items.get(position);
                        super.bindData(item);//不能删
                        ItemOrderListBinding itemOrderListBinding = (ItemOrderListBinding) binding;
                        itemOrderListBinding.setItem(entity);
                    }

                    @Override
                    public void doClick(View view) {
                        Bundle bundle = new Bundle();
                        HaibeiOrderList entity = items.get(position);
                        super.doClick(view);
                        bundle.putSerializable("message",entity);
                        JumpUtil.overlay(getContext(), HaiBeiOrderDetailsActivity.class,bundle);
                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    //海贝兑换问号
    private void doAgreement(int type) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .getAgreement(type)
                .compose(this.<BaseData<AgreementEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<AgreementEntity>>compose())
                .subscribe(new BaseObserver<BaseData<AgreementEntity>>() {
                    @Override
                    public void onNext(BaseData<AgreementEntity> agreementEntityBaseData) {
                        if (agreementEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {
                            if (agreementEntityBaseData.getData() != null && agreementEntityBaseData.getData().getContent() != null && agreementEntityBaseData.getData().getTitle() != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString(WEBVIEW_TITLE, agreementEntityBaseData.getData().getTitle());
                                bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_CONTENT);
                                bundle.putString(WEBVIEW_CONTENT, agreementEntityBaseData.getData().getContent());
                                JumpUtil.overlay(getContext(), BaseWebviewActivity.class, bundle);
                            }
                        } else {
                            showError(agreementEntityBaseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });

    }
}
