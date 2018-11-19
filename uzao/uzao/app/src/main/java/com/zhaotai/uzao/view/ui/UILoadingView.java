package com.zhaotai.uzao.view.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.utils.PixelUtil;

/**
 * Time: 2017/8/31
 * Created by LiYou
 * Description :
 * 用于显示 Loading 的 {@link View}，支持颜色和大小的设置。
 */

public class UILoadingView extends View {
    private int mSize;
    private int mPaintColor;
    private int mAnimateValue = 0;
    private ValueAnimator mAnimator;
    private Paint mPaint;
    private static final int LINE_COUNT = 12;
    private static final int DEGREE_PER_LINE = 360 / LINE_COUNT;
    private int progress = 0;
    private boolean showProgress = false;
    private TextPaint mTextPaint;

    public UILoadingView(Context context) {
        this(context, null);
    }

    public UILoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.UILoadingStyle);
    }

    public UILoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.UILoadingView, defStyleAttr, 0);
        mSize = array.getDimensionPixelSize(R.styleable.UILoadingView_ui_loading_view_size, (int)PixelUtil.dp2px(32,context));
        mPaintColor = array.getInt(R.styleable.UILoadingView_android_color, Color.WHITE);
        array.recycle();
        initPaint();
    }

    public UILoadingView(Context context, int size, int color) {
        super(context);
        mSize = size;
        mPaintColor = color;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.progress_text));
        mTextPaint.setTextSize(24);
    }

    public void setColor(int color) {
        mPaintColor = color;
        mPaint.setColor(color);
        invalidate();
    }

    public void setSize(int size) {
        mSize = size;
        requestLayout();
    }

    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mAnimateValue = (int) animation.getAnimatedValue();
            invalidate();
        }
    };

    public void start() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofInt(0, LINE_COUNT - 1);
            mAnimator.addUpdateListener(mUpdateListener);
            mAnimator.setDuration(600);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.start();
        } else if (!mAnimator.isStarted()) {
            mAnimator.start();
        }
    }

    public void stop() {
        if (mAnimator != null) {
            mAnimator.removeUpdateListener(mUpdateListener);
            mAnimator.removeAllUpdateListeners();
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    private void drawLoading(Canvas canvas, int rotateDegrees) {
        int width = mSize / 12, height = mSize / 6;
        mPaint.setStrokeWidth(width);

        canvas.rotate(rotateDegrees, mSize / 2, mSize / 2);
        canvas.translate(mSize / 2, mSize / 2);

        for (int i = 0; i < LINE_COUNT; i++) {
            canvas.rotate(DEGREE_PER_LINE);
//            mPaint.setAlpha((int) (255f * (i + 1) / LINE_COUNT));
            mPaint.setColor(getCurrentColor(mPaintColor, Color.WHITE, i, LINE_COUNT));
            canvas.translate(0, -mSize / 2 + width / 2);
            canvas.drawLine(0, 0, 0, height, mPaint);
            canvas.translate(0, mSize / 2 - width / 2);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showProgress) {
            drawProgress(canvas);
        }
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        drawLoading(canvas, mAnimateValue * DEGREE_PER_LINE);
        canvas.restoreToCount(saveCount);
    }

    /**
     * 画进度
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        String text = String.valueOf(progress);

        String.valueOf(progress);
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        int width = rect.width();//文字宽
        int height = rect.height();//文字高
        canvas.drawText(text, 0, text.length(), (mSize - width) / 2, (mSize+height) / 2, mTextPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            start();
        } else {
            stop();
        }
    }

    private int getCurrentColor(int fromColor, int toColor, int progress, int max) {
        int r = Color.red(fromColor) + (Color.red(toColor) - Color.red(fromColor)) * progress / max;
        int g = Color.green(fromColor) + (Color.green(toColor) - Color.green(fromColor)) * progress / max;
        int b = Color.blue(fromColor) + (Color.blue(toColor) - Color.blue(fromColor)) * progress / max;

        return Color.rgb(r, g, b);
    }

    /**
     * 最大值100
     *
     * @param progress 当前进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void showProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }
}
