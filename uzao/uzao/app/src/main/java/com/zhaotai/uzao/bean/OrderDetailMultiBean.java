package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Time: 2018/7/17 0017
 * Created by LiYou
 * Description : 订单详情多布局实体类
 */
public class OrderDetailMultiBean implements MultiItemEntity {

    public static final int TYPE_SECTION = 1;
    public static final int TYPE_SECTION_VIEW = 2;
    public static final int TYPE_SECTION_MATERIAL = 3;
    public static final int TYPE_SECTION_WAIT_RECEIVE = 4;//待收货的section
    public static final int TYPE_ITEM = 5;
    public static final int TYPE_BOTTOM = 6;


    public OrderDetailMultiBean(int itemType) {
        this.itemType = itemType;
    }

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public String packageOrderNo;//包裹订单Id
    public String packageNum;//包裹
    public String state;//订单状态

    public boolean isMaterial;//是否素材
    public String isCommend;//是否评论

    public String pic; // 图片
    public String name;   //名字
    public String price; //价格
    public String category;//规格
    public String time;//授权时间

    public String count;//数量
    public String remainingReceiveTime;//倒计时

    public int orderId;

}
