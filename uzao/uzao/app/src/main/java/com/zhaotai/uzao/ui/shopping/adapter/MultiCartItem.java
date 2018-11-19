package com.zhaotai.uzao.ui.shopping.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Time: 2018/3/20
 * Created by LiYou
 * Description :
 */

public class MultiCartItem implements MultiItemEntity {

    public static final int TYPE_NORMAL = 1;//普通商品
    public static final int TYPE_ACTIVITY = 2;//活动商品
    public static final int TYPE_INVALID = 3;//失效商品
    public static final int TYPE_GUESS_TITLE = 4;//猜你喜欢title
    public static final int TYPE_RECOMMEND = 5;//推荐商品
    public static final int TYPE_EMPTY_GOODS = 6;//全部商品空页面
    public static final int TYPE_EMPTY_INVALID_GOODS = 7;//失效商品空页面
    private int itemType;

    public MultiCartItem(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public int groupId;
    public boolean isGroupHeader;
    public boolean isSelect;
    public boolean isGroupSelect;

    public String spuName;
    public String unitPrice;
    public String count;
    public String properties;
    public String spuPic;
    public String lessThanPriceAdd;
    public String lessThanPriceAddY;
    public String spuId;
    public String spuType;
    public String description;
    public String displayPriceY;
    public String cartId;
    public String storeCount;//库存

    //活动
    public String activityIcon;
    public String cut;

    //失效
    public int invalidCount;
    public boolean isInvalidGoods;
}
