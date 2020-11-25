package com.netmi.workerbusiness.ui.mine;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.CreateContractEntity;
import com.netmi.baselibrary.data.entity.TemplateListEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MessApi;
import com.netmi.workerbusiness.databinding.ActivityContractExamineInfoBinding;
import com.netmi.workerbusiness.databinding.ContractExamineListItemBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

//合同管理列表
public class ContractExamineListActivity extends BaseXRecyclerActivity<ActivityContractExamineInfoBinding,TemplateListEntity> {

    @Override
    protected int getContentView() {
        return R.layout.activity_contract_examine_info;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("合同列表");
        initRecyclerView();


    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if(view.getId()==R.id.ll_back){
            finish();
        }
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

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MessApi.class)
                .templateList("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<TemplateListEntity>>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<List<TemplateListEntity>> data) {
                        showData(data.getData());
                    }
                });
    }

    private void initRecyclerView() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);
        adapter = new BaseRViewAdapter<TemplateListEntity, BaseViewHolder>(getContext()) {

            @Override
            public int layoutResId(int viewType) {
                return R.layout.contract_examine_list_item;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<TemplateListEntity>(binding) {
                    @Override
                    public void bindData(TemplateListEntity item) {
//                        PublicNoticeEntity entity = items.get(position);
                        super.bindData(item);//不能删
                        ContractExamineListItemBinding itemOff = (ContractExamineListItemBinding) binding;
                        if(item.getStatus()==2){
                            itemOff.tvText.setText(item.getTitle());
                            itemOff.tvTextTime.setText("签署时间："+item.getAdd_time());
                        }else {
                            itemOff.topLilayout.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int id = view.getId();
                        if(id == R.id.top_relayout){
                            //查看
                            examine(getItems().get(position).getContract_id());
                        }else if(id == R.id.bottom_relayout){
                            //下载合同
                            downExamine(getItems().get(position).getContract_id());
                        }


                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }
    //查看合同
    public void examine(String contract_id){
        RetrofitApiFactory.createApi(MessApi.class)
                .getPreviewUrl(contract_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<CreateContractEntity>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<CreateContractEntity> data) {
                        String url = data.getData().getUrl();
                        startInit(url);
                    }
                });
    }
    //下载合同
    public void downExamine(String contract_id){
        RetrofitApiFactory.createApi(MessApi.class)
                .downloadContract(contract_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<CreateContractEntity>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<CreateContractEntity> data) {
                        String url = data.getData().getUrl();
                        startInit(url);
                    }
                });
    }
    public void startInit(String url){
        // 外部浏览器
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri content = Uri.parse(url);
        intent.setData(content);
        startActivity(intent);
    }
}
