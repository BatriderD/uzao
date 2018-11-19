package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Time: 2018/8/6 0006
 * Created by LiYou
 * Description :
 */
public class CategoryMultiRightBean implements MultiItemEntity {

    public static final int TYPE_SECTION_HEADER = 1;
    public static final int TYPE_ITEM_IMAGE = 2;

    public CategoryMultiRightBean(int itemType) {
        this.itemType = itemType;
    }

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public String categoryName;
    public String categoryCode;
    public String icon;
    public String associateType;
    public String associateData;
}
