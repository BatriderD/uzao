package com.zhaotai.uzao.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class UseOnCarrierPreviewBean {
    public UseOnCarrierPreviewBean(String aspectName, Bitmap bitmap) {
        this.aspectName = aspectName;
        this.bitmap = bitmap;
    }

    public UseOnCarrierPreviewBean(String aspectName, String thumbnail) {

        this.aspectName = aspectName;
        this.thumbnail = thumbnail;
    }

    public String aspectName;//"正面"
    public String thumbnail;//11111.jpg
    public Bitmap bitmap;//11111.jpg
}
