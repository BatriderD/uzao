package com.zhaotai.uzao.ui.theme.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.utils.ScreenUtils;

/**
 * description:
 * author : ZP
 * date: 2018/1/8 0008.
 */

public class MaterialTabAdapter extends BaseQuickAdapter<CategoryBean, BaseViewHolder> {
    private int selected = -1;

    public MaterialTabAdapter() {
        super(R.layout.item_catch_tab);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryBean item) {
        helper.setText(R.id.tv_tab_name, item.categoryName);
        helper.getView(R.id.v_tabL_line).setVisibility(item.isChecked ? View.VISIBLE : View.INVISIBLE);
        helper.getView(R.id.tv_tab_name).setSelected(item.isChecked);
        View rootView = helper.getView(R.id.rl_item_root);
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(mContext)/5;
        rootView.setLayoutParams(layoutParams);
    }

    public void setselected(int position) {
        if (selected >= 0 && getData().size() > selected) {
            getItem(selected).isChecked = false;
        }
        getItem(position).isChecked = true;
        notifyItemChanged(selected);
        notifyItemChanged(position);
        selected = position;
    }

    public int getSelected() {
        return selected;
    }
}
