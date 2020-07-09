package com.liemi.basemall.ui.personal.userinfo;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bigkoo.pickerview.TimePickerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.databinding.ActivityUserInfoBinding;
import com.liemi.basemall.ui.NormalDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding> {
    //请求打开相机的requestCode
    private static final int REQUEST_OPEN_CAMERA = 1001;
    //请求打开相册的requestCode
    private static final int REQUEST_OPEN_ALBUM = 1002;
    //请求跳转修改昵称页面的requestCode
    private static final int REQUEST_OPEN_CHANGE_NICK_NAME = 1003;

    private int mCurrentStep = 0;//当前执行的操作，0位默认，1为请求相机，2为请求读写权限

    private UserInfoEntity userInfoEntity;

    private ChangeHeadSexDialog changeHeadDialog;
    private ChangeHeadSexDialog changeSexDialog;
    private TimePickerView changeBirthdayView;
    //用户退出登录的提示
    private NormalDialog mLogoutConfirmDialog;

    private UpdateHeadImageHandler handler = new UpdateHeadImageHandler();

    @Override
    protected int getContentView() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void initUI() {
        getTvTitle().setText("个人信息");
    }

    @Override
    protected void initData() {
        doUserInfo();
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            //共享元素动画的退出
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.ll_head_image) {
            showChangeHeadDialog();
            return;
        }
        if (view.getId() == R.id.ll_nick_name) {
            Bundle bundle = new Bundle();
            if (userInfoEntity != null) {
                bundle.putString(ChangeNickNameActivity.USER_NICK_NAME, userInfoEntity.getNickname());
            }
            JumpUtil.startForResult(this, ChangeNickNameActivity.class, REQUEST_OPEN_CHANGE_NICK_NAME, bundle);
            return;
        }
        if (view.getId() == R.id.ll_sex) {
            showChangeSexDialog();
            return;
        }
        if (view.getId() == R.id.ll_birthday) {
            showChangeBirthday();
            return;
        }
        if (view.getId() == R.id.ll_phone) {
            JumpUtil.startForResult(this, ChangePhoneActivity.class, REQUEST_OPEN_CHANGE_NICK_NAME, null);

            return;
        }
        if (view.getId() == R.id.btn_exit) {
            //询问用户是否退出登录
            showLogoutDialog();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_OPEN_CAMERA || requestCode == REQUEST_OPEN_ALBUM) && data != null) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null) {
                Logs.i("拍照或相册返回数据：" + images.size());
                showProgress("上传中");
                //请求上传图片  TODO 为支持多种云存储,此处应采用 material/index/upload 接口
                OssUtils ossUtils = new OssUtils().initOss();
                ossUtils.putObjectSync(images.get(0).path, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        Message message = new Message();
                        message.obj = OssUtils.OSS_HOST + request.getObjectKey();
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {

                    }
                });
            }
        } else if (requestCode == REQUEST_OPEN_CHANGE_NICK_NAME && data != null) {
            if (data.getBooleanExtra("isChangeSuccess", false)) {
                doUserInfo();
            }
        }
    }

    //显示用户更换性别的dialog
    private void showChangeSexDialog() {
        if (changeSexDialog == null) {
            changeSexDialog = new ChangeHeadSexDialog(this);
            changeSexDialog.setButtonStr("男", "女");
        }
        if (!changeSexDialog.isShowing()) {
            changeSexDialog.showBottom();
        }
        changeSexDialog.setClickFirstItemListener(new ChangeHeadSexDialog.ClickFirstItemListener() {
            @Override
            public void clickFirstItem(String string) {
                if (userInfoEntity == null) {
                    showError("用户信息为空");
                    return;
                }
                if (userInfoEntity.getSex() != UserInfoEntity.SEX_MAN) {
                    doUpdateUserInfo(null, null, "1", null);
                }
                changeSexDialog.dismiss();
            }
        });
        changeSexDialog.setClickSecondItemListener(new ChangeHeadSexDialog.ClickSecondItemListener() {
            @Override
            public void clickSecondItem(String string) {
                if (userInfoEntity == null) {
                    showError("用户信息为空");
                    return;
                }
                if (userInfoEntity.getSex() != UserInfoEntity.SEX_WOMEN) {
                    doUpdateUserInfo(null, null, "2", null);
                }
                changeSexDialog.dismiss();
            }
        });
    }

    //显示用户更换头像的dialog
    private void showChangeHeadDialog() {
        if (changeHeadDialog == null) {
            changeHeadDialog = new ChangeHeadSexDialog(this);
        }
        changeHeadDialog.setButtonStr("拍摄新照片", "从照片库中选择");
        if (!changeHeadDialog.isShowing()) {
            changeHeadDialog.showBottom();
        }
        changeHeadDialog.setClickFirstItemListener(new ChangeHeadSexDialog.ClickFirstItemListener() {
            @Override
            public void clickFirstItem(String string) {
                //查看是否拥有相机权限
                mCurrentStep = 1;
                openCamera();

                changeHeadDialog.dismiss();
            }
        });
        changeHeadDialog.setClickSecondItemListener(new ChangeHeadSexDialog.ClickSecondItemListener() {
            @Override
            public void clickSecondItem(String string) {
                //查看是否拥有文件读写权限
                mCurrentStep = 2;
                openAlbum();
                changeHeadDialog.dismiss();
            }
        });

    }

    //显示询问用户是否退出登录
    private void showLogoutDialog() {
        if (mLogoutConfirmDialog == null) {
            mLogoutConfirmDialog = new NormalDialog(this);
        }
        if (!mLogoutConfirmDialog.isShowing()) {
            mLogoutConfirmDialog.show();
        }
        mLogoutConfirmDialog.setMessageInfo(getString(R.string.confirm_logout), true, 16, true);
        mLogoutConfirmDialog.setTitleInfo("", false);
        mLogoutConfirmDialog.showLineTitleWithMessage(false);
        mLogoutConfirmDialog.setCancelInfo("取消", true);
        mLogoutConfirmDialog.setConfirmInfo("确定");

        mLogoutConfirmDialog.setClickConfirmListener(new NormalDialog.ClickConfirmListener() {
            @Override
            public void clickConfirm() {
                MApplication.getInstance().gotoLogin();
            }
        });

    }

    //显示用户更换生日的view
    private void showChangeBirthday() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);
        Calendar endDate = Calendar.getInstance();
        changeBirthdayView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                doUpdateUserInfo(null, null, null, DateUtil.formatDateTime(date, "yyyy-MM-dd"));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")
                .setSubmitText("确定")
                .setSubmitColor(this.getResources().getColor(R.color.gray_1B252D))
                .setCancelColor(this.getResources().getColor(R.color.gray_1B252D))
                .setOutSideCancelable(true)
                .setRangDate(startDate, endDate)
                .setDate(endDate)
                .isCyclic(false)
                .isCenterLabel(false)
                .build();
        changeBirthdayView.show();
    }

    //打开相机
    private void openCamera() {
        //返回true说明可以直接进行下一步
        mCurrentStep = 0;
        ImagePicker.getInstance().setSelectLimit(1);
        Intent intent = new Intent(UserInfoActivity.this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true);
        startActivityForResult(intent, REQUEST_OPEN_CAMERA);
    }

    //打开相册
    private void openAlbum() {
        mCurrentStep = 0;
        ImagePicker.getInstance().setSelectLimit(1);
        Intent intent = new Intent(UserInfoActivity.this, ImageGridActivity.class);
        startActivityForResult(intent, REQUEST_OPEN_CAMERA);
    }

    //显示用户个人信息
    private void showUserInfo(UserInfoEntity userInfoEntity) {
        this.userInfoEntity = userInfoEntity;
        mBinding.setBirthday(userInfoEntity.getDate_birth());
        mBinding.setHeadImageUrl(userInfoEntity.getHead_url());
        mBinding.setNickName(userInfoEntity.getNickname());
        mBinding.setPhone(userInfoEntity.getPhone());
        mBinding.setSex(userInfoEntity.getSexFormat());
    }


    //请求个人信息
    private void doUserInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getUserInfo(1)
                .compose(this.<BaseData<UserInfoEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<UserInfoEntity>>compose())
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {

                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        showUserInfo(data.getData());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });

    }

    /*
     * 请求修改个人信息
     * */
    private void doUpdateUserInfo(final String headImage, final String nickName,  final String sex, final String birthday) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUserInfoUpdate(headImage,nickName, sex, birthday)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("修改成功");
                        if (!TextUtils.isEmpty(nickName)) {
                            mBinding.setNickName(nickName);
                            return;
                        }
                        if (!TextUtils.isEmpty(headImage)) {
                            mBinding.setHeadImageUrl(headImage);
                            return;
                        }
                        if (!TextUtils.isEmpty(sex)) {
                            if (sex.equals("1")) {
                                mBinding.setSex("男");
                            } else if (sex.equals("2")) {
                                mBinding.setSex("女");
                            } else {
                                mBinding.setSex("未知");
                            }
                        }
                        if (!TextUtils.isEmpty(birthday)) {
                            mBinding.setBirthday(birthday);
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });

    }


    public class UpdateHeadImageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideProgress();
            if (msg != null) {
                doUpdateUserInfo( String.valueOf(msg.obj),null, null, null);
            }
        }
    }

}
