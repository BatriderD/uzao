package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * description:  我的足迹实体类
 * author : ZP
 * date: 2017/12/2 0002.
 */

public class MyTrackBean extends SectionEntity {

    public int inSidePos;
    /**
     * date : 2017-12-02
     * footprintModels : [{"changedFields":"","description":"","extend1":"","extend2":"","extend3":"","price":2,"priceY":"0.02","recDate":"1512201633000","recStatus":"A","recUserId":"910338411292893184","sequenceNBR":"936867565903306752","spuId":"911855899797139456","spuName":"熊本熊三联抱枕","thumbnail":"911855744402411520.png","userId":"910338411292893184"}]
     */

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FootprintModelsBean getFootprintModel() {
        return footprintModel;
    }

    public void setFootprintModel(FootprintModelsBean footprintModel) {
        this.footprintModel = footprintModel;
    }

    private FootprintModelsBean footprintModel;

    public MyTrackBean(boolean isHeader, String header, int inSidePos) {
        super(isHeader, header);
        this.inSidePos = inSidePos;
    }


    public static class FootprintModelsBean {
        /**
         * changedFields :
         * description :
         * entityId : 950670362494947328
         * entityName : 美女
         * entityType : SourceMaterial
         * extend1 :
         * extend2 :
         * extend3 :
         * price : 0
         * priceY : 0.00
         * recDate : 1516955100000
         * recStatus : A
         * recUserId : 910338411292893184
         * sequenceNBR : 956805155892850688
         * thumbnail : 950670210321375232.jpg
         * userId : 910338411292893184
         */

        private String changedFields;
        private String description;
        private String entityId;
        private String entityName;
        private String entityType;
        private String extend1;
        private String extend2;
        private String extend3;
        private int price;
        private String priceY;
        private String recDate;
        private String recStatus;
        private String recUserId;
        private String sequenceNBR;
        private String thumbnail;
        private String userId;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        private boolean selected;

        public String getChangedFields() {
            return changedFields;
        }

        public void setChangedFields(String changedFields) {
            this.changedFields = changedFields;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getEntityName() {
            return entityName;
        }

        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }

        public String getEntityType() {
            return entityType;
        }

        public void setEntityType(String entityType) {
            this.entityType = entityType;
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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getPriceY() {
            return priceY;
        }

        public void setPriceY(String priceY) {
            this.priceY = priceY;
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

        public String getSequenceNBR() {
            return sequenceNBR;
        }

        public void setSequenceNBR(String sequenceNBR) {
            this.sequenceNBR = sequenceNBR;
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
    }
}
