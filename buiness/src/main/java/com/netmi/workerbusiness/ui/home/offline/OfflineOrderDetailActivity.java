package com.netmi.workerbusiness.ui.home.offline;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.liemi.basemall.ui.NormalDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.OfflineGoodApi;
import com.netmi.workerbusiness.data.entity.home.offlineorder.OfflineOrderDetailEntity;
import com.netmi.workerbusiness.databinding.ActivityOfflineOrderDetailBinding;
import com.netmi.workerbusiness.databinding.ItemPostImgsBinding;
import com.netmi.workerbusiness.widget.NormalEditDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;

public class OfflineOrderDetailActivity extends BaseActivity<ActivityOfflineOrderDetailBinding> {
    private String order_id;
    private List<String> imgUrl = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_offline_order_detail;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("订单详情");
        order_id = getIntent().getExtras().getString(JumpUtil.ID);
        doGetOrderDetails();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_comment) {
            if (mBinding.getDetailsEntity().getStatus() == 9) {
                Bundle args = new Bundle();    //订单评价
                args.putString(JumpUtil.ID, order_id);
                JumpUtil.overlay(getContext(), OfflineEvaluationActivity.class, args);
            }
        } else if (view.getId() == R.id.tv_copy) {  //复制单号
            showError(mBinding.getDetailsEntity().getOrder_no());
            KeyboardUtils.putTextIntoClip(getContext(), mBinding.getDetailsEntity().getOrder_no());
        } else if (view.getId() == R.id.tv_refuse_refund) {  //拒绝退款
            showEditDialog("拒绝原因", null, "请输入退款原因",
                    "确定", "取消", new NormalEditDialog.ClickConfirmListener() {
                        @Override
                        public void clickConfirm(String name) {
                            Refuse(name);
                        }
                    }, null, true, false);
        } else if (view.getId() == R.id.tv_agree_refund) {  //同意退款
            showLogoutDialog();
        }

    }

    //获取订单详情
    private void doGetOrderDetails() {
        RetrofitApiFactory.createApi(OfflineGoodApi.class)
                .getDetail(order_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<OfflineOrderDetailEntity>>(this) {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        finish();
                    }

                    @Override
                    public void onSuccess(BaseData<OfflineOrderDetailEntity> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            if (data.getData() != null) {
                                mBinding.setDetailsEntity(data.getData());
                                //状态     0待付款；1待核销；3待评价；9完成；7已退款
                                //订单状态：0待付款；1待核销；9交易完成；7已经退款 3待评价 4：申请退款中 5： 拒绝退款
                                if (data.getData().getStatus() == 0) {
                                    mBinding.llRemind.setVisibility(View.GONE);
                                    mBinding.tvCheckTime.setVisibility(View.GONE);
                                    mBinding.tvRefundTime.setVisibility(View.GONE);
                                    mBinding.tvGetScore.setVisibility(View.GONE);
                                    mBinding.llBottom.setVisibility(View.GONE);
                                    mBinding.llRealPay.setVisibility(View.GONE);
                                    mBinding.tvPayTime.setVisibility(View.GONE);
                                    mBinding.llRefundInfo.setVisibility(View.GONE);
                                } else if (data.getData().getStatus() == 1) {
                                    mBinding.tvCheckTime.setVisibility(View.GONE);
                                    mBinding.tvRefundTime.setVisibility(View.GONE);
                                    mBinding.tvGetScore.setVisibility(View.GONE);
                                    mBinding.llBottom.setVisibility(View.GONE);
                                    mBinding.llRefundInfo.setVisibility(View.GONE);
                                } else if (data.getData().getStatus() == 3) {
                                    mBinding.llBottom.setVisibility(View.GONE);
                                    mBinding.tvRefundTime.setVisibility(View.GONE);
                                    mBinding.tvGetScore.setVisibility(View.GONE);
                                    mBinding.vLine.setVisibility(View.VISIBLE);
                                    mBinding.llRefundInfo.setVisibility(View.GONE);
                                } else if (data.getData().getStatus() == 7) {
                                    mBinding.llBottom.setVisibility(View.GONE);
                                    mBinding.vLine.setVisibility(View.VISIBLE);
                                    mBinding.tvGetScore.setVisibility(View.GONE);
                                    mBinding.tvCheckTime.setVisibility(View.GONE);
                                } else if (data.getData().getStatus() == 9) {
                                    mBinding.tvRefundTime.setVisibility(View.GONE);
                                    mBinding.tvGetScore.setVisibility(View.GONE);
                                    mBinding.vLine.setVisibility(View.VISIBLE);
                                    mBinding.llRefundInfo.setVisibility(View.GONE);
                                } else if (data.getData().getStatus() == 4) {
                                    mBinding.tvComment.setVisibility(View.GONE);
                                    mBinding.llRefund.setVisibility(View.VISIBLE);//退款按钮
                                    mBinding.llRefundInfo.setVisibility(View.VISIBLE);//退款信息
                                    mBinding.tvApplyTime.setVisibility(View.VISIBLE);
                                    mBinding.tvCheckTime.setVisibility(View.GONE);
                                    mBinding.tvRefundTime.setVisibility(View.GONE);
                                    mBinding.tvGetScore.setVisibility(View.GONE);
                                    showRefundImg(data.getData());
                                } else if (data.getData().getStatus() == 5) {
                                    mBinding.llBottom.setVisibility(View.GONE);
                                    mBinding.llRefundInfo.setVisibility(View.VISIBLE);//退款信息
                                    mBinding.tvResultTime.setVisibility(View.VISIBLE);
                                    mBinding.tvResultTime.setText("拒绝退款时间：" + data.getData().getRefundMsg().getApproval_time());
                                    showRefundImg(data.getData());
                                }
                                if (data.getData().getType().equals("12")) {
                                    // "type": "11",//订单类型 11线下订单 12门店直接买单
                                    mBinding.llGood.setVisibility(View.GONE);
                                    mBinding.llRemind.setVisibility(View.GONE);
                                    mBinding.llNum.setVisibility(View.GONE);
                                    mBinding.tvCreateTime.setVisibility(View.GONE);
                                    mBinding.tvCheckTime.setVisibility(View.GONE);
                                    mBinding.tvRefundTime.setVisibility(View.GONE);
                                    mBinding.llBottom.setVisibility(View.GONE);
                                }
                            } else {
                                showError(getString(R.string.sharemall_lack_info));
                                finish();
                            }
                        } else {
                            showError(data.getErrmsg());
                            finish();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private NormalDialog mLogoutConfirmDialog;

    //显示询问用户是否同意退款
    private void showLogoutDialog() {

        if (mLogoutConfirmDialog == null) {
            mLogoutConfirmDialog = new NormalDialog(getContext());
        }

        if (!mLogoutConfirmDialog.isShowing()) {
            mLogoutConfirmDialog.show();
        }
        mLogoutConfirmDialog.setMessageInfo("您是否要同意退款申请？", true, 16, true);
        mLogoutConfirmDialog.setTitleInfo(getString(R.string.warm_remind), false);
        mLogoutConfirmDialog.showLineTitleWithMessage(false);
        mLogoutConfirmDialog.setCancelInfo(getString(R.string.cancel), true);
        mLogoutConfirmDialog.setConfirmInfo(getString(R.string.confirm));
        mLogoutConfirmDialog.setClickConfirmListener(new NormalDialog.ClickConfirmListener() {
            @Override
            public void clickConfirm() {
                mLogoutConfirmDialog.dismiss();
                Agree();
            }
        });
    }


    /**
     * 显示编辑框
     */
    private void showEditDialog(String title, String subTittle, String hint, String confirm, String cancel,
                                NormalEditDialog.ClickConfirmListener confirmListner,
                                NormalEditDialog.ClickCancelListener cancelListener,
                                boolean isNeed, boolean isNumber) {
        new NormalEditDialog(getContext())
                .text(title, subTittle, hint, confirm, cancel)
                .setClickCancelListener(cancelListener)
                .setClickConfirmListener(confirmListner)
                .isNeedEditText(isNeed)
                .isNumber(isNumber).showCenter();

    }

    private void showRefundImg(OfflineOrderDetailEntity entity) {
        /**
         * 图片列表
         * */
        RecyclerView recyclerViewImgs = mBinding.rvImgs;
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
        imgUrl.addAll(entity.getRefundMsg().getImg_urls());
        imgsAdapter.setData(imgUrl);
    }

    //拒绝退款
    private void Refuse(String remark) {
        RetrofitApiFactory.createApi(OfflineGoodApi.class)
                .refuse(order_id, remark)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        doGetOrderDetails();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        finish();
                    }
                });
    }

    //同意退款
    private void Agree() {
        RetrofitApiFactory.createApi(OfflineGoodApi.class)
                .agree(order_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
//                        doGetOrderDetails();
                        finish();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        finish();
                    }
                });
    }


}
