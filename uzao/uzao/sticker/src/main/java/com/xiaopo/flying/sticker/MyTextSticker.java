package com.xiaopo.flying.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.File;

/**
 * description:修改的文字控件
 * author : ZP
 * date: 2017/11/3 0003.
 */

public class MyTextSticker extends Sticker {
    private final TextPaint textPaint;
    private String text;
    private Context context;
    private int textWidth;
    private int textHeight;
    private StaticLayout staticLayout;
    //
    public SpannableString finalText;
    //字体名称
    private String fontName;

    //文件名称
    private String fileName;
    private boolean isHorizon = true;
    /**
     * Line spacing multiplier.
     */
    private float lineSpacingMultiplier = 1.0f;
    /**
     * Additional line spacing.
     */
    private float lineSpacingExtra = 0.0f;
    private StaticLayout defineStaticLayout;
    private String wordartId;

    public String getWordartId() {
        return wordartId;
    }

    public void setWordartId(String wordartId) {
        this.wordartId = wordartId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    private int version;

    public MyTextSticker(Context context) {
        this.context = context;
        text = "请输入文本内容";
        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(28);
        textPaint.setColor(Color.BLACK);
        resizeText();
    }

    public String getFontName() {
        return fontName;
    }

    public float getTextSize() {
        return textPaint.getTextSize();
    }

    public MyTextSticker setTextSize(float size) {
        textPaint.setTextSize(size);
        return this;
    }

    public MyTextSticker setTextSize(float size, boolean sp) {
        textPaint.setTextSize(convertSpToPx(size));
        return this;
    }

    /**
     * 获取字体文件名称
     *
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    public boolean isHorizon() {
        return isHorizon;
    }

    public MyTextSticker setHorizon(boolean horizon) {
        isHorizon = horizon;
        return this;
    }

    public float getLineSpacingExtra() {
        return lineSpacingExtra;
    }

    /**
     * 文字初始化
     * 修改文字信息一定要掉一下这个方法
     */
    public MyTextSticker resizeText() {
        if (isHorizon) {
            //横向
            applySpacing();
            staticLayout = new StaticLayout(finalText, textPaint, (int) Math.ceil(StaticLayout.getDesiredWidth(finalText, textPaint)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            defineStaticLayout = new StaticLayout(finalText, textPaint, (int) Math.ceil(StaticLayout.getDesiredWidth(finalText, textPaint)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        } else {
            //纵向
            staticLayout = new StaticLayout(text, textPaint, (int) Math.ceil(StaticLayout.getDesiredWidth(text.substring(0, 1), textPaint)), Layout.Alignment.ALIGN_CENTER, 1, 10 * lineSpacingExtra, false);
            defineStaticLayout = new StaticLayout(text, textPaint, (int) Math.ceil(StaticLayout.getDesiredWidth(text.substring(0, 1), textPaint)), Layout.Alignment.ALIGN_CENTER, 1, 0.0f, false);
        }
        textWidth = staticLayout.getWidth();
        textHeight = staticLayout.getHeight();
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Matrix matrix = getMatrix();
        canvas.save();
        canvas.concat(matrix);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    @Override
    public int getWidth() {
        return textWidth;
    }

    @Override
    public int getHeight() {
        return textHeight;
    }

    public int getDefineHeight() {
        return defineStaticLayout.getHeight();
    }

    public int getDefineWidth() {
        return defineStaticLayout.getWidth();
    }


    /**
     * 这里不用
     *
     * @param drawable 图片drawable
     * @return this
     */
    @Override
    public Sticker setDrawable(@NonNull Drawable drawable) {
        return null;
    }


    /**
     * 文字没有图片
     *
     * @return drawable
     */
    @NonNull
    @Override
    public Drawable getDrawable() {
        return null;
    }

    /**
     * 设置透明度
     *
     * @param alpha 透明度值  0透明 255不透明
     * @return this
     */
    @NonNull
    @Override
    public Sticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        textPaint.setAlpha(alpha);
        return this;
    }

//    /**
//     * 设置字体 typeface
//     *
//     * @param typeface 字体
//     * @return 文字编辑框
//     */
//    @NonNull
//    public MyTextSticker setTypeface(@Nullable String fileName, @Nullable String fontName, @Nullable Typeface typeface) {
//        this.fileName = fileName;
//        this.fontName = fontName;
//        textPaint.setTypeface(typeface);
//        return this;
//    }

    /**
     * 设置字体 typeface
     *
     * @param typeface 字体
     * @return 文字编辑框
     */
    @NonNull
    public MyTextSticker setTypeface(@Nullable String fileName, @Nullable String fontName, @Nullable Typeface typeface, @NonNull int version, @NonNull String wordartId) {
        this.fileName = fileName;
        this.fontName = fontName;
        this.wordartId = wordartId;
        this.version = version;
        textPaint.setTypeface(typeface);
        return this;
    }

    /**
     * 获得字体
     *
     * @return 字体
     */
    public Typeface getTypeface() {
        return textPaint.getTypeface();
    }

    /**
     * 设置字体 typeface
     *
     * @param typeface 字体
     * @return 文字编辑框
     */
    @NonNull
    public MyTextSticker setTypeface(@Nullable Typeface typeface) {
        textPaint.setTypeface(typeface);
        return this;
    }

    /**
     * 获得字体当前颜色
     *
     * @return 颜色值
     */
    public int getTextColor() {
        return textPaint.getColor();
    }

    /**
     * 设置字体颜色
     *
     * @param color 字体颜色
     * @return textSticker
     */
    @NonNull
    public MyTextSticker setTextColor(@ColorInt int color) {
        textPaint.setColor(color);
        return this;
    }

    /**
     * 设置文字间距
     *
     * @param add        文字间距倍数
     * @param multiplier 增加文字间距
     * @return this
     */
    @NonNull
    public MyTextSticker setLineSpacing(float add, float multiplier) {
        lineSpacingMultiplier = multiplier;
        lineSpacingExtra = add;
        return this;
    }

    /**
     * 设置文字间距
     *
     * @param add 固定增加文字距离
     * @return this
     */
    @NonNull
    public MyTextSticker setLineSpacing(float add) {
        lineSpacingExtra = add;
        return this;
    }

    /**
     * @return the number of pixels which scaledPixels corresponds to on the device.
     */
    private float convertSpToPx(float scaledPixels) {
        return scaledPixels * context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 计算字间距
     */
    private void applySpacing() {
        StringBuilder spaceBuilder = new StringBuilder();
        for (int i = 0; i < lineSpacingExtra; i++) {
            spaceBuilder.append(" ");
        }
        if (this.text == null) return;
        StringBuilder builder = new StringBuilder();
        String[] split = text.split("\n");
        for (int j = 0; j < split.length; j++) {
            for (int i = 0; i < split[j].length(); i++) {
                char c = split[j].charAt(i);
                builder.append(c);
                if (i + 1 < split[j].length()) {
                    if (c != '\n') {
                        builder.append(spaceBuilder.toString());
                    }
                }
            }
            if (split.length >= 1 && j != split.length - 1) {
                builder.append('\n');
            }

        }
        finalText = new SpannableString(builder.toString());
    }

    /**
     * 文本内容
     *
     * @return this
     */
    public String getText() {
        return this.text;
    }

    /**
     * 文字内容
     *
     * @param text 文字
     * @return this
     */
    @NonNull
    public MyTextSticker setText(@Nullable String text) {
        this.text = text;
        return this;
    }

    /**
     * 将控件转成数据
     *
     * @return 数据内容
     */
    @Override
    public StickerDataBean getStickerData() {
        TextStickerDataBean stickerDataBean = new TextStickerDataBean();
        stickerDataBean.type = 1;
        setStickerData(stickerDataBean);
        stickerDataBean.color = toHexFromColor(this.getTextColor());
        stickerDataBean.id = this.getText();
        stickerDataBean.isHorizon = this.isHorizon;
        stickerDataBean.spacing = this.getLineSpacingExtra();
        stickerDataBean.fontFileName = this.fileName;
        stickerDataBean.fontName = this.fontName;
        stickerDataBean.size = this.getTextSize();
        stickerDataBean.version = this.getVersion();
        stickerDataBean.wordartId = this.getWordartId();
        return stickerDataBean;
    }

    @Override
    public String toString() {
        return super.toString() + "  id=" + this.getText() + " isHorizon";
    }

    /**
     * 将数据化转成文字控件
     *
     * @param textStickerDataBean 文本数据
     * @param context             context
     * @return
     */
    public MyTextSticker initMyTextSicker(TextStickerDataBean textStickerDataBean, Context context) {
        Matrix matrix = new Matrix();
        matrix.setValues(textStickerDataBean.matrixData);
        setMatrix(matrix);
        File fontFile = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), textStickerDataBean.fontFileName);
        setTextColor(Color.parseColor(textStickerDataBean.color))
                .setText(textStickerDataBean.id)
                .setLineSpacing(textStickerDataBean.spacing)
                .setHorizon(textStickerDataBean.isHorizon)
                .setTypeface(textStickerDataBean.fontFileName, textStickerDataBean.fontName, FontCache.getFromFile(textStickerDataBean.fontFileName, fontFile.getAbsolutePath()), textStickerDataBean.version, textStickerDataBean.wordartId)
                .setTextSize(textStickerDataBean.size)
                .resizeText();
        return this;
    }

    /**
     * 获得截图
     *
     * @return 截图
     * @throws Exception 异常
     */
    @Override
    public Bitmap getBitmap() throws Exception {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        staticLayout.draw(canvas);
        return bitmap;
    }


    /**
     * Color对象转换成字符串
     *
     * @param color Color对象
     * @return 16进制颜色字符串
     */
    public static String toHexFromColor(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);

        StringBuilder sb = new StringBuilder();

        if (red > 255)
            red = 255;

        if (red < 0)
            red = 0;

        if (green > 255)
            green = 255;

        if (green < 0)
            green = 0;
        if (blue > 255)
            blue = 255;

        if (blue < 0)
            blue = 0;

        String r = Integer.toHexString(red).toUpperCase();
        String g = Integer.toHexString(green).toUpperCase();
        String b = Integer.toHexString(blue).toUpperCase();

        sb.append("#");
        sb.append(r.length() == 1 ? "0" + r : r);
        sb.append(g.length() == 1 ? "0" + g : g);
        sb.append(b.length() == 1 ? "0" + b : b);

        return sb.toString();
    }
}
