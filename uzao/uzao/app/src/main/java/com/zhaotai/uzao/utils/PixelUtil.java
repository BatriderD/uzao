package com.zhaotai.uzao.utils;

import android.content.Context;

import com.zhaotai.uzao.app.MyApplication;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description :像素转换器
 */

public class PixelUtil {

    /**
     * dp转 px.
     *
     * @param value the value
     * @return the int
     */
    public static float dp2px(float value) {
        final float scale = MyApplication.getAppResources().getDisplayMetrics().densityDpi;
        return value * scale / 160 + 0.5f;
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
     * px转dp.
     *
     * @param value the value
     * @return the int
     */
    public static float px2dp(float value) {
        final float scale = MyApplication.getAppResources().getDisplayMetrics().densityDpi;
        return value * 160 / scale + 0.5f;
    }

    /**
     * px转dp.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static float px2dp(float value, Context context) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return value * 160 / scale + 0.5f;
    }

    /**
     * px转sp.
     *
     * @param value the value
     * @return the int
     */
    public static int px2sp(float value) {
        final float scale = MyApplication.getAppResources().getDisplayMetrics().scaledDensity;
        return (int) (value/scale +0.5f);
    }
}
