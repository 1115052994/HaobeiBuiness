package com.liemi.basemall.ui.personal.helperservice;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.entity.user.HelperServiceEntity;
import com.liemi.basemall.databinding.ActivityLinkHelperBinding;
import com.liemi.basemall.ui.NormalDialog;
import com.netmi.baselibrary.ui.BaseActivity;

public class LinkHelperActivity extends BaseActivity<ActivityLinkHelperBinding> {

    public static final String LINK_HELPER_SERVICE = "linkHelperService";
    private HelperServiceEntity mEntity;
    private NormalDialog normalDialog;
    @Override
    protected int getContentView() {
        return R.layout.activity_link_helper;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("联系客服");
    }

    @Override
    protected void initData() {
        mEntity = (HelperServiceEntity) getIntent().getExtras().getSerializable(LINK_HELPER_SERVICE);
        if(mEntity == null){
            showError("客服信息为空");
            finish();
            return;
        }
        mBinding.setHelperUser(mEntity);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if(view.getId() == R.id.ll_call_phone){
            showCallDialog();
        }
    }

    private void showCallDialog(){
        if(normalDialog == null){
            normalDialog = new NormalDialog(this);
        }

        if(!normalDialog.isShowing()){
            normalDialog.show();
        }

        normalDialog.setTitleInfo("温馨提示",true);
        normalDialog.setMessageInfo("是否拨打："+mEntity.getShop_tel(),true);
        normalDialog.setCancelInfo("取消",true);
        normalDialog.setConfirmInfo("确认");

        normalDialog.setClickConfirmListener(new NormalDialog.ClickConfirmListener() {
            @Override
            public void clickConfirm() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mEntity.getShop_tel()));
                startActivity(intent);
                normalDialog.dismiss();
            }
        });
    }
}
