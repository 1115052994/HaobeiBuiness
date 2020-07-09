package com.netmi.workerbusiness.ui.mine.wallet;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.amap.api.services.core.PoiItem;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.CityChoiceEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.event.LocationEvent;
import com.netmi.workerbusiness.databinding.ActivityBaiduMapBinding;
import com.tencent.mm.opensdk.utils.Log;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class BaiduMapActivity extends BaseActivity<ActivityBaiduMapBinding> implements TextWatcher {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private SuggestionSearch mSuggestionSearch;
    private List<SuggestionResult.SuggestionInfo> entity;

    private double latitude = 0;
    private double longitude = 0;
    private String p_name;
    private String c_name;
    private String d_name;

    private String p_id;
    private String c_id;
    private String d_id;
    private String address;

    private List<CityChoiceEntity> entities = new ArrayList();
    private static final int BAIDU_READ_PHONE_STATE = 100;


    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_baidu_map;
    }


    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {
            init();
        }
    }

    private void init() {

        //获取map
        mMapView = mBinding.bmapview;
        mBaiduMap = mMapView.getMap();
        //开启定位层
        mBaiduMap.setMyLocationEnabled(true);

        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(new LatLng(39.915119, 116.403963))//设定中心点坐标 ,要移动到的点
                .zoom(17) //设置级别，放大地图到20倍
                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        //初始化定位监听器
        mLocationClient = new LocationClient(this);
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
        option.setAddrType("all");
        //设置locationClientOption
        mLocationClient.setLocOption(option);
        //注册LocationListener监听器
        BDLocationListener myLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //获取当前位置并定位
                c_name = bdLocation.getCity();
                d_name = bdLocation.getDistrict();
                latitude = bdLocation.getLatitude();
                longitude = bdLocation.getLongitude();
                address = bdLocation.getAddrStr();
                mBinding.etAddress.setText(address);
                mBinding.etAddress.dismissDropDown();

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(0)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(0).latitude(latitude)
                        .longitude(longitude).build();
                mBaiduMap.setMyLocationData(locData);
            }
        };
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();

        //定义定位状态，参数（定位模式，是否开启方向，自定义图片代替原图片，颜色，颜色）
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, false, null
                , 0xAAFFFF88, 0xAA00FF00
        );
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);

        //1、实例化mSuggestionSearch ，并添加监听器。用于处理搜索到的结果。
        mSuggestionSearch = SuggestionSearch.newInstance();
        //编写监听器
        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                //处理sug检索结果
                if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                    Log.i("result: ", "没有找到");
                    return;
                    //未找到相关结果
                } else {
                    //获取在线建议检索结果，并显示到listview中
                    List<SuggestionResult.SuggestionInfo> resl = suggestionResult.getAllSuggestions();

                    entity = resl;
                    List<String> name = new ArrayList();
                    for (int i = 0; i < resl.size(); i++) {
                        if (resl.get(i) != null) {
                            name.add(resl.get(i).getKey());
                        }
                    }
                    ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                            getApplicationContext(),
                            R.layout.item_layout, name);
                    mBinding.etAddress.setAdapter(aAdapter);
                    aAdapter.notifyDataSetChanged();
                    mBinding.etAddress.showDropDown();
                }
            }
        };
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);

        mBinding.etAddress.addTextChangedListener(this);// 添加文本输入框监听事件
        mBinding.etAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                SuggestionResult.SuggestionInfo suggestionInfo = entity.get(position);
                System.out.println("chufadi");
                mBinding.etAddress.setText(suggestionInfo.key);

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(0)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(0).latitude(suggestionInfo.pt.latitude)
                        .longitude(suggestionInfo.pt.longitude).build();
                mBaiduMap.setMyLocationData(locData);
                longitude = suggestionInfo.pt.longitude;
                latitude = suggestionInfo.pt.latitude;
                address = suggestionInfo.getKey();
                c_name = suggestionInfo.getCity();
                d_name = suggestionInfo.getDistrict();
                mBinding.etAddress.dismissDropDown();
            }
        });

    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_confirm) {
            if (latitude == 0 && longitude == 0) {
                showError("请输入地址");
            } else {
                doGetProvince();//获取全部省市区信息，获取id
            }
        }
    }

    private void getPCDID() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = 0; j < entities.get(i).getC_list().size(); j++) {
                for (int k = 0; k < entities.get(i).getC_list().get(j).getD_list().size(); k++) {
                    if (c_name.equals(entities.get(i).getC_list().get(j).getName()) &&
                            d_name.equals(entities.get(i).getC_list().get(j).getD_list().get(k).getName())) {
                        d_id = entities.get(i).getC_list().get(j).getD_list().get(k).getId();
                        c_id = entities.get(i).getC_list().get(j).getId();
                        p_id = entities.get(i).getId();
                        Log.e("weng", p_id);
                        Log.e("weng", c_id);
                        Log.e("weng", d_id);
                        EventBus.getDefault().post(new LocationEvent(String.valueOf(longitude), String.valueOf(latitude), address, p_name, c_name, d_name, p_id, c_id, d_id));
                        finish();
                    }
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //设置请求参数：keyword搜索的关键字，城市可以固定，城市限制设定为否。
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword("" + charSequence)
                .city(c_name)
                .citylimit(false)
        );

    }

    @Override
    public void afterTextChanged(Editable editable) {

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
                        if (data.getData() != null) {
                            entities = data.getData();
                            getPCDID();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            showError("没有权限,请手动开启定位权限");
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(BaiduMapActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        } else {
            init();
        }
    }


    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    init();
                } else {
                    // 没有获取到权限，做特殊处理
                    showError("获取位置权限失败，请手动开启");
                }
                break;
            default:
                break;
        }
    }

}
