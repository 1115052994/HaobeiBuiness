package com.netmi.workerbusiness.ui.home;


import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.widget.CommonPopupWindow;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.CommodityApi;
import com.netmi.workerbusiness.data.entity.home.FinancialListEntity;
import com.netmi.workerbusiness.databinding.ActivityFinancialStatementsBinding;
import com.netmi.workerbusiness.databinding.PopRentUniversityBinding;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;


public class FinancialStatementsActivity extends BaseActivity<ActivityFinancialStatementsBinding> {
    //类型 1:日 2:周 3:月
    private String type = "1";
    //类型  分别为 成交额和订单
    private String type2 = "";

    private int popNum; //view=1表示是第一个表
    private int start = 0; //view=1表示是第一个表

    //用户选择商户类型  1:线上 2:线下 3:线上+线下
    private int shop_user_type;

    private BarChart chart1;
    private BarChart chart2;

    private BarChart chart3;
    private BarChart chart4;
    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    private Legend legend;              //图例


    @Override
    protected int getContentView() {
        return R.layout.activity_financial_statements;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("财务报表");
        shop_user_type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        if (shop_user_type == 1) {
            mBinding.llOutline.setVisibility(View.GONE);
        } else if (shop_user_type == 2) {
            mBinding.llLine.setVisibility(View.GONE);
        }

        chart1 = mBinding.chart1;
        initBarChart(chart1);
        chart2 = mBinding.chart2;
        initBarChart(chart2);
        chart3 = mBinding.chart3;
        initBarChart(chart3);
        chart4 = mBinding.chart4;
        initBarChart(chart4);

        getFinancial(type, "amount");
        getFinancial(type, "order");

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_day) {
            chart1.clear();
            initBarChart(chart1);
            chart2.clear();
            initBarChart(chart2);
            chart3.clear();
            initBarChart(chart3);
            chart4.clear();
            initBarChart(chart4);
            type = "1";
            getFinancial(type, "amount");
            getFinancial(type, "order");
            mBinding.tvDay.setTextColor(getResources().getColor(R.color.color_FA8709));
            mBinding.tvWeek.setTextColor(getResources().getColor(R.color.color_353535));
            mBinding.tvMonth.setTextColor(getResources().getColor(R.color.color_353535));
        } else if (id == R.id.tv_week) {
            chart1.clear();
            initBarChart(chart1);
            chart2.clear();
            initBarChart(chart2);
            chart3.clear();
            initBarChart(chart3);
            chart4.clear();
            initBarChart(chart4);
            type = "2";
            getFinancial(type, "amount");
            getFinancial(type, "order");
            mBinding.tvDay.setTextColor(getResources().getColor(R.color.color_353535));
            mBinding.tvWeek.setTextColor(getResources().getColor(R.color.color_FA8709));
            mBinding.tvMonth.setTextColor(getResources().getColor(R.color.color_353535));
        } else if (id == R.id.tv_month) {
            chart1.clear();
            initBarChart(chart1);
            chart2.clear();
            initBarChart(chart2);
            chart3.clear();
            initBarChart(chart3);
            chart4.clear();
            initBarChart(chart4);
            type = "3";
            getFinancial(type, "amount");
            getFinancial(type, "order");
            mBinding.tvDay.setTextColor(getResources().getColor(R.color.color_353535));
            mBinding.tvWeek.setTextColor(getResources().getColor(R.color.color_353535));
            mBinding.tvMonth.setTextColor(getResources().getColor(R.color.color_FA8709));


//            case R.id.tv_time_one:
//                popNum = 1;
//                showNumPop(mBinding.tvTimeOne.getWidth());
//                break;
//            case R.id.tv_time_two:
//                popNum = 2;
//                showNumPop(mBinding.tvTimeTwo.getWidth());
//                break;
//            case R.id.tv_time_three:
//                popNum = 3;
//                showNumPop(mBinding.tvTimeThree.getWidth());
//                break;
//            case R.id.tv_time_four:
//                popNum = 4;
//                showNumPop(mBinding.tvTimeFour.getWidth());
//                break;
        }
    }

    /**
     * 初始化BarChart图表
     */
    private void initBarChart(BarChart barChart) {
        /***图表设置***/
        //背景颜色
        barChart.setBackgroundColor(getResources().getColor(R.color.color_FFF9F9F9));
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //显示边框
        barChart.setDrawBorders(false);
        barChart.setClickable(false);

        //设置动画效果
        barChart.animateY(1000, Easing.Linear);
        barChart.animateX(1000, Easing.Linear);
        //不显示右下角描述
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setGranularity(1f);
        //不显示X Y轴线条 以下一个为轴线，一个为网格线
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);

        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);
        //不显示左右侧Y轴
        leftAxis.setEnabled(true);
        rightAxis.setEnabled(false);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);

        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(getResources().getColor(R.color.color_FFB6B6B6));

        /***折线图例 标签 设置***/
        legend = barChart.getLegend();
        legend.setEnabled(false);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

    }

    /**
     * 柱状图始化设置 一个BarDataSet 代表一列柱状图
     *
     * @param barDataSet 柱状图
     * @param color      柱状图颜色
     */
    private void initBarDataSet(BarDataSet barDataSet, int color) {
        barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        //显示柱状图顶部值
        barDataSet.setDrawValues(true);

        barDataSet.setValueTextColor(getResources().getColor(R.color.color_FFFA8709));
        barDataSet.setValueTextSize(10f);

        if (type2.equals("amount")) {
            barDataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    Log.e("weng", FloatUtils.formatDouble(value) + "");
                    return FloatUtils.formatDouble(value) + "";
                }
            });
        } else if (type2.equals("order")) {
            barDataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int n = (int) value;
                    return n + "";
                }
            });
        }
    }


    public void showBarChart(List<FinancialListEntity.OnlineListBean> dateValueList, String
            name, int color) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dateValueList.size(); i++) {
            /**
             * 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
            if (name.equals("交易额")) {
                BarEntry barEntry = new BarEntry(i, (float) dateValueList.get(i).getAmount());
                entries.add(barEntry);
            } else if (name.equals("订单数量")) {
                BarEntry barEntry = new BarEntry(i, (float) dateValueList.get(i).getCount());
                entries.add(barEntry);
            }
        }

//         每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, name);
        initBarDataSet(barDataSet, color);


        //X轴自定义值  第一个表
        chart1.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

//                Logs.e("chart1", "value：" + value);
                if (value >= dateValueList.size()) return "";

                return dateValueList.get((int) value).getDataStr();
            }
        });


        //X轴自定义值  第二个表
        chart2.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
//                Log.e("weng", String.valueOf((int) value));
//                Logs.e("chart2", "value：" + value);
                if (value >= dateValueList.size()) return "";
                return dateValueList.get((int) value).getDataStr();
            }
        });

        BarData data = new BarData(barDataSet);
        if (name.equals("交易额")) {
            chart1.setData(data);
            chart1.notifyDataSetChanged();
            chart1.getData().notifyDataChanged();
        } else if (name.equals("订单数量")) {
            chart2.setData(data);
            chart2.notifyDataSetChanged();
            chart2.getData().notifyDataChanged();
        }
    }

    public void showBarChartOutline(List<FinancialListEntity.OutlineListBean> dateValueList, String
            name, int color) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dateValueList.size(); i++) {
            /**
             * 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
            if (name.equals("交易额")) {
                BarEntry barEntry = new BarEntry(i, (float) dateValueList.get(i).getAmount());
                entries.add(barEntry);
            } else if (name.equals("订单数量")) {
                BarEntry barEntry = new BarEntry(i, (float) dateValueList.get(i).getCount());
                entries.add(barEntry);
            }
        }

//         每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, name);
        initBarDataSet(barDataSet, color);


        //X轴自定义值  第一个表
        chart3.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

//                Logs.e("chart1", "value：" + value);
                if (value >= dateValueList.size()) return "";

                return dateValueList.get((int) value).getDataStr();
            }
        });


        //X轴自定义值  第二个表
        chart4.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
//                Log.e("weng", String.valueOf((int) value));
//                Logs.e("chart2", "value：" + value);
                if (value >= dateValueList.size()) return "";
                return dateValueList.get((int) value).getDataStr();
            }
        });

        BarData data = new BarData(barDataSet);
        if (name.equals("交易额")) {
            chart3.setData(data);
            chart3.notifyDataSetChanged();
            chart3.getData().notifyDataChanged();
        } else if (name.equals("订单数量")) {
            chart4.setData(data);
            chart4.notifyDataSetChanged();
            chart4.getData().notifyDataChanged();
        }
    }

    @Override
    protected void initData() {

    }


    private CommonPopupWindow numPop;

    /**
     * 下拉框
     */
    private void showNumPop(int width) {
        if (numPop == null) {
            numPop = new CommonPopupWindow<PopRentUniversityBinding>(getActivity(), R.layout.pop_rent_university,
                    width, ViewGroup.LayoutParams.WRAP_CONTENT) {
                @Override
                protected void initView() {
                    popBinding.rvMenu.setLayoutManager(new LinearLayoutManager(getContext()));
                    BaseRViewAdapter<String, BaseViewHolder<String>> adapter;
                    popBinding.rvMenu.setAdapter(adapter = new BaseRViewAdapter<String, BaseViewHolder<String>>(getContext()) {
                        @Override
                        public int layoutResId(int viewType) {
                            return R.layout.item_pop_topping_card;
                        }

                        @Override
                        public BaseViewHolder<String> holderInstance(ViewDataBinding binding) {
                            return new BaseViewHolder<String>(binding) {
                                @Override
                                public void doClick(View view) {
                                    super.doClick(view);
                                    if (popNum == 1) {
                                        mBinding.tvTimeOne.setText(items.get(position));
                                    } else if (popNum == 2) {
                                        mBinding.tvTimeTwo.setText(items.get(position));
                                    } else if (popNum == 3) {
                                        mBinding.tvTimeThree.setText(items.get(position));
                                    } else if (popNum == 4) {
                                        mBinding.tvTimeFour.setText(items.get(position));
                                    }

                                    //  类型 1:日 2:周 3:月
                                    if (items.get(position).equals("日")) {
                                        type = "1";
                                    } else if (items.get(position).equals("周")) {
                                        type = "2";
                                    } else if (items.get(position).equals("月")) {
                                        type = "3";
                                    }
                                    if (popNum == 1) {
                                        chart1.clear();
                                        initBarChart(chart1);
                                        getFinancial(type, "amount");
                                    } else if (popNum == 2) {
                                        chart2.clear();
                                        initBarChart(chart2);
                                        getFinancial(type, "order");
                                    } else if (popNum == 3) {
                                        chart3.clear();
                                        initBarChart(chart3);
                                        getFinancial(type, "amount");
                                    } else if (popNum == 4) {
                                        chart4.clear();
                                        initBarChart(chart4);
                                        getFinancial(type, "order");
                                    }
                                    numPop.getPopupWindow().dismiss();
                                }
                            };
                        }
                    });
                    adapter.insert("日");
                    adapter.insert("周");
                    adapter.insert("月");
                }

                @Override
                protected void initEvent() {
                }
            };
        }
        CommonPopupWindow.LayoutGravity layoutGravity = new CommonPopupWindow.LayoutGravity(CommonPopupWindow.LayoutGravity.TO_BOTTOM);
        if (popNum == 1) {
            numPop.showBashOfAnchor(mBinding.tvTimeOne, layoutGravity, 0, 0);
        } else if (popNum == 2) {
            numPop.showBashOfAnchor(mBinding.tvTimeTwo, layoutGravity, 0, 0);
        } else if (popNum == 3) {
            numPop.showBashOfAnchor(mBinding.tvTimeThree, layoutGravity, 0, 0);
        } else if (popNum == 4) {
            numPop.showBashOfAnchor(mBinding.tvTimeFour, layoutGravity, 0, 0);
        }
    }


    private void getFinancial(String type, String amount_order) {
        RetrofitApiFactory.createApi(CommodityApi.class)
                .financialList(type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<FinancialListEntity>>() {
                    @Override
                    public void onSuccess(BaseData<FinancialListEntity> data) {
//                        Collections.reverse(data.getData().getOnline_list());//将集合 逆序排列，转换成需要的顺序

                        if (amount_order.equals("amount")) {
                            type2 = "amount";
                            showBarChart(data.getData().getOnline_list(), "交易额", getResources().getColor(R.color.color_FFFA8709));
                            showBarChartOutline(data.getData().getOutline_list(), "交易额", getResources().getColor(R.color.color_FFFA8709));
                        } else if (amount_order.equals("order")) {
                            type2 = "order";
                            showBarChart(data.getData().getOnline_list(), "订单数量", getResources().getColor(R.color.color_FFFA8709));
                            showBarChartOutline(data.getData().getOutline_list(), "订单数量", getResources().getColor(R.color.color_FFFA8709));
                        }
//                        } else {  //以下为每次点击时加载单独表格数据
//                            if (amount_order.equals("amount")) {
//                                type2 = "amount";
//                                // //用户选择商户类型  1:线上 2:线下 3:线上+线下
//                                if (popNum == 1) {
//                                    showBarChart(data.getData().getOnline_list(), "交易额", getResources().getColor(R.color.color_FFFA8709));
//                                } else if (popNum == 3) {
//                                    showBarChartOutline(data.getData().getOutline_list(), "交易额", getResources().getColor(R.color.color_FFFA8709));
//                                }
//                            } else if (amount_order.equals("order")) {
//                                type2 = "order";
//                                if (popNum == 2) {
//                                    showBarChart(data.getData().getOnline_list(), "订单数量", getResources().getColor(R.color.color_FFFA8709));
//                                } else if (popNum == 4) {
//                                    showBarChartOutline(data.getData().getOutline_list(), "订单数量", getResources().getColor(R.color.color_FFFA8709));
//                                }
//                            }
//                        }
                    }
                });
    }

}
