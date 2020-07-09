package com.liemi.basemall.ui.personal.refund;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.databinding.ActivityApplyRefundBinding;

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
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.utils.oss.MultiPutListener;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.personal.order.OrderDetailActivity.ORDER_ENTITY;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

public class ApplyRefundActivity extends BaseActivity<ActivityApplyRefundBinding> {

    public final static String REFUND_POSITION = "refund_position";

    public final static int REQUEST_CODE = 1;
    public final static int RESULT_CODE = -1;
    private OrderDetailsEntity orderDetailsEntity;
    BaseRViewAdapter<String, BaseViewHolder> adapter;
    private boolean canAddImg = true;
    private int max = 6;
    private ArrayList<ImageItem> images;
    private int position;

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_refund;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("换货申请");
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
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        if (position == getItemCount() - 1) {
                            if (position >= max) {
                                ((ModpersonalItemMultiPicBinding) binding).ivPic.setVisibility(View.GONE);
                                canAddImg = false;
                            } else {
                                getBinding().ivPic.setVisibility(View.VISIBLE);
                                getBinding().ivPic.setImageResource(R.mipmap.icon_feed_back_upload);
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
        position = getIntent().getIntExtra(REFUND_POSITION, 0);
        orderDetailsEntity = (OrderDetailsEntity) getIntent().getSerializableExtra(ORDER_ENTITY);
        if (orderDetailsEntity == null) {
            ToastUtils.showShort("没有订单数据");
            finish();
            return;
        }
        if (orderDetailsEntity.getStatus() == 5) {
            getRightSetting().setText("取消申请");
            mBinding.llRefundStatus.setVisibility(View.VISIBLE);
        } else if (orderDetailsEntity.getStatus() == 6) {
            getRightSetting().setText("重新申请");
            mBinding.llRefundStatus.setVisibility(View.VISIBLE);
        } else if (orderDetailsEntity.getStatus() == 7) {
            mBinding.llRefundStatus.setVisibility(View.VISIBLE);
        }
        mBinding.setItem(orderDetailsEntity.getMeOrders().get(position));
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_confirm) {
            String content = mBinding.etContent.getText().toString();
            if (TextUtils.isEmpty(mBinding.etPhone.getText().toString())) {
                ToastUtils.showShort("请输入联系方式");
                return;
            }
            if (TextUtils.isEmpty(content)) {
                ToastUtils.showShort("请先描述问题");
                return;
            }
            if (!adapter.getItems().isEmpty()) {
                showProgress("");
                new OssUtils().initOss().multiPutObjectSync(adapter.getItems(), 0, new MultiPutListener() {
                    @Override
                    public void onSuccess(List<String> uploads) {
                        doGetChange(orderDetailsEntity.getMeOrders().get(position).getId(), mBinding.etContent.getText().toString(), mBinding.etPhone.getText().toString(), uploads);
                    }

                    @Override
                    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                        ToastUtils.showShort("图片上传失败，请重试！");
                        hideProgress();
                    }
                });
            } else {
                doGetChange(orderDetailsEntity.getMeOrders().get(position).getOrder_id(), mBinding.etContent.getText().toString(), mBinding.etPhone.getText().toString(), null);
            }
        }

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

    public void doGetChange(String id, String remark, String phone, List<String> img_url) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getChange(id, remark, phone, img_url)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort("申请已提交！");
//                            Intent intent = new Intent();
//                            intent.putExtra(POSITION,position = getIntent().getIntExtra(REPLACE_POSITION, 0));
//                            intent.putExtra(REPLACE_STATUS,"1");
//                            setResult(RESULT_CODE,intent);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }
}
