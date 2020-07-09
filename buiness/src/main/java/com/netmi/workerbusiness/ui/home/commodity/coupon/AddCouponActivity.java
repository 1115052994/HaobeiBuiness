package com.netmi.workerbusiness.ui.home.commodity.coupon;

import android.graphics.Color;
import android.view.View;
import android.widget.CompoundButton;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.CouponApi;
import com.netmi.workerbusiness.databinding.ActivityAddCouponBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Date;

public class AddCouponActivity extends BaseActivity<ActivityAddCouponBinding> implements CompoundButton.OnCheckedChangeListener {

    private int start = 0;

    //记录的时间用于显示（年月日），上传的时间用于接口上传（年月日时分）
    private String tvTimeFirst = ""; //记录第一次的时间
    private String timeFirstUp = ""; //上传的第一次的时间
    private String tvTimeSecond = ""; //记录第二次的时间
    private String TimeSecondUp = ""; //上传的第二次的时间
    private String timeTitle = ""; //时间选择器标题

    @Override
    protected int getContentView() {
        return R.layout.activity_add_coupon;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("新增优惠券");
        mBinding.setCheckListener(this);
        new InputListenView(mBinding.tvConfirm, mBinding.etTargetAccount, mBinding.etDelAccount, mBinding.etNum, mBinding.etContent) {

        };
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_time) {
            KeyboardUtils.hideKeyboard(mBinding.etTargetAccount);
            KeyboardUtils.hideKeyboard(mBinding.etDelAccount);
            KeyboardUtils.hideKeyboard(mBinding.etNum);
            showTimePicker();
        } else if (id == R.id.tv_confirm) {
            addCoupon();
        }
    }

    private void addCoupon() {
        RetrofitApiFactory.createApi(CouponApi.class)
                .addCoupon(mBinding.etNum.getText().toString(), timeFirstUp, TimeSecondUp, start,
                        mBinding.etContent.getText().toString(),
                        Integer.valueOf(mBinding.etTargetAccount.getText().toString()),
                        Integer.valueOf(mBinding.etDelAccount.getText().toString()))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        finish();
                    }

                    @Override
                    public void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });
    }

    private TimePickerView pickerTimeView;
    private Date date;

    private void showTimePicker() {
        if (pickerTimeView == null) {
            //时间选择器
            pickerTimeView = new TimePickerView.Builder(getContext(), (Date date, View v) -> {
//                Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                this.date = date;
                mBinding.tvTime.setText(DateUtil.formatDateTime(date, DateUtil.DF_YYYY_MM_DD));
                tvTimeFirst = DateUtil.formatDateTime(date, DateUtil.DF_YYYY_MM_DD);
                timeFirstUp = DateUtil.formatDateTime(date, DateUtil.DF_YYYY_MM_DD_HH_MM);
                showTimePicker2();

            })
                    .setTitleText("开始时间")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{true, true, true, true, true, false})
                    .build();
        }
        pickerTimeView.setDate(DateUtil.getCalendar(date));
        pickerTimeView.show();

    }

    private TimePickerView pickerTimeView2;
    private Date date2;

    private void showTimePicker2() {
        if (pickerTimeView2 == null) {
            //时间选择器
            pickerTimeView2 = new TimePickerView.Builder(getContext(), (Date date2, View v) -> {
//                Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                this.date2 = date2;
                mBinding.tvTime.setText(tvTimeFirst + "——" + DateUtil.formatDateTime(date2, DateUtil.DF_YYYY_MM_DD));
                tvTimeSecond = DateUtil.formatDateTime(date2, DateUtil.DF_YYYY_MM_DD);
                TimeSecondUp = DateUtil.formatDateTime(date2, DateUtil.DF_YYYY_MM_DD_HH_MM);
            })
                    .setTitleText("结束时间")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{true, true, true, true, true, false})
                    .build();
        }
        pickerTimeView2.setDate(DateUtil.getCalendar(date2));
        pickerTimeView2.show();

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
        if (isCheck) {
//            showError("开启");
            start = 1;
        } else {
//            showError("关闭");
            start = 0;
        }
    }
}
