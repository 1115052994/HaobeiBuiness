package com.liemi.basemall.ui.store.comment;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.comment.CommentEntity;
import com.liemi.basemall.databinding.FragmentXrecyclerviewBinding;
import com.liemi.basemall.databinding.ItemCommentBinding;
import com.liemi.basemall.widget.MyRecyclerView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.store.good.BaseGoodsDetailedActivity.ITEM_ID;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/25 15:43
 * 修改备注：
 */
public class CommentFragment extends BaseFragment<FragmentXrecyclerviewBinding> implements XRecyclerView.LoadingListener {

    private static final String COMMENT_FLAG = "comment_flag";

    /**
     * 页数
     */
    private int startPage = 0;

    /**
     * 总条数
     */
    private int totalCount;

    /**
     * 列表加载数据方式
     */
    private int LOADING_TYPE = -1;

    private BaseRViewAdapter<CommentEntity, BaseViewHolder> adapter;

    private String itemId, flag;

    @Override
    protected int getContentView() {
        return R.layout.fragment_xrecyclerview;
    }

    public static CommentFragment newInstance(String itemId, String flag) {
        CommentFragment f = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, itemId);
        bundle.putString(COMMENT_FLAG, flag);
        f.setArguments(bundle);
        return f;
    }

    @Override
    protected void initUI() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<CommentEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_comment;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<CommentEntity>(binding) {
                    @Override
                    public void bindData(CommentEntity item) {
                        getBinding().rbStarServer.setStar(item.getLevel(), false);
                        MyRecyclerView rvPic = getBinding().rvImg;
                        rvPic.setNestedScrollingEnabled(false);
                        if (getItem(position).getMeCommetImgs() != null
                                && !getItem(position).getMeCommetImgs().isEmpty()) {
                            rvPic.setLayoutManager(new GridLayoutManager(context, 3));
                            final BaseRViewAdapter<String, BaseViewHolder> imgAdapter = new BaseRViewAdapter<String, BaseViewHolder>(context) {

                                @Override
                                public int layoutResId(int position) {
                                    return R.layout.item_multi_pic_show;
                                }

                                @Override
                                public BaseViewHolder holderInstance(ViewDataBinding binding) {
                                    return new BaseViewHolder(binding) {

                                        @Override
                                        public void doClick(View view) {
                                            super.doClick(view);
                                            //打开预览
                                            Intent intentPreview = new Intent(getContext(), ImagePreviewDelActivity.class);
                                            intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, ImageItemUtil.String2ImageItem(getItems()));
                                            intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                                            intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                                            intentPreview.putExtra(ImagePicker.EXTRA_PREVIEW_HIDE_DEL, true);
                                            startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                                        }
                                    };
                                }
                            };

                            rvPic.setAdapter(imgAdapter);
                            imgAdapter.setData(getItem(position).getMeCommetImgs());
                        }
                        super.bindData(item);
                    }

                    @Override
                    public ItemCommentBinding getBinding() {
                        return (ItemCommentBinding) super.getBinding();
                    }
                };
            }
        });

        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    protected void initData() {
        itemId = getArguments().getString(ITEM_ID);
        flag = getArguments().getString(COMMENT_FLAG);
        xRecyclerView.refresh();
    }

    //只要设置一遍标题数量即可
    private boolean setTitleCount;

    public void showData(PageEntity<CommentEntity> pageEntity) {
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                xRecyclerView.setLoadingMoreEnabled(true);
            }
            adapter.setData(pageEntity.getList());
        } else if (LOADING_TYPE == Constant.LOAD_MORE) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                adapter.insert(adapter.getItemCount(), pageEntity.getList());
            }
        }
        totalCount = pageEntity.getTotal_pages();
        startPage = adapter.getItemCount();

        if (getActivity() instanceof CommentAllActivity
                && !setTitleCount) {
            ((CommentAllActivity) getActivity()).setTitleCount(flag, totalCount);
            setTitleCount = true;
        }
    }

    @Override
    public void onRefresh() {
        startPage = 0;
        LOADING_TYPE = Constant.PULL_REFRESH;
        xRecyclerView.setLoadingMoreEnabled(false);
        doListComment();
    }

    @Override
    public void onLoadMore() {
        LOADING_TYPE = Constant.LOAD_MORE;
        doListComment();
    }

    @Override
    public void hideProgress() {
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            xRecyclerView.refreshComplete();
        } else {
            xRecyclerView.loadMoreComplete();
        }
        if (startPage >= totalCount) {
            xRecyclerView.setNoMore(true);
        }
    }

    private void doListComment() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listComment(PageUtil.toPage(startPage), Constant.PAGE_ROWS, itemId, "0".equals(flag) ? null : flag)
                .compose(RxSchedulers.<BaseData<PageEntity<CommentEntity>>>compose())
                .compose((this).<BaseData<PageEntity<CommentEntity>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData<PageEntity<CommentEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<PageEntity<CommentEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


}
