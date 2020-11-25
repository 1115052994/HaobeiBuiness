package com.netmi.workerbusiness.ui.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
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
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.ImageUploadUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.SystemUtil;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.netmi.baselibrary.widget.CityPickerView;
import com.netmi.baselibrary.widget.FruitAdapter;
import com.netmi.baselibrary.widget.MLoadingDialog;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.LoginApi;
import com.netmi.workerbusiness.data.entity.mine.GetApplyInfo;
import com.netmi.workerbusiness.data.event.LocationEvent;
import com.netmi.workerbusiness.data.event.StoreRemarkEvent;
import com.netmi.workerbusiness.databinding.ActivityPersonalOfflineInfoRestrictionsBinding;
import com.netmi.workerbusiness.ui.mine.StoreRemarkActivity;
import com.netmi.workerbusiness.ui.mine.wallet.BaiduMapActivity;
import com.netmi.workerbusiness.ui.utils.GetLocation;
import com.netmi.workerbusiness.ui.utils.PermissionUtils;
import com.netmi.workerbusiness.widget.ContractVerifyDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.netmi.workerbusiness.ui.login.CategoryVerifyActivity.CATEGORY_ID;
import static com.netmi.workerbusiness.ui.login.CategoryVerifyActivity.CATEGORY_NAME;
import static com.netmi.workerbusiness.ui.mine.StoreInfoActivity.REQUEST_CHANGE_REMARK;
import static com.netmi.workerbusiness.ui.mine.wallet.BaseBaiduActivity.BAIDU_READ_PHONE_STATE;

//线下限制商家入驻
public class PersonalOfflineInfoRestrictionsActivity extends BaseActivity<ActivityPersonalOfflineInfoRestrictionsBinding> {
    //身份证正面 请求打开相册的requestCode
    private static final int REQUEST_OPEN_ALBUM_POSITIVE = 1002;
    //身份证反面
    private static final int REQUEST_OPEN_ALBUM_NEGATIVE = 1003;
    //手持身份证
    private static final int REQUEST_OPEN_ALBUM_HAND = 1011;
    //门店图片
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
    private String service;
    private String et_id_company_name;
    private String hold_url;
    //经营分类id
    private String category_id;


    //身份证正面图片
    private String positiveUrl;
    //身份证反面图片
    private String negativeUrl;
    //手持身份证图片
    private String handtiveUrl;
    //营业执照
    private String buiness_license;
    //店铺Logo
    private String logo_pic;
    private String time;
    private String label;

    //线上线下店铺选择页面传进来的type
    private String user_type_event = "0"; //1线上  2线下

    private int currentCode = -1;
    private int mCurrentStep = 0;//当前执行的操作，0位默认，1为请求相机，2为请求读写权限
    private String[] permissions = new String[]{PermissionUtils.PERMISSION_CAMERA, PermissionUtils.PERMISSION_READ_STORAGE, PermissionUtils.PERMISSION_WRITE_STORAGE};
    private MyLocationListener myListener;
    private GetLocation instance;
    private String edit;
    private CityPickerView cityPickerView;


    @Override
    protected int getContentView() {
        return R.layout.activity_personal_offline_info_restrictions;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("实体商家入驻信息");
        if (getIntent() != null && getIntent().getExtras() != null) {
            user_type_event = getIntent().getExtras().getString(JumpUtil.VALUE);
        }
        label = getIntent().getStringExtra("label");

        edit = getIntent().getStringExtra("edit");
//        if (edit.equals("1")) {
            doGetUserInfo();
//        }

        myListener = new MyLocationListener();

    }

    @Override
    protected void initData() {
        doInitOssConfigure();

        //请求省市级
        doGetProvince();
    }

