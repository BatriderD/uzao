package com.xiaopo.flying.sticker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 元素的抽象类  修改了一些参数和方法
 * @author wupanjie
 */
public abstract class Sticker {

    private final float[] matrixValues = new float[9];
    private final float[] unrotatedWrapperCorner = new float[8];
    private final float[] unrotatedPoint = new float[2];
    private final float[] boundPoints = new float[8];
    private final float[] mappedBounds = new float[8];
    private final RectF trappedRect = new RectF();
    //矩阵
    private final Matrix matrix = new Matrix();
    //增强渲染参数
    public ColorData colorData =new ColorData();
    boolean lockLayerOrder = false;
    boolean showIconAndBoard = true;
    int mapLayer = 3;
//    0 贴纸 1 底图 2 形状,模板
    public int type = 0;
    public boolean isshowIconSpecial = false;
    private boolean isFlippedHorizontally;
    private boolean isFlippedVertically;

    //是否锁定图层
    private String locked;
    //是否显示元素
    private boolean visable =true;

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public boolean isFlippedHorizontally() {
        return isFlippedHorizontally;
    }

    /**
     * 水平翻转
     * @param flippedHorizontally 是否反转  true 翻转  false 不翻转
     * @return this
     */
    @NonNull
    public Sticker setFlippedHorizontally(boolean flippedHorizontally) {
        isFlippedHorizontally = flippedHorizontally;
        return this;
    }
    /**
     * 是否垂直翻转了
     * @return true 反转 false 未翻转
     */
    public boolean isFlippedVertically() {
        return isFlippedVertically;
    }

    @NonNull
    public Sticker setFlippedVertically(boolean flippedVertically) {
        isFlippedVertically = flippedVertically;
        return this;
    }

    @NonNull
    public Matrix getMatrix() {
        return matrix;
    }

    public Sticker setMatrix(@Nullable Matrix matrix) {
        this.matrix.set(matrix);
        return this;
    }

    public abstract void draw(@NonNull Canvas canvas);


    public abstract int getWidth();

    public abstract int getHeight();

    @NonNull
    public abstract Drawable getDrawable();

    public abstract Sticker setDrawable(@NonNull Drawable drawable);

    @NonNull
    public abstract Sticker setAlpha(@IntRange(from = 0, to = 255) int alpha);

    /**
     * 获得四个角的位置
     * @return 四个角的位置坐标  x1,y1 ……x4,y4
     */
    public float[] getBoundPoints() {
        float[] points = new float[8];
        getBoundPoints(points);
        return points;
    }

    /**
     * 获得坐标数据
     * @param points 元素的坐标数组
     */
    public void getBoundPoints(@NonNull float[] points) {
        if (!isFlippedHorizontally) {
            if (!isFlippedVertically) {
                points[0] = 0f;
                points[1] = 0f;
                points[2] = getWidth();
                points[3] = 0f;
                points[4] = 0f;
                points[5] = getHeight();
                points[6] = getWidth();
                points[7] = getHeight();
            } else {
                points[0] = 0f;
                points[1] = getHeight();
                points[2] = getWidth();
                points[3] = getHeight();
                points[4] = 0f;
                points[5] = 0f;
                points[6] = getWidth();
                points[7] = 0f;
            }
        } else {
            if (!isFlippedVertically) {
                points[0] = getWidth();
                points[1] = 0f;
                points[2] = 0f;
                points[3] = 0f;
                points[4] = getWidth();
                points[5] = getHeight();
                points[6] = 0f;
                points[7] = getHeight();
            } else {
                points[0] = getWidth();
                points[1] = getHeight();
                points[2] = 0f;
                points[3] = getHeight();
                points[4] = getWidth();
                points[5] = 0f;
                points[6] = 0f;
                points[7] = 0f;
            }
        }
    }


    @NonNull
    public float[] getMappedBoundPoints() {
        float[] dst = new float[8];
        getMappedPoints(dst, getBoundPoints());
        return dst;
    }

    @NonNull
    public float[] getMappedPoints(@NonNull float[] src) {
        float[] dst = new float[src.length];
        matrix.mapPoints(dst, src);
        return dst;
    }

    public void getMappedPoints(@NonNull float[] dst, @NonNull float[] src) {
        matrix.mapPoints(dst, src);
    }

    @NonNull
    public RectF getBound() {
        RectF bound = new RectF();
        getBound(bound);
        return bound;
    }

    public void getBound(@NonNull RectF dst) {
        dst.set(0, 0, getWidth(), getHeight());
    }

    @NonNull
    public RectF getMappedBound() {
        RectF dst = new RectF();
        getMappedBound(dst, getBound());
        return dst;
    }

    public void getMappedBound(@NonNull RectF dst, @NonNull RectF bound) {
        matrix.mapRect(dst, bound);
    }

    @NonNull
    public PointF getCenterPoint() {
        PointF center = new PointF();
        getCenterPoint(center);
        return center;
    }

    /**
     * 设置中心点
     * @param dst 中心点
     */
    public void getCenterPoint(@NonNull PointF dst) {
        dst.set(getWidth() * 1f / 2, getHeight() * 1f / 2);
    }

    /**
     * 获得中心点坐标
     * @return 中心点
     */
    @NonNull
    public PointF getMappedCenterPoint() {
        PointF pointF = getCenterPoint();
        getMappedCenterPoint(pointF, new float[2], new float[2]);
        return pointF;
    }

