package com.netmi.workerbusiness.ui.home.commodity.online;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityGoodDetailRichTextBinding;
import com.netmi.workerbusiness.ui.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_TAKE;

public class GoodDetailRichTextActivity extends BaseActivity<ActivityGoodDetailRichTextBinding> {

    public static final int PERMISSION_REQUSTER_CODE = 100;
    public static final String RICH_TEXT_STR = "rich_text_str";
    private String color = "#333333";


    String[] mPermissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected int getContentView() {
        return R.layout.activity_good_detail_rich_text;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("编辑商品详情");
        getRightSetting().setText("保存");
        //初始化编辑高度
        // mEditor.setEditorHeight(200);
        //初始化字体大小
        mBinding.editor.setEditorFontSize(16);
        //初始化字体颜色
        mBinding.editor.setEditorFontColor(getResources().getColor(R.color.ff333333));
        //mEditor.setEditorBackgroundColor(Color.BLUE);

        //初始化内边距
        mBinding.editor.setPadding(10, 10, 10, 10);
        //设置编辑框背景，可以是网络图片
        // mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        // mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        //设置默认显示语句
        mBinding.editor.setPlaceholder("请输入商品内容");
        //设置编辑器是否可用
        mBinding.editor.setInputEnabled(true);

//        mBinding.editor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
//            @Override
//            public void onTextChange(String text) {
//                mBinding.editor.setTextColor(Color.parseColor(color));
//            }
//        });

        if (!TextUtils.isEmpty(getIntent().getStringExtra(RICH_TEXT_STR))) {
            mBinding.editor.setHtml(getIntent().getStringExtra(RICH_TEXT_STR));
        }
    }

    private Disposable mDisposable;

    @Override
    protected void onDestroy() {
        cancelTask();
        super.onDestroy();
    }

    @Override
    protected void initData() {
        mBinding.editor.setFocusable(true);
        mBinding.editor.setFocusableInTouchMode(true);
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        Log.e(TAG, "onNext: " + number + url);
//                        if (url != null) {
//                            insertImg(url);
//                            url = null;
//                        }
                        if (urlList.size() != 0) {
                            for (int i = 0; i < urlList.size(); i++) {
                                insertImg(urlList.get(i));
                            }
                            urlList.clear();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //取消订阅
                        cancelTask();
                    }

                    @Override
                    public void onComplete() {
                        //取消订阅
                        cancelTask();
                    }
                });
    }

    private void cancelTask() {
        if (mDisposable != null)
            mDisposable.dispose();
    }

    private int mCurrentStep = 0;//当前执行的操作，0位默认，1为请求相机，2为请求读写权限

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.ll_font_weight) {
            mBinding.editor.setBold();
        } else if (id == R.id.ll_camera) {//查看是否拥有相机权限
            mCurrentStep = 1;
            PermissionUtils permissionUtils = new PermissionUtils();
            if (permissionUtils.checkPermission(mPermissionList, GoodDetailRichTextActivity.this)) {
                openAlum();
            } else {
                ActivityCompat.requestPermissions(this, mPermissionList, PERMISSION_REQUSTER_CODE);
            }
        } else if (id == R.id.ll_keyboard) {
        } else if (id == R.id.cv_333333) {
            mBinding.editor.setTextColor(getResources().getColor(R.color.ff333333));
            color = "#333333";
        } else if (id == R.id.cv_4b8efe) {
            mBinding.editor.setTextColor(Color.parseColor("#4b8efe"));
            color = "#4b8efe";
        } else if (id == R.id.cv_ffd906) {
            mBinding.editor.setTextColor(Color.parseColor("#ffd906"));
            color = "#ffd906";
        } else if (id == R.id.cv_ff0000) {
            mBinding.editor.setTextColor(Color.parseColor("#ff0000"));
            color = "#ff0000";
        } else if (id == R.id.cv_obdb4c) {
            mBinding.editor.setTextColor(Color.parseColor("#0bdb4c"));
            color = "#0bdb4c";
        } else if (id == R.id.tv_setting) {
            if (!TextUtils.isEmpty(mBinding.editor.getHtml())) {
                Intent intent = new Intent();
                intent.putExtra(RICH_TEXT_STR, mBinding.editor.getHtml());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtils.showShort("富文本为空");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUSTER_CODE:
                boolean writeExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && writeExternalStorage && readExternalStorage) {
                    //选择图片
                    ImagePicker.getInstance().setMultiMode(true);
                    ImagePicker.getInstance().setCrop(false);
                    startActivityForResult(new Intent(this, ImageGridActivity.class), REQUEST_CODE_TAKE);
                } else {
                    Toast.makeText(this, "请设置必要权限", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private String url = null;
    private ArrayList<String> urlList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_TAKE) {
                final ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && !images.isEmpty()) {
                    showProgress("");
                    for (int i = 0; i < images.size(); i++) {
                        new OssUtils().initOss().putObjectSync(images.get(i).path, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                            @Override
                            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                /**拿到URL*/url = OssUtils.OSS_HOST + request.getObjectKey();
                                urlList.add(url);
                                Log.e(TAG, "onSuccess: " + url);
                                hideProgress();
                            }

                            @Override
                            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                                ToastUtils.showShort("上传失败，请重试！");
                                hideProgress();
                            }
                        });
                    }
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void insertImg(final String url, final int percent) {
        mBinding.editor.focusEditor();
        mBinding.editor.insertImage(url.trim(), "huangxiaoguo\" style=\"max-width:" + percent + "%");

    }

    private void insertImg(String url) {
        insertImg(url, 100);
    }

    //打开相机
    private void openAlum() {
        //返回true说明可以直接进行下一步
        mCurrentStep = 0;
        ImagePicker.getInstance().setSelectLimit(9);
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TAKE);
    }

}
