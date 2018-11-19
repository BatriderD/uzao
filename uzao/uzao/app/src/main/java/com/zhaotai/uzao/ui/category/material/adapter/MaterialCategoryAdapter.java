package com.zhaotai.uzao.ui.category.material.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.MultiMaterialCategoryBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;

import java.util.List;

/**
 * Time: 2018/3/23
 * Created by LiYou
 * Description : 素材分类
 */

public class MaterialCategoryAdapter extends BaseMultiItemQuickAdapter<MultiMaterialCategoryBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MaterialCategoryAdapter(List<MultiMaterialCategoryBean> data) {
        super(data);
        addItemType(MultiMaterialCategoryBean.TYPE_CATEGORY, R.layout.item_main_custom_category);
        addItemType(MultiMaterialCategoryBean.TYPE_LINE, R.layout.item_main_line);
        addItemType(MultiMaterialCategoryBean.TYPE_RECOMMEND_MATERIAL_TITLE, R.layout.item_main_child_title);
        addItemType(MultiMaterialCategoryBean.TYPE_RECOMMEND_MATERIAL, R.layout.item_main_custom_recommend_spu);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiMaterialCategoryBean item) {
        switch (helper.getItemViewType()) {
            case MultiMaterialCategoryBean.TYPE_CATEGORY:
                initCategory(helper, item);
                break;
            case MultiMaterialCategoryBean.TYPE_RECOMMEND_MATERIAL_TITLE:
                helper.setText(R.id.tv_main_child_title, "推荐素材");
                break;
            case MultiMaterialCategoryBean.TYPE_RECOMMEND_MATERIAL:
                initRecommendMaterial(helper, item);
                break;
        }
    }

    /**
     * 推荐素材
     */
    private void initRecommendMaterial(BaseViewHolder helper, MultiMaterialCategoryBean item) {
        helper.setText(R.id.tv_main_custom_recommend_spu_name, item.sourceMaterialName);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.thumbnail),
                (ImageView) helper.getView(R.id.iv_main_custom_recommend_spu_pic));

        helper.addOnClickListener(R.id.iv_main_custom_recommend_spu_pic);
    }

    /**
     * 勾选 分类
     */
    private void initCategory(BaseViewHolder helper, MultiMaterialCategoryBean item) {
        helper.setText(R.id.iv_main_custom_category_name, item.categoryName);
        switch (helper.getLayoutPosition()) {
            case 0:
                helper.setImageResource(R.id.iv_main_custom_category_pic, R.drawable.icon_material_category_1);
                break;
            case 1:
                helper.setImageResource(R.id.iv_main_custom_category_pic, R.drawable.icon_material_category_2);
                break;
            case 2:
                helper.setImageResource(R.id.iv_main_custom_category_pic, R.drawable.icon_material_category_3);
                break;
            case 3:
                helper.setImageResource(R.id.iv_main_custom_category_pic, R.drawable.icon_material_category_4);
                break;
            case 4:
                helper.setImageResource(R.id.iv_main_custom_category_pic, R.drawable.icon_material_category_5);
                break;
            case 5:
                helper.setImageResource(R.id.iv_main_custom_category_pic, R.drawable.icon_material_category_6);
                break;
            default:
                helper.setImageResource(R.id.iv_main_custom_category_pic, R.drawable.icon_material_category_7);
                break;
        }
        helper.addOnClickListener(R.id.iv_main_custom_category_pic);
    }
}
