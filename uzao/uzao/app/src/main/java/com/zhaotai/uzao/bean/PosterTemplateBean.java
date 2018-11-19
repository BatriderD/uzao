package com.zhaotai.uzao.bean;

public class PosterTemplateBean extends BaseFormBean {
    private String _id;
    private String lockStatus;
    private String qrShareType;
    private String posterType;
    private String designMeta;
    private String usePointType;
    private String posterBackgroundUrl;
    private String usePointNameOrId;
    private String posterBackgroundId;
    private String recDate;
    private String nodeLevel;
    private String createDate;
    private Object qrShareId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getQrShareType() {
        return qrShareType;
    }

    public void setQrShareType(String qrShareType) {
        this.qrShareType = qrShareType;
    }

    public String getPosterType() {
        return posterType;
    }

    public void setPosterType(String posterType) {
        this.posterType = posterType;
    }

    public String getDesignMeta() {
        return designMeta;
    }

    public void setDesignMeta(String designMeta) {
        this.designMeta = designMeta;
    }

    public String getUsePointType() {
        return usePointType;
    }

    public void setUsePointType(String usePointType) {
        this.usePointType = usePointType;
    }

    public String getPosterBackgroundUrl() {
        return posterBackgroundUrl;
    }

    public void setPosterBackgroundUrl(String posterBackgroundUrl) {
        this.posterBackgroundUrl = posterBackgroundUrl;
    }

    public String getUsePointNameOrId() {
        return usePointNameOrId;
    }

    public void setUsePointNameOrId(String usePointNameOrId) {
        this.usePointNameOrId = usePointNameOrId;
    }

    public String getPosterBackgroundId() {
        return posterBackgroundId;
    }

    public void setPosterBackgroundId(String posterBackgroundId) {
        this.posterBackgroundId = posterBackgroundId;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(String nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Object getQrShareId() {
        return qrShareId;
    }

    public void setQrShareId(Object qrShareId) {
        this.qrShareId = qrShareId;
    }
}
