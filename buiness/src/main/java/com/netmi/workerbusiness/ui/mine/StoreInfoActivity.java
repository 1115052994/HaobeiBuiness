package com.netmi.workerbusiness.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.liemi.basemall.ui.personal.userinfo.ChangeHeadSexDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.ImageUploadUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.cache.HeadUrlCache;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.data.event.LocationEvent;
import com.netmi.workerbusiness.data.event.StoreRemarkEvent;
import com.netmi.workerbusiness.databinding.ActivityStoreInfoBinding;
import com.netmi.workerbusiness.ui.mine.wallet.BaiduMapActivity;
import com.netmi.workerbusiness.ui.utils.PermissionUtils;
import com.netmi.workerbusiness.widget.RequestPermissionDialog;
import com.tencent.mm.opensdk.utils.Log;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;

public class StoreInfoActivity extends BaseActivity<ActivityStoreInfoBinding> implements RequestPermissionDialog.RequestPermissionResultListener {
    //头像请求打开相机的requestCode
    private static final int REQUEST_OPEN_CAMERA = 1001;
    //头像请求打开相册的requestCode
    private static final int REQUEST_OPEN_ALBUM = 1002;
    //店铺图片请求打开相机的requestCode
    private static final int SHOP_REQUEST_OPEN_CAMERA = 1003;
    //店铺图片请求打开相册的requestCode
    private static final int SHOP_REQUEST_OPEN_ALBUM = 1004;
    //请求修改简介的requestCode
    public static final int REQUEST_CHANGE_REMARK = 2001;
    //剪辑图片
    private static final int CUT_PICTURE = 10010;

    private ChangeHeadSexDialog changeHeadDialog;

    private int dialogTyp = 0;   // 0为 更换头像，1为更换店铺图片

    private int mCurrentStep = 0;//当前执行的操作，0位默认，1为请求相机，2为请求读写权限
    //请求权限
    private static final int REQUEST_PERMISSIONS = 3002;
    private int shopId;
    private String logo_url;
    private String shop_url;

    private String longitude;
    private String latitude;
    private String p_name;
    private String c_name;
    private String d_name;
    private String p_id;
    private String c_id;
    private String d_id;
    private String address;

