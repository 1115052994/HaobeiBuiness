package com.liemi.basemall.ui.personal.address;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.AddressEntity;
import com.liemi.basemall.databinding.ActivityAddressAddBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.CityChoiceEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.CityPickerView;
import com.netmi.baselibrary.widget.MLoadingDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;
import java.util.List;
import io.reactivex.annotations.NonNull;
import static com.liemi.basemall.ui.personal.address.AddressManageActivity.ADDRESS_ENTITY;

/**
 * 类描述：添加新地址或者编辑地址
 * 创建人：Simple
 * 创建时间：2018/1/19 11:31
 * 修改备注：
 */
public class AddressAddActivity extends BaseActivity<ActivityAddressAddBinding> {

    private AddressEntity addressEntity;

    private CityPickerView cityPickerView;
    @Override
    protected int getContentView() {
        return R.layout.activity_address_add;
    }

    @Override
    protected void initUI() {
        addressEntity = (AddressEntity) getIntent().getSerializableExtra(ADDRESS_ENTITY);
        if (addressEntity != null) {
            getTvTitle().setText("编辑地址");
            mBinding.tvBelongArea.setText((addressEntity.getPname()
                    + "-" + addressEntity.getCname()
                    + "-" + addressEntity.getDname()));

            mBinding.cbDefault.setChecked(addressEntity.getIs_top() == 1);
            mBinding.setItem(addressEntity);
        } else {
            getTvTitle().setText("添加地址");
        }
        getRightSetting().setText("保存");
    }

    @Override
    protected void initData() {
        cityPickerView = new CityPickerView(this);
        doGetProvince();
    }

    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.ll_belong_area) {
            if (!cityPickerView.getProvinceList().isEmpty()) {
                KeyboardUtils.hideKeyboard(view);
                cityPickerView.show(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        if (addressEntity != null) {
                            CityChoiceEntity pEntity = cityPickerView.getChoiceProvince();
                            CityChoiceEntity.CListBean cEntity = cityPickerView.getChoiceCity();
                            CityChoiceEntity.CListBean.DListBean dEntity = cityPickerView.getChoiceArea();
                            addressEntity.setP_id(pEntity.getId());
                            addressEntity.setC_id(cEntity.getId());
                            addressEntity.setD_id(dEntity.getId());
                            addressEntity.setPname(pEntity.getName());
                            addressEntity.setCname(cEntity.getName());
                            addressEntity.setDname(dEntity.getName());
                        }
                        mBinding.tvBelongArea.setText((cityPickerView.getChoiceProvince().getName()
                                + "-" + cityPickerView.getChoiceCity().getName()
                                + "-" + cityPickerView.getChoiceArea().getName()));
                    }
                });


            } else {
                //城市加载中
                showProgress("");
                if (loadProvinceError)
                    doGetProvince();
            }
        } else if (view.getId() == R.id.tv_setting) {
            String name = mBinding.etName.getText().toString();
            String phone = mBinding.etTel.getText().toString();
            String city = mBinding.tvBelongArea.getText().toString();
            String address = mBinding.etDetailAddress.getText().toString();
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showShort("请先输入联系人");
            } else if (TextUtils.isEmpty(phone)) {
                ToastUtils.showShort("请先输入手机号");
            } else if (TextUtils.isEmpty(city)) {
                ToastUtils.showShort("请先选择所在地区");
            } else if (TextUtils.isEmpty(address)) {
                ToastUtils.showShort("请先输入详细地址");
            } else if (!Strings.isPhone(phone)) {
                ToastUtils.showShort("请输入正确的手机号码");
            } else {
                if (addressEntity == null) {
                    doAddressSave(name, cityPickerView.getChoiceProvince().getId(),
                            cityPickerView.getChoiceCity().getId(), cityPickerView.getChoiceArea().getId(),
                            phone, address, mBinding.cbDefault.isChecked() ? 1 : 0);
                } else {
                    addressEntity.setFull_name(mBinding.tvBelongArea.getText() + address);
                    doAddressEdit(name, addressEntity.getP_id(),
                            addressEntity.getC_id(), addressEntity.getD_id(),
                            phone, address, mBinding.cbDefault.isChecked() ? 1 : 0);
                }
            }

        }

    }

    //加载失败后， 点击可再次加载
    private boolean loadProvinceError = false;

    private void doGetProvince() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .listCity(1)
                .compose(RxSchedulers.<BaseData<List<CityChoiceEntity>>>compose())
                .compose((this).<BaseData<List<CityChoiceEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<List<CityChoiceEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        loadProvinceError = true;
                    }

                    @Override
                    public void onNext(@NonNull BaseData<List<CityChoiceEntity>> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE
                                && data.getData() != null) {

                            //组装数据
                            cityPickerView.loadCityData(data);
                            if (MLoadingDialog.isShow()) {
                                hideProgress();
                                mBinding.tvBelongArea.performClick();
                            }

                        } else {
                            showError(data.getErrmsg());
                            loadProvinceError = true;
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }
    //保存新地址
    private void doAddressSave(String name, String pid, String cid, String did, String tel, String address, int isTop) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doAddNewAddress(name, pid, cid, did, tel, address, isTop)
                .compose(RxSchedulers.<BaseData<AddressEntity>>compose())
                .compose((this).<BaseData<AddressEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<AddressEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<AddressEntity> data) {
                        Intent intent = new Intent();
                        intent.putExtra(ADDRESS_ENTITY, data.getData());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }
    //保存编辑后的地址
    private void doAddressEdit(final String name, String pid, String cid, String did, final String tel, final String address, int isTop) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUpdateAddress(addressEntity.getMaid(), name, pid, cid, did, tel, address, isTop,null)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        addressEntity.setName(name);
                        addressEntity.setTel(tel);
                        addressEntity.setAddress(address);
                        Intent intent = new Intent();
                        intent.putExtra(ADDRESS_ENTITY, addressEntity);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }

}
