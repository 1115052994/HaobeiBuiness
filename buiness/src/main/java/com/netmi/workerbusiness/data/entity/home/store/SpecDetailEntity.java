package com.netmi.workerbusiness.data.entity.home.store;


import android.os.Parcel;
import android.os.Parcelable;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Sherlock
 * 创建时间：2019/4/11
 * 修改备注：
 */
public class SpecDetailEntity extends BaseEntity implements Parcelable {
    private String value_names;
    private String value_ids;
    private String price;
    private String stock;
    private String discount;

    public SpecDetailEntity(String value_names, String value_ids) {
        this.value_names = value_names;
        this.value_ids = value_ids;
    }

    public SpecDetailEntity(String value_names, String value_ids, String price, String stock, String discount) {
        this.value_names = value_names;
        this.value_ids = value_ids;
        this.price = price;
        this.stock = stock;
        this.discount = discount;
    }

    public String getValue_names() {
        return value_names;
    }

    public void setValue_names(String value_names) {
        this.value_names = value_names;
    }

    public String getValue_ids() {
        return value_ids;
    }

    public void setValue_ids(String value_ids) {
        this.value_ids = value_ids;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public SpecDetailEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.value_names);
        dest.writeString(this.value_ids);
        dest.writeString(this.price);
        dest.writeString(this.stock);
        dest.writeString(this.discount);
    }

    protected SpecDetailEntity(Parcel in) {
        this.value_names = in.readString();
        this.value_ids = in.readString();
        this.price = in.readString();
        this.stock = in.readString();
        this.discount = in.readString();
    }

    public static final Creator<SpecDetailEntity> CREATOR = new Creator<SpecDetailEntity>() {
        @Override
        public SpecDetailEntity createFromParcel(Parcel source) {
            return new SpecDetailEntity(source);
        }

        @Override
        public SpecDetailEntity[] newArray(int size) {
            return new SpecDetailEntity[size];
        }
    };
}
