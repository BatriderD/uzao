package com.zhaotai.uzao.bean;
/**
 * description:贴纸标签 bean
 * author : ZP
 * date: 2017/9/18 0018.
 */

import java.util.List;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class StickerTagBean {

    /**
     * agencyCode : SUPER_ADMIN
     * categoryCode : MATERIALTAG
     * categoryName : 素材标签
     * categoryPy : SUCAIBIAOQIAN
     * changedFields :
     * children : [{"agencyCode":"SUPER_ADMIN","categoryCode":"024","categoryName":"表情","categoryPy":"BIAOQING","changedFields":"","children":[],"description":"","extend1":"","extend2":"","extend3":"","hasChildren":false,"levelNum":2,"orderNum":0,"parentCode":"MATERIALTAG","recDate":"1501636991000","recStatus":"A","recUserId":"864292916800020480","sequenceNBR":"892556349538373632","tags":[{"agencyCode":"SUPER_ADMIN","categoryCode":"024","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639744000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892567899380723712","tagCode":"0241","tagName":"表情标签"},{"agencyCode":"SUPER_ADMIN","categoryCode":"024","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639758000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892567957119512576","tagCode":"0242","tagName":"表情标签测试"},{"agencyCode":"SUPER_ADMIN","categoryCode":"024","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639779000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892568045216673792","tagCode":"0243","tagName":"表情笑脸"}]},{"agencyCode":"SUPER_ADMIN","categoryCode":"023","categoryName":"色彩","categoryPy":"SECAI","changedFields":"","children":[],"description":"","extend1":"","extend2":"","extend3":"","hasChildren":false,"levelNum":2,"orderNum":0,"parentCode":"MATERIALTAG","recDate":"1501636968000","recStatus":"A","recUserId":"864292916800020480","sequenceNBR":"892556253312651264","tags":[{"agencyCode":"SUPER_ADMIN","categoryCode":"023","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639705000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892567732258680832","tagCode":"0231","tagName":"色彩标签"},{"agencyCode":"SUPER_ADMIN","categoryCode":"023","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1503037015000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"898428477580034048","tagCode":"0232","tagName":"色彩标签测试"}]},{"agencyCode":"SUPER_ADMIN","categoryCode":"022","categoryName":"文字","categoryPy":"WENZI","changedFields":"","children":[],"description":"","extend1":"","extend2":"","extend3":"","hasChildren":false,"levelNum":2,"orderNum":0,"parentCode":"MATERIALTAG","recDate":"1501636959000","recStatus":"A","recUserId":"864292916800020480","sequenceNBR":"892556215987539968","tags":[{"agencyCode":"SUPER_ADMIN","categoryCode":"022","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639635000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892567442264502272","tagCode":"0221","tagName":"文字标签"},{"agencyCode":"SUPER_ADMIN","categoryCode":"022","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639657000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892567530386829312","tagCode":"0222","tagName":"文字测试标签"}]},{"agencyCode":"SUPER_ADMIN","categoryCode":"021","categoryName":"饰品","categoryPy":"SHIPIN","changedFields":"","children":[],"description":"","extend1":"","extend2":"","extend3":"","hasChildren":false,"levelNum":2,"orderNum":0,"parentCode":"MATERIALTAG","recDate":"1501636947000","recStatus":"A","recUserId":"864292916800020480","sequenceNBR":"892556165781721088","tags":[{"agencyCode":"SUPER_ADMIN","categoryCode":"021","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639543000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892567056195596288","tagCode":"0201","tagName":"饰品标签"},{"agencyCode":"SUPER_ADMIN","categoryCode":"021","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639556000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892567107869421568","tagCode":"0203","tagName":"饰品标签测试1"},{"agencyCode":"SUPER_ADMIN","categoryCode":"021","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639571000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892567173086654464","tagCode":"0202","tagName":"饰品测试1"}]}]
     * description :
     * extend1 :
     * extend2 :
     * extend3 :
     * hasChildren : false
     * levelNum : 1
     * orderNum : 0
     * parentCode : -1
     * recDate : 1501636760000
     * recStatus : A
     * recUserId : 864292916800020480
     * sequenceNBR : 892555379706236928
     * tags : []
     */

    private String agencyCode;
    private String categoryCode;
    private String categoryName;
    private String categoryPy;
    private String changedFields;
    private String description;
    private String extend1;
    private String extend2;
    private String extend3;
    private boolean hasChildren;
    private int levelNum;
    private int orderNum;
    private String parentCode;
    private String recDate;
    private String recStatus;
    private String recUserId;
    private String sequenceNBR;
    private List<ChildrenBean> children;
    private List<?> tags;

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryPy() {
        return categoryPy;
    }

    public void setCategoryPy(String categoryPy) {
        this.categoryPy = categoryPy;
    }

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

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public List<?> getTags() {
        return tags;
    }

    public void setTags(List<?> tags) {
        this.tags = tags;
    }

    public static class ChildrenBean {
        /**
         * agencyCode : SUPER_ADMIN
         * categoryCode : 024
         * categoryName : 表情
         * categoryPy : BIAOQING
         * changedFields :
         * children : []
         * description :
         * extend1 :
         * extend2 :
         * extend3 :
         * hasChildren : false
         * levelNum : 2
         * orderNum : 0
         * parentCode : MATERIALTAG
         * recDate : 1501636991000
         * recStatus : A
         * recUserId : 864292916800020480
         * sequenceNBR : 892556349538373632
         * tags : [{"agencyCode":"SUPER_ADMIN","categoryCode":"024","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639744000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892567899380723712","tagCode":"0241","tagName":"表情标签"},{"agencyCode":"SUPER_ADMIN","categoryCode":"024","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639758000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892567957119512576","tagCode":"0242","tagName":"表情标签测试"},{"agencyCode":"SUPER_ADMIN","categoryCode":"024","description":"","extend1":"MATERIALTAG","extend2":"","extend3":"","recDate":"1501639779000","recStatus":"A","recUserId":"864346236910678016","sequenceNBR":"892568045216673792","tagCode":"0243","tagName":"表情笑脸"}]
         */

        private String agencyCode;
        private String categoryCode;
        private String categoryName;
        private String categoryPy;
        private String changedFields;
        private String description;
        private String extend1;
        private String extend2;
        private String extend3;
        private boolean hasChildren;
        private int levelNum;
        private int orderNum;
        private String parentCode;
        private String recDate;
        private String recStatus;
        private String recUserId;
        private String sequenceNBR;
        private List<?> children;
        private List<TagsBean> tags;

        public String getAgencyCode() {
            return agencyCode;
        }

        public void setAgencyCode(String agencyCode) {
            this.agencyCode = agencyCode;
        }

        public String getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryPy() {
            return categoryPy;
        }

        public void setCategoryPy(String categoryPy) {
            this.categoryPy = categoryPy;
        }

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

        public boolean isHasChildren() {
            return hasChildren;
        }

        public void setHasChildren(boolean hasChildren) {
            this.hasChildren = hasChildren;
        }

        public int getLevelNum() {
            return levelNum;
        }

        public void setLevelNum(int levelNum) {
            this.levelNum = levelNum;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public String getParentCode() {
            return parentCode;
        }

        public void setParentCode(String parentCode) {
            this.parentCode = parentCode;
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

        public List<?> getChildren() {
            return children;
        }

        public void setChildren(List<?> children) {
            this.children = children;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public static class TagsBean {
            /**
             * agencyCode : SUPER_ADMIN
             * categoryCode : 024
             * description :
             * extend1 : MATERIALTAG
             * extend2 :
             * extend3 :
             * recDate : 1501639744000
             * recStatus : A
             * recUserId : 864346236910678016
             * sequenceNBR : 892567899380723712
             * tagCode : 0241
             * tagName : 表情标签
             */

            private String agencyCode;
            private String categoryCode;
            private String description;
            private String extend1;
            private String extend2;
            private String extend3;
            private String recDate;
            private String recStatus;
            private String recUserId;
            private String sequenceNBR;
            private String tagCode;
            private String tagName;

            public String getAgencyCode() {
                return agencyCode;
            }

            public void setAgencyCode(String agencyCode) {
                this.agencyCode = agencyCode;
            }

            public String getCategoryCode() {
                return categoryCode;
            }

            public void setCategoryCode(String categoryCode) {
                this.categoryCode = categoryCode;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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

            public String getTagCode() {
                return tagCode;
            }

            public void setTagCode(String tagCode) {
                this.tagCode = tagCode;
            }

            public String getTagName() {
                return tagName;
            }

            public void setTagName(String tagName) {
                this.tagName = tagName;
            }
        }
    }
}
