package com.netmi.workerbusiness.ui.login;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.LoginApi;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.netmi.workerbusiness.data.entity.CateGoryVerifyEntity;
import com.netmi.workerbusiness.data.entity.home.category.GoodsOneCateEntity;
import com.netmi.workerbusiness.databinding.ActivityCategoryVerifyBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class CategoryVerifyActivity extends BaseActivity<ActivityCategoryVerifyBinding> {

    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";

    private RecyclerView rvLeft, rvRight;
    private BaseRViewAdapter<CateGoryVerifyEntity, BaseViewHolder> leftAdapter;
    private BaseRViewAdapter<CateGoryVerifyEntity.CategoryBean, BaseViewHolder> rightAdapter;

    private String id;
    private String name;

    //线上线下店铺选择页面传进来的type
    private String user_type_event = "0"; //1线上  2线下

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_category_verify;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("经营类目");
        user_type_event = getIntent().getExtras().getString(JumpUtil.TYPE);
        mBinding.tvConfirm.setVisibility(View.GONE);
        rvLeft = mBinding.rvLeft;
        rvRight = mBinding.rvRight;
        rvLeft.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRight.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLeft.setAdapter(leftAdapter = new BaseRViewAdapter<CateGoryVerifyEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_select_categroy_verify_left;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        for (CateGoryVerifyEntity entity : getItems()) {
                            if (position == getItems().indexOf(entity)) {
                                leftAdapter.getItem(position).setCheck(true);
                            } else {
                                leftAdapter.getItem(getItems().indexOf(entity)).setCheck(false);
                            }
                        }
                        leftAdapter.notifyDataSetChanged();
                        rightAdapter.setData(getItem(position).getCategory());
                    }
                };
            }
        });

        rvRight.setAdapter(rightAdapter = new BaseRViewAdapter<CateGoryVerifyEntity.CategoryBean, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_select_category_verify_right;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        for (CateGoryVerifyEntity.CategoryBean entity : getItems()) {
                            if (position == getItems().indexOf(entity)) {
                                getItem(position).setCheck(true);
                                id = getItem(position).getMcid();
                                name = getItem(position).getName();
                                mBinding.tvConfirm.setVisibility(View.VISIBLE);
                            } else {
                                getItem(getItems().indexOf(entity)).setCheck(false);
                            }
                        }
                        rightAdapter.notifyDataSetChanged();
                    }
                };
            }
        });
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        if (user_type_event.equals("2")) {
            doListCategory("0");
        } else {
            doListCategory("1");
        }
    }

    private void doListCategory(String type) {
        //线上店铺 0： 线下店铺 默认： 1
        RetrofitApiFactory.createApi(LoginApi.class)
                .getCategory(type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<CateGoryVerifyEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<CateGoryVerifyEntity>> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            CateGoryVerifyEntity first = data.getData().getList().get(0);
                            first.setCheck(true);
                            leftAdapter.setData(data.getData().getList());
                            rightAdapter.setData(first.getCategory());
                        } else {
                            showError(data.getErrmsg());
                        }
                    }
                });
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_confirm) {
            Intent intent = new Intent();
            intent.putExtra(CATEGORY_ID, id);
            intent.putExtra(CATEGORY_NAME, name);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
