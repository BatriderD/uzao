package com.zhaotai.uzao.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public class BitmapColorUtils {
    private Paint mPaint;
    private ColorMatrix cMatrix = new ColorMatrix();
    private Canvas canvas;
    private final Bitmap bitmapCopy;
    private Bitmap mOrgBitmap;

    public BitmapColorUtils(Bitmap orgBitmap) {
        mOrgBitmap = orgBitmap;
        bitmapCopy = Bitmap.createBitmap(orgBitmap.getWidth(), orgBitmap.getWidth(), Bitmap.Config.ARGB_8888);
        mPaint = new Paint();
        canvas = new Canvas(bitmapCopy);
        canvas.save();
    }

    public Bitmap ChangeSaturation(int progress) {

        // 设置饱和度
        float bhd = (float) (progress / 100.0);
        System.out.println("啦啦啦 我是饱和度"+bhd);
        cMatrix.setSaturation(bhd);

        mPaint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

        // 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
        canvas.drawBitmap(mOrgBitmap, 0, 0, mPaint);
        return  bitmapCopy;
    }

    private Bitmap getChangedBitmap() {
        canvas.restore();
        mPaint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        canvas.drawBitmap(mOrgBitmap, 0, 0, mPaint);
        return bitmapCopy;
    }
}
