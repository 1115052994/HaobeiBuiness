package com.liemi.basemall.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/9 17:37
 * 修改备注：
 */
public class DashLineView extends View {
    /**
     * view中心点的距离
     */
    private int center;
    /**
     * view的高度
     */
    private int height;
    /**
     * 画笔
     */
    private Paint mPaint;

    public DashLineView(Context context) {
        super(context);
        initView();
    }


    public DashLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        center = getMeasuredWidth() / 2;
        height = getMeasuredHeight();
    }

    private void initView() {

        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#c4c4c4"));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(4);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(center, 0, center, 12, mPaint);
        for (int i = 1; i <( height / 14)+2; i++) {
            canvas.drawLine(center, i * 14, center, 12 + i * 14, mPaint);
        }
    }
}
