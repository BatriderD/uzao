package com.zhaotai.uzao.ui.main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;

/**
 * description: 首页的全部菜单 Adapter
 * author : ZP
 * date: 2018/3/26 0026.
 */

public class MainItemSelectAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public String getmItem() {
        return mItem;
    }

    public void setmItem(String mItem) {
        this.mItem = mItem;
        notifyDataSetChanged();
    }

    private String mItem = "";

    public MainItemSelectAdapter() {
        super(R.layout.item_main_item_select);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_item_tag, item);
        helper.getView(R.id.tv_item_tag).setSelected(mItem.equals(item));
    }
}
