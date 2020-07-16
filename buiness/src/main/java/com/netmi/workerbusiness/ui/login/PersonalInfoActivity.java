package com.netmi.workerbusiness.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.liemi.basemall.data.api.MineApi;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.CityChoiceEntity;
import com.netmi.baselibrary.data.entity.OssConfigureEntity;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;


import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.ImageUploadUtils;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.SystemUtil;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.netmi.baselibrary.widget.CityPickerView;
import com.netmi.baselibrary.widget.MLoadingDialog;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.LoginApi;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.data.event.LocationEvent;
import com.netmi.workerbusiness.data.event.StoreRemarkEvent;
import com.netmi.workerbusiness.databinding.ActivityPersonalInfoBinding;
import com.netmi.workerbusiness.ui.MainActivity;
import com.netmi.workerbusiness.ui.mine.StoreRemarkActivity;
import com.netmi.workerbusiness.ui.mine.wallet.BaiduMapActivity;
import com.netmi.workerbusiness.ui.utils.PermissionUtils;


import com.trello.rxlifecycle2.android.ActivityEvent;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.netmi.workerbusiness.ui.login.CategoryVerifyActivity.CATEGORY_ID;
import static com.netmi.workerbusiness.ui.login.CategoryVerifyActivity.CATEGORY_NAME;
import static com.netmi.workerbusiness.ui.mine.StoreInfoActivity.REQUEST_CHANGE_REMARK;

public class PersonalInfoActivity extends BaseActivity<ActivityPersonalInfoBinding> {

    //身份证正面 请求打开相册的requestCode
    private static final int REQUEST_OPEN_ALBUM_POSITIVE = 1002;
    //身份证反面
    private static final int REQUEST_OPEN_ALBUM_NEGATIVE = 1003;
    //手持身份证
    private static final int REQUEST_OPEN_ALBUM_HAND = 1011;
    //门店图片http://oss-cn-beijing.aliyuncs.com
    private static final int REQUEST_OPEN_ALBUM_SHOP_PIC = 1004;
    //环境图片
    private static final int REQUEST_OPEN_ALBUM_ENVIRONMENT_PIC = 1005;
    //营业执照
    private static final int REQUEST_OPEN_ALBUM_BUSINESS_LICENSE = 1006;
    //经营许可证
    private static final int REQUEST_OPEN_ALBUM_BUSINESS_LICENSE_TWO = 1007;
    //店铺logo
    private static final int REQUEST_OPEN_ALBUM_LOGO_PIC = 1008;
    //经营分类
    private static final int REQUEST_CATEGORY = 10010;

    private String shopName;
    private String real_name;
    private String idCard;
    private String remark;
    private String businessNumber;
    private String longitude;
    private String latitude;
    private String p_id;
    private String c_id;
    private String d_id;
    private String address;
    private String c_name;
    //经营分类id
    private String category_id;

    //身份证正面图片
    private String positiveUrl;
    //身份证反面图片
    private String negativeUrl;
    //手持身份证图片
    private String handtiveUrl;
    //门店图片
    private String shop_pic;
    //环境图片
    private String environment_pic;
    //营业执照
    private String buiness_license;
    //经营许可证
    private String buiness_license_two;
    //店铺Logo
    private String logo_pic;
    private int mCurrentStep = 0;//当前执行的操作，0位默认，1为请求相机，2为请求读写权限

    private int currentCode = -1;
    private String[] permissions = new String[]{PermissionUtils.PERMISSION_CAMERA, PermissionUtils.PERMISSION_READ_STORAGE, PermissionUtils.PERMISSION_WRITE_STORAGE};
    private boolean isVerified;
    //线上线下店铺选择页面传进来的type
    private String user_type_event = "0"; //1线上  2线下

