package com.zhaotai.uzao.utils;

import android.graphics.Color;

/**
 * <strong>颜色工具类</strong><br>
 * <ul>
 * <li>颜色进制转换</li>
 * <li>颜色合法性校验</li>
 * </ul>
 * <br>
 *
 * @author Aaron.ffp
 * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils.java, 2016-03-02 11:32:40.447 Exp $
 */
public class ColorUtils {

    private static String msg = "";

    private static String regHex = "^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$";
    private static String regRgb = "^(RGB\\(|rgb\\()([0-9]{1,3},){2}[0-9]{1,3}\\)$";
    private static String regRepRgb = "(rgb|\\(|\\)|RGB)*";

    public ColorUtils() {
    }

    /**
     * <strong>颜色十六进制转颜色RGB</strong><br>
     * <ul>
     * <li>颜色十六进制参数不合法时，返回null</li>
     * </ul>
     * <br>
     *
     * @param hex 颜色十六进制
     * @return 颜色RGB
     */
    public static String hex2Rgb(String hex) {
        StringBuilder sb = new StringBuilder();

        if (!ColorUtils.isHex(hex)) {
            msg = "颜色十六进制格式 【" + hex + "】 不合法，请确认！";
            LogUtils.logd(msg);
            return null;
        }

        String c = RegUtils.replace(hex.toUpperCase(), "#", "");

        String r = Integer.parseInt((c.length() == 3 ? c.substring(0, 1) + c.substring(0, 1) : c.substring(0, 2)), 16) + "";
        String g = Integer.parseInt((c.length() == 3 ? c.substring(1, 2) + c.substring(1, 2) : c.substring(2, 4)), 16) + "";
        String b = Integer.parseInt((c.length() == 3 ? c.substring(2, 3) + c.substring(2, 3) : c.substring(4, 6)), 16) + "";

        sb.append("RGB(" + r + "," + g + "," + b + ")");

        return sb.toString();
    }


