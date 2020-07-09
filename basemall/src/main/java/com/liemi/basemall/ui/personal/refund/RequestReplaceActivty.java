package com.liemi.basemall.ui.personal.refund;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import com.liemi.basemall.R;
import com.liemi.basemall.contract.FileContract;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.ExchangeGoodEntity;
import com.liemi.basemall.data.entity.order.OrderDetailsEntity;
import com.liemi.basemall.data.entity.order.ReplaceInfoEntity;
import com.liemi.basemall.data.event.OrderRefundEvent;
import com.liemi.basemall.databinding.ActivityRequsetReplaceBinding;
import com.liemi.basemall.presenter.CompressPresenterImpl;
import com.liemi.basemall.presenter.FilePresenterImpl;
import com.liemi.basemall.ui.personal.order.OrderDetailActivity;
import com.liemi.basemall.widget.PhotoAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UpFilesEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;


public class RequestReplaceActivty extends BaseActivity<ActivityRequsetReplaceBinding> implements FileContract.FileUpView, CompressPresenterImpl.CompressView{

    //换货商品下标
    public static final String REPLACE_POSITION = "repalce_position";
    //订单详情列表下标
    public static final String POSITION = "position";
    //换货状态
    public static final String REPLACE_STATUS = "replace_status";

    public final static int REQUEST_CODE = 1;
    public final static int RESULT_CODE = -1;

    private OrderDetailsEntity orderDetailsEntity;

    private ReplaceInfoEntity replaceInfoEntity;

    private List<String> compressPaths;
    private ArrayList<ImageItem> images;
    private List<String> uploadUrls;//上传文件后获取的路径

    private FilePresenterImpl filePresenter;
    private CompressPresenterImpl compressPresenter;
    private PhotoAdapter photoAdapter;

    private int position;

    //判断是否为更新换货信息
    private boolean detail_to_update;

    @Override
    protected int getContentView() {
        return R.layout.activity_requset_replace;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("申请退货换货");
        mBinding.rvPic.setLayoutManager(new GridLayoutManager(this, 4));
        photoAdapter  = new PhotoAdapter(getContext());
        photoAdapter.setMax(6);
        mBinding.rvPic.setAdapter(photoAdapter);
        uploadUrls=new ArrayList<>();
    }

