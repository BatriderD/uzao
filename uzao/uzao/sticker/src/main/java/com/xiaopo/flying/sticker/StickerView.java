package com.xiaopo.flying.sticker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Sticker View
 *
 * @author wupanjie
 */
public class StickerView extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    public static final String TAG = "StickerView";
    public static final int DEFAULT_MIN_CLICK_DELAY_TIME = 200;
    public static final int FLIP_HORIZONTALLY = 1;
    public static final int FLIP_VERTICALLY = 1 << 1;
    public final boolean bringToFrontCurrentSticker;
    public final List<Sticker> stickers = new ArrayList<>();
    public final List<BitmapStickerIcon> icons = new ArrayList<>(4);
    public final Paint borderPaint = new Paint();
    public final RectF stickerRect = new RectF();
    public final Matrix sizeMatrix = new Matrix();
    public final Matrix downMatrix = new Matrix();
    public final Matrix moveMatrix = new Matrix();
    // region storing variables
    public final float[] bitmapPoints = new float[8];
    public final float[] bounds = new float[8];
    public final float[] point = new float[2];
    public final PointF currentCenterPoint = new PointF();
    public final float[] tmp = new float[2];
    // 触摸 和点击的位移
    private final int touchSlop;
    //绘制控件的主画笔
    public Paint mPaint;
    //
    public Paint mLinePaint;
    public BitmapShader templateShader;
    public PorterDuffXfermode xfermode;
    public Shader mixShader;
    public int shapeColor = 0;
    //标志是否显示显示Icon
    public boolean showIcons;
    //标识 是否显示线框
    public boolean showBorder;
    public PointF midPoint = new PointF();
    public BitmapStickerIcon currentIcon;
    //the first point down position
    public float downX;
    public float downY;
    public float oldDistance = 0f;
    public float oldRotation = 0f;
    @ActionMode
    public int currentMode = ActionMode.NONE;
    //当前选中的sticker
    public Sticker handlingSticker;
    //锁定  锁定就不可选中 不可缩放了
    public boolean locked;
    // 约束  控件不会被移出编辑区域  现在是用中心店控制的
    public boolean constrained = true;
    //操作的回调
    public OnStickerOperationListener onStickerOperationListener;
    //控件选中的回调
    public OnSelectorListener onSelectorListener;
    //上次点击的时间 用来判断是点击 还是移动
    public long lastClickTime = 0;
    //最小双击时间 小于这个时间就是双击
    public int minClickDelayTime = DEFAULT_MIN_CLICK_DELAY_TIME;
    //蒙版图片 bitmap 用来和别的图片区交集
    public Bitmap templateBitmap = null;
    //空白的背景图片
    private Bitmap whiteBgBitmap;
    //网格的密度
    private int lineSpacing = 75;
    //绘制图像的画笔过滤器 用来抗锯齿
    private PaintFlagsDrawFilter paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public boolean isShowLine() {
        return showLine;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }

    private boolean showLine = false;

    public StickerView(Context context) {
        this(context, null);
    }

    public StickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = (ViewConfiguration.get(context).getScaledTouchSlop() + 1) / 10;
        // 初始化paint
        mPaint = new Paint();
        //关闭硬件加速 不然蒙版报错
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        //初始化划线的画笔 抗锯齿等功能
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStyle(Paint.Style.STROKE);
        //        设置画笔宽度
        mLinePaint.setStrokeWidth(4);
        //模板的重要步骤  图片相交
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.StickerView);
            showIcons = a.getBoolean(R.styleable.StickerView_showIcons, false);
            showBorder = a.getBoolean(R.styleable.StickerView_showBorder, false);
            bringToFrontCurrentSticker =
                    a.getBoolean(R.styleable.StickerView_bringToFrontCurrentSticker, false);

            borderPaint.setAntiAlias(true);
            borderPaint.setStrokeWidth(4);
            borderPaint.setColor(a.getColor(R.styleable.StickerView_borderColor, Color.BLACK));
            borderPaint.setAlpha(a.getInteger(R.styleable.StickerView_borderAlpha, 128));

            // 设置默认四周图标
            configDefaultIcons();
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    /**
     * 获得屏幕宽度
     *
     * @param context context
     * @return  屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * dp转 px.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static float dp2px(float value, Context context) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return value * scale / 160 + 0.5f;
    }

    /**
     * 是否显示图标
     *
     * @return true 显示 false不显示
     */
    public boolean isShowIcons() {
        return showIcons;
    }

    /**
     * 设置是否显示 控件四周的图标
     *
     * @param showIcons 不显示
     */
    public void setShowIcons(boolean showIcons) {
        this.showIcons = showIcons;
    }


    /**
     * 是否显示控件的边缘 线框
     * @return true 显示  false 不显示
     */
    public boolean isShowBorder() {
        return showBorder;
    }

    /**
     * 设置显示控件边缘
     * @param showBorder 是否现实边缘
     */
    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }


    /**
     * 设置蒙版的图片  这里需要注意蒙版需要和本控件大小一样
     * @param templateBitmap 蒙版图片
     */
    public void setTemplateBitmap(Bitmap templateBitmap) {
        this.templateBitmap = templateBitmap;
    }

    /**
     * 设置默认的图标
     */
    public void configDefaultIcons() {
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(
                ContextCompat.getDrawable(getContext(), R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(
                ContextCompat.getDrawable(getContext(), R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon flipIcon = new BitmapStickerIcon(
                ContextCompat.getDrawable(getContext(), R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());

        icons.clear();
        icons.add(deleteIcon);
        icons.add(zoomIcon);
        icons.add(flipIcon);
    }

    /**
     * 更换指定两位置的图层
     * @param oldPos 旧位置
     * @param newPos 新位置
     */
    public void swapLayers(int oldPos, int newPos) {
        if (stickers.size() >= oldPos && stickers.size() >= newPos) {
            Collections.swap(stickers, oldPos, newPos);
            invalidate();
        }
    }

    /**
     * Sends sticker from layer [[oldPos]] to layer [[newPos]].
     * Does nothing if either of the specified layers doesn't exist.
     */
    public void sendToLayer(int oldPos, int newPos) {
        if (stickers.size() >= oldPos && stickers.size() >= newPos) {
            Sticker s = stickers.get(oldPos);
            stickers.remove(oldPos);
            stickers.add(newPos, s);
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            stickerRect.left = left;
            stickerRect.top = top;
            stickerRect.right = right;
            stickerRect.bottom = bottom;
        }
    }


    /**
     * viewGroup的绘制回调
     * @param canvas canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {

        super.dispatchDraw(canvas);
        //设置抗锯齿
        canvas.setDrawFilter(paintFlagsDrawFilter);
        //根据有没有蒙版图片来处理
        if (templateBitmap == null) {
            //画网格
            //直接按顺序根据图层绘制需要的图层
            drawStickers(canvas);
            //根据是否显示网格绘制网格
            if (showLine) {
                //绘制网格
                drawLine(canvas);
            }
            //绘制选中图标
            drawBoard(canvas);
        } else {
            /**
             * 有蒙版的情况较为复杂
             * 给画笔设置混合器
             * 来处理图片取交集的处理
             */
            setPaint();
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
            drawBoard(canvas);
            mPaint.setShader(null);
            mixShader = null;
        }
    }

    /**
     * 绘制选中元素的边框 和角的图片
     *
     * @param canvas 画布
     */
    private void drawBoard(Canvas canvas) {

        if (handlingSticker != null && !locked && (showBorder || showIcons) && handlingSticker.isVisable()) {

            getStickerPoints(handlingSticker, bitmapPoints);

            float x1 = bitmapPoints[0];
            float y1 = bitmapPoints[1];
            float x2 = bitmapPoints[2];
            float y2 = bitmapPoints[3];
            float x3 = bitmapPoints[4];
            float y3 = bitmapPoints[5];
            float x4 = bitmapPoints[6];
            float y4 = bitmapPoints[7];
            //绘制四边的线
            if (showBorder && handlingSticker.showIconAndBoard) {
                canvas.drawLine(x1, y1, x2, y2, borderPaint);
                canvas.drawLine(x1, y1, x3, y3, borderPaint);
                canvas.drawLine(x2, y2, x4, y4, borderPaint);
                canvas.drawLine(x4, y4, x3, y3, borderPaint);
            }

            //draw 四周的图标
            if (showIcons && handlingSticker.showIconAndBoard) {
                float rotation = calculateRotation(x4, y4, x3, y3);
                for (int i = 0; i < icons.size(); i++) {
                    BitmapStickerIcon icon = icons.get(i);
                    switch (icon.getPosition()) {
                        case BitmapStickerIcon.LEFT_TOP:
                            configIconMatrix(icon, x1, y1, rotation);
                            break;

                        case BitmapStickerIcon.RIGHT_TOP:
                            if (handlingSticker instanceof MyTextSticker) {
                                //只有文字设置右上角多图片
                                configIconMatrix(icon, x2, y2, rotation);
                            } else {
                                continue;
                            }

                            break;

                        case BitmapStickerIcon.LEFT_BOTTOM:
                            configIconMatrix(icon, x3, y3, rotation);
                            break;

                        case BitmapStickerIcon.RIGHT_BOTOM:
                            configIconMatrix(icon, x4, y4, rotation);
                            break;
                    }
                    icon.draw(canvas, borderPaint);
                }
            }
        }
    }


    /**
     * 获得着色器
     * @return 获得混合器
     */
    public Shader getTemplateShader() {
        if (templateBitmap != null) {
            if (templateShader == null) {
                templateShader = new BitmapShader(templateBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            }
        }
        return templateShader;
    }


    /**
     * 设置混合器的画笔
     */
    public void setPaint() {
        Bitmap bitmap = drawStickersOnBitmap();
        //此混合器的元素为上面绘制有元素的空白图片
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        getTemplateShader();
        if (templateShader != null) {
            //在蒙版不为空的情况下和图片混合
            mixShader = new ComposeShader(bitmapShader, templateShader, xfermode);
        } else {
            mixShader = bitmapShader;
        }
        //将混合器给画笔
        mPaint.setShader(mixShader);
    }

    /**
     * 绘制元素到图片上
     * @return 带有元素的 和本空间等大的一张图片
     */
    protected Bitmap drawStickersOnBitmap() {

        if (whiteBgBitmap == null) {
            whiteBgBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                    Bitmap.Config.ARGB_8888);
        }
        //设置背景  如果如果没有背景色 就是透明，背景色
        if (shapeColor != 0) {
            whiteBgBitmap.eraseColor(shapeColor);
        } else {
            whiteBgBitmap.eraseColor(0);
        }
        Canvas canvas = new Canvas(whiteBgBitmap);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        //画贴纸
        drawStickers(canvas);
        //画网格
        if (showLine) {
            drawLine(canvas);
        }
        return whiteBgBitmap;

    }

    /**
     * 画网格
     *
     * @param canvas 画布
     */
    private void drawLine(Canvas canvas) {

        int measuredWidth = getWidth();
        int measuredHeight = getHeight();


        // 创建矩形框
        Rect mRect = new Rect(0, 0, measuredWidth, measuredHeight);
        // 绘制边框
        canvas.drawRect(mRect, mLinePaint);

//        画线
//        设置画笔
        mLinePaint.setStrokeWidth(3);
        //线的显示效果：破折号格式
        PathEffect effects = new DashPathEffect(new float[]{5, 5}, 1);
        mLinePaint.setPathEffect(effects);
        //画竖线 根据线之间的宽度
        int widthLines = (int) Math.ceil(measuredWidth / lineSpacing);
        for (int i = 1; i <= widthLines; i++) {
            int drawWidth = lineSpacing * i;
            //画竖线
            canvas.drawLine(drawWidth, 0, drawWidth, measuredHeight, mLinePaint);
        }
        //画横线
        int heightLines = (int) Math.ceil(measuredHeight / lineSpacing);
        for (int i = 1; i <= heightLines; i++) {
            int drawHeight = lineSpacing * i;
            //画横线
            canvas.drawLine(0, drawHeight, measuredWidth, drawHeight, mLinePaint);
        }
    }

    /**
     * 绘制控件
     * @param canvas 画布
     */
    protected void drawStickers(Canvas canvas) {
        for (int i = 0; i < stickers.size(); i++) {
            Sticker sticker = stickers.get(i);
            if (sticker != null && sticker.isVisable()) {
                sticker.draw(canvas);
            }
        }

    }


    /**
     * 设置icon的位置和大小
     * @param icon      图片
     * @param x         位置x
     * @param y         位置y
     * @param rotation  旋转角度
     */
    protected void configIconMatrix(@NonNull BitmapStickerIcon icon, float x, float y,
                                    float rotation) {
        icon.setX(x);
        icon.setY(y);
        icon.getMatrix().reset();

        icon.getMatrix().postRotate(rotation, icon.getWidth() / 2, icon.getHeight() / 2);
        icon.getMatrix().postTranslate(x - icon.getWidth() / 2, y - icon.getHeight() / 2);
    }

    /**
     * 点击事件的拦截
     * @param ev 鼠标事件
     * @return 是否拦击
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (locked) return super.onInterceptTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();

                if (onSelectorListener != null) {
                    onSelectorListener.onSelected();
                }
                return findCurrentIconTouched() != null || findHandlingSticker() != null;
        }
        return super.onInterceptTouchEvent(ev);
    }


    /**
     * 点击事件的拦截
     * @param event 鼠标事件
     * @return 自己处理拦击
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (locked) {
            return super.onTouchEvent(event);
        }
        //获取手势
        int action = MotionEventCompat.getActionMasked(event);
        //根据类型处理
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!onTouchDown(event)) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDistance = calculateDistance(event);
                oldRotation = calculateRotation(event);

                midPoint = calculateMidPoint(event);

                if (handlingSticker != null && isInStickerArea(handlingSticker, event.getX(1),
                        event.getY(1)) && findCurrentIconTouched() == null) {
                    currentMode = ActionMode.ZOOM_WITH_TWO_FINGER;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                handleCurrentMode(event);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                onTouchUp(event);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (currentMode == ActionMode.ZOOM_WITH_TWO_FINGER && handlingSticker != null) {
                    if (onStickerOperationListener != null) {
                        onStickerOperationListener.onStickerZoomFinished(handlingSticker);
                    }
                }
                currentMode = ActionMode.NONE;
                break;
        }

        return true;
    }

    /**
     * @param event MotionEvent received from {@link #onTouchEvent)
     * @return true if has touch something
     */
    protected boolean onTouchDown(@NonNull MotionEvent event) {
        currentMode = ActionMode.DRAG;

        downX = event.getX();
        downY = event.getY();

        midPoint = calculateMidPoint();
        oldDistance = calculateDistance(midPoint.x, midPoint.y, downX, downY);
        oldRotation = calculateRotation(midPoint.x, midPoint.y, downX, downY);

        currentIcon = findCurrentIconTouched();
        if (currentIcon != null) {
            currentMode = ActionMode.ICON;
            currentIcon.onActionDown(this, event);
        } else {
            handlingSticker = findHandlingSticker();
        }

        if (handlingSticker != null) {
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerTouchedDown(handlingSticker);
            }
            downMatrix.set(handlingSticker.getMatrix());
            if (bringToFrontCurrentSticker) {
                stickers.remove(handlingSticker);
                stickers.add(handlingSticker);
            }
        }

        if (currentIcon == null && handlingSticker == null) {
            return false;
        }
        invalidate();
        return true;
    }

    protected void onTouchUp(@NonNull MotionEvent event) {
        long currentTime = SystemClock.uptimeMillis();

        if (currentMode == ActionMode.ICON && currentIcon != null && handlingSticker != null) {
            currentIcon.onActionUp(this, event);
        }

        if (currentMode == ActionMode.DRAG
                && Math.abs(event.getX() - downX) < touchSlop
                && Math.abs(event.getY() - downY) < touchSlop
                && handlingSticker != null) {
            currentMode = ActionMode.CLICK;
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerClicked(handlingSticker);
            }
            if (currentTime - lastClickTime < minClickDelayTime) {
                if (onStickerOperationListener != null) {
                    onStickerOperationListener.onStickerDoubleTapped(handlingSticker);
                }
            }
        }

        if (currentMode == ActionMode.DRAG && handlingSticker != null) {
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerDragFinished(handlingSticker);
            }
        }

        currentMode = ActionMode.NONE;
        lastClickTime = currentTime;
    }

    protected void handleCurrentMode(@NonNull MotionEvent event) {
        switch (currentMode) {
            case ActionMode.NONE:
                break;
            case ActionMode.CLICK:

                break;
            case ActionMode.DRAG:
                if (handlingSticker != null) {
                    moveMatrix.set(downMatrix);
                    moveMatrix.postTranslate(event.getX() - downX, event.getY() - downY);
                    handlingSticker.setMatrix(moveMatrix);
                    if (constrained) {
                        constrainSticker(handlingSticker);
                    }
                }
                break;
            case ActionMode.ZOOM_WITH_TWO_FINGER:
                if (handlingSticker != null) {
                    float newDistance = calculateDistance(event);
                    float newRotation = calculateRotation(event);

                    moveMatrix.set(downMatrix);
                    moveMatrix.postScale(newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                            midPoint.y);
                    moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y);
                    handlingSticker.setMatrix(moveMatrix);
                    if (onStickerOperationListener != null) {
                        onStickerOperationListener.onStickerZoom(handlingSticker);
                    }
                }

                break;

            case ActionMode.ICON:
                if (handlingSticker != null && currentIcon != null) {
                    currentIcon.onActionMove(this, event);
                }
                break;
        }
    }

    public void zoomAndRotateCurrentSticker(@NonNull MotionEvent event) {
        zoomAndRotateSticker(handlingSticker, event);
    }

    public void zoomAndRotateSticker(@Nullable Sticker sticker, @NonNull MotionEvent event) {
        if (sticker != null) {
            float newDistance = calculateDistance(midPoint.x, midPoint.y, event.getX(), event.getY());
            float newRotation = calculateRotation(midPoint.x, midPoint.y, event.getX(), event.getY());

            moveMatrix.set(downMatrix);
            moveMatrix.postScale(newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                    midPoint.y);
            moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y);
            handlingSticker.setMatrix(moveMatrix);
        }
    }

    protected void constrainSticker(@NonNull Sticker sticker) {
        float moveX = 0;
        float moveY = 0;
        int width = getWidth();
        int height = getHeight();
        sticker.getMappedCenterPoint(currentCenterPoint, point, tmp);
        if (currentCenterPoint.x < 0) {
            moveX = -currentCenterPoint.x;
        }

        if (currentCenterPoint.x > width) {
            moveX = width - currentCenterPoint.x;
        }

        if (currentCenterPoint.y < 0) {
            moveY = -currentCenterPoint.y;
        }

        if (currentCenterPoint.y > height) {
            moveY = height - currentCenterPoint.y;
        }
        sticker.getMatrix().postTranslate(moveX, moveY);
    }

    @Nullable
    protected BitmapStickerIcon findCurrentIconTouched() {
        for (BitmapStickerIcon icon : icons) {
            float x = icon.getX() - downX;
            float y = icon.getY() - downY;
            float distance_pow_2 = x * x + y * y;
            if (distance_pow_2 <= Math.pow(icon.getIconRadius() + icon.getIconRadius(), 2)) {
                return icon;
            }
        }

        return null;
    }

    /**
     * find the touched Sticker  ooo
     **/
    @Nullable
    protected Sticker findHandlingSticker() {
        if (handlingSticker != null) {
            handlingSticker.showIconAndBoard = false;
        }
        for (int i = stickers.size() - 1; i >= 0; i--) {
            Sticker sticker = stickers.get(i);
            if (isInStickerArea(sticker, downX, downY)) {
                if ("Y".equals(sticker.getLocked())) {
                    Toast.makeText(getContext(), "此元素不可拖动", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "findHandlingSticker: 你拖动的控件锁住了");
                    continue;
                } else if (!sticker.isVisable()) {
                    Log.d(TAG, "findHandlingSticker: 你点击的控件不可见");
                    continue;
                }
                sticker.showIconAndBoard = true;

                invalidate();
                if (onStickerOperationListener != null) {
                    onStickerOperationListener.onStickerSelected(sticker);
                }
                return sticker;
            }
        }
        invalidate();
        if (onStickerOperationListener != null) {
            onStickerOperationListener.onStickerSelected(null);
        }
        return null;
    }

    /**
     * 此点击区域是不是在sticker上
     *
     * @param sticker 检测sticker
     * @param downX   检测点x
     * @param downY   检测点y
     * @return
     */
    protected boolean isInStickerArea(@NonNull Sticker sticker, float downX, float downY) {
        tmp[0] = downX;
        tmp[1] = downY;
        return sticker.contains(tmp);
    }

    @NonNull
    protected PointF calculateMidPoint(@Nullable MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) {
            midPoint.set(0, 0);
            return midPoint;
        }
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        midPoint.set(x, y);
        return midPoint;
    }

    /**
     * 计算当前选中的图片的handingSticker 的点
     * @return 中心点
     */
    @NonNull
    public PointF calculateMidPoint() {
        if (handlingSticker == null) {
            midPoint.set(0, 0);
            return midPoint;
        }
        //选中的图标中心点
        handlingSticker.getMappedCenterPoint(midPoint, point, tmp);
        return midPoint;
    }

    /**
     * 计算手势旋转角度
     **/
    protected float calculateRotation(@Nullable MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) {
            return 0f;
        }
        return calculateRotation(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
    }

    /**
     * 根据两点计算计算旋转角度
     * @param x1 点1的x
     * @param y1 点1的y
     * @param x2 点2的x
     * @param y2 点2的y
     * @return 旋转角度
     */
    protected float calculateRotation(float x1, float y1, float x2, float y2) {
        double x = x1 - x2;
        double y = y1 - y2;
        double radians = Math.atan2(y, x);
        return (float) Math.toDegrees(radians);
    }

    /**
     * 计算双手手指之间的距离
     **/
    protected float calculateDistance(@Nullable MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) {
            return 0f;
        }
        return calculateDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
    }

    /**
     * 计算亮点之间距离
     *
     * @param x1 点1的x
     * @param y1 点1的y
     * @param x2 点2的x
     * @param y2 点2的y
     * @return
     */
    protected float calculateDistance(float x1, float y1, float x2, float y2) {
        double x = x1 - x2;
        double y = y1 - y2;

        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        for (int i = 0; i < stickers.size(); i++) {
            Sticker sticker = stickers.get(i);
            if (sticker != null) {
                transformSticker(sticker);
            }
        }
    }

    /**
     * Sticker's drawable will be too bigger or smaller
     * This method is to transform it to fit
     * step 1：let the center of the sticker image is coincident with the center of the View.
     * step 2：Calculate the zoom and zoom
     **/
    protected void transformSticker(@Nullable Sticker sticker) {
        if (sticker == null) {
            Log.e(TAG, "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null");
            return;
        }

        sizeMatrix.reset();

        float width = getWidth();
        float height = getHeight();
        float stickerWidth = sticker.getWidth();
        float stickerHeight = sticker.getHeight();
        //step 1
        float offsetX = (width - stickerWidth) / 2;
        float offsetY = (height - stickerHeight) / 2;

        sizeMatrix.postTranslate(offsetX, offsetY);

        //step 2
        float scaleFactor;
        if (width < height) {
            scaleFactor = width / stickerWidth;
        } else {
            scaleFactor = height / stickerHeight;
        }

        sizeMatrix.postScale(scaleFactor / 2f, scaleFactor / 2f, width / 2f, height / 2f);

        sticker.getMatrix().reset();
        sticker.setMatrix(sizeMatrix);

        invalidate();
    }

    /**
     * 翻转当前sticker
     *
     * @param direction 翻转方向
     */
    public void flipCurrentSticker(int direction) {
        flip(handlingSticker, direction);
    }

    /**
     * 翻转当前sticker
     *
     * @param sticker   当前sticker
     * @param direction 方向
     */
    public void flip(@Nullable Sticker sticker, @Flip int direction) {
        if (sticker != null) {
            sticker.getCenterPoint(midPoint);
            if ((direction & FLIP_HORIZONTALLY) > 0) {
                sticker.getMatrix().preScale(-1, 1, midPoint.x, midPoint.y);
                sticker.setFlippedHorizontally(!sticker.isFlippedHorizontally());
            }
            if ((direction & FLIP_VERTICALLY) > 0) {
                sticker.getMatrix().preScale(1, -1, midPoint.x, midPoint.y);
                sticker.setFlippedVertically(!sticker.isFlippedVertically());
            }

            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerFlipped(sticker);
            }

            invalidate();
        }
    }


    /**
     * 替换当前sticker
     *
     * @param sticker 替换的sticker
     * @return 是否替换成功
     */
    public boolean replace(@Nullable Sticker sticker) {
        return replace(sticker, true);
    }

    /**
     * 是否带状态替换
     *
     * @param sticker 替换sticker
     * @param needStayState 是否保留原始状态
     * @return 是否替换成功
     */
    public boolean replace(@Nullable Sticker sticker, boolean needStayState) {
        if (handlingSticker != null && sticker != null) {
            float width = getWidth();
            float height = getHeight();
            if (needStayState) {
                sticker.setMatrix(handlingSticker.getMatrix());
                sticker.setFlippedVertically(handlingSticker.isFlippedVertically());
                sticker.setFlippedHorizontally(handlingSticker.isFlippedHorizontally());
            } else {
                handlingSticker.getMatrix().reset();
                // reset scale, angle, and put it in center
                float offsetX = (width - handlingSticker.getWidth()) / 2f;
                float offsetY = (height - handlingSticker.getHeight()) / 2f;
                sticker.getMatrix().postTranslate(offsetX, offsetY);

                float scaleFactor;
                if (width < height) {
                    scaleFactor = width / handlingSticker.getWidth();
                } else {
                    scaleFactor = height / handlingSticker.getHeight();
                }
                sticker.getMatrix().postScale(scaleFactor / 2f, scaleFactor / 2f, width / 2f, height / 2f);
            }
            int index = stickers.indexOf(handlingSticker);
            stickers.set(index, sticker);
            handlingSticker = sticker;

            invalidate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 移除指定sticker
     *
     * @param sticker sticker
     * @return 是否移成功
     */
    public boolean remove(@Nullable Sticker sticker) {
        if (stickers.contains(sticker)) {
            stickers.remove(sticker);
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerDeleted(sticker);
            }
            if (handlingSticker == sticker) {
                handlingSticker = null;
            }
            invalidate();

            return true;
        } else {
            Log.d(TAG, "remove: the sticker is not in this StickerView");

            return false;
        }
    }


    /**
     * 移除指定位置sticker
     */
    public boolean remove(int pos) {
        if (stickers.size() >= pos - 1) {
            Sticker sticker = stickers.get(pos);
            stickers.remove(sticker);
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerDeleted(sticker);
            }
            if (handlingSticker == sticker) {
                handlingSticker = null;
            }
            invalidate();

            return true;
        } else {
            return false;
        }

    }

    /**
     * 移除当前sticker
     *
     * @return 是否删除成功
     */
    public boolean removeCurrentSticker() {
        return remove(handlingSticker);
    }

    /**
     * 删除所有sticker
     */
    public void removeAllStickers() {
        if (handlingSticker != null) {
            handlingSticker = null;
        }
        for (int i = 0; i < stickers.size(); i++) {
            stickers.get(i).release();
        }
        stickers.clear();
        invalidate();
    }

    /**
     * 删除所有sticker
     */
    public void removeAllUnLockedStickers() {
        if (handlingSticker != null) {
            handlingSticker = null;
        }
        Iterator<Sticker> iterator = stickers.iterator();
        while (iterator.hasNext()) {
            Sticker bean = iterator.next();
            //名字是否合适
            if (!"Y".equals(bean.getLocked())) {
                iterator.remove();
                bean.release();
            }
        }
        invalidate();
    }

    /**
     * 新增一批sticker
     * @param newStickers 新的stickers
     */
    public void addNewStickers(List<Sticker> newStickers) {
        if (handlingSticker != null) {
            handlingSticker = null;
        }
        for (int i = 0; i < stickers.size(); i++) {
            stickers.get(i).release();
        }
        stickers.clear();
        stickers.addAll(newStickers);
        invalidate();
    }

    /**
     * 增加sticker
     *
     * @param sticker 新的sticker
     * @return stickerView
     */
    @NonNull
    public StickerView addSticker(@NonNull Sticker sticker) {
        return addSticker(sticker, Sticker.Position.CENTER);
    }

    /**
     * 增加sticker到指定位置
     *
     * @param sticker sticker
     * @param position 目标位置
     * @return stickerView
     */
    public StickerView addSticker(@NonNull final Sticker sticker,
                                  final @Sticker.Position int position) {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, position);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    addStickerImmediately(sticker, position);
                }
            });
        }
        return this;
    }

    /**
     * 立刻增加指定sticker
     *
     * @param sticker  sticker
     * @param position 位置
     */
    protected void addStickerImmediately(@NonNull Sticker sticker, @Sticker.Position int position) {
        setStickerPosition(sticker, position);


        float scaleFactor, widthScaleFactor, heightScaleFactor;

//        widthScaleFactor = (float) getWidth() / sticker.getDrawable().getIntrinsicWidth();
//        heightScaleFactor = (float) getHeight() / sticker.getDrawable().getIntrinsicHeight();
        widthScaleFactor = (float) getWidth() / sticker.getWidth();
        heightScaleFactor = (float) getHeight() / sticker.getHeight();
        scaleFactor = widthScaleFactor > heightScaleFactor ? heightScaleFactor : widthScaleFactor;

        sticker.getMatrix()
                .postScale(scaleFactor / 2, scaleFactor / 2, getWidth() / 2, getHeight() / 2);

        handlingSticker = sticker;
        stickers.add(sticker);
        if (onStickerOperationListener != null) {
            onStickerOperationListener.onStickerAdded(sticker);
        }
        invalidate();
    }

    /**
     *  根据携带的矩阵添加sticker
     * @param sticker 控件
     * @param matrix 矩阵
     */
    public void addStickerNochange(@NonNull final Sticker sticker, @NonNull final Matrix matrix) {

        if (ViewCompat.isLaidOut(this)) {
            addStickerNoChangeImmediately(sticker, matrix);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    addStickerNoChangeImmediately(sticker, matrix);
                }
            });
        }
    }

    /**
     * 增加原样的sticker
     *
     * @param sticker sticker
     * @param scale  缩放系数
     */
    public void addStickerCenterWithScale(@NonNull final Sticker sticker, @NonNull float scale) {

        setStickerPosition(sticker, Sticker.Position.CENTER);

        sticker.getMatrix()
                .postScale(scale, scale, getWidth() / 2, getHeight() / 2);

        handlingSticker = sticker;
        stickers.add(sticker);
        if (onStickerOperationListener != null) {
            onStickerOperationListener.onStickerAdded(sticker);
        }
        invalidate();
    }

    private void addStickerNoChangeImmediately(@NonNull Sticker sticker, @NonNull Matrix matrix) {
        sticker.setMatrix(matrix);
        stickers.add(sticker);
        handlingSticker = sticker;

        if (onStickerOperationListener != null) {
            onStickerOperationListener.onStickerAdded(sticker);
        }
        invalidate();
    }


    /**
     * 填充或者适配讲一个图片放指定大小
     * 填充指的是最小边充满屏幕 最大边居中
     * @param sticker sticker
     * @param type 0 填充 1 适配
     */
    public void changeFillStyleSticker2(@NonNull Sticker sticker, int type) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        int drawableWidth = sticker.getWidth();
        int drawableHeight = sticker.getHeight();

        float widthScale = viewWidth * 1f / drawableWidth;
        float heightScale = viewHeight * 1f / drawableHeight;
        Matrix matrix = new Matrix();
        if (widthScale > heightScale) {
            //高度 heightScale 适配  宽度 widthScale填充
            if (type == 0) {
                // 0填充
                matrix.postScale(widthScale, widthScale);
                matrix.postRotate(sticker.getCurrentAngle(), sticker.getWidth() * widthScale / 2, sticker.getHeight() * widthScale / 2);
                matrix.postTranslate(0, (viewHeight - (drawableHeight * widthScale)) / 2);
            } else {
                matrix.postScale(heightScale, heightScale);
                matrix.postRotate(sticker.getCurrentAngle(), sticker.getWidth() * heightScale / 2, sticker.getHeight() * heightScale / 2);
                matrix.postTranslate((viewWidth - (drawableWidth * heightScale)) / 2, 0);
            }


        } else {
            if (type == 0) {
                // 0填充
                matrix.postScale(heightScale, heightScale);
                matrix.postRotate(sticker.getCurrentAngle(), sticker.getWidth() * heightScale / 2, sticker.getHeight() * heightScale / 2);
                matrix.postTranslate((viewWidth - (drawableWidth * heightScale)) / 2, 0);
            } else {
                matrix.postScale(widthScale, widthScale);
                matrix.postRotate(sticker.getCurrentAngle(), sticker.getWidth() * widthScale / 2, sticker.getHeight() * widthScale / 2);
                matrix.postTranslate(0, (viewHeight - (drawableHeight * widthScale)) / 2);
            }

        }
        sticker.setMatrix(matrix);
        if (onStickerOperationListener != null) {
            //缩放结束的回调
            onStickerOperationListener.onStickerZoomFinished(sticker);
        }
        invalidate();
    }

    /**
     * 增加sticker带填充模式
     *
     * @param sticker sticker
     * @param type    0填充 1适配
     */
    public void addFillStyleSticker(@NonNull Sticker sticker, int type) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        int drawableWidth = sticker.getWidth();
        int drawableHeight = sticker.getHeight();

        float widthScale = (viewWidth - 100) * 1f / drawableWidth;
        float heightScale = (viewHeight - 100) * 1f / drawableHeight;
        Matrix matrix = new Matrix();
        if (widthScale > heightScale) {
            //高度 heightScale 适配  宽度 widthScale填充
            if (type == 0) {
                // 0填充
                matrix.postScale(widthScale, widthScale);
                matrix.postTranslate(0, (viewHeight - (drawableHeight * widthScale)) / 2);
            } else {
                matrix.postScale(heightScale, heightScale);
                matrix.postTranslate((viewWidth - (drawableWidth * heightScale)) / 2, (viewHeight - drawableHeight * heightScale) / 2);
            }


        } else {
            if (type == 0) {
                // 0填充
                matrix.postScale(heightScale, heightScale);
                matrix.postTranslate((viewWidth - (drawableWidth * heightScale)) / 2, 0);
            } else {
                matrix.postScale(widthScale, widthScale);
                matrix.postTranslate((viewWidth - drawableWidth * widthScale) / 2, (viewHeight - (drawableHeight * widthScale)) / 2);
            }

        }
        sticker.setMatrix(matrix);
        if (onStickerOperationListener != null) {
            onStickerOperationListener.onStickerZoomFinished(sticker);
        }
        invalidate();
    }



    /**
     * 立刻增加指定sticker 到指定图层
     *
     * @param sticker  sticker
     * @param position 位置
     */
    protected void addStickerImmediately(@NonNull Sticker sticker, @Sticker.Position int position, int layer) {
        setStickerPosition(sticker, position);


        float scaleFactor, widthScaleFactor, heightScaleFactor;

        widthScaleFactor = (float) getWidth() / sticker.getWidth();
        heightScaleFactor = (float) getHeight() / sticker.getHeight();
//        widthScaleFactor = (float) getWidth() / sticker.getDrawable().getIntrinsicWidth();
//        heightScaleFactor = (float) getHeight() / sticker.getDrawable().getIntrinsicHeight();
        scaleFactor = widthScaleFactor > heightScaleFactor ? heightScaleFactor : widthScaleFactor;

        sticker.getMatrix()
                .postScale(scaleFactor / 2, scaleFactor / 2, getWidth() / 2, getHeight() / 2);

        if (layer == 0) {
            stickers.add(0, sticker);
        } else if (layer == 1) {
            if (stickers.size() == 0) {
                stickers.add(0, sticker);
            } else {
                if (stickers.get(0).mapLayer == 0) {
//                    说明这一个是底图
                    stickers.add(1, sticker);
                } else {
                    stickers.add(0, sticker);
                }

            }
        }

        handlingSticker = sticker;
        if (onStickerOperationListener != null) {
            onStickerOperationListener.onStickerAdded(sticker);
        }
        invalidate();
    }

    /**
     * 设置sticker 位置 大小
     */

    protected void setStickerPosition(@NonNull Sticker sticker, @Sticker.Position int position) {
        float width;
        float height;
        if (getWidth() == 0 && getHeight() == 0 && position == Sticker.Position.Full) {
            width = getScreenWidth(getContext()) - dp2px(30, getContext());
            height = getScreenWidth(getContext()) - dp2px(30, getContext());
        } else {
            width = getWidth();
            height = getHeight();
        }
        float offsetX = width - sticker.getWidth();
        float offsetY = height - sticker.getHeight();

        if ((position & Sticker.Position.TOP) > 0) {
            offsetY /= 4f;
        } else if ((position & Sticker.Position.BOTTOM) > 0) {
            offsetY *= 3f / 4f;
        } else {
            offsetY /= 2f;
        }
        if ((position & Sticker.Position.LEFT) > 0) {
            offsetX /= 4f;
        } else if ((position & Sticker.Position.RIGHT) > 0) {
            offsetX *= 3f / 4f;
        } else {
            offsetX /= 2f;
        }
        sticker.getMatrix().postTranslate(offsetX, offsetY);
    }

    /**
     * 返回sticker矩阵点 ?
     *
     * @param sticker sticker
     * @return 矩阵点
     */
    @NonNull
    public float[] getStickerPoints(@Nullable Sticker sticker) {
        float[] points = new float[8];
        getStickerPoints(sticker, points);
        return points;
    }

    /**
     * 矩阵点填充到 指定数据组里
     *
     * @param sticker
     * @param dst
     */
    public void getStickerPoints(@Nullable Sticker sticker, @NonNull float[] dst) {
        if (sticker == null) {
            Arrays.fill(dst, 0);
            return;
        }
        sticker.getBoundPoints(bounds);
        sticker.getMappedPoints(dst, bounds);
    }

    /**
     * 保存到指定图片中
     *
     * @param file
     */
    public void save(@NonNull File file) {
        try {
            if (stickers.size() != 0) {
                shapeColor = 0;
                StickerUtils.saveImageToGallery(file, createBitmap());
//                StickerUtils.notifySystemGallery(getContext(), file);
            }
        } catch (IllegalArgumentException | IllegalStateException ignored) {
            //
        }
    }


    /**
     * 生成和原图大小相同的空图片
     *
     * @return 一张和原始图片大小相同的空图片
     * @throws OutOfMemoryError
     */
    @NonNull
    public Bitmap createBitmap() throws OutOfMemoryError {
        handlingSticker = null;
        if (stickers.size() != 0) {
            shapeColor = 0;
        }
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        return bitmap;
    }




    /**
     * 获取当前sticker；列表数目
     *
     * @return sticker数目
     */
    public int getStickerCount() {
        return stickers.size();
    }

    /**
     * 当前sticker是否为空
     *
     * @return 是否为空
     */
    public boolean isNoneSticker() {
        return getStickerCount() == 0;
    }

    public boolean isLocked() {
        return locked;
    }

    /**
     * 锁当前view
     *
     * @param locked
     * @return
     */
    @NonNull
    public StickerView setLocked(boolean locked) {
        this.locked = locked;
        invalidate();
        return this;
    }

    /**
     * 获得区别时间
     *
     * @return 区别时间
     */
    public int getMinClickDelayTime() {
        return minClickDelayTime;
    }

    /**
     * 点击和拖动手势区别的时间
     *
     * @param minClickDelayTime 时间
     * @return
     */
    @NonNull
    public StickerView setMinClickDelayTime(int minClickDelayTime) {
        this.minClickDelayTime = minClickDelayTime;
        return this;
    }

    public boolean isConstrained() {
        return constrained;
    }

    @NonNull
    public StickerView setConstrained(boolean constrained) {
        this.constrained = constrained;
        postInvalidate();
        return this;
    }

    @Nullable
    public OnStickerOperationListener getOnStickerOperationListener() {
        return onStickerOperationListener;
    }

    @NonNull
    public StickerView setOnStickerOperationListener(
            @Nullable OnStickerOperationListener onStickerOperationListener) {
        this.onStickerOperationListener = onStickerOperationListener;
        return this;
    }

    @Nullable
    public Sticker getCurrentSticker() {
        return handlingSticker;
    }

    @NonNull
    public List<BitmapStickerIcon> getIcons() {
        return icons;
    }

    public void setIcons(@NonNull List<BitmapStickerIcon> icons) {
        this.icons.clear();
        this.icons.addAll(icons);
        invalidate();
    }

    public void setOnSelectorListener(OnSelectorListener selecterListener) {
        this.onSelectorListener = selecterListener;
    }

    /**
     * 蒙版不为空的时候 设置绘制监听
     */
    @Override
    public void onGlobalLayout() {
        if (templateBitmap != null && getWidth() != 0 && getHeight() != 0) {
            templateBitmap = Bitmap.createScaledBitmap(templateBitmap, getWidth(), getHeight(), true);
            getViewTreeObserver()
                    .removeGlobalOnLayoutListener(this);
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    public void cleanHandlingSticker() {
        handlingSticker = null;
        invalidate();
    }

    /**
     * 手动关闭引用
     */
    public void releaseHanding() {
        this.icons.clear();
        for (int i = 0; i < stickers.size(); i++) {
            stickers.get(i).release();
        }
        if (whiteBgBitmap != null && !whiteBgBitmap.isRecycled()) {
            whiteBgBitmap.recycle();
        }
        if (templateBitmap != null && !templateBitmap.isRecycled()) {
            templateBitmap.recycle();
        }
        onSelectorListener = null;
        templateShader = null;
        handlingSticker = null;
        onStickerOperationListener = null;
    }

    public Sticker getHandingSticker() {
        return handlingSticker;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    public void moveSticker(int oldPos, int newPos) {
        if (oldPos == newPos) return;
        Sticker remove = stickers.remove(oldPos);
        stickers.add(newPos, remove);
        invalidate();
    }


    @IntDef({
            ActionMode.NONE, ActionMode.DRAG, ActionMode.ZOOM_WITH_TWO_FINGER, ActionMode.ICON,
            ActionMode.CLICK,
    })
    @Retention(RetentionPolicy.SOURCE)
    protected @interface ActionMode {

        int NONE = 0;
        int DRAG = 1;
        int ZOOM_WITH_TWO_FINGER = 2;
        int ICON = 3;
        int CLICK = 4;
    }

    @IntDef(flag = true, value = {FLIP_HORIZONTALLY, FLIP_VERTICALLY})
    @Retention(RetentionPolicy.SOURCE)
    protected @interface Flip {
    }

    /**
     * 操作监听回调
     */
    public interface OnStickerOperationListener {
        void onStickerAdded(@NonNull Sticker sticker);

        void onStickerClicked(@NonNull Sticker sticker);

        void onStickerDeleted(@NonNull Sticker sticker);

        void onStickerDragFinished(@NonNull Sticker sticker);

        void onStickerTouchedDown(@NonNull Sticker sticker);

        void onStickerZoomFinished(@NonNull Sticker sticker);

        void onStickerFlipped(@NonNull Sticker sticker);

        void onStickerDoubleTapped(@NonNull Sticker sticker);

        void onStickerSelected(Sticker sticker);

        void onStickerZoom(Sticker sticker);
    }


    public interface OnSelectorListener {
        void onSelected();
    }


}
