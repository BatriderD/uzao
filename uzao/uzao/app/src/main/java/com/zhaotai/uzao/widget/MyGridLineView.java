package com.zhaotai.uzao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhaotai.uzao.R;

/**
 * description:
 * author : ZP
 * date: 2017/11/21 0021.
 */

public class MyGridLineView extends View {

    private Paint mPaint;

    private int mLineColor, mStokeColor;
    private int strokeWidth, lineWidth;
    private int lineSpacing = 50;

    public MyGridLineView(Context context) {
        this(context, null);
    }

    public MyGridLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGridLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyGridLineView);
        mLineColor = typedArray.getColor(R.styleable.MyGridLineView_gridLineColor, Color.BLACK);
        mStokeColor = typedArray.getColor(R.styleable.MyGridLineView_gridStokeColor, Color.BLACK);
        strokeWidth = typedArray.getInt(R.styleable.MyGridLineView_gridStrokeWidth, 4);
        lineWidth = typedArray.getInt(R.styleable.MyGridLineView_gridLineWidth, 4);
        lineSpacing = typedArray.getInt(R.styleable.MyGridLineView_gridLineSpacing, 50);
        typedArray.recycle();


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();


        // 创建矩形框
        Rect mRect = new Rect(0, 0, measuredWidth, measuredHeight);
//        设置画笔
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(mStokeColor);
        // 绘制边框
        canvas.drawRect(mRect, mPaint);

//        画线
//        设置画笔
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(lineWidth);
        PathEffect effects = new DashPathEffect(new float[]{5, 5}, 1);
        mPaint.setPathEffect(effects);//线的显示效果：破折号格式
//画线
        int widthLines = (int) Math.ceil(measuredWidth / lineSpacing);
        for (int i = 1; i <= widthLines; i++) {
            int drawWidth = lineSpacing * i;
//            画竖线
            canvas.drawLine(drawWidth, 0, drawWidth, measuredHeight, mPaint);

        }

        int heightLines = (int) Math.ceil(measuredHeight / lineSpacing);
        for (int i = 1; i <= heightLines; i++) {
            int drawHeight = lineSpacing * i;
            //            画横线
            canvas.drawLine(0, drawHeight, measuredWidth, drawHeight, mPaint);
        }
    }

    public int getmLineColor() {
        return mLineColor;
    }

    public void setmLineColor(int mLineColor) {
        this.mLineColor = mLineColor;
    }

    public int getmStokeColor() {
        return mStokeColor;
    }

    public void setmStokeColor(int mStokeColor) {
        this.mStokeColor = mStokeColor;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public void hideLine(){

    }
}
