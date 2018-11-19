package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Time: 2017/5/26
 * Created by LiYou
 * Description :
 */

public class ShoppingCartSection extends SectionEntity<ShoppingGoodsBean> {

    public boolean isSelected;

    public ShoppingCartSection(boolean isHeader, String header) {
        super(isHeader, header);
        isSelected = false;
    }

    public ShoppingCartSection(ShoppingGoodsBean shoppingGoodsBean) {
        super(shoppingGoodsBean);
        isSelected = false;
    }
}
