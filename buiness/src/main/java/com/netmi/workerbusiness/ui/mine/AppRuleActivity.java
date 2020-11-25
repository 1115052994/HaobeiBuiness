package com.netmi.workerbusiness.ui.mine;

import android.os.Bundle;
import android.view.View;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.ContentEntity;
import com.netmi.workerbusiness.databinding.ActivityAppRuleBinding;
import com.netmi.workerbusiness.utils.HTMLFormat;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;

public class AppRuleActivity extends BaseActivity<ActivityAppRuleBinding> {
    private ContentEntity entity;

    @Override
    protected int getContentView() {
        return R.layout.activity_app_rule;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("平台规则");
//        adapter = new BaseRViewAdapter<ContentEntity, BaseViewHolder>(getContext()) {
//            @Override
//            public int layoutResId(int viewType) {
//                return R.layout.item_rule;
//            }
//
//            @Override
//            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
//                return new BaseViewHolder<ContentEntity>(binding) {
//
//                    @Override
//                    public void bindData(ContentEntity item) {
//                        super.bindData(item);//不能删
//                    }
//
//                    @Override
//                    public void doClick(View view) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString(WEBVIEW_TITLE, items.get(position).getTitle());
//                        bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_CONTENT);
//                        bundle.putString(WEBVIEW_CONTENT, HTMLFormat.getNewData(items.get(position).getContent()));
//                        JumpUtil.overlay(getContext(), BaseWebviewActivity.class, bundle);
//                    }
//                };
//            }
//        };
//        xRecyclerView = mBinding.xrvData;
//        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        xRecyclerView.setAdapter(adapter);
//        xRecyclerView.setPullRefreshEnabled(true);
//        xRecyclerView.setLoadingMoreEnabled(true);
//        xRecyclerView.setLoadingListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_rule_service) {    //服务协议
            service(38);
        } else if (id == R.id.tv_rule_secret) {    //隐私协议
            service(33);
        } else if (id == R.id.tv_rule_secret_office) {    //线下商家销售奖励政策
            service(53);
        }
    }


    private void service(Integer type) {
        RetrofitApiFactory.createApi(MineApi.class)
                .content(type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ContentEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ContentEntity> data) {
                        entity = data.getData();
                        Bundle bundle = new Bundle();
                        bundle.putString(WEBVIEW_TITLE, data.getData().getTitle());
                        bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_CONTENT);
                        bundle.putString(WEBVIEW_CONTENT, HTMLFormat.getNewData(entity.getContent()));
                        JumpUtil.overlay(getContext(), BaseWebviewActivity.class, bundle);
                    }
                });
    }


//    @Override
//    protected void doListData() {
//        RetrofitApiFactory.createApi(MineApi.class)
//                .contentNew("")
//                .compose(RxSchedulers.compose())
//                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new XObserver<BaseData<PageEntity<ContentEntity>>>() {
//                    @Override
//                    public void onSuccess(BaseData<PageEntity<ContentEntity>> data) {
//                        showData(data.getData());
//                    }
//                });
//    }
}
