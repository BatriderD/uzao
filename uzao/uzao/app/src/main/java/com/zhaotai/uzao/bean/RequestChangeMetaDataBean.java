package com.zhaotai.uzao.bean;

/**
 * description: 修改元数据的请求类
 * author : ZP
 * date: 2017/12/1 0001.
 */

public class RequestChangeMetaDataBean {
    //        "designMeta":"mock",                //类型：String  必有字段  备注：设计元数据
//                "thmbnail":"mock"                //类型：String  必有字段  备注：缩略图
    private String designMeta;
    private String thmbnail;

    public RequestChangeMetaDataBean(String designMeta, String thmbnail) {
        this.designMeta = designMeta;
        this.thmbnail = thmbnail;
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
}
