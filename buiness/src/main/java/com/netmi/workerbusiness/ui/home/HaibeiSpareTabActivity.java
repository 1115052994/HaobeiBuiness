package com.netmi.workerbusiness.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.CouponApi;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.home.HaibeiConfidenceEntity;
import com.netmi.workerbusiness.data.entity.mine.ContentEntity;
import com.netmi.workerbusiness.databinding.ActivityHaibeiSpareBinding;
import com.netmi.workerbusiness.databinding.ActivityHaibeiSpareTabBinding;
import com.netmi.workerbusiness.ui.utils.MyTimeUtil;
import com.netmi.workerbusiness.ui.view.LineChartMarkView;
import com.netmi.workerbusiness.utils.HTMLFormat;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;

public class HaibeiSpareTabActivity extends BaseActivity<ActivityHaibeiSpareTabBinding> {

    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例
    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_haibei_spare_tab;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("海贝指数");
        getRightSetting().setVisibility(View.VISIBLE);
        mBinding.tvRemark1.setText("（近7日）");
        mBinding.tvRemark2.setText("（近7日）");
        getFinancial("1");


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
            mBinding.tvRemark1.setText("（近7日）");
            mBinding.tvRemark2.setText("（近7日）");
            getFinancial("1");
        } else if (view.getId() == R.id.cb_week) {
            mBinding.day.setChecked(false);
            mBinding.day.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.week.setChecked(true);
            mBinding.week.setTextColor(getResources().getColor(R.color.white));
            mBinding.month.setChecked(false);
            mBinding.month.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.tvRemark1.setText("（近12周）");
            mBinding.tvRemark2.setText("（近12周）");
            getFinancial("2");
        } else if (view.getId() == R.id.cb_month) {
            mBinding.day.setChecked(false);
            mBinding.day.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.week.setChecked(false);
            mBinding.week.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.month.setChecked(true);
            mBinding.month.setTextColor(getResources().getColor(R.color.white));
            mBinding.tvRemark1.setText("（近12月）");
            mBinding.tvRemark2.setText("（近12月）");
            getFinancial("3");
        }
    }



    private void getFinancial(String type) {
        RetrofitApiFactory.createApi(CouponApi.class)
                .doList(type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<HaibeiConfidenceEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<HaibeiConfidenceEntity>> data) {
                        if (dataExist(data)) {

                            List<String> listX = new ArrayList<>();
                            List<Entry> synthesize = new ArrayList<>();
                            List<Entry> confidence = new ArrayList<>();
                            if (data.getData().getList().size() > 0) {

                                for (int i = 0; i < data.getData().getList().size(); i++) {
                                    if (type.equals("1")) {
                                        listX.add(MyTimeUtil.getStringTime3(data.getData().getList().get(i).getTime()));
                                    } else if (type.equals("2")) {   //按周显示
                                        listX.add("第" + (i + 1) + "周");
                                    } else if (type.equals("3")) {   //按月显示
                                        listX.add("第" + (i + 1) + "月");
                                    }
                                    Entry syn = new Entry(i,(float) data.getData().getList().get(i).getConfidence());
                                    Entry con = new Entry(i,(float)data.getData().getList().get(i).getConfidence());
                                    synthesize.add(syn);
                                    confidence.add(con);
                                }
                                initChart(mBinding.lineChartSynthesize,Float.valueOf(data.getData().getMin_synthesize()),Float.valueOf(data.getData().getMax_synthesize()));
                                showLineChart(synthesize,listX, "我的收益", Color.CYAN,mBinding.lineChartSynthesize);
//                                initChart(mBinding.lineChartConfidence,Float.valueOf(data.getData().getMin_confidence()),Float.valueOf(data.getData().getMax_confidence()));
//                                showLineChart(confidence,listX, "我的收益", Color.CYAN,mBinding.lineChartConfidence);
                            }
                            mBinding.setData(data.getData());
                        }
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


    /**
     * 初始化图表
     */
    private void initChart(LineChart lineChart,float minimum,float maxmum) {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否可以拖动
        lineChart.setDragEnabled(true);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(minimum);
        xAxis.setGranularity(0.01f);
        xAxis.setAxisMaxValue(maxmum);

        //还是显示了网格线，而且不是我们想要的 虚线 。其实那是 X Y轴自己的网格线，禁掉即可
        xAxis.setDrawGridLines(false);
        rightYaxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);
        //设置X Y轴网格线为虚线
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        rightYaxis.setEnabled(false);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYaxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
    }


    /**
     * 展示曲线
     *
     * @param dataList 数据集合
     * @param name     曲线名称
     * @param color    曲线颜色
     */
    public void showLineChart(List<Entry> dataList,List<String> listX, String name, int color, LineChart lineChart) {

        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(dataList, name);
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);


        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Log.e("weng", FloatUtils.formatDouble(value) + "");
                return listX.get((int) value % listX.size());
            }
        });
        //设置X轴分割数量
        xAxis.setLabelCount(dataList.size(),false);

        //设置Y轴分割数量
        leftYAxis.setLabelCount(dataList.size(),false);

        setMarkerView(lineChart,xAxis);

        //设置右下角文字
        Description description = new Description();
        description.setText("");
        description.setEnabled(false);
        lineChart.setDescription(description);

    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置线性渐变
        lineDataSet.setFillColor(Color.parseColor("#ffffff"));
        //不显示圆点
        lineDataSet.setDrawCircles(false);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    public void setMarkerView(LineChart lineChart, XAxis xAxis) {
        LineChartMarkView mv = new LineChartMarkView(this, xAxis.getValueFormatter());
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        lineChart.invalidate();
    }

}
