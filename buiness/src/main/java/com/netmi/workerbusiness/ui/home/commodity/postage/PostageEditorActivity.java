package com.netmi.workerbusiness.ui.home.commodity.postage;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.LoginApi;
import com.netmi.workerbusiness.data.api.PostageApi;
import com.netmi.workerbusiness.data.event.PostageListEntity;
import com.netmi.workerbusiness.databinding.ActivityPostageEditorBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import static com.netmi.workerbusiness.ui.home.commodity.postage.PostageTempleDetailActivity.FG_ID;
import static com.netmi.workerbusiness.ui.home.commodity.postage.PostageTempleDetailActivity.TEMPLE_NAME;

public class PostageEditorActivity extends BaseActivity<ActivityPostageEditorBinding> implements SwipeRefreshLayout.OnRefreshListener {
    public static final String JUMP_FROM = "jump_from";
    public static final String FROM_CREATE = "from_create";
    public static final String FROM_TEMPLE = "from_temple";
    public static final String POST_TEMPLE_NAME = "post_temple_name";
    public static final String POST_TEMPLE_ID = "post_temple_id";

    public static final int TEMPLE_UPDATE = 10001;
    private String type;

    private BaseRViewAdapter PostageListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected int getContentView() {
        return R.layout.activity_postage_editor;
    }

    @Override
    protected void initUI() {
        type = getIntent().getExtras().getString(JUMP_FROM);
        if (type.equals(FROM_CREATE)) {
            getTvTitle().setText("选择邮费模板");
        } else {
            getTvTitle().setText("邮费模板");
        }
        getPostageList();
        initRecycleView();
        mBinding.refreshView.setOnRefreshListener(this);
    }

    private void initRecycleView() {
        recyclerView = mBinding.xrvData;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(PostageListAdapter = new BaseRViewAdapter<PostageListEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_postage_list;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId() == R.id.ll_content) {
                            if (TextUtils.equals(getIntent().getStringExtra(JUMP_FROM), FROM_CREATE)) {
                                Intent intent = new Intent();
                                intent.putExtra(POST_TEMPLE_NAME, getItem(position).getTemplate_name());
                                intent.putExtra(POST_TEMPLE_ID, getItem(position).getT_id());
                                setResult(RESULT_OK, intent);
                                finish();
                            } else if (TextUtils.equals(getIntent().getStringExtra(JUMP_FROM), FROM_TEMPLE)) {
                                Bundle bundle = new Bundle();
                                bundle.putString(FG_ID, getItem(position).getT_id());
                                bundle.putString(TEMPLE_NAME, getItem(position).getTemplate_name());
                                JumpUtil.startForResult(PostageEditorActivity.this, PostageTempleDetailActivity.class, TEMPLE_UPDATE, bundle);
                            }
                        }
                    }
                };
            }
        });
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.btn_bottom) {
            JumpUtil.overlay(getContext(), AddPostageActivity.class);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPostageList();
    }

    @Override
    public void onRefresh() {
        getPostageList();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        mBinding.refreshView.setRefreshing(false);
    }

    //获取邮费列表
    private void getPostageList() {
        RetrofitApiFactory.createApi(PostageApi.class)
                .postageList("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<PostageListEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<List<PostageListEntity>> data) {
                        PostageListAdapter.setData(data.getData());
                    }
                });
    }
}
