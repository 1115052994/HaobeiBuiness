package com.liemi.basemall.ui.personal.refund;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.liemi.basemall.R;
import com.liemi.basemall.contract.FileContract;
import com.liemi.basemall.data.api.ETHApi;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.FileEntity;
import com.liemi.basemall.data.entity.eth.ETHRateEntity;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.data.entity.order.RefundDetailedEntity;
import com.liemi.basemall.data.entity.order.RefundPriceEntity;
import com.liemi.basemall.data.entity.order.RefundReasonEntity;
import com.liemi.basemall.databinding.ActivityUpdateApplyRefundBinding;
import com.liemi.basemall.databinding.BasemallItemSelect2Binding;
import com.liemi.basemall.databinding.BasemallItemSelectBinding;
import com.liemi.basemall.presenter.CompressPresenterImpl;
import com.liemi.basemall.presenter.FilePresenterImpl;
import com.liemi.basemall.ui.personal.order.OrderDetailActivity;
import com.liemi.basemall.widget.MyBaseDialog;
import com.liemi.basemall.widget.MyRecyclerView;
import com.liemi.basemall.widget.PhotoAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.mob.tools.utils.FileUtils;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UpFilesEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.personal.order.OrderDetailActivity.ORDER_ENTITY;
import static com.liemi.basemall.ui.personal.refund.RefundApplySuccessActivity.REFUND_TIP;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

public class UpdateApplyActivity extends BaseActivity<ActivityUpdateApplyRefundBinding> implements View.OnClickListener, FileContract.FileUpView, CompressPresenterImpl.CompressView {
    public static final String SUB_ORDER_DETAILED = "subOrder";

    private MyBaseDialog goodStatusDialog;
    private MyBaseDialog refundReasonDialog;
    private RefundDetailedEntity detailedEntity;
    private double maxPrice;

    private List<String> refundReasons;//退款原因列表
    private List<String> goodStatuses;   //货物状态
    private String refundReason;
    private String goodStatus;
    private List<String> compressPaths;
    private ArrayList<ImageItem> images;
    private List<String> uploadUrls;        //上传文件后获取的路径
    private String priceTotal;

    private FilePresenterImpl filePresenter;
    private CompressPresenterImpl compressPresenter;
    private PhotoAdapter photoAdapter;
    private int selectIndex = -1;
    private String postage;