    public void getMappedCenterPoint(@NonNull PointF dst, @NonNull float[] mappedPoints,
                                     @NonNull float[] src) {
        getCenterPoint(dst);
        src[0] = dst.x;
        src[1] = dst.y;
        getMappedPoints(mappedPoints, src);
        dst.set(mappedPoints[0], mappedPoints[1]);
    }

    /**
     * 获得当前缩放系数
     * @return 缩放系数
     */
    public float getCurrentScale() {
        return getMatrixScale(matrix);
    }

    /**
     * 获得当前高
     * @return 元素高度
     */
    public float getCurrentHeight() {
        return getMatrixScale(matrix) * getHeight();
    }

    /**
     * 获得当前宽度
     * @return 元素宽度
     */
    public float getCurrentWidth() {
        return getMatrixScale(matrix) * getWidth();
    }

    /**
     * 获得矩阵的缩放系数
     * @return 矩阵缩放系数
     */
    public float getMatrixScale(@NonNull Matrix matrix) {
        return (float) Math.sqrt(Math.pow(getMatrixValue(matrix, Matrix.MSCALE_X), 2) + Math.pow(
                getMatrixValue(matrix, Matrix.MSKEW_Y), 2));
    }

    /**
     * 获得矩阵旋转角度
     * @return - current image rotation angle.
     */
    public float getCurrentAngle() {
        return getMatrixAngle(matrix);
    }

    /**
     * This method calculates rotation angle for given Matrix object.
     */
    public float getMatrixAngle(@NonNull Matrix matrix) {
        return (float) Math.toDegrees(-(Math.atan2(getMatrixValue(matrix, Matrix.MSKEW_X),
                getMatrixValue(matrix, Matrix.MSCALE_X))));
    }

    /**
     * 获取矩阵的制定位置的参数  2 和5 分别是x y的数字
     * @param matrix 矩阵
     * @param valueIndex 目标数字的下标
     * @return 矩阵对应位置的数字
     */
    public float getMatrixValue(@NonNull Matrix matrix, @IntRange(from = 0, to = 9) int valueIndex) {
        matrix.getValues(matrixValues);
        return matrixValues[valueIndex];
    }

    public boolean contains(float x, float y) {
        return contains(new float[]{x, y});
    }

    /**
     * 控件是否包含给定坐标
     * @param point 给定的坐标点
     * @return 给定
     */
    public boolean contains(@NonNull float[] point) {
        Matrix tempMatrix = new Matrix();
        tempMatrix.setRotate(-getCurrentAngle());
        getBoundPoints(boundPoints);
        getMappedPoints(mappedBounds, boundPoints);
        tempMatrix.mapPoints(unrotatedWrapperCorner, mappedBounds);
        tempMatrix.mapPoints(unrotatedPoint, point);
        StickerUtils.trapToRect(trappedRect, unrotatedWrapperCorner);
        return trappedRect.contains(unrotatedPoint[0], unrotatedPoint[1]);
    }

    /**
     * 释放
     */
    public void release() {
    }


    public Matrix getBigMatrix() {
//        1 获取平移量  2 获取旋转角度  3 获取缩放系数
        Matrix matrix = getMatrix();
        float transX = getMatrixValue(matrix, 2);
        float transY = getMatrixValue(matrix, 5);

        float currentAngle = getCurrentAngle();

        float currentScale = getCurrentScale();
//        扩大两偏移量   2按中心进行旋转   3 设置缩放系数


//        获取偏移量
        Matrix matrixBig = new Matrix();
        matrixBig.postRotate(currentAngle);
        matrixBig.postScale(currentScale * 2, currentScale*2);
        matrixBig.postTranslate(transX * 2, transY * 2);
        System.out.println("lalal " + matrixBig.toString());
        return matrixBig;
    }

    /**
     * 获得平移x
     *
     * @return x 平移量
     */
    public float getTransX() {
        return getMatrixValue(matrix, 2);
    }

    /**
     * 获得平移Y
     *
     * @return y平移量
     */
    public float getTransY() {
        return getMatrixValue(matrix, 5);
    }

    public StickerDataBean getStickerData() {
        return null;
    }

    public void setStickerData(StickerDataBean stickerDataBean) {
        float[] src = new float[9];
        matrix.getValues(src);
        stickerDataBean.matrixData = src;
        stickerDataBean.scale = this.getCurrentScale();
        stickerDataBean.rotation = this.getCurrentAngle();
        stickerDataBean.left = getMatrixValue(matrix, 2);
        stickerDataBean.top = getMatrixValue(matrix, 5);
    }

    @Override
    public String toString() {
        return "基础信息=" + getMappedBoundPoints();
    }

    public void setVisable(boolean visable) {
        this.visable = visable;
    }

    public boolean isVisable() {
        return visable;
    }

    @IntDef(flag = true, value = {
            Position.CENTER, Position.TOP, Position.BOTTOM, Position.LEFT, Position.RIGHT, Position.Full
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Position {
        int CENTER = 1;
        int TOP = 1 << 1;
        int LEFT = 1 << 2;
        int RIGHT = 1 << 3;
        int BOTTOM = 1 << 4;
        int Full = 1 << 5;
    }

    public Bitmap getBitmap() throws Exception {
        return null;
    }
}
