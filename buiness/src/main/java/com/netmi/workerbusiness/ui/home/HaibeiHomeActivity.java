package com.netmi.workerbusiness.ui.home;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.CouponApi;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.home.HaibeiConfidenceEntity;
import com.netmi.workerbusiness.data.entity.mine.ContentEntity;
import com.netmi.workerbusiness.databinding.ActivityHaibeiHomeBinding;
import com.netmi.workerbusiness.utils.HTMLFormat;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;

public class HaibeiHomeActivity extends BaseActivity<ActivityHaibeiHomeBinding> {

    //获取价格天数
    private int days = 7;
    //1天2周3月
    private String type;

    private XAxis x;
    private XAxis x2;
    private YAxis y;
    private YAxis y2;

    private int count = 0;

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_haibei_home;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("海贝指数");
        initChart();
        //默认为天
        type = "1";
        getFinancial();
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.ll_question) {
            service(39);
        } else if (view.getId() == R.id.cb_day) {
            mBinding.day.setChecked(true);
            mBinding.day.setTextColor(getResources().getColor(R.color.white));
            mBinding.week.setChecked(false);
            mBinding.week.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.month.setChecked(false);
            mBinding.month.setTextColor(getResources().getColor(R.color.theme_text_black));
            type = "1";
            mBinding.tvOneHint.setText("(近7天)");
            mBinding.tvTwoHint.setText("(近7天)");
//            x.setLabelCount(7);
//            x2.setLabelCount(7);
//            y.setLabelCount(7);
//            y2.setLabelCount(7);
            getFinancial();
        } else if (view.getId() == R.id.cb_week) {
            mBinding.day.setChecked(false);
            mBinding.day.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.week.setChecked(true);
            mBinding.week.setTextColor(getResources().getColor(R.color.white));
            mBinding.month.setChecked(false);
            mBinding.month.setTextColor(getResources().getColor(R.color.theme_text_black));
            type = "2";
            mBinding.tvOneHint.setText("(近12周)");
            mBinding.tvTwoHint.setText("(近12周)");
//            x.setLabelCount(12);
//            x2.setLabelCount(12);
//            y.setLabelCount(12);
//            y2.setLabelCount(12);
            getFinancial();
        } else if (view.getId() == R.id.cb_month) {
            mBinding.day.setChecked(false);
            mBinding.day.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.week.setChecked(false);
            mBinding.week.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.month.setChecked(true);
            mBinding.month.setTextColor(getResources().getColor(R.color.white));
            type = "3";
            mBinding.tvOneHint.setText("(近12月)");
            mBinding.tvTwoHint.setText("(近12月)");
//            x.setLabelCount(12);
//            x2.setLabelCount(12);
//            y.setLabelCount(12);
//            y2.setLabelCount(12);
            getFinancial();
        }
    }

    private void initChart() {
//        topBinding.chart.setViewPortOffsets(0, 0, 0, 0);
//        topBinding.chart.setBackgroundColor(Color.rgb(104, 241, 175));
        // no description text
        mBinding.chart.getDescription().setEnabled(false);
        mBinding.chart2.getDescription().setEnabled(false);
        // enable touch gestures
        mBinding.chart.setTouchEnabled(false);
        mBinding.chart2.setTouchEnabled(false);
        // enable scaling and dragging
        mBinding.chart.setDragEnabled(true);
        mBinding.chart2.setDragEnabled(true);
        mBinding.chart.setScaleEnabled(true);
        mBinding.chart2.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        mBinding.chart.setPinchZoom(false);
        mBinding.chart2.setPinchZoom(false);
//        mBinding.chart.setDrawGridBackground(true);
//        mBinding.chart.setMaxHighlightDistance(300);
        setX();
/**
 * 信心指数
 * */
        y = mBinding.chart.getAxisLeft();
//        y.setTypeface(tfLight);
//        y.setLabelCount(days, true);
        y.setTextColor(Color.parseColor("#9CABDB"));
        y.setTextSize(8);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(true);
//        y.setAxisLineColor(Color.GREEN);
        mBinding.chart.getAxisRight().setEnabled(false);
        mBinding.chart.getLegend().setEnabled(false);
        mBinding.chart.animateXY(2000, 2000);
        mBinding.chart.setDrawBorders(false);
        // don't forget to refresh the drawing
        mBinding.chart.invalidate();

        /**
         * 综合指数
         * */
        y2 = mBinding.chart2.getAxisLeft();
//        y.setTypeface(tfLight);
//        y2.setLabelCount(days, true);
        y2.setTextColor(Color.parseColor("#9CABDB"));
        y2.setTextSize(8);
        y2.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y2.setDrawGridLines(true);
//        y.setAxisLineColor(Color.GREEN);
        mBinding.chart2.getAxisRight().setEnabled(false);
        mBinding.chart2.getLegend().setEnabled(false);
        mBinding.chart2.animateXY(2000, 2000);
        mBinding.chart2.setDrawBorders(false);
        // don't forget to refresh the drawing
        mBinding.chart2.invalidate();
    }

    //设置x轴
    private void setX() {
        /**
         * 信心指数
         * */
        x = mBinding.chart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
//        x.setLabelCount(days, true);
        x.setTextColor(Color.parseColor("#9CABDB"));
        x.setDrawGridLines(false);
        /**
         * 综合指数
         * */
        x2 = mBinding.chart2.getXAxis();
        x2.setEnabled(true);
        x2.setPosition(XAxis.XAxisPosition.BOTTOM);
//        x2.setLabelCount(days, true);
        x2.setTextColor(Color.parseColor("#9CABDB"));
        x2.setDrawGridLines(false);
    }

    public void setData(final List<HaibeiConfidenceEntity> list, String type) {
        ArrayList<Entry> values = new ArrayList<>();
        values.clear();
        if (type.equals("1")) {
            for (int i = 6; i >= 0; i--) {
                values.add(0, new Entry(i, Float.valueOf(list.get(i).getConfidence())));
            }
        } else {
            for (int i = 11; i >= 0; i--) {
                values.add(0, new Entry(i, Float.valueOf(list.get(i).getConfidence())));
//             整个图像左右翻转
//            values.add(0, new Entry(i, list.get(6 - i).getPrice()));
            }
        }

        LineDataSet set1;
//        if (mBinding.chart.getData() != null &&
//                mBinding.chart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) mBinding.chart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            mBinding.chart.getData().notifyDataChanged();
//            mBinding.chart.notifyDataSetChanged();
//        } else {
        if (mBinding.chart.getData() != null &&
                mBinding.chart.getData().getDataSetCount() > 0) {
            mBinding.chart.getData().clearValues();
            mBinding.chart.getLineData().clearValues();

            mBinding.chart.setScaleMinima(1.0f, 1.0f);
            mBinding.chart.getViewPortHandler().refresh(new Matrix(), mBinding.chart, true);
        }
        // create a dataset and give it a type
        set1 = new LineDataSet(values, "DataSet " + type);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setCircleRadius(4f);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(getResources().getColor(R.color.orange_FF7030));
        /**设置图表线的颜色*/
        set1.setColor(getResources().getColor(R.color.orange_FF7030));
        set1.setFillDrawable(getResources().getDrawable(R.drawable.fillcolor_form));
        set1.setFillAlpha(20);
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return mBinding.chart.getAxisLeft().getAxisMinimum();
            }
        });
        // create a data object with the data sets
        LineData data = new LineData(set1);
