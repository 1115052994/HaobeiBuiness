package com.netmi.workerbusiness.ui.home.commodity.category;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivitySetTagBinding;

public class SetTagActivity extends BaseActivity<ActivitySetTagBinding> {
    public static final String TAG_NAME = "tag_name";

    @Override
    protected int getContentView() {
        return R.layout.activity_set_tag;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("添加标签");

    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.iv_clear) {
            mBinding.etTag.setText("");
        } else if (id == R.id.btn_save) {
            if (TextUtils.isEmpty(mBinding.etTag.getText())) {
                ToastUtils.showShort("请输入标签名称");
            } else {
                Intent intent = new Intent();
                intent.putExtra(TAG_NAME, mBinding.etTag.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
