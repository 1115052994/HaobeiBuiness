package com.netmi.workerbusiness.ui.home.commodity.coupon;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.CommodityApi;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.netmi.workerbusiness.data.entity.home.linecommodity.LineCommodityListEntity;
import com.netmi.workerbusiness.data.entity.home.postage.ServiceDesEntity;
import com.netmi.workerbusiness.data.entity.home.store.StoreCateEntity;
import com.netmi.workerbusiness.databinding.ActivityServiceDesciptionBinding;
import com.netmi.workerbusiness.databinding.ItemLineCommodityBinding;
import com.netmi.workerbusiness.databinding.ItemServiceDescriptionBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

public class ServiceDesciptionActivity extends BaseActivity<ActivityServiceDesciptionBinding> {

    public static final String SERVICE_DES = "service_description";
    private RecyclerView recyclerView;
    private BaseRViewAdapter adapter;
    ArrayList<String> chooseList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_service_desciption;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("服务描述");
        getRightSetting().setText("保存");
        getList();
        chooseList.clear();
        chooseList = getIntent().getStringArrayListExtra(SERVICE_DES);
        initRecyclerView();

    }


    private void initRecyclerView() {
        adapter = new BaseRViewAdapter<ServiceDesEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_service_description;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<ServiceDesEntity>(binding) {
                    @Override
                    public void bindData(ServiceDesEntity item) {
                        super.bindData(item);//不能删
                        ItemServiceDescriptionBinding itemBinding = (ItemServiceDescriptionBinding) binding;
                        ServiceDesEntity entity = items.get(position);
                        if (chooseList.size() > 0) {
                            for (int i = 0; i < chooseList.size(); i++) {
                                if (entity.getId().equals(chooseList.get(i))) {
                                    entity.setRed(true);
                                    itemBinding.iv.setImageResource(R.mipmap.ic_check_red);
//                                } else {
//                                    entity.setRed(false);
//                                    itemBinding.iv.setImageResource(R.mipmap.ic_uncheck_white);
                                }
                            }
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        ItemServiceDescriptionBinding itemBinding = (ItemServiceDescriptionBinding) binding;
                        ServiceDesEntity entity = items.get(position);
                        //改变UI
                        if (entity.isRed()) {
                            itemBinding.iv.setImageResource(R.mipmap.ic_uncheck_white);
                            //todo 删除的逻辑
                            for (int i = 0; i < chooseList.size(); i++) {
                                if (entity.getId().equals(chooseList.get(i))) {
                                    chooseList.remove(i);
                                }
                            }
                            entity.setRed(false);
                        } else if (!entity.isRed()) {
                            itemBinding.iv.setImageResource(R.mipmap.ic_check_red);
                            chooseList.add(entity.getId());
                            entity.setRed(true);
                        }
                    }
                };
            }

        };
        recyclerView = mBinding.rvData;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }


    @Override
    protected void initData() {

    }


    protected void getList() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .serviceDes("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<ServiceDesEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<ServiceDesEntity>> data) {
                        adapter.setData(data.getData().getList());
                    }
                });
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_setting) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(SERVICE_DES, chooseList);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
