package com.netmi.workerbusiness.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
                            List<String> synthesize = new ArrayList<>();
                            List<String> confidence = new ArrayList<>();
                            if (data.getData().getList().size() > 0) {
                                for (int i = 0; i < data.getData().getList().size(); i++) {
                                    if (type.equals("1")) {
                                        listX.add(MyTimeUtil.getStringTime3(data.getData().getList().get(i).getTime()));
                                    } else if (type.equals("2")) {   //按周显示
                                        listX.add("第" + (i + 1) + "周");
                                    } else if (type.equals("3")) {   //按月显示
                                        listX.add("第" + (i + 1) + "月");
                                    }
                                    synthesize.add(data.getData().getList().get(i).getSynthesize());
                                    confidence.add(data.getData().getList().get(i).getConfidence());
                                }
                                setChartProperties(mBinding.lineChartSynthesize,data.getData().getList());
                                setChartXAxis(mBinding.lineChartSynthesize,listX);
                                setChartYAxis(mBinding.lineChartSynthesize,data.getData());
                                setData(synthesize,synthesize,mBinding.lineChartSynthesize);

                                setChartProperties(mBinding.lineChartConfidence,data.getData().getList());
                                setChartXAxis(mBinding.lineChartConfidence,listX);
                                setChartYAxis(mBinding.lineChartConfidence,data.getData());
                                setData(confidence,confidence,mBinding.lineChartConfidence);
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

    @SuppressLint("ResourceAsColor")
    private void setChartProperties(LineChart mLineChart, List<HaibeiConfidenceEntity> xValueList) {
        //设置描述文本不显示
        mLineChart.getDescription().setEnabled(false);
        //设置是否显示表格背景
        mLineChart.setDrawGridBackground(true);
        //设置是否可以触摸
        mLineChart.setTouchEnabled(true);
        mLineChart.setDragDecelerationFrictionCoef(0.9f);
        //设置是否可以拖拽
        mLineChart.setDragEnabled(true);
        //设置是否可以缩放
        mLineChart.setScaleEnabled(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setHighlightPerDragEnabled(true);
        mLineChart.setPinchZoom(true);
        //设置背景颜色
        mLineChart.setBackgroundColor(Color.parseColor("#FAFAFA"));
        //设置一页最大显示个数为10，超出部分就滑动
        float ratio = (float) xValueList.size()/(float) 10;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        mLineChart.zoom(ratio,1f,0,0);
        //设置从X轴出来的动画时间
        //mLineChart.animateX(1500);
        //设置XY轴动画
        mLineChart.animateXY(1500,1500, Easing.EaseInSine, Easing.EaseInSine);
        setChartLegend(mLineChart);
    }

    private void setChartLegend(LineChart mLineChart) {
        //获取图例对象
        Legend legend = mLineChart.getLegend();
        //设置图例不显示
        legend.setEnabled(false);
    }
    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    public void setMarkerView(LineChart mLineChart,XAxis xAxis) {
        LineChartMarkView mv = new LineChartMarkView(this, xAxis.getValueFormatter());
        mv.setChartView(mLineChart);
        mLineChart.setMarker(mv);
        mLineChart.invalidate();
    }

    private void setChartXAxis(LineChart mLineChart,List<String> listX) {

        //X轴
        XAxis xAxis = mLineChart.getXAxis();
//        //设置线为虚线
//        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置字体大小10sp
        xAxis.setTextSize(10f);
        //设置X轴字体颜色
        xAxis.setTextColor(Color.parseColor("#000000"));
        //设置从X轴发出横线
        xAxis.setDrawGridLines(false);
        xAxis.setGridColor(Color.GRAY);
        //设置网格线宽度
        xAxis.setGridLineWidth(1);
        //设置显示X轴
        xAxis.setDrawAxisLine(true);
        //设置X轴显示的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //自定义设置横坐标   设置自定义X轴值
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String tradeDate = listX.get((int) value % listX.size());
                return tradeDate;
            }
        });
        //一个界面显示6个Lable，那么这里要设置11个
        xAxis.setLabelCount(13);
        //设置最小间隔，防止当放大时出现重复标签
        xAxis.setGranularity(1f);
        //设置为true当一个页面显示条目过多，X轴值隔一个显示一个
        xAxis.setGranularityEnabled(true);
        //设置X轴的颜色
        xAxis.setAxisLineColor(Color.parseColor("#f4f4f4"));
        //设置X轴的宽度
        xAxis.setAxisLineWidth(1f);
        mLineChart.invalidate();

        setMarkerView(mLineChart,xAxis);
    }

    private void setChartYAxis(LineChart mLineChart,PageEntity<HaibeiConfidenceEntity> xValueList) {
        YAxis leftAxis = mLineChart.getAxisLeft();
        //设置从Y轴发出横向直线(网格线)
        leftAxis.setDrawGridLines(true);
        //设置线为虚线
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置网格线的颜色
        leftAxis.setGridColor(Color.GRAY);
        //设置网格线宽度
        leftAxis.setGridLineWidth(1);
        //设置Y轴最小值是0，从0开始
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularityEnabled(true);

        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value>=10000000){
                    return (value/10000000)+"(千万)";
                }else if(value>=1000000){
                    return (value/1000000)+"(百万)";
                } else if(value>=10000){
                    return (value/10000)+"(万)";
                }
                    return String.valueOf(value);
            }
        });

        //设置Y轴分割数量
        leftAxis.setLabelCount(xValueList.getList().size());
        //设置最小间隔，防止当放大时出现重复标签
        leftAxis.setGranularity(1f);
        //如果沿着轴线的线应该被绘制，则将其设置为true,隐藏Y轴
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setTextSize(10f);
        leftAxis.setTextColor(Color.GRAY);
        //设置左边X轴显示
        leftAxis.setEnabled(true);
        //设置Y轴的颜色
        leftAxis.setAxisLineColor(Color.parseColor("#F4F4F4"));
        //设置Y轴的宽度
        leftAxis.setAxisLineWidth(1f);

        YAxis rightAxis = mLineChart.getAxisRight();
        //设置右边Y轴不显示
        rightAxis.setEnabled(false);
    }

    private void setData(List<String> xValueList, List<String> yValueList,LineChart mLineChart) {
        ArrayList<Entry> yValues = new ArrayList<>();
        for(int i=0;i<xValueList.size();i++){
            if(String.valueOf(Float.parseFloat(yValueList.get(i))).contains("E-4")){
                yValues.add(new Entry(i,Float.parseFloat("0.0010")));
            }else {
                yValues.add(new Entry(i,Float.parseFloat(yValueList.get(i))));
            }

        }

        LineDataSet set;
        if(mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0){
            set = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            set.setValues(yValues);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        }else{
            //设置值给图表
            set = new LineDataSet(yValues, "");
            //设置图标不显示
            set.setDrawIcons(true);
            //设置Y值使用左边Y轴的坐标值
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            //设置线的颜色
            set.setColors(Color.parseColor("#FF2525"));
            //设置数据点圆形的颜色
            set.setCircleColor(Color.GRAY);
            //设置填充圆形中间的颜色
//            set.setCircleColorHole(ColorAndImgUtils.ONE_COLOR);
            //设置折线宽度
            set.setLineWidth(1f);
            //设置折现点圆点半径
            set.setCircleRadius(4f);

            //设置十字线颜色
            set.setHighLightColor(Color.parseColor("#47DBCC"));
            //设置显示十字线，必须显示十字线，否则MarkerView不生效
            set.setHighlightEnabled(true);
            //设置是否在数据点中间显示一个孔
            set.setDrawCircleHole(false);

            //设置填充
            //设置允许填充
            set.setDrawFilled(true);
            set.setFillAlpha(50);
            //设置背景渐变
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                //设置渐变
//                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.line_chart_gradient);
//                set.setFillDrawable(drawable);
            }else {
                set.setFillColor(Color.GRAY);
            }

            LineData data = new LineData(set);
            //设置不显示数据点的值
            data.setDrawValues(true);
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {

                    return String.valueOf(value);
                }
            });
            mLineChart.setData(data);
            mLineChart.invalidate();
        }
    }


}
