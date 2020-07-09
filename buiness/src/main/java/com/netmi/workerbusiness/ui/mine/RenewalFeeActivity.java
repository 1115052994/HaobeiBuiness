package com.netmi.workerbusiness.ui.mine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ImageUploadUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.SystemUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.data.entity.mine.ShopPayRecordEntity;
import com.netmi.workerbusiness.databinding.ActivityRenewalFeeBinding;
import com.netmi.workerbusiness.ui.utils.PermissionUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RenewalFeeActivity extends BaseActivity<ActivityRenewalFeeBinding> {
    //服务费证明
    private String firstUrl;
    //押金证明
    private String secondUrl;

    //服务费证明 请求打开相册的requestCode
    private static final int REQUEST_OPEN_ALBUM_FIRST = 1002;
    //押金证明 请求打开相册的requestCode
    private static final int REQUEST_OPEN_ALBUM_SECOND = 1003;
    private int mCurrentStep = 0;//当前执行的操作，0位默认，1为请求相机，2为请求读写权限
    private int currentCode = -1;
    private String[] permissions = new String[]{PermissionUtils.PERMISSION_CAMERA, PermissionUtils.PERMISSION_READ_STORAGE, PermissionUtils.PERMISSION_WRITE_STORAGE};
    private ShopInfoEntity entity;
    private int payStatus = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_renewal_fee;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("缴费");
        if (getIntent().getExtras().getInt(JumpUtil.TYPE) == 1000) {
            entity = (ShopInfoEntity) getIntent().getExtras().getSerializable(JumpUtil.VALUE);
            if (entity != null) {
                mBinding.setFirst(entity.getService_prove());
                mBinding.tvFirst.setVisibility(View.INVISIBLE);
                mBinding.setSecond(entity.getDeposit_prove());
                mBinding.tvSecond.setVisibility(View.INVISIBLE);
                mBinding.tvPay.setClickable(false);
                mBinding.tvPay.setText("审核中");
                mBinding.tvPay.setBackgroundColor(getResources().getColor(R.color.theme_text_gray));
            }
        } else {
            payStatus = getIntent().getExtras().getInt(JumpUtil.CODE);
        }
    }

    @Override
    protected void initData() {
        doGetShopPayRecord();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int code = -1;
        int id = view.getId();
        if (id == R.id.tv_pay) {
            if (!SystemUtil.isFastDoubleClick()) {
                check();
            }
        } else if (id == R.id.rlFirst) {
            code = REQUEST_OPEN_ALBUM_FIRST;
        } else if (id == R.id.rlSecond) {
            code = REQUEST_OPEN_ALBUM_SECOND;
        }

        currentCode = code;
        if (code != -1) {
            //查看是否拥有相机权限
            mCurrentStep = 1;
            if (new PermissionUtils().checkPermission(permissions, RenewalFeeActivity.this)) {
                openAlbum(code);
            } else {
                PermissionUtils.checkAndRequestPermissions(RenewalFeeActivity.this, permissions);
            }
        }

    }

    private void check() {
        if (mBinding.etServiceAccount.getText().toString().isEmpty()) {
            showError("请输入服务费账号");
//        } else if (mBinding.etDepositAccount.getText().toString().isEmpty()) {
//            showError("请输入押金账号");
        } else if (mBinding.tvFirst.getVisibility() == View.VISIBLE) {
            showError("请上传服务费证明");
//        } else if (mBinding.tvSecond.getVisibility() == View.VISIBLE) {
//            showError("请上传押金证明");
        } else {
            if (payStatus == 0) {
                commitShopPay();
            } else {
                updateShopPay();
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
            if (new PermissionUtils().checkPermission(permissions, RenewalFeeActivity.this)) {
                openAlbum(currentCode);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_OPEN_ALBUM_FIRST || requestCode == REQUEST_OPEN_ALBUM_SECOND) && data != null) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            ImageUploadUtils.uploadByOss(images, this, urls -> {
                if (currentCode == REQUEST_OPEN_ALBUM_FIRST) {
                    firstUrl = urls.get(0);

                    mBinding.setFirst(firstUrl);
                    mBinding.tvFirst.setVisibility(View.INVISIBLE);
                } else if (currentCode == REQUEST_OPEN_ALBUM_SECOND) {
                    secondUrl = urls.get(0);

                    mBinding.setSecond(secondUrl);
                    mBinding.tvSecond.setVisibility(View.INVISIBLE);
                }
            }, null);
        }
    }

    private void doGetShopPayRecord() {
        RetrofitApiFactory.createApi(MineApi.class)
                .shopPayRecord("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopPayRecordEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopPayRecordEntity> data) {
                        mBinding.setModel(data.getData());
                        mBinding.setDepositAccount(data.getData().getDeposit_account());
                        mBinding.setServiceAccount(data.getData().getService_account());
                        //    mBinding.tvAllPay.setText("应付金额：¥" + String.valueOf(Double.valueOf(data.getData().getService_money()) + Double.valueOf(data.getData().getDeposit())));
                    }
                });
    }


    private void commitShopPay() {
        RetrofitApiFactory.createApi(MineApi.class)
                .commitShopPay(firstUrl, secondUrl, mBinding.etServiceAccount.getText().toString(), mBinding.etDepositAccount.getText().toString())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("提交成功");
                        finish();
                    }
                });
    }

    private void updateShopPay() {
        RetrofitApiFactory.createApi(MineApi.class)
                .updateShopPay(firstUrl, secondUrl)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("提交成功");
                        finish();
                    }
                });
    }


}
