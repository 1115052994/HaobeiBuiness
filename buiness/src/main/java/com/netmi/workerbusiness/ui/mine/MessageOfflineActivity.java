package com.netmi.workerbusiness.ui.mine;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ImageUploadUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.entity.mine.GetApplyInfo;
import com.netmi.workerbusiness.databinding.ActivityOfflineMessageBinding;
import com.netmi.workerbusiness.ui.utils.PermissionUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;

//信息管理
public class MessageOfflineActivity extends BaseActivity<ActivityOfflineMessageBinding> {
    //身份证正面 请求打开相册的requestCode
    private static final int REQUEST_OPEN_ALBUM_POSITIVE = 1002;
    //身份证反面
    private static final int REQUEST_OPEN_ALBUM_NEGATIVE = 1003;
    //手持身份证
    private static final int REQUEST_OPEN_ALBUM_HAND = 1011;
    //营业执照
    private static final int REQUEST_OPEN_ALBUM_BUSINESS_LICENSE = 1006;
    private int currentCode = -1;
    private int mCurrentStep = 0;//当前执行的操作，0位默认，1为请求相机，2为请求读写权限
    private String[] permissions = new String[]{PermissionUtils.PERMISSION_CAMERA, PermissionUtils.PERMISSION_READ_STORAGE, PermissionUtils.PERMISSION_WRITE_STORAGE};


    //身份证正面图片
    private String positiveUrl;
    //身份证反面图片
    private String negativeUrl;
    //    //手持身份证图片
    private String handtiveUrl;
    //营业执照
    private String buiness_license;
    String edit = "";
    @Override
    protected int getContentView() {
        return R.layout.activity_offline_message;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("信息管理");
        TextView rightSetting = getRightSetting();
//        rightSetting.setText("保存");
//        rightSetting.setTextColor(Color.parseColor("#007AFF"));
        rightSetting.setOnClickListener(this::doClick);
    }

    @Override
    protected void initData() {
        edit = getIntent().getStringExtra("edit");
                if (edit.equals("1")){
                    doGetShopInfo();
                }

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int code = -1;
        int id = view.getId();
        if(id == R.id.tv_setting){

        }else if(id == R.id.round_image_car1_layout){
            code = REQUEST_OPEN_ALBUM_POSITIVE;
        }else if(id == R.id.round_image_car1_layout2){
            code = REQUEST_OPEN_ALBUM_NEGATIVE;
        }else if(id == R.id.re_hold_car){
            code = REQUEST_OPEN_ALBUM_HAND;
        }else if(id == R.id.round_image_license_layout){
            code = REQUEST_OPEN_ALBUM_BUSINESS_LICENSE;
        }
        currentCode = code;
        if (code != -1) {
            //查看是否拥有相机权限
            mCurrentStep = 1;
            if (new PermissionUtils().checkPermission(permissions, MessageOfflineActivity.this)) {
                openAlbum(code);
            } else {
                PermissionUtils.checkAndRequestPermissions(MessageOfflineActivity.this, permissions);
            }
        }
    }

    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(com.netmi.workerbusiness.data.api.MineApi.class)
                .getApplyInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<GetApplyInfo>>() {
                    @Override
                    public void onSuccess(BaseData<GetApplyInfo> data) {
                        ActivityOfflineMessageBinding mBinding = (ActivityOfflineMessageBinding) MessageOfflineActivity.this.mBinding;
                        mBinding.setModel(data.getData());
                        if(!TextUtils.isEmpty(data.getData().getFront_card_url())){
                            mBinding.tvTextCar1.setVisibility(View.GONE);
                        }
                        if(!TextUtils.isEmpty(data.getData().getBack_card_url())){
                            mBinding.tvTextCar2.setVisibility(View.GONE);
                        }
                        if(!TextUtils.isEmpty(data.getData().getHold_card_url())){
                            mBinding.tvTextCar3.setVisibility(View.GONE);
                        }else {
                            mBinding.reHoldCar.setVisibility(View.GONE);
                        }
                        if(!TextUtils.isEmpty(data.getData().getLicense_url())){
                            mBinding.tvTextLicense.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFail(BaseData<GetApplyInfo> data) {
                        super.onFail(data);
                        if(data.getErrmsg().contains("资料未上传")){
                            showError("店铺资料未上传补充");
                        }
                    }
                });
    }

    //跳转相册选择
    private void openAlbum(int code) {
        mCurrentStep = 0;
        ImagePicker.getInstance().setSelectLimit(1);
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, code);
    }
    //权限获取
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQUEST_CODE) {
            if (new PermissionUtils().checkPermission(permissions, MessageOfflineActivity.this)) {
                openAlbum(currentCode);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

            ImageUploadUtils.uploadByOssZip(images, this, urls -> {
                if (currentCode == REQUEST_OPEN_ALBUM_POSITIVE) {
                    positiveUrl = urls.get(0);

                    mBinding.setIdTop(positiveUrl);
                    mBinding.tvTextCar1.setVisibility(View.INVISIBLE);
                } else if (currentCode == REQUEST_OPEN_ALBUM_NEGATIVE) {
                    negativeUrl = urls.get(0);

                    mBinding.setIdTopRight(negativeUrl);
                    mBinding.tvTextCar2.setVisibility(View.INVISIBLE);
                }
                else if (currentCode == REQUEST_OPEN_ALBUM_HAND) {
                    handtiveUrl = urls.get(0);

                    mBinding.setIdBottom(handtiveUrl);
                    mBinding.tvTextCar3.setVisibility(View.INVISIBLE);
                }
                else if (currentCode == REQUEST_OPEN_ALBUM_BUSINESS_LICENSE) {
                    buiness_license = urls.get(0);

                    mBinding.setIdBottom2(buiness_license);
                    mBinding.tvTextLicense.setVisibility(View.INVISIBLE);
                }
            }, null);

        }
    }





}
