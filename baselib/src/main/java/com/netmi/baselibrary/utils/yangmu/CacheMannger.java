package com.netmi.baselibrary.utils.yangmu;

import android.content.Context;
import android.text.format.Formatter;

import java.io.File;


/**
 * Created by Yangmu on 17/1/22.
 */

public class CacheMannger {
    private static final String TAG = "ym";
    private static SdCardUtils sdCard;
    private static int i = 0;
//    应用缓存目录
    private static String savePath = "";

    public static void setSavePath(String savePath) {
        CacheMannger.savePath = savePath;
    }

//        private static String savePath = AppConfig.SAVEDFILE_LOCATION;
    //清除应用缓存（主要是图片）
    public static void clearCache(){
        File file = new File(savePath);
        if (!file.exists()){
            file.mkdirs();
        }
        clearFile(file);
    }
    //获取缓存文件夹内存大小
    public static String getCacheSize(Context c){

        File file = new File(savePath);
        if (!file.exists()){
            file.mkdirs();
        }

        if (sdCard == null){
            sdCard = new SdCardUtils();
        }
//        Log.e(TAG, "getCacheSize: "+(sdCard==null)+(file==null));
//        Log.e(TAG, "getCacheSize: "+file.getAbsolutePath());
        long totalSize = sdCard.getFolderSize(file);
        String size = Formatter.formatFileSize(c, totalSize);
        return size;
    }

    public static void clearFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                clearFile(f);
            }
            file.delete();
        }
    }
    //测试用
//    public  static void test(){
//        sdCard.test("38.txt",savePath);
//    }


}
