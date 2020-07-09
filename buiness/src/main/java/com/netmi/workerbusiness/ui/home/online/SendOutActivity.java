package com.netmi.workerbusiness.ui.home.online;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.netmi.workerbusiness.data.entity.home.lineorder.LineOrderDetailEntity;
import com.netmi.workerbusiness.data.entity.home.lineorder.LineOrderListEntity;
import com.netmi.workerbusiness.data.entity.home.lineorder.LogisticsCommand;
import com.netmi.workerbusiness.data.entity.home.lineorder.LogisticsEntity;
import com.netmi.workerbusiness.data.entity.home.lineorder.StoreExpressEntity;
import com.netmi.workerbusiness.data.entity.home.postage.PostageTempleItemEntity;
import com.netmi.workerbusiness.data.entity.home.postage.TempleGroupNameEntity;
import com.netmi.workerbusiness.databinding.ActivitySendOutBinding;
import com.netmi.workerbusiness.databinding.ItemAddPostageTempleBinding;
import com.netmi.workerbusiness.databinding.ItemLogisticsSendBinding;
import com.netmi.workerbusiness.databinding.ItemSendGoodBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class SendOutActivity extends BaseActivity<ActivitySendOutBinding> {

    private LineOrderListEntity entity;
    private BaseRViewAdapter goodAdapter;
    private BaseRViewAdapter logisticsAdapter;
    private List<StoreExpressEntity> expressList = new ArrayList<>();
    List<LogisticsEntity> baseEntities = new ArrayList<>();

    private int position;
    private String code;
    private int type; //TYPE 为1 表示在订单列表页面进入， 为2 表示在订单详情页面进入


    @Override
    protected int getContentView() {
        return R.layout.activity_send_out;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("商家发货");
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        if (type == 1) {
            entity = (LineOrderListEntity) getIntent().getExtras().getSerializable(JumpUtil.VALUE);
        } else if (type == 2) {
            entity = (LineOrderDetailEntity) getIntent().getExtras().getSerializable(JumpUtil.VALUE);
        }
//        position = getIntent().getExtras().getInt(JumpUtil.TYPE);

        RecyclerView rvGoods = mBinding.rvGoods;
        rvGoods.setNestedScrollingEnabled(false);
        rvGoods.setLayoutManager(new LinearLayoutManager(this));
        goodAdapter = new BaseRViewAdapter<LineOrderListEntity.MainOrdersBean.OrderSkusBean, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_send_good;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        ItemSendGoodBinding itemSendGoodBinding = (ItemSendGoodBinding) binding;
                        LineOrderListEntity.MainOrdersBean.OrderSkusBean entity = items.get(position);
                        if (entity.getStatus() != 1) {
                            itemSendGoodBinding.llGood.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                    }
                };
            }
        };
        goodAdapter.setData(entity.getMainOrders().get(0).getOrderSkus());
        rvGoods.setAdapter(goodAdapter);

        RecyclerView recyclerView = mBinding.rvLogistics;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(logisticsAdapter = new BaseRViewAdapter<LogisticsEntity, BaseViewHolder>(this) {

            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_logistics_send;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        new InputListenView(mBinding.tvConfirm, ((ItemLogisticsSendBinding) (getBinding())).etNum,
                                ((ItemLogisticsSendBinding) (getBinding())).etRemark) {
                            @Override
                            public void afterTextChanged(Editable editable) {
                                super.afterTextChanged(editable);
                                baseEntities.get(position).setLogistics_no(((ItemLogisticsSendBinding) (getBinding())).etNum.getText().toString());
                                baseEntities.get(position).setLogistics_remark(((ItemLogisticsSendBinding) (getBinding())).etRemark.getText().toString());
                            }
                        };
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (expressList.isEmpty()) {
                            doListExpress((ItemLogisticsSendBinding) (getBinding()), position);
                        } else {
                            showExpressCompany((ItemLogisticsSendBinding) (getBinding()), position);
                        }
                    }
                };
            }
        });

    }

    @Override
    protected void initData() {
        LogisticsEntity logisticsEntity = new LogisticsEntity();
        baseEntities.add(logisticsEntity);
        logisticsAdapter.setData(baseEntities);
    }

    private boolean canSend = true;

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_confirm) {   //提交发货
            canSend = true;
            for (int i = 0; i < baseEntities.size(); i++) {
                if (baseEntities.get(i).getLogistics_no() == null || baseEntities.get(i).getLogistics_company_code() == null) {
                    canSend = false;
                }
            }
            if (canSend) {
                LogisticsCommand logisticsCommand = new LogisticsCommand();
                logisticsCommand.setOrder_id(entity.getOrder_id());
                logisticsCommand.setLogistics(baseEntities);
                send(logisticsCommand);
            } else {
                showError("请填写所有信息");
            }

        } else if (id == R.id.iv_add) {
            LogisticsEntity logisticsEntity = new LogisticsEntity();
            baseEntities.add(logisticsEntity);
            logisticsAdapter.insert(logisticsEntity);
        }
    }

    private void doListExpress(ItemLogisticsSendBinding binding, int position) {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .listExpress("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<List<StoreExpressEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<List<StoreExpressEntity>> data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            if (!Strings.isEmpty(data.getData())) {
                                expressList.addAll(data.getData());
                            }
                            showExpressCompany(binding, position);
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void showExpressCompany(ItemLogisticsSendBinding binding, int position) {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (int options1, int option2, int options3, View v) -> {
            binding.tvCompany.setText(expressList.get(options1).getName());
            code = expressList.get(options1).getLogistics_company_code();
            baseEntities.get(position).setLogistics_company_code(code);
            binding.tvCompany.setTag(R.id.tag_data, expressList.get(options1).getCode());
            logisticsAdapter.notifyDataSetChanged();
        }).build();
        pvOptions.setPicker(expressList);
        pvOptions.setSelectOptions(0);
        pvOptions.show();
    }

    private void send(LogisticsCommand logisticsCommand) {
        RetrofitApiFactory.createApi(StoreApi.class)
                .sendOut(logisticsCommand)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {

                    @Override
                    public void onSuccess(BaseData data) {
                        finish();
                    }

                });
    }

}
