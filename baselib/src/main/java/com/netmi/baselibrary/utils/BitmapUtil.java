package com.netmi.baselibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by huangjun on 2016/9/19.
 */
public class BitmapUtil {


    /** 采样率压缩
     * @param filePath 压缩图
     * @param file 压缩的图片保存地址
     */
    public  static void pixeCompressBitmap(String filePath, File file){
        //采样率，数值越高，图片像素越低
        int inSampleSize=2;
        BitmapFactory.Options osts=new BitmapFactory.Options();
        osts.inSampleSize=inSampleSize;
        //inJustDecodeBounds设为True时，不会真正加载图片，而是得到图片的宽高信息。
        osts.inJustDecodeBounds=false;
        Bitmap bitmap= BitmapFactory.decodeFile(filePath,osts);
        ByteArrayOutputStream stream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        try {
            if (file.exists()){
                file.delete();
            }else{
                file.createNewFile();
            }
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write(stream.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**尺寸压缩
     * @param bitmap 要压缩的图片
     * @param ratio 压缩比例，值越大，图片的尺寸就越小
     * @param file 压缩的图片保存地址
     */
    public static void sizeCompressBitmap(Bitmap bitmap,int ratio,File file){
        if (ratio<=0){
            return;
        }
        Bitmap result=Bitmap.createBitmap(bitmap.getWidth()/ratio,bitmap.getHeight()/ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas =new Canvas();
        Rect rect=new Rect(0,0,bitmap.getWidth()/ratio,bitmap.getHeight()/ratio);
        canvas.drawBitmap(bitmap,null,rect,null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /** 质量压缩,
     * @param bitmap 要压缩的图片
     * @param file //压缩的图片保存地址
     *  Hint to the compressor, 0-100. 0 meaning compress for small size, 100 meaning compress for max quality. Some
     * formats, like PNG which is lossless, will ignore the quality setting
     * quality  (0-100)  100是不压缩，值越小，压缩得越厉害
     */
    public static void qualityCompressBitmap(Bitmap bitmap,File file){
        //字节数组输出流
        ByteArrayOutputStream stream =new ByteArrayOutputStream();
        int quality=20;
        //图片压缩后把数据放在stream中
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality, stream);
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            //不断把stream的数据写文件输出流中去
            fileOutputStream.write(stream.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