    @Override
    protected int getContentView() {
        return R.layout.activity_update_apply_refund;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("申请退款");
        mBinding.rvImg.setLayoutManager(new GridLayoutManager(getContext(),3));
        mBinding.rvImg.setNestedScrollingEnabled(false);
        photoAdapter=new PhotoAdapter(getContext());
        photoAdapter.setMax(6);
        mBinding.rvImg.setAdapter(photoAdapter);

        mBinding.etCustomPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String price=s.toString();
                /*if (Strings.isNumber(price)){
                    double dPrice=Double.parseDouble(price);
                    if (dPrice>maxPrice){
                        mBinding.etCustomPrice.setText(priceTotal);
                    }else{
                        priceTotal=price;
                    }
                }else{
                    mBinding.etCustomPrice.setText(priceTotal);
                }*/
            }
        });
    }

    @Override
    protected void initData() {
        detailedEntity= (RefundDetailedEntity) getIntent().getSerializableExtra(SUB_ORDER_DETAILED);
        if (detailedEntity==null || detailedEntity.getMeOrders()==null || detailedEntity.getMeOrders().size()<=0) {
            ToastUtils.showShort("缺少订单相关参数");
            finish();
            return;
        }

        uploadUrls=new ArrayList<>();
        filePresenter=new FilePresenterImpl().setFileUpView(this);
        compressPresenter=new CompressPresenterImpl().setCompressView(this);
        mBinding.setItem(detailedEntity.getMeOrders().get(0));
        mBinding.setData(detailedEntity);

        refundReason=detailedEntity.getBec_type();
        goodStatus=detailedEntity.getType()==1?"未收到货物":"已收到货物";
        mBinding.tvGoodsStatus.setText(goodStatus);

        if (detailedEntity.getMeRefundImgs()!=null){
            images=new ArrayList<>();
            List<String> imgUrls=new ArrayList<>();
            for (RefundDetailedEntity.RefundImgsBean refundImgsBean : detailedEntity.getMeRefundImgs()) {
                ImageItem item=new ImageItem();
                item.path=refundImgsBean.getImg_url();
                imgUrls.add(item.path);
                images.add(item);
            }
            photoAdapter.setData(imgUrls);
            photoAdapter.notifyDataSetChanged();
        }

        mBinding.tvRefundPriceDescribe.setText("最多"+detailedEntity.getMeOrders().get(0).getShowPrice()+"，不含发货邮费0.00YMS");

        doGetRefundPrice(detailedEntity.getMeOrders().get(0).getId());

        doListReason();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_goods_status) {
            goodStatuses = new ArrayList<>();
            goodStatuses.add("未收到货物");
            goodStatuses.add("已收到货物");
            showGoodStatusDialog(goodStatuses);
        } else if (i == R.id.tv_refund_reason) {
            showRefundReasonDialog(refundReasons);
        } else if (i == R.id.tv_confirm) {       //确定
            OrderDetailsEntity.MeOrdersBean bean = detailedEntity.getMeOrders().get(0);
            if (detailedEntity.getType() == 1) { //1：未发货 2：已发货
                if (validateData(bean.getId(), refundReason, mBinding.etRefundRemark.getText().toString().trim(),
                        bean.getPrice_total(), null, 1)) {
                    doApplyRefund(bean.getId(), refundReason, mBinding.etRefundRemark.getText().toString().trim(),
                            String.valueOf(maxPrice));
                }
            } else {
                if (validateData(bean.getId(), refundReason, mBinding.etRefundRemark.getText().toString().trim(),
                        bean.getPrice_total(), null, 1)) {
                    if (images == null || images.size() <= 0) {
                        doApplyRefundGood(bean.getId(), refundReason, mBinding.etRefundRemark.getText().toString().trim(),
                                String.valueOf(maxPrice), null, 1);
                    } else {
                        compressPresenter.compressFiles(images);
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case ImagePicker.RESULT_CODE_ITEMS:
                //添加图片返回
                if (data != null && requestCode == REQUEST_CODE_SELECT) {
                    images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    if (images != null) {
                        photoAdapter.setData(ImageItemUtil.ImageItem2String(images));
                    }
                }
                break;
            case ImagePicker.RESULT_CODE_BACK:
                //预览图片返回
                if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                    images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                    if (images != null) {
                        photoAdapter.setData(ImageItemUtil.ImageItem2String(images));
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (filePresenter!=null){
            filePresenter.destroy();
        }
        if (compressPresenter!=null){
            compressPresenter.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_close) {
            if (goodStatusDialog!=null){
                goodStatusDialog.dismiss();
            }
            if (refundReasonDialog!=null){
                refundReasonDialog.dismiss();
            }
        }
    }

    private void showGoodStatusDialog(List<String> list){
        if(goodStatusDialog==null){
            goodStatusDialog= MyBaseDialog.getDialog(getContext(),R.layout.dialog_apply_refund_status);
            ((TextView)goodStatusDialog.findViewById(R.id.tv_title)).setText("货物状态");
            goodStatusDialog.findViewById(R.id.tv_close).setOnClickListener(this);
            BaseRViewAdapter<String, BaseViewHolder> menuAdapter = new BaseRViewAdapter<String, BaseViewHolder>(getContext()) {


                @Override
                public int layoutResId(int viewType) {
                    return R.layout.basemall_item_select2;
                }

                @Override
                public BaseViewHolder holderInstance(ViewDataBinding binding) {
                    return new BaseViewHolder(binding) {
                        @Override
                        public void bindData(Object item) {
                            super.bindData(item);

                            if (selectIndex == position) {
                                getBinding().ivSelected.setImageResource(R.drawable.baselib_bg_colorb52902_radius15dp);
                            } else {
                                getBinding().ivSelected.setImageResource(R.drawable.shape_round20_stroke1dp555555_colorwhite);
                            }
                        }

                        @Override
                        public BasemallItemSelect2Binding getBinding() {
                            return (BasemallItemSelect2Binding) super.getBinding();
                        }

                        @Override
                        public void doClick(View view) {
                            super.doClick(view);
                            notifyDataSetChanged();
                            if (refundReasonDialog != null) {
                                refundReasonDialog.dismiss();
                            }

                            selectIndex = position;
                            if (position<0 || position>=goodStatuses.size()){
                                return;
                            }
                            goodStatus=goodStatuses.get(position);
                            mBinding.tvGoodsStatus.setText(goodStatus);
                        }
                    };
                }
            };
            MyRecyclerView recyclerView= ((MyRecyclerView)goodStatusDialog.findViewById(R.id.rv_list));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            menuAdapter.setData(list);
            recyclerView.setAdapter(menuAdapter);
        }
        goodStatusDialog.showBottom();
    }

    private void showRefundReasonDialog(List<String> list){
        if(refundReasonDialog==null) {
            refundReasonDialog = MyBaseDialog.getDialog(getContext(), R.layout.dialog_apply_refund_status);
            ((TextView) refundReasonDialog.findViewById(R.id.tv_title)).setText("退款原因");
            refundReasonDialog.findViewById(R.id.tv_close).setOnClickListener(this);

            BaseRViewAdapter<String, BaseViewHolder> menuAdapter = new BaseRViewAdapter<String, BaseViewHolder>(getContext()) {

                private int selectIndex = -1;
                @Override
                public int layoutResId(int viewType) {
                    return R.layout.basemall_item_select2;
                }

                @Override
                public BaseViewHolder holderInstance(ViewDataBinding binding) {
                    return new BaseViewHolder(binding) {
                        @Override
                        public void bindData(Object item) {
                            super.bindData(item);

                            if (selectIndex == position) {
                                getBinding().ivSelected.setImageResource(R.drawable.baselib_bg_colorb52902_radius15dp);
                            } else {
                                getBinding().ivSelected.setImageResource(R.drawable.shape_round20_stroke1dp555555_colorwhite);
                            }
                        }

                        @Override
                        public BasemallItemSelect2Binding getBinding() {
                            return (BasemallItemSelect2Binding) super.getBinding();
                        }

                        @Override
                        public void doClick(View view) {
                            super.doClick(view);
                            notifyDataSetChanged();
                            if (refundReasonDialog != null) {
                                refundReasonDialog.dismiss();
                            }

                            selectIndex = position;
                            if (position<0 || position>=refundReasons.size()){
                                return;
                            }
                            refundReason=refundReasons.get(position);
                            mBinding.tvRefundReason.setText(refundReason);
                        }
                    };
                }
            };
            MyRecyclerView recyclerView= ((MyRecyclerView)refundReasonDialog.findViewById(R.id.rv_list));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            menuAdapter.setData(list);
            recyclerView.setAdapter(menuAdapter);
        }
        refundReasonDialog.showBottom();
    }


    private boolean validateData(String orderId,
                                String refundReason,
                                String refundRemark,
                                String priceTotal,
                                List<String> imgUrls,
                                int refundStatus){
        if (Strings.isEmpty(orderId)){
            ToastUtils.showShort("缺少订单相关参数");
            return false;
        }
        if (Strings.isEmpty(refundReason)){
            ToastUtils.showShort("请选择退款原因");
            return false;
        }
        return true;
    }

    private void doApplyRefund(String orderId,
                                String refundReason,
                                String refundRemark,String priceTotal) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .updateApplyRefund(orderId, refundReason, refundRemark,priceTotal,1)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        hideProgress();
                        JumpUtil.overlay(getContext(),RefundApplySuccessActivity.class,REFUND_TIP,"您的退款申请已提交给商家");
                        MApplication.getInstance().appManager.finishActivity(OrderDetailActivity.class);
                        MApplication.getInstance().appManager.finishActivity(SelectRefundTypeActivity.class);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doGetRefundPrice(String orderId){
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getRefundPrice(orderId)
                .compose(RxSchedulers.<BaseData<RefundPriceEntity>>compose())
                .compose((this).<BaseData<RefundPriceEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<RefundPriceEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<RefundPriceEntity> data) {
                        RefundPriceEntity refundPriceEntity=data.getData();
                        if (refundPriceEntity==null || Strings.isEmpty(refundPriceEntity.getRefund_price())){
                            priceTotal=detailedEntity.getMeOrders().get(0).getPrice_total();
                            maxPrice= Double.parseDouble(priceTotal);
                        }else{
                            priceTotal=refundPriceEntity.getRefund_price();
                            maxPrice = Double.parseDouble(priceTotal);
                        }
                        if (refundPriceEntity!=null && "4".equals(refundPriceEntity.getPay_channel())){ //退款类型 4.以太币
                            mBinding.etCustomPrice.setText(refundPriceEntity.getRefund_price());
                            mBinding.tvMoneySymbol.setText("ETH");
//                                if (detailedEntity.getType()==1){ //1：未发货 2：已发货
//                                    mBinding.tvRefundPriceDescribe.setText("最多 " + refundPriceEntity.getRefund_price() + " ETH");
//                                }else {
                            mBinding.tvRefundPriceDescribe.setText("最多 " + refundPriceEntity.getRefund_price() + " ETH，不含发货邮费" + FloatUtils.formatMoney(postage));
//                                }
                        }else {
                            mBinding.etCustomPrice.setText(detailedEntity.getMeOrders().get(0).getPrice_total());
//                                if (detailedEntity.getType()==1){ //1：未发货 2：已发货
//                                    mBinding.tvRefundPriceDescribe.setText("最多" + FloatUtils.formatMoney(maxPrice));
//                                }else{
                            mBinding.tvRefundPriceDescribe.setText("最多" + FloatUtils.formatMoney(maxPrice) + "，不含发货邮费" + FloatUtils.formatMoney(postage));
//                                }
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doGetETHRate(){
        showProgress("");
        RetrofitApiFactory.createApi(ETHApi.class)
                .getETHRate(null)
                .compose(RxSchedulers.<BaseData<ETHRateEntity>>compose())
                .compose((this).<BaseData<ETHRateEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<ETHRateEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@android.support.annotation.NonNull BaseData<ETHRateEntity> ethRateEntityBaseData) {
                        hideProgress();
                        if (ethRateEntityBaseData.getErrcode()!= Constant.SUCCESS_CODE){
                            showError(ethRateEntityBaseData.getErrmsg());
                            return;
                        }

                        doGetETHRateSuccess(ethRateEntityBaseData.getData());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void doGetETHRateSuccess(ETHRateEntity ethRateEntity) {
        if (ethRateEntity==null || Strings.isEmpty(ethRateEntity.getEth_cny())){

        }else{
            double ethCny=Double.parseDouble(ethRateEntity.getEth_cny());
            DecimalFormat format = new DecimalFormat("###0.000000");
            format.setRoundingMode(RoundingMode.HALF_UP);

            maxPrice= Double.parseDouble(format.format(maxPrice/ethCny));
            mBinding.etCustomPrice.setText(format.format(maxPrice));
            mBinding.tvMoneySymbol.setText("ETH");
            mBinding.tvRefundPriceDescribe.setText("最多 "+format.format(maxPrice)+" ETH，不含发货邮费"+FloatUtils.formatMoney(postage));
        }
    }

    /**
     * id	是	int	子订单id
     name	是	string	退款原因
     remark	否	string	退款备注
     img_url	否	string	退款图片数组
     price_total	是	int	退款价格
     refund_status	是	string	1等待商家审核2商家已审核，用户填写快递单号
     */
    private void doApplyRefundGood(String orderId,
                                    String refundReason,
                                    String refundRemark,
                                    String priceTotal,
                                    List<String> imgUrls,
                                    int refundStatus){
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .updateApplyRefundGood(orderId, refundReason, refundRemark,priceTotal, imgUrls, 2)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        hideProgress();
                        JumpUtil.overlay(getContext(),RefundApplySuccessActivity.class,REFUND_TIP,"您的退款退货申请已提交给商家");
                        MApplication.getInstance().appManager.finishActivity(OrderDetailActivity.class);
                        MApplication.getInstance().appManager.finishActivity(SelectRefundTypeActivity.class);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doListReason() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listRefundReason(0)
                .compose(RxSchedulers.<BaseData<List<RefundReasonEntity>>>compose())
                .compose((this).<BaseData<List<RefundReasonEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<RefundReasonEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<List<RefundReasonEntity>> data) {
                        if (data.getData() != null && !data.getData().isEmpty()) {
                            refundReasons=new ArrayList<>();
                            for (RefundReasonEntity refundReasonEntity : data.getData()) {
                                refundReasons.add(refundReasonEntity.getName());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }

    @Override
    public void fileUpSuccess(UpFilesEntity entity) {
        uploadUrls.add(entity.getUrl());

        if (compressPaths!=null && compressPaths.size()>0) {
            FileUtils.deleteFile(compressPaths.get(0));
            compressPaths.remove(0);
        }
        if (compressPaths==null || compressPaths.size()<=0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    OrderDetailsEntity.MeOrdersBean bean=detailedEntity.getMeOrders().get(0);
                    doApplyRefundGood(bean.getId(),refundReason,mBinding.etRefundRemark.getText().toString().trim(),
                            String.valueOf(maxPrice),uploadUrls,1);
                }
            });

        }else{
            filePresenter.fileUp(compressPaths.get(0));
        }


    }

    @Override
    public void fileFailure(String msg) {
        if (compressPaths!=null && compressPaths.size()>0) {
            for (String compressPath : compressPaths) {
                FileUtils.deleteFile(compressPath);
            }
        }
        ToastUtils.showShort(msg);
    }

    @Override
    public void compressSuccess(List<String> compressPaths) {
        this.compressPaths=compressPaths;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                filePresenter.fileUp(UpdateApplyActivity.this.compressPaths.get(0));
            }
        });
    }

    @Override
    public void compressFailure(String msg) {
        ToastUtils.showShort(msg);
    }
}
