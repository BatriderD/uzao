package com.zhaotai.uzao.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.CategoryMultiRightBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.List;


/**
 * Time: 2018/8/6
 * Created by LiYou
 * Description :
 */

public class CategoryRightSectionAdapter extends BaseMultiItemQuickAdapter<CategoryMultiRightBean, BaseViewHolder> {

    public CategoryRightSectionAdapter(List<CategoryMultiRightBean> data) {
        super(data);
        addItemType(CategoryMultiRightBean.TYPE_SECTION_HEADER, R.layout.item_category_right_multi_section_header);
        addItemType(CategoryMultiRightBean.TYPE_ITEM_IMAGE, R.layout.item_category_right);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryMultiRightBean item) {
        switch (item.getItemType()) {
            case CategoryMultiRightBean.TYPE_SECTION_HEADER:
                helper.setText(R.id.tv_category_right_header_name, item.categoryName);
                break;
            case CategoryMultiRightBean.TYPE_ITEM_IMAGE:
                helper.setText(R.id.category_right_text, item.categoryName);
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.icon, (ImageView) helper.getView(R.id.iv_category_right_img));
                break;
        }
    }
}
