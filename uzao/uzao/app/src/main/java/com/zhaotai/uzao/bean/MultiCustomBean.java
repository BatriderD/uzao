package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Time: 2018/3/23
 * Created by LiYou
 * Description :
 */

public class MultiCustomBean implements MultiItemEntity {

    public static final int TYPE_CATEGORY = 1;//分类
    public static final int TYPE_RECOMMEND_SPU_TITLE = 2;//推荐商品标题
    public static final int TYPE_RECOMMEND_SPU = 3;//推荐商品
    public static final int TYPE_LINE = 4;//分割线

    public MultiCustomBean(int itemType) {
        this.itemType = itemType;
    }

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public boolean isSelect;

    public String navigateCode;
    public String navigateName;
    public String associateType;
    public String associateData;
    public String icon;

    public String spuName;
    public String thumbnail;
    public String sequenceNBR;
}
