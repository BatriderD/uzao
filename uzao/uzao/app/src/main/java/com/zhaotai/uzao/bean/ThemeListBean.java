package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * description:
 * author : ZP
 * date: 2018/3/17 0017.
 */

public class ThemeListBean {
    /**
     * assocTags :
     * brandAlias :
     * brandId : 0
     * brandName :
     * commentCount : 6
     * createDate : 1517919751000
     * describe : 11111
     * favoriteCount : 0
     * id : 960851194698317824
     * pic : u1/M00/01/36/oYYBAFqmIp2AHH01AAAPMHSJ9jA460.png
     * pyName :
     * source : user
     * tags : [{"code":"0302","name":"小清新"}]
     * themeName : 1111
     * userId : 908263618104045568
     * viewCount : 0
     */

    private String assocTags;
    private String brandAlias;
    private String brandId;
    private String brandName;
    private int commentCount;
    private String createDate;
    private String describe;
    private int favoriteCount;
    private String id;
    private String pic;
    private String pyName;
    private String source;
    private String themeName;
    private String userId;
    private int viewCount;
    private List<TagsBean> tags;

    public String getAssocTags() {
        return assocTags;
    }

    public void setAssocTags(String assocTags) {
        this.assocTags = assocTags;
    }

    public String getBrandAlias() {
        return brandAlias;
    }

    public void setBrandAlias(String brandAlias) {
        this.brandAlias = brandAlias;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPyName() {
        return pyName;
    }

    public void setPyName(String pyName) {
        this.pyName = pyName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean {
        /**
         * code : 0302
         * name : 小清新
         */

        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
