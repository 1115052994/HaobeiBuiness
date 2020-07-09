package com.liemi.basemall.presenter;

import android.graphics.Bitmap;

import com.lzy.imagepicker.bean.ImageItem;
import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.ui.BaseView;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.ImageUtils;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Bingo on 2018/7/16.
 */

public class CompressPresenterImpl implements BasePresenter {

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
        compressView=null;
    }

    @Override
    public void onError(String message) {
        ToastUtils.showShort(message);
    }

    public interface CompressView extends BaseView {
        void compressSuccess(List<String> compressPaths);
        void compressFailure(String msg);
    }

    private CompressView compressView;

    public CompressPresenterImpl setCompressView(CompressView compressView) {
        this.compressView = compressView;
        return this;
    }



    public void compressFiles2(final List<String> filePaths){
        compressView.showProgress("");
        Observable<List<String>> observable= Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter e) {
                try {
                    List<String> compressPaths=new ArrayList<>();
                    String saveFilePath;
                    for (String filePath : filePaths) {
                        if (filePath.contains("http")){
                            compressPaths.add(filePath);
                        }else {
                            saveFilePath = AppManager.getApp().getCacheDir() + "/img/" + new Date().getTime() + ".jpg";
                            if (ImageUtils.save(ImageUtils.compressByQuality(ImageUtils.getBitmap(filePath), 1024 * 1024L), saveFilePath, Bitmap.CompressFormat.JPEG)) {
                                compressPaths.add(saveFilePath);
                            } else {
                                compressView.hideProgress();
                                compressView.compressFailure("提交图片失败");
                                break;
                            }
                        }
                    }

                    e.onNext(compressPaths);
                }catch (Exception ex){
                    compressView.hideProgress();
                    compressView.compressFailure("提交图片失败");
                }

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
        if (compressView instanceof RxAppCompatActivity){
            observable=observable.compose(((RxAppCompatActivity)compressView).<List<String>>bindUntilEvent(ActivityEvent.DESTROY));
        }else{
            observable=observable.compose(((RxFragment)compressView).<List<String>>bindUntilEvent(FragmentEvent.DESTROY));
        }
        observable.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> compressPaths) throws Exception {
                //允许图片最大1m
                if (compressPaths != null && compressPaths.size() > 0) {
                    compressView.hideProgress();
                    compressView.compressSuccess(compressPaths);
                } else {
                    compressView.hideProgress();
                    compressView.compressFailure("提交图片失败");
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                compressView.hideProgress();
                compressView.compressFailure("提交图片失败");
            }
        });
    }

    public void compressFiles(final List<ImageItem> images){
        compressView.showProgress("压缩中");
        Observable<List<String>> observable= Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter e) {
                try {
                    List<String> compressPaths=new ArrayList<>();
                    String saveFilePath;
                    for (ImageItem image : images) {
                        if (image.path.contains("http")){
                            compressPaths.add(image.path);
                        }else {
                            saveFilePath = AppManager.getApp().getCacheDir() + "/img/" + new Date().getTime() + ".jpg";
                            if (ImageUtils.save(ImageUtils.compressImageFromFile(image.path), saveFilePath, Bitmap.CompressFormat.JPEG)) {
                                compressPaths.add(saveFilePath);
                            } else {
                                compressView.hideProgress();
                                compressView.compressFailure("提交图片失败");
                                break;
                            }
                        }
                    }

                    e.onNext(compressPaths);
                }catch (Exception ex){
                    System.gc();
                    compressView.hideProgress();
                    compressView.compressFailure("提交图片失败");
                }

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
        if (compressView instanceof RxAppCompatActivity){
            observable=observable.compose(((RxAppCompatActivity)compressView).<List<String>>bindUntilEvent(ActivityEvent.DESTROY));
        }else{
            observable=observable.compose(((RxFragment)compressView).<List<String>>bindUntilEvent(FragmentEvent.DESTROY));
        }
        observable.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> compressPaths) throws Exception {
                System.gc();
                //允许图片最大1m
                if (compressPaths != null && compressPaths.size() > 0) {
                    compressView.hideProgress();
                    compressView.compressSuccess(compressPaths);
                } else {
                    compressView.hideProgress();
                    compressView.compressFailure("提交图片失败");
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.gc();
                compressView.hideProgress();
                compressView.compressFailure("提交图片失败");
            }
        });
    }

    public void compressFilesFromImageItemByByQuality(final List<ImageItem> images){
        compressView.showProgress("");
        Observable<List<String>> observable= Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter e) {
                try {
                    List<String> compressPaths=new ArrayList<>();
                    String saveFilePath;
                    for (ImageItem image : images) {
                        if (image.path.contains("http")){
                            compressPaths.add(image.path);
                        }else {
                            saveFilePath = AppManager.getApp().getCacheDir() + "/img/" + new Date().getTime() + ".jpg";
                            if (ImageUtils.save(ImageUtils.compressByQuality(ImageUtils.getBitmap(image.path), 1024 * 1024L), saveFilePath, Bitmap.CompressFormat.JPEG)) {
                                compressPaths.add(saveFilePath);
                            } else {
                                compressView.hideProgress();
                                compressView.compressFailure("提交图片失败");
                                break;
                            }
                        }
                    }

                    e.onNext(compressPaths);
                }catch (Exception ex){
                    compressView.hideProgress();
                    compressView.compressFailure("提交图片失败");
                }

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
        if (compressView instanceof RxAppCompatActivity){
            observable=observable.compose(((RxAppCompatActivity)compressView).<List<String>>bindUntilEvent(ActivityEvent.DESTROY));
        }else{
            observable=observable.compose(((RxFragment)compressView).<List<String>>bindUntilEvent(FragmentEvent.DESTROY));
        }
        observable.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> compressPaths) throws Exception {
                //允许图片最大1m
                if (compressPaths != null && compressPaths.size() > 0) {
                    compressView.hideProgress();
                    compressView.compressSuccess(compressPaths);
                } else {
                    compressView.hideProgress();
                    compressView.compressFailure("提交图片失败");
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                compressView.hideProgress();
                compressView.compressFailure("提交图片失败");
            }
        });
    }

}
