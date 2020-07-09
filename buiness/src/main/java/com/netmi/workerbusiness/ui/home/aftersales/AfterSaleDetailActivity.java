package com.netmi.workerbusiness.ui.home.aftersales;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.liemi.basemall.widget.PhotoAdapter;
import com.netease.nim.uikit.common.ToastHelper;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.AfterSaleApi;
import com.netmi.workerbusiness.data.entity.home.aftersale.AfterSaleDetailEntity;
import com.netmi.workerbusiness.data.entity.home.lineorder.LineOrderListEntity;
import com.netmi.workerbusiness.databinding.ActivityAfterSaleDetailBinding;
import com.netmi.workerbusiness.databinding.DialogChangeNoteBinding;
import com.netmi.workerbusiness.databinding.ItemOrderGoodBinding;
import com.netmi.workerbusiness.databinding.ItemPostImgsBinding;
import com.netmi.workerbusiness.ui.utils.AlertDialogButtonUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

public class AfterSaleDetailActivity extends BaseActivity<ActivityAfterSaleDetailBinding> {
    private String refund_id;
    private RecyclerView rvGoods;
    private List<String> imgUrl = new ArrayList<>();
    private PhotoAdapter photoAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_after_sale_detail;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("退款详情");
        refund_id = getIntent().getExtras().getString(JumpUtil.ID);
        getDetail(refund_id);
    }

    private void initRecycleview() {
        rvGoods = mBinding.rvGoods;
        rvGoods.setNestedScrollingEnabled(false);
        rvGoods.setLayoutManager(new LinearLayoutManager(getContext()));
        BaseRViewAdapter<AfterSaleDetailEntity.OrderSkuBean, BaseViewHolder> goodAdapter = initGoodsAdapter();
        rvGoods.setAdapter(goodAdapter);
        goodAdapter.setData(mBinding.getModel().getOrderSku());

        mBinding.rvImgs.setLayoutManager(new GridLayoutManager(this, 4));

        photoAdapter = new PhotoAdapter(getContext());
        mBinding.rvImgs.setAdapter(photoAdapter);

        imgUrl.clear();
        for (int i = 0; i < mBinding.getModel().getMeRefundImgs().size(); i++) {
            imgUrl.add(mBinding.getModel().getMeRefundImgs().get(i).getImg_url());
        }
        photoAdapter.setMax(imgUrl.size());
        photoAdapter.setAddImg(false);
        photoAdapter.setCanAddImg(false);
        photoAdapter.setData(imgUrl);

//        /**
//         * 图片列表
//         * */
//        RecyclerView recyclerViewImgs = mBinding.rvImgs;
//        recyclerViewImgs.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        BaseRViewAdapter<String, BaseViewHolder> imgsAdapter = new BaseRViewAdapter<String, BaseViewHolder>(getContext()) {
//            @Override
//            public int layoutResId(int viewType) {
//                return R.layout.item_post_imgs;
//            }
//
//            @Override
//            public BaseViewHolder holderInstance(ViewDataBinding binding) {
//                return new BaseViewHolder(binding) {
//                    @Override
//                    public void bindData(Object item) {
//                        super.bindData(item);
//                        GlideShowImageUtils.displayNetImage(getContext(), getItem(position), ((ItemPostImgsBinding) getBinding()).ivPic, R.drawable.bg_radius_4dp_solid_white, 8);
//                    }
//
//                    @Override
//                    public void doClick(View view) {
//                        super.doClick(view);
//                        ItemPostImgsBinding itemPostImgsBinding = (ItemPostImgsBinding) binding;
//                        itemPostImgsBinding.ivPic.performClick();
//                    }
//                };
//            }
//        };
//        recyclerViewImgs.setAdapter(imgsAdapter);
//        imgUrl.clear();
//        for (int i = 0; i < mBinding.getModel().getMeRefundImgs().size(); i++) {
//            imgUrl.add(mBinding.getModel().getMeRefundImgs().get(i).getImg_url());
//        }
//        imgsAdapter.setData(imgUrl);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_copy) {  //复制单号
            KeyboardUtils.putTextIntoClip(getContext(), mBinding.getModel().getRefund_no());
            showError(ResourceUtil.getString(R.string.copy_success));
        } else if (id == R.id.tv_disagree) { //拒绝
            ////0.取消退款申请 1.发起退款退货申请    2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
            // 1.发起退款退货申请 （卖家第一次审核）   3、已填写物流单号，等待卖家审核（卖家第二次审核）
            showRefuseDialog();
        } else if (id == R.id.tv_agree) {  //同意
            showAgreeDialog();
        }
    }

    //拒绝弹窗
    private void showRefuseDialog() {
        DialogChangeNoteBinding noteBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_change_note, mBinding.llContent, false);
        noteBinding.tvTitle.setText("拒绝退款");
        noteBinding.etNote.setHint("请输入拒绝原因");
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(noteBinding.getRoot()).create();
        noteBinding.tvCancel.setOnClickListener((v) -> alertDialog.dismiss());
        noteBinding.tvConfirm.setOnClickListener((v) -> {
            String note = noteBinding.etNote.getText().toString();
            if (!TextUtils.isEmpty(note)) {
                alertDialog.dismiss();
                //"type": "1",//1：未发货 2：已发货
                if (mBinding.getModel().getType().equals("1")) { //退款时拒绝
                    disagree(note); //第二次审核拒绝
                } else if (mBinding.getModel().getType().equals("2")) { //退款退货时拒绝
                    if (mBinding.getModel().getRefund_status() == 1) {
                        firstReview("2", note); //第一次审核拒绝

                    } else if (mBinding.getModel().getRefund_status() == 3) {
                        disagree(note); //第二次审核拒绝
                    }
                }
            } else {
                ToastHelper.showToast(getContext(), noteBinding.etNote.getHint().toString());
            }
        });
        alertDialog.show();
    }

    //同意弹窗
    private void showAgreeDialog() {
        AlertDialogButtonUtil.setAlertButtonColor(new AlertDialog.Builder(this)
                .setMessage("确认同意退款申请？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mBinding.getModel().getType().equals("1")) {//退款时同意
                            agree();//第二次审核同意
                        } else if (mBinding.getModel().getType().equals("2")) {  //退款退货时同意

                            if (mBinding.getModel().getRefund_status() == 1) {
                                firstReview("1", ""); //第一次审核同意
                            } else if (mBinding.getModel().getRefund_status() == 3) {
                                agree();   //第二次审核同意
                            }

                        }
                    }
                })
                .show());
    }


    //初始化商品adapter
    protected BaseRViewAdapter<AfterSaleDetailEntity.OrderSkuBean, BaseViewHolder>
    initGoodsAdapter() {
        BaseRViewAdapter<AfterSaleDetailEntity.OrderSkuBean, BaseViewHolder> goodAdapter = new BaseRViewAdapter<AfterSaleDetailEntity.OrderSkuBean, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_refund_good;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                    }
                };
            }
        };
        return goodAdapter;
    }

    private void getDetail(String id) {
        RetrofitApiFactory.createApi(AfterSaleApi.class)
                .getAfterSaleDetail(id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<AfterSaleDetailEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<AfterSaleDetailEntity> data) {
                        mBinding.setModel(data.getData());
                        initRecycleview();
                    }
                });
    }


    /**
     * 第一次审核
     * status_ayy	number	状态 1： 卖家已审核 2： 卖家拒绝退款	Y
     * refuse_remark	string	拒绝退款说明，拒绝时必填	N
     */
    private void firstReview(String status_ayy, String refuse_remark) {
        RetrofitApiFactory.createApi(AfterSaleApi.class)
                .firstReview(mBinding.getModel().getId(), status_ayy, refuse_remark)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        getDetail(refund_id);
                    }

                });
    }

    /**
     * 第二次审核（同意）
     */
    private void agree() {
        RetrofitApiFactory.createApi(AfterSaleApi.class)
                .agree(mBinding.getModel().getId(), mBinding.getModel().getPrice_total())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        getDetail(refund_id);

                    }
                });
    }

    /**
     * 第二次审核（拒绝）
     */
    private void disagree(String refuse_remark) {
        RetrofitApiFactory.createApi(AfterSaleApi.class)
                .disagree(mBinding.getModel().getId(), refuse_remark)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        getDetail(refund_id);

                    }
                });
    }


}
