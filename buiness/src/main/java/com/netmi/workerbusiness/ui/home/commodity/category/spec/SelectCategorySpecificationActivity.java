package com.netmi.workerbusiness.ui.home.commodity.category.spec;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
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
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.algorithm.CartesianProductUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.netmi.workerbusiness.data.entity.home.store.SpecDetailEntity;
import com.netmi.workerbusiness.data.entity.home.store.StoreCateEntity;
import com.netmi.workerbusiness.databinding.ActivitySelectCategoryBinding;
import com.netmi.workerbusiness.databinding.ItemStoreCateParentBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.CATEGORY_OR_SPECIFICATION;

public class SelectCategorySpecificationActivity extends BaseActivity<ActivitySelectCategoryBinding> {

    private RecyclerView rvLeft, rvRight;
    private int currentCheckPoistion;
    private StoreCateEntity currentCheckObj;
    private BaseRViewAdapter<StoreCateEntity, BaseViewHolder> parentAdapter;
    private BaseRViewAdapter<StoreCateEntity.MePropValues, BaseViewHolder> childAdapter;
    private static final int SAVE = 123;
    private static final int ADD = 124;
    private ArrayList<SpecDetailEntity> results;
    private boolean haveBoxOne = false;
    private boolean onBackpressed = false;
    private int step = 0;
    private String goodID = "";

    private boolean isCreate = false; //是否是创建规格，否则为编辑规格

    /**
     * 1表示从新建商品页面进入 没有删除按钮
     * 2表示从mainfragment页面进入 需要删除按钮
     */
    private int type;

