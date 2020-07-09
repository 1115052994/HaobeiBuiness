package com.liemi.basemall.ui.personal.setting;


import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.databinding.ActivitySuggestionFeedbackBinding;
import com.liemi.basemall.databinding.ModpersonalItemMultiPicBinding;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.utils.oss.MultiPutListener;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

public class SuggestionFeedbackActivity extends BaseActivity<ActivitySuggestionFeedbackBinding> {


    private BaseRViewAdapter<String, BaseViewHolder> adapter;
    private int max = 4;
    private boolean canAddImg = true;
    private ArrayList<ImageItem> images = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_suggestion_feedback;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("意见反馈");

        mBinding.rvPic.setLayoutManager(new GridLayoutManager(this, 4));
        mBinding.rvPic.setAdapter(adapter = new BaseRViewAdapter<String, BaseViewHolder>(this) {

            @Override
            public int layoutResId(int position) {
                return R.layout.modpersonal_item_multi_pic;
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
                        ModpersonalItemMultiPicBinding picBinding = (ModpersonalItemMultiPicBinding) getBinding();
                        if (position == getItemCount() - 1) {
                            if (position >= max) {
                                picBinding.ivPic.setVisibility(View.GONE);
                                canAddImg = false;
                            } else {
                                picBinding.ivPic.setVisibility(View.VISIBLE);
                                picBinding.ivPic.setImageResource(R.mipmap.ic_upload_file);
                                canAddImg = true;
                            }
                        } else {
                            getBinding().ivPic.setVisibility(View.VISIBLE);
                            if (getItem(position).startsWith("http")) {
                                GlideShowImageUtils.displayNetImage(context, getItem(position), getBinding().ivPic, R.drawable.baselib_bg_default_pic);
                            } else {
                                GlideShowImageUtils.displayNativeImage(context, getItem(position), getBinding().ivPic, R.drawable.baselib_bg_default_pic);
                            }
                        }
                    }

                    @Override
                    public ModpersonalItemMultiPicBinding getBinding() {
                        return (ModpersonalItemMultiPicBinding) super.getBinding();
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (position == adapter.getItemCount() - 1) {
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
                        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                        startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                    }
                };
            }
        });


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

    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.bt_submit) {
            final String content = mBinding.etContent.getText().toString();
            final String phone = mBinding.etPhone.getText().toString();
            if (content.isEmpty()) {
                ToastUtils.showShort("请先输入意见反馈");
                return;
            }

            if (phone.isEmpty() || !Strings.isPhone(phone)) {
                ToastUtils.showShort("请输入正确的手机号码");
                return;
            }


            if (adapter != null && !adapter.getItems().isEmpty()) {
                showProgress("");
                new OssUtils().initOss().multiPutObjectSync(adapter.getItems(), 0, new MultiPutListener() {
                    @Override
                    public void onSuccess(final List<String> uploads) {
                        Logs.i("图片上传成功："+uploads.size());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                                String[] pics = new String[uploads.size()];
                                for(int i = 0; i < pics.length; i ++){
                                    pics[i] = uploads.get(i);
                                }
     //                           doFeedBack(content, phone, pics);
                            }
                        });

                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        ToastUtils.showShort("图片上传失败，请重试！");
                        hideProgress();
                    }
                });
            } else {
                doFeedBack(content, phone, null);
            }

        }

    }


    private void doFeedBack(String remark, String tel, List<String> imgs) {
//        showProgress("");
//        RetrofitApiFactory.createApi(MineApi.class)
//                .doSuggestionBack(null, remark,tel, imgs)
//                .compose(RxSchedulers.<BaseData>compose())
//                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new XObserver<BaseData>() {
//                    @Override
//                    protected void onError(ApiException ex) {
//                        hideProgress();
//                        showError(ex.getMessage());
//                    }
//
//                    @Override
//                    public void onSuccess(BaseData data) {
//                        showError(data.getErrmsg());
//                        finish();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        hideProgress();
//                    }
//                });
//

    }

}
