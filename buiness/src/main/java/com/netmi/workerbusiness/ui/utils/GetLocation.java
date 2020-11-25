package com.netmi.workerbusiness.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class GetLocation {
    private Activity activity;
    private LocationClient mLocationClient;
    private static GetLocation mSingleton = null;
    public static GetLocation getInstance() {
        if (mSingleton == null) {
            synchronized (GetLocation.class) {
                if (mSingleton == null) {
                    mSingleton = new GetLocation();
                }
            }
        }
        return mSingleton;
    }
    public LocationClient getAddares(Activity context){
        this.activity = context;
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT >= 23) {
            return showContacts();
        } else {
            return init();
        }
    }

    public LocationClient showContacts() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity,"没有权限,请手动开启定位权限",Toast.LENGTH_LONG).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 100);
        } else {
           return init();
        }
        return null;
    }
    public LocationClient init(){
        // 定位初始化
        mLocationClient = new LocationClient(activity);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);
        //这个要写
        option.setAddrType("all");
        option.setIsNeedAddress(true);
//        option.setIsNeedLocationDescribe(true);
        mLocationClient.setLocOption(option);
        return mLocationClient;

    }



}
