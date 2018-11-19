package com.zhaotai.uzao.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.zhaotai.uzao.R;

public class HKImageView extends ImageView {

    public HKImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public HKImageView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPadding(3, 3, 5, 5);
        // 画边框  
        Rect rect1 = getRect(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);



        paint.setColor(Color.LTGRAY);

        Bitmap shadow = BitmapFactory.decodeResource(getResources(), R.drawable.bg_shadow_jointly);
        Rect rect = getRect(canvas);
        RectF rectF = new RectF(rect.left, rect.bottom-3, rect.right, rect.bottom + 20);

        canvas.drawBitmap(shadow, null, rectF, paint);

    }

    Rect getRect(Canvas canvas) {
        Rect rect = canvas.getClipBounds();
        rect.bottom -= getPaddingBottom();
        rect.right -= getPaddingRight();
        rect.left += getPaddingLeft();
        rect.top += getPaddingTop();
        return rect;
    }
}  