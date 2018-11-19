package com.zhaotai.uzao.bean;

import java.io.Serializable;

public class SceneManagerPostBean extends BaseFormBean implements Serializable {
    /**
     * _id : 1025188749753004032
     * contentBody : <p>ggg<img src="http://192.168.2.32/u1/M00/05/1A/oYYBAFtjrPyAcEpTAAMNzmLqxeo93.jpeg" data-filename="20140529202219_MC4Te.jpeg" style="width: 938px;"></p><p>gfgfgfg<img src="http://192.168.2.32/u2/M00/05/19/ooYBAFtjrQSACvvZAAFp7c6rk5o54.jpeg" data-filename="20150416H0746_uczkX.thumb.700_0.jpeg" style="width: 700px;"></p>
     * title : 657
     * themeId : 1018752982981894144
     * nickName : null
     * userId : 952623124682199040
     * abstractContent : ggg gfgfgfg
     * images : /u1/M00/05/1A/oYYBAFtjrPyAcEpTAAMNzmLqxeo93.jpeg,/u2/M00/05/19/ooYBAFtjrQSACvvZAAFp7c6rk5o54.jpeg
     * isTop : N
     * avatar : null
     * isEssence : N
     * recDate : 1533259019684
     * nodeLevel : 0
     * createDate : 1533259019684
     */

    private String _id;
    private String contentBody;
    private String title;
    private String themeId;
    private String nickName;
    private String userId;
    private String abstractContent;
    private String images;
    private String isTop;
    private String avatar;
    private String isEssence;
    private String recDate;
    private String nodeLevel;
    private String createDate;
    private String commentCount;
    public String id;

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAbstractContent() {
        return abstractContent;
    }

    public void setAbstractContent(String abstractContent) {
        this.abstractContent = abstractContent;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIsEssence() {
        return isEssence;
    }

    public void setIsEssence(String isEssence) {
        this.isEssence = isEssence;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(String nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
