package com.liemi.basemall.data.entity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/5/22
 * 修改备注：
 */
public class ImgCodeEntity {
    /**
     * url : http://netmiyms.s3.ap-northeast-1.amazonaws.com/2019022711093416940.png
     * code : uVBbl
     */

    private String url;
    private String sign;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
