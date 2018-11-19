package com.zhaotai.uzao.bean;

/**
 * 相册管理实体类
 */
public class ScenePhotoManagerBean extends BaseFormBean{
    public boolean selected;
    /**
     * albumId : 1025323487717285888
     * changedFields :
     * createTime : 1533311603000
     * description :
     * ext : jpeg
     * extend1 :
     * extend2 :
     * extend3 :
     * fileId : u2/M00/05/1C/ooYBAFtkenKAau-6AAWG3JHxYqA16.jpeg
     * height : 931
     * isResize :
     * length : 362204
     * name : 20151031200037_zuWna.jpeg
     * recDate : 1533311603000
     * recStatus : A
     * recUserId : 952623124682199040
     * sequenceNBR : 1025409300991893504
     * userId : 910338054026272768
     * width : 658
     */

    private String albumId;
    private String changedFields;
    private String createTime;
    private String description;
    private String ext;
    private String extend1;
    private String extend2;
    private String extend3;
    private String fileId;
    private String height;
    private String isResize;
    private int length;
    private String name;
    private String recDate;
    private String recStatus;
    private String recUserId;
    private String sequenceNBR;
    private String userId;
    private String width;

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getIsResize() {
        return isResize;
    }

    public void setIsResize(String isResize) {
        this.isResize = isResize;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return "ScenePhotoManagerBean{" +
                "ext='" + ext + '\'' +
                ", fileId='" + fileId + '\'' +
                ", height='" + height + '\'' +
                ", isResize='" + isResize + '\'' +
                ", length=" + length +
                ", name='" + name + '\'' +
                '}';
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
