package com.netmi.baselibrary.ui;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.R;
import com.netmi.baselibrary.databinding.BaselibFragmentListBinding;
import com.netmi.baselibrary.databinding.ItemMultiPictureBinding;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.utils.oss.OssUtils;

import java.util.ArrayList;
import java.util.List;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

/**
 * 类描述：多图上传
 * 创建人：Sherlock
 * 创建时间：2019/4/22
 * 修改备注：
 */
public class MultiPictureSelectFragment extends BaseXRecyclerFragment<BaselibFragmentListBinding, String> {
    /**
     * 编辑模式
     */
    public static final int TYPE_DEFAULT = 1111;
    /**
     * 浏览模式，图片展示不能修改
     */
    public static final int TYPE_BROWSER = 1112;
    private int max;
    private int lineCount;
    private int type;

    private final static String MAX = "max";
    private final static String LINE_COUNT = "spanCount";
    private final static String TYPE = "type";
    private boolean canAddImg = true;

    private ArrayList<ImageItem> images = null;

    //成功上传到服务器的图片路径
    private ArrayList<String> imgUrls = new ArrayList<>();
    private ArrayList<String> orignalUrls;

    /**
     * @param max   最大图片上传数量
     * @param count 每行显示最大图片数量
     */
    public static MultiPictureSelectFragment newInstance(int max, int count) {

        return newInstance(max, count, TYPE_DEFAULT);
    }

    /**
     * @param max   最大图片上传数量
     * @param count 每行显示最大图片数量
     */
    public static MultiPictureSelectFragment newInstance(int max, int count, int type) {
        Bundle args = new Bundle();
        args.putInt(MAX, max <= 0 ? 3 : max);
        args.putInt(LINE_COUNT, count <= 0 ? 3 : count);
        args.putInt(TYPE, type);
        MultiPictureSelectFragment fragment = new MultiPictureSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void doListData() {

    }

    @Override
    protected int getContentView() {
        return R.layout.baselib_fragment_list;
    }

    @Override
    protected void initUI() {
        max = getArguments().getInt(MAX);
        lineCount = getArguments().getInt(LINE_COUNT);
        type = getArguments().getInt(TYPE);
        initPicRecyclerView();
    }

    private void initPicRecyclerView() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), lineCount));
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<String, BaseViewHolder>(getContext()) {
            @Override
            public int getItemCount() {
                if (type == TYPE_BROWSER) {
                    return super.getItemCount();
                } else {
                    return super.getItemCount() + 1;
                }
            }

            @Override
            public int layoutResId(int position) {
                return R.layout.item_multi_picture;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<String>(binding) {
                    @Override
                    public void bindData(String item) {
                        super.bindData(item);
                        if (type == TYPE_DEFAULT && position == getItemCount() - 1) {
                            if (position >= max) {
                                getBinding().ivPic.setVisibility(View.GONE);
                                canAddImg = false;
                            } else {
                                getBinding().ivPic.setVisibility(View.VISIBLE);
                                getBinding().ivPic.setImageResource(R.mipmap.ic_img_multipic_default);
                                canAddImg = true;
                            }
                            getBinding().tvString.setVisibility(View.VISIBLE);
                        } else {
                            getBinding().tvString.setVisibility(View.GONE);
                            getBinding().ivPic.setVisibility(View.VISIBLE);
                            if (getItem(position).startsWith("http")) {
                                GlideShowImageUtils.displayNetImage(context, getItem(position), getBinding().ivPic, R.drawable.baselib_bg_default_pic);
                            } else {
                                GlideShowImageUtils.displayNativeImage(context, getItem(position), getBinding().ivPic, R.drawable.baselib_bg_default_pic);
                            }
                        }
                    }

                    @Override
                    public ItemMultiPictureBinding getBinding() {
                        Log.e("weng", super.getBinding().toString());
                        return (ItemMultiPictureBinding) super.getBinding();
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);

                        if (view == getBinding().ivPic) {
                            if (type == TYPE_DEFAULT && position == adapter.getItemCount() - 1) {
                                //图片选择
                                if (canAddImg) {
                                    ImagePicker.getInstance().setMultiMode(true);
                                    ImagePicker.getInstance().setSelectLimit(max);
                                    Intent intent = new Intent(getContext(), ImageGridActivity.class);
                                    if (images != null)
                                        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
                                    startActivityForResult(intent, REQUEST_CODE_SELECT);
                                    return;
                                }
                            }

                            //打开预览
                            Intent intentPreview = new Intent(getContext(), ImagePreviewDelActivity.class);
                            intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, ImageItemUtil.String2ImageItem(adapter.getItems()));
                            intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                            intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, type == TYPE_DEFAULT);//是否能删除
                            startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                        }
                    }

                };
            }
        });
    }

    /**
     * 设置浏览图片,同时设置为预览模式
     */
    public void imagePreview(List<String> urls) {
        type = TYPE_BROWSER;
        Logs.e(urls.toString());
        adapter.setData(urls);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    adapter.setData(ImageItemUtil.ImageItem2String(images));
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    adapter.setData(ImageItemUtil.ImageItem2String(images));
                }
            }
        }
    }

    /**
     * 图片存在OSS
     * 上传图片为空时（未选择图片），返回false
     */
    public boolean uploadOSS(OnUploadSuccess success, OnUploadFailed failed, boolean canBeEmpty) {
        if (images == null || images.isEmpty()) {
            if (!canBeEmpty)
                showError(R.string.please_select_update_img);
            return false;
        }
        showProgress("");
        imgUrls.clear();

        for (int i = 0; i < images.size(); i++) {

            if (orignalUrls != null && orignalUrls.contains(images.get(i).path)) {
                imgUrls.add(images.get(i).path);
                Logs.e(images.get(i).path);
                if (imgUrls.size() == images.size()) {
                    showError(R.string.upload_success);
                    if (success != null) {
                        success.onUploadSuccess(imgUrls);
                    }
                }
            } else {
                new OssUtils().initOss().putObjectSync(images.get(i).path, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        Logs.e(OssUtils.OSS_HOST + request.getObjectKey());
//                doUpdateUserInfo(OssUtils.OSS_HOST + request.getObjectKey(), null, null, null, null);
                        imgUrls.add(OssUtils.OSS_HOST + request.getObjectKey());
                        if (imgUrls.size() == images.size()) {
                            showError(R.string.upload_success);
                            if (success != null) {
                                success.onUploadSuccess(imgUrls);
                            }
                        }
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                        showError(R.string.upload_failed);
                        if (failed != null) {
                            failed.onUploadFailed();
                        }
                        return;
                    }
                });
            }
        }
        return true;
    }

    /**
     * 图片存在OSS
     * 上传图片为空时（未选择图片），返回false
     */
    public boolean uploadOSS(OnUploadSuccess success, OnUploadFailed failed) {
        return uploadOSS(success, failed, true);
    }

    /**
     * 图片存在后台
     */
    public void upload(OnUploadSuccess success, OnUploadFailed failed) {

    }

    /**
     * 图片填充
     */
    public void fillUrlImgs(List<String> imgs) {
        adapter.setData(imgs);
        orignalUrls = new ArrayList<>();
        orignalUrls.addAll(imgs);
        images = ImageItemUtil.String2ImageItem(imgs);
    }

    public interface OnUploadSuccess {
        void onUploadSuccess(List<String> urls);
    }

    public interface OnUploadFailed {
        void onUploadFailed();
    }

}
