package com.liemi.basemall.ui.store.comment;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.liemi.basemall.R;
import com.liemi.basemall.databinding.ActivityCommentAllBinding;
import com.netmi.baselibrary.ui.BaseActivity;

import java.util.ArrayList;

import static com.liemi.basemall.ui.store.good.BaseGoodsDetailedActivity.ITEM_ID;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/25 15:39
 * 修改备注：
 */
public class CommentAllActivity extends BaseActivity<ActivityCommentAllBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_comment_all;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("全部评价");
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        String itemId = getIntent().getStringExtra(ITEM_ID);
        fragmentList.add(CommentFragment.newInstance(itemId, "0"));
        fragmentList.add(CommentFragment.newInstance(itemId, "1"));
        mBinding.tlGroup.setViewPager(mBinding.vpGroup,
                new String[]{"全部", "有图"}, this, fragmentList);
    }

    @Override
    protected void initData() {

    }

    public void setTitleCount(String flag, int count) {
        if (TextUtils.isEmpty(flag)) return;
        if (flag.equals("0")) {
            mBinding.tlGroup.getTitleView(0).setText(("全部（" + count + "）"));
        } else {
            mBinding.tlGroup.getTitleView(1).setText(("有图（" + count + "）"));
        }
    }

}
