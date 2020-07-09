package com.liemi.basemall.ui.personal.digitalasset;


import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ActivityQrcodeScanBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.Logs;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class QRCodeScanActivity extends BaseActivity<ActivityQrcodeScanBinding> implements QRCodeView.Delegate {

    private Disposable disposable;
    private int count = 0;
    /**
     * 扫描出错最大重试次数
     */
    private static final int MAX_RETRY_TIME = 3;

    @Override
    protected int getContentView() {
        return R.layout.activity_qrcode_scan;
    }

    @Override
    protected void onStart() {
        super.onStart();
        disposable = new RxPermissions(this).requestEach(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            mBinding.zxvScan.startCamera();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            finish();
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            finish();
                        }
                    }
                });
        mBinding.zxvScan.startSpotAndShowRect();
    }

    @Override
    protected void initUI() {
        mBinding.zxvScan.setDelegate(this);
    }

    @Override
    protected void onStop() {
        mBinding.zxvScan.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mBinding.zxvScan.onDestroy();
        try {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        } catch (Exception e) {
            Logs.e(e);
        }
        super.onDestroy();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Logs.e("扫描成功：" + result);
        if (!TextUtils.isEmpty(result)) {
            Intent scanIntent = new Intent();
            scanIntent.putExtra("scan_result", result);
            setResult(10002, scanIntent);
            finish();
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Logs.e("扫描出错");
        if ((++count) > MAX_RETRY_TIME) {
            showError("扫描错误，请重试");
            finish();
        } else {
            mBinding.zxvScan.startCamera();
        }
    }
}
