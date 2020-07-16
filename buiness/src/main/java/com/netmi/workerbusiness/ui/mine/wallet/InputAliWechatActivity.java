package com.netmi.workerbusiness.ui.mine.wallet;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.liemi.basemall.data.entity.user.WalletDetailsEntity;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.cache.WithdrawCache;
import com.netmi.workerbusiness.data.entity.mess.PublicNoticeEntity;
import com.netmi.workerbusiness.data.entity.walllet.AliWechatEntity;
import com.netmi.workerbusiness.databinding.ActivityInputAliWechatBinding;
import com.netmi.workerbusiness.databinding.ItemOfficialPushBinding;
import com.netmi.workerbusiness.databinding.ItemPayMessageBinding;
import com.netmi.workerbusiness.ui.dialog.Pay_Message_Dialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import static com.netmi.workerbusiness.ui.mine.wallet.MineWithdrawDetailActivity.ALI_WITHDRAE;
import static com.netmi.workerbusiness.ui.mine.wallet.MineWithdrawDetailActivity.WECHAT_WITHDRAE;

//支付宝和微信添加
public class InputAliWechatActivity extends BaseXRecyclerActivity<ActivityInputAliWechatBinding, AliWechatEntity> implements Pay_Message_Dialog.ClickBindPhoneListener {
    private int type; //30 支付宝 ； 31  微信
    private boolean add;//添加还是编辑

    public static final String NAME_WITHDRAW = "name_withdraw";
    public static final String ACCOUNT_WITHDRAW = "account_withdraw";

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_input_ali_wechat;
    }

    private Pay_Message_Dialog pay_message_dialog;
    private int posinid;
    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("提现信息管理");
        initRecyclerView();
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        pay_message_dialog = new Pay_Message_Dialog(this);
        pay_message_dialog.setClickBindMessageListener(this);


    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }
    //添加支付账户信息 0支付宝1微信
    private void addInfo(String typeUp, String name, String phone) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .addAliInfo(typeUp, name, phone)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        hideProgress();
                        pay_message_dialog.dismiss();
                    }
                });
    }
    //获取支付账户信息   0支付宝1微信
    private void getInfo(String type) {
        RetrofitApiFactory.createApi(MineApi.class)
                .getAliInfoList(type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<AliWechatEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<List<AliWechatEntity>> data) {
                        showData(data.getData());
                        pay_message_dialog.dismiss();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        onComplete();
                    }

                });
    }
    //编辑支付账户信息   0支付宝1微信
    private void editInfo(int id,String name,String phone) {
        RetrofitApiFactory.createApi(MineApi.class)
                .setEditInfo(String.valueOf(id),name,phone)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<String>>() {
                    @Override
                    public void onSuccess(BaseData<String> data) {
                        pay_message_dialog.dismiss();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        onComplete();
                    }

                });
    }

    //删除支付账户信息   0支付宝1微信
    private void deleteInfo(int id) {
        RetrofitApiFactory.createApi(MineApi.class)
                .deleteInfo(String.valueOf(id))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<String>>() {
                    @Override
                    public void onSuccess(BaseData<String> data) {
                        showError("删除成功");
                        xRecyclerView.refresh();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        onComplete();
                    }

                });
    }



    //对话框取消
    @Override
    public void clickBindMessageCan() {
        pay_message_dialog.dismiss();
    }

    //对话框确定
    @Override
    public void clickBindMessageCon(String name, String phone) {
        //添加
        if(add){
            if(type == 30){//支付宝
                addInfo("0",name,phone);
            }else {
                addInfo("1",name,phone);
            }
        }  else {
            //编辑
            editInfo(posinid,name,phone);
        }
        xRecyclerView.refresh();
    }

    private void initRecyclerView() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);
        adapter = new BaseRViewAdapter<AliWechatEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_pay_message;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<AliWechatEntity>(binding) {
                    @Override
                    public void bindData(AliWechatEntity item) {
                        AliWechatEntity entity = items.get(position);
                        super.bindData(item);//不能删
                        ItemPayMessageBinding itemPayMessageBinding = (ItemPayMessageBinding) binding;
                        itemPayMessageBinding.tvPayName.setText(entity.getName());
                        itemPayMessageBinding.tvPayPhone.setText(entity.getPhone());
                    }

                    @Override
                    public void doClick(View view) {
                        AliWechatEntity entity = items.get(position);
                        super.doClick(view);
                        posinid = entity.getId();
                        //编辑
                        if(view.getId() == R.id.tv_message_edit){
                            add=false;
                            pay_message_dialog.show();
                        }else if(view.getId() == R.id.tv_message_dele){
                            //删除
                            deleteInfo(posinid);
                        }else if(view.getId() == R.id.pay_message_layout){
                            setBundle(entity);
                            finish();
                        }

                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    private void setBundle(AliWechatEntity entity) {
        Bundle args = new Bundle();
        args.putString(NAME_WITHDRAW,entity.getName());
        args.putString(ACCOUNT_WITHDRAW,entity.getPhone());
        Intent intent = new Intent();
        intent.putExtra("pay",args);
        if(type == 30){
            setResult(RESULT_OK, intent);
        }else if(type == 31){
            setResult(RESULT_OK, intent);
        }
    }

    @Override//数据请求   全部数据展示
    protected void doListData() {
        if(type == 30){//支付宝
            getInfo("0");
        }else if(type == 31){//微信
            getInfo("1");
        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if(view.getId() == R.id.ll_add_bank){
            add=true;
            pay_message_dialog.show();
        }
    }
}


