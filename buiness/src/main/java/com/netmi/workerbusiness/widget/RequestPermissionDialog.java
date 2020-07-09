package com.netmi.workerbusiness.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.liemi.basemall.databinding.DialogNormalBinding;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.workerbusiness.R;


public class RequestPermissionDialog extends Dialog implements ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {
    //权限请求的requestCode
    private static final int PERMISSION_REQUEST_CODE = 1000;

    private Activity activity;
    private String[] permissions;
    //权限接收回调
    private RequestPermissionResultListener permissionResultListener;

    public RequestPermissionDialog(Activity activity, String[] permissions, RequestPermissionResultListener permissionResultListener){
        this(activity);
        this.activity = activity;
        this.permissions = permissions;
        this.permissionResultListener = permissionResultListener;
    }

    private RequestPermissionDialog(@NonNull Context context) {
        this(context,com.netmi.baselibrary.R.style.showDialog);
    }

    private RequestPermissionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private RequestPermissionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }

    //设置接口
    public void setPermissionResultListener(RequestPermissionResultListener permissionResultListener){
        this.permissionResultListener = permissionResultListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogNormalBinding normalBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), com.liemi.basemall.R.layout.dialog_normal,null,false);
        setContentView(normalBinding.getRoot());
        normalBinding.setIsShowCancel(true);
        normalBinding.setIsShowMessage(true);
        normalBinding.setIsShowTitle(true);
        normalBinding.setTitle(getContext().getString(R.string.permission_request));
        normalBinding.setMessage(getContext().getString(R.string.permission_request_tip));
        normalBinding.setCancelStr(getContext().getString(R.string.cancel));
        normalBinding.setConfirmStr(getContext().getString(R.string.confirm));
        normalBinding.setClick(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            Logs.i("dialog中收到权限回调");
            for(int i = 0; i < permissions.length; i++){
                int permissionResult = ContextCompat.checkSelfPermission(activity,permissions[i]);
                if(permissionResult == PackageManager.PERMISSION_DENIED){
                    //权限被拒绝
                    if(permissionResultListener != null){
                        permissionResultListener.requestPermissionFinish(false);
                    }
                    dismiss();
                    return;
                }
            }
            //已获得全部权限
            if(permissionResultListener != null){
                permissionResultListener.requestPermissionFinish(true);
            }
            dismiss();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            dismiss();
            if (permissionResultListener != null) {
                //用户选择取消，不请求权限
                permissionResultListener.requestPermissionFinish(false);
            }
        } else if (id == R.id.tv_confirm) {//开始请求权限
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);
        }
    }


    //申请权限结果的回调
    public interface RequestPermissionResultListener{
        void requestPermissionFinish(boolean result);
    }


}
