package com.zhaotai.uzao.bean.EventBean;

import com.zhaotai.uzao.bean.post.TemplateImageInfo;

import java.util.List;

/**
 * Time: 2017/12/18
 * Created by LiYou
 * Description : 上架需要的数据
 */

public class PutawayEvent {

    public PutawayEvent(){}

    public PutawayEvent(String mkuId, String designId, String isTemplate, String type, List<String> spuImages, List<TemplateImageInfo> skuImages) {
        this.mkuId = mkuId;
        this.designId = designId;
        this.isTemplate = isTemplate;
        this.type = type;
        this.spuImages = spuImages;
        this.skuImages = skuImages;
    }

    public String mkuId;
    public String designId;
    public String isTemplate;
    public String type;//2d 3d
    public List<String> spuImages;
    public List<TemplateImageInfo> skuImages;

}
