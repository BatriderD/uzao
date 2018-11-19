package com.zhaotai.uzao.bean.post;

import java.io.Serializable;

/**
 * Time: 2017/9/11
 * Created by LiYou
 * Description :
 */

public class TemplateImageInfo implements Serializable{

    public String aspectName;//"正面"
    public String thumbnail;//11111.jpg




    public TemplateImageInfo(String aspectName) {
        this.aspectName = aspectName;

    }

    public TemplateImageInfo(String aspectName, String thumbnail) {
        this.aspectName = aspectName;
        this.thumbnail = thumbnail;
    }
}
