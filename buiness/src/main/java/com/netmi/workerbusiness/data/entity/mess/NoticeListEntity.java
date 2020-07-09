package com.netmi.workerbusiness.data.entity.mess;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/3/19
 * 修改备注：
 */
public class NoticeListEntity {

    public NoticeListEntity(String name, String all_no_read) {
        this.name = name;
        this.all_no_read = all_no_read;
    }

    private String name;
    private String all_no_read;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAll_no_read() {
        return all_no_read;
    }

    public void setAll_no_read(String all_no_read) {
        this.all_no_read = all_no_read;
    }
}
