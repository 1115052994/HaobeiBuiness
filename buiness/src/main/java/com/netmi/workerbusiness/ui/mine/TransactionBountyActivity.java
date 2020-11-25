package com.netmi.workerbusiness.ui.mine;

import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageBonusEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.BountyEntity;
import com.netmi.workerbusiness.databinding.ActivityTransactionBountyBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Date;

public class TransactionBountyActivity extends BaseXRecyclerActivity<ActivityTransactionBountyBinding, BountyEntity> {

    public final static int CHOOSE_TIME = 123;
    private String tvTime = ""; //记录时间
    private TimePickerView pickerTimeView;
    private Date date;


    @Override
    protected int getContentView() {
        return R.layout.activity_transaction_bounty;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("奖励金");
        mBinding.tvLeftTopBounty.setSelected(true);
        mBinding.tvLeftBounty.setSelected(true);
        initRecycleView();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_time) {
            showTimePicker();
        } else if (view.getId() == R.id.left_layout_bounty) {//奖励金明细
            if(!mBinding.tvLeftTopBounty.isSelected()){
                setSelect(view);
                xRecyclerView.refresh();
            }
        }else if (view.getId() == R.id.right_layout_bounty) {//转换额度明细
            if(!mBinding.tvRightTopBounty.isSelected()){
                setSelect(view);
                xRecyclerView.refresh();
            }
        }

    }

    private void showTimePicker() {
        if (pickerTimeView == null) {
            //时间选择器
            pickerTimeView = new TimePickerView.Builder(getContext(), (Date date, View v) -> {
                this.date = date;
                mBinding.tvTime.setText(DateUtil.formatDateTime(date, DateUtil.DF_YYYY_MM));
                tvTime = DateUtil.formatDateTime(date, DateUtil.DF_YYYY_MM);
                xRecyclerView.refresh();
            })
                    .setTitleText("选择时间")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{true, true, false, false, false, false})
                    .build();
        }
        pickerTimeView.setDate(DateUtil.getCalendar(date));
        pickerTimeView.show();

    }

    private void initRecycleView() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<BountyEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_bounty_details;
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
                        switch (view.getId()) {
                        }
                    }


                };
            }
        });

    }

    @Override
    protected void initData() {
        String todayTime = DateUtil.getCurrentDateYMD();
        mBinding.tvTime.setText(todayTime);
        tvTime = todayTime;
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        showProgress("");
        if(mBinding.tvLeftTopBounty.isSelected()){
            RetrofitApiFactory.createApi(MineApi.class)
                    .getBounsList(tvTime, PageUtil.toPage(startPage), 50)
                    .compose(RxSchedulers.compose())
                    .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new XObserver<BaseData<PageBonusEntity<BountyEntity>>>() {
                        @Override
                        public void onSuccess(BaseData<PageBonusEntity<BountyEntity>> data) {
                            showData(data.getData().getList());
                            mBinding.setExpenditure(data.getData().getTotal_data().getSupport_amount());
                            mBinding.setIncome(data.getData().getTotal_data().getIncome_amount());
                            hideProgress();
                        }
                    });
        }else if(mBinding.tvRightTopBounty.isSelected()){
            RetrofitApiFactory.createApi(MineApi.class)
                    .getBounsQuotaList(tvTime, PageUtil.toPage(startPage), 50)
                    .compose(RxSchedulers.compose())
                    .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new XObserver<BaseData<PageBonusEntity<BountyEntity>>>() {
                        @Override
                        public void onSuccess(BaseData<PageBonusEntity<BountyEntity>> data) {
                            showData(data.getData().getList());
                            mBinding.setExpenditure(data.getData().getTotal_data().getSupport_amount());
                            mBinding.setIncome(data.getData().getTotal_data().getIncome_amount());
                            hideProgress();
                        }
                    });
        }


    }
    public void setSelect(View view){
        mBinding.tvLeftTopBounty.setSelected(view.getId() == R.id.left_layout_bounty);
        mBinding.tvLeftBounty.setSelected(view.getId() == R.id.left_layout_bounty);
        mBinding.tvRightTopBounty.setSelected(view.getId() == R.id.right_layout_bounty);
        mBinding.tvRightBounty.setSelected(view.getId() == R.id.right_layout_bounty);
    }

}
