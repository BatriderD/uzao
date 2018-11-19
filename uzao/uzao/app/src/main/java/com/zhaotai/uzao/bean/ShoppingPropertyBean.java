package com.zhaotai.uzao.bean;

/**
 * Time: 2017/5/25
 * Created by LiYou
 * Description : 加入购物车时 用到
 */

public class ShoppingPropertyBean {

    public ShoppingPropertyBean(String spuId, String skuId, int count) {
        this.spuId = spuId;
        this.skuId = skuId;
        this.count = count;
    }

    public String spuId;
    public String skuId;
    public int count;

    @Override
    public String toString() {
        return "ShoppingPropertyBean{" +
                "spuId='" + spuId + '\'' +
                ", skuId='" + skuId + '\'' +
                ", count=" + count +
                '}';
    }
}
