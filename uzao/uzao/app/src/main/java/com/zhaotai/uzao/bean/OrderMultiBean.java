package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Time: 2018/7/18 0018
 * Created by LiYou
 * Description :
 */
public class OrderMultiBean implements MultiItemEntity {

    public static final int TYPE_SECTION_ORDER_NUM = 1;//订单编号
    public static final int TYPE_SECTION_WAIT_DELIVERY = 2;//待发货表头
    public static final int TYPE_SECTION_WAIT_PAY = 3;//待付款表头
    public static final int TYPE_SECTION_PACKAGE = 4;//分包表头
    public static final int TYPE_ITEM_GOODS_VERTICAL = 5;//竖向商品列表
    public static final int TYPE_ITEM_GOODS_HORIZONTAL = 6;//横向商品列表
    public static final int TYPE_ITEM_GOODS_SINGLE_WITH_BOTTOM = 7;//商品列表带button
    public static final int TYPE_ITEM_GOODS_HORIZONTAL_WITH_BOTTOM = 8;//商品列表带button
    public static final int TYPE_ITEM_GOODS_SECTION_MATERIAL = 9;//素材
    public static final int TYPE_ITEM_MATERIAL = 10;//素材
    public static final int TYPE_ITEM_BOTTOM = 11;//底部
    public static final int TYPE_ITEM_BOTTOM_PRICE = 12;//底部-带订单金额


    public OrderMultiBean(int itemType) {
        this.itemType = itemType;
    }

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public String orderNo;//订单编号
    public String orderStatus;
    public String orderType;
    public String packageStatus;
    public String packageOrderNo;//包裹订单Id
    public String packageNum;
    public String orderPrice;

    public String pic;
    public String pic1;
    public String pic2;
    public String pic3;
    public String horizontalSize;
    public String name;
    public String category;
    public String price;
    public String count;
    public String isCommend;
    public String time;//素材有效时间
    public String materialId;//素材有效时间

    public boolean isLast;
    public boolean isMaterial;
    public boolean isSelected;
}
