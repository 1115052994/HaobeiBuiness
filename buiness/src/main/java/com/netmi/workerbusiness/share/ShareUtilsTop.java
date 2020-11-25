package com.netmi.workerbusiness.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.netmi.baselibrary.ui.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShareUtilsTop {
    Bitmap bitmap;
    //微信好友纯图片分享
    public void weiXin(BaseActivity view, String url, String title, String text, String imageurl, int id)  {
        getBitamp(imageurl,view);
    }

    public void weixinCircle(BaseActivity view, String url, String title, String text, String imageurl, int id) {
        getBitamp2(imageurl,view,title,text);
    }
    public void getBitamp(String imaurl,BaseActivity view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(imaurl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                UMImage web = new UMImage(view, bitmap);//这是分享好友时的缩略图
                UMImage thumb =  new UMImage(view, bitmap);//这里是你需要显示的图片
                web.setThumb(thumb);
                web.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
                new ShareAction(view)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(web)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {
                                view.hideProgress();
                            }

                            @Override
                            public void onResult(SHARE_MEDIA share_media) { }
                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) { }
                            @Override
                            public void onCancel(SHARE_MEDIA share_media) { }
                        })
                        .share();
            }
        }).start();
    }

    public void getBitamp2(String imaurl,BaseActivity view,String title,String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(imaurl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                UMImage web =new UMImage(view,bitmap);
                web.setTitle(title);//标题
                web.setDescription(text);//描述
                web.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
            new ShareAction(view)
                    .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                    .withMedia(web)
                    .setCallback(new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
                            view.hideProgress();
                        }

                        @Override
                        public void onResult(SHARE_MEDIA share_media) { }
                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) { }
                        @Override
                        public void onCancel(SHARE_MEDIA share_media) { }
                    })
                    .share();
            }
        }).start();
    }
}
