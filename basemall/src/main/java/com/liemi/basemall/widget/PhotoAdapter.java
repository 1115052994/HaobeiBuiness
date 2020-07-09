package com.liemi.basemall.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ItemMultiPicBinding;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;

import java.util.ArrayList;
import java.util.List;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

/**
 * Created by Bingo on 2018/6/18.
 */

public class PhotoAdapter extends BaseRViewAdapter<String, BaseViewHolder> {
    private int max = 3;

    private boolean canAddImg = true; //是否支持添加图片

    private ArrayList<ImageItem> images;

    private boolean isAddImg = true;

    public void setAddImg(boolean addImg) {
        isAddImg = addImg;
    }

    public ArrayList<ImageItem> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageItem> images) {
        this.images = images;
    }

    @Override
    public void setData(List<String> items) {
        super.setData(items);
        this.images = ImageItemUtil.String2ImageItem(getItems());
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isCanAddImg() {
        return canAddImg;
    }

    public void setCanAddImg(boolean canAddImg) {
        this.canAddImg = canAddImg;
    }

    public PhotoAdapter(Context context) {
        super(context);
    }

    @Override
    public int layoutResId(int position) {
        return R.layout.item_multi_pic;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.position = position;
        holder.bindData(null);
    }

    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {
        return new BaseViewHolder(binding) {
            @Override
            public void bindData(Object item) {
                super.bindData(item);
                if (position == getItemCount() - 1) {
                    if (isAddImg) { //是否支持添加图片
                        if (position >= max) {
                            getBinding().llUpload.setVisibility(View.GONE);
                            getBinding().ivPic.setVisibility(View.GONE);
                            canAddImg = false;
                        } else {
                            getBinding().llUpload.setVisibility(View.VISIBLE);
                            getBinding().ivPic.setVisibility(View.GONE);
                            canAddImg = true;
                        }
                    } else {
                        getBinding().llUpload.setVisibility(View.GONE);
                        getBinding().ivPic.setVisibility(View.GONE);
                        canAddImg = false;
                    }
                } else {
                    getBinding().llUpload.setVisibility(View.GONE);
                    getBinding().ivPic.setVisibility(View.VISIBLE);
                    if (getItem(position).startsWith("http")) {
                        GlideShowImageUtils.displayNetImage(context, getItem(position), getBinding().ivPic, R.drawable.baselib_bg_default_pic);
                    } else {
                        GlideShowImageUtils.displayNativeImage(context, getItem(position), getBinding().ivPic, R.drawable.baselib_bg_default_pic);
                    }
                }
            }

            @Override
            public ItemMultiPicBinding getBinding() {
                return (ItemMultiPicBinding) super.getBinding();
            }

            @Override
            public void doClick(View view) {
                super.doClick(view);
                if (position == getItemCount() - 1) {
                    //图片选择
                    if (canAddImg) {
                        ImagePicker.getInstance().setMultiMode(true);
                        ImagePicker.getInstance().setSelectLimit(max);
                        Intent intent = new Intent(context, ImageGridActivity.class);
                        if (images != null)
                            intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, ImageItemUtil.String2ImageItem(getItems()));
                        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_SELECT);
                        return;
                    }
                }

                //打开预览
                Intent intentPreview = new Intent(context, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, images);
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                ((Activity) context).startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
            }
        };
    }
}
