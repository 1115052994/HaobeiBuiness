package com.netmi.workerbusiness.ui.mine;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.basemall.databinding.ActivityXrecyclerviewBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.TransactionEntity;
import com.netmi.workerbusiness.data.entity.mine.WalletEntity;
import com.netmi.workerbusiness.databinding.ActivityTransactionDetailsBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Date;

import static com.netmi.workerbusiness.ui.mine.TimeChooseActivity.END;
import static com.netmi.workerbusiness.ui.mine.TimeChooseActivity.START;
import static com.netmi.workerbusiness.ui.mine.TimeChooseActivity.TIME;

public class TransactionDetailsActivity extends BaseXRecyclerActivity<ActivityTransactionDetailsBinding, TransactionEntity> {

    public final static int CHOOSE_TIME = 123;
    private String tvTime = ""; //记录时间
    private TimePickerView pickerTimeView;
    private Date date;

    @Override
    protected int getContentView() {
        return R.layout.activity_transaction_details;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("交易明细");
        initRecycleView();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_time) {
            showTimePicker();
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
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<TransactionEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_transaction_details;
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
        RetrofitApiFactory.createApi(MineApi.class)
                .transactionDetails(tvTime, PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<TransactionEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<TransactionEntity>> data) {
                        showData(data.getData());
                        mBinding.setExpenditure(data.getData().getTotal_income().getOut_income());
                        mBinding.setIncome(data.getData().getTotal_income().getEnter_income());
                        hideProgress();
                    }
                });
    }

}
