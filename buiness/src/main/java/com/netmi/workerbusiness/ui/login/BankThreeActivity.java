package com.netmi.workerbusiness.ui.login;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.BankThreeApi;
import com.netmi.workerbusiness.data.entity.walllet.BankBean;
import com.netmi.workerbusiness.databinding.ActivityBankThreeBinding;
import com.netmi.workerbusiness.databinding.ActivityBankthreeListBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;


public class BankThreeActivity extends BaseActivity<ActivityBankThreeBinding> {

    private RecyclerView rvDankThree;
    private BaseRViewAdapter<BankBean, BaseViewHolder> bankAdapter;
    public int position;
    protected List<BankBean> list;

    public static final String BANKTHREE_NAME = "bankthree_name";
    public static final String BANKTHREE_CODE = "bankthree_code";
    public static final String BANKTHREE_BANKID = "bankthree_bankid";

    @Override
    protected int getContentView() {
        return R.layout.activity_bank_three;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("银行列表");
        rvDankThree = mBinding.rvDankThree;
        rvDankThree.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDankThree.setAdapter(bankAdapter = new BaseRViewAdapter<BankBean, BaseViewHolder>(getContext()) {

            @Override
            public int layoutResId(int viewType) {
                return R.layout.activity_bankthree_list;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<BankBean>(binding) {
                    @Override
                    public void bindData(BankBean item) {
                        super.bindData(item);
                        ActivityBankthreeListBinding binding1 = (ActivityBankthreeListBinding)getBinding();
                        binding1.tvItemBank.setText(item.getName());
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        Intent intent = new Intent();
                        intent.putExtra(BANKTHREE_NAME, items.get(position).getName());
                        intent.putExtra(BANKTHREE_CODE, items.get(position).getCode());
                        intent.putExtra(BANKTHREE_BANKID, items.get(position).getId());
                        setResult(2000, intent);
                        finish();
                    }
                };
            }
        });
    }

    @Override
    protected void initData() {
        doListBank("");
    }

    private void doListBank(String bank) {
        RetrofitApiFactory.createApi(BankThreeApi.class)
                .getBankThreeConvert(bank)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<BankBean>>>() {
                               @Override
                               public void onSuccess(BaseData<PageEntity<BankBean>> data) {
                                   bankAdapter.setData(data.getData().getList());
                               }
                           }
                );
    }



}
