package com.zhaotai.uzao.bean;

/**
 * description: 消息详情bean类
 * author : zp
 * date: 2017/7/21
 */

public class MessageDetailBean {

    /**
     * description :
     * entityId : 870209690921930752
     * entityType : design
     * eventCode : publishUnApprove
     * extend1 : systemMessages
     * extend2 :
     * extend3 :
     * hasReaded : Y
     * messageContent : 很抱歉，您申请上架的商品1122没有通过审核。（理由：${approveIdea}）请您重新完善资质后再提交送审！
     * messageTitle : 很抱歉，您申请上架的商品1122没有通过审核。（理由：${approveIdea}）请您重新完善资质后再提交送审！
     * parentId : null
     * recDate : 1500633285251
     * recStatus : A
     * recUserId : 864692181783351296
     * sequenceNBR : 97020969120712
     * topic :
     * userId : 864692181783351296
     */

    private String description;
    private String entityId;
    private String entityType;
    private String eventCode;
    private String extend1;
    private String extend2;
    private String extend3;
    private String hasReaded;
    private String messageContent;
    private String messageTitle;
    private Object parentId;
    private String recDate;
    private String recStatus;
    private String recUserId;
    private String sequenceNBR;
    private String topic;
    private String userId;

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

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
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

    public String getHasReaded() {
        return hasReaded;
    }

    public void setHasReaded(String hasReaded) {
        this.hasReaded = hasReaded;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public Object getParentId() {
        return parentId;
    }

    public void setParentId(Object parentId) {
        this.parentId = parentId;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
