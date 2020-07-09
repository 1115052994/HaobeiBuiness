package com.liemi.basemall.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.netmi.baselibrary.utils.ToastUtils;

import java.util.Hashtable;

/*
 * 二维码生成类
 * https://www.jianshu.com/p/b275e818de6a
 * */
public class QRCodeUtils {
    /**
     * @param content 要生成二维码的内容
     * @param width   图片宽度
     * @param height  图片高度
     * @return
     */
    @Nullable
    public static Bitmap createQRCodeBitmap(String content, int width, int height) {
        return createQRCodeBitmap(content,width,height,"UTF-8","H","2", Color.BLACK,Color.WHITE);
    }

    /**
     * @param content          要生成二维码的内容
     * @param width            图片宽度
     * @param height           图片高度
     * @param charset_set      字符集/字符转码格式，默认“ISO-8859-1”
     * @param error_correction 容错级别 默认“L”
     * @param margin           边距，默认4
     * @param color_black      黑色色块自定义颜色
     * @param color_white      白色色块自定义颜色
     * @return
     */
    public static Bitmap createQRCodeBitmap(String content, int width, int height, @Nullable String charset_set,
                                            @Nullable String error_correction, @Nullable String margin,
                                            @ColorInt int color_black, @ColorInt int color_white) {

        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort("二维码内容为空");
            return null;
        }
        if (width < 0 || height < 0) {
            ToastUtils.showShort("图片宽高必须大于0");
            return null;
        }
        try {
            Hashtable<EncodeHintType,String> hints = new Hashtable<>();
            if(!TextUtils.isEmpty(charset_set)){
                hints.put(EncodeHintType.CHARACTER_SET,charset_set);
            }
            if(!TextUtils.isEmpty(error_correction)){
                hints.put(EncodeHintType.ERROR_CORRECTION,error_correction);
            }
            if(!TextUtils.isEmpty(margin)){
                hints.put(EncodeHintType.MARGIN,margin);
            }
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE,width,height);
            int[] pixels = new int[width * height];
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    if(bitMatrix.get(x,y)){
                        pixels[y * width + x] = color_black;
                    }else{
                        pixels[y * width + x] = color_white;
                    }
                }
            }
            //创建bitmap对象
            Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels,0,width,0,0,width,height);
            return bitmap;
        }  catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