    @Override
    protected int getContentView() {
        return R.layout.activity_select_category;
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_setting) {//保存
//                if (getIntent().getParcelableArrayListExtra(JumpUtil.VALUE) == null){
//
//                }
            if (getIntent().getExtras().getString(CATEGORY_OR_SPECIFICATION).equals("")) {
                Log.e("weng", "空字符串表示从规格编辑进入，不需要进行保存操作，否则在创建商品页面进入，需要返回数据");
            } else {
                results = getResults();
                if (results.isEmpty()) {
                    showError("请选择规格");
                    return;
                }
            }
            Bundle args = new Bundle();
            args.putParcelableArrayList(JumpUtil.VALUE, results);
            args.putString(JumpUtil.ID, goodID);
            JumpUtil.startForResult(this, CategorySpecificationCompleteActivity.class, SAVE, args);
        } else if (id == R.id.btn_add) {//新增
            Bundle data = new Bundle();
            data.putParcelableArrayList(JumpUtil.VALUE, (ArrayList<StoreCateEntity>) parentAdapter.getItems());
            JumpUtil.startForResult(this, CreateCategorySpecificationActivity.class, ADD, data);
        } else if (id == R.id.btn_del) {  //删除规格
//                showError("删除规格");
            if (step == 0) {
                if (mBinding.rvRight.getVisibility() == View.GONE) {
                    haveBoxOne = true;
                    getList();
                    step = 1;
                } else {
                    haveBoxOne = false;

                    results = getResults();
                    if (results.isEmpty()) {
                        showError("请选择规格");
                        return;
                    }
//                        showError("删除二级规格");
                    delCateTwo();
                }
            } else if (step == 1) {
//                    showError("删除一级规格");

                if (cateOne.size() == 0) {  //当为选中任何一个一级规格时，点击返回最初的状态
                    haveBoxOne = false;
                    getList();
                    step = 0;
                } else {
                    delCateOne();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        haveBoxOne = false;
        onBackpressed = true;
        getList();
    }

    //获取选中   规格
    private ArrayList<SpecDetailEntity> getResults() {
        if (results == null)
            results = new ArrayList<>();
        cateTwo.clear();
        List<List<String>> chooseName = new ArrayList<>();
        List<List<String>> chooseID = new ArrayList<>();

        List<String> childStock = new ArrayList<>();
        List<String> childPrice = new ArrayList<>();
        List<String> childDiscount = new ArrayList<>();
        for (int i = 0; i < parentAdapter.getItems().size(); i++) {
            StoreCateEntity parent = parentAdapter.getItem(i);
            if (parent.getCount() > 0) {
                List<String> childName = new ArrayList<>();
                List<String> childId = new ArrayList<>();
                for (int j = 0; j < parent.getMePropValues().size(); j++) {
                    StoreCateEntity.MePropValues child = parent.getMePropValues().get(j);
                    if (child.isCheck()) {
                        childName.add(child.getValue_name());
                        childId.add(child.getValue_id());
                        cateTwo.add(child.getValue_id());
                    }
                }
                for (int k = 0; k < results.size(); k++) {
                    childStock.add(results.get(k).getStock());
                    childPrice.add(results.get(k).getPrice());
                    childDiscount.add(results.get(k).getDiscount());
                }
                if (!childId.isEmpty()) {
                    chooseName.add(childName);
                    chooseID.add(childId);
                }
            }
        }
        results.clear();
        List<String> names = CartesianProductUtil.listCartesianProduct(chooseName, " ");
        List<String> ids = CartesianProductUtil.listCartesianProduct(chooseID, ",");

        if (isCreate) { // 创建商品
            for (int i = 0; i < names.size(); i++) {
                results.add(new SpecDetailEntity(names.get(i), ids.get(i)));
            }
        } else { //编辑进入
            for (int i = 0; i < names.size(); i++) {
                if (i <= childPrice.size() - 1) {  //添加原先就存在的规格组，含价格、库存等参数
                    results.add(new SpecDetailEntity(names.get(i), ids.get(i), childPrice.get(i), childStock.get(i), childDiscount.get(i)));
                } else {                       //新添加的规格组，不含价格、库存等参数
                    results.add(new SpecDetailEntity(names.get(i), ids.get(i)));
                }
            }
        }
        return results;
    }

    /**
     * 判断是否是已经有了,没有返回-1
     */
    private int isContained(String name) {
        for (int i = 0; i < results.size(); i++) {
            if (TextUtils.equals(results.get(i).getValue_names(), name))
                return i;
        }
        return -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAVE) {
            if (resultCode == JumpUtil.SUCCESS) {
                Intent it = new Intent();
                Bundle args = new Bundle();
                args.putParcelableArrayList(JumpUtil.VALUE, data.getExtras().getParcelableArrayList(JumpUtil.VALUE));
                it.putExtras(args);
                setResult(RESULT_OK, it);
                results = data.getExtras().getParcelableArrayList(JumpUtil.VALUE);
                finish();
            } else {
                results = data.getExtras().getParcelableArrayList(JumpUtil.VALUE);
            }
        } else if (requestCode == ADD && resultCode == JumpUtil.SUCCESS) {
            initData();
        }
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("选择规格");
        getRightSetting().setText("保存");
        mBinding.rvRight.setVisibility(View.GONE);
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        if (type == 1) {
            mBinding.btnDel.setVisibility(View.GONE);
            mBinding.vLine.setVisibility(View.GONE);
        }
        if (getIntent().getExtras().getString(CATEGORY_OR_SPECIFICATION) != null) {
            if (getIntent().getExtras().getString(CATEGORY_OR_SPECIFICATION).equals("specification")) {
                goodID = getIntent().getExtras().getString(JumpUtil.ID);
                mBinding.rvRight.setVisibility(View.VISIBLE);
            } else if (getIntent().getExtras().getString(CATEGORY_OR_SPECIFICATION).equals("")) {
                getRightSetting().setText("");
            }
        }
        rvLeft = mBinding.rvLeft;
        rvRight = mBinding.rvRight;
        rvLeft.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRight.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLeft.setAdapter(parentAdapter = new BaseRViewAdapter<StoreCateEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_store_cate_parent;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<StoreCateEntity>(binding) {
                    @Override
                    public void bindData(StoreCateEntity item) {
                        super.bindData(item);//不能删
                        ItemStoreCateParentBinding itemBinding = (ItemStoreCateParentBinding) binding;
                        if (haveBoxOne) {
                            itemBinding.iv.setVisibility(View.VISIBLE);
                        } else {
                            itemBinding.iv.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        ItemStoreCateParentBinding itemBinding = (ItemStoreCateParentBinding) binding;
                        StoreCateEntity entity = items.get(position);
                        if (step == 1) {//选中需要删除的一级规格

                            //改变UI
                            if (entity.isRed()) {
                                itemBinding.iv.setImageResource(R.mipmap.ic_uncheck_white);
                                cateOne.remove(cateOne.size() - 1);//删除最后一个元素
                                entity.setRed(false);
                            } else if (!entity.isRed()) {
                                itemBinding.iv.setImageResource(R.mipmap.ic_check_red);
                                cateOne.add(entity.getProp_id());
                                entity.setRed(true);
                            }
                        } else {
                            if (currentCheckPoistion != position) {
                                items.get(currentCheckPoistion).setCheck(false);
                                items.get(position).setCheck(true);
                                currentCheckPoistion = position;
                                currentCheckObj = items.get(currentCheckPoistion);
                                childAdapter.setData(currentCheckObj.getMePropValues());

                                //显示右边的列表，并将左边列表中的checkbox隐藏
                                mBinding.rvRight.setVisibility(View.VISIBLE);
                                itemBinding.iv.setVisibility(View.GONE);
                                haveBoxOne = false;
                            } else {
                                //显示右边的列表，并将左边列表中的checkbox隐藏
                                mBinding.rvRight.setVisibility(View.VISIBLE);
                                itemBinding.iv.setVisibility(View.GONE);
                                haveBoxOne = false;
                            }
                        }

                    }
                };
            }
        });
        rvRight.setAdapter(childAdapter = new BaseRViewAdapter<StoreCateEntity.MePropValues, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_store_cate_child;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        items.get(position).setCheck();
                        if (items.get(position).isCheck())
                            parentAdapter.getItems().get(currentCheckPoistion).pushCount();
                        else
                            parentAdapter.getItems().get(currentCheckPoistion).popCount();
                    }
                };
            }
        });
    }


    @Override
    protected void initData() {
        getList();
    }

    private void getList() {
        showProgress("");
        if (getIntent().getParcelableArrayListExtra(JumpUtil.VALUE) != null && getIntent().getParcelableArrayListExtra(JumpUtil.VALUE).size() > 0) {
            results = getIntent().getParcelableArrayListExtra(JumpUtil.VALUE);
        }
        RetrofitApiFactory.createApi(StoreApi.class)
                .storeCate("whatever")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<PageEntity<StoreCateEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        Log.e("weng", ex.getMessage());
                    }

                    @Override
                    public void onNext(BaseData<PageEntity<StoreCateEntity>> data) {

                        if (data.getErrcode() == Constant.SUCCESS_CODE && data.getData() != null) {
                            if (data.getData().getList().size() > 0) {
                                if (results != null) {
                                    List<StoreCateEntity> storeCateEntities = data.getData().getList();
                                    boolean isSetFirstCheck = false;
                                    for (SpecDetailEntity specDetailEntity : results) {
                                        for (StoreCateEntity storeCateEntity : storeCateEntities) {
                                            for (StoreCateEntity.MePropValues childrenBean : storeCateEntity.getMePropValues()) {
                                                String[] ids = specDetailEntity.getValue_ids().split(",");
                                                Log.e("weng", specDetailEntity.getValue_ids());
                                                for (String id : ids) {
                                                    if (TextUtils.equals(id, childrenBean.getValue_id())) {
                                                        storeCateEntities.get(storeCateEntities.indexOf(storeCateEntity))
                                                                .getMePropValues()
                                                                .get(storeCateEntity.getMePropValues().indexOf(childrenBean))
                                                                .setTrue();
//                                                                .setCheck();
                                                        storeCateEntities.get(storeCateEntities.indexOf(storeCateEntity)).pushCount();

                                                        if (!isSetFirstCheck) {
                                                            isSetFirstCheck = true;
                                                            currentCheckPoistion = storeCateEntities.indexOf(storeCateEntity);
                                                            storeCateEntities.get(currentCheckPoistion).setCheck(true);
                                                            currentCheckObj = storeCateEntity;
                                                            childAdapter.setData(currentCheckObj.getMePropValues());
                                                            parentAdapter.setData(storeCateEntities);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    /**
                                     * 编辑商详状态下直接跳转到商品规格库存价格输入页面
                                     * */
                                    if (!onBackpressed) {
                                        if (getIntent().getExtras().getString(CATEGORY_OR_SPECIFICATION).equals("")) {
                                            Log.e("weng", "空字符串表示从规格编辑进入，不需要进行保存操作，否则在创建商品页面进入，需要返回数据");
                                        } else {
                                            results = getResults();
                                            if (results.isEmpty()) {
                                                showError("请选择规格");
                                                return;
                                            }
                                        }
                                        Bundle args = new Bundle();
                                        args.putParcelableArrayList(JumpUtil.VALUE, results);
                                        JumpUtil.startForResult(getActivity(), CategorySpecificationCompleteActivity.class, SAVE, args);
                                    }

                                } else {
                                    StoreCateEntity item = data.getData().getList().get(0);
                                    if (item != null) {
                                        item.setCheck(true);
                                        currentCheckObj = item;
                                        currentCheckPoistion = 0;
                                        childAdapter.setData(currentCheckObj.getMePropValues());
                                    }
                                    parentAdapter.setData(data.getData().getList());
                                    results = null;
                                    isCreate = true;


                                }
                            }
                        } else
                            showError(data.getErrmsg());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


    List<String> cateOne = new ArrayList<>();
    List<String> cateTwo = new ArrayList<>();

    //删除一级规格
    private void delCateOne() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .deleteOne(cateOne)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        initData();
                        cateOne.clear();
                    }
                });
    }

    //删除二级规格
    private void delCateTwo() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .deleteTwo(cateTwo)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        //重启页面
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        cateTwo.clear();
                    }
                });
    }

}
