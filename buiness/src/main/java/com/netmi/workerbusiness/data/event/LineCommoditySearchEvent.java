package com.netmi.workerbusiness.data.event;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/3
 * 修改备注：
 */
public class LineCommoditySearchEvent {

    private String content;

    public LineCommoditySearchEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
