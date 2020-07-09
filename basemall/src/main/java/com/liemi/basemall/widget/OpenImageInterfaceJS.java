package com.liemi.basemall.widget;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.netmi.baselibrary.utils.JumpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/4/2 15:41
 * 修改备注：
 */
public class OpenImageInterfaceJS {

    private Activity context;

    private List<String> showImgs = new ArrayList<>();

    public OpenImageInterfaceJS(Activity context) {
        this.context = context;
    }

    @JavascriptInterface
    public void readImageUrl(String url) {
        showImgs.add(url);
    }


    @JavascriptInterface
    public void openImage(String currentImg) {
        JumpUtil.overlayImagePreview(context, showImgs, showImgs.indexOf(currentImg));
    }

}
