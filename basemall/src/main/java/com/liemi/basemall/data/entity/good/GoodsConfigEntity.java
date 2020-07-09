package com.liemi.basemall.data.entity.good;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/9/21 15:10
 * 修改备注：
 */
public class GoodsConfigEntity extends BaseEntity implements Serializable {


    /**
     * img_url : http://baigeli-1257261482.cos.ap-shanghai.myqcloud.com/backend/frontend_15344929392351.jpg
     * name : 一手货源
     */

    private String img_url;
    private String name;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
