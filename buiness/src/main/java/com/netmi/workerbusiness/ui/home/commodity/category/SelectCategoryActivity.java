package com.netmi.workerbusiness.ui.home.commodity.category;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.netmi.workerbusiness.data.entity.home.category.GoodsOneCateEntity;
import com.netmi.workerbusiness.data.entity.home.category.GoodsTwoCateEntity;
import com.netmi.workerbusiness.databinding.ActivitySelectCategoryBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class SelectCategoryActivity extends BaseActivity<ActivitySelectCategoryBinding> {

    public static final String NAME_LIST = "name_list";
    public static final String ID_LIST = "id_list";

    private RecyclerView rvLeft, rvRight;

    private int leftCheckPosition = -1;

    private BaseRViewAdapter<GoodsOneCateEntity, BaseViewHolder> leftAdapter;

    private BaseRViewAdapter<GoodsOneCateEntity.SecondCategoryBean, BaseViewHolder> rightAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_select_category;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("选择类目");
        getRightSetting().setText("保存");
        mBinding.llBottom.setVisibility(View.GONE);
        rvLeft = mBinding.rvLeft;
        rvRight = mBinding.rvRight;
        rvLeft.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRight.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLeft.setAdapter(leftAdapter = new BaseRViewAdapter<GoodsOneCateEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_select_categroy_left;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        for (GoodsOneCateEntity entity : getItems()) {
                            if (position == getItems().indexOf(entity)) {
                                leftAdapter.getItem(position).setCheck(true);
                                leftCheckPosition = position;
                            } else {
                                leftAdapter.getItem(getItems().indexOf(entity)).setCheck(false);
                            }
                        }
                        leftAdapter.notifyDataSetChanged();
                        rightAdapter.setData(getItem(position).getSecond_category());
                    }
                };
            }
        });

        rvRight.setAdapter(rightAdapter = new BaseRViewAdapter<GoodsOneCateEntity.SecondCategoryBean, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_select_category_right;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (getItem(position).isCheck()) {
                            leftAdapter.getItem(leftCheckPosition).getSecond_category().get(position).setCheck(false);
                        } else {
                            leftAdapter.getItem(leftCheckPosition).getSecond_category().get(position).setCheck(true);
                        }
                        rightAdapter.notifyDataSetChanged();
                    }
                };
            }
        });

    }

    @Override
    protected void initData() {
        doListCategory();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_setting) {
            boolean isSave = false;
            for (GoodsOneCateEntity entity : leftAdapter.getItems()) {
                for (GoodsOneCateEntity.SecondCategoryBean entityTwo : leftAdapter.getItems().get(leftAdapter.getItems().indexOf(entity)).getSecond_category()) {
                    if (entityTwo.isCheck()) {
                        isSave = true;
                        break;
                    }
                }
            }
            if (isSave) {
                ArrayList<String> idList = new ArrayList<>();
                ArrayList<String> nameList = new ArrayList<>();
                for (GoodsOneCateEntity entity : leftAdapter.getItems()) {
                    for (GoodsOneCateEntity.SecondCategoryBean entityTwo : leftAdapter.getItems().get(leftAdapter.getItems().indexOf(entity)).getSecond_category()) {
                        if (entityTwo.isCheck()) {
                            idList.add(entityTwo.getMcid());
                            nameList.add(entityTwo.getName());
                        }
                    }
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra(ID_LIST, idList);
                intent.putStringArrayListExtra(NAME_LIST, nameList);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtils.showShort("请选择类目");
            }
        }
    }

    private void doListCategory() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .listCategory(0, Constant.ALL_PAGES, "1", null, null, null)
                .compose(RxSchedulers.<BaseData<List<GoodsOneCateEntity>>>compose())
                .compose((this).<BaseData<List<GoodsOneCateEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<List<GoodsOneCateEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        Log.e("weng", ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<List<GoodsOneCateEntity>> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            showData(data.getData());
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


    public void showData(List<GoodsOneCateEntity> list) {
        if (list != null && !list.isEmpty()) {
            if (getIntent().getStringArrayListExtra(ID_LIST) != null && getIntent().getStringArrayListExtra(ID_LIST).size() > 0) {
                List<String> ids = getIntent().getStringArrayListExtra(ID_LIST);
                boolean setFirstCheck = false;
                for (String id : ids) {
                    for (GoodsOneCateEntity entityOne : list) {
                        for (GoodsOneCateEntity.SecondCategoryBean entityTwo : entityOne.getSecond_category()) {
                            if (TextUtils.equals(id, entityTwo.getMcid())) {
                                list.get(list.indexOf(entityOne)).getSecond_category().get(entityOne.getSecond_category().indexOf(entityTwo)).setCheck(true);
                                if (!setFirstCheck) {
                                    list.get(list.indexOf(entityOne)).setCheck(true);
                                    setFirstCheck = true;
                                    leftCheckPosition = list.indexOf(entityOne);
                                    leftAdapter.setData(list);
                                    rightAdapter.setData(list.get(list.indexOf(entityOne)).getSecond_category());
                                }
                            }
                        }
                    }
                }

            } else {
                GoodsOneCateEntity first = list.get(0);
                first.setCheck(true);
                leftCheckPosition = 0;
                leftAdapter.setData(list);
                rightAdapter.setData(first.getSecond_category());
            }
        }
    }
}
