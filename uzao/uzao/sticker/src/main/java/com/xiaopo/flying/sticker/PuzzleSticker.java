package com.xiaopo.flying.sticker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import puzzle.PuzzleLayout;
import puzzle.PuzzlePiece;

/**
 * description:
 * author : zp
 * date: 2017/8/25
 */

public class PuzzleSticker extends Sticker {

    public Drawable drawable;
    private Rect realBounds;
    private List<Bitmap> bitmaps = new ArrayList<>();

    public List<PuzzlePiece> getPuzzlePieces() {
        return puzzlePieces;
    }

    private  List<PuzzlePiece> puzzlePieces = null;


    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public PuzzleLayout getmPuzzleLayout() {
        return mPuzzleLayout;
    }

    public void setmPuzzleLayout(PuzzleLayout mPuzzleLayout) {
        this.mPuzzleLayout = mPuzzleLayout;
    }

    private PuzzleLayout mPuzzleLayout;


    public PuzzleSticker(Drawable drawable, PuzzleLayout puzzleLayout) {
       this(drawable,puzzleLayout,new ArrayList<Bitmap>(),new ArrayList<PuzzlePiece>());

    }

    public PuzzleSticker(Drawable drawable, PuzzleLayout puzzleLayout, List<Bitmap> bitmaps, List<PuzzlePiece> puzzlePieces) {
        mPuzzleLayout = puzzleLayout;
        this.drawable = drawable;
        mapLayer = 1;
        lockLayerOrder = true;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
        this.bitmaps = bitmaps;
        this.puzzlePieces = puzzlePieces;
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
        return drawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    @NonNull
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public PuzzleSticker setDrawable(@NonNull Drawable drawable) {
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
