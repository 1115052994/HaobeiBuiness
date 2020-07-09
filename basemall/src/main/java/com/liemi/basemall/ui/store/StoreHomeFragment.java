package com.liemi.basemall.ui.store;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.FloorApi;
import com.liemi.basemall.data.entity.floor.FloorPageEntity;
import com.liemi.basemall.data.entity.floor.NewFloorEntity;
import com.liemi.basemall.databinding.FragmentXrecyclerviewBinding;
import com.liemi.basemall.ui.home.FloorAdapter;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_SEARCH_BAR;
import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/16 15:36
 * 修改备注：
 */
public class StoreHomeFragment extends BaseXRecyclerFragment<FragmentXrecyclerviewBinding, NewFloorEntity> {

    public static final String TAG = StoreHomeFragment.class.getName();

    private String storeId;

    @Override
    protected int getContentView() {
        return R.layout.fragment_xrecyclerview;
    }

    public static StoreHomeFragment newInstance(String storeId) {
        StoreHomeFragment f = new StoreHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STORE_ID, storeId);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeId = getArguments().getString(STORE_ID);
    }

    @Override
    protected void initUI() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FloorAdapter(getContext(), xRecyclerView).setShopId(storeId).setLifecycleFragment(this);

        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    public void showData(FloorPageEntity<NewFloorEntity> pageEntity) {

        if (LOADING_TYPE == Constant.PULL_REFRESH && pageEntity != null) {
            if (!Strings.isEmpty(pageEntity.getContent().getList())) {
                //搜索栏另外处理
                for (NewFloorEntity floorEntity : pageEntity.getContent().getList()) {
                    if (floorEntity.getType() == FLOOR_SEARCH_BAR) {
                        pageEntity.getContent().getList().remove(floorEntity);
                        pageEntity.setTotal_pages(pageEntity.getTotal_pages() - 1);
                        break;
                    }
                }
            }
        }

        super.showData(pageEntity);
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(FloorApi.class)
                .doListFloors(1, storeId)
                .compose(RxSchedulers.<BaseData<FloorPageEntity<NewFloorEntity>>>compose())
                .compose((this).<BaseData<FloorPageEntity<NewFloorEntity>>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<FloorPageEntity<NewFloorEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<FloorPageEntity<NewFloorEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


}
