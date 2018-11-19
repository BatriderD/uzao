package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * description: 主题的模块实体类
 * author : ZP
 * date: 2018/1/24 0024.
 */

public class ThemeModuleBean implements Serializable {
    public static final String TYPE_CUSTOM_SPU= "designspu";
    public static final String TYPE_SPU= "spu";
    public static final String TYPE_MATERIAL= "material";
    public static final String TYPE_DESIGNER= "designer";
    public static final String TYPE_NEW_SPU= "addSpu";
    public static final String TYPE_NEW_MATERIAL= "addMaterial";

    private boolean canBeEdit = true;
    private String partNo;
    private String description;

    public String entityType;

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    private String themeId;

    public ThemeModuleBean() {
    }

    public boolean isCanBeEdit() {
        return canBeEdit;
    }

    public void setCanBeEdit(boolean canBeEdit) {
        this.canBeEdit = canBeEdit;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ThemeContentModel> getThemeContentModels() {
        return themeContentModels;
    }

    public void setThemeContentModels(List<ThemeContentModel> themeContentModels) {
        this.themeContentModels = themeContentModels;
    }

    private List<ThemeContentModel> themeContentModels;


    public static class ThemeContentModel implements Serializable {


        /**
         * entityType : mock
         * entityId : mock
         * entityName : mock
         * entityPrice : mock
         * entityPic : mock
         * orderNum : mock
         */

        private String entityType;
        private String entityId;
        private String entityName;
        private String entityPrice;
        private String entityPic;
        private String orderNum;
        private String buyCounts;
        private String viewCounts;

        public String getBuyCounts() {
            return buyCounts;
        }

        public void setBuyCounts(String buyCounts) {
            this.buyCounts = buyCounts;
        }

        public String getViewCounts() {
            return viewCounts;
        }

        public void setViewCounts(String viewCounts) {
            this.viewCounts = viewCounts;
        }

        public String getEntityPriceY() {
            return entityPriceY;
        }

        public void setEntityPriceY(String entityPriceY) {
            this.entityPriceY = entityPriceY;
        }

        private String entityPriceY;

        public String getEntityType() {
            return entityType;
        }

        public void setEntityType(String entityType) {
            this.entityType = entityType;
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

        public String getEntityPrice() {
            return entityPrice;
        }

        public void setEntityPrice(String entityPrice) {
            this.entityPrice = entityPrice;
        }

        public String getEntityPic() {
            return entityPic;
        }

        public void setEntityPic(String entityPic) {
            this.entityPic = entityPic;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }
    }

}
