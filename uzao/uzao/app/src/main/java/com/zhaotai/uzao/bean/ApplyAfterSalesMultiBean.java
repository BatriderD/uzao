package com.zhaotai.uzao.bean;

import android.net.Uri;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Time: 2018/7/31 0031
 * Created by LiYou
 * Description : 申请售后
 */
public class ApplyAfterSalesMultiBean implements MultiItemEntity {

    public static final int TYPE_SECTION_ORDER_NUM = 1;//订单编号
    public static final int TYPE_SECTION_VIEW = 2;//分割
    public static final int TYPE_ITEM_GOODS_SELECT = 3;//选择商品列表
    public static final int TYPE_ITEM_APPLY = 4;//申请内容
    public static final int TYPE_ITEM_IMAGE = 5;//图片

    public static final String APPLY_TYPE_REPLACEMENT = "Replacement";//换货
    public static final String APPLY_TYPE_RETURNED = "Returned";//退款

    public ApplyAfterSalesMultiBean(int itemType) {
        this.itemType = itemType;
    }

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public String orderId;
    public String packageNum;
    public String applyType ;//申请类型

    public String reason;
    public String pic;
    public String category;
    public String name;
    public String price;
    public String count;
    public String sequenceNBR;
    public int availableSkuCount;
    public String imageFile;
    public Uri imageUri;

    public boolean isSelected;
    public boolean isAddImage;
    public boolean isImage;

}
