package com.xiaopo.flying.sticker;

/**
 * description:
 * author : ZP
 * date: 2017/11/22 0022.
 */

public class MyDrawableStickerDataBean extends StickerDataBean {

    public String originalUrl;
    public boolean isAlph;
    public SourceRectBean clipBean;
    public String filterType;
    public String filterName;
    public String filterPic;
    //    图片的缩放系数
    public float resizeScale;
    //    标准缩放系数
    public float localStanderScale;
    public boolean isVector;
}