    @Override
    protected void initData() {
        position = getIntent().getIntExtra(REPLACE_POSITION, 0);
        orderDetailsEntity = (OrderDetailsEntity) getIntent().getSerializableExtra(OrderDetailActivity.ORDER_ENTITY);
        replaceInfoEntity = (ReplaceInfoEntity)getIntent().getSerializableExtra(RequestReplaceDetailActivity.REPLACEINFOENTITY);

        if (getIntent().getStringExtra(RequestReplaceDetailActivity.TO_UPDATE) != null){
            if (TextUtils.equals(getIntent().getStringExtra(RequestReplaceDetailActivity.TO_UPDATE),"yes")){
                detail_to_update = true;
            }
        }else {
            detail_to_update = false;
        }

        if (orderDetailsEntity == null && replaceInfoEntity == null) {
            ToastUtils.showShort("没有订单");
            finish();
            return;
        }
        if (replaceInfoEntity != null){
            if (detail_to_update){
                //固定子项数据填充
                ExchangeGoodEntity exchangeGoodEntity = new ExchangeGoodEntity();
                exchangeGoodEntity.setImg_url(replaceInfoEntity.getOrderSku().getItem_img());
                exchangeGoodEntity.setPrice(replaceInfoEntity.getOrderSku().getSku_price());
                exchangeGoodEntity.setSize(replaceInfoEntity.getOrderSku().getNum());
                exchangeGoodEntity.setTitle(replaceInfoEntity.getOrderSku().getSpu_name());
                exchangeGoodEntity.setSpecifications("规格：" + replaceInfoEntity.getOrderSku().getType());
                mBinding.layoutExchangeGood.setItem(exchangeGoodEntity);

                mBinding.etPhone.setText(replaceInfoEntity.getPhone());
                mBinding.etContent.setText(replaceInfoEntity.getRemark());
                if (replaceInfoEntity.getMeChangeImg() != null ){
                    images=new ArrayList<>();
                    List<String> imgUrls=new ArrayList<>();
                    for (int i = 0;i < replaceInfoEntity.getMeChangeImg().size();i++){
                        ImageItem item=new ImageItem();
                        item.path = replaceInfoEntity.getMeChangeImg().get(i);
                        imgUrls.add(item.path);
                        images.add(item);
                    }
                    photoAdapter.setData(imgUrls);
                }
            }

        }
        if (orderDetailsEntity != null){
            ExchangeGoodEntity exchangeGoodEntity = new ExchangeGoodEntity();
            exchangeGoodEntity.setImg_url(orderDetailsEntity.getMeOrders().get(position).getImg_url());
            exchangeGoodEntity.setPrice(orderDetailsEntity.getMeOrders().get(position).getPrice());
            exchangeGoodEntity.setSize(orderDetailsEntity.getMeOrders().get(position).getNum());
            exchangeGoodEntity.setTitle(orderDetailsEntity.getMeOrders().get(position).getTitle());
            exchangeGoodEntity.setSpecifications("规格：" +orderDetailsEntity.getMeOrders().get(position).getColor_name());
            mBinding.layoutExchangeGood.setItem(exchangeGoodEntity);
        }

        filePresenter=new FilePresenterImpl().setFileUpView(this);
        compressPresenter=new CompressPresenterImpl().setCompressView(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    photoAdapter.setData(ImageItemUtil.ImageItem2String(images));
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    photoAdapter.setData(ImageItemUtil.ImageItem2String(images));
                }
            }
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



    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.btn_request_replace) {
            String content = mBinding.etContent.getText().toString();
            if (TextUtils.isEmpty(mBinding.etPhone.getText().toString())) {
                ToastUtils.showShort("请输入联系方式");
            }else if (TextUtils.isEmpty(content)) {
                ToastUtils.showShort("请先描述问题");
            }else {
                if (photoAdapter.getImages() == null){
                    doChangeOrUpdate(detail_to_update,null);
                }else {
                    if (photoAdapter.getImages().size() == 0 ){
                        doChangeOrUpdate(detail_to_update,null);
                    }else {
                        compressPresenter.compressFilesFromImageItemByByQuality(images);
                    }
                }

            }
        }
    }

    public void doChangeOrUpdate(boolean detail_to_update,List<String> uploadUrls){
        if (detail_to_update){
            doGetChangeUpdate(detail_to_update ? replaceInfoEntity.getOrderSku().getId():orderDetailsEntity.getMeOrders().get(position).getId(),
                    mBinding.etContent.getText().toString(),
                    mBinding.etPhone.getText().toString(),
                    uploadUrls);
        }else {
            doGetChange(detail_to_update ? replaceInfoEntity.getOrderSku().getId():orderDetailsEntity.getMeOrders().get(position).getId(),
                    mBinding.etContent.getText().toString(),
                    mBinding.etPhone.getText().toString(),
                    uploadUrls);
        }
    }

    public void doRefreshFinish(String replace_status){
        if (getIntent().getStringExtra(RequestReplaceDetailActivity.REPLACE_FROM) != null){
            if (TextUtils.equals(getIntent().getStringExtra(RequestReplaceDetailActivity.REPLACE_FROM),RequestReplaceDetailActivity.REPLACE_FROM_LIST)){
                EventBus.getDefault().post(new OrderRefundEvent());
            }else {
                Intent intent = new Intent();
                intent.putExtra(POSITION,position = getIntent().getIntExtra(REPLACE_POSITION, 0));
                intent.putExtra(REPLACE_STATUS,replace_status);
                setResult(RESULT_CODE,intent);
            }
        }else {
            Intent intent = new Intent();
            intent.putExtra(POSITION,position = getIntent().getIntExtra(REPLACE_POSITION, 0));
            intent.putExtra(REPLACE_STATUS,replace_status);
            setResult(RESULT_CODE,intent);
        }
    }

    public void doGetChange(String id,String remark,String phone,List<String> img_url){
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getChange(id, remark,phone,img_url)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            ToastUtils.showShort("申请已提交！");
                            doRefreshFinish("6");
                            finish();
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    public void doGetChangeUpdate(String id,String remark,String phone,List<String> img_url){
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getChangeUpdate(id, remark,phone,img_url)
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
                        doRefreshFinish("1");
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
            com.mob.tools.utils.FileUtils.deleteFile(compressPaths.get(0));
            compressPaths.remove(0);
        }
        if (compressPaths==null || compressPaths.size()<=0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    doChangeOrUpdate(detail_to_update,uploadUrls);
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
                com.mob.tools.utils.FileUtils.deleteFile(compressPath);
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
                filePresenter.fileUp(RequestReplaceActivty.this.compressPaths.get(0));
            }
        });
    }

    @Override
    public void compressFailure(String msg) {
        ToastUtils.showShort(msg);
    }
}
