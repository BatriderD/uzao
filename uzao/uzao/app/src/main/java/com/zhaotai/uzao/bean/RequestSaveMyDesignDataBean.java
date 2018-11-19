package com.zhaotai.uzao.bean;

/**
 * description:
 * author : ZP
 * date: 2017/12/1 0001.
 */

public class RequestSaveMyDesignDataBean {


    private String designMeta;       //设计元数据
    private String thmbnail;         //缩略图
    private String sampleId;         //载体id
    private String mkuId;            // mkuid
    private String templateSpuId;    //模板商品id
    private String designType;       //备注：无

    public RequestSaveMyDesignDataBean(String designMeta, String thmbnail, String sampleId, String mkuId) {
        this.designMeta = designMeta;
        this.thmbnail = thmbnail;
        this.sampleId = sampleId;
        this.mkuId = mkuId;
    }

    public RequestSaveMyDesignDataBean(String designMeta, String thmbnail, String sampleId) {
        this.designMeta = designMeta;
        this.thmbnail = thmbnail;
        this.sampleId = sampleId;
    }

    public String getDesignMeta() {
        return designMeta;
    }

    public void setDesignMeta(String designMeta) {
        this.designMeta = designMeta;
    }

    public String getThmbnail() {
        return thmbnail;
    }

    public void setThmbnail(String thmbnail) {
        this.thmbnail = thmbnail;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getMkuId() {
        return mkuId;
    }

    public void setMkuId(String mkuId) {
        this.mkuId = mkuId;
    }

    public String getTemplateSpuId() {
        return templateSpuId;
    }

    public void setTemplateSpuId(String templateSpuId) {
        this.templateSpuId = templateSpuId;
    }

    public String getDesignType() {
        return designType;
    }

    @Override
    public String toString() {
        return "RequestSaveMyDesignDataBean{" +
                "designMeta='" + designMeta + '\'' +
                ", thmbnail='" + thmbnail + '\'' +
                ", sampleId='" + sampleId + '\'' +
                ", mkuId='" + mkuId + '\'' +
                ", templateSpuId='" + templateSpuId + '\'' +
                ", designType='" + designType + '\'' +
                '}';
    }
}
