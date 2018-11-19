package com.xiaopo.flying.sticker;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * description:
 * author : ZP
 * date: 2018/1/31 0031.
 */

public class EmptySticker extends Sticker {
    @Override
    public void draw(@NonNull Canvas canvas) {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @NonNull
    @Override
    public Drawable getDrawable() {
        return null;
    }

    @Override
    public Sticker setDrawable(@NonNull Drawable drawable) {
        return null;
    }

    @NonNull
    @Override
    public Sticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        return null;
    }
}
