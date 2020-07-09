package com.netmi.workerbusiness.ui.home.offline;

import android.databinding.ViewDataBinding;
import android.inputmethodservice.Keyboard;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liemi.basemall.databinding.ActivityXrecyclerviewBinding;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.OfflineGoodApi;
import com.netmi.workerbusiness.data.api.OrderApi;
import com.netmi.workerbusiness.data.entity.home.OrderEvaluationEntity;
import com.netmi.workerbusiness.data.entity.home.offlineorder.OfflineOrderEvaluationEntity;
import com.netmi.workerbusiness.databinding.ActivityEvaluationBinding;
import com.netmi.workerbusiness.databinding.ItemOfflineEvaluationBinding;
import com.netmi.workerbusiness.databinding.ItemOrderEvaluationBinding;
import com.netmi.workerbusiness.databinding.ItemPostImgsBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

public class OfflineEvaluationActivity extends BaseXRecyclerActivity<ActivityEvaluationBinding, OfflineOrderEvaluationEntity> {
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
        adapter = new BaseRViewAdapter<OfflineOrderEvaluationEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_offline_evaluation;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<OfflineOrderEvaluationEntity>(binding) {

                    @Override
                    public void bindData(OfflineOrderEvaluationEntity item) {
                        super.bindData(item);//不能删
                        ItemOfflineEvaluationBinding itemOfflineEvaluationBinding = (ItemOfflineEvaluationBinding) binding;
                        OfflineOrderEvaluationEntity entity = items.get(position);
                        if (entity.getLevel() == 0) {
                            itemOfflineEvaluationBinding.iv1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOfflineEvaluationBinding.iv2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOfflineEvaluationBinding.iv3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOfflineEvaluationBinding.iv4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOfflineEvaluationBinding.iv5.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                        } else if (entity.getLevel() == 1) {
                            itemOfflineEvaluationBinding.tvEvaluation.setText("很差");
                            itemOfflineEvaluationBinding.iv2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOfflineEvaluationBinding.iv3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOfflineEvaluationBinding.iv4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOfflineEvaluationBinding.iv5.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                        } else if (entity.getLevel() == 2) {
                            itemOfflineEvaluationBinding.tvEvaluation.setText("较差");
                            itemOfflineEvaluationBinding.iv3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOfflineEvaluationBinding.iv4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOfflineEvaluationBinding.iv5.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                        } else if (entity.getLevel() == 3) {
                            itemOfflineEvaluationBinding.tvEvaluation.setText("一般");
                            itemOfflineEvaluationBinding.iv4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                            itemOfflineEvaluationBinding.iv5.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                        } else if (entity.getLevel() == 4) {
                            itemOfflineEvaluationBinding.tvEvaluation.setText("很好");
                            itemOfflineEvaluationBinding.iv5.setImageDrawable(getResources().getDrawable(R.mipmap.ic_red_star_half));
                        } else {
                            itemOfflineEvaluationBinding.tvEvaluation.setText("非常好");
                        }
                        if (entity.getTo_commet() != null && !entity.getTo_commet().getContent().equals("")) {
                            itemOfflineEvaluationBinding.etReplayContent.setText(entity.getTo_commet().getContent());
//                            itemOfflineEvaluationBinding.etReplayContent.setClickable(false);
                            itemOfflineEvaluationBinding.tvConfirm.setVisibility(View.GONE);
                        } else {
//                            itemOfflineEvaluationBinding.etReplayContent.setFocusable(true);
//                            itemOfflineEvaluationBinding.etReplayContent.setFocusableInTouchMode(true);
//                            itemOfflineEvaluationBinding.etReplayContent.setCursorVisible(true);
//                            itemOfflineEvaluationBinding.etReplayContent.requestFocus();
                        }

                        if (entity.getVideo_url().isEmpty()) {  //是否有视频
                            itemOfflineEvaluationBinding.videoplayer.setVisibility(View.GONE);
                            /**
                             * 图片列表
                             * */
                            RecyclerView recyclerViewImgs = itemOfflineEvaluationBinding.rvImgs;
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
                                        }
                                    };
                                }
                            };
                            recyclerViewImgs.setAdapter(imgsAdapter);
                            imgUrl.clear();
                            imgUrl.addAll(entity.getMeCommetImgs());
                            imgsAdapter.setData(imgUrl);

                        } else if (!entity.getVideo_url().isEmpty()) {
                            itemOfflineEvaluationBinding.videoplayer.setVisibility(View.VISIBLE);
                            itemOfflineEvaluationBinding.videoplayer.setUp(entity.getVideo_url(), "", Jzvd.SCREEN_WINDOW_NORMAL);
                            if (!TextUtils.isEmpty(entity.getItem_url())) {
                                GlideShowImageUtils.displayNetImage(getContext(), entity.getItem_url(), itemOfflineEvaluationBinding.videoplayer.thumbImageView, R.drawable.baselib_bg_default_pic);
                            } else {
                                Glide.with(getContext())
                                        .load(entity.getVideo_url())
                                        .apply(RequestOptions.frameOf(10))
                                        .into(itemOfflineEvaluationBinding.videoplayer.thumbImageView);
                            }
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        ItemOfflineEvaluationBinding itemOfflineEvaluationBinding = (ItemOfflineEvaluationBinding) binding;
                        OfflineOrderEvaluationEntity entity = items.get(position);
                        super.doClick(view);
                        if (view.getId() == R.id.tv_confirm) {
                            if (itemOfflineEvaluationBinding.etReplayContent.getText().toString().isEmpty()) {
                                showError("请输入回复");
                            } else {
                                comment(entity.getCommet_id(), itemOfflineEvaluationBinding.etReplayContent.getText().toString());
                            }
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
        RetrofitApiFactory.createApi(OfflineGoodApi.class)
                .getEvaluation(order_id, "0", "10", null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<OfflineOrderEvaluationEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<OfflineOrderEvaluationEntity>> data) {
                        if (data.getData().getTotal_pages() == 0) {
                            finish();
                            showError("暂无评价");
                        } else {
                            showData(data.getData());
                        }
                    }
                });
    }

    private void comment(String commet_id, String content) {  //回复评价
        RetrofitApiFactory.createApi(OfflineGoodApi.class)
                .comment(content, commet_id)
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
