package com.zhaotai.uzao.ui.design.catchWord.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.CatchWordTabBean;
import com.zhaotai.uzao.utils.ScreenUtils;

/**
 * description:
 * author : ZP
 * date: 2018/1/8 0008.
 */

public class CatchTabAdapter extends BaseQuickAdapter<CatchWordTabBean, BaseViewHolder> {
    private int selected = -1;
    public CatchTabAdapter() {
        super(R.layout.item_catch_tab);
    }

    @Override
    protected void convert(BaseViewHolder helper, CatchWordTabBean item) {
        View rootView = helper.getView(R.id.rl_item_root);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) rootView.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(mContext) / 5;
        rootView.setLayoutParams(layoutParams);

        helper.setText(R.id.tv_tab_name, item.getCategoryName());
        helper.getView(R.id.v_tabL_line).setVisibility(item.isSelected() ? View.VISIBLE : View.INVISIBLE);
        helper.getView(R.id.tv_tab_name).setSelected(item.isSelected());
    }


    public void setselected(int position) {
        if (selected >= 0 && getData().size() > selected) {
            getItem(selected).setSelected(false);
        }
        getItem(position).setSelected(true);
        notifyItemChanged(selected);
        notifyItemChanged(position);
        selected = position;
    }

    public int getSelected() {
        return selected;
    }

}
