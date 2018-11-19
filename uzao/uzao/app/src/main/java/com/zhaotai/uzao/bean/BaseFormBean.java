package com.zhaotai.uzao.bean;

/**
 * 动态表单基类
 */
public class BaseFormBean {

    private String AGENCY_CODE;
    private String GROUP_CODE;
    private String entityId;
    private String entityType;

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
}
