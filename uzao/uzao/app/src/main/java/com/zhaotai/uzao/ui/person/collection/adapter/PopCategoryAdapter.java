package com.zhaotai.uzao.ui.person.collection.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.ui.person.collection.model.CategoryCodeBean;
import com.zhaotai.uzao.utils.StringUtil;

/**
 * Time: 2017/11/24
 * Created by LiYou
 * Description :
 */

public class PopCategoryAdapter extends BaseQuickAdapter<CategoryCodeBean, BaseViewHolder> {

    public PopCategoryAdapter() {
        super(R.layout.item_pop_collect_product_category);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryCodeBean item) {
        if (StringUtil.isEmpty(item.CATEGORY_NAME)) {
            helper.setText(R.id.tv_pop_category_name, item.name);
        } else {
            helper.setText(R.id.tv_pop_category_name, item.CATEGORY_NAME);
        }

        if (item.isSelect) {
            helper.setTextColor(R.id.tv_pop_category_name, Color.WHITE)
                    .setBackgroundColor(R.id.tv_pop_category_name, ContextCompat.getColor(mContext,R.color.red));
        } else {
            helper.setTextColor(R.id.tv_pop_category_name,ContextCompat.getColor(mContext,R.color.black))
                    .setBackgroundRes(R.id.tv_pop_category_name,R.drawable.shape_collect_tab_bg_rect_line);
        }
    }
}
