package com.zhaotai.uzao.bean;

import java.io.Serializable;

/**
 * description: 工艺的请求bean类
 * author : ZP
 * date: 2017/12/16 0016.
 */

public class TechnologyBean implements Serializable {
    /**
     * changedFields :
     * craftCode : 00012
     * craftName : 喷TT绘
     * description : 结局解决军军军军
     * extend1 :
     * extend2 :
     * extend3 :
     * lockDate : 1513340348000
     * lockStatus : N
     * lockUserId : 908232819585626112
     * recDate : 1513340333000
     * recStatus : A
     * recUserId : 908232819585626112
     * sequenceNBR : 941557773215318016
     * thumbnail : 941639521613709312_resize.png
     * userName : 王浩
     * video : 941640048250519552.mp4
     */

    private String changedFields;
    private String craftCode;
    private String craftName;
    private String description;
    private String extend1;
    private String extend2;
    private String extend3;
    private String lockDate;
    private String lockStatus;
    private String lockUserId;
    private String recDate;
    private String recStatus;
    private String recUserId;
    private String sequenceNBR;
    private String thumbnail;
    private String userName;
    private String video;
    public boolean selected;

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }

    public String getCraftCode() {
        return craftCode;
    }

    public void setCraftCode(String craftCode) {
        this.craftCode = craftCode;
    }

    public String getCraftName() {
        return craftName;
    }

    public void setCraftName(String craftName) {
        this.craftName = craftName;
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

    public String getLockDate() {
        return lockDate;
    }

    public void setLockDate(String lockDate) {
        this.lockDate = lockDate;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getLockUserId() {
        return lockUserId;
    }

    public void setLockUserId(String lockUserId) {
        this.lockUserId = lockUserId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
