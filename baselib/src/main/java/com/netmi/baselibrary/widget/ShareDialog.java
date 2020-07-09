package com.netmi.baselibrary.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.netmi.baselibrary.R;
import com.netmi.baselibrary.data.entity.ShareEntity;
import com.netmi.baselibrary.utils.AppUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/14 18:37
 * 修改备注：
 */
public class ShareDialog extends Dialog implements PlatformActionListener {

    private ShareEntity entity;

    public ShareDialog(Context context, ShareEntity shareEntity) {
        super(context, R.style.showDialog);
        entity = shareEntity;
        if (entity == null) {
            entity = new ShareEntity();
            entity.setActivity((Activity) context);
        }
        initUI();
    }

    /**
     * 对话框布局初始化
     */
    private void initUI() {

        setContentView(R.layout.baselib_dialog_share_layout);

        findViewById(R.id.rl_wechat_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppUtils.hasFunction()) return;
                shareToPlatform(Wechat.NAME);
                dismiss();
            }
        });
        findViewById(R.id.rl_wechat_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppUtils.hasFunction()) return;

                shareToPlatform(WechatMoments.NAME);
                dismiss();
            }
        });

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 显示
     */
    public void showDialog() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.dialog_bottom_anim_style);
        dialogWindow.setGravity(Gravity.BOTTOM);
        show();
    }

    private void shareToPlatform(String platformName) {
        if (entity != null) {
            Platform.ShareParams shareParams = new Platform.ShareParams();
            shareParams.setShareType(Platform.SHARE_WEBPAGE);
            shareParams.setTitle(entity.getTitle());
            shareParams.setUrl(entity.getLinkUrl());
            shareParams.setText(entity.getContent());
            shareParams.setImageUrl(entity.getImgUrl());
            Platform platform = ShareSDK.getPlatform(platformName);
            platform.setPlatformActionListener(this);
            platform.share(shareParams);
        } else {
            Toast.makeText(getContext(), "请先初始化分享数据！", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        doResult(1, "感谢您的分享");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        doResult(-1, "分享失败了T.T");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        //doResult(0, "您取消了分享！");
    }

    private void doResult(int resultCode, final String msg) {
        if (entity != null) {
            entity.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