    // 接收广播
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void remark(StoreRemarkEvent event) {
//        mBinding.tvRemark.setText(event.getRemark());
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
        mBinding.addresText.setText(event.getAddress());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContractVerifyDialog.dismiss();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void doClick(View view) {
        Bundle args = new Bundle();
        super.doClick(view);
        int code = -1;
        int id = view.getId();
        if (id == R.id.ll_back) {
            finish();
        } else if (id == R.id.rlPositive) {
            code = REQUEST_OPEN_ALBUM_POSITIVE;
        } else if (id == R.id.rlNegative) {
            code = REQUEST_OPEN_ALBUM_NEGATIVE;
        } else if (id == R.id.rlHand) {
            code = REQUEST_OPEN_ALBUM_HAND;
        } else if (id == R.id.rl_shop_pic) {
            code = REQUEST_OPEN_ALBUM_SHOP_PIC;
        } else if (id == R.id.rl_environment_pic) {
            code = REQUEST_OPEN_ALBUM_ENVIRONMENT_PIC;
        } else if (id == R.id.rl_business_pic) {
            code = REQUEST_OPEN_ALBUM_BUSINESS_LICENSE;
        } else if (id == R.id.rl_logo_pic) {
            code = REQUEST_OPEN_ALBUM_LOGO_PIC;
        } else if (id == R.id.tv_confirm) {
            if (!SystemUtil.isFastDoubleClick()) {
                check();
            }
        }  else if (id == R.id.ll_remark) {
            JumpUtil.startForResult(this, StoreRemarkActivity.class, REQUEST_CHANGE_REMARK, args);
        } else if (id == R.id.ll_time_choose) {
            showTimePicker();
        } else if (id == R.id.ll_location||id == R.id.addres_text) {
            if (Build.VERSION.SDK_INT >= 23) {
                showContacts();
            }else JumpUtil.overlay(getContext(), BaiduMapActivity.class);
        } else if (id == R.id.ll_location_city) {
            startSelect(view);
        } else if (id == R.id.tv_service) {
            List<String> strings = new ArrayList<>();
            strings.add("5%");
            strings.add("10%");
            strings.add("12%");
            strings.add("15%");
            strings.add("20%");
            strings.add("30%");
            showPopupWindow(mBinding.tvService, strings);
        } else if (id == R.id.ll_category) {
            args.putString(JumpUtil.TYPE, user_type_event);
            JumpUtil.startForResult(this, CategoryVerifyActivity.class, REQUEST_CATEGORY, args);
        }
        currentCode = code;
        if (code != -1) {
            //查看是否拥有相机权限
            mCurrentStep = 1;
            if (new PermissionUtils().checkPermission(permissions, PersonalOfflineInfoRestrictionsActivity.this)) {
                openAlbum(code);
            } else {
                PermissionUtils.checkAndRequestPermissions(PersonalOfflineInfoRestrictionsActivity.this, permissions);
            }
        }
    }

    //地区选择
    public void startSelect(View view) {
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
                mBinding.tvAddress.setText(builder.toString());
            });
        } else {
            //城市加载中
            showProgress("");
            if (loadProvinceError)
                doGetProvince();
        }
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
                            mBinding.llLocation.performClick();
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

    private List<String> shop_url = new ArrayList<>();
    private List<String> img_url = new ArrayList<>();
    private String license_url = "";

    private void check() {

        shopName = mBinding.etShopName.getText().toString();
        real_name = mBinding.etName.getText().toString();
        idCard = mBinding.etIdCard.getText().toString();
        remark = mBinding.etMerchantContent.getText().toString();
        businessNumber = mBinding.etBusinessNumber.getText().toString();
        service = mBinding.tvService.getText().toString();
        et_id_company_name = mBinding.etIdCompanyName.getText().toString();
        label = mBinding.etShopBiaoqian.getText().toString();
//        remark = mBinding.etMerchantContent().getText().toString();

        if (TextUtils.isEmpty(shopName)) {
            showError("请输入店铺名称");
        }
//        else if(TextUtils.isEmpty(remark)){
//            showError("请输入商家简介");
//        }
        else if (TextUtils.isEmpty(service)) {
            showError("请选择服务费比例");
        } else if (TextUtils.isEmpty(idCard)) {
            showError("请输入法人身份证号");
        } else if (TextUtils.isEmpty(real_name)) {
            showError("请输入法人姓名");
        } else if (TextUtils.isEmpty(et_id_company_name)) {
            showError("请输入公司名称");
        } else if (TextUtils.isEmpty(idCard)) {
            showError("请输入法人身份证号");
        } else if (TextUtils.isEmpty(businessNumber)) {
            showError("请输入营业执照编号");
        } else if (TextUtils.isEmpty(mBinding.getIdTop())) {
            showError("请上传身份证正面照");
        } else if (TextUtils.isEmpty(mBinding.getIdBottom())) {
            showError("请上传身份证背面照");
        } else if (TextUtils.isEmpty(mBinding.getHandPic())) {
            showError("请上传手持身份证");
        } else if (TextUtils.isEmpty(mBinding.getBusinessPic())) {
            showError("请上传营业执照");
        } else if (TextUtils.isEmpty(mBinding.getShopLogo())) {
            showError("请上传店铺logo");
        } else if (TextUtils.isEmpty(category_id)) {
            showError("请选择经营类目");
        } else {
            if (UserInfoCache.get().getShop_user_type().equals("2")) {
                if (TextUtils.isEmpty(mBinding.getShopPic())) {
                    showError("请上传门店图片");
                    return;
                } else if (TextUtils.isEmpty(mBinding.getEnvironmentPic())) {
                    showError("请上传店内环境图片");
                    return;
                } else if (time.equals("") || time.equals("请选择营业时间")) {
                    showError("请选择营业时间");
                    return;
                } else if (mBinding.addresText.getText().toString().equals("省/市/区详细地址")) {
                    showError("请选择地址");
                    return;
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("");
            builder.setMessage("请确认是否提交");
            builder.setPositiveButton("确定", (dialog, which) -> {
                shop_url.add(mBinding.getShopPic());
                img_url.add(mBinding.getEnvironmentPic());
                license_url = mBinding.getBusinessPic();
                doVerified("2", shopName, logo_pic, remark, longitude, latitude, time, shop_url, img_url, positiveUrl, negativeUrl, license_url, real_name, idCard, p_id, c_id, d_id, label, address, category_id, businessNumber, et_id_company_name,handtiveUrl, service.substring(0, service.length() - 1));
                dialog.dismiss();
            });
            builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    }

    private String code;

    private void doVerified(String type, String shopName, String logo_url, String content, String longitude, String latitude, String time
            , List<String> shop_url, List<String> img_url, String front_url, String back_url,
                            String license_url, String real_name, String idCard, String p_id, String c_id, String d_id, String label, String address, String category_id, String license_num, String et_id_company_name,String hold_url, String service_service_charge) {

        if (type.equals("2")) {
            RetrofitApiFactory.createApi(LoginApi.class)
                    .createNew(shopName, logo_url, content, longitude, latitude, time, shop_url, img_url.get(0), front_url, back_url, license_url, real_name, idCard, p_id, c_id, d_id, label, address, category_id, license_num, et_id_company_name, service_service_charge, hold_url, license_url, code)
                    .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                    .compose(RxSchedulers.compose())
                    .subscribe(new XObserver<BaseData>() {
                        public void onSuccess(BaseData baseData) {
                            JumpUtil.overlay(getContext(), BindingThreeActivity.class, "name", real_name, "idcard", idCard,"phone");
//                            doGetShopInfo();
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


    }

    private void doGetUserInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getUserInfo(0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        Log.e("weng2", user_type_event);
                        if (user_type_event.equals("1")) {
                            mBinding.llEnvironmentPic.setVisibility(View.GONE);
                            mBinding.llTimeChoose.setVisibility(View.GONE);
                            mBinding.llLocation.setVisibility(View.GONE);
                        } else if (user_type_event.equals("0")) {
                            if (data.getData().getShop_user_type().equals("1")) {//	用户选择商户类型 0:未选择类型 1:线上 2:线下
                                mBinding.llEnvironmentPic.setVisibility(View.GONE);
                                mBinding.llTimeChoose.setVisibility(View.GONE);
                                mBinding.llLocation.setVisibility(View.GONE);
                            }
                        }
                    }
                });
        //获取商家信息
        RetrofitApiFactory.createApi(com.netmi.workerbusiness.data.api.MineApi.class)
                .getApplyInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<GetApplyInfo>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(BaseData<GetApplyInfo> data) {
                        GetApplyInfo data1 = data.getData();
                        logo_pic = data1.getLogo_url();//店铺头像
                        shopName = data1.getName();//店铺名称
                        remark = data1.getContent();//商家简介
                        category_id = data1.getCategory_id();//经营类目id
                        service = data1.getService_charge();//服务费比例
                        longitude = data1.getLongitude();//经度
                        latitude = data1.getLatitude();//纬度
                        address = data1.getAddress();//商铺位置
                        time = data1.getDo_time();//营业时间
                        real_name = data1.getReal_name();//法人姓名
                        idCard = data1.getIdcard();//身份证号
                        et_id_company_name = data1.getCompany_name();//公司名称
                        businessNumber = data1.getLicense_num();//营业执照编号
                        positiveUrl = data1.getFront_card_url();//身份证正面
                        negativeUrl = data1.getBack_card_url();//身份证反面
                        handtiveUrl = data1.getHold_card_url();//手持身份证
                        buiness_license = data1.getLicense_url();//营业执照
                        shop_url.add(data1.getShop_url());//门店照片1
                        img_url.add(data1.getImg_url());//门店环境照片
                        p_id = data1.getP_id();
                        c_id = data1.getC_id();
                        d_id = data1.getD_id();
                        mBinding.etMerchantContent.setText(remark);//商家简介
                        mBinding.tvService.setText(service+"%");
                        if (logo_pic != null || !TextUtils.isEmpty(logo_pic)) {
                            mBinding.ivLogo.setVisibility(View.INVISIBLE);
                        }
                        mBinding.setShopLogo(logo_pic);//店铺图片
                        mBinding.etShopName.setText(shopName);//店铺名称
                        mBinding.addresText.setText(address);//商铺位置
                        mBinding.tvCategory.setText(data1.getCategory_name());//经营类目名称
                        if (TextUtils.isEmpty(time) || time.equals("")) {
                            mBinding.tvTime.setHint("请选择营业时间");
                            mBinding.tvTime.setTextColor(Color.parseColor("#c4c4c4"));
                        } else {
                            mBinding.tvTime.setText(time);//营业时间
                            mBinding.tvTime.setTextColor(Color.parseColor("#333333"));
                        }
                        mBinding.etName.setText(real_name);//法人姓名
                        mBinding.etIdCard.setText(idCard);//身份证号
                        mBinding.etIdCompanyName.setText(et_id_company_name);//公司名称
                        mBinding.etBusinessNumber.setText(businessNumber);//营业执照编号
                        mBinding.setIdTop(positiveUrl);//身份证正面
                        mBinding.setIdBottom(negativeUrl);//身份证反面
                        mBinding.setHandPic(handtiveUrl);//手持身份证
                        mBinding.setBusinessPic(buiness_license);//营业执照
                        mBinding.setShopPic(shop_url.get(0));//门店照片1
                        mBinding.setEnvironmentPic(img_url.get(0));//门店环境照片
                    }
                });
    }
    private PopupWindow mPopupWindow;
    private ListView listView;
    private FruitAdapter adapter_list;

    @SuppressLint("NewApi")
    public void showPopupWindow(View binding, List<String> item) {
        // app_bar 是item中的一个组件 用来确定 popupwindow的位置
        if (mPopupWindow == null) {          // 判断是否 为空 每次点击item展示的popupWindow都是同一个对象 只是位置不同数据不同
            // 引入popupwindow 里面的自定义布局， 并且设置点击事件
            View popupWindowView = LayoutInflater.from(this).inflate(R.layout.locality_more_share_text, null);
            listView = popupWindowView.findViewById(R.id.xrv_data);
            adapter_list = new FruitAdapter(this, R.layout.simple_list_item, item);
            listView.setAdapter(adapter_list);
            // 构造对象 传入自定义布局对象，宽和高
            mPopupWindow = new PopupWindow(popupWindowView, binding.getWidth(), WindowManager.LayoutParams.WRAP_CONTENT);
            // 点击popupwindow之外  popupwindow消失
            mPopupWindow.setOutsideTouchable(true);
            // 设置显示动画
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            //设置可以点击
            mPopupWindow.setTouchable(true);
            // 设置 背景图，如果在自定义布局中设置了背景这里就不要再设置了（重复绘制）
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            mPopupWindow.update();
            mPopupWindow.setFocusable(true);
        } else {
            adapter_list.setData(item);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mBinding.tvService.setText(item.get(i));
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAsDropDown(binding, 0, 0, Gravity.RIGHT);
    }

    private double latitude_new = 0;

    //实现BDLocationListener接口,BDLocationListener为结果监听接口，异步获取定位结果
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            String addrStr = location.getAddrStr();
            latitude_new = location.getLatitude();
            mBinding.addresText.setText(addrStr);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQUEST_CODE) {
            if (new PermissionUtils().checkPermission(permissions, PersonalOfflineInfoRestrictionsActivity.this)) {
                openAlbum(currentCode);
            }
        } else if (requestCode == BAIDU_READ_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                JumpUtil.overlay(getContext(), BaiduMapActivity.class);
            } else {
                // 没有获取到权限，做特殊处理
                showError("获取位置权限失败，请手动开启");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

            ImageUploadUtils.uploadByOssZip(images, this, urls -> {
                if (currentCode == REQUEST_OPEN_ALBUM_SHOP_PIC) {
                    shop_url.clear();
                    shop_url.add(urls.get(0));

                    mBinding.setShopPic(shop_url.get(0));
                    mBinding.ivShop.setVisibility(View.INVISIBLE);
                } else if (currentCode == REQUEST_OPEN_ALBUM_ENVIRONMENT_PIC) {
                    img_url.clear();
                    img_url.add(urls.get(0));

                    mBinding.setEnvironmentPic(img_url.get(0));
                    mBinding.ivEnvironment.setVisibility(View.INVISIBLE);
                } else if (currentCode == REQUEST_OPEN_ALBUM_POSITIVE) {
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
                    mBinding.ivHand.setVisibility(View.INVISIBLE);
                } else if (currentCode == REQUEST_OPEN_ALBUM_BUSINESS_LICENSE) {
                    buiness_license = urls.get(0);

                    mBinding.setBusinessPic(buiness_license);
                    mBinding.ivBusiness.setVisibility(View.INVISIBLE);
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

    private void openAlbum(int code) {
        mCurrentStep = 0;
        ImagePicker.getInstance().setSelectLimit(1);
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, code);
    }


    //获取oss配置
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

    private TimePickerView pickerTimeView;
    private TimePickerView pickerTimeViewTwo;
    private Date date;

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


    //获取定位权限
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            showError("没有权限,请手动开启定位权限");
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }else JumpUtil.overlay(getContext(), BaiduMapActivity.class);
    }

}
