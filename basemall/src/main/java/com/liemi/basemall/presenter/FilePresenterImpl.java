package com.liemi.basemall.presenter;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.liemi.basemall.contract.FileContract;
import com.liemi.basemall.data.api.FileApi;
import com.liemi.basemall.data.entity.FileEntity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UpFilesEntity;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.oss.MultiPutListener;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Bingo on 2018/6/1.
 */

public class FilePresenterImpl implements FileContract.Presenter {
    private FileContract.FilesUpView filesUpView;
    private FileContract.FileUpView fileUpView;
    private OssUtils ossUtils;


    public FilePresenterImpl setFilesUpView(FileContract.FilesUpView filesUpView) {
        this.filesUpView = filesUpView;
        return this;
    }

    public FilePresenterImpl setFileUpView(FileContract.FileUpView fileUpView) {
        this.fileUpView = fileUpView;
        return this;
    }

    public FilePresenterImpl() {

        ossUtils = new OssUtils().initOss();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        fileUpView=null;
        filesUpView=null;
    }

    @Override
    public void onError(String message) {
        ToastUtils.showShort(message);
    }

    /**
     * 多图上传
     * @param filePaths 文件路径
     */
    public void filesUp(List<String> filePaths){
        if (filePaths==null || filePaths.size()<=0){
            filesUpView.filesUpFailure("请选择需要上传的图片");
            return;
        }
        final List<String> containHttp=new ArrayList<>();
        Iterator<String> pathIterator=filePaths.iterator();
        while (pathIterator.hasNext()){
            String filePath=pathIterator.next();
            if (filePath.contains("http")){
                containHttp.add(filePath);
                pathIterator.remove();
            }
        }
        if (filePaths.size()<=0){
            filesUpView.filesUpSuccess(containHttp);
            return;
        }

        filesUpView.showProgress("上传中");
        ossUtils.multiPutObjectSync(filePaths, 0, new MultiPutListener() {
            @Override
            public void onSuccess(List<String> uploads) {
                System.gc();
                filesUpView.hideProgress();
                if (uploads!=null){
                    uploads.addAll(0,containHttp);
                }else{
                    filesUpView.filesUpFailure("服务端保存图片失败");
                    return;
                }

                filesUpView.filesUpSuccess(uploads);
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                System.gc();
                filesUpView.hideProgress();
                ToastUtils.showShort("提交图片失败");
            }
        });


    }


    /**
     * 上传文件
     * @param filePath 路径
     */
    public void fileUp(String filePath) {
        if (Strings.isEmpty(filePath)){
            fileUpView.fileFailure("文件路径缺失");
            return;
        }
        if (filePath.contains("http")){
            UpFilesEntity entity=new UpFilesEntity();
            entity.setUrl(filePath);
            fileUpView.fileUpSuccess(entity);
            return;
        }
        fileUpView.showProgress("");
        File file=new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        io.reactivex.Observable<BaseData<UpFilesEntity>> observable= RetrofitApiFactory.createApi(FileApi.class)
                .fileUp(part)
                .compose(RxSchedulers.<BaseData<UpFilesEntity>>compose());
        if (fileUpView instanceof RxAppCompatActivity){
            observable=observable.compose(((RxAppCompatActivity)fileUpView).<BaseData<UpFilesEntity>>bindUntilEvent(ActivityEvent.DESTROY));
        }else{
            observable=observable.compose(((RxFragment)fileUpView).<BaseData<UpFilesEntity>>bindUntilEvent(FragmentEvent.DESTROY));
        }

        observable.subscribe(new XObserver<BaseData<UpFilesEntity>>() {
            @Override
            protected void onError(ApiException ex) {
                fileUpView.hideProgress();
                fileUpView.fileFailure(ex.getMessage());
            }

            @Override
            public void onSuccess(BaseData<UpFilesEntity> data) {
                fileUpView.hideProgress();
                fileUpView.fileUpSuccess(data.getData());
            }

            @Override
            public void onFail(BaseData<UpFilesEntity> data) {
                fileUpView.hideProgress();
                fileUpView.fileFailure(data.getErrmsg());
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
