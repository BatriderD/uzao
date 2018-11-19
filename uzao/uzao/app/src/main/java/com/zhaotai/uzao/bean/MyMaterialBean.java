package com.zhaotai.uzao.bean;

import java.io.Serializable;

/**
 * description: 我的素材bean类
 * author : ZP
 * date: 2018/1/9 0009.
 */

public class MyMaterialBean {
    /**
     * authPeriod : null
     * categoryCode : A0001
     * categoryCode1 : A
     * categoryCode2 : A00
     * categoryNames : 精灵宝可梦>皮卡丘>喜庆
     * changedFields :
     * createTime : 1515556415000
     * description :
     * designIdea : 2018狗年大吉海报设计
     * designInfo : {"userName":"管理员","nickName":"管理员","avatar":""}
     * designerId : 9527
     * endTime : 4102329600000
     * extend1 :
     * extend2 :
     * extend3 :
     * fileMime : PNG
     * fileRefer : 123.jpg
     * fileSize : 100M
     * periodUnit : forever
     * price : 0
     * promotionPrice : null
     * recDate : 1515556415000
     * recStatus : A
     * recUserId : 908655613469315072
     * remainTimeFormat :
     * saleMode : free
     * sequenceNBR : 950938644431048704
     * source : agency
     * sourceMaterialCode : 948469407577817089
     * sourceMaterialId : 948469407577817088
     * sourceMaterialName : 狗年海报
     * thumbnail : 950666485490016256.jpg
     * userId : 908655613469315072
     */

    private Object authPeriod;
    private DesignInfoBean assignDesigner;
    private String assignDesignerId;
    private String categoryCode;
    private String categoryCode1;
    private String categoryCode2;
    private String categoryNames;
    private String changedFields;
    private String createTime;
    private String description;
    private String designIdea;
    private DesignInfoBean designInfo;
    private String designerId;
    private String endTime;
    private String extend1;
    private String extend2;
    private String extend3;
    private String fileMime;
    private String fileRefer;
    private String fileSize;
    private String periodUnit;
    private int price;
    private Object promotionPrice;
    private String recDate;
    private String recStatus;
    private String recUserId;
    private String remainTimeFormat;
    private String saleMode;
    private String sequenceNBR;
    private String source;
    private String sourceMaterialCode;
    private String sourceMaterialId;
    private String sourceMaterialName;
    private String thumbnail;
    private String userId;
    private float scale;
    public Data data;

    public String getAssignDesignerId() {
        return assignDesignerId;
    }

    public void setAssignDesignerId(String assignDesignerId) {
        this.assignDesignerId = assignDesignerId;
    }

    public DesignInfoBean getAssignDesigner() {
        return assignDesigner;
    }

    public void setAssignDesigner(DesignInfoBean assignDesigner) {
        this.assignDesigner = assignDesigner;
    }

    public class Data implements Serializable {
        public String height;
        public String nickName;
        public float resizeScale;
        public String width;
        public String userId;
        public String fileMime;
        public String avatar;
    }


    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Object getAuthPeriod() {
        return authPeriod;
    }

    public void setAuthPeriod(Object authPeriod) {
        this.authPeriod = authPeriod;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryCode1() {
        return categoryCode1;
    }

    public void setCategoryCode1(String categoryCode1) {
        this.categoryCode1 = categoryCode1;
    }

    public String getCategoryCode2() {
        return categoryCode2;
    }

    public void setCategoryCode2(String categoryCode2) {
        this.categoryCode2 = categoryCode2;
    }

    public String getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(String categoryNames) {
        this.categoryNames = categoryNames;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignIdea() {
        return designIdea;
    }

    public void setDesignIdea(String designIdea) {
        this.designIdea = designIdea;
    }

    public DesignInfoBean getDesignInfo() {
        return designInfo;
    }

    public void setDesignInfo(DesignInfoBean designInfo) {
        this.designInfo = designInfo;
    }

    public String getDesignerId() {
        return designerId;
    }

    public void setDesignerId(String designerId) {
        this.designerId = designerId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2;
    }

    public String getExtend3() {
        return extend3;
    }

    public void setExtend3(String extend3) {
        this.extend3 = extend3;
    }

    public String getFileMime() {
        return fileMime;
    }

    public void setFileMime(String fileMime) {
        this.fileMime = fileMime;
    }

    public String getFileRefer() {
        return fileRefer;
    }

    public void setFileRefer(String fileRefer) {
        this.fileRefer = fileRefer;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Object getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(Object promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(String recStatus) {
        this.recStatus = recStatus;
    }

    public String getRecUserId() {
        return recUserId;
    }

    public void setRecUserId(String recUserId) {
        this.recUserId = recUserId;
    }

    public String getRemainTimeFormat() {
        return remainTimeFormat;
    }

    public void setRemainTimeFormat(String remainTimeFormat) {
        this.remainTimeFormat = remainTimeFormat;
    }

    public String getSaleMode() {
        return saleMode;
    }

    public void setSaleMode(String saleMode) {
        this.saleMode = saleMode;
    }

    public String getSequenceNBR() {
        return sequenceNBR;
    }

    public void setSequenceNBR(String sequenceNBR) {
        this.sequenceNBR = sequenceNBR;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceMaterialCode() {
        return sourceMaterialCode;
    }

    public void setSourceMaterialCode(String sourceMaterialCode) {
        this.sourceMaterialCode = sourceMaterialCode;
    }

    public String getSourceMaterialId() {
        return sourceMaterialId;
    }

    public void setSourceMaterialId(String sourceMaterialId) {
        this.sourceMaterialId = sourceMaterialId;
    }

    public String getSourceMaterialName() {
        return sourceMaterialName;
    }

    public void setSourceMaterialName(String sourceMaterialName) {
        this.sourceMaterialName = sourceMaterialName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static class DesignInfoBean {
        /**
         * userName : 管理员
         * nickName : 管理员
         * avatar :
         */

        private String userName;
        private String nickName;
        private String avatar;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
