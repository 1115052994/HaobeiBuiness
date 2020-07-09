package com.netmi.workerbusiness.ui.home.commodity.category.spec;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.netmi.workerbusiness.data.entity.home.linecommodity.createcommofity.CreateGoodCommand;
import com.netmi.workerbusiness.data.entity.home.store.SpecDetailEntity;
import com.netmi.workerbusiness.databinding.ActivityCategorySpecificationCompleteBinding;
import com.netmi.workerbusiness.databinding.ItemCategorySpecificationCompleteBinding;
import com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.netmi.workerbusiness.ui.home.commodity.category.MyCreateStoreOrderFragment.SHELF_PRICE_UPDATE;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.CREATE_GOOD_DETAI;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.CREATE_GOOD_FROM;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.GOOD_ID;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.SHELF_LIST;

public class CategorySpecificationCompleteActivity extends BaseActivity<ActivityCategorySpecificationCompleteBinding> {

    private ArrayList<SpecDetailEntity> list;
    private BaseRViewAdapter<SpecDetailEntity, BaseViewHolder> adapter;
    private int resultCode = 0;
    private boolean isPrice;
    private CreateGoodCommand createGoodCommand;
    private String goodID = "";

    @Override
    protected int getContentView() {
        return R.layout.activity_category_specification_complete;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("商品规格");
        getRightSetting().setText("保存");
        goodID = getIntent().getExtras().getString(JumpUtil.ID);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseRViewAdapter<SpecDetailEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_category_specification_complete;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<SpecDetailEntity>(binding) {
                    @Override
                    public void bindData(SpecDetailEntity item) {
                        super.bindData(item);
                        final ItemCategorySpecificationCompleteBinding itemBinding = (ItemCategorySpecificationCompleteBinding) binding;
//                        itemBinding.etDiscount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                            @Override
//                            public void onFocusChange(View view, boolean b) {
//                                if (!b) {//失去焦点
//                                    if (Strings.roundTwoDecimals(Strings.toDouble(itemBinding.etDiscount.getText().toString())) >= 0.05
//                                            && Strings.roundTwoDecimals(Strings.toDouble(itemBinding.etDiscount.getText().toString())) <= 1.0) {
//                                        itemBinding.etDiscount.setText("" + Strings.roundTwoDecimals(Strings.toDouble(itemBinding.etDiscount.getText().toString())));
//                                    } else {
//                                        ToastUtils.showShort("营销系数需大于等于0.05 且小于等于1");
//                                        itemBinding.etDiscount.setText("");
//                                    }
//                                }
//                            }
//                        });
                        itemBinding.etStock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                list.get(position).setStock(itemBinding.etStock.getText().toString());
                            }
                        });

                        itemBinding.etPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                list.get(position).setPrice(itemBinding.etPrice.getText().toString());
                            }
                        });


                        itemBinding.ivDelete.setVisibility(isPrice ? View.GONE : View.VISIBLE);
//                        if (isPrice) {
//                            itemBinding.etDiscount.setEnabled(false);
//                            itemBinding.etDiscount.setTextColor(Color.parseColor("#cccccc"));
//                        } else {
//                            itemBinding.etDiscount.setEnabled(true);
//                            itemBinding.etDiscount.setTextColor(Color.parseColor("#555555"));
//                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId() == R.id.iv_delete) {
                            adapter.remove(position);
                            list.remove(position);
                        }
                    }
                };
            }
        };

        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_setting) {
            if (check()) {
                if (getIntent().getExtras().getParcelableArrayList(SHELF_PRICE_UPDATE) != null) {
                    //从specification选择后返回进行展示的处理
                    createGoodCommand = (CreateGoodCommand) getIntent().getSerializableExtra(CREATE_GOOD_DETAI);
                    createGoodCommand.setItem_id(getIntent().getStringExtra(GOOD_ID));
                    List<CreateGoodCommand.SpecBean> specBeanList = new ArrayList<>();
                    for (SpecDetailEntity entity : list) {
                        CreateGoodCommand.SpecBean specBean = new CreateGoodCommand.SpecBean();
                        specBean.setDiscount(entity.getDiscount());
                        specBean.setPrice(entity.getPrice());
                        specBean.setStock(entity.getStock());
                        specBean.setValue_ids(entity.getValue_ids());
                        specBean.setValue_names(entity.getValue_names());
                        specBeanList.add(specBean);
                    }
//                        createGoodCommand.setSpec(specBeanList);
                    createGoodCommand.setItem_value_list(specBeanList);
                    doGetUpdateShelf(createGoodCommand);
                } else {
                    resultCode = JumpUtil.SUCCESS;
                    onBackPressed();
                }
            }
        } else if (id == R.id.btn_add) {
            onBackPressed();
        }
    }


    public void doGetUpdateShelf(CreateGoodCommand createGoodCommand) {
        RetrofitApiFactory.createApi(StoreApi.class)
                .getUpdateGood(createGoodCommand)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            ToastUtils.showShort("编辑成功");
//                            EventBus.getDefault().post(new ShelfUpdateEvent());
                            finish();
                        } else {
                            ToastUtils.showShort(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        KeyboardUtils.hideKeyBoardForce(getContext());
        Intent it = new Intent();
        Bundle args = new Bundle();
        args.putParcelableArrayList(JumpUtil.VALUE, list);
        it.putExtras(args);
        setResult(resultCode, it);
        super.onBackPressed();

    }

    private boolean check() {
        for (int i = 0; i < list.size(); i++) {
            SpecDetailEntity item = list.get(i);
            if (Strings.toInt(item.getStock()) < 0) {
                showError("库存不能小于0");
                return false;
            }
            if (Strings.toDouble(item.getPrice()) <= 0) {
                showError("价格不能小于0");
                return false;
            }
//            double discount = Strings.toDouble(item.getDiscount());
//            if (discount < 0.05 || discount > 1) {
//                showError("请输入合理的营销系数,在0.05到1之间");
//                return false;
//            }

        }
        return true;
    }

    @Override
    protected void initData() {
        if (getIntent().getExtras().getParcelableArrayList(SHELF_PRICE_UPDATE) != null) {
            list = getIntent().getExtras().getParcelableArrayList(SHELF_PRICE_UPDATE);
            isPrice = true;
        } else {
            list = getIntent().getExtras().getParcelableArrayList(JumpUtil.VALUE);
            isPrice = false;
        }
        if (isPrice) {
            mBinding.btnAdd.setVisibility(View.GONE);
        }
        if (list != null && !list.isEmpty()) {
            adapter.setData(list);
            Log.e("weng", list.get(0).getValue_ids() + "");
            Log.e("weng", list.get(0).getDiscount() + "");
            Log.e("weng", list.get(0).getPrice() + "");
            Log.e("weng", list.get(0).getStock() + "");
            Log.e("weng", list.get(0).getValue_names() + "");
        } else {
            showError("未知错误");
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //隐藏软键盘
//        KeyboardUtils.hideKeyBoardForce(getContext());
    }
}
