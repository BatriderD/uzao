package com.xiaopo.flying.sticker;

import java.io.Serializable;

/**
 * description:
 * author : ZP
 * date: 2017/12/6 0006.
 */


public class SourceRectBean implements Serializable {
    /**
     * x : 0
     * y : 0
     * width : 756
     * height : 743
     */

    private int x;
    private int y;
    private int width;
    private int height;

    public SourceRectBean(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public SourceRectBean copy(){
        return new SourceRectBean(x,y,width,height);
    }
}