    private CityPickerView cityPickerView;

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("实体商家");
        new InputListenView(mBinding.tvConfirm, mBinding.etShopName, mBinding.etName, mBinding.etIdCard) {

        };
        if (getIntent() != null && getIntent().getExtras() != null) {
            user_type_event = getIntent().getExtras().getString(JumpUtil.VALUE);
        }
        doGetUserInfo(1);
    }

    // 接收广播
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void remark(StoreRemarkEvent event) {
        mBinding.tvRemark.setText(event.getRemark());
        remark = event.getRemark();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void location(LocationEvent event) {
        longitude = event.getLongitude();
        latitude = event.getLatitude();
        p_id = event.getP_id();
        c_id = event.getC_id();
        d_id = event.getD_id();
        address = event.getAddress();
        mBinding.tvAddress.setText(event.getAddress());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void initData() {

        cityPickerView = new CityPickerView(this);
        doInitOssConfigure();
        //请求省市级
        doGetProvince();
    }

    @Override
    public void doClick(View view) {
        Bundle args = new Bundle();
        super.doClick(view);
        int code = -1;
        int id = view.getId();
        if (id == R.id.ll_back) {
            MApplication.getInstance().gotoLogin();
        } else if (id == R.id.rlPositive) {
            code = REQUEST_OPEN_ALBUM_POSITIVE;
        } else if (id == R.id.rlNegative) {
            code = REQUEST_OPEN_ALBUM_NEGATIVE;
        } else if (id == R.id.botPositive) {
            code = REQUEST_OPEN_ALBUM_HAND;
        } else if (id == R.id.rl_shop_pic) {
            code = REQUEST_OPEN_ALBUM_SHOP_PIC;
        } else if (id == R.id.rl_environment_pic) {
            code = REQUEST_OPEN_ALBUM_ENVIRONMENT_PIC;
        } else if (id == R.id.rl_business_pic) {
            code = REQUEST_OPEN_ALBUM_BUSINESS_LICENSE;
        } else if (id == R.id.rl_business_two_pic) {
            code = REQUEST_OPEN_ALBUM_BUSINESS_LICENSE_TWO;
        } else if (id == R.id.rl_logo_pic) {
            code = REQUEST_OPEN_ALBUM_LOGO_PIC;
        } else if (id == R.id.tv_confirm) {
            if (!SystemUtil.isFastDoubleClick()) {
                check();
            }
        }
        else if (id == R.id.ll_remark) {
            JumpUtil.startForResult(this, StoreRemarkActivity.class, REQUEST_CHANGE_REMARK, args);
        }
        else if (id == R.id.ll_time_choose) {
            showTimePicker();
        } else if (id == R.id.ll_location) {
//            JumpUtil.overlay(getContext(), LocationActivity.class, args);
            JumpUtil.overlay(getContext(), BaiduMapActivity.class);
        } else if(id == R.id.ll_location_city){
            startSelect(view);
        }

        else if (id == R.id.ll_category) {
            args.putString(JumpUtil.TYPE, user_type_event);
            JumpUtil.startForResult(this, CategoryVerifyActivity.class, REQUEST_CATEGORY, args);
        }
        currentCode = code;
        if (code != -1) {
            //查看是否拥有相机权限
            mCurrentStep = 1;
            if (new PermissionUtils().checkPermission(permissions, PersonalInfoActivity.this)) {
                openAlbum(code);
            } else {
                PermissionUtils.checkAndRequestPermissions(PersonalInfoActivity.this, permissions);
            }
        }
    }


    private void openAlbum(int code) {
        mCurrentStep = 0;
        ImagePicker.getInstance().setSelectLimit(1);
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQUEST_CODE) {
            if (new PermissionUtils().checkPermission(permissions, PersonalInfoActivity.this)) {
                openAlbum(currentCode);
            }
        }
    }

    //地区选择
    public void startSelect(View view){
        if (!cityPickerView.getProvinceList().isEmpty()) {
            KeyboardUtils.hideKeyboard(view);
            cityPickerView.show((int options1, int option2, int options3, View v) -> {
                StringBuilder builder = new StringBuilder();
                if (cityPickerView.getChoiceProvince() != null) {
                    builder.append(cityPickerView.getChoiceProvince().getName());
                }
                if (cityPickerView.getChoiceCity() != null) {
                    builder.append("-").append(cityPickerView.getChoiceCity().getName());
                }
                if (cityPickerView.getChoiceArea() != null) {
                    builder.append("-").append(cityPickerView.getChoiceArea().getName());
                }
                mBinding.tvAddressCity.setText(builder.toString());
            });
        } else {
            //城市加载中
            showProgress("");
            if (loadProvinceError)
                doGetProvince();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            ImageUploadUtils.uploadByOss(images, this, urls -> {
                if (currentCode == REQUEST_OPEN_ALBUM_POSITIVE) {
                    positiveUrl = urls.get(0);

                    mBinding.setIdTop(positiveUrl);
                    mBinding.ivPositive.setVisibility(View.INVISIBLE);
                } else if (currentCode == REQUEST_OPEN_ALBUM_NEGATIVE) {
                    negativeUrl = urls.get(0);

                    mBinding.setIdBottom(negativeUrl);
                    mBinding.ivNegative.setVisibility(View.INVISIBLE);
                } else if (currentCode == REQUEST_OPEN_ALBUM_HAND) {
                    handtiveUrl = urls.get(0);

                    mBinding.setHandPic(handtiveUrl);
                    mBinding.ivPositiveHand.setVisibility(View.INVISIBLE);
                }
                else if (currentCode == REQUEST_OPEN_ALBUM_SHOP_PIC) {
                    shop_pic = urls.get(0);

                    mBinding.setShopPic(shop_pic);
                    mBinding.ivShop.setVisibility(View.INVISIBLE);
                } else if (currentCode == REQUEST_OPEN_ALBUM_ENVIRONMENT_PIC) {
                    environment_pic = urls.get(0);

                    mBinding.setEnvironmentPic(environment_pic);
                    mBinding.ivEnvironment.setVisibility(View.INVISIBLE);
                } else if (currentCode == REQUEST_OPEN_ALBUM_BUSINESS_LICENSE) {
                    buiness_license = urls.get(0);

                    mBinding.setBusinessPic(buiness_license);
                    mBinding.ivBusiness.setVisibility(View.INVISIBLE);
                } else if (currentCode == REQUEST_OPEN_ALBUM_BUSINESS_LICENSE_TWO) {
                    buiness_license_two = urls.get(0);

                    mBinding.setBusinessTwoPic(buiness_license_two);
                    mBinding.ivBusinessTwo.setVisibility(View.INVISIBLE);
                } else if (currentCode == REQUEST_OPEN_ALBUM_LOGO_PIC) {
                    logo_pic = urls.get(0);

                    mBinding.setShopLogo(logo_pic);
                    mBinding.ivLogo.setVisibility(View.INVISIBLE);
                }
            }, null);

            if (requestCode == REQUEST_CATEGORY && resultCode == RESULT_OK) {
                category_id = data.getStringExtra(CATEGORY_ID);
                mBinding.tvCategory.setText(data.getStringExtra(CATEGORY_NAME));
            }
        }
    }


    private List<String> shop_url = new ArrayList<>();
    private List<String> img_url = new ArrayList<>();
    private String front_url;
    private String back_url;
    private String license_url = "";
    private String operation_url = "";


    private void check() {
        shopName = mBinding.etShopName.getText().toString();
        real_name = mBinding.etName.getText().toString();
        idCard = mBinding.etIdCard.getText().toString();
        remark = mBinding.etMerchantContent.getText().toString();
        businessNumber = mBinding.etBusinessNumber.getText().toString();
//        remark = mBinding.etMerchantContent().getText().toString();

        if (TextUtils.isEmpty(shopName)) {
            showError("请输入店铺名称");
        } else if(TextUtils.isEmpty(remark)){
            showError("请输入商家简介");
        }else if (TextUtils.isEmpty(idCard)) {
            showError("请输入法人身份证号");
        }else if (TextUtils.isEmpty(real_name)) {
            showError("请输入法人姓名");
        }else if (TextUtils.isEmpty(handtiveUrl)) {
            showError("请上传手持身份证");
        } else if (TextUtils.isEmpty(idCard)) {
            showError("请输入法人身份证号");
        }else if (TextUtils.isEmpty(businessNumber)) {
            showError("请输入营业执照编号");
        } else if (mBinding.ivPositiveHand.getVisibility() == View.VISIBLE) {
            showError("请上传手持身份证");
        } else if (mBinding.ivPositive.getVisibility() == View.VISIBLE) {
            showError("请上传身份证正面照");
        } else if (mBinding.ivNegative.getVisibility() == View.VISIBLE) {
            showError("请上传身份证背面照");
        } else if (mBinding.ivBusiness.getVisibility() == View.VISIBLE) {
            showError("请上传营业执照");
        } else if (mBinding.ivBusinessTwo.getVisibility() == View.VISIBLE) {
            showError("请上传经营许可");
        } else if (mBinding.ivLogo.getVisibility() == View.VISIBLE) {
            showError("请上传店铺logo");
        } else if (TextUtils.isEmpty(category_id)) {
            showError("请选择经营类目");
        } else {
            if (UserInfoCache.get().getShop_user_type().equals("2")) {
                if (mBinding.ivShop.getVisibility() == View.VISIBLE) {
                    showError("请上传门店图片");
                } else if (mBinding.ivEnvironment.getVisibility() == View.VISIBLE) {
                    showError("请上传店内环境图片");
                } else if (time.equals("")) {
                    showError("请选择事件");
                } else if (mBinding.tvAddress.getText().toString().equals("点击选择")) {
                    showError("请选择地址");
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("");
            builder.setMessage("请确认是否提交");
            builder.setPositiveButton("确定", (dialog, which) -> {
                shop_url.add(mBinding.getShopPic());
                img_url.add(mBinding.getEnvironmentPic());
                license_url = mBinding.getBusinessPic();
                operation_url = mBinding.getBusinessTwoPic();
                doVerified(shopName, logo_pic, remark, longitude, latitude, time, shop_url, img_url, positiveUrl, negativeUrl, license_url, real_name, idCard, operation_url, p_id, c_id, d_id, address, category_id,businessNumber,handtiveUrl);
                dialog.dismiss();
            });
            builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    }

    private void doVerified(String shopName, String logo_url, String content, String longitude, String latitude, String time
            , List<String> shop_url, List<String> img_url, String front_url, String back_url,
                            String license_url, String real_name, String idCard, String operation_url, String p_id, String c_id, String d_id, String address, String category_id,String license_num,String hold_card_url) {
        RetrofitApiFactory.createApi(LoginApi.class)
                .shopSettled(shopName, logo_url, content, longitude, latitude, time, shop_url, img_url.get(0), front_url, back_url, license_url, real_name, idCard, operation_url, p_id, c_id, d_id, address, category_id,license_num,hold_card_url)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new XObserver<BaseData>() {
                    public void onSuccess(BaseData baseData) {
                        doGetShopInfo();
                    }

                    @Override
                    public void onFail(BaseData data) {
                        super.onFail(data);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                    }
                });

    }

    private void doInitOssConfigure() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .getOssConfigure(0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<OssConfigureEntity>>() {
                    @Override
                    public void onSuccess(BaseData<OssConfigureEntity> data) {
                        OssUtils.initConfigure(data.getData());
                    }
                });
    }

    private void doGetUserInfo(int type) {
        RetrofitApiFactory.createApi(MineApi.class)
                .getUserInfo(0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        Log.e("weng2", user_type_event);
                        if (user_type_event.equals("1")) {
//                            mBinding.llShopPic.setVisibility(View.GONE);
                            mBinding.llEnvironmentPic.setVisibility(View.GONE);
//                            mBinding.llRemark.setVisibility(View.GONE);
                            mBinding.llTimeChoose.setVisibility(View.GONE);
                            mBinding.llLocation.setVisibility(View.GONE);
                        } else if (user_type_event.equals("0")) {
                            if (data.getData().getShop_user_type().equals("1")) {//	用户选择商户类型 0:未选择类型 1:线上 2:线下
//                                mBinding.llShopPic.setVisibility(View.GONE);
                                mBinding.llEnvironmentPic.setVisibility(View.GONE);
//                                mBinding.llRemark.setVisibility(View.GONE);
                                mBinding.llTimeChoose.setVisibility(View.GONE);
                                mBinding.llLocation.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(com.netmi.workerbusiness.data.api.MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        UserInfoCache.get().setShop_pay_status(String.valueOf(data.getData().getShop_pay_status()));
                        UserInfoCache.get().setShop_apply_status(String.valueOf(data.getData().getShop_apply_status()));
                        JumpUtil.overlay(getContext(), MainActivity.class);
                    }
                });
    }


    private TimePickerView pickerTimeView;
    private TimePickerView pickerTimeViewTwo;
    private Date date;
    private String time = "";

    private void showTimePicker() {
        if (pickerTimeView == null) {
            //时间选择器
            pickerTimeView = new TimePickerView.Builder(getContext(), (Date date, View v) -> {
                this.date = date;
                time = DateUtil.formatDateTime(date, DateUtil.DF_HH_MM);
                showTimePicker2();
            })
                    .setTitleText("选择开业时间")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{false, false, false, true, true, false})
                    .build();
        }
        pickerTimeView.setDate(DateUtil.getCalendar(date));
        pickerTimeView.show();
    }


    private void showTimePicker2() {
        if (pickerTimeViewTwo == null) {
            //时间选择器
            pickerTimeViewTwo = new TimePickerView.Builder(getContext(), (Date date2, View v2) -> {
                this.date = date2;
                time = time + "~" + DateUtil.formatDateTime(date2, DateUtil.DF_HH_MM);
                mBinding.tvTime.setText(time);
            })
                    .setTitleText("选择关门时间")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{false, false, false, true, true, false})
                    .build();
        }
        pickerTimeViewTwo.setDate(DateUtil.getCalendar(date));
        pickerTimeViewTwo.show();
    }
    //加载失败后， 点击可再次加载
    private boolean loadProvinceError = false;
    //加载省市区
    private void doGetProvince() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .listCity(1)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<CityChoiceEntity>>>(this) {

                    @Override
                    public void onSuccess(BaseData<List<CityChoiceEntity>> data) {
                        //组装数据
                        cityPickerView.loadCityData(data);
                        if (MLoadingDialog.isShow()) {
                            hideProgress();
                            mBinding.llLocationCity.performClick();
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        loadProvinceError = true;
                    }

                    @Override
                    public void onFail(BaseData<List<CityChoiceEntity>> data) {
                        super.onFail(data);
                        loadProvinceError = true;
                    }

                });
    }

}
