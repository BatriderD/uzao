package com.zhaotai.uzao.bean;

/**
 * Time: 2017/5/13
 * Created by LiYou
 * Description : 商品 实体类
 */

public class CommodityBean {

    public CommodityBean(String designerName, String goodsName, String price) {
        this.designerName = designerName;
        this.goodsName = goodsName;
        this.price = price;
    }

    //设计师名字
    public String designerName;
    //商品名称
    public String goodsName;
    //商品价格
    public String price;
    //商品图片
    public String img;
}
