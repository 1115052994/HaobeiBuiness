package com.netmi.baselibrary.utils.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/7 9:32
 * 修改备注：
 */
public class GlideShowImageUtils {

    /**
     * 通过文件路径加载本地图片
     *
     * @param imagePath
     * @param mImageView
     */
    public static void displayNativeImage(Context context, String imagePath, ImageView mImageView, int defaultImg) {
        Glide.with(context)
                .load(new File(imagePath))
                .apply(RequestOptions.noAnimation()
                        .placeholder(defaultImg))
                .into(mImageView);
    }

    /**
     * 通过文件路径加载本地图片
     *
     * @param imagePath
     * @param mImageView
     */
    public static void displayNativeRadiusImage(Context context, String imagePath, ImageView mImageView, int radius, int defaultImg) {
        Glide.with(context)
                .load(new File(imagePath))
                .apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(radius))
                        .placeholder(defaultImg)
                        //noAnimation
                        .dontAnimate()
                        .autoClone())
                .into(mImageView);
    }


    /**
     * 通过文件路径加载本地图片
     *
     * @param imagePath
     * @param mImageView
     */
    public static void displayNativeImageWithoutDefault(Context context, String imagePath, ImageView mImageView) {
        Glide.with(context)
                .load(new File(imagePath))
                .apply(RequestOptions.noAnimation())
                .into(mImageView);
    }

    /**
     * 显示手机本地圆形图片
     *
     * @param context
     * @param imagePath
     * @param mImageView
     * @param defaultImg
     */
    public static void displayCircleNativeImage(Context context, String imagePath, ImageView mImageView, int defaultImg) {
        Glide.with(context)
                .load(new File(imagePath))
                .apply(RequestOptions
                        .circleCropTransform()
                        .placeholder(defaultImg))
                .transition(withCrossFade())
                .into(mImageView);
    }


    /**
     * 显示网络图片
     *
     * @param imageUrl
     * @param mImageView
     */
    public static void displayNetImage(Context context, String imageUrl, ImageView mImageView) {
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .apply(RequestOptions.noAnimation())
                .into(mImageView);
    }

    /**
     * 显示网络图片
     *
     * @param imageUrl
     * @param mImageView
     * @param defaultImg
     */
    public static void displayNetImage(Context context, String imageUrl, ImageView mImageView, int defaultImg) {
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .apply(RequestOptions.noAnimation()
                        .placeholder(defaultImg))
                .into(mImageView);
    }

    public static void gifload(Context context, String imageUrl, ImageView mImageView) {
        Glide.with(context)
                .load(imageUrl)
                .into(mImageView);
    }

    /**
     * 显示网络圆形图片
     *
     * @param context
     * @param imageUrl
     * @param mImageView
     * @param defaultImg
     */
    public static void displayCircleNetImage(Context context, String imageUrl, ImageView mImageView, int defaultImg) {

        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .apply(RequestOptions
                        .circleCropTransform()
                        .placeholder(defaultImg)
                        //noAnimation
                        .dontAnimate()
                        .autoClone())
                .into(mImageView);
    }

    /**
     * 显示网络圆形带边框图片
     *
     * @param context
     * @param imageUrl
     * @param mImageView
     * @param border
     * @param borderColor
     * @param defaultImg
     */
    public static void displayCircleBorderNetImage(Context context, String imageUrl, ImageView mImageView, int border, int borderColor, int defaultImg) {
        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(defaultImg)
                        .transform(new GlideCircleTransform(context, border, borderColor))
                        //noAnimation
                        .dontAnimate()
                        .autoClone())
                .into(mImageView);
    }


    /**
     * 显示网络图片 圆角
     *
     * @param imageUrl
     * @param mImageView
     * @param defaultImg 默认图片
     * @param Displayer  圆角大小
     */
    public static void displayNetImage(Context context, String imageUrl, ImageView mImageView, int defaultImg,
                                       int Displayer) {
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(Displayer))
                        .placeholder(defaultImg)
                        //noAnimation
                        .dontAnimate()
                        .autoClone())
                .into(mImageView);
    }

    /**
     * @param resourceId
     * @param mImageView
     */
    public static void displayResourceRadiusImage(Context context, int resourceId, ImageView mImageView, int radius, int defaultImg) {
        Glide.with(context)
                .load(resourceId)
                .apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(radius))
                        .placeholder(defaultImg)
                        //noAnimation
                        .dontAnimate()
                        .autoClone())
                .into(mImageView);
    }


}
