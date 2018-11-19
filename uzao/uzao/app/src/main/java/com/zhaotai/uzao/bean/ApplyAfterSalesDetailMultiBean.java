package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Time: 2018/7/31 0031
 * Created by LiYou
 * Description : 申请售后详情实体类
 */
public class ApplyAfterSalesDetailMultiBean implements MultiItemEntity {

    public static final int TYPE_SECTION_ORDER_NUM = 1;//订单编号
    public static final int TYPE_ITEM_GOODS = 2;//选择商品列表
    public static final int TYPE_ITEM_APPLY = 3;//申请内容
    public static final int TYPE_ITEM_SECTION_IMAGE = 4;//图片
    public static final int TYPE_ITEM_IMAGE = 5;//图片
    public static final int TYPE_ITEM_SECTION_TRANSPORT = 7;//物流section
    public static final int TYPE_ITEM_TRANSPORT = 8;//物流信息
    public static final int TYPE_ITEM_REJECT_REASON = 9;//售后失败原因


    public ApplyAfterSalesDetailMultiBean(int itemType) {
        this.itemType = itemType;
    }

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public String orderId;
    public String packageNum;
    public String applyType;//申请类型
    public String reason;

    public String pic;
    public List<String> picList;
    public int picPosition;
    public String category;
    public String name;
    public String price;
    public String count;
    public String imageFile;
    public String transportName;
    public String transportNo;
    public String rejectReason;//售后失败原因
    public boolean platformTransport;//是否有平台物流信息
    public String sectionTransportName;


}
