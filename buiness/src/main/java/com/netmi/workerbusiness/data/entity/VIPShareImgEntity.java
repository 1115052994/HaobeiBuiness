package com.netmi.workerbusiness.data.entity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/4/13
 * 修改备注：
 */
public class VIPShareImgEntity {
    /**
     * 邀请好友的海报
     */
    private String img_url;
    private String invite_code;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }
}
