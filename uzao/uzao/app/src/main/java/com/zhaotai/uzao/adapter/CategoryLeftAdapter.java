package com.zhaotai.uzao.adapter;


import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.MainTabBean;


/**
 * Time: 2017/5/12
 * Created by LiYou
 * Description : 分类 左边
 */

public class CategoryLeftAdapter extends BaseQuickAdapter<MainTabBean, BaseViewHolder> {

    public CategoryLeftAdapter() {
        super(R.layout.item_category_left);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainTabBean item) {
        helper.setText(R.id.category_left_text, item.navigateName);
        if (item.isChecked) {
            //选中状态
            helper.setTextColor(R.id.category_left_text, ContextCompat.getColor(mContext, R.color.red));
        } else {
            //未选中状态
            helper.setTextColor(R.id.category_left_text, ContextCompat.getColor(mContext, R.color.black));
        }
    }
}
