package com.liemi.basemall.ui.personal.refund;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.liemi.basemall.R;
import com.liemi.basemall.contract.FileContract;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.FileEntity;
import com.liemi.basemall.data.entity.order.LogisticCompanyEntity;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.data.entity.order.RefundDetailedEntity;
import com.liemi.basemall.data.entity.order.RefundReasonEntity;
import com.liemi.basemall.databinding.ActivityRefundApplyLogisticBinding;
import com.liemi.basemall.databinding.BasemallItemSelect2Binding;
import com.liemi.basemall.databinding.BasemallItemSelectBinding;
import com.liemi.basemall.presenter.CompressPresenterImpl;
import com.liemi.basemall.presenter.FilePresenterImpl;
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
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.personal.refund.RefundApplySuccessActivity.REFUND_TIP;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

public class RefundApplyLogisticActivity extends BaseActivity<ActivityRefundApplyLogisticBinding> implements View.OnClickListener, FileContract.FileUpView, CompressPresenterImpl.CompressView {

    public static final String SUB_ORDER_DETAILED = "subOrder";

    private List<String> company;
    private RefundDetailedEntity detailedEntity;
    private MyBaseDialog companyDialog;

    private String companyName;
    private String companyCode;

    private List<String> compressPaths;
    private ArrayList<ImageItem> images;
    private List<String> uploadUrls;        //上传文件后获取的路径

    private FilePresenterImpl filePresenter;
    private CompressPresenterImpl compressPresenter;
    private PhotoAdapter photoAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_apply_logistic;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("申请退款");

        mBinding.rvImg.setLayoutManager(new GridLayoutManager(getContext(),3));
        photoAdapter=new PhotoAdapter(getContext());
        photoAdapter.setMax(6);
        mBinding.rvImg.setAdapter(photoAdapter);
    }

    @Override
    protected void initData() {
        doListCompany();

        detailedEntity= (RefundDetailedEntity) getIntent().getSerializableExtra(SUB_ORDER_DETAILED);
        if (detailedEntity==null || detailedEntity.getMeOrders()==null || detailedEntity.getMeOrders().size()<=0) {
            ToastUtils.showShort("缺少订单相关参数");
            finish();
            return;
        }

        companyName=detailedEntity.getMail_name();
        companyCode=detailedEntity.getMail_code();

        if (!Strings.isEmpty(detailedEntity.getMail_no()) && detailedEntity.getImgs()!=null){
            images=new ArrayList<>();
            List<String> imgUrls=new ArrayList<>();
            for (RefundDetailedEntity.RefundImgsBean refundImgsBean : detailedEntity.getMeRefundImgs()) {
                ImageItem item=new ImageItem();
                item.path=refundImgsBean.getImg_url();
                imgUrls.add(item.path);
                images.add(item);
            }
            photoAdapter.setData(imgUrls);
        }

        mBinding.setItem(detailedEntity.getMeOrders().get(0));
        mBinding.setData(detailedEntity);
        uploadUrls=new ArrayList<>();
        filePresenter=new FilePresenterImpl().setFileUpView(this);
        compressPresenter=new CompressPresenterImpl().setCompressView(this);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_logistic_company) {
            showCompanyDialog(company);
        }else if(i==R.id.tv_submit){       //确定
            if (validateData(detailedEntity.getMeOrders().get(0).getId(),
                    mBinding.etLogisticNo.getText().toString().trim(),
                    companyName,
                    companyCode, null,2)) {
                if (images == null || images.size() <= 0) {
                    if (Strings.isEmpty(detailedEntity.getMail_no())) {
                        doApplyRefundLogistic(null);
                    }else{
                        doUpdateApplyLogistic(null);
                    }
                } else {
                    compressPresenter.compressFiles(images);
                }
            }
        }
    }

    private boolean validateData(String id,
                                     String logisticsNo,
                                     String logisticsCompany,
                                     String companyCode,
                                     List<String> imgs,
                                     int refundStatus){
        if (Strings.isEmpty(id)){
            ToastUtils.showShort("缺少订单相关参数");
            return false;
        }
        if (Strings.isEmpty(logisticsNo)){
            ToastUtils.showShort("请填写物流单号");
            return false;
        }

        if (Strings.isEmpty(logisticsCompany)){
            ToastUtils.showShort("请选择快递公司");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_close) {
            if (companyDialog!=null){
                companyDialog.dismiss();
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

    private void showCompanyDialog(List<String> list){
        if(companyDialog==null){
            companyDialog= MyBaseDialog.getDialog(getContext(),R.layout.dialog_level_altitude_list);
            ((TextView)companyDialog.findViewById(R.id.tv_title)).setText("物流公司");
            companyDialog.findViewById(R.id.tv_close).setOnClickListener(this);
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
                            if (companyDialog != null) {
                                companyDialog.dismiss();
                            }

                            selectIndex = position;
                            if (position<0 || position>=companyList.size()){
                                return;
                            }
                            companyName=companyList.get(position).getName();
                            companyCode=companyList.get(position).getId();
                            mBinding.tvLogisticCompany.setText(companyName);
                        }
                    };
                }
            };
            RecyclerView recyclerView= ((RecyclerView)companyDialog.findViewById(R.id.rv_list));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            menuAdapter.setData(list);
            recyclerView.setAdapter(menuAdapter);
        }
        companyDialog.showBottom();
    }

    private List<LogisticCompanyEntity> companyList;

    private void doListCompany() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listLogisticCompany(0)
                .compose(RxSchedulers.<BaseData<List<LogisticCompanyEntity>>>compose())
                .compose((this).<BaseData<List<LogisticCompanyEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<LogisticCompanyEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<List<LogisticCompanyEntity>> data) {
                        if (data.getData() != null && !data.getData().isEmpty()) {
                            companyList = data.getData();
                            company = new ArrayList<>();

                            for (LogisticCompanyEntity entity : companyList) {
                                company.add(entity.getName());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }


    private void doApplyRefundLogistic(List<String> img_urls) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .applyRefundLogistic(detailedEntity.getMeOrders().get(0).getId(),
                        mBinding.etLogisticNo.getText().toString().trim(),
                        companyName,
                        companyCode, img_urls,2)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        MApplication.getInstance().appManager.finishActivity(RefundDetailedActivity.class);
                        JumpUtil.overlay(getContext(), RefundApplySuccessActivity.class, REFUND_TIP, "您的退款退货申请已提交给商家");
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doUpdateApplyLogistic(List<String> img_urls){
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .updateApplyLogistic(detailedEntity.getMeOrders().get(0).getId(),
                        mBinding.etLogisticNo.getText().toString().trim(),
                        companyName,
                        companyCode, img_urls,2)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        MApplication.getInstance().appManager.finishActivity(RefundDetailedActivity.class);
                        JumpUtil.overlay(getContext(), RefundApplySuccessActivity.class, REFUND_TIP, "您的退款退货申请已提交给商家");
                        finish();
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
                    if (Strings.isEmpty(detailedEntity.getMail_no())) {
                        doApplyRefundLogistic(uploadUrls);
                    }else{
                        doUpdateApplyLogistic(uploadUrls);
                    }
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
                filePresenter.fileUp(RefundApplyLogisticActivity.this.compressPaths.get(0));
            }
        });
    }

    @Override
    public void compressFailure(String msg) {
        ToastUtils.showShort(msg);
    }
}
