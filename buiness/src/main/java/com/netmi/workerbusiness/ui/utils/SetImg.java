package com.netmi.workerbusiness.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//将布局View转为图片保存本地
public class SetImg {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void saveBitmap(View v, Context context) {
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }


        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "客商e宝图片";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        Bitmap bm = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        v.draw(canvas);
        String TAG = "TIKTOK";
        Log.e(TAG, "保存图片");
        /*  String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "同业宝名片";*/

        /* File f = new File(storePath, fileName);*/
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "保存图片");
            Toast.makeText(context,"保存图片成功",Toast.LENGTH_LONG).show();
            notfil(file,context);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "保存文件失败");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "保存失败");
            e.printStackTrace();
        }
    }
    //这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！
    public static void notfil(File file, Context context){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public static void saveBitmap(Bitmap bm, Context context) {
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }


        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "客商e宝图片";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        String TAG = "TIKTOK";
        Log.e(TAG, "保存图片");
        /*  String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "同业宝名片";*/

        /* File f = new File(storePath, fileName);*/
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "保存图片");
            Toast.makeText(context,"保存图片成功",Toast.LENGTH_LONG).show();
            notfil(file,context);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "保存文件失败");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "保存失败");
            e.printStackTrace();
        }
    }
}
