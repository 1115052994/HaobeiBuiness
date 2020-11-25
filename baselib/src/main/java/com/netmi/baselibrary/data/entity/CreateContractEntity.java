package com.netmi.baselibrary.data.entity;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/24
 * 修改备注：
 */
public class CreateContractEntity extends BaseEntity {


    /**
     * shortUrl : https://t.bestsign.info/naxD4E7gzi6EZ5
     */

    private String shortUrl;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
