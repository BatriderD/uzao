package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * description:
 * author : zp
 * date: 2017/8/18
 */

public class HomeValuesBean implements MultiItemEntity, Serializable {


    /**
     * _id : 897629449804115968
     * AGENCY_CODE : SUPER_ADMIN
     * GROUP_CODE : appHomebanner
     * entityId : 2
     * entityType : FREEAPIS
     * referName : hello
     * contentBody : {"aboutMe":"1234567890111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111s","nickName":"hello","favoriteCount":0,"myDesignSpuCount":6,"avatar":"894802824486010880.jpg","cityName":"铜川市"}
     * imageName : 31.jpg
     * referType : designerHome
     * image : 897629078780235776.jpg
     * caption : 轮播
     * referId : 864300998066200576
     * recDate : 1502846512639
     * createDate : 1502846512639
     */
    private int type;


    @Override
    public int getItemType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    private String caption;
    private String brandName;
    private String brandId;
    private String _id;
    private String AGENCY_CODE;
    private String GROUP_CODE;
    private String entityId;
    private String entityType;
    private String referName;
    private String contentBody;
    private String brandBody;
    private String referproductBody1;
    private String referproductBody2;
    private String referproductId2;
    private String imageName;
    private String referType;
    private String referproductName2;
    private String referproductName1;
    private String image;
    private String referproductId1;
    private String referId;
    private String recDate;
    private String createDate;


    public String getReferproductBody1() {
        return referproductBody1;
    }

    public void setReferproductBody1(String referproductBody1) {
        this.referproductBody1 = referproductBody1;
    }

    public String getReferproductBody2() {
        return referproductBody2;
    }

    public void setReferproductBody2(String referproductBody2) {
        this.referproductBody2 = referproductBody2;
    }

    public String getReferproductId2() {
        return referproductId2;
    }

    public void setReferproductId2(String referproductId2) {
        this.referproductId2 = referproductId2;
    }

    public String getReferproductName2() {
        return referproductName2;
    }

    public void setReferproductName2(String referproductName2) {
        this.referproductName2 = referproductName2;
    }

    public String getReferproductName1() {
        return referproductName1;
    }

    public void setReferproductName1(String referproductName1) {
        this.referproductName1 = referproductName1;
    }

    public String getReferproductId1() {
        return referproductId1;
    }

    public void setReferproductId1(String referproductId1) {
        this.referproductId1 = referproductId1;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandBody() {
        return brandBody;
    }

    public void setBrandBody(String brandBody) {
        this.brandBody = brandBody;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAGENCY_CODE() {
        return AGENCY_CODE;
    }

    public void setAGENCY_CODE(String AGENCY_CODE) {
        this.AGENCY_CODE = AGENCY_CODE;
    }

    public String getGROUP_CODE() {
        return GROUP_CODE;
    }

    public void setGROUP_CODE(String GROUP_CODE) {
        this.GROUP_CODE = GROUP_CODE;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getReferName() {
        return referName;
    }

    public void setReferName(String referName) {
        this.referName = referName;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getReferType() {
        return referType;
    }

    public void setReferType(String referType) {
        this.referType = referType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}


