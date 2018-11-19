package com.xiaopo.flying.sticker;

import android.graphics.Bitmap;
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
public class MyDrawableSticker extends Sticker {


    public Drawable drawable;
    private Rect realBounds;


    //信息存储
    private StickerDataInfo info = new StickerDataInfo();

    public StickerDataInfo getInfo() {
        return info;
    }

    public void setInfo(StickerDataInfo info) {
        this.info = info;
    }


    /**
     * 1 原始图片地址，2显示的drawable
     */
    public MyDrawableSticker(String url, Drawable drawable) {
        info.url = url;
        this.drawable = drawable;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
    }



    @NonNull
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public MyDrawableSticker setDrawable(@NonNull Drawable drawable) {
        if (this.drawable != null && this.drawable instanceof BitmapDrawable) {
            BitmapUtils.recycleBitmap(((BitmapDrawable) this.drawable).getBitmap());
        }
        this.drawable = drawable;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
        return this;
    }

    /**
     * 绘制
     * @param canvas 画布
     */
    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.concat(getMatrix());
        drawable.setBounds(realBounds);
        drawable.setColorFilter(new ColorMatrixColorFilter(ColorMatrixUtils.handleImageEffect2(colorData.getContrast(), colorData.getSaturation(), colorData.getBrightness(), colorData.getHue())));
        drawable.draw(canvas);
        canvas.restore();
    }

    /**
     * 设置透明度
     * @param alpha 透明度 0 透明 -255 不透明
     * @return this
     */
    @NonNull
    @Override
    public MyDrawableSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        drawable.setAlpha(alpha);
        return this;
    }

    /**
     * 宽度
     * @return 宽度
     */
    @Override
    public int getWidth() {
        return drawable.getIntrinsicWidth();
    }

    /**
     * 高度
     * @return  高度
     */
    @Override
    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    /**
     * 处理缓存
     */
    @Override
    public void release() {
        super.release();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapUtils.recycleBitmap(((BitmapDrawable) drawable).getBitmap());
            drawable = null;
        }
    }


    /**
     * 控件数据化 数据
     * @return 数据
     */
    @Override
    public StickerDataBean getStickerData() {
        MyDrawableStickerDataBean drawableStickerDataBean = new MyDrawableStickerDataBean();
        setStickerData(drawableStickerDataBean);
        drawableStickerDataBean.isAlph = this.info.isAlph;
        if (info.clipBean != null) {
            drawableStickerDataBean.clipBean = this.info.clipBean.copy();
        }

        drawableStickerDataBean.originalUrl = this.info.url;
        drawableStickerDataBean.filterType = this.info.filterType;
        drawableStickerDataBean.filterName = this.info.filterName;
        drawableStickerDataBean.filterPic = this.info.filterPic;
        drawableStickerDataBean.isVector = this.info.isVector;
        drawableStickerDataBean.resizeScale = this.info.resizeScale;

        return drawableStickerDataBean;
    }

    /**
     * 数据化变成控件
     * @param stickerDataBean 图片数据
     * @return this
     */
    public MyDrawableSticker initDrawableSticker(MyDrawableStickerDataBean stickerDataBean) {
        Matrix matrix = new Matrix();
        matrix.setValues(stickerDataBean.matrixData);
        setMatrix(matrix);

        info.url = stickerDataBean.originalUrl;
        info.isAlph = stickerDataBean.isAlph;
        info.clipBean = stickerDataBean.clipBean;
        info.filterType = stickerDataBean.filterType;
        info.filterName = stickerDataBean.filterName;
        info.filterPic = stickerDataBean.filterPic;
        info.isVector = stickerDataBean.isVector;
        info.resizeScale = stickerDataBean.resizeScale;
        return this;
    }

    /**
     * 获得一个能代表本元素的截图 截图不需要很大
     * @return 图片
     * @throws Exception 捕获异常
     */
    @Override
    public Bitmap getBitmap() throws Exception {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bm = bitmapDrawable.getBitmap();
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth = bm.getWidth() / 4;
        int newHeight = bm.getHeight() / 4;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
    }

}


