package com.netmi.workerbusiness.ui.home.commodity.postage;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.CityChoiceEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityAddAreaBinding;
import com.netmi.workerbusiness.databinding.ItemAddPostageAreaBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class AddAreaActivity extends BaseActivity<ActivityAddAreaBinding> {

    public final static int SELECT_AREA = 1001;
    public final static String AREA_STR = "area_str";
    public final static String AREA_ID_LIST = "area_id_list";
    public final static String UPDATE_POSITION = "update_position";

    private RecyclerView recyclerView;
    private BaseRViewAdapter adapter;
    private List<CityChoiceEntity> cityChoiceEntities = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_add_area;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("添加地区");
        getRightSetting().setText("保存");
        initRecycleview();
    }

    private void initRecycleview() {
        recyclerView = mBinding.rvData;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new BaseRViewAdapter<CityChoiceEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_add_postage_area;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        ((ItemAddPostageAreaBinding) getBinding()).cbCheck.setChecked(getItem(position).isChecked());
                        ((ItemAddPostageAreaBinding) getBinding()).cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                cityChoiceEntities.get(position).setChecked(isChecked);
                            }
                        });
                    }
                };
            }

        });
        doGetProvince();
    }

    @Override
    protected void initData() {

    }

    private void doGetProvince() {
        showProgress("");
        RetrofitApiFactory.createApi(CommonApi.class)
                .listCityPostage(0, 1)
                .compose(RxSchedulers.<BaseData<List<CityChoiceEntity>>>compose())
                .compose((this).<BaseData<List<CityChoiceEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<List<CityChoiceEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<List<CityChoiceEntity>> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE
                                && data.getData() != null) {
                            cityChoiceEntities.addAll(data.getData());
                            adapter.setData(cityChoiceEntities);
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

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_setting) {//点击保存
            boolean isChecked = false;
            for (CityChoiceEntity cityChoiceEntity : cityChoiceEntities) {
                if (cityChoiceEntity.isChecked()) {
                    isChecked = cityChoiceEntity.isChecked();
                    break;
                }
            }
            if (isChecked) {
                StringBuffer str_area = new StringBuffer();
                ArrayList<String> region_list = new ArrayList<>();
                for (CityChoiceEntity cityChoiceEntity : cityChoiceEntities) {
                    if (cityChoiceEntity.isChecked()) {
                        if (TextUtils.isEmpty(str_area)) {
                            str_area.append(cityChoiceEntity.getName());
                        } else {
                            str_area.append("、" + cityChoiceEntity.getName());
                        }
                        region_list.add(cityChoiceEntity.getId());
                    }

                }
                Intent intent = new Intent();
                if (getIntent().getIntExtra(UPDATE_POSITION, -1) != -1) {
                    intent.putExtra(UPDATE_POSITION, getIntent().getIntExtra(UPDATE_POSITION, -1));
                }
                intent.putExtra(AREA_STR, str_area.toString());
                intent.putStringArrayListExtra(AREA_ID_LIST, region_list);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtils.showShort("请选择地区");
            }
        }
    }
}
