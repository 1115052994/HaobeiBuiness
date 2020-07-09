package com.liemi.basemall.ui.personal;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ActivityMessageDetailForTextBinding;
import com.netmi.baselibrary.ui.BaseActivity;

public class MessageDetailForTextActivity extends BaseActivity<ActivityMessageDetailForTextBinding> {

    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    @Override
    protected int getContentView() {
        return R.layout.activity_message_detail_for_text;
    }

    @Override
    protected void initUI() {
        if (getIntent().getStringExtra(TITLE) != null){
            getTvTitle().setText(getIntent().getStringExtra(TITLE));
        }
        if (getIntent().getStringExtra(CONTENT) != null){
            mBinding.tvContent.setText(getIntent().getStringExtra(CONTENT));
        }

    }

    @Override
    protected void initData() {

    }
}
