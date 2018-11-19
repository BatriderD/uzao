package com.zhaotai.uzao.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Time: 2018/3/23
 * Created by LiYou
 * Description :
 */

public class MultiMaterialCategoryBean implements MultiItemEntity {

    public static final int TYPE_CATEGORY = 1;//分类
    public static final int TYPE_RECOMMEND_MATERIAL_TITLE = 2;//推荐素材标题
    public static final int TYPE_RECOMMEND_MATERIAL = 3;//推荐素材
    public static final int TYPE_LINE = 4;//分割线

    public MultiMaterialCategoryBean(int itemType) {
        this.itemType = itemType;
    }

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public String categoryCode;
    public String categoryName;
    public List<CategoryBean> children;

    public String sequenceNBR;
    public String thumbnail;
    public String sourceMaterialName;

}
