package com.zhaotai.uzao.bean;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.Serializable;

/**
 * Time: 2017/6/5
 * Created by LiYou
 * Description :  第三方登录图标列表bean
 */

public class ThirdPartLoginIconBean implements Serializable {
    private int icon;
    private String name;

    public SHARE_MEDIA getMedia() {
        return media;
    }

    public void setMedia(SHARE_MEDIA media) {
        this.media = media;
    }

    private SHARE_MEDIA media;

    public ThirdPartLoginIconBean(int icon, String name, SHARE_MEDIA media) {
        this.icon = icon;
        this.name = name;
        this.media = media;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
