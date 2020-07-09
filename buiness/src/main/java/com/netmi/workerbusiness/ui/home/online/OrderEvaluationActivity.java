package com.netmi.workerbusiness.ui.home.online;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.OrderApi;
import com.netmi.workerbusiness.data.entity.home.OrderEvaluationEntity;
import com.netmi.workerbusiness.databinding.ActivityEvaluationBinding;
import com.netmi.workerbusiness.databinding.ItemOrderEvaluationBinding;
import com.netmi.workerbusiness.databinding.ItemPostImgsBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.netmi.baselibrary.ui.MultiPictureSelectFragment.TYPE_DEFAULT;

public class OrderEvaluationActivity extends BaseXRecyclerActivity<ActivityEvaluationBinding, OrderEvaluationEntity> {
    private String order_id;
    private List<String> imgUrl = new ArrayList<>();


    @Override
    protected int getContentView() {
        return R.layout.activity_evaluation;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("订单评价");
        order_id = getIntent().getExtras().getString(JumpUtil.ID);
        initRecyclerView();
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    private void initRecyclerView() {
        adapter = new BaseRViewAdapter<OrderEvaluationEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_order_evaluation;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<OrderEvaluationEntity>(binding) {

                    @Override
                    public void bindData(OrderEvaluationEntity item) {
                        super.bindData(item);//不能删
                        ItemOrderEvaluationBinding itemOrderEvaluationBinding = (ItemOrderEvaluationBinding) binding;
                        OrderEvaluationEntity entity = items.get(position);
                        if (entity.getLevel() == 0) {
                            itemOrderEvaluationBinding.iv1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOrderEvaluationBinding.iv2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOrderEvaluationBinding.iv3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOrderEvaluationBinding.iv4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOrderEvaluationBinding.iv5.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                        } else if (entity.getLevel() == 1) {
                            itemOrderEvaluationBinding.tvEvaluation.setText("很差");
                            itemOrderEvaluationBinding.iv2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOrderEvaluationBinding.iv3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOrderEvaluationBinding.iv4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOrderEvaluationBinding.iv5.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                        } else if (entity.getLevel() == 2) {
                            itemOrderEvaluationBinding.tvEvaluation.setText("较差");
                            itemOrderEvaluationBinding.iv3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOrderEvaluationBinding.iv4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOrderEvaluationBinding.iv5.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                        } else if (entity.getLevel() == 3) {
                            itemOrderEvaluationBinding.tvEvaluation.setText("一般");
                            itemOrderEvaluationBinding.iv4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOrderEvaluationBinding.iv5.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                        } else if (entity.getLevel() == 4) {
                            itemOrderEvaluationBinding.tvEvaluation.setText("很好");
                            itemOrderEvaluationBinding.iv5.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                        } else {
                            itemOrderEvaluationBinding.tvEvaluation.setText("非常好");
                        }
                        if (entity.getTo_commet() == null || entity.getTo_commet().getContent() == null) {
//                            itemOrderEvaluationBinding.etReplayContent.setFocusable(true);
//                            itemOrderEvaluationBinding.etReplayContent.setFocusableInTouchMode(true);
//                            itemOrderEvaluationBinding.etReplayContent.setCursorVisible(true);
//                            itemOrderEvaluationBinding.etReplayContent.requestFocus();
                        } else {
                            itemOrderEvaluationBinding.etReplayContent.setText(entity.getTo_commet().getContent());
//                            itemOrderEvaluationBinding.etReplayContent.setClickable(false);
                            itemOrderEvaluationBinding.tvConfirm.setVisibility(View.GONE);
                        }

                        if (entity.getVideo_url().isEmpty()) {  //是否有视频
                            itemOrderEvaluationBinding.videoplayer.setVisibility(View.GONE);

                            /**
                             * 图片列表
                             * */
                            RecyclerView recyclerViewImgs = itemOrderEvaluationBinding.rvImgs;
                            recyclerViewImgs.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            BaseRViewAdapter<String, BaseViewHolder> imgsAdapter = new BaseRViewAdapter<String, BaseViewHolder>(getContext()) {
                                @Override
                                public int layoutResId(int viewType) {
                                    return R.layout.item_post_imgs;
                                }

                                @Override
                                public BaseViewHolder holderInstance(ViewDataBinding binding) {
                                    return new BaseViewHolder(binding) {
                                        @Override
                                        public void bindData(Object item) {
                                            super.bindData(item);
                                            GlideShowImageUtils.displayNetImage(getContext(), getItem(position), ((ItemPostImgsBinding) getBinding()).ivPic, R.drawable.bg_radius_4dp_solid_white, 8);
                                        }

                                        @Override
                                        public void doClick(View view) {
                                            super.doClick(view);
                                            ItemPostImgsBinding itemPostImgsBinding = (ItemPostImgsBinding) binding;
//                                            itemPostImgsBinding.ivPic.performClick();
                                            //打开预览
                                            Intent intentPreview = new Intent(getContext(), ImagePreviewDelActivity.class);
                                            intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, ImageItemUtil.String2ImageItem(getItems()));
                                            intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                                            intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                                            intentPreview.putExtra(ImagePicker.EXTRA_PREVIEW_HIDE_DEL, true);
                                            startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                                        }
                                    };
                                }
                            };
                            recyclerViewImgs.setAdapter(imgsAdapter);
                            imgUrl.clear();
                            if (entity.getFlag().equals("1")) {
                                for (int i = 0; i < entity.getMeCommetImgs().size(); i++) {
                                    imgUrl.add(entity.getMeCommetImgs().get(i));
                                }
                            }
                            imgsAdapter.setData(imgUrl);
                        } else {
                            itemOrderEvaluationBinding.videoplayer.setVisibility(View.VISIBLE);
                            itemOrderEvaluationBinding.videoplayer.setUp(entity.getVideo_url(), "", Jzvd.SCREEN_WINDOW_NORMAL);
                            if (!TextUtils.isEmpty(entity.getImg_url())) {
                                GlideShowImageUtils.displayNetImage(getContext(), entity.getImg_url(), itemOrderEvaluationBinding.videoplayer.thumbImageView, R.drawable.baselib_bg_default_pic);
                            } else {
                                Glide.with(getContext())
                                        .load(entity.getVideo_url())
                                        .apply(RequestOptions.frameOf(10))
                                        .into(itemOrderEvaluationBinding.videoplayer.thumbImageView);
                            }
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        ItemOrderEvaluationBinding itemOrderEvaluationBinding = (ItemOrderEvaluationBinding) binding;
                        OrderEvaluationEntity entity = items.get(position);
                        super.doClick(view);
                        if (view.getId() == R.id.tv_confirm) {
                            if (itemOrderEvaluationBinding.etReplayContent.getText().toString().isEmpty()) {
                                showError("请输入回复");
                            } else {
                                comment(entity.getCommet_id(), itemOrderEvaluationBinding.etReplayContent.getText().toString());
                            }
                            //                            case R.id.videoplayer:
////                                JumpUtil.overlay(getContext(), VideoPlayer2Activity.class, VideoPlayerActivity.VIDEO_URL, entity.getVideo_url());
//                                break;
                        }
                    }
                };
            }
        };
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);
    }


    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .order_comment_list(order_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<OrderEvaluationEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<List<OrderEvaluationEntity>> data) {
                        adapter.setData(data.getData());
                    }
                });
    }

    private void comment(String commet_id, String content) {  //回复评价
        RetrofitApiFactory.createApi(OrderApi.class)
                .comment(commet_id, content)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("已成功回复");
                        finish();
                    }
                });
    }

}
