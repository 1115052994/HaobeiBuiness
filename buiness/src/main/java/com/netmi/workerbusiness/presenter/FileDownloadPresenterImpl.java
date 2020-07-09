package com.netmi.workerbusiness.presenter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import com.bumptech.glide.Glide;
import com.netmi.workerbusiness.data.api.FileDownloadContract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/1
 * 修改备注：
 */
public class FileDownloadPresenterImpl implements FileDownloadContract.Presenter {

    private FileDownloadContract.View view;

    private Context context;

    private List<String> imgUrls = new ArrayList<>();

    public FileDownloadPresenterImpl(FileDownloadContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void doDownloadFiles(List<String> filePaths) {
        imgUrls.clear();
        Collections.reverse(filePaths);
        Observable.fromIterable(filePaths)
                .map(new Function<String, File>() {
                    @Override
                    public File apply(String path) throws Exception {
                        //保存当前图片
                        File file = Glide.with(context).download(path).submit().get();
                        //创建文件夹
                        File dir = new File(getFilePath(context,"SeashellMall"));
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        //String.valueOf(new Date().getTime())
                        //path.subSequence(path.lastIndexOf("/") + 1, path.length()).toString();
                        String saveFileName = String.valueOf(new Date().getTime()) +
                                path.subSequence(path.lastIndexOf("."), path.length()).toString();
                        //将图片复制到本地
                        File saveFile = new File(dir, saveFileName);
                        if (!saveFile.exists()) {
                            FileInputStream fis = new FileInputStream(file);
                            FileOutputStream fos = new FileOutputStream(saveFile);
                            byte[] b = new byte[1028 * 8];
                            while (fis.read(b) != -1) {
                                fos.write(b);
                            }
                        }
                        //通知系统更新图库
                        String extension = MimeTypeMap.getFileExtensionFromUrl(saveFile.getAbsolutePath());
                        String mimeTypes = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                        MediaScannerConnection.scanFile(context, new String[]{saveFile.getAbsolutePath()}, new String[]{mimeTypes}, null);

                        //通知相册更新
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(file);
                        intent.setData(uri);
                        context.sendBroadcast(intent);

                        return saveFile;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(File file) {
                        imgUrls.add(file.getAbsolutePath());
                        view.fileDownloadProgress((int) ((imgUrls.size() / (float) filePaths.size() * 100)));
                    }

                    @Override
                    public void onError(Throwable t) {
                        view.fileDownloadFail(t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.fileDownloadResult(imgUrls);
                    }
                });
    }

    public String getFilePath(Context context, String dir) {
        String directoryPath="";
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {//判断外部存储是否可用
            directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+dir;
        }else{//没外部存储就使用内部存储
            directoryPath=context.getFilesDir()+ File.separator+dir;
        }
        File file = new File(directoryPath);
        if(!file.exists()){//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
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

    }

    @Override
    public void onError(String message) {

    }
}
