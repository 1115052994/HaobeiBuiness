package com.liemi.basemall.ui.personal.order;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.data.entity.order.LogisticEntity;
import com.liemi.basemall.databinding.ActivityLogisticTrackBinding;
import com.liemi.basemall.databinding.ItemLogisticTrackBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/9 17:26
 * 修改备注：
 */
public class LogisticTrackActivity extends BaseActivity<ActivityLogisticTrackBinding> implements XRecyclerView.LoadingListener {

    public static final String MPID = "mpid";

    BaseRViewAdapter<LogisticEntity.ListBean, BaseViewHolder> adapter;
    private List<LogisticEntity> entity = new ArrayList<>();
    private List<String> expressList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_logistic_track;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("物流详情");
        getRightSetting().setText("物流选择");
        xRecyclerView = mBinding.xrlLogisticTrack;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<LogisticEntity.ListBean, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_logistic_track;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                    }
                };
            }

            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                ItemLogisticTrackBinding binding = (ItemLogisticTrackBinding) holder.getBinding();
                LogisticEntity.ListBean bean = getItem(position);
                binding.tvLogisticsInfo.setText(bean.getContent());

                binding.tvLogisticsDate.setText(DateUtil.formatDateTime(DateUtil.strToDate(bean.getTime(), DateUtil.DF_YYYY_MM_DD_HH_MM_SS), DateUtil.DF_MM_DD));
                binding.tvLogisticsTime.setText(DateUtil.formatDateTime(DateUtil.strToDate(bean.getTime(), DateUtil.DF_YYYY_MM_DD_HH_MM_SS), DateUtil.DF_HH_MM));
                if (position == getItemCount() - 1) {
                    binding.viewBottom.setVisibility(View.VISIBLE);
                    binding.viewLine.setVisibility(View.GONE);
                } else {
                    binding.viewBottom.setVisibility(View.GONE);
                    binding.viewLine.setVisibility(View.VISIBLE);
                }

                if (position == 0) {
                    binding.viewTop.setVisibility(View.VISIBLE);
                    binding.ivIcon.setImageResource(R.mipmap.ic_logistic_current_step);
                } else {
                    binding.viewTop.setVisibility(View.GONE);
                    binding.ivIcon.setImageResource(R.mipmap.ic_logistics_step);
                }
            }
        });

        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);

        mBinding.setItem(new LogisticEntity());
        mBinding.executePendingBindings();
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    public void showData(LogisticEntity logisticEntity) {
        if (logisticEntity != null
                && logisticEntity.getList() != null) {
            adapter.setData(logisticEntity.getList());
        }
        mBinding.setItem(logisticEntity);
        mBinding.executePendingBindings();
    }

    @Override
    public void onRefresh() {
        doGetLogistic();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        xRecyclerView.refreshComplete();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_copy) {
            KeyboardUtils.putTextIntoClip(this, mBinding.tvLogisticMailNo.getText().toString());
        } else if (i == R.id.tv_setting) {
            showExpressCompany();
        }
    }


    private void doGetLogistic() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .getLogistic(getIntent().getStringExtra(MPID))
                .compose(RxSchedulers.<BaseData<List<LogisticEntity>>>compose())
                .compose((this).<BaseData<List<LogisticEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<LogisticEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<List<LogisticEntity>> data) {
                        showData(data.getData().get(0));
                        entity = data.getData();
                        expressList.clear();
                        for (LogisticEntity logisticEntity : data.getData()) {
                            expressList.add(logisticEntity.getCompany());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void showExpressCompany() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                showData(entity.get(options1));
            }
        }).build();
        pvOptions.setPicker(expressList);
        pvOptions.setSelectOptions(0);
        pvOptions.show();
    }

}
