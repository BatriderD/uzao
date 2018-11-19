package com.zhaotai.uzao.bean;

import com.zhaotai.uzao.global.GlobalVariable;

/**
 * Time: 2017/7/20
 * Created by LiYou
 * Description : 意见反馈
 */

public class RequestSaveArtWorkBean {


   public String designName;
   public String designIdea;
   public String isPrivate;
   public String designMeta;
   public String thmbnail;
   public String source = GlobalVariable.ANDROID;
    public RequestSaveArtWorkBean(String title, String idea, String isPrivate, String jSonString, String filename) {
        designName =title;
        designIdea = idea;
        this.isPrivate = isPrivate;
        this.designMeta = jSonString;
        this.thmbnail = filename;
    }
}