    /**
     * Color对象转换成字符串
     *
     * @param color Color对象
     * @return 16进制颜色字符串
     */
    public static String toHexFromColor(int color) {
        StringBuffer stringBuffer = new StringBuffer("#");
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


    /**
     * <strong>颜色十六进制转颜色RGB</strong><br>
     * <ul>
     * <li>颜色十六进制参数不合法时，返回null</li>
     * </ul>
     * <br>
     *
     * @param hex 颜色十六进制
     * @return 颜色RGB颜色对象类
     */
    public static ColorBean hex2RgbBean(String hex) {
        StringBuilder sb = new StringBuilder();

        if (!ColorUtils.isHex(hex)) {
            msg = "颜色十六进制格式 【" + hex + "】 不合法，请确认！";
            LogUtils.logd(msg);
            return null;
        }

        String c = RegUtils.replace(hex.toUpperCase(), "#", "");

        int r = Integer.parseInt((c.length() == 3 ? c.substring(0, 1) + c.substring(0, 1) : c.substring(0, 2)), 16);
        int g = Integer.parseInt((c.length() == 3 ? c.substring(1, 2) + c.substring(1, 2) : c.substring(2, 4)), 16);
        int b = Integer.parseInt((c.length() == 3 ? c.substring(2, 3) + c.substring(2, 3) : c.substring(4, 6)), 16);

        sb.append("RGB(" + r + "," + g + "," + b + ")");

        return new ColorBean(r, g, b);
    }

    /**
     * 颜色RGB转十六进制
     * <p>
     * 颜色RGB不合法，则返回
     *
     * @param rgb 颜色RGB
     * @return 合法时返回颜色十六进制
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils.java rgb2Hex, 2016-03-15 23:49:33.224 Exp $
     */
    public static String rgb2Hex(String rgb) {
        StringBuilder sb = new StringBuilder();

        if (!ColorUtils.isRgb(rgb)) {
            msg = "颜色 RGB 格式【" + rgb + "】 不合法，请确认！";
            LogUtils.logd(msg);
            return null;
        }

        String r = Integer.toHexString(ColorUtils.getRed(rgb)).toUpperCase();
        String g = Integer.toHexString(ColorUtils.getGreen(rgb)).toUpperCase();
        String b = Integer.toHexString(ColorUtils.getBlue(rgb)).toUpperCase();

        sb.append("#");
        sb.append(r.length() == 1 ? "0" + r : r);
        sb.append(g.length() == 1 ? "0" + g : g);
        sb.append(b.length() == 1 ? "0" + b : b);

        return sb.toString();
    }

    /**
     * 根据各个颜色值 获取颜色字符串
     *
     * @param alp   透明度
     * @param red   红色
     * @param green 绿色
     * @param blue  蓝色
     * @return
     */
    public static String rgb2Hex(int alp, int red, int green, int blue) {
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
        if (alp > 255)
            alp = 255;

        if (alp < 0)
            alp = 0;
        String a = Integer.toHexString(alp).toUpperCase();
        String r = Integer.toHexString(red).toUpperCase();
        String g = Integer.toHexString(green).toUpperCase();
        String b = Integer.toHexString(blue).toUpperCase();

        sb.append("#");
        sb.append(a.length() == 1 ? "0" + a : a);
        sb.append(r.length() == 1 ? "0" + r : r);
        sb.append(g.length() == 1 ? "0" + g : g);
        sb.append(b.length() == 1 ? "0" + b : b);

        return sb.toString();
    }

    /**
     * <strong>获取颜色RGB红色值</strong><br>
     * <br>
     *
     * @param rgb 颜色RGB
     * @return 红色值
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils.java getRed, 2016-03-22 23:48:50.501 Exp $
     */
    public static int getRed(String rgb) {
        return Integer.valueOf(ColorUtils.getRGB(rgb)[0]);
    }

    /**
     * <strong>获取颜色RGB绿色值</strong><br>
     * <br>
     *
     * @param rgb 颜色RGB
     * @return 绿色值
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils.java getGreen, 2016-03-22 23:48:16.290 Exp $
     */
    public static int getGreen(String rgb) {
        return Integer.valueOf(ColorUtils.getRGB(rgb)[1]);
    }

    /**
     * <strong>获取颜色RGB蓝色值</strong><br>
     * <br>
     *
     * @param rgb 颜色RGB
     * @return 蓝色数值
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils.java getBlue, 2016-03-22 23:47:20.801 Exp $
     */
    public static int getBlue(String rgb) {
        return Integer.valueOf(ColorUtils.getRGB(rgb)[2]);
    }

    /**
     * <strong>获取颜色RGB数组</strong><br>
     * <br>
     *
     * @param rgb 颜色RGB
     * @return 颜色数组[红，绿，蓝]
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils.java getRGB, 2016-03-21 23:46:00.944 Exp $
     */
    public static String[] getRGB(String rgb) {
        return RegUtils.replace(RegUtils.replaceSpace(rgb), regRepRgb, "").split(",");
    }

    /**
     * <strong>验证颜色十六进制是否合法</strong><br>
     * <ul>
     * <li>规则：#号开头，三位或六位字母数字组成的字符串</li>
     * </ul>
     * <br>
     *
     * @param hex 十六进制颜色
     * @return 合法则返回true
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils.java isHex, 2016-03-20 23:44:03.133 Exp $
     */
    public static boolean isHex(String hex) {
        return RegUtils.reg(hex, regHex);
    }

    /**
     * <strong>验证颜色RGB是否合法</strong><br>
     * <ul>
     * <li>1、RGB符合正则表达式</li>
     * <li>2、颜色值在0-255之间</li>
     * </ul>
     * <br>
     *
     * @param rgb 颜色RGB
     * @return 是否合法，合法返回true
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils.java isRgb, 2016-03-12 23:41:19.925 Exp $
     */
    public static boolean isRgb(String rgb) {
        boolean r = ColorUtils.getRed(rgb) >= 0 && ColorUtils.getRed(rgb) <= 255;
        boolean g = ColorUtils.getGreen(rgb) >= 0 && ColorUtils.getGreen(rgb) <= 255;
        boolean b = ColorUtils.getBlue(rgb) >= 0 && ColorUtils.getBlue(rgb) <= 255;

        return ColorUtils.isRgbFormat(rgb) && r && g && b;
    }

    /**
     * <strong>验证颜色RGB是否匹配正则表达式</strong><br>
     * <ul>
     * <li>匹配则返回true</li>
     * </ul>
     * <br>
     *
     * @param rgb 颜色RGB
     * @return 是否匹配
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util ColorUtils.java isRgbFormat, 2016-03-03 23:40:12.267 Exp $
     */
    public static boolean isRgbFormat(String rgb) {
        return RegUtils.reg(RegUtils.replaceSpace(rgb), regRgb);
    }


    public static class ColorBean {
        int red;
        int green;
        int blue;

        public ColorBean(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public int getGreen() {
            return green;
        }

        public void setGreen(int green) {
            this.green = green;
        }

        public int getBlue() {
            return blue;
        }

        public void setBlue(int blue) {
            this.blue = blue;
        }

        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            this.red = red;
        }
    }

    /**
     * 设置颜色透明度
     *
     * @param color
     * @param alpha
     * @return color
     */
    public static int setColorAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

}