package com.zhaotai.uzao.bean.post;

import com.zhaotai.uzao.bean.TemplateBean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/9/11
 * Created by LiYou
 * Description : 载体购买 上架
 */

public class DesignInfo implements Serializable{

    public String sampleId;
    public String skuId;
    public String spuName;
    public String designId;
    public String designIdea;
    public String description;
    public String isTemplate;
    public String salesPriceY;
    public String sourceMaterialIds;
    public List<DesignSkuInfo> skus;
    public List<TemplateBean.TagsBean> tags;


    public List<String> spuImages;
    public List<TemplateImageInfo> skuImages;

}
