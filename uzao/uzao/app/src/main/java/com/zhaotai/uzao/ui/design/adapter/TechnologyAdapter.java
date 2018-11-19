package com.zhaotai.uzao.ui.design.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.TechnologyBean;

/**
 * description: 工艺列表选择adapter
 * author : ZP
 * date: 2017/12/12 0012.
 */

public class TechnologyAdapter extends BaseQuickAdapter<TechnologyBean, BaseViewHolder> {
    private int selected = 0;

    public TechnologyAdapter() {
        super(R.layout.item_technology);
    }

    @Override
    protected void convert(BaseViewHolder helper, TechnologyBean item) {
        helper.getView(R.id.iv_item_technology).setSelected(item.selected);
        helper.getView(R.id.ll_item_technology).setSelected(item.selected);
        helper.getView(R.id.item_split_line).setVisibility(item.selected ? View.GONE : View.VISIBLE);
         helper.setText(R.id.tv_item_technology, item.getCraftName());
    }


    public void setselected(int position) {
        if (selected >= 0 && getData().size() > selected) {
            getItem(selected).selected = false;
        }
        getItem(position).selected = true;
        notifyItemChanged(selected);
        notifyItemChanged(position);
        selected = position;
    }

    public int getSelected() {
        return selected;
    }
}
