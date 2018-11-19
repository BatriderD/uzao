package com.zhaotai.uzao.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;


/**
 * Time: 2017/5/12
 * Created by LiYou
 * Description :
 */

public class CategoryRightAdapter extends BaseQuickAdapter<CategoryBean, BaseViewHolder> {
    public CategoryRightAdapter() {
        super(R.layout.item_category_right);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryBean item) {
        helper.setText(R.id.category_right_text, item.categoryName);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.icon, (ImageView) helper.getView(R.id.iv_category_right_img));
    }
}
