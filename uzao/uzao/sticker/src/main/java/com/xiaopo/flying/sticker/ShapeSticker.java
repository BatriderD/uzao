package com.xiaopo.flying.sticker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author : zp
 * date: 2017/8/25
 */

public class ShapeSticker extends Sticker {

    public Drawable drawable;
    private Rect realBounds;
    public Bitmap shapeTemplate;

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    private List<Bitmap> bitmaps =new ArrayList<>();
    public Drawable orgBitmap = null;
    public Matrix innerMatrix = null;

    public ShapeSticker(@NonNull Drawable drawable, @NonNull Bitmap shapeTemplate) {
        mapLayer = 1;
        lockLayerOrder = true;
        this.drawable = drawable;
        this.shapeTemplate = shapeTemplate;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.concat(getMatrix());
        drawable.setBounds(realBounds);
        drawable.setColorFilter(new ColorMatrixColorFilter(ColorMatrixUtils.handleImageEffect(colorData.getContrast(),colorData.getSaturation(),colorData.getBrightness())));
        drawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public int getWidth() {
        return shapeTemplate.getWidth();
    }

    @Override
    public int getHeight() {
        return shapeTemplate.getHeight();
    }

    @NonNull
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public ShapeSticker setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        return this;
    }


    @NonNull
    @Override
    public Sticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        return null;
    }


    @Override
    public void release() {
        super.release();
        if (drawable != null) {
            drawable = null;
        }
    }
}
