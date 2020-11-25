package com.netease.nim.uikit.common.util.media;

import android.content.res.Resources;
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

    /**
     * 获取压缩后的图片
     *
     * @param res
     * @param resId
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        /**
         * 1.获取图片的像素宽高(不加载图片至内存中,所以不会占用资源)
         * 2.计算需要压缩的比例
         * 3.按将图片用计算出的比例压缩,并加载至内存中使用
         */
        // 首先不加载图片,仅获取图片尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
        options.inJustDecodeBounds = true;
        // 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
        BitmapFactory.decodeResource(res, resId, options);

        // 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
        options.inJustDecodeBounds = false;
        options.inScaled = false;
        // 利用计算的比例值获取压缩后的图片对象
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 计算压缩比例值
     *
     * @param options   解析图片的配置信息
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 保存图片原宽高值
        final int height = options.outHeight;
        final int width = options.outWidth;
        // 初始化压缩比例为1
        int inSampleSize = 1;

        // 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 压缩比例值每次循环两倍增加,
            // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    /** 采样率压缩
     * @param filePath 压缩图
     * @param file 压缩的图片保存地址
     */
    public  static void pixeCompressBitmap(String filePath, File file){
        //采样率，数值越高，图片像素越低
        int inSampleSize=8;
        BitmapFactory.Options osts=new BitmapFactory.Options();
        osts.inSampleSize=inSampleSize;
        //inJustDecodeBounds设为True时，不会真正加载图片，而是得到图片的宽高信息。
        osts.inJustDecodeBounds=false;
        Bitmap bitmap= BitmapFactory.decodeFile(filePath,osts);
        ByteArrayOutputStream stream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
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
