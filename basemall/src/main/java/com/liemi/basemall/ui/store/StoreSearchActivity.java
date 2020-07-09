package com.liemi.basemall.ui.store;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.CategoryApi;
import com.liemi.basemall.data.event.SearchKeyWordEvent;
import com.liemi.basemall.databinding.ActivityStoreSearchBinding;
import com.liemi.basemall.ui.home.SearchRecordAdapter;
import com.liemi.basemall.ui.store.good.StoreGoodsFragment;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/3/20 14:19
 * 修改备注：
 */
public class StoreSearchActivity extends BaseActivity<ActivityStoreSearchBinding> {

    private SearchRecordAdapter hotAdapter;
    private List<String> hotRecords;
    private String storeId;

    @Override
    protected int getContentView() {
        return R.layout.activity_store_search;
    }

    @Override
    protected void initUI() {
        hotRecords = new ArrayList<>();
        hotAdapter = new SearchRecordAdapter(hotRecords);
        mBinding.idHotLabel.setAdapter(hotAdapter);
        mBinding.idHotLabel.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mBinding.etKeyword.setText(hotAdapter.getItem(position));
                doSearch();
                return false;
            }
        });

        mBinding.etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!TextUtils.isEmpty(s)) {
//                    mBinding.tvCancel.setText(getString(R.string.search));
                } else {
                    mBinding.tvClose.setText(getString(R.string.cancel));
                }
                if (TextUtils.isEmpty(s)) {
                    mBinding.svSearchRecord.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mBinding.etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyboardUtils.hideKeyboard(mBinding.etKeyword);
                    String content = mBinding.etKeyword.getText().toString().trim();
                    if (Strings.isEmpty(content)) {
                        return true;
                    }

                    doSearch();
                    return true;
                }
                return false;
            }
        });

        storeId = getIntent().getStringExtra(STORE_ID);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.rl_fragment_goods, StoreGoodsFragment.newInstance(storeId, null));
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void initData() {
        getHotSearchList();
    }

    public String getEtSearchText() {
        return mBinding.etKeyword.getText().toString();
    }

    private void doSearch() {
        KeyboardUtils.hideKeyboard(mBinding.etKeyword);
        EventBus.getDefault().post(new SearchKeyWordEvent());
        mBinding.etKeyword.setSelection(getEtSearchText().length());
        mBinding.svSearchRecord.setVisibility(View.GONE);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_close) {//如果有内容搜索，反之是取消功能
            if (TextUtils.equals(mBinding.tvClose.getText(), getString(R.string.search))) {
                doSearch();
            } else {
                KeyboardUtils.hideKeyboard(view);
                onBackPressed();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    private void getHotSearchList() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .getHotSearchList(storeId)
                .cache()
                .compose(RxSchedulers.<BaseData<List<String>>>compose())
                .compose((this).<BaseData<List<String>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<String>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<List<String>> data) {
                        hotRecords.clear();
                        if (!Strings.isEmpty(data.getData())) {
                            hotRecords.addAll(data.getData());
                        }
                        hotAdapter.notifyDataChanged();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

}
