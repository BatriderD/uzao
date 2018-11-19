package com.zhaotai.uzao.ui.design.material.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.utils.ScreenUtils;

/**
 * description: 素材列表tab 的Adapter
 * author : ZP
 * date: 2018/1/8 0008.
 */

public class MaterialListTabAdapter extends BaseQuickAdapter<CategoryBean, BaseViewHolder> {
    private int selected = -1;
    public MaterialListTabAdapter() {
        super(R.layout.item_catch_tab);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryBean item) {
        View rootView = helper.getView(R.id.rl_item_root);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) rootView.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(mContext) / 5;
        rootView.setLayoutParams(layoutParams);
        
        helper.setText(R.id.tv_tab_name, item.categoryName);
        helper.getView(R.id.v_tabL_line).setVisibility(item.selected ? View.VISIBLE : View.INVISIBLE);
        helper.getView(R.id.tv_tab_name).setSelected(item.selected);
    }

    /**
     * 设置标签被选中
     * @param position 选中位置
     */
    public void setSelected(int position) {
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
