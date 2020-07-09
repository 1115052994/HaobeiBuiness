package com.netmi.workerbusiness.data.entity.home.store;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.netmi.baselibrary.utils.Logs;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Sherlock
 * 创建时间：2019/4/11
 * 修改备注：
 */
public class StoreCateEntity extends BaseObservable implements Parcelable {

    /**
     * "prop_id": "1000139",        //一级规格ID
     * "shop_id": "213",        //商家ID
     * "prop_name": "型号",        //一级规格名称
     * "prop_code": null,        //一级规格编号
     * "remark": null,        //预留字段
     * "sort": "14",        //排序
     * "is_show": "1",        //是否有显示 0：否 1是
     * "mePropValues": [        //二级规格列表
     * {
     * "value_id": "2253",        //二级规格ID
     * "prop_id": "1000139",        //一级规格ID
     * "value_name": "苹果X手机壳（唯美红花）",        //二级规格名称
     * "img_url": null,        //二级规格图片
     * "is_img": "0",        //是否按图片展示 0：否 1是
     * "value_code": null        //二级规格编号
     * }
     */

    private String prop_id;
    private String shop_id;
    private String prop_name;
    private String prop_code;
    private String remark;
    private String sort;
    private String is_show;
    private boolean red = false;
    private List<MePropValues> mePropValues = new ArrayList<>();
    /**
     * 是否是当前选中一级目录
     */
    private boolean isCheck = false;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
        notifyChange();
    }

    /**
     * 选中子节点数目
     */
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void pushCount() {
        count++;
        Logs.e(count + " ");
        notifyChange();
    }

    public void popCount() {
        count--;
        Logs.e(count + " ");

        notifyChange();
    }

    public String getProp_id() {
        return prop_id;
    }

    public void setProp_id(String prop_id) {
        this.prop_id = prop_id;
    }

    public String getProp_name() {
        return prop_name;
    }

    public void setProp_name(String prop_name) {
        this.prop_name = prop_name;
    }

    public List<MePropValues> getMePropValues() {
        return mePropValues;
    }

    public void setMePropValues(List<MePropValues> mePropValues) {
        this.mePropValues = mePropValues;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getProp_code() {
        return prop_code;
    }

    public void setProp_code(String prop_code) {
        this.prop_code = prop_code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }

    public boolean isRed() {
        return red;
    }

    public void setRed(boolean red) {
        this.red = red;
    }

    public static class MePropValues extends BaseObservable implements Parcelable {

        /**
         * "value_id": "2253",        //二级规格ID
         * "prop_id": "1000139",        //一级规格ID
         * "value_name": "苹果X手机壳（唯美红花）",        //二级规格名称
         * "img_url": null,        //二级规格图片
         * "is_img": "0",        //是否按图片展示 0：否 1是
         * "value_code": null        //二级规格编号
         */

        private String value_id;
        private String prop_id;
        private String value_name;
        private String img_url;
        private String is_img;
        private String value_code;
        private StoreCateEntity parent;

        public StoreCateEntity getParent() {
            return parent;
        }

        public void setParent(StoreCateEntity parent) {
            this.parent = parent;
        }

        /**
         * 是否是当前选中2级目录
         */
        private boolean isCheck = false;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck() {
//            this.parent = parent;
            isCheck = !isCheck;

//            if (isCheck)//选中
//                parent.pushCount();//+1
//            else//没选中
//                parent.popCount();//-1
//            Logs.e(parent.hashCode() + " " + parent.getCount());

            notifyChange();
        }

        public void setTrue() {
            isCheck=true;
            notifyChange();
        }

        public String getValue_id() {
            return value_id;
        }

        public void setValue_id(String value_id) {
            this.value_id = value_id;
        }

        public String getProp_id() {
            return prop_id;
        }

        public void setProp_id(String prop_id) {
            this.prop_id = prop_id;
        }

        public String getValue_name() {
            return value_name;
        }

        public void setValue_name(String value_name) {
            this.value_name = value_name;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getIs_img() {
            return is_img;
        }

        public void setIs_img(String is_img) {
            this.is_img = is_img;
        }

        public String getValue_code() {
            return value_code;
        }

        public void setValue_code(String value_code) {
            this.value_code = value_code;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.value_id);
            dest.writeString(this.prop_id);
            dest.writeString(this.value_name);
            dest.writeString(this.img_url);
            dest.writeString(this.is_img);
            dest.writeString(this.value_code);
            dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
        }

        public MePropValues() {
        }

        protected MePropValues(Parcel in) {
            this.value_id = in.readString();
            this.prop_id = in.readString();
            this.value_name = in.readString();
            this.img_url = in.readString();
            this.is_img = in.readString();
            this.value_code = in.readString();
            this.isCheck = in.readByte() != 0;
        }

        public static final Creator<MePropValues> CREATOR = new Creator<MePropValues>() {
            @Override
            public MePropValues createFromParcel(Parcel source) {
                return new MePropValues(source);
            }

            @Override
            public MePropValues[] newArray(int size) {
                return new MePropValues[size];
            }
        };


    }

    public StoreCateEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.prop_id);
        dest.writeString(this.prop_name);
        dest.writeString(this.shop_id);
        dest.writeString(this.prop_code);
        dest.writeString(this.remark);
        dest.writeString(this.sort);
        dest.writeString(this.is_show);
        dest.writeTypedList(this.mePropValues);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
        dest.writeInt(this.count);
    }

    protected StoreCateEntity(Parcel in) {
        this.prop_id = in.readString();
        this.prop_name = in.readString();
        this.shop_id = in.readString();
        this.prop_code = in.readString();
        this.remark = in.readString();
        this.sort = in.readString();
        this.is_show = in.readString();
        this.mePropValues = in.createTypedArrayList(MePropValues.CREATOR);
        this.isCheck = in.readByte() != 0;
        this.count = in.readInt();
    }

    public static final Creator<StoreCateEntity> CREATOR = new Creator<StoreCateEntity>() {
        @Override
        public StoreCateEntity createFromParcel(Parcel source) {
            return new StoreCateEntity(source);
        }

        @Override
        public StoreCateEntity[] newArray(int size) {
            return new StoreCateEntity[size];
        }
    };
}
