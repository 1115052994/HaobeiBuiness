package com.liemi.basemall.data.entity.shopcar;

import com.liemi.basemall.data.entity.StoreEntity;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.Strings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/21 21:23
 * 修改备注：
 */
public class ShopCartEntity extends BaseEntity implements Serializable {
    private StoreEntity shop;
    private List<GoodsDetailedEntity> list;
    private boolean checked;
    private String remark;
    //最终邮费
    private String postage;

    public ShopCartEntity(){
        list = new ArrayList<>();
    }

    public ShopCartEntity(ShopCartAdapterEntity entity){
        shop = entity.getShop();
        list = entity.getList();
        checked = entity.isChecked();
    }


    public String getShowPostage(){
        if(Strings.toDouble(postage) > 0) {
            return FloatUtils.formatMoney(postage);
        } else {
            return "包邮";
        }
    }

    public String getPostage() {
        return postage;
    }

    public StoreEntity getShop() {
        return shop;
    }

    public void setShop(StoreEntity shop) {
        this.shop = shop;
    }

    public List<GoodsDetailedEntity> getList() {
        return list;
    }

    public void setList(List<GoodsDetailedEntity> list) {
        this.list = list;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public void setPostage(String postage) {
        this.postage = postage;
    }
}
