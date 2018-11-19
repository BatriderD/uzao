package com.zhaotai.uzao.bean;

import com.zhaotai.uzao.bean.post.DesignInfo;
import com.zhaotai.uzao.bean.post.TemplateDesignInfo;

import java.io.Serializable;

/**
 * Time: 2017/5/26
 * Created by LiYou
 * Description : 购物车里面商品
 */

public class ShoppingGoodsBean implements Serializable {

    public String count;//数量
    public String designerId;
    public String preferentialWay;//优惠方式
    public String properties;//规格
    public String recDate;//加入时间
    public String sequenceNBR;////购物车ID
    public String skuId;//商品skuID
    public String spuType;//商品spuID
    public String spuId;//商品类型
    public String sampleSpuId;//载体
    public String spuName;//商品名称
    public String spuPic;//商品组图
    public String unitPrice;//商品单价
    public String storeCount;//库存
    public String userId;
    public boolean invalid;
    public String lessThanPriceAdd;//降价
    public String lessThanPriceAddY;
    public TemplateDesignInfo graphicDesignModel;
    public DesignInfo designSpuModel;
    public String isUseTemplateSpu;

    /**
     * 是否被选中
     */
    private boolean isChildSelected;

    public boolean isChildSelected() {
        return isChildSelected;
    }

    public void setIsChildSelected(boolean childSelected) {
        isChildSelected = childSelected;
    }
}
