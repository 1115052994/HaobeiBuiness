package com.netmi.workerbusiness.ui.home.commodity.postage;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.PostageApi;
import com.netmi.workerbusiness.data.entity.home.postage.AddFreightTempleCommand;
import com.netmi.workerbusiness.data.entity.home.postage.PostageTempleItemEntity;
import com.netmi.workerbusiness.data.entity.home.postage.TempleGroupNameEntity;
import com.netmi.workerbusiness.databinding.ActivityAddPostageBinding;
import com.netmi.workerbusiness.databinding.ItemAddPostageTempleBinding;
import com.netmi.workerbusiness.databinding.ItemPostageTempleNameBinding;
import com.netmi.workerbusiness.utils.ButtonUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class AddPostageActivity extends BaseActivity<ActivityAddPostageBinding> {

    private BaseRViewAdapter<BaseEntity, BaseViewHolder> adapter;
    List<BaseEntity> baseEntities = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_add_postage;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("添加模板");
        getRightSetting().setText("保存");
        getRightSetting().setTextColor(getResources().getColor(R.color.ff333333));
        RecyclerView recyclerView = mBinding.rvData;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new BaseRViewAdapter<BaseEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType == 1) {
                    return R.layout.item_postage_temple_name;
                } else {
                    return R.layout.item_add_postage_temple;
                }
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        if (getBinding() instanceof ItemAddPostageTempleBinding) {
                            if (TextUtils.equals(((PostageTempleItemEntity) getItem(position)).getRegion_name(), "全国默认地区")) {
                                ((ItemAddPostageTempleBinding) (getBinding())).tvAddAreaInfo.setVisibility(View.GONE);
                                ((ItemAddPostageTempleBinding) (getBinding())).ivEdit.setVisibility(View.GONE);
                                ((ItemAddPostageTempleBinding) (getBinding())).ivDelete.setVisibility(View.GONE);
                            } else {
                                ((ItemAddPostageTempleBinding) (getBinding())).tvAddAreaInfo.setVisibility(View.VISIBLE);
                                ((ItemAddPostageTempleBinding) (getBinding())).tvRegionName.setText("指定地区");
                                if (!TextUtils.isEmpty(((PostageTempleItemEntity) getItem(position)).getRegion_name())) {
                                    ((ItemAddPostageTempleBinding) (getBinding())).tvAddAreaInfo.setText(((PostageTempleItemEntity) getItem(position)).getRegion_name());
                                }
                                ((ItemAddPostageTempleBinding) (getBinding())).ivEdit.setVisibility(View.VISIBLE);
                                ((ItemAddPostageTempleBinding) (getBinding())).ivDelete.setVisibility(View.VISIBLE);
                            }
                            new InputListenView(mBinding.btnBottom, ((ItemAddPostageTempleBinding) (getBinding())).etFirstPiece,
                                    ((ItemAddPostageTempleBinding) (getBinding())).etFirstFreight,
                                    ((ItemAddPostageTempleBinding) (getBinding())).etContinuePiece,
                                    ((ItemAddPostageTempleBinding) (getBinding())).etContinueFreight) {
                                @Override
                                public void afterTextChanged(Editable editable) {
                                    super.afterTextChanged(editable);
//                                    ((PostageTempleItemEntity) baseEntities.get(position)).setFirst_item(((ItemAddPostageTempleBinding) (getBinding())).etFirstPiece.getText().toString());
//                                    ((PostageTempleItemEntity) baseEntities.get(position)).setFirst_money(((ItemAddPostageTempleBinding) (getBinding())).etFirstFreight.getText().toString());
//                                    if (((ItemAddPostageTempleBinding) (getBinding())).etContinuePiece.getText().toString().equals("0")
//                                            || ((ItemAddPostageTempleBinding) (getBinding())).etContinueFreight.getText().toString().equals("0")) {
//                                        showError("续件和运费不能为0");
//                                    } else {
//                                        ((PostageTempleItemEntity) baseEntities.get(position)).setAdd_item(((ItemAddPostageTempleBinding) (getBinding())).etContinuePiece.getText().toString());
//                                        ((PostageTempleItemEntity) baseEntities.get(position)).setAdd_money(((ItemAddPostageTempleBinding) (getBinding())).etContinueFreight.getText().toString());
//                                    }
                                    ((PostageTempleItemEntity) baseEntities.get(position)).setFirst_item(((ItemAddPostageTempleBinding) (getBinding())).etFirstPiece.getText().toString());
                                    ((PostageTempleItemEntity) baseEntities.get(position)).setFirst_money(((ItemAddPostageTempleBinding) (getBinding())).etFirstFreight.getText().toString());
                                    ((PostageTempleItemEntity) baseEntities.get(position)).setAdd_item(((ItemAddPostageTempleBinding) (getBinding())).etContinuePiece.getText().toString());
                                    ((PostageTempleItemEntity) baseEntities.get(position)).setAdd_money(((ItemAddPostageTempleBinding) (getBinding())).etContinueFreight.getText().toString());
                                }
                            };
                        } else if (getBinding() instanceof ItemPostageTempleNameBinding) {
                            new InputListenView(mBinding.btnBottom, ((ItemPostageTempleNameBinding) (getBinding())).etPostageTempleName) {
                                @Override
                                public void afterTextChanged(Editable editable) {
                                    super.afterTextChanged(editable);
                                    ((TempleGroupNameEntity) baseEntities.get(position)).setTemplate_group_name(((ItemPostageTempleNameBinding) (getBinding())).etPostageTempleName.getText().toString());
                                }
                            };
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int id = view.getId();
                        if (id == R.id.iv_edit) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(AddAreaActivity.UPDATE_POSITION, position);
                            JumpUtil.startForResult(AddPostageActivity.this, AddAreaActivity.class, AddAreaActivity.SELECT_AREA, bundle);
                        } else if (id == R.id.iv_delete) {//删除子模板
                            baseEntities.remove(position);
                            adapter.remove(position);
                        }
                    }
                };
            }


            @Override
            public int getItemViewType(int position) {
                if (adapter.getItem(position) instanceof TempleGroupNameEntity) {
                    return 1;
                } else if (adapter.getItem(position) instanceof PostageTempleItemEntity) {
                    return 2;
                }
                return super.getItemViewType(position);
            }
        });
    }


    @Override
    protected void initData() {
        baseEntities.add(new TempleGroupNameEntity());
        PostageTempleItemEntity postageTempleItemEntity = new PostageTempleItemEntity();
        postageTempleItemEntity.setRegion_name("全国默认地区");
        List<String> list = new ArrayList<>();
        list.add("0");
        postageTempleItemEntity.setRegion(list);
        baseEntities.add(postageTempleItemEntity);
        adapter.setData(baseEntities);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.btn_bottom) {
            JumpUtil.startForResult(this, AddAreaActivity.class, AddAreaActivity.SELECT_AREA, null);
        } else if (id == R.id.tv_setting) {
            if (ButtonUtil.isFastClick()) { //防止快速重复点击
                if (TextUtils.isEmpty(((TempleGroupNameEntity) baseEntities.get(0)).getTemplate_group_name())) {
                    ToastUtils.showShort("请输入模板名称");
                    return;
                } else {
                    for (int i = 1; i < baseEntities.size(); i++) {
                        if (TextUtils.isEmpty(((PostageTempleItemEntity) baseEntities.get(i)).getFirst_item())) {
                            ToastUtils.showShort("指定地区" + ((PostageTempleItemEntity) baseEntities.get(i)).getRegion_name() + "首件个数不能为空");
                            return;
                        } else if (TextUtils.isEmpty(((PostageTempleItemEntity) baseEntities.get(i)).getFirst_money())) {
                            ToastUtils.showShort("指定地区" + ((PostageTempleItemEntity) baseEntities.get(i)).getRegion_name() + "首件运费不能为空");
                            return;
                        } else if (TextUtils.isEmpty(((PostageTempleItemEntity) baseEntities.get(i)).getAdd_item())) {
                            ToastUtils.showShort("指定地区" + ((PostageTempleItemEntity) baseEntities.get(i)).getRegion_name() + "续件个数不能为空");
                            return;
                        } else if (TextUtils.isEmpty(((PostageTempleItemEntity) baseEntities.get(i)).getAdd_money())) {
                            ToastUtils.showShort("指定地区" + ((PostageTempleItemEntity) baseEntities.get(i)).getRegion_name() + "续件运费不能为空");
                            return;
                        }
                    }
                }
                AddFreightTempleCommand addFreightTempleCommand = new AddFreightTempleCommand();
                String template_group_name = ((TempleGroupNameEntity) baseEntities.get(0)).getTemplate_group_name();
                addFreightTempleCommand.setTemplate_name(template_group_name);
                List<PostageTempleItemEntity> template_list = new ArrayList<>();
                for (int i = 1; i < baseEntities.size(); i++) {
                    template_list.add(((PostageTempleItemEntity) baseEntities.get(i)));
                }
                addFreightTempleCommand.setRegion_conf(template_list);
                doCreateFreightTemple(addFreightTempleCommand);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AddAreaActivity.SELECT_AREA) {
                int update_position = data.getIntExtra(AddAreaActivity.UPDATE_POSITION, -1);
                if (update_position == -1) {
                    PostageTempleItemEntity postageTempleItemEntity = new PostageTempleItemEntity();
                    postageTempleItemEntity.setRegion_name(data.getStringExtra(AddAreaActivity.AREA_STR));
                    postageTempleItemEntity.setRegion(data.getStringArrayListExtra(AddAreaActivity.AREA_ID_LIST));
                    baseEntities.add(postageTempleItemEntity);
                    adapter.insert(postageTempleItemEntity);
                } else {
                    ((PostageTempleItemEntity) baseEntities.get(update_position)).setRegion_name(data.getStringExtra(AddAreaActivity.AREA_STR));
                    ((PostageTempleItemEntity) baseEntities.get(update_position)).setRegion(data.getStringArrayListExtra(AddAreaActivity.AREA_ID_LIST));
                    adapter.notifyDataSetChanged();
                }

            }
        }
    }


    //新建模板
    public void doCreateFreightTemple(AddFreightTempleCommand addFreightTempleCommod) {
        RetrofitApiFactory.createApi(PostageApi.class)
                .getAddFreight(addFreightTempleCommod)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            showError("创建成功");
                            finish();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }
}

