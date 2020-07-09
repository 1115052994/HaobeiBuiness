package com.liemi.basemall.data.entity;

/**
 * 类描述：
 * 创建人：Sherlock
 * 创建时间：2019/2/25
 * 修改备注：
 */
public class ConfirmDialogEntity {
    private String titleText;
    private String contentText;
    private String icon;
    private int titleColor = 0;
    private int contentColor = 0;


    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public int getContentColor() {
        return contentColor;
    }

    public void setContentColor(int contentColor) {
        this.contentColor = contentColor;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public ConfirmDialogEntity(String titleText, String contentText) {
        this.titleText = titleText;
        this.contentText = contentText;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
