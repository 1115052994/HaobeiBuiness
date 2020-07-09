package com.liemi.basemall.ui.personal.refund;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.ExchangeGoodEntity;
import com.liemi.basemall.data.entity.order.ReplaceInfoEntity;
import com.liemi.basemall.data.event.OrderRefundEvent;
import com.liemi.basemall.databinding.ActivityRequestReplaceDetailBinding;
import com.liemi.basemall.databinding.ItemReplaceImgsBinding;
import com.liemi.basemall.ui.personal.order.OrderDetailActivity;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.widget.ConfirmDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;


public class RequestReplaceDetailActivity extends BaseActivity<ActivityRequestReplaceDetailBinding> {

    public static final int RESULT_CODE = -1;
    public static final int REQUEST_CODE = 2;

    //换货商品主键
    public static final String REPLACE_ID = "replace_id";
    //订单详情换货列表下标
    public static final String POSITION = "position";
    //商品换货状态
    public static final String REPLACE_STATUS = "replace_status";
    //跳转到换货页面进行编辑
    public static final String TO_UPDATE = "to_update";
    //换货信息实体类
    public static final String REPLACEINFOENTITY= "replaceinfoentity";

    //判断更新换货信息来自哪个页面（换货列表或者订单详情）
    public static final String REPLACE_FROM = "replace_from";
    //换货列表页面（区别于订单详情即可）
    public static final String REPLACE_FROM_LIST = "replace_from_list";

    ReplaceInfoEntity replaceInfoEntity;

    BaseRViewAdapter<String, BaseViewHolder> adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_request_replace_detail;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("换货申请");
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        RecyclerView recyclerView = mBinding.rvPic;
        mBinding.rvPic.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter =  new BaseRViewAdapter<String, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_replace_imgs;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        GlideShowImageUtils.displayNetImage(context, getItem(position), getBinding().riv, R.drawable.baselib_bg_default_pic);
                    }

                    @Override
                    public ItemReplaceImgsBinding getBinding() {
                        return (ItemReplaceImgsBinding)super.getBinding();
                    }
                };
            }
        });
    }

    @Override
    protected void initData() {
        if (getIntent().getIntExtra(REPLACE_ID,-1) != -1){
            doReplaceInfo(getIntent().getIntExtra(REPLACE_ID,-1));
        }else {
            ToastUtils.showShort("暂无换货详情");
            finish();
        }
    }

    public void doReplaceInfo(int replace_id){
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getChangeDetail(replace_id)
                .compose(RxSchedulers.<BaseData<ReplaceInfoEntity>>compose())
                .compose((this).<BaseData<ReplaceInfoEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ReplaceInfoEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(final BaseData<ReplaceInfoEntity> data) {
                        showData(data.getData());
                        mBinding.etPhone.setText(data.getData().getPhone());
                        mBinding.etContent.setText(data.getData().getRemark());
                        if (TextUtils.isEmpty(data.getData().getRefuse_remark())){
                            mBinding.tvRefuseRemark.setVisibility(View.GONE);
                        }else {
                            mBinding.tvRefuseRemark.setVisibility(View.VISIBLE);
                            mBinding.tvRefuseRemark.setText(data.getData().getRefuse_remark());
                        }
                        //状态：0: 已取消换货 1：发起换货 2：完成换货 3、拒绝换货
                        if (TextUtils.equals(data.getData().getStatus(),"1")){
                            getRightSetting().setText("取消换货");
                            getRightSetting().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new ConfirmDialog(getContext())
                                            .setContentText("您确定要取消换货申请吗？")
                                            .setConfirmListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    doCancelRequest(Integer.parseInt(data.getData().getOrderSku().getId()));
                                                }
                                            }).show();
                                }
                            });
                            mBinding.tvReplaceStatus.setText("审核中");
                            mBinding.ivReplaceStatus.setImageResource(R.mipmap.ic_examine_ing);
                        }else if (TextUtils.equals(data.getData().getStatus(),"2")){
                            mBinding.tvReplaceStatus.setText("换货成功");
                            mBinding.ivReplaceStatus.setImageResource(R.mipmap.ic_examine_success);
                        }else {
                            getRightSetting().setText("重新申请");
                            getRightSetting().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle refundBundle = new Bundle();
                                    refundBundle.putSerializable(OrderDetailActivity.ORDER_ENTITY, getIntent().getSerializableExtra(OrderDetailActivity.ORDER_ENTITY));
                                    refundBundle.putSerializable(REPLACEINFOENTITY,data.getData());
                                    refundBundle.putString(REPLACE_FROM,getIntent().getStringExtra(REPLACE_FROM));
                                    refundBundle.putString(TO_UPDATE,"yes");
                                    refundBundle.putInt(RequestReplaceActivty.REPLACE_POSITION, getIntent().getIntExtra(POSITION,-1));
                                    JumpUtil.overlay(getActivity(), RequestReplaceActivty.class, refundBundle);
                                    finish();
                                }
                            });
                            mBinding.tvReplaceStatus.setText("换货失败");
                            mBinding.ivReplaceStatus.setImageResource(R.mipmap.ic_examine_fail);
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


    private void showData(ReplaceInfoEntity entity){
        replaceInfoEntity = entity;
        ExchangeGoodEntity exchangeGoodEntity = new ExchangeGoodEntity();
        exchangeGoodEntity.setImg_url(replaceInfoEntity.getOrderSku().getItem_img());
        exchangeGoodEntity.setPrice(replaceInfoEntity.getOrderSku().getSku_price());
        exchangeGoodEntity.setSize(replaceInfoEntity.getOrderSku().getNum());
        exchangeGoodEntity.setTitle(replaceInfoEntity.getOrderSku().getSpu_name());
        exchangeGoodEntity.setSpecifications("规格：" + replaceInfoEntity.getOrderSku().getType());
        mBinding.layoutExchangeGood.setItem(exchangeGoodEntity);
        adapter.setData(replaceInfoEntity.getMeChangeImg());
    }


    public void doCancelRequest(int replace_id){
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getReplaceCancel(replace_id)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort("成功");
                        if (TextUtils.equals(getIntent().getStringExtra(REPLACE_FROM),REPLACE_FROM_LIST)){
                            EventBus.getDefault().post(new OrderRefundEvent());
                        }else {
                            Intent intent = new Intent();
                            intent.putExtra(POSITION,getIntent().getIntExtra(POSITION,-1));
                            intent.putExtra(REPLACE_STATUS,"4");
                            setResult(RESULT_CODE,intent);
                        }
                        finish();
                    }
                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }
}