//            data.setValueTypeface(tfLight);
        data.setValueTextSize(9f);
        //设置是否显示图表线上的数字标注
        data.setDrawValues(true);

        XAxis x = mBinding.chart.getXAxis();
        if (type.equals("1")) {
            x.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return getDate(list.get((int) value).getTime()); //mList为存有月份的集合
                }
            });
        } else if (type.equals("2")) {
            x.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return "第" + (((int) value) + 1) + "周"; //mList为存有月份的集合
                }
            });
        } else if (type.equals("3")) {
            count = 0;
            x.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return "第" + (((int) value) + 1) + "月"; //mList为存有月份的集合
                }
            });
        }
        // set data
        mBinding.chart.setData(data);
        mBinding.chart.invalidate();
    }
//    }


    public void setData2(final List<HaibeiConfidenceEntity> list, String type) {
        ArrayList<Entry> values = new ArrayList<>();
        values.clear();
        if (type.equals("1")) {
            for (int i = 6; i >= 0; i--) {
                values.add(0, new Entry(i, Float.valueOf(list.get(i).getSynthesize())));
            }
        } else {
            for (int i = 11; i >= 0; i--) {
                values.add(0, new Entry(i, Float.valueOf(list.get(i).getSynthesize())));
//             整个图像左右翻转
//            values.add(0, new Entry(i, list.get(6 - i).getPrice()));
            }
        }

        LineDataSet set1;
//        if (mBinding.chart2.getData() != null &&
//                mBinding.chart2.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) mBinding.chart2.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            mBinding.chart2.getData().notifyDataChanged();
//            mBinding.chart2.notifyDataSetChanged();
//        } else {
        if (mBinding.chart2.getData() != null &&
                mBinding.chart2.getData().getDataSetCount() > 0) {
            mBinding.chart2.getData().clearValues();
            mBinding.chart2.getLineData().clearValues();

            mBinding.chart2.setScaleMinima(1.0f, 1.0f);
            mBinding.chart2.getViewPortHandler().refresh(new Matrix(), mBinding.chart2, true);
        }
        // create a dataset and give it a type
        set1 = new LineDataSet(values, "DataSet " + type);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setCircleRadius(4f);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(getResources().getColor(R.color.orange_FF7030));
        /**设置图表线的颜色*/
        set1.setColor(getResources().getColor(R.color.orange_FF7030));
        set1.setFillDrawable(getResources().getDrawable(R.drawable.fillcolor_form));
        set1.setFillAlpha(20);

        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return mBinding.chart2.getAxisLeft().getAxisMinimum();
            }
        });
        // create a data object with the data sets
        LineData data = new LineData(set1);
