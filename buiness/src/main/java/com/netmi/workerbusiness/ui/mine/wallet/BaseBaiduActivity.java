package com.netmi.workerbusiness.ui.mine.wallet;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityBaiduMapBinding;

public class BaseBaiduActivity extends BaseActivity<ActivityBaiduMapBinding> {
    public MapView mMapView;
    public BaiduMap mBaiduMap;
    public LocationClient mLocationClient;
    public static final int BAIDU_READ_PHONE_STATE = 100;
    public MapStatus mMapStatus;
    public MapStatusUpdate mMapStatusUpdate;
    public LatLng latLngView;
    public BitmapDescriptor bitmap;
    public String adcode;
    @Override
    protected int getContentView() {
        return R.layout.activity_baidu_map;
    }

    @Override
    protected void initUI() {
    //判断是否为android6.0系统版本，如果是，需要动态添加权限

        initialize();
    }

    @Override
    protected void initData() {

    }

    private void initialize(){
        //获取map
        mMapView = mBinding.bmapview;
        mBaiduMap = mMapView.getMap();
        //设置是否显示比例尺控件
        mMapView.showScaleControl(false);
        //设置是否显示缩放控件
        mMapView.showZoomControls(false);
        // 删除百度地图LoGo
        mMapView.removeViewAt(1);
        //开启定位层
        mBaiduMap.setMyLocationEnabled(true);

        //定义地图状态
        mMapStatus = new MapStatus.Builder()
                .target(new LatLng(39.915119, 116.403963))//设定中心点坐标 ,要移动到的点
                .zoom(18) //设置级别，放大地图到20倍
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态(北京天安门)
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        //初始化定位监听器
        mLocationClient = new LocationClient(this);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, false, null
                , 0xAAFFFF88, 0xAA00FF00
        );
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
        option.setAddrType("all");

        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_loc_marker);
        //设置locationClientOption
        mLocationClient.setLocOption(option);

    }


}
