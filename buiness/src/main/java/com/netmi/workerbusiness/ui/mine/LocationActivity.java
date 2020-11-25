package com.netmi.workerbusiness.ui.mine;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.databinding.ActivityLocationBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends BaseActivity<ActivityLocationBinding> implements TextWatcher, PoiSearch.OnPoiSearchListener {
    private MapView mapView;
    private AMap aMap;
    private int shopId;
    private String logo_url;
    private String shop_url;
    private String name;
    private String remark;
    private String time;
    private double latitude = 0;
    private double longitude = 0;
    private String p_name;
    private String c_name;
    private String d_name;
    private String address;
    private Marker locationMarker;

    private List<PoiItem> entity;
    private boolean isfirstinput = true;

    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private LatLonPoint latLonPoint;
    private LatLng latLng;

    /**
     * POI搜索
     */
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索

    @Override
    protected int getContentView() {
        return R.layout.activity_location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setMapLanguage(AMap.CHINESE);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
        aMap.setMyLocationEnabled(true);  //开启定位功能
//        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
        aMap.getUiSettings().setZoomControlsEnabled(true);  //设置地图默认的缩放按钮是否显示
        aMap.getUiSettings().setCompassEnabled(false);  //设置地图默认的指南针是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(false);  //设置地图默认的定位按钮是否显示
        aMap.getUiSettings().setScrollGesturesEnabled(true);  //设置地图是否可以手势滑动
        aMap.getUiSettings().setZoomGesturesEnabled(true);  //设置地图是否可以手势缩放大小
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_loc_mine_marker));//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        mBinding.etAddress.addTextChangedListener(this);// 添加文本输入框监听事件

        mBinding.etAddress.setOnItemClickListener((adapterView, view, position, id) -> {
            if (entity != null && entity.size() > position) {
                PoiItem poiItem = entity.get(position);
                latitude = poiItem.getLatLonPoint().getLatitude();
                longitude = poiItem.getLatLonPoint().getLongitude();
                p_name = poiItem.getProvinceName();
                c_name = poiItem.getCityName();
                d_name = poiItem.getAdName();
                address = poiItem.getSnippet() + "" + (poiItem + "");

                mBinding.etAddress.dismissDropDown();
            }
        });

        getCurrentLocationLatLng();
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("请选择位置");

        shopId = getIntent().getExtras().getInt(JumpUtil.ID);
        logo_url = getIntent().getExtras().getString(JumpUtil.TYPE);
        name = getIntent().getExtras().getString(JumpUtil.VALUE);
        remark = getIntent().getExtras().getString(JumpUtil.CODE);
        time = getIntent().getExtras().getString(JumpUtil.FLAG);
        shop_url = getIntent().getExtras().getString(JumpUtil.UID);
    }

    @Override
    protected void initData() {
    }


    private void getCurrentLocationLatLng() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

 /* //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景） 设置了场景就不用配置定位模式等
    option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
    if(null != locationClient){
        locationClient.setLocationOption(option);
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        locationClient.stopLocation();
        locationClient.startLocation();
    }*/
        // 同时使用网络定位和GPS定位,优先返回最高精度的定位结果,以及对应的地址描述信息
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //只会使用网络定位
        /* mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);*/
        //只使用GPS进行定位
        /*mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);*/
        // 设置为单次定位 默认为false
        mLocationOption.setOnceLocation(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。默认连续定位 切最低时间间隔为1000ms
        mLocationOption.setInterval(3500);
        //设置是否返回地址信息（默认返回地址信息）
        /*mLocationOption.setNeedAddress(true);*/
        //关闭缓存机制 默认开启 ，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存,不区分单次定位还是连续定位。GPS定位结果不会被缓存。
        /*mLocationOption.setLocationCacheEnable(false);*/
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    /**
     * 定位回调监听器
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    double currentLat = amapLocation.getLatitude();//获取纬度
                    double currentLon = amapLocation.getLongitude();//获取经度
                    latLonPoint = new LatLonPoint(currentLat, currentLon);  // latlng形式的
                    /*currentLatLng = new LatLng(currentLat, currentLon);*/   //latlng形式的
                    Log.i("currentLocation", "currentLat : " + currentLat + " currentLon : " + currentLon);
                    amapLocation.getAccuracy();//获取精度信息

                    latLng = new LatLng(currentLat, currentLon);

                    mBinding.etAddress.setText(amapLocation.getPoiName());
                    mBinding.etAddress.setSelection(mBinding.etAddress.length());
                    latitude = currentLat;
                    longitude = currentLon;
                    p_name = amapLocation.getProvince();
                    c_name = amapLocation.getCity();
                    d_name = amapLocation.getDistrict();
                    address = amapLocation.getPoiName();

                    setPosition();
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };


    private void setPosition() {
//        /**
//         * 设置初始化地图位置
//         * CameraPosition() 方法
//         *  var1 target 目标  AmapRealTimeECanal.WEISHANXIAN
//         *  var2 zoom  变焦
//         *  var3 tilt 倾斜
//         *  var4 bearing 方位
//         *
        changeCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 35, 30, 0)));
        Log.e("weng", latLng + "");

        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_loc_marker)));
        locationMarker.showInfoWindow();
        //设置Marker在屏幕上,不跟随地图移动
//        locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
        locationMarker.setPosition(latLng);
        locationMarker.setZIndex(1);
//        aMap.clear();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!newText.equals("")) {
            //开始POI搜索
            showProgressDialog();// 显示进度框
            currentPage = 0;
            query = new PoiSearch.Query(newText, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            query.setPageSize(10);// 设置每页最多返回多少条poiitem
            query.setPageNum(currentPage);// 设置查第一页
            query.setCityLimit(false);

            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();
        } else {
            showError("请输入地址");
        }


    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }


    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_confirm) {
            if (latitude == 0 && longitude == 0) {
                showError("请输入地址");
            } else {
//                doUpdateShopInfo(shopId, logo_url, name, remark, time, String.valueOf(longitude), String.valueOf(latitude), p_name, c_name, d_name, address, shop_url);
//                EventBus.getDefault().post(new LocationEvent(String.valueOf(longitude), String.valueOf(latitude), address, p_name, c_name, d_name));
                finish();
            }
        }
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {
        aMap.moveCamera(update);
    }


    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        //开始搜索后停止定位功能
        mLocationClient.stopLocation();
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页

                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();

                        entity = poiItems;
                        List<String> name = new ArrayList();
                        for (int i = 0; i < poiItems.size(); i++) {
                            if (poiItems.get(i).getLatLonPoint() != null) {
                                name.add(poiItems.get(i).getTitle());
                            }
                        }
                        ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.item_layout, name);
                        mBinding.etAddress.setAdapter(aAdapter);
                        aAdapter.notifyDataSetChanged();

                        mBinding.etAddress.showDropDown();

                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                    } else {
                    }
                }
            } else {
                showError("对不起，没有搜索到相关数据");
            }
        } else {
            showError(String.valueOf(rCode));
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 更新用户信息
     */
    private void doUpdateShopInfo(Integer id, String logo_url, String name, String remark, String opening_hours, String longitude, String latitude
            , String p_name, String c_name, String d_name, String address, String img_url, String p_id, String c_id, String d_id) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUpdateShopInfo(id, logo_url, name, remark, opening_hours, longitude, latitude, p_name, c_name, d_name, address, img_url, p_id, c_id, d_id)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("修改成功");

                    }

                });
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (mLocationClient != null) {
            mLocationClient.startLocation(); // 启动定位
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();//销毁定位客户端。
        }
    }
}

