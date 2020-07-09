package com.liemi.basemall.ui.personal;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.MessageEntity;
import com.liemi.basemall.data.entity.UserNoticeEntity;
import com.liemi.basemall.databinding.ActivityMessageBinding;
import com.liemi.basemall.databinding.ItemMessageBinding;
import com.liemi.basemall.ui.personal.order.OrderDetailActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.netmi.baselibrary.data.Constant.LOAD_MORE;
import static com.netmi.baselibrary.data.Constant.ORDER_MPID;
import static com.netmi.baselibrary.data.Constant.PULL_REFRESH;

public class MessageActivity extends BaseActivity<ActivityMessageBinding> implements XRecyclerView.LoadingListener {
    private int mTotalItems = 0;//总条数
    private int mPages = 10;//每次加载10条数据
    private int mLoadItem = 0;//当前加载的总条数
    private int mLoadPage = 0;//当前从第几页开始加载
    //默认刷新
    protected int LOADING_TYPE = PULL_REFRESH;
    private BaseRViewAdapter<UserNoticeEntity.NoticeDetailsEntity, BaseViewHolder> adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_message;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("消息");
        getRightSetting().setText("全部已读");
        xRecyclerView = mBinding.xrvMessage;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<UserNoticeEntity.NoticeDetailsEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_message;
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
                        switch (items.get(position).getLink_type()){
                            case 1:
                                //跳转
                                Bundle bundle1 = new Bundle();
                                bundle1.putString(MessageDetailForTextActivity.TITLE,items.get(position).getTitle());
                                bundle1.putString(MessageDetailForTextActivity.CONTENT,items.get(position).getRemark());
                                JumpUtil.overlay(getContext(),MessageDetailForTextActivity.class,bundle1);
                                break;
                            case 2:
                                //跳转连接
                                Intent linkIntent = new Intent(MessageActivity.this,BaseWebviewActivity.class);
                                linkIntent.putExtra(BaseWebviewActivity.WEBVIEW_TITLE,items.get(position).getTitle());
                                linkIntent.putExtra(BaseWebviewActivity.WEBVIEW_CONTENT,items.get(position).getParam());
                                linkIntent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE,BaseWebviewActivity.WEBVIEW_TYPE_URL);
                                startActivity(linkIntent);
                                break;
                            case 3:
                                //跳转富文本
                                Intent textIntent = new Intent(MessageActivity.this,BaseWebviewActivity.class);
                                textIntent.putExtra(BaseWebviewActivity.WEBVIEW_TITLE,items.get(position).getTitle());
                                textIntent.putExtra(BaseWebviewActivity.WEBVIEW_CONTENT,items.get(position).getContent());
                                textIntent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE,BaseWebviewActivity.WEBVIEW_TYPE_CONTENT);
                                startActivity(textIntent);
                                break;
                            case 4:
                                //跳转订单
                                Bundle bundle = new Bundle();
                                bundle.putString(ORDER_MPID,items.get(position).getParam());
                                JumpUtil.overlay(MessageActivity.this,OrderDetailActivity.class,bundle);
                                break;
                            case 5:
                                //跳转来样定制

                                break;
                        }
                        //设置已读
                        doNoticeRead(items.get(position).getNotice_id(),position);
                    }
                };
            }


        });


    }


    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if(view.getId() == R.id.tv_setting){
            doReadAll();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        xRecyclerView.refresh();
    }

    @Override
    public void onRefresh() {
        mLoadPage = 0;
        mLoadItem = 0;
        mTotalItems = 10;
        LOADING_TYPE = PULL_REFRESH;
        doListData();
    }

    @Override
    public void onLoadMore() {
        mLoadPage++;
        LOADING_TYPE = LOAD_MORE;
        doListData();

    }

    private void showData(UserNoticeEntity userNoticeEntity) {
        mTotalItems = userNoticeEntity.getTotal_pages();
        mLoadItem +=  userNoticeEntity.getList()== null ? 0 : userNoticeEntity.getList().size();
        if (LOADING_TYPE == PULL_REFRESH) {
            adapter.setData(userNoticeEntity.getList());
            adapter.notifyDataSetChanged();
            xRecyclerView.refreshComplete();
        }
        if (LOADING_TYPE == LOAD_MORE) {
            adapter.insert(adapter.getItemCount(), userNoticeEntity.getList());
            adapter.notifyDataSetChanged();
            xRecyclerView.loadMoreComplete();
        }

        if(mLoadItem >= mTotalItems){
            xRecyclerView.setLoadingMoreEnabled(false);
        }else{
            xRecyclerView.setLoadingMoreEnabled(true);
        }
    }

    //请求公告列表
    private void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doUserNotices(new String[]{"1"},null,mLoadPage,mPages)
                .compose(RxSchedulers.<BaseData<UserNoticeEntity>>compose())
                .compose((this).<BaseData<UserNoticeEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserNoticeEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserNoticeEntity> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        getActivity().hideProgress();

                    }
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());

                    }
                });

    }

    //请求公告已读
    private void doNoticeRead(int noticeId, final int position){
        RetrofitApiFactory.createApi(MineApi.class)
                .doSetNoticeRead(noticeId)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        adapter.getItem(position).setIs_read(1);
                        adapter.notifyPosition(position);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    protected void onError(ApiException ex) {

                    }
                });
    }
    //设置全部已读
    private void doReadAll(){
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doNoticeAllRead("defaultData")
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("设置成功");
                        doListData();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });
    }
}
