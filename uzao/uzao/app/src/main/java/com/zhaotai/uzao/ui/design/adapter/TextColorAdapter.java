package com.zhaotai.uzao.ui.design.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;

/**
 * 颜色选择器adapter
 */

public class TextColorAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public TextColorAdapter() {
        super(R.layout.item_text_color);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivColor = helper.getView(R.id.iv_item_text_color);
        ivColor.setBackgroundColor(Color.parseColor(item));
    }
}
