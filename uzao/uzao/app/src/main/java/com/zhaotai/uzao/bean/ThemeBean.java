package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * description: 主题bean
 * author : ZP
 * date: 2018/1/22 0022.
 */

public class ThemeBean implements Serializable {

    public boolean selected;
    public String id;
    public String themeId;
    public String status;
    public String name;
    public String isPublic;
    public String cover;
    public String description;
    public long recDate;
    public String sequenceNBR;
    public String materialCount;
    public String spuCount;
    public String recStatus;
    public String userId;
    public String type;
    public String canComment;

    public List<TagsBean> tags;
    public String pic;
    public String themeName;
    public String commentCount;
    public String favoriteCount;
    public String viewCount;
    public String userName;
    public String avatar;
    public String wapUrl;


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public static class TagsBean implements Serializable {
        public String tagCode;
        public String tagName;

        public TagsBean(String tagCode, String tagName) {
            this.tagCode = tagCode;
            this.tagName = tagName;
        }
    }
}
