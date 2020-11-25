package com.netmi.baselibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.lzy.imagepicker.bean.ImageItem;
import com.netmi.baselibrary.R;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UpFilesEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseView;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.netmi.baselibrary.utils.yangmu.FileUtils;
import com.netmi.baselibrary.utils.yangmu.PhotoMannager;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Sherlock
 * 创建时间：2019/5/28
 * 修改备注：
 */
public class ImageUploadUtils {

    /**
     * Oss 图片上传再次封装
     *
     * @param images                图片来源
     * @param view                  如果view不为空，上传时出提示框，成功或失败会有提示语句
     * @param successLisentner      成功时回调对象
     * @param uploadfailedLisentner 失败时回调对象
     */
    public static void uploadByOss(ArrayList<ImageItem> images, BaseView view, UploadSuccessListener successLisentner, OssUploadfailedLitsener uploadfailedLisentner) {
        List<String> resultUrls = new ArrayList<>();
        if (view != null)
            view.showProgress("");

        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                new OssUtils().initOss().putObjectSync(images.get(i).path, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        String url = OssUtils.OSS_HOST + request.getObjectKey();
                        Logs.e(url);
                        resultUrls.add(url);

                        if (successLisentner != null && resultUrls.size() == images.size()) {
                            if (view != null)
                                view.showError(ResourceUtil.getString(R.string.upload_success));
                            successLisentner.onSuccess(resultUrls);
                        }
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                        if (view != null)
                            view.showError(ResourceUtil.getString(R.string.upload_failed));
                        if (uploadfailedLisentner != null)
                            uploadfailedLisentner.onFailed(request, clientException, serviceException);
                        return;
                    }
                });
            }

        } else {
            if (view != null)
                view.hideProgress();
        }
    }


    /**
     * Oss 图片上传再次封装  带压缩
     *
     * @param images                图片来源
     * @param view                  如果view不为空，上传时出提示框，成功或失败会有提示语句
     * @param successLisentner      成功时回调对象
     * @param uploadfailedLisentner 失败时回调对象
     */
    public static void uploadByOssZip(ArrayList<ImageItem> images, BaseView view, UploadSuccessListener successLisentner, OssUploadfailedLitsener uploadfailedLisentner) {
        List<String> resultUrls = new ArrayList<>();
        if (view != null)
            view.showProgress("");

        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                new OssUtils().initOss().putObjectSyncZip(images.get(i).path, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        String url = OssUtils.OSS_HOST + request.getObjectKey();
                        Logs.e(url);
                        resultUrls.add(url);

                        if (successLisentner != null && resultUrls.size() == images.size()) {
                            if (view != null)
                                view.showError(ResourceUtil.getString(R.string.upload_success));
                            successLisentner.onSuccess(resultUrls);
                        }
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                        if (view != null)
                            view.showError(ResourceUtil.getString(R.string.upload_failed));
                        if (uploadfailedLisentner != null)
                            uploadfailedLisentner.onFailed(request, clientException, serviceException);
                        return;
                    }
                });
            }

        } else {
            if (view != null)
                view.hideProgress();
        }
    }

    /**
     * 后台图片上传
     *
     * @param filePaths             图片来源
     * @param view                  如果view不为空，上传时出提示框，成功或失败会有提示语句
     * @param successLisentner      成功时回调对象
     * @param uploadfailedLisentner 失败时回调对象
     */
    public void uploadFiles(List<ImageItem> filePaths, BaseView view, String cacheLocation, UploadSuccessListener successLisentner, UploadFailedLitsener uploadfailedLisentner) {
        List<String> resultUrls = new ArrayList<>();
        view.showProgress("");
        if (filePaths != null && !filePaths.isEmpty()) {
            for (int i = 0; i < filePaths.size(); i++) {
                File file = new File(filePaths.get(0).path);
                String orginalName = file.getName() + ".jpg";
                //1MB = 1024*1024B
                Logs.e(file.length() + "");
                if (file.length() >= 1024 * 1024) {
                    Bitmap bit = BitmapFactory.decodeFile(filePaths.get(0).path);
                    bit = PhotoMannager.scaleBitmap(bit);
                    file = PhotoMannager.saveBitmap(cacheLocation, orginalName, bit, 80);
                    Logs.e(file.length() + "");
                    bit.recycle();
                }
                XObserver observer = new XObserver<BaseData<UpFilesEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UpFilesEntity> baseData) {
                        Logs.e(baseData.getData().getUrl());
//                            doUpdateUserInfo(null, baseData.getData().getUrl());
                        resultUrls.add(baseData.getData().getUrl());
                        if (resultUrls.size() == filePaths.size()) {
                            view.showError(ResourceUtil.getString(R.string.upload_success));
                            if (successLisentner != null)
                                successLisentner.onSuccess(resultUrls);
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        if (uploadfailedLisentner != null)
                            uploadfailedLisentner.onFailed(ex.getMessage());
                        else
                            super.onError(ex);
                        return;
                    }

                    @Override
                    public void onFail(BaseData<UpFilesEntity> data) {
                        if (uploadfailedLisentner != null)
                            uploadfailedLisentner.onFailed(data.getErrmsg());
                        else
                            super.onFail(data);
                        return;
                    }
                };
                if (view instanceof BaseFragment) {
                    RetrofitApiFactory.createApi(CommonApi.class, false)
                            .uploadFiles(FileUtils.createMutipartPng(file))
                            .compose(((BaseFragment) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                            .compose(RxSchedulers.compose())
                            .subscribe(observer);
                } else if (view instanceof BaseActivity) {
                    RetrofitApiFactory.createApi(CommonApi.class, false)
                            .uploadFiles(FileUtils.createMutipartPng(file))
                            .compose(((BaseActivity) view).bindUntilEvent(ActivityEvent.DESTROY))
                            .compose(RxSchedulers.compose())
                            .subscribe(observer);
                }
            }
        } else {
            view.hideProgress();
        }
    }

    public interface UploadSuccessListener {
        void onSuccess(List<String> urls);
    }

    public interface OssUploadfailedLitsener {
        void onFailed(PutObjectRequest request, ClientException clientException, ServiceException serviceException);
    }

    public interface UploadFailedLitsener {
        void onFailed(String mess);
    }
}
