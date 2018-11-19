package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Time: 2018/3/22
 * Created by LiYou
 * Description :
 */

public class MultiMainBean implements MultiItemEntity {
    public static final int TYPE_BANNER = 1;//轮播图
    public static final int TYPE_INSPIRATION_GALLERY_TITLE = 2;//灵感画廊标题
    public static final int TYPE_INSPIRATION_GALLERY = 3;//灵感画廊
    public static final int TYPE_POPULAR_DESIGN_TITLE = 4;//人气设计标题
    public static final int TYPE_POPULAR_DESIGN = 5;//人气设计
    public static final int TYPE_RECOMMEND_SPU_TITLE = 6;//推荐商品标题
    public static final int TYPE_RECOMMEND_SPU = 7;//推荐商品
    public static final int TYPE_MORE = 8;//更多
    public static final int TYPE_RECOMMEND_MATERIAL_TITLE = 9;//推荐素材标题
    public static final int TYPE_RECOMMEND_MATERIAL = 10;//推荐素材
    public static final int TYPE_BRAND_TITLE = 11;//合作品牌标题
    public static final int TYPE_BRAND_THEME = 12;//品牌主题
    public static final int TYPE_BRAND = 13;//品牌
    public static final int TYPE_ABOUT_US_TITLE = 14;//了解我们标题
    public static final int TYPE_ABOUT_US = 15;//了解我们


    public MultiMainBean(int itemType) {
        this.itemType = itemType;
    }

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public List<DynamicValuesBean> values;
    public DynamicValuesBean value;
    public String referType;
    public String groupType;
    public int inSidePos;

}
