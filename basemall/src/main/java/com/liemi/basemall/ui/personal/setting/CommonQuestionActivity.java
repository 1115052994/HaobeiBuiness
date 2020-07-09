package com.liemi.basemall.ui.personal.setting;


import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.user.CommonQuestionListEntity;
import com.liemi.basemall.databinding.ActivityCommonQuestionBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class CommonQuestionActivity extends BaseActivity<ActivityCommonQuestionBinding> implements XRecyclerView.LoadingListener {
    //请求常见问题的标识
    public static final String LOAD_COMMON_QUESTION = "1";//常见问题
    public static final String LOAD_CONTACT_US = "2";//联系我们


    private int startPage = 0;
    private int pages = 10;
    private int totalPages = 0;
    private int loadItems = 0;
    private int loadTyp = -1;

    private BaseRViewAdapter<CommonQuestionListEntity.CommonQustionEntity,BaseViewHolder> adapter;
    @Override
    protected int getContentView() {
        return R.layout.activity_common_question;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("常见问题");
        xRecyclerView = mBinding.frContent;
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseRViewAdapter<CommonQuestionListEntity.CommonQustionEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_common_question;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                BaseViewHolder viewHolder = new BaseViewHolder(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        //跳转到webView页面
                        Intent intent = new Intent(CommonQuestionActivity.this,BaseWebviewActivity.class);
                        intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE,BaseWebviewActivity.WEBVIEW_TYPE_CONTENT);
                        intent.putExtra(BaseWebviewActivity.WEBVIEW_CONTENT, items.get(position).getContent());
                        intent.putExtra(BaseWebviewActivity.WEBVIEW_TITLE,items.get(position).getTitle());
                        startActivity(intent);
                    }
                };
                return viewHolder;
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        doCommonQuestion();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        startPage++;
        loadTyp = Constant.LOAD_MORE;
    }

    //显示常见问题
    private void showCommonQuestion(CommonQuestionListEntity commonQuestionListEntity){
        if(!TextUtils.isEmpty(commonQuestionListEntity.getTotal_pages())){
            totalPages = Integer.parseInt(commonQuestionListEntity.getTotal_pages());
        }
        loadItems += commonQuestionListEntity.getList().size();

        if(loadTyp == -1){
            adapter.setData(commonQuestionListEntity.getList());
        }else if(loadTyp == Constant.LOAD_MORE){
            adapter.insert(adapter.getItemCount(),commonQuestionListEntity.getList());
        }

        if(loadItems >= totalPages){
            xRecyclerView.setLoadingMoreEnabled(false);
        }else{
            xRecyclerView.setLoadingMoreEnabled(true);
        }

    }

    //请求常见问题
    private void doCommonQuestion(){
        /*
        RetrofitApiFactory.createApi(MineApi.class)
                .doGetCommonQuestionList(LOAD_COMMON_QUESTION,startPage,pages)
                .compose(this.<BaseData<CommonQuestionListEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<CommonQuestionListEntity>>compose())
                .subscribe(new BaseObserver<BaseData<CommonQuestionListEntity>>() {
                    @Override
                    public void onNext(BaseData<CommonQuestionListEntity> commonQuestionListEntityBaseData) {
                        if(commonQuestionListEntityBaseData.getErrcode() == Constant.SUCCESS_CODE){
                            showCommonQuestion(commonQuestionListEntityBaseData.getData());
                        }else{
                            showError(commonQuestionListEntityBaseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });
                */
    }
}
