package com.netmi.workerbusiness.ui.login;

import android.view.View;

import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.entity.mine.GetApplyInfo;
import com.netmi.workerbusiness.databinding.ActivityLoginLabelBinding;

import java.util.List;

public class LabelActivity extends BaseActivity<ActivityLoginLabelBinding> {

    private String label;
    private List<BaseData<GetApplyInfo>> list;

    @Override
    protected int getContentView() {
        return R.layout.activity_login_label;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("店铺标签");
        getRightSetting().setText("新增");
    }

    @Override
    protected void initData() {


    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        //新增  添加标签
        if (view.getId() == R.id.tv_setting) {
            mBinding.llTankuangThree.setVisibility(View.VISIBLE);
            label = mBinding.biaoqianXianshi.getText().toString();
        }
        if (view.getId() == R.id.tankuang_positiveButton) {

            JumpUtil.overlay(this, PersonalOfflineInfoRestrictionsActivity.class);
        }
    }


}


