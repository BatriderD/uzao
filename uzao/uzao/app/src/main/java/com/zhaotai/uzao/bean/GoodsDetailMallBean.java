package com.zhaotai.uzao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Time: 2017/5/17
 * Created by LiYou
 * Description : 商品详情实体类
 */

public class GoodsDetailMallBean implements Serializable {

    public List<String> spuImages;

    //基本信息
    public BasicInfo basicInfo;
    //活动信息
    public ActivityModelBean activityInfo;
    //商品详情
    public List<String> detailContent;
    //载体信息
    public SpuModel sampleSpu;

    public List<Sku> skus;

    public List<PropertyBean> properties;

    //设计师信息
    public PersonBean designer;

    public List<TagBean> tagInfo;


    public class BasicInfo implements Serializable {
        public List<String> spuImages;
        //标签
        public List<TagBean> tags;

        public String designIdea;

        public String sequenceNBR;

        public SpuModel spuModel;

        public String sampleId;

        public String isTemplate;

        public String mkuId;

        public String designType;

        public String marketPriceY;
    }


    public class Sku implements Serializable {
        public String spuId;

        public int storeNum;//库存

        public String sequenceNBR;// skuid

        public String skuValues;//

        public String skuKey;//

        public String skuName;

        public int marketPrice;

        public String thumbnail;

        public String price;

        public String priceY;

        public String marketPriceY;

        public List<PropertyBean> skuProperties;
    }

    public class SpuModel implements Serializable {
        //商品价格
        public String displayPriceY;

        public String displayPrice;
        //商品名字
        public String spuName;
        //商品图片
        public String thumbnail;
        //商品类型
        public String spuType;

        public String brandId;

        public String brandName;

        public String status;

        public DesignerBean assignDesigner;//设计师

        public String designerId;//设计师ID
    }
}
