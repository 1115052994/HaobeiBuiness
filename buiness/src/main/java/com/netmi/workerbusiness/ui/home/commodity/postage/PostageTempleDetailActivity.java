package com.netmi.workerbusiness.ui.home.commodity.postage;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.PostageApi;
import com.netmi.workerbusiness.data.entity.home.postage.PostageDetailEntity;
import com.netmi.workerbusiness.data.entity.home.postage.PostageTempleDetailListEntity;
import com.netmi.workerbusiness.data.entity.home.postage.PostageTempleItemEntity;
import com.netmi.workerbusiness.data.entity.home.postage.UpdateFreightTempleCommand;
import com.netmi.workerbusiness.databinding.ActivitySelectListBottomBtnBinding;
import com.netmi.workerbusiness.databinding.ItemAddPostageTempleBinding;
import com.netmi.workerbusiness.databinding.ItemPostageTempleNameBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class PostageTempleDetailActivity extends BaseXRecyclerActivity<ActivitySelectListBottomBtnBinding, PostageDetailEntity.RegionConfBean> {

    public static final String FG_ID = "fg_id";
    public static final String TEMPLE_NAME = "temple_name";


    List<PostageDetailEntity.RegionConfBean> postageDetailEntities = new ArrayList<>();
    ItemPostageTempleNameBinding topBinding;

    @Override
    protected int getContentView() {
        return R.layout.activity_select_list_bottom_btn;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("编辑模板");
        getRightSetting().setText("保存");
        mBinding.btnAdd.setText("添加地区");
        topBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.item_postage_temple_name, mBinding.llContent, false);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(TEMPLE_NAME))) {
            ((EditText) topBinding.getRoot().findViewById(R.id.et_postage_temple_name)).setText(getIntent().getStringExtra(TEMPLE_NAME));
        }

        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.addHeaderView(topBinding.getRoot());
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<PostageDetailEntity.RegionConfBean, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_add_postage_temple;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);

                        final PostageDetailEntity.RegionConfBean regionConfBean = getItem(position);
                        if (TextUtils.equals(regionConfBean.getRegion_name(), "全国默认地区")) {
                            ((ItemAddPostageTempleBinding) (getBinding())).tvAddAreaInfo.setVisibility(View.GONE);
                            ((ItemAddPostageTempleBinding) (getBinding())).ivEdit.setVisibility(View.GONE);
                            ((ItemAddPostageTempleBinding) (getBinding())).ivDelete.setVisibility(View.GONE);
                        } else {
                            ((ItemAddPostageTempleBinding) (getBinding())).tvAddAreaInfo.setVisibility(View.VISIBLE);
                            ((ItemAddPostageTempleBinding) (getBinding())).tvRegionName.setText("指定地区");
                            if (!TextUtils.isEmpty((getItem(position)).getRegion_name())) {
                                ((ItemAddPostageTempleBinding) (getBinding())).tvAddAreaInfo.setText((getItem(position)).getRegion_name());
                            }
                            ((ItemAddPostageTempleBinding) (getBinding())).ivEdit.setVisibility(View.VISIBLE);
                            ((ItemAddPostageTempleBinding) (getBinding())).ivDelete.setVisibility(View.VISIBLE);
                        }
                        ((ItemAddPostageTempleBinding) getBinding()).etFirstPiece.setText(regionConfBean.getFirst_item());
                        ((ItemAddPostageTempleBinding) getBinding()).etFirstFreight.setText(regionConfBean.getFirst_money());
                        ((ItemAddPostageTempleBinding) getBinding()).etContinuePiece.setText(regionConfBean.getAdd_item());
                        ((ItemAddPostageTempleBinding) getBinding()).etContinueFreight.setText(regionConfBean.getAdd_money());

                        new InputListenView(
//                                mBinding.btnAdd
                                mBinding.view,
                                ((ItemAddPostageTempleBinding) (getBinding())).etFirstPiece,
                                ((ItemAddPostageTempleBinding) (getBinding())).etFirstFreight,
                                ((ItemAddPostageTempleBinding) (getBinding())).etContinuePiece,
                                ((ItemAddPostageTempleBinding) (getBinding())).etContinueFreight
                        ) {

                            @Override
                            public void afterTextChanged(Editable editable) {
                                super.afterTextChanged(editable);
                                postageDetailEntities.get(position).setFirst_item(((ItemAddPostageTempleBinding) (getBinding())).etFirstPiece.getText().toString());
                                postageDetailEntities.get(position).setFirst_money(((ItemAddPostageTempleBinding) (getBinding())).etFirstFreight.getText().toString());
                                if (((ItemAddPostageTempleBinding) (getBinding())).etContinuePiece.getText().toString().equals("0")
                                        || ((ItemAddPostageTempleBinding) (getBinding())).etContinueFreight.getText().toString().equals("0")) {
                                    showError("续件和运费不能为0");
                                } else {
                                    postageDetailEntities.get(position).setAdd_item(((ItemAddPostageTempleBinding) (getBinding())).etContinuePiece.getText().toString());
                                    postageDetailEntities.get(position).setAdd_money(((ItemAddPostageTempleBinding) (getBinding())).etContinueFreight.getText().toString());
                                }
//                                postageDetailEntities.get(position).setFirst_item(((ItemAddPostageTempleBinding) (getBinding())).etFirstPiece.getText().toString());
//                                postageDetailEntities.get(position).setFirst_money(((ItemAddPostageTempleBinding) (getBinding())).etFirstFreight.getText().toString());
//                                postageDetailEntities.get(position).setAdd_item(((ItemAddPostageTempleBinding) (getBinding())).etContinuePiece.getText().toString());
//                                postageDetailEntities.get(position).setAdd_money(((ItemAddPostageTempleBinding) (getBinding())).etContinueFreight.getText().toString());
                                postageDetailEntities.get(position).setT_id(getIntent().getStringExtra(FG_ID));
                            }
                        };
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int id = view.getId();
                        if (id == R.id.iv_edit) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(AddAreaActivity.UPDATE_POSITION, position);

                            JumpUtil.startForResult(PostageTempleDetailActivity.this, AddAreaActivity.class, AddAreaActivity.SELECT_AREA, bundle);
                        } else if (id == R.id.iv_delete) {//删除子模板
                            xRecyclerView.refresh();
                            doDeleteChildTemple(postageDetailEntities.get(position).getTc_id(), position);
                        }
                    }
                };
            }
        });

    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.btn_add) {
            JumpUtil.startForResult(this, AddAreaActivity.class, AddAreaActivity.SELECT_AREA, null);
        } else if (id == R.id.tv_setting) {
            if (TextUtils.isEmpty(((EditText) topBinding.getRoot().findViewById(R.id.et_postage_temple_name)).getText().toString())) {
                ToastUtils.showShort("请输入模板名称");
                return;
            } else {
                for (int i = 0; i < postageDetailEntities.size(); i++) {
                    if (TextUtils.isEmpty(postageDetailEntities.get(i).getFirst_item())) {
                        ToastUtils.showShort("指定地区" + postageDetailEntities.get(i).getRegion_name() + "首件个数不能为零");
                        return;
                    } else if (TextUtils.isEmpty(postageDetailEntities.get(i).getFirst_money())) {
                        ToastUtils.showShort("指定地区" + postageDetailEntities.get(i).getRegion_name() + "首件运费不能为零");
                        return;
                    } else if (TextUtils.isEmpty(postageDetailEntities.get(i).getAdd_item())) {
                        ToastUtils.showShort("指定地区" + postageDetailEntities.get(i).getRegion_name() + "续件个数不能为零");
                        return;
                    } else if (TextUtils.isEmpty(postageDetailEntities.get(i).getAdd_money())) {
                        ToastUtils.showShort("指定地区" + postageDetailEntities.get(i).getRegion_name() + "续件运费不能为零");
                        return;
                    }
                }
            }
            UpdateFreightTempleCommand updateFreightTempleCommand = new UpdateFreightTempleCommand();
            String template_group_name = ((EditText) topBinding.getRoot().findViewById(R.id.et_postage_temple_name)).getText().toString();
            updateFreightTempleCommand.setTemplate_name(template_group_name);
            updateFreightTempleCommand.setT_id(getIntent().getStringExtra(FG_ID));
            List<PostageDetailEntity.RegionConfBean> template_list = new ArrayList<>();
            for (int i = 0; i < postageDetailEntities.size(); i++) {
                template_list.add((postageDetailEntities.get(i)));
            }
            updateFreightTempleCommand.setRegion_conf(template_list);
            doUpdateFreightTemple(updateFreightTempleCommand);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AddAreaActivity.SELECT_AREA) {
                int update_position = data.getIntExtra(AddAreaActivity.UPDATE_POSITION, -1);
                if (update_position == -1) {
                    PostageDetailEntity.RegionConfBean postageDetailEntity = new PostageDetailEntity.RegionConfBean();
                    postageDetailEntity.setRegion_name(data.getStringExtra(AddAreaActivity.AREA_STR));
                    postageDetailEntity.setRegion(data.getStringArrayListExtra(AddAreaActivity.AREA_ID_LIST));
                    adapter.insert(postageDetailEntity);
                } else {
                    postageDetailEntities.get(update_position).setRegion_name(data.getStringExtra(AddAreaActivity.AREA_STR));
                    postageDetailEntities.get(update_position).setRegion(data.getStringArrayListExtra(AddAreaActivity.AREA_ID_LIST));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    //修改
    public void doUpdateFreightTemple(UpdateFreightTempleCommand updateFreightTempleCommand) {
        RetrofitApiFactory.createApi(PostageApi.class)
                .getUpdateFreight(updateFreightTempleCommand)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            ToastUtils.showShort("修改成功");
                            finish();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    //删除模板
    public void doDeleteChildTemple(String shop_freight_id, final int position) {
        RetrofitApiFactory.createApi(PostageApi.class)
                .getDeleteFreight(shop_freight_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            postageDetailEntities.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }

    //详情内容
    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(PostageApi.class)
                .getPostageDetail(getIntent().getStringExtra(FG_ID))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PostageDetailEntity>>() {
                    @Override
                    public void onSuccess(BaseData<PostageDetailEntity> data) {
                        adapter.setData(data.getData().getRegion_conf());
                        postageDetailEntities = adapter.getItems();
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

    //    @Override
//    public void showData(PageEntity<PostageDetailEntity> pageEntity) {
//        if (adapter == null) {
//            showError("请先初始化适配器");
//            return;
//        }
//        if (LOADING_TYPE == Constant.PULL_REFRESH) {
//            if (pageEntity.getSecond_category() != null && !pageEntity.getSecond_category().isEmpty()) {
//                xRecyclerView.setLoadingMoreEnabled(loadMoreEnabled);
//            }
//            adapter.setData(pageEntity.getSecond_category());
//            postageTempleDetailListEntities = adapter.getItems();
//        } else if (LOADING_TYPE == Constant.LOAD_MORE) {
//            if (pageEntity.getSecond_category() != null && !pageEntity.getSecond_category().isEmpty()) {
//                adapter.insert(adapter.getItemCount(), pageEntity.getSecond_category());
//                postageTempleDetailListEntities = adapter.getItems();
//            }
//        }
//        totalCount = pageEntity.getIs_total();
//        startPage = adapter.getItemCount();
//    }
////
    @Override
    public void hideProgress() {
        super.hideProgress();
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            xRecyclerView.refreshComplete();
        } else {
            xRecyclerView.loadMoreComplete();
        }
        if (adapter != null && adapter.getItemCount() <= 0) {
//            mBinding.btnAdd.setVisibility(View.GONE);
            xRecyclerView.setNoMore(false);
        } else if (startPage >= totalCount && loadMoreEnabled) {
//            mBinding.btnAdd.setVisibility(View.VISIBLE);
            xRecyclerView.setNoMore(true);
        }
    }
}
