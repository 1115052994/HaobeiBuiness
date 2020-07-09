package com.liemi.basemall.ui.store.good;

import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.CategoryApi;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.comment.CommentEntity;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.liemi.basemall.data.event.GoodsCollectEvent;
import com.liemi.basemall.ui.shopcar.ShoppingActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.IntentUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.StatusBarUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.ConfirmDialog;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.data.entity.good.GoodsDetailedEntity.GOODS_ENTITY;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/9/21 10:57
 * 修改备注：
 */
public abstract class BaseGoodsDetailedActivity<T extends ViewDataBinding> extends BaseXRecyclerActivity<T, GoodsDetailedEntity> implements CompoundButton.OnCheckedChangeListener {

    public static final String ITEM_ID = "item_id";

    protected GoodsDetailedEntity goodEntity;

    protected abstract LinearLayout getLlTop();

    protected abstract LinearLayout getLlTopWhite();

    protected abstract MyXRecyclerView getXrvData();

    protected abstract CheckBox getCbCollect();

    @Override
    public void setBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
        StatusBarUtil.setImgTransparent(getActivity());
    }

    @Override
    protected void initUI() {
        xRecyclerView = getXrvData();
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int item = layoutManager.findFirstVisibleItemPosition();
                View itemView = layoutManager.findViewByPosition(item);
                if (itemView != null) {
                    int itemHeight = itemView.getHeight();
                    if (item == 1) {
                        float alpha = Math.abs(layoutManager.findViewByPosition(item).getTop()) / (float) itemHeight;
                        getLlTop().setAlpha(1 - alpha);
                        getLlTopWhite().setAlpha(alpha);
                    }
                }
            }

        });

        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    protected void initData() {
    }

    public void showData(GoodsDetailedEntity goodEntity) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int i = compoundButton.getId();
        if (i == R.id.cb_collect) {
            if (MApplication.getInstance().checkUserIsLogin()) {
                if (goodEntity != null) {
                    if (b) {
                        //收藏商品
                        if (goodEntity.getIs_collection() == 0) {//未收藏状态
                            doCollection();
                        }
                    } else {
                        //取消收藏商品
                        if (goodEntity.getIs_collection() == 1) {//收藏状态
                            doCollectionDel();
                        }
                    }
                }
            } else if (b) {
                compoundButton.setChecked(false);
            }

        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.iv_back || i == R.id.iv_back_white) {
            finish();

        } else if (i == R.id.iv_shop_cart || i == R.id.iv_shop_cart_white) {
            if (MApplication.getInstance().checkUserIsLogin()) {
                JumpUtil.overlay(getContext(), ShoppingActivity.class);
            }
        } else if (i == R.id.iv_share || i == R.id.iv_share_white) {
            if (MApplication.getInstance().checkUserIsLogin()) {
                if (goodEntity == null) {
                    ToastUtils.showShort("没有可以分享的商品内容");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(GOODS_ENTITY, goodEntity);
                JumpUtil.overlay(getContext(), ShareGoodActivity.class, bundle);
            }
        } else if (i == R.id.iv_server) {
            if (MApplication.getInstance().checkUserIsLogin()) {
                if (goodEntity == null || goodEntity.getShop() == null || TextUtils.isEmpty(goodEntity.getShop().getShop_tel())) {
                    ToastUtils.showShort("未配置客服电话！");
                    return;
                }
                new ConfirmDialog(this)
                        .setContentText("客服电话：" + goodEntity.getShop().getShop_tel())
                        .setConfirmText("拨打")
                        .setConfirmListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(IntentUtils.getDialIntent(goodEntity.getShop().getShop_tel()));
                            }
                        }).show();
            }
        }
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .getGoodsDetailed(getIntent().getStringExtra(ITEM_ID))
                .compose(RxSchedulers.<BaseData<GoodsDetailedEntity>>compose())
                .compose((this).<BaseData<GoodsDetailedEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<GoodsDetailedEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<GoodsDetailedEntity> data) {
                        if (data.getData() == null) {
                            ToastUtils.showShort("没有商品详情");
                            finish();
                            return;
                        }
                        if (data.getData().getStatus() != 5) {
                            //状态：1上传中 2上架待审核 3待定价 4待上架
                            // 5已上架 6 下架待审核 7已下架'
                            ToastUtils.showShort("商品未上架");
                            finish();
                            return;
                        }
                        goodEntity = data.getData();
                        doListComment();
                    }

                    @Override
                    public void onComplete() {
                        if (goodEntity == null) {
                            hideProgress();
                        }
                    }
                });
    }

    private void doListComment() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listComment(0, 1, goodEntity.getItem_id(), null)
                .compose(RxSchedulers.<BaseData<PageEntity<CommentEntity>>>compose())
                .compose((this).<BaseData<PageEntity<CommentEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<CommentEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showData(goodEntity);
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<PageEntity<CommentEntity>> data) {
                        if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                            CommentEntity firstComment = data.getData().getList().get(0);
                            firstComment.setSum_commet(data.getData().getTotal_pages());
                            goodEntity.setMeCommet(firstComment);
                        }
                    }

                    @Override
                    public void onComplete() {
                        showData(goodEntity);
                        hideProgress();
                    }
                });
    }

    private void doCollection() {
        if (goodEntity == null) {
            ToastUtils.showShort("请先等待商品加载完成。");
            return;
        }
        showProgress("");
        RetrofitApiFactory.createApi(CategoryApi.class)
                .goodCollection(goodEntity.getItem_id())
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        getCbCollect().setChecked(true);
                        getCbCollect().setText("已收藏");
                        getCbCollect().setTextColor(0xFFB52902);
                        goodEntity.setIs_collection(1);
                        EventBus.getDefault().post(new GoodsCollectEvent());
                        ToastUtils.showShort("收藏成功");
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doCollectionDel() {
        if (goodEntity == null) {
            ToastUtils.showShort("请先等待商品加载完成。");
            return;
        }
        showProgress("");
        RetrofitApiFactory.createApi(CategoryApi.class)
                .goodCollectionDel(new String[]{goodEntity.getItem_id()})
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        getCbCollect().setChecked(false);
                        getCbCollect().setText("收藏");
                        getCbCollect().setTextColor(0xFF878787);
                        goodEntity.setIs_collection(0);
                        EventBus.getDefault().post(new GoodsCollectEvent());
                        ToastUtils.showShort("您已取消收藏");
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


}
