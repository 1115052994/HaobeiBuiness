package com.netmi.workerbusiness.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


/*
 * 权限操作类
 * */
public class PermissionUtils {
    //申请权限的dialog
//    private RequestPermissionDialog requestPermissionDialog;
    public static int REQUEST_CODE = 79;

    /*
     * 权限集合
     * */
    //相机权限
    public static String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    //文件读写权限
    public static String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    //查看是否拥有权限
    public boolean checkPermission(String[] permissions, Context context) {
        if (permissions == null || permissions.length < 1) {
            return true;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (int i = 0; i < permissions.length; i++) {
            int permissionResult = ContextCompat.checkSelfPermission(context, permissions[i]);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }

        return true;
    }

    public static String[] needtPermissions = {"android.permission.READ_PHONE_STATE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_WIFI_STATE"

    };
    /**
     * 检查多权限
     */
    public static String[] checkPermission(Context context, @NonNull String... permissions) {
        List<String> noPermission = new ArrayList<>();
        for (String permission : permissions) {
            // 检查该权限是否已经获取
            if (!checkPermission(context, permission)) {
                noPermission.add(permission);
            }
        }
        String[] result = new String[noPermission.size()];
        return noPermission.toArray(result);
    }

    /**
     * 检查单个权限
     */
    public static boolean checkPermission(Context context, @NonNull String permission) {
        List<String> noPermission = new ArrayList<>();
        // 检查该权限是否已经获取
        int i = ContextCompat.checkSelfPermission(context, permission);
        // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
        if (i == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * 动态申请权限
     */
    public static void requestPermission(Activity context, String... permissions) {
        if (permissions.length > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(permissions, REQUEST_CODE);
            }
        }
    }
    /**
     * 动态申请权限
     */
    public static void checkAndRequestPermissions(Activity context, String... permissions){
        requestPermission(context,checkPermission(context,permissions));
    }
//
//    //查看是否拥有权限，如果没有权限则取申请权限
//    public boolean checkPermission(String[] permissions, Activity activity, RequestPermissionDialog.RequestPermissionResultListener permissionResultListener) {
//        if (!checkPermission(permissions, activity)) {
//            if (requestPermissionDialog == null) {
//                requestPermissionDialog = new RequestPermissionDialog(activity, permissions, permissionResultListener);
//            }
//            if (!requestPermissionDialog.isShowing()) {
//                requestPermissionDialog.show();
//            }
//            return false;
//        }
//        return true;
//    }
}
