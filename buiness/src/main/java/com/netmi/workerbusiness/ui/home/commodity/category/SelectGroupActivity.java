package com.netmi.workerbusiness.ui.home.commodity.category;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.entity.home.store.StoreDetailFirstListEntity;
import com.netmi.workerbusiness.databinding.ActivitySelectListBottomBtnBinding;
import com.netmi.workerbusiness.widget.AddGroupDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class SelectGroupActivity extends BaseXRecyclerActivity<ActivitySelectListBottomBtnBinding,StoreDetailFirstListEntity> {

    public static final String SHOP_ID = "shop_id";

    public static final String NAME_LIST = "name_list";

    public static final String ID_LIST = "id_list";

    private AddGroupDialog mDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_select_list_bottom_btn;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("选择店内分组");
        getRightSetting().setText("保存");
        mBinding.btnAdd.setText("添加分组");
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<StoreDetailFirstListEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_select_store_good_group;
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
                        if (view.getId() == R.id.cb_check) {
                            if (getItem(position).isCheck()) {
                                getItem(position).setCheck(false);
                            } else {
                                getItem(position).setCheck(true);
                            }
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
    protected void doListData() {
//        showProgress("");
//        RetrofitApiFactory.createApi(StoreApi.class)
//                .getGroupList(null, 0, Constant.ALL_PAGES)
//                .compose(RxSchedulers.<BaseData<PageEntity<StoreDetailFirstListEntity>>>compose())
//                .compose((this).<BaseData<PageEntity<StoreDetailFirstListEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new BaseObserver<BaseData<PageEntity<StoreDetailFirstListEntity>>>() {
//                    @Override
//                    protected void onError(ApiException ex) {
//                        showError(ex.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(@NonNull BaseData<PageEntity<StoreDetailFirstListEntity>> data) {
//                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
//                            if (data != null && data.getData() != null) {
//                                PageEntity<StoreDetailFirstListEntity> pageEntity = data.getData();
//                                if (getIntent().getStringArrayListExtra(ID_LIST) != null && getIntent().getStringArrayListExtra(ID_LIST).size() > 0){
//                                    List<String> ids =  getIntent().getStringArrayListExtra(ID_LIST);
//                                    for (String s : ids){
//                                        for (StoreDetailFirstListEntity entity : pageEntity.getSecond_category()){
//                                            if (TextUtils.equals(entity.getId(),s)){
//                                                pageEntity.getSecond_category().get(pageEntity.getSecond_category().indexOf(entity)).setCheck(true);
//                                            }
//                                        }
//                                    }
//                                }
//                                showData(pageEntity);
//                            }
//                        } else {
//                            showError(data.getErrmsg());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        hideProgress();
//                    }
//                });
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.btn_add) {
            showAddGroupDialog();
        } else if (id == R.id.tv_setting) {//保存
            boolean isSave = false;
            for (StoreDetailFirstListEntity entity : adapter.getItems()) {
                if (entity.isCheck()) {
                    isSave = true;
                    break;
                }
            }
            if (isSave) {
                ArrayList<String> nameList = new ArrayList<>();
                ArrayList<String> idList = new ArrayList<>();
                for (StoreDetailFirstListEntity entity : adapter.getItems()) {
                    if (entity.isCheck()) {
                        nameList.add(entity.getGroup_name());
                        idList.add(entity.getId());
                    }
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra(NAME_LIST, nameList);
                intent.putExtra(ID_LIST, idList);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtils.showShort("请选择分组");
            }
        }
    }

    public void showAddGroupDialog(){
        if (mDialog == null){
            mDialog = new AddGroupDialog(this);
        }
        if (!mDialog.isShowing()){
            mDialog.show();
        }
        mDialog.setClickConfirmListener(new AddGroupDialog.ClickConfirmListener() {
            @Override
            public void clickConfirm(String name) {
                doAddGrooup(name);
            }
        });
    }

    //添加分组
    public void doAddGrooup(String group_name) {
//        showProgress("");
//        RetrofitApiFactory.createApi(StoreApi.class)
//                .getAddGroup(group_name)
//                .compose(RxSchedulers.<BaseData>compose())
//                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new BaseObserver<BaseData>() {
//                    @Override
//                    protected void onError(ApiException ex) {
//                        showError(ex.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(@NonNull BaseData data) {
//                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
//                            ToastUtils.showShort("添加成功");
//                            xRecyclerView.refresh();
//                        } else {
//                            showError(data.getErrmsg());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        hideProgress();
//                    }
//                });
    }
}