    @Override
    protected int getContentView() {
        return R.layout.activity_store_info;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("信息修改");
        shopId = Integer.valueOf(getIntent().getExtras().getString(JumpUtil.VALUE));
        doGetShopInfo();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        Bundle args = new Bundle();
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.ll_logo) {
            dialogTyp = 0;
            showChangeHeadDialog();
        } else if (id == R.id.ll_remark) {
            args.putSerializable(JumpUtil.VALUE, mBinding.getModel());
            JumpUtil.startForResult(this, StoreRemarkActivity.class, REQUEST_CHANGE_REMARK, args);
        } else if (id == R.id.ll_time_choose) {
            showTimePicker();
        } else if (id == R.id.ll_location) {
            //地理位置
            args.putInt(JumpUtil.ID, shopId);
            args.putString(JumpUtil.TYPE, logo_url);
            args.putString(JumpUtil.VALUE, mBinding.etName.getText().toString());
            args.putString(JumpUtil.CODE, mBinding.tvRemark.getText().toString());
            args.putString(JumpUtil.FLAG, time);
            args.putString(JumpUtil.UID, shop_url);
//            JumpUtil.overlay(getContext(), LocationActivity.class, args);
            JumpUtil.overlay(getContext(), BaiduMapActivity.class);


        } else if (id == R.id.ll_shop_pic) {
            dialogTyp = 1;
            showChangeHeadDialog();
        } else if (id == R.id.tv_confirm) {
            doUpdateShopInfo(shopId, logo_url, mBinding.etName.getText().toString(), mBinding.tvRemark.getText().toString(), time, longitude, latitude, p_name, c_name, d_name, address, shop_url, p_id, c_id, d_id);

        }
    }

    private String[] needsPermissions = {PermissionUtils.PERMISSION_CAMERA, PermissionUtils.PERMISSION_READ_STORAGE, PermissionUtils.PERMISSION_WRITE_STORAGE};

    //显示用户更换头像的dialog
    private void showChangeHeadDialog() {
        if (changeHeadDialog == null) {
            changeHeadDialog = new ChangeHeadSexDialog(this);
        }
        changeHeadDialog.setButtonStr(getString(R.string.take_new_photo), getString(R.string.select_from_photo_gallery));
        if (!changeHeadDialog.isShowing()) {
            changeHeadDialog.showBottom();
        }
        changeHeadDialog.setClickFirstItemListener(string -> {
            //查看是否拥有相机权限
            mCurrentStep = 1;
            PermissionUtils permissionUtils = new PermissionUtils();
            if (permissionUtils.checkPermission(new String[]{PermissionUtils.PERMISSION_CAMERA},
                    StoreInfoActivity.this)) {
                openCamera();
            } else {
                ActivityCompat.requestPermissions(this, needsPermissions, REQUEST_PERMISSIONS);
            }

            changeHeadDialog.dismiss();
        });
        changeHeadDialog.setClickSecondItemListener(string -> {
            //查看是否拥有文件读写权限
            PermissionUtils permissionUtils = new PermissionUtils();
            mCurrentStep = 2;
            if (permissionUtils.checkPermission(new String[]{PermissionUtils.PERMISSION_READ_STORAGE, PermissionUtils.PERMISSION_WRITE_STORAGE},
                    StoreInfoActivity.this)) {
                openAlbum();
            } else {
                ActivityCompat.requestPermissions(this, needsPermissions, REQUEST_PERMISSIONS);
            }
            changeHeadDialog.dismiss();
        });
    }

    //打开相机
    private void openCamera() {
        //返回true说明可以直接进行下一步
        if (dialogTyp == 0) {
            mCurrentStep = 0;
            ImagePicker.getInstance().setSelectLimit(1);
            Intent intent = new Intent(this, ImageGridActivity.class);
            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true);
            startActivityForResult(intent, REQUEST_OPEN_CAMERA);
        } else if (dialogTyp == 1) {
            mCurrentStep = 0;
            ImagePicker.getInstance().setSelectLimit(1);
            Intent intent = new Intent(this, ImageGridActivity.class);
            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true);
            startActivityForResult(intent, SHOP_REQUEST_OPEN_CAMERA);
        }
    }

    //打开相册
    private void openAlbum() {
        if (dialogTyp == 0) {
            mCurrentStep = 0;
            ImagePicker.getInstance().setSelectLimit(1);
            ImagePicker.getInstance().setMultiMode(false);
            ImagePicker.getInstance().setCrop(true);                            //允许裁剪（单选才有效）
            ImagePicker.getInstance().setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
            ImagePicker.getInstance().setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
            ImagePicker.getInstance().setOutPutX(1000);                         //保存文件的宽度。单位像素
            ImagePicker.getInstance().setOutPutY(1000);                         //保存文件的高度。单位像素
            Intent intent = new Intent(this, ImageGridActivity.class);
            startActivityForResult(intent, REQUEST_OPEN_CAMERA);
            android.util.Log.e("weng", dialogTyp + "");
        } else if (dialogTyp == 1) {
            mCurrentStep = 0;
            ImagePicker.getInstance().setSelectLimit(1);
            Intent intent = new Intent(this, ImageGridActivity.class);
            startActivityForResult(intent, SHOP_REQUEST_OPEN_CAMERA);
        }
    }

    @Override
    public void requestPermissionFinish(boolean result) {
        //权限请求回调结果
        if (result) {
            if (mCurrentStep == 1) {
                openCamera();
            } else if (mCurrentStep == 2) {
                openAlbum();
            }
        } else {
            showError(getString(R.string.not_granted_permission));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_OPEN_CAMERA || requestCode == REQUEST_OPEN_ALBUM) && data != null) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            ImageUploadUtils.uploadByOss(images, this, urls -> {
                logo_url = urls.get(0);
                mBinding.setLogoUrl(logo_url);
            }, null);
        } else if ((requestCode == SHOP_REQUEST_OPEN_CAMERA || requestCode == SHOP_REQUEST_OPEN_ALBUM) && data != null) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            ImageUploadUtils.uploadByOss(images, this, urls -> {
                shop_url = urls.get(0);
                mBinding.setShopUrl(shop_url);
            }, null);
        } else if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHANGE_REMARK) {
                data.getExtras().getString(JumpUtil.VALUE);
                Log.e("weng", data.getExtras().getString(JumpUtil.VALUE));
            }
        }
    }

    /**
     * 更新用户信息
     */
    private void doUpdateShopInfo(Integer id, String logo_url, String name, String remark, String opening_hours, String longitude, String latitude
            , String p_name, String c_name, String d_name, String address, String img_url,String p_id, String c_id, String d_id) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUpdateShopInfo(id, logo_url, name, remark, opening_hours, longitude, latitude, p_name, c_name, d_name,address, img_url ,p_id,c_id,d_id)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        finish();
                    }
                });
    }

    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        mBinding.setModel(data.getData());
                        logo_url = data.getData().getLogo_url();
                        shop_url = data.getData().getImg_url();
                        mBinding.setLogoUrl(logo_url);
                        mBinding.setShopUrl(shop_url);
                        mBinding.tvAddress.setText(data.getData().getFull_name());

                        time = data.getData().getOpening_hours();
                        mBinding.tvRemark.setText(data.getData().getRemark());
                        longitude = data.getData().getLongitude();
                        latitude = data.getData().getLatitude();
                        p_name = data.getData().getP_name();
                        c_name = data.getData().getC_name();
                        d_name = data.getData().getD_name();
                        p_id = data.getData().getP_id();
                        c_id = data.getData().getC_id();
                        d_id = data.getData().getD_id();
                        address = data.getData().getAddress();
                        if (data.getData().getShop_user_type() == 1) {
                            mBinding.llTimeChoose.setVisibility(View.GONE);
                            mBinding.llLocation.setVisibility(View.GONE);
                        }
                        HeadUrlCache.put(data.getData().getLogo_url());


                    }
                });
    }

    // 接收广播
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void remark(StoreRemarkEvent event) {
        mBinding.tvRemark.setText(event.getRemark());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void location(LocationEvent event) {
        longitude = event.getLongitude();
        latitude = event.getLatitude();
        p_name = event.getP_name();
        c_name = event.getC_name();
        d_name = event.getD_name();
        p_id = event.getP_id();
        c_id = event.getC_id();
        d_id = event.getD_id();
        address = event.getAddress();
        mBinding.tvAddress.setText(c_name+""+d_name+"  "+address);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        doGetShopInfo();
    }

    private TimePickerView pickerTimeView;
    private TimePickerView pickerTimeViewTwo;
    private Date date;
    private String time;

    private void showTimePicker() {
        if (pickerTimeView == null) {
            //时间选择器
            pickerTimeView = new TimePickerView.Builder(getContext(), (Date date, View v) -> {
                this.date = date;
                time = DateUtil.formatDateTime(date, DateUtil.DF_HH_MM);
                showTimePicker2();
            })
                    .setTitleText("选择开业时间")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{false, false, false, true, true, false})
                    .build();
        }
        pickerTimeView.setDate(DateUtil.getCalendar(date));
        pickerTimeView.show();
    }


    private void showTimePicker2() {
        if (pickerTimeViewTwo == null) {
            //时间选择器
            pickerTimeViewTwo = new TimePickerView.Builder(getContext(), (Date date2, View v2) -> {
                this.date = date2;
                time = time + "~" + DateUtil.formatDateTime(date2, DateUtil.DF_HH_MM);
                mBinding.tvTime.setText(time);
            })
                    .setTitleText("选择关门时间")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{false, false, false, true, true, false})
                    .build();
        }
        pickerTimeViewTwo.setDate(DateUtil.getCalendar(date));
        pickerTimeViewTwo.show();

    }
}
