package com.xiaopo.flying.sticker;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/8 0008.
 */

public class ColorData implements Serializable {


    private float saturationNum = 1.0F;
    private float brightNum = 0.0F;
    private float contrastNum = 1.0F;
    //    取值范围  -180  ~180
    private float hue = 0.0F;


    private int saturationProgress = 127;
    private int brightProgress = 127;
    private int contrastProgress = 127;
    //    取值范围  -180  ~180
    private int hueProgress = 127;


    public ColorData() {

    }

    public float getHue() {
        return hue;
    }

    public void setHue(int hue) {
        hueProgress = hue;
        this.hue = (hue - 127) * 1.0f / 127 * 180;
    }

    public int getHueProgress() {
        return hueProgress;
    }

    public int getSaturationProgress() {
        return saturationProgress;
    }


    public int getBrightProgress() {
        return brightProgress;
    }

    public void setBrightProgress(int brightProgress) {
        this.brightProgress = brightProgress;
    }

    public int getContrastProgress() {
        return contrastProgress;
    }

    public void setContrastProgress(int contrastProgress) {
        this.contrastProgress = contrastProgress;
    }


    public float getSaturation() {
        return this.saturationNum;
    }

    public void setSaturation(int saturationNum) {
        saturationProgress = saturationNum;
        this.saturationNum = (float)saturationNum * 1.0F / 128.0F;
    }

    public float getBrightness() {
        return this.brightNum;
    }

    public void setBrightness(int brightNum) {
        brightProgress = brightNum;
        this.brightNum = (float)(brightNum - 128);
    }

    public float getContrast() {
        return this.contrastNum;
    }

    public void setContrast(int contrastNum) {
        contrastProgress = contrastNum;
        this.contrastNum = (float)((double)(contrastNum / 2 + 64) / 128.0D);
    }


}
