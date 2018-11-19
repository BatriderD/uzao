package com.zhaotai.uzao.bean;

import android.graphics.Bitmap;

import com.xiaopo.flying.sticker.BitmapUtils;

/**
 * description: 图层数据bean类
 * author : ZP
 * date: 2017/11/11 0011.
 */

public class LayerDataBean {

    public boolean lock = false;

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected = true;
    private Bitmap bitmapContent;
    private boolean visable = true;

    public boolean isVisable() {
        return visable;
    }

    public void setVisable(boolean visable) {
        this.visable = visable;
    }

    public Bitmap getBitmapContent() {
        return bitmapContent;
    }

    public void setBitmapContent(Bitmap bitmapContent) {
        this.bitmapContent = bitmapContent;
    }

    public void recycleBitmap() {
        BitmapUtils.recycleBitmap(bitmapContent);
    }
}
