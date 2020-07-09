package com.liemi.basemall.data.entity.good;

public class ShareImgEntity {

    /**
     * share_img :
     */

    private String share_img;


    /**
     * 邀请好友的海报
     */
    private String invite_url;
    private String invite_code;

    public String getShare_img() {
        return share_img;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }


    public String getInvite_url() {
        return invite_url;
    }

    public void setInvite_url(String invite_url) {
        this.invite_url = invite_url;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }
}
