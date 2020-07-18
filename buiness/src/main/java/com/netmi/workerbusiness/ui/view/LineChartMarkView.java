package com.netmi.workerbusiness.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.netmi.workerbusiness.R;
import java.text.DecimalFormat;

public class LineChartMarkView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue;
    private Context context;
    private IAxisValueFormatter xAxisValueFormatter;
    DecimalFormat df = new DecimalFormat(".00");

    public LineChartMarkView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.layout_markview);
        this.xAxisValueFormatter = xAxisValueFormatter;
        this.context = context;

        tvDate = findViewById(R.id.tv_date);
        tvValue = findViewById(R.id.tv_value);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //展示自定义X轴值 后的X轴内容
        tvDate.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
        tvValue.setText("分数：" + e.getY());
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {

        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

    /**
     *  重写绘制方法   如果不需要固定显示直接删除就行
     */
    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        Chart chart = getChartView();
        //绘制方法是直接复制过来的，没动
        int saveId = canvas.save();
      //因为上面和下面也会出现遮挡的效果，所以我这边直接将M a r ke r Vi e w显示的Y轴的高度固定了///
        canvas.translate(chart.getWidth()-getWidth(), 0);
        draw(canvas);
        canvas.restoreToCount(saveId);
    }




}

