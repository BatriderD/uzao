package com.xiaopo.flying.sticker;

import java.io.Serializable;

/**
 * description: sticker的变形数据都存这里面
 * author : ZP
 * date: 2017/12/8 0008.
 */

public class StickerDataInfo implements Serializable {
    //    素材id
    public String materialId;
    //    原始图片地址
    public String url;
    //    1裁剪数据
    public SourceRectBean clipBean;
    //    2白色透明化数据
    public boolean isAlph;
    //    滤镜
    public String filterName;
    //    滤镜类别
    public String filterType;
    //    滤镜地址
    public String filterPic;
    //    图片的缩放系数
    public float resizeScale;
    public float localStanderScale;
    public boolean isVector;
    //图片版本  可能有修改 需要判断是不是一版本的
    public int version;

    public StickerDataInfo copy() {
        StickerDataInfo info = new StickerDataInfo();
        info.isAlph = isAlph;
        info.url = url;
        if (clipBean != null) {
            info.clipBean = new SourceRectBean(clipBean.getX(), clipBean.getY(), clipBean.getWidth(), clipBean.getHeight());
        }
        info.filterName = filterName;
        info.filterPic = filterPic;
        info.resizeScale = resizeScale;
        info.isVector = isVector;
        return info;
    }
}
