package com.netmi.workerbusiness.ui.message;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MessApi;
import com.netmi.workerbusiness.data.entity.mess.PublicNoticeEntity;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.ActivityPublicNoticeBinding;
import com.netmi.workerbusiness.databinding.ItemOfficialPushBinding;
import com.netmi.workerbusiness.ui.home.offline.OfflineOrderDetailActivity;
import com.netmi.workerbusiness.ui.home.online.LineOrderDetailActivity;
import com.netmi.workerbusiness.ui.mine.StoreCreditScoreActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.annotations.NonNull;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_URL;


public class PublicNoticeActivity extends BaseXRecyclerActivity<ActivityPublicNoticeBinding, PublicNoticeEntity> {

    // type_arr  1 平台公告（改为3）  2订单通知  5 规则中心
    private int type;
    private String title;

    @Override
    protected int getContentView() {
        return R.layout.activity_public_notice;
    }

    @Override
    protected void initUI() {
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        if (type == 3) {
            title = "平台公告";
        } else if (type == 2) {
            title = "订单通知";
        } else if (type == 5) {
            title = "规则中心";
        }
        getTvTitle().setText(title);
        initRecyclerView();
    }

    @Override
    protected void initDefault() {
        fullScreen(true);
    }

    private void initRecyclerView() {

        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);


        adapter = new BaseRViewAdapter<PublicNoticeEntity, BaseViewHolder>(getContext()) {

            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_official_push;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<PublicNoticeEntity>(binding) {
                    @Override
                    public void bindData(PublicNoticeEntity item) {
                        PublicNoticeEntity entity = items.get(position);
                        super.bindData(item);//不能删
                        ItemOfficialPushBinding itemOfficialPushBinding = (ItemOfficialPushBinding) binding;
//                        if (entity.getIs_read().equals("1")) {
//                            itemOfficialPushBinding.ivRedMine.setVisibility(View.GONE);
//                        } else {
//                            itemOfficialPushBinding.ivRedMine.setVisibility(View.VISIBLE);
//                        }
                        //type_arr  1 平台公告  2订单通知  5 系统通知
                        if (type == 3) {
                            itemOfficialPushBinding.ivLogo.setImageResource(R.mipmap.ic_app_notice);
                            itemOfficialPushBinding.tvRightSub.setVisibility(View.GONE);
                            itemOfficialPushBinding.tvLeftTitle.setText(entity.getTitle());
                            itemOfficialPushBinding.tvRemark.setText(entity.getRemark());
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) itemOfficialPushBinding.tvClickToView.getLayoutParams();
                            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            itemOfficialPushBinding.tvClickToView.setLayoutParams(lp);

                        } else if (type == 2) {
                            itemOfficialPushBinding.ivLogo.setImageResource(R.mipmap.ic_order_notice);
                            itemOfficialPushBinding.tvRemark.setText(entity.getTitle());
                        } else if (type == 5) {
                            itemOfficialPushBinding.ivLogo.setImageResource(R.mipmap.ic_system_notice);
                            itemOfficialPushBinding.tvRightSub.setVisibility(View.GONE);
                            itemOfficialPushBinding.tvLeftTitle.setText(entity.getTitle());
                            itemOfficialPushBinding.tvRemark.setText(entity.getRemark());
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) itemOfficialPushBinding.tvClickToView.getLayoutParams();
                            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            itemOfficialPushBinding.tvClickToView.setLayoutParams(lp);
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        PublicNoticeEntity entity = items.get(position);
                        read(entity.getNotice_id());
                        super.doClick(view);
                        if (type == 3) {
                            // "link_type": "2",//公告展现方式 1：无点击效果 2：点击跳转链接 3：点击跳转富文本 4：点击跳转订单详情 5：跳转退款订单详情 6:跳转商品详情
                            Bundle bundle = new Bundle();
                            if (entity.getLink_type().equals("2")) {
                                bundle.putString(WEBVIEW_TITLE, entity.getTitle());
                                bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_URL);
                                bundle.putString(WEBVIEW_CONTENT, AppUtils.getBaseApi() + entity.getParam());
                                JumpUtil.overlay(context, BaseWebviewActivity.class, bundle);
                            } else if (entity.getLink_type().equals("3")) {
                                bundle.putString(WEBVIEW_TITLE, entity.getTitle());
                                bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_CONTENT);
                                bundle.putString(WEBVIEW_CONTENT, entity.getContent());
                                JumpUtil.overlay(context, BaseWebviewActivity.class, bundle);
                            }
//                            else if (entity.getLink_type().equals("6")) {
//                                doGetShopInfo();
//                            }
                            xRecyclerView.refresh();
                        } else if (type == 2) {
                            if (entity.getLink_type().equals("4")) {
                                Bundle bundle = new Bundle();
                                if (entity.getOrder_type().equals("11")) {//* order_type :  "11", //0表示普通消息；11表示线下订单消息
                                    bundle.putString(JumpUtil.ID, entity.getParam());
                                    JumpUtil.overlay(getActivity(), OfflineOrderDetailActivity.class, bundle);
                                } else {
                                    bundle.putInt(LineOrderDetailActivity.ORDER_DETAILS_ID, Integer.valueOf(entity.getParam()));
                                    JumpUtil.overlay(getActivity(), LineOrderDetailActivity.class, bundle);
                                }
                            }
                            xRecyclerView.refresh();
                        } else if (type == 5) {
                            Bundle bundle = new Bundle();
                            if (entity.getLink_type().equals("6")) {
                                doGetShopInfo();
                            } else {
                                bundle.putString(WEBVIEW_TITLE, entity.getTitle());
                                bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_CONTENT);
                                bundle.putString(WEBVIEW_CONTENT, entity.getContent());
                                JumpUtil.overlay(context, BaseWebviewActivity.class, bundle);
                            }
                            xRecyclerView.refresh();
                        }

                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }


    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MessApi.class)
                .getNotice(new Integer[]{type}, String.valueOf(PageUtil.toPage(startPage)), "10")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<PublicNoticeEntity>>>() {
                    @Override
                    public void onSuccess(@NonNull BaseData<PageEntity<PublicNoticeEntity>> data) {
                        showData(data.getData());
                    }
                });
    }

    private void read(String notice_id) {
        RetrofitApiFactory.createApi(MessApi.class)
                .read(notice_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(@NonNull BaseData data) {
                        xRecyclerView.refresh();
                    }
                });
    }

    private int credit_score;

    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(com.netmi.workerbusiness.data.api.MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        credit_score = data.getData().getScore();
                        Bundle bundle = new Bundle();
                        bundle.putInt(JumpUtil.VALUE, credit_score);
                        JumpUtil.overlay(getContext(), StoreCreditScoreActivity.class, bundle);
                    }
                });
    }
}
