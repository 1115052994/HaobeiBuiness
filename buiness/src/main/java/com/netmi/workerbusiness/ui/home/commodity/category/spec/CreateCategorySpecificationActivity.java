package com.netmi.workerbusiness.ui.home.commodity.category.spec;

import android.os.Build;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.netmi.workerbusiness.data.entity.home.store.StoreCateEntity;
import com.netmi.workerbusiness.databinding.ActivityCreateCategorySpecificationBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

public class CreateCategorySpecificationActivity extends BaseActivity<ActivityCreateCategorySpecificationBinding> {


    private List<String> typeList;
    private List<String> preList;
    private ArrayList<StoreCateEntity> data;
    private int typeOption = -1;
    private int preLevelOption = -1;

    @Override
    protected int getContentView() {
        return R.layout.activity_create_category_specification;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("商品规格");
        getRightSetting().setText("保存");
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        KeyboardUtils.hideKeyboard(view);
        int id = view.getId();
        if (id == R.id.tv_setting) {
            check();
        } else if (id == R.id.ll_type) {//条件选择器
            OptionsPickerView typeView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    typeOption = options1;

                    if (typeOption == 0) {
                        mBinding.llPreLevel.setVisibility(View.GONE);
                    } else {
                        mBinding.llPreLevel.setVisibility(View.VISIBLE);
                    }
                    mBinding.tvType.setText(typeList.get(typeOption));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        TransitionManager.beginDelayedTransition(mBinding.llType);
                    }
                }
            })
                    .setSubCalSize(14)
                    .setTitleText("请选择类型")
                    .build();
            typeView.setSelectOptions(typeOption < 0 ? 0 : typeOption);
            typeView.setPicker(typeList);
            typeView.show();
        } else if (id == R.id.ll_pre_level) {//条件选择器
            OptionsPickerView pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    preLevelOption = options1;
                    mBinding.tvPreLevel.setText(preList.get(preLevelOption));

                }
            })
                    .setSubCalSize(14)
                    .setTitleText("选择上级")
                    .build();
            pickerView.setSelectOptions(preLevelOption < 0 ? 0 : preLevelOption);
            pickerView.setPicker(preList);
            pickerView.show();
        }
    }

    private void check() {
        String valueName = mBinding.etName.getText().toString();
        String pid = null;
        if (TextUtils.isEmpty(valueName)) {
            showError("请输入规格名称");
            return;
        }
        if (typeOption < 0) {
            showError("请选择类型");
            return;
        }
        if (typeOption == 1) {
            if (preLevelOption < 0) {
                showError("请选择上级");
                return;
            } else {
                pid = data.get(preLevelOption).getProp_id();
                //新建二级规格
                doCreateTwo(pid, valueName);
            }
        } else {
            //新建一级规格
            doCreateOne(valueName, null);
        }
    }

    private void doCreateOne(String valueName, String pid) {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .createCateOne(valueName, pid)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            showError("新增成功");
                            setResult(JumpUtil.SUCCESS);
                            finish();
                        } else
                            showError(data.getErrmsg());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doCreateTwo(String prop_id, String value_name) {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .createCateTwo(prop_id, value_name, null, null, null)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            showError("新增成功");
                            setResult(JumpUtil.SUCCESS);
                            finish();
                        } else
                            showError(data.getErrmsg());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    @Override
    protected void initData() {
        typeList = new ArrayList();
        typeList.add("一级规格");
        typeList.add("二级规格");
        data = getIntent().getExtras().getParcelableArrayList(JumpUtil.VALUE);
        preList = new ArrayList();
        for (int i = 0; i < data.size(); i++) {
            preList.add(data.get(i).getProp_name());
        }
    }
}
