package com.netmi.workerbusiness.ui.home.commodity.offline;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.bigkoo.pickerview.TimePickerView;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.entity.home.offlinecommodity.OfflineGoodDetailEntity;
import com.netmi.workerbusiness.databinding.ActivityPurchaseNotesBinding;

import java.util.Date;

public class PurchaseNotesActivity extends BaseActivity<ActivityPurchaseNotesBinding> implements CompoundButton.OnCheckedChangeListener {
    private int check;
    public static final String PURCHASENOTE = "PurchaseNotes";
    private String startTime = "";
    private String endTime = "";

    private OfflineGoodDetailEntity entity;

    //TYPE表示从哪个页面进入 1表示创建商品 2表示编辑商品
    private int type;

    @Override
    protected int getContentView() {
        return R.layout.activity_purchase_notes;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("购买须知");
        getRightSetting().setText("保存");
        mBinding.setCheckListener(this);
        if (getIntent().getExtras().get(JumpUtil.TYPE) != null && getIntent().getExtras().getInt(JumpUtil.TYPE) == 2) {
            if (getIntent().getExtras().get(JumpUtil.VALUE) != null) {
                entity = (OfflineGoodDetailEntity) getIntent().getExtras().getSerializable(JumpUtil.VALUE);
                mBinding.etRule.setText(entity.getPurchase_note());
                mBinding.etRule.setSelection(mBinding.etRule.length());
                mBinding.tvStartTime.setText(entity.getStart_date());
                startTime = entity.getStart_date();
                mBinding.tvEndTime.setText(entity.getEnd_date());
                endTime = entity.getEnd_date();
            }
        } else if (getIntent().getExtras().get(JumpUtil.TYPE) != null && getIntent().getExtras().getInt(JumpUtil.TYPE) == 1) {
            mBinding.etRule.setText(getIntent().getExtras().getString(JumpUtil.CODE));
            startTime = getIntent().getExtras().getString(JumpUtil.VALUE);
            mBinding.tvStartTime.setText(startTime);
            endTime = getIntent().getExtras().getString(JumpUtil.FLAG);
            mBinding.tvEndTime.setText(endTime);
            check = getIntent().getExtras().getInt(JumpUtil.ID);
            if (check == 1) {
                mBinding.rbYms.setChecked(true);

            }
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_start_time) {
            showTimePicker1();
            KeyboardUtils.hideKeyboard(mBinding.etRule);
        } else if (id == R.id.tv_end_time) {
            showTimePicker2();
            KeyboardUtils.hideKeyboard(mBinding.etRule);
        } else if (id == R.id.tv_hour) {
            showTimePicker3();
        } else if (id == R.id.tv_setting) {
            checkData();
        }
    }

    private void checkData() {
        if (mBinding.tvStartTime.getText() == "2019年01月01日" || mBinding.tvEndTime.getText() == "2019年01月01日") {
            showError("请输入有效期");
        } else if (mBinding.etRule.getText().toString().isEmpty()) {
            showError("请输入使用规则");
        } else if (startTime.equals("") || endTime.equals("")) {
            showError("请输入使用时间");
        } else {
            Intent intent = new Intent();
            Bundle args = new Bundle();
            args.putString(JumpUtil.VALUE, startTime);
            args.putString(JumpUtil.TYPE, endTime);
//            args.putString(JumpUtil.ID, mBinding.tvHour.getText().toString());
            args.putString(JumpUtil.FLAG, mBinding.etRule.getText().toString());
            args.putInt(JumpUtil.ID, check);
            intent.putExtra(PURCHASENOTE, args);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
        if (isCheck) {
//            showError("开启");
            check = 1;
            mBinding.etRule.setText("周末、法定节假日通用");
            mBinding.etRule.setSelection(10);
        } else {
//            showError("关闭");
            check = 0;
            mBinding.etRule.setText("");
        }
    }

    private TimePickerView pickerTimeView1;
    private TimePickerView pickerTimeView2;
    private TimePickerView pickerTimeView3;
    private TimePickerView pickerTimeView4;
    private Date date;

    private void showTimePicker1() {
        if (pickerTimeView1 == null) {
            //时间选择器
            pickerTimeView1 = new TimePickerView.Builder(getContext(), (Date date, View v) -> {

                this.date = date;
                mBinding.tvStartTime.setText(DateUtil.formatDateTime(date, DateUtil.DF_YYYY_MM_DD_CHINA));
                startTime = DateUtil.formatDateTime(date, DateUtil.DF_YYYY_MM_DD);
            })
                    .setTitleText("开始日期")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .build();
        }
        pickerTimeView1.setDate(DateUtil.getCalendar(date));
        pickerTimeView1.show();

    }

    private void showTimePicker2() {
        if (pickerTimeView2 == null) {
            //时间选择器
            pickerTimeView2 = new TimePickerView.Builder(getContext(), (Date date, View v) -> {
                this.date = date;
                mBinding.tvEndTime.setText(DateUtil.formatDateTime(date, DateUtil.DF_YYYY_MM_DD_CHINA));
                endTime = DateUtil.formatDateTime(date, DateUtil.DF_YYYY_MM_DD);
            })
                    .setTitleText("结束日期")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .build();
        }
        pickerTimeView2.setDate(DateUtil.getCalendar(date));
        pickerTimeView2.show();

    }

    private String time;

    private void showTimePicker3() {
        if (pickerTimeView3 == null) {
            //时间选择器
            pickerTimeView3 = new TimePickerView.Builder(getContext(), (Date date, View v) -> {
                this.date = date;
                time = DateUtil.formatDateTime(date, DateUtil.DF_HH_MM);
                showTimePicker4();

            })
                    .setTitleText("开始时间")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{false, false, false, true, true, false})
                    .build();
        }
        pickerTimeView3.setDate(DateUtil.getCalendar(date));
        pickerTimeView3.show();

    }

    private void showTimePicker4() {
        if (pickerTimeView4 == null) {
            //时间选择器
            pickerTimeView4 = new TimePickerView.Builder(getContext(), (Date date, View v) -> {
                this.date = date;
                time = time + "~" + DateUtil.formatDateTime(date, DateUtil.DF_HH_MM);
                mBinding.tvHour.setText(time);
            })
                    .setTitleText("结束时间")
                    .setTitleColor(getResources().getColor(R.color.theme_text_black))
                    .setTitleBgColor(Color.parseColor("#EFEFF4"))
                    .setBgColor(Color.parseColor("#EFEFF4"))
                    .setType(new boolean[]{false, false, false, true, true, false})
                    .build();
        }
        pickerTimeView4.setDate(DateUtil.getCalendar(date));
        pickerTimeView4.show();

    }


}
