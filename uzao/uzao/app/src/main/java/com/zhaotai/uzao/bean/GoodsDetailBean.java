package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * Time: 2017/5/17
 * Created by LiYou
 * Description : 商品详情实体类
 */

public class GoodsDetailBean {
    //基本信息
    public BasicInfo basicInfo;
    //商品详情
    public String sampleSpuContent;
    //设计详情 包括轮播图
    public String designInfo;
    //属性
    public SampleSpu sampleSpu;
    //设计师信息
    public PersonBean designer;

    public class BasicInfo {
        //设计理念
        public String designIdea;
        //价格
        public String salesPrice;
        public String salesPriceY;
        public SpuModel spuModel;
        public String sequenceNBR;
        public String sampleId;// spuid

        public class SpuModel {
            //商品名字
            public String spuName;
            public String thumbnail;
        }
    }

    public class SampleSpu {
        public List<Sku> skus;
        public List<PropertyBean> properties;
    }

    public class Sku {
        public String spuId;
        public String storeNum;//库存
        public String sequenceNBR;// skuid
        public List<PropertyBean> skuProperties;
    }

}
