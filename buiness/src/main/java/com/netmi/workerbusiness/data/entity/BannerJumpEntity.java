package com.netmi.workerbusiness.data.entity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_URL;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/4/2
 * 修改备注：
 */
public class BannerJumpEntity {
    public void toJump(Context context, BannerEntity entity) {
        switch (entity.getShow_type()) {
            case 2:
                //跳转外链
            case 3:
                //跳富文本
                if (!TextUtils.isEmpty(entity.getParam())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(WEBVIEW_TITLE, TextUtils.isEmpty(entity.getName()) ? "详情" : entity.getName());
                    bundle.putInt(WEBVIEW_TYPE, entity.getShow_type());
                    bundle.putString(WEBVIEW_CONTENT, entity.getShow_type() == WEBVIEW_TYPE_URL ? entity.getParam() : entity.getContent());
                    JumpUtil.overlay(context, BaseWebviewActivity.class, bundle);
                } else {
                    ToastUtils.showShort("未配置页面");
                }
                break;
            case 5:
                //跳商品
                if (!TextUtils.isEmpty(entity.getParam())) {
//                    GoodDetailPageActivity.start(context, entity.getParam(), null);
                } else {
                    ToastUtils.showShort("未配置商品");
                }
                break;
            case 6:
                //跳店铺
                if (!TextUtils.isEmpty(entity.getParam())) {
//                    StoreDetailActivity.start(context, entity.getParam());
                } else {
                    ToastUtils.showShort("未配置店铺");
                }
                break;
        }
    }


}