//            data.setValueTypeface(tfLight);
        data.setValueTextSize(9f);
        //设置是否显示图表线上的数字标注
        data.setDrawValues(true);

        XAxis x = mBinding.chart2.getXAxis();
        if (type.equals("1")) {
            x.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return getDate(list.get((int) value).getTime()); //mList为存有月份的集合
                }
            });
        } else if (type.equals("2")) {
            x.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return "第" + (((int) value) + 1) + "周"; //mList为存有月份的集合
                }
            });
        } else if (type.equals("3")) {
            x.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return "第" + (((int) value) + 1) + "月"; //mList为存有月份的集合
                }
            });
        }
        // set data
        mBinding.chart2.setData(data);
        mBinding.chart2.invalidate();

    }
//    }


    private String getDate(String date) {
        String[] strs = date.split("-");
        if (strs.length > 2)
            return strs[1] + "-" + strs[2];
        return date;
    }

    private void getFinancial() {
        RetrofitApiFactory.createApi(CouponApi.class)
                .doList(type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<HaibeiConfidenceEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<HaibeiConfidenceEntity>> data) {
//                        Collections.reverse(data.getData().getOnline_list());//将集合 逆序排列，转换成需要的顺序
                        setData(data.getData().getList(), type);
                        setData2(data.getData().getList(), type);
//                        mBinding.tvOne.setText(data.getData().get(data.getData().size() - 1).getOne());
//                        mBinding.tvTwo.setText(data.getData().get(data.getData().size() - 1).getTwo());
                    }
                });
    }

    private void service(Integer type) {
        RetrofitApiFactory.createApi(MineApi.class)
                .content(type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ContentEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ContentEntity> data) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WEBVIEW_TITLE, data.getData().getTitle());
                        bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_CONTENT);
                        bundle.putString(WEBVIEW_CONTENT, HTMLFormat.getNewData(data.getData().getContent()));
                        JumpUtil.overlay(getContext(), BaseWebviewActivity.class, bundle);
                    }
                });
    }
}
