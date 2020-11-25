package com.netmi.workerbusiness.ui.mine.wallet;

import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.event.LocationEvent;

import org.greenrobot.eventbus.EventBus;

public class BaiduMapActivity extends BaseBaiduActivity  {
    @Override
    protected void initUI() {
        super.initUI();
        getTvTitle().setText("店铺地址");
        initView();
    }

    private void initView() {
        //注册LocationListener监听器
        BDLocationListener myLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //获取当前位置并定位
                adcode = bdLocation.getAdCode();
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(0)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(0)
                        .latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude())
                        .build();
                mBaiduMap.setMyLocationData(locData);
                mBinding.etAddress.setText(bdLocation.getAddrStr());
                latLngView = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            }
        };
        mLocationClient.registerLocationListener(myLocationListener);
        mLocationClient.start();
        //手指点击监听
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapPoiClick(MapPoi arg0) {
                // TODO Auto-generated method stub
            }
            //此方法就是点击地图监听
            @Override
            public void onMapClick(LatLng latLng) {
                //获取经纬度
                mBaiduMap.clear();
                latLngView = latLng;
                MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmap);
                mBaiduMap.addOverlay(options);
                GeoCoder geocode = GeoCoder.newInstance();
                //设置查询结果监听者
                geocode.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                            showError("找不到此位置");
                            return;
                        }
                        if (result != null && result.error == SearchResult.ERRORNO.NO_ERROR) {
                            //得到位置
                            mBinding.etAddress.setText(result.getAddress());
                            showError(result.getAddress());
                        }
                        adcode = String.valueOf(result.getAdcode());

                    }
                    /**
                     * 地理编码查询结果回调函数
                     */
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult result) {

                    }
                });
                // 发起反地理编码请求
                geocode.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
            }
        });
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_confirm) {
            String address = mBinding.etAddress.getText().toString();
            if (address == null || address == null) {
                showError("检查GPS和定位权限是否开起");
            } else {
                //获取全部省市区信息，获取id
                if (adcode == null || adcode.equals("")) {
                    showError("信息获取失败");
                    return;
                }
                EventBus.getDefault().post(new LocationEvent(String.valueOf(latLngView.longitude), String.valueOf(latLngView.latitude), address, "", "", "", adcode.substring(0, 2) + "0000", adcode.substring(0, 4) + "00", adcode));
                finish();
            }
        } else if (view.getId() == R.id.tv_location) {
            if (latLngView != null) {
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLngView));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy(); // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理   mMapView.onDestroy(); } @Override protected void onResume() { super.onResume(); // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理   mMapView.onResume(); } @Override protected void onPause() { super.onPause(); // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理   mMapView.onPause(); }
        mMapView.onDestroy();
    }
}