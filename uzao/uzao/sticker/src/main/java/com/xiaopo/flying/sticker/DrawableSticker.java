package com.xiaopo.flying.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * @author wupanjie
 */
public class DrawableSticker extends Sticker {


    public Drawable drawable;
    private Rect realBounds;
    private boolean isAlph;
    //显示图片地址
    private String showPicPath;
    //原始地址图片url
    private String originalUrl;
    //压缩后的地址
    private String compressPath;
    //原始图片地址
    private String originalAddressPath;

    public DrawableSticker() {

    }
    public DrawableSticker(Drawable drawable) {
        this.drawable = drawable;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
    }

//    /**
//     * 此构造方法主要是用 本地路径原始图片生成缩略图 以此记住simple size 为以后运算方便
//     */
//    public DrawableSticker(String originalUrl, String originalAddressPath) {
////        1 设置基本信息
//        this.originalUrl = originalUrl;
////        2 创建压缩后大小的合适大小的bitmap 并保存simpleSize
////        3 生成sticker
//    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String url) {
        this.originalUrl = url;
    }

    public boolean isAlph() {
        return isAlph;
    }

    public void setAlph(boolean alph) {
        isAlph = alph;
    }

    public String getShowPicPath() {
        return showPicPath;
    }

    public void setShowPicPath(String showPicPath) {
        this.showPicPath = showPicPath;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public String getOriginalAddressPath() {
        return originalAddressPath;
    }

    public void setOriginalAddressPath(String originalAddressPath) {
        this.originalAddressPath = originalAddressPath;
    }

    @NonNull
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public DrawableSticker setDrawable(@NonNull Drawable drawable) {
        if (this.drawable != null && this.drawable instanceof BitmapDrawable) {
            BitmapUtils.recycleBitmap(((BitmapDrawable) this.drawable).getBitmap());
        }
        this.drawable = drawable;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.concat(getMatrix());
        drawable.setBounds(realBounds);
        drawable.setColorFilter(new ColorMatrixColorFilter(ColorMatrixUtils.handleImageEffect(colorData.getContrast(), colorData.getSaturation(), colorData.getBrightness())));
        drawable.draw(canvas);
        canvas.restore();
    }

    //画高清大图
    public void drawBig(@NonNull Canvas canvas) {
        canvas.save();
        canvas.concat(getBigMatrix());
        drawable.setBounds(realBounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    @NonNull
    @Override
    public DrawableSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        drawable.setAlpha(alpha);
        return this;
    }

    @Override
    public int getWidth() {
        return drawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    @Override
    public void release() {
        super.release();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapUtils.recycleBitmap(((BitmapDrawable) drawable).getBitmap());
            drawable = null;
        }
    }

    @Override
    public StickerDataBean getStickerData() {
        DrawableStickerDataBean drawableStickerDataBean = new DrawableStickerDataBean();
        setStickerData(drawableStickerDataBean);
        drawableStickerDataBean.colorData = this.colorData;
        drawableStickerDataBean.compressPath = this.compressPath;
        drawableStickerDataBean.originalAddressPath = this.getOriginalAddressPath();
        drawableStickerDataBean.showPicPath = this.showPicPath;
        drawableStickerDataBean.originalUrl = this.originalUrl;
        return drawableStickerDataBean;
    }

    public DrawableSticker initDrawableSticker(Context context, DrawableStickerDataBean stickerDataBean) {
        Matrix matrix = new Matrix();
        matrix.setValues(stickerDataBean.matrixData);
        setMatrix(matrix);
        originalAddressPath = stickerDataBean.originalAddressPath;
        originalUrl = stickerDataBean.originalUrl;
        originalUrl = stickerDataBean.originalUrl;
        compressPath = stickerDataBean.compressPath;
        Bitmap showBiamp = BitmapFactory.decodeFile(stickerDataBean.showPicPath);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), showBiamp);
        setDrawable(bitmapDrawable);
        return this;
    }
}
