package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/9/6
 * Created by LiYou
 * Description :
 */

public class TemplateBean implements Serializable{

    public static final String TYPE_3D = "3d";

    public String designType; //判断2d 还是 3d
    public List<TagsBean> tags;    //标签
    public String designIdea;
    public String sequenceNBR;
    public String sampleId;
    public String spuId;
    public List<String> spuImages;
    public String marketPriceY;
    public String designCount;
    public String spuName;
    public String image;
    public String salesPriceY;
    public String mkuValues;
    public String id;
    public String skuId;
    public String recommendedPriceY;
    public GoodsBean spuModel;
    public GoodsBean basicInfo;
    public List<PropertyBean> spuPropertyTypes;
    public List<Mku> mkus;
    public List<Sku> skus;
    public List<Sku> productSkus;
    public List<TagsBean> tagInfo;
    public SampleSpu sampleSpu;

    public class Mku implements Serializable{
        public String mkuKey;
        public String mkuValues;
        public String sequenceNBR;
    }

    public class Sku implements Serializable{
        public String skuKey;
        public String skuValues;
        public int storeNum;//库存
        public String sequenceNBR;
        public String priceY;//成本价
        public int price;//成本价
        public int marketPrice;//上架价格
        public String marketPriceY;//成本价
        public boolean isSelected;
        public String thumbnail;
        public String salePrice;

    }

    public class SampleSpu implements Serializable {
        public String recommendedPriceY;//建议价格
        public String marketPriceY;//成本价
        public String spuCode;//商品编号
        public List<TagsBean> tags;
    }


    public static class TagsBean {
        public TagsBean() {
        }

        public TagsBean(String tagCode, String tagName) {
            this.tagCode = tagCode;
            this.tagName = tagName;
        }

        public String tagCode;
        public String tagName;
    }

}
