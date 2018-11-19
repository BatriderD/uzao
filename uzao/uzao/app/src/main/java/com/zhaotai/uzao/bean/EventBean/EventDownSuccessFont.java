package com.zhaotai.uzao.bean.EventBean;

import android.graphics.Typeface;

/**
 * description:
 * author : ZP
 * date: 2018/3/28 0028.
 */

public class EventDownSuccessFont {
    public String fileName;
    public String fontName;
    public Typeface typeface;
    public int version;
    public String wordartId;

    public EventDownSuccessFont(String fileName, String fontName) {
        this.fileName = fileName;
        this.fontName = fontName;
    }

    public EventDownSuccessFont(String fileName, String fontName, Typeface typeface) {
        this.fileName = fileName;
        this.fontName = fontName;
        this.typeface = typeface;
    }
}
