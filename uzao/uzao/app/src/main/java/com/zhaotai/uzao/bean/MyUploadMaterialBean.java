package com.zhaotai.uzao.bean;

/**
 * description: 我的上传素材列表
 * author : ZP
 * date: 2018/1/10 0010.
 */

public class MyUploadMaterialBean {

    /**
     * thumbnail : 950280542262149120.jpg
     * price : 1
     * recDate : 1515399817000
     * priceY : 0.01
     * status : published
     * sourceMaterialCode : 950281553399861249
     * sequenceNbr : 950281553399861248
     * fileMime : JPG
     * sourceMaterialName : 王朝晖的素材一
     * saleMode : charge
     */

    private String thumbnail;
    private int price;
    private String recDate;
    private String priceY;
    private String status;
    private String sourceMaterialCode;
    private String sequenceNbr;
    public String sequenceNBR;
    private String fileMime;
    private String sourceMaterialName;
    private String saleMode;
    private float scale;

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getPriceY() {
        return priceY;
    }

    public void setPriceY(String priceY) {
        this.priceY = priceY;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSourceMaterialCode() {
        return sourceMaterialCode;
    }

    public void setSourceMaterialCode(String sourceMaterialCode) {
        this.sourceMaterialCode = sourceMaterialCode;
    }

    public String getSequenceNbr() {
        return sequenceNbr;
    }

    public void setSequenceNbr(String sequenceNbr) {
        this.sequenceNbr = sequenceNbr;
    }

    public String getFileMime() {
        return fileMime;
    }

    public void setFileMime(String fileMime) {
        this.fileMime = fileMime;
    }

    public String getSourceMaterialName() {
        return sourceMaterialName;
    }

    public void setSourceMaterialName(String sourceMaterialName) {
        this.sourceMaterialName = sourceMaterialName;
    }

    public String getSaleMode() {
        return saleMode;
    }

    public void setSaleMode(String saleMode) {
        this.saleMode = saleMode;
    }
}
