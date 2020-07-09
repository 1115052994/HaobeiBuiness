package com.netmi.workerbusiness.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.ActivityTimeChooseBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeChooseActivity extends BaseActivity<ActivityTimeChooseBinding> {
    public static final String START = "start";
    public static final String END = "end";
    public static final String TIME = "time";


    private String tvTimeFirst = ""; //记录第一次的时间
    private String tvTimeSecond = ""; //记录第二次的时间
    private TimePickerView pickerTimeView;
    private Date date;
    private TimePickerView pickerTimeView2;
    private Date date2;

    @Override
    protected int getContentView() {
        return R.layout.activity_time_choose;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("选择时间");
        getRightSetting().setText("完成");
        showTimePicker();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_setting) {
            if (!tvTimeFirst.equals("")) {
                if (!tvTimeSecond.equals("")) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(START, tvTimeFirst);
                    bundle.putString(END, tvTimeSecond);
                    intent.putExtra(TIME, bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showError("请选择结束时间");
                }
            } else {
                showError("请选择开始时间");
            }
        } else if (id == R.id.ll_start_time) {
            showTimePicker();
        } else if (id == R.id.ll_end_time) {
            showTimePicker2();
        }
    }

    private void showTimePicker() {
        if (pickerTimeView == null) {
            //时间选择器
            pickerTimeView = new TimePickerView.Builder(getContext(), (Date date, View v) -> {
//                Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                this.date = date;
                mBinding.tvStartTime.setText(DateUtil.formatDateTime(date, DateUtil.DF_YYYY_MM_DD));
                mBinding.tvStartTime.setTextColor(getResources().getColor(R.color.blue_0A73C3));
                mBinding.line1.setBackgroundColor(getResources().getColor(R.color.blue_0A73C3));

                tvTimeFirst = DateUtil.formatDateTime(date, DateUtil.DF_YYYY_MM_DD);
                showTimePicker2();

            })
                    .setTitleText("开始时间")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .build();
        }
        pickerTimeView.setDate(DateUtil.getCalendar(date));
        pickerTimeView.show();

    }


    private void showTimePicker2() {
        if (pickerTimeView2 == null) {
            //时间选择器
            pickerTimeView2 = new TimePickerView.Builder(getContext(), (Date date2, View v) -> {
//                Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                this.date2 = date2;
                mBinding.tvEndTime.setText(DateUtil.formatDateTime(date2, DateUtil.DF_YYYY_MM_DD));
                mBinding.tvEndTime.setTextColor(getResources().getColor(R.color.blue_0A73C3));
                mBinding.line2.setBackgroundColor(getResources().getColor(R.color.blue_0A73C3));

                tvTimeSecond = DateUtil.formatDateTime(date2, DateUtil.DF_YYYY_MM_DD);
            })
                    .setTitleText("结束时间")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .build();
        }
        pickerTimeView2.setDate(DateUtil.getCalendar(date2));
        pickerTimeView2.show();

    }


}
