package com.zhaotai.uzao.bean;

/**
 * description: sitkcer 图片信息类
 * author : ZP
 * date: 2017/12/29 0029.
 */

public class StickerPicInfoBean {
    public StickerPicInfoBean(String name, float resizeScale) {
        this.name = name;
        this.resizeScale = resizeScale;
    }

    public String name;
    public float resizeScale;
}
