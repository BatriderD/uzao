package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Time: 2017/7/21
 * Created by LiYou
 * Description :
 */

public class SectionGoodsBean extends SectionEntity<ShoppingGoodsBean> {

    public String icon;

    public SectionGoodsBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SectionGoodsBean(boolean isHeader, String header, String icon) {
        super(isHeader, header);
        this.icon = icon;
    }

    public SectionGoodsBean(ShoppingGoodsBean shoppingGoodsBean) {
        super(shoppingGoodsBean);
    }
}
