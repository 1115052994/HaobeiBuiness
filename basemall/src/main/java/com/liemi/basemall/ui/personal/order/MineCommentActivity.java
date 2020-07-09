package com.liemi.basemall.ui.personal.order;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.comment.CommentItemEntity;
import com.liemi.basemall.data.entity.comment.MuchCommentEntity;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.data.event.OrderUpdateEvent;
import com.liemi.basemall.databinding.ActivityMineCommentBinding;
import com.liemi.basemall.databinding.ItemMineCommentBinding;
import com.liemi.basemall.databinding.ItemMultiPicBinding;
import com.liemi.basemall.widget.RatingBarView;
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
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.utils.oss.MultiPutListener;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.personal.order.OrderDetailActivity.ORDER_ENTITY;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;
import static com.netmi.baselibrary.data.Constant.ORDER_SUCCESS;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/10 10:47
 * 修改备注：
 */
public class MineCommentActivity extends BaseActivity<ActivityMineCommentBinding> {

    private BaseRViewAdapter<OrderDetailsEntity.MeOrdersBean, BaseViewHolder> adapter;

    private RecyclerView recyclerView;

    private MuchCommentEntity muchCommentEntity;

    private OrderDetailsEntity detailsEntity;

    private int editPosition;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_comment;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("评价");
        recyclerView = mBinding.rvGoods;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new BaseRViewAdapter<OrderDetailsEntity.MeOrdersBean, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_mine_comment;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    private int max = 4;

                    private boolean canAddImg = true;


                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);

                        RecyclerView rvPic = getBinding().rvPic;
                        rvPic.setNestedScrollingEnabled(false);
                        final CommentItemEntity itemEntity = muchCommentEntity.getList().get(position);
                        rvPic.setLayoutManager(new GridLayoutManager(context, 3));
                        /*PhotoAdapter adapter=new PhotoAdapter(context);
                        rvPic.setAdapter(adapter);*/

                        final BaseRViewAdapter<String, BaseViewHolder> adapter;
                        rvPic.setAdapter(adapter = new BaseRViewAdapter<String, BaseViewHolder>(context) {

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

                                    private int subPosition;

                                    @Override
                                    public void bindData(Object item) {
                                        subPosition = this.position;
                                        super.bindData(item);
                                        if (subPosition == getItemCount() - 1) {
                                            if (subPosition >= max) {
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
                                            getBinding().ivPic.setVisibility(View.VISIBLE);
                                            if (getItem(subPosition).startsWith("http")) {
                                                GlideShowImageUtils.displayNetImage(context, getItem(subPosition), getBinding().ivPic, R.drawable.baselib_bg_default_pic);
                                            } else {
                                                GlideShowImageUtils.displayNativeImage(context, getItem(subPosition), getBinding().ivPic, R.drawable.baselib_bg_default_pic);
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
                                        editPosition = muchCommentEntity.getList().indexOf(itemEntity);
                                        if (subPosition == getItemCount() - 1) {
                                            //图片选择
                                            if (canAddImg) {
                                                ImagePicker.getInstance().setMultiMode(true);
                                                ImagePicker.getInstance().setSelectLimit(max);
                                                Intent intent = new Intent(getContext(), ImageGridActivity.class);
                                                if (itemEntity.getImgs() != null
                                                        && !itemEntity.getImgs().isEmpty())
                                                    intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, ImageItemUtil.String2ImageItem(itemEntity.getImgs()));
                                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                                return;
                                            }
                                        }

                                        //打开预览
                                        Intent intentPreview = new Intent(getContext(), ImagePreviewDelActivity.class);
                                        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, ImageItemUtil.String2ImageItem(getItems()));
                                        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, subPosition);
                                        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                                        startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                                    }
                                };
                            }
                        });


                        getBinding().rbStarServer.setOnRatingListener(new RatingBarView.OnRatingListener() {
                            @Override
                            public void onRating(Object bindObject, int RatingScore) {
                                itemEntity.setLevel(RatingScore);
                            }
                        });

                        if (itemEntity.getImgs() != null) {
                            adapter.setData(itemEntity.getImgs());
                        }

                        getBinding().etContent.setText(muchCommentEntity.getList().get(position).getContent());
                        getBinding().etContent.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                muchCommentEntity.getList().get(position).setContent(editable.toString());
                            }
                        });
                    }

                    @Override
                    public ItemMineCommentBinding getBinding() {
                        return (ItemMineCommentBinding) super.getBinding();
                    }
                };
            }
        });

    }

    @Override
    protected void initData() {
        muchCommentEntity = new MuchCommentEntity();
        detailsEntity = (OrderDetailsEntity) getIntent().getSerializableExtra(ORDER_ENTITY);
        muchCommentEntity.setOrder_id(detailsEntity.getId());
        for (OrderDetailsEntity.MeOrdersBean bean : detailsEntity.getMeOrders()) {
            muchCommentEntity.getList().add(new CommentItemEntity(bean.getId()));
        }
        adapter.setData(detailsEntity.getMeOrders());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    muchCommentEntity.getList().get(editPosition).setImgs(ImageItemUtil.ImageItem2String(images));
                    adapter.notifyDataSetChanged();
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    muchCommentEntity.getList().get(editPosition).setImgs(ImageItemUtil.ImageItem2String(images));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }


    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_comment) {
            for (CommentItemEntity itemEntity : muchCommentEntity.getList()) {
                if (TextUtils.isEmpty(itemEntity.getContent())) {
                    ToastUtils.showShort("请先输入评论内容！");
                    return;
                }
            }
            uploadCommentImg(0);

        }
    }

    private void uploadCommentImg(final int position) {
        if(muchCommentEntity.getList().size() <= position) {
            doSubmitComment();
            return;
        }
        if (muchCommentEntity.getList().get(position).getImgs().isEmpty()) {
            uploadCommentImg(position + 1);
            return;
        }
        showProgress("");
        new OssUtils().initOss().multiPutObjectSync(muchCommentEntity.getList().get(position).getImgs(), 0, new MultiPutListener() {
            @Override
            public void onSuccess(List<String> uploads) {
                muchCommentEntity.getList().get(position).setImgs(uploads);
                if (muchCommentEntity.getList().size() == position + 1) {
                    doSubmitComment();
                } else {
                    uploadCommentImg(position + 1);
                }
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                ToastUtils.showShort("图片上传失败，请重试！");
                hideProgress();
            }
        });
    }

    private void doSubmitComment() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .submitComment(muchCommentEntity)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort("操作成功");
                        EventBus.getDefault().post(new OrderUpdateEvent(detailsEntity.getMpid(), ORDER_SUCCESS));
                        MApplication.getInstance().appManager.finishActivity(OrderDetailActivity.class);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

}
