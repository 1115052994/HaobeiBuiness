package com.netmi.baselibrary.data.entity;

/**
 * 类描述：图片上传之后返回实体
 * 创建人：Simple
 * 创建时间：2017/7/19 14:05
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class UpFilesEntity {

    private String original;
    private String type;
    private int size;
    private String url;
    private String title;
    private String state;
    private String md5;
    private String sha1;
    private String originalname;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }
}
