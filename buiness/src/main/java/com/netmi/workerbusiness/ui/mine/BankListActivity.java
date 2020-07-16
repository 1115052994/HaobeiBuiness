package com.netmi.workerbusiness.ui.mine;

import android.content.Intent;
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
import com.netmi.workerbusiness.data.entity.HomePageEntity;
import com.netmi.workerbusiness.data.entity.mine.BankListEntity;
import com.netmi.workerbusiness.data.entity.mine.WalletEntity;
import com.netmi.workerbusiness.databinding.ActivityBankListBinding;
import com.netmi.workerbusiness.databinding.ItemBankBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;
//银行卡添加
public class BankListActivity extends BaseXRecyclerActivity<ActivityBankListBinding, BankListEntity> {
    public static final String BANK_MESS = "bank_mess";
    public static final int FROM_WALLET = 10001;
    public static final int FROM_WITHDRAW = 10002;

    private int type;

    @Override
    protected int getContentView() {
        return R.layout.activity_bank_list;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("银行卡");
//        getRightSetting().setText("解绑");
//        getRightSetting().setTextColor(getResources().getColor(R.color.a9a9a9));
        getBankList();
        initRecyclerView();
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        mBinding.refreshView.setOnRefreshListener(this::onRefresh);

    }


    private void initRecyclerView() {
        adapter = new BaseRViewAdapter<BankListEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_bank;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<BankListEntity>(binding) {
                    @Override
                    public void bindData(BankListEntity item) {
                        super.bindData(item);//不能删
                        ItemBankBinding itemBinding = (ItemBankBinding) binding;
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId() == R.id.ll_content) {
                            if (type == FROM_WITHDRAW) {
                                Intent intent = new Intent();
                                intent.putExtra(BANK_MESS, adapter.getItem(position));
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        } else if (view.getId() == R.id.tv_untied) {
                            delBank(String.valueOf(items.get(position).getId()), "", "");
                        }
                    }
                };
            }

        };
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingListener(this);
    }


    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }


    @Override
    protected void doListData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
//        if (id == R.id.tv_setting) {   //解绑
//            JumpUtil.overlay(getContext(), UntiedBankActivity.class);
//        } else
//
        if (id == R.id.ll_add_bank) {    //添加银行卡
            JumpUtil.overlay(getContext(), AddBankActivity.class);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    //解除绑定
    private void delBank(String id, String tel, String code) {
        RetrofitApiFactory.createApi(MineApi.class)
                .delete_bank(id, tel, code)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        onRefresh();
                    }
                });
    }
}
