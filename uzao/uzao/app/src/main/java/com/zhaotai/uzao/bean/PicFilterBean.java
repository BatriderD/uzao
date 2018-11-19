package com.zhaotai.uzao.bean;

/**
 * description: 图片滤镜item Bean
 * author : ZP
 * date: 2017/12/28 0028.
 */

public class PicFilterBean {
    public String pic;
    public String name;
    public String type;

    public PicFilterBean(String pic, String name, String type) {
        this.pic = pic;
        this.name = name;
        this.type = type;
    }
}
