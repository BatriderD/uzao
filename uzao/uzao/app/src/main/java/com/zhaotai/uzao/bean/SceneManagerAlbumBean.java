package com.zhaotai.uzao.bean;

import java.util.List;

public class SceneManagerAlbumBean extends BaseFormBean{
    public boolean isAdd = false;
    /**
     * sequenceNBR : 0
     * recDate :
     * recUserId :
     * recStatus :
     * extend1 :
     * extend2 :
     * extend3 :
     * description :
     * changedFields :
     * themeId : 0
     * userId : 0
     * albumName :
     * albumCover :
     * photoCount : 1
     * createTime :
     * themeAlbumPhotoModels : [{"sequenceNBR":0,"recDate":"","recUserId":"","recStatus":"","extend1":"","extend2":"","extend3":"","description":"","changedFields":"","albumId":0,"userId":0,"name":"","fileId":"","ext":"","length":0,"width":"","height":"","createTime":"","isResize":""}]
     */

    private String sequenceNBR;
    private String recDate;
    private String recUserId;
    private String recStatus;
    private String extend1;
    private String extend2;
    private String extend3;
    private String description;
    private String changedFields;
    private String themeId;
    private String userId;
    private String albumName;
    private String albumCover;
    private String createTime;

    public String getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(String photoCount) {
        this.photoCount = photoCount;
    }

    private String photoCount;
    private List<ThemeAlbumPhotoModelsBean> themeAlbumPhotoModels;

    public SceneManagerAlbumBean(String albumName) {
        this.albumName = albumName;
    }

    public SceneManagerAlbumBean(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public String getSequenceNBR() {
        return sequenceNBR;
    }

    public void setSequenceNBR(String sequenceNBR) {
        this.sequenceNBR = sequenceNBR;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getRecUserId() {
        return recUserId;
    }

    public void setRecUserId(String recUserId) {
        this.recUserId = recUserId;
    }

    public String getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(String recStatus) {
        this.recStatus = recStatus;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<ThemeAlbumPhotoModelsBean> getThemeAlbumPhotoModels() {
        return themeAlbumPhotoModels;
    }

    public void setThemeAlbumPhotoModels(List<ThemeAlbumPhotoModelsBean> themeAlbumPhotoModels) {
        this.themeAlbumPhotoModels = themeAlbumPhotoModels;
    }

    public static class ThemeAlbumPhotoModelsBean {
        /**
         * sequenceNBR : 0
         * recDate :
         * recUserId :
         * recStatus :
         * extend1 :
         * extend2 :
         * extend3 :
         * description :
         * changedFields :
         * albumId : 0
         * userId : 0
         * name :
         * fileId :
         * ext :
         * length : 0
         * width :
         * height :
         * createTime :
         * isResize :
         */

        private int sequenceNBR;
        private String recDate;
        private String recUserId;
        private String recStatus;
        private String extend1;
        private String extend2;
        private String extend3;
        private String description;
        private String changedFields;
        private int albumId;
        private int userId;
        private String name;
        private String fileId;
        private String ext;
        private int length;
        private String width;
        private String height;
        private String createTime;
        private String isResize;

        public int getSequenceNBR() {
            return sequenceNBR;
        }

        public void setSequenceNBR(int sequenceNBR) {
            this.sequenceNBR = sequenceNBR;
        }

        public String getRecDate() {
            return recDate;
        }

        public void setRecDate(String recDate) {
            this.recDate = recDate;
        }

        public String getRecUserId() {
            return recUserId;
        }

        public void setRecUserId(String recUserId) {
            this.recUserId = recUserId;
        }

        public String getRecStatus() {
            return recStatus;
        }

        public void setRecStatus(String recStatus) {
            this.recStatus = recStatus;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getChangedFields() {
            return changedFields;
        }

        public void setChangedFields(String changedFields) {
            this.changedFields = changedFields;
        }

        public int getAlbumId() {
            return albumId;
        }

        public void setAlbumId(int albumId) {
            this.albumId = albumId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getIsResize() {
            return isResize;
        }

        public void setIsResize(String isResize) {
            this.isResize = isResize;
        }
    }
}
