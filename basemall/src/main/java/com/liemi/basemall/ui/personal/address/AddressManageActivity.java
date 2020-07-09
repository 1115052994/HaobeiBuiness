package com.liemi.basemall.ui.personal.address;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.AddressEntity;
import com.liemi.basemall.databinding.ActivityXrecyclerviewBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.widget.ConfirmDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.annotations.NonNull;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/9 9:43
 * 修改备注：
 */
public class AddressManageActivity extends BaseXRecyclerActivity<ActivityXrecyclerviewBinding, AddressEntity> {

    public static final String ADDRESS_ENTITY = "address_entity";
    public static final String CHOICE_ADDRESS = "choice_address";
    public static final String CHOICE_ADDRESS_MAID = "choice_address_maid";

    private final static int ADDRESS_ADD = 0x11;
    private final static int ADDRESS_EDIT = 0x12;

    @Override
    protected int getContentView() {
        return R.layout.activity_xrecyclerview;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("地址管理");
        getRightSetting().setText("新增");

        //地址列表数据
        adapter = new BaseRViewAdapter<AddressEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_address;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId() == R.id.tv_eidt) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(ADDRESS_ENTITY, adapter.getItem(position));
                            JumpUtil.startForResult(getActivity(), AddressAddActivity.class, ADDRESS_EDIT, bundle);
                        } else if (view.getId() == R.id.tv_delete) {
                            new ConfirmDialog(getContext())
                                    .setContentText("确认删除地址吗？")
                                    .setConfirmListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            doAddressDel(adapter.getItem(position));
                                        }
                                    }).show();
                        } else if (view.getId() == R.id.rb_default_address) {
                            AddressEntity entity = adapter.getItem(position);
                            //doSetDefaultAddress(entity.getMaid(), position);
                            doAddressEdit(items.get(position),items.get(position).getIs_top() == 0 ? 1 : 0);
                        } else if (getIntent().getIntExtra(CHOICE_ADDRESS, 0) > 0) {
                            Intent intent = new Intent();
                            intent.putExtra(ADDRESS_ENTITY, adapter.getItem(position));
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                };
            }
        };

        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDRESS_ADD) {
                xRecyclerView.refresh();
            } else if (requestCode == ADDRESS_EDIT) {
                AddressEntity addressEntity = (AddressEntity) data.getSerializableExtra(ADDRESS_ENTITY);
                if (addressEntity != null) {
                    for (AddressEntity entity : adapter.getItems()) {
                        if (entity.getMaid().equals(addressEntity.getMaid())) {
                            adapter.replace(adapter.getItems().indexOf(entity), addressEntity);
                            break;
                        }
                    }
                } else {
                    xRecyclerView.refresh();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        String maid = getIntent().getStringExtra(CHOICE_ADDRESS_MAID);
        if (!TextUtils.isEmpty(maid) || getIntent().getIntExtra(CHOICE_ADDRESS, 0) > 0) {
            AddressEntity resultAddress = null;
            for (AddressEntity entity : adapter.getItems()) {
                if (TextUtils.equals(entity.getMaid(), maid)) {
                    resultAddress = entity;
                    break;
                }
            }
            if (resultAddress == null
                    && adapter.getItemCount() > 0) {
                resultAddress = adapter.getItem(0);
            }
            Intent intent = new Intent();
            intent.putExtra(ADDRESS_ENTITY, resultAddress);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);

        //点击设置按钮，跳转到添加地址页面
        if (view.getId() == R.id.tv_setting) {
            JumpUtil.overlay(this, AddressAddActivity.class);
            return;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onRefresh();
    }

    //获取收货地址列表
    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doAddressList(PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(RxSchedulers.<BaseData<PageEntity<AddressEntity>>>compose())
                .compose((this).<BaseData<PageEntity<AddressEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<AddressEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<PageEntity<AddressEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }


    private void doAddressDel(final AddressEntity addressEntity) {
        RetrofitApiFactory.createApi(MineApi.class)
                .doDeleteAddress(addressEntity.getMaid())
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            adapter.remove(addressEntity);
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
    private void doAddressEdit(final AddressEntity entity, final int isTop) {

        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUpdateAddress(entity.getMaid(), entity.getName(), entity.getP_id(), entity.getC_id(), entity.getD_id(), entity.getTel(), entity.getAddress(), isTop,null)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        for (AddressEntity addressEntity : adapter.getItems()) {
                            addressEntity.setIs_top(0);
                        }
                        entity.setIs_top(isTop);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }
    private void doSetDefaultAddress(String address_id, final int position) {
        /*
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .addressSetDefault(address_id)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    public void onNext(BaseData baseData) {
                        showError(baseData.getErrmsg());
                        if (baseData.getErrcode() == Constant.SUCCESS_CODE) {
                            //遍历adapter，设置默认的地址
                            for (int i = 0; i < adapter.getItemCount(); i++) {
                                if (i == position) {
                                    adapter.getItem(i).setIs_default(1);
                                } else {
                                    adapter.getItem(i).setIs_default(2);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });
                */

    }

}
