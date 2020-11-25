package com.netmi.workerbusiness.ui.home.haibei;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.entity.haibei.HaibeiOrderList;
import com.netmi.workerbusiness.databinding.ActivityHaiBeiOrderDetailsBinding;

public class HaiBeiOrderDetailsActivity extends BaseActivity<ActivityHaiBeiOrderDetailsBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_hai_bei_order_details;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getIntent().getStringExtra("订单详情"));
        HaibeiOrderList message = (HaibeiOrderList) getIntent().getSerializableExtra("message");
        mBinding.setItem(message);

    }

    @Override
    protected void initData() {


    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if(view.getId()==R.id.copy_order_message){
            copy(mBinding.copyOrderMessage.getText().toString());
        }
    }

    /*
     * 复制到剪切板
     * */
    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            showError("复制成功");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
