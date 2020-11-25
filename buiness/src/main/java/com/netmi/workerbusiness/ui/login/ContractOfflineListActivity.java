package com.netmi.workerbusiness.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.CreateContractEntity;
import com.netmi.baselibrary.data.entity.TemplateListEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.widget.ContractAdapter;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.LoginApi;
import com.netmi.workerbusiness.data.api.MessApi;
import com.netmi.workerbusiness.databinding.ActivityContractOfflineInfoBinding;
import com.netmi.workerbusiness.ui.MainActivity;
import com.netmi.workerbusiness.widget.ContractVerifyDialog;
import com.sobot.chat.activity.WebViewActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

//线下合同列表
public class ContractOfflineListActivity extends BaseActivity<ActivityContractOfflineInfoBinding> {


    private ContractAdapter contractAdapter;
    private List<TemplateListEntity> list = new ArrayList<>();
    private AlertDialog alertDialog;
    String edit;
    private List<String> shop_url = new ArrayList<>();
    private List<String> img_url = new ArrayList<>();
    private String license_url = "";

    @Override
    protected int getContentView() {
        return R.layout.activity_contract_offline_info;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("合同列表");
        mBinding.listItem.setAdapter(contractAdapter = new ContractAdapter(this, R.layout.contract_list_item, list));
        mBinding.listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                doData(list.get(i).getTid());
            }
        });
        edit = getIntent().getStringExtra("edit");
        if (edit.equals("1")) {
            ContractVerifyDialog.show(getActivity());
            ContractVerifyDialog.setlict(new ContractVerifyDialog.MyListener() {
                @Override
                public void getData(String res) {
                    if (TextUtils.isEmpty(res)) {
                        showError("请输入正确验证码");
                        return;
                    }else if (res.length() != 6 ) {
                        showError("请输入6位验证码");
                        return;
                    }
                    doVerified(res);
                }
            });

        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        if (view.getId() == R.id.ll_back) {
            finish();
        } else if (view.getId() == R.id.tv_confirm) {
            intStart();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doListData();
    }

    protected void doListData() {
        RetrofitApiFactory.createApi(MessApi.class)
                .templateList("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<TemplateListEntity>>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<List<TemplateListEntity>> data) {
                        list.clear();
                        list.addAll(data.getData());
                        contractAdapter.notifyDataSetChanged();
                    }
                });
    }

    protected void doData(String tid) {
        RetrofitApiFactory.createApi(MessApi.class)
                .createContract(tid)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<CreateContractEntity>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<CreateContractEntity> data) {
                        Bundle binder = new Bundle();
                        String shortUrl = data.getData().getShortUrl();
                        binder.putString("url", shortUrl);
                        JumpUtil.overlay(ContractOfflineListActivity.this, WebViewActivity.class, binder);
                    }
                });
    }

    public void intStart() {
        showProgress("");
        RetrofitApiFactory.createApi(MessApi.class)
                .confirmContract("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<String>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<String> data) {
                        //提交
                        hideProgress();
                        JumpUtil.overlay(getContext(), MainActivity.class);
                        finish();
                    }

                    @Override
                    public void onFail(BaseData data) {
                        showError(data.getErrmsg());
                        hideProgress();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContractVerifyDialog.dismiss();
        EventBus.getDefault().unregister(this);
    }

    private void doVerified(String code) {
        RetrofitApiFactory.createApi(LoginApi.class)
                .getCreateSignInfo(code)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {

                    }
                });

    }


}

