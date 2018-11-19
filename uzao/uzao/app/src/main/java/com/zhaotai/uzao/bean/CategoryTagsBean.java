package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * description: 个人标签选择页面
 * author : ZP
 * date: 2018/1/22 0022.
 */

public class CategoryTagsBean {


    /**
     * agencyCode :
     * categoryCode : -1
     * categoryName : 所有标签
     * categoryPy :
     * changedFields :
     * children : [{"agencyCode":"SUPER_ADMIN","categoryCode":"01","categoryName":"产地","categoryPy":"CHANDI","changedFields":"","children":[],"description":"产地","extend1":"","extend2":"","extend3":"","hasChildren":false,"levelNum":1,"orderNum":0,"parentCode":"-1","recDate":"1516957850000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"956816690350477312","tags":[{"agencyCode":"SUPER_ADMIN","categoryCode":"01","description":"","extend1":"","extend2":"","extend3":"","recDate":"1517023562000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"957092305955729408","tagCode":"0101","tagName":"测试","tagType":"sample,spu,material,theme,brand,designer,"},{"agencyCode":"SUPER_ADMIN","categoryCode":"01","description":"","extend1":"","extend2":"","extend3":"","recDate":"1517023574000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"957092355599511552","tagCode":"0102","tagName":"测试11","tagType":"sample,spu,material,theme,brand,designer,"}]},{"agencyCode":"SUPER_ADMIN","categoryCode":"04","categoryName":"颜色","categoryPy":"YANSE","changedFields":"","children":[],"description":"风格","extend1":"","extend2":"","extend3":"","hasChildren":false,"levelNum":1,"orderNum":0,"parentCode":"-1","recDate":"1516957017000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"956813198034554880","tags":[{"agencyCode":"SUPER_ADMIN","categoryCode":"04","description":"","extend1":"","extend2":"","extend3":"","recDate":"1516957825000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"956813303655518208","tagCode":"0401","tagName":"测试1","tagType":"sample,material,theme,brand,designer,"}]},{"agencyCode":"SUPER_ADMIN","categoryCode":"03","categoryName":"风格","categoryPy":"FENGGE","changedFields":"","children":[],"description":"风格","extend1":"","extend2":"","extend3":"","hasChildren":false,"levelNum":1,"orderNum":0,"parentCode":"-1","recDate":"1516957009000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"956813161535721472","tags":[{"agencyCode":"SUPER_ADMIN","categoryCode":"03","description":"","extend1":"","extend2":"","extend3":"","recDate":"1516957521000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"956815311145881600","tagCode":"0302","tagName":"321","tagType":"sample,sample,spu,material,theme,brand,designer,"}]}]
     * description :
     * extend1 :
     * extend2 :
     * extend3 :
     * hasChildren : true
     * levelNum : null
     * orderNum : null
     * parentCode :
     * recDate : null
     * recStatus :
     * recUserId :
     * sequenceNBR : null
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
    private Object levelNum;
    private Object orderNum;
    private String parentCode;
    private Object recDate;
    private String recStatus;
    private String recUserId;
    private Object sequenceNBR;
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

    public Object getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(Object levelNum) {
        this.levelNum = levelNum;
    }

    public Object getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Object orderNum) {
        this.orderNum = orderNum;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Object getRecDate() {
        return recDate;
    }

    public void setRecDate(Object recDate) {
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

    public Object getSequenceNBR() {
        return sequenceNBR;
    }

    public void setSequenceNBR(Object sequenceNBR) {
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
         * categoryCode : 01
         * categoryName : 产地
         * categoryPy : CHANDI
         * changedFields :
         * children : []
         * description : 产地
         * extend1 :
         * extend2 :
         * extend3 :
         * hasChildren : false
         * levelNum : 1
         * orderNum : 0
         * parentCode : -1
         * recDate : 1516957850000
         * recStatus : A
         * recUserId : 908508635489906688
         * sequenceNBR : 956816690350477312
         * tags : [{"agencyCode":"SUPER_ADMIN","categoryCode":"01","description":"","extend1":"","extend2":"","extend3":"","recDate":"1517023562000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"957092305955729408","tagCode":"0101","tagName":"测试","tagType":"sample,spu,material,theme,brand,designer,"},{"agencyCode":"SUPER_ADMIN","categoryCode":"01","description":"","extend1":"","extend2":"","extend3":"","recDate":"1517023574000","recStatus":"A","recUserId":"908508635489906688","sequenceNBR":"957092355599511552","tagCode":"0102","tagName":"测试11","tagType":"sample,spu,material,theme,brand,designer,"}]
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
             * categoryCode : 01
             * description :
             * extend1 :
             * extend2 :
             * extend3 :
             * recDate : 1517023562000
             * recStatus : A
             * recUserId : 908508635489906688
             * sequenceNBR : 957092305955729408
             * tagCode : 0101
             * tagName : 测试
             * tagType : sample,spu,material,theme,brand,designer,
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
            private String tagType;

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            private boolean selected;

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

            public String getTagType() {
                return tagType;
            }

            public void setTagType(String tagType) {
                this.tagType = tagType;
            }

            @Override
            public boolean equals(Object obj) {
                if(obj instanceof TagsBean){
                    if (((TagsBean) obj).getTagCode()== this.tagCode){
                        return true;
                    }else {
                        return false;
                    }
                }else {
                    return false;
                }
            }
        }
    }
}
