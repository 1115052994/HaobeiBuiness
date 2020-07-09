package com.netmi.baselibrary.data.entity;

import android.app.Activity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/7/25 21:20
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ShareEntity {

    /**
     * 分享的界面
     */
    private Activity activity;
    /**
     * 分享图片的url
     */
    private String imgUrl = "https://liemimofang.oss-cn-hangzhou.aliyuncs.com/2018/03/08/19/35/27/5f59c229f3bc4ee5a87b36f0672fad2d.jpg";
    /**
     * 标题
     */
    private String title = "嗨布网";
    /**
     * 内容
     */
    private String content = "为你推荐嗨布网APP，马上去下载";
    /**
     * 链接
     */
    private String linkUrl;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

}
