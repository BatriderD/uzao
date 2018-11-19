package com.zhaotai.uzao.bean.post;

import java.io.Serializable;

/**
 * Time: 2018/1/31
 * Created by LiYou
 * Description :
 */

public class TemplateDesignInfo implements Serializable {
    public String sampleId;
    public String mkuId;
    public String thmbnail;
    public String designMeta;

    public TemplateDesignInfo(String sampleId, String mkuId, String thmbnail, String designMeta) {
        this.sampleId = sampleId;
        this.mkuId = mkuId;
        this.thmbnail = thmbnail;
        this.designMeta = designMeta;
    }
}
