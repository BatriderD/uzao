package com.xiaopo.flying.sticker;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class FontCache {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface get(String name, Context context) {
        if (name == "normal") return Typeface.DEFAULT;
        Typeface tf = fontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }

//    /**
//     * 从文件中获取字体
//     *
//     * @param fileName 字体名称
//     * @param filePath 字体文件路径
//     * @return 字体typeface
//     */
//    public static Typeface getFromFile(String fileName, String filePath) {
//
//        Typeface tf;
//            try {
//                tf = Typeface.createFromFile(filePath);
//                if (tf != null) {
//                    fontCache.put(fileName, tf);
//                }
//            } catch (Exception e) {
//                return null;
//            }
//
//        return tf;
//    }

    /**
     * 从文件中获取字体
     *
     * @param fileName 字体名称
     * @param filePath 字体文件路径
     * @return 字体typeface
     */
    public static Typeface getFromFile(String fileName, String filePath) {

        Typeface tf = fontCache.get(fileName);

        if (tf == null) {
            try {
                tf = Typeface.createFromFile(filePath);
                if (tf != null) {
                    fontCache.put(fileName, tf);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return tf;
    }

    public static Typeface reFreshFile(String fileName, String filePath) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromFile(filePath);
            if (tf != null) {
                fontCache.put(fileName, tf);
            }
        } catch (Exception e) {
            return null;
        }
        return tf;
    }

}