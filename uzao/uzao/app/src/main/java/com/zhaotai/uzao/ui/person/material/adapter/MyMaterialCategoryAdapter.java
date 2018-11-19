package com.zhaotai.uzao.ui.person.material.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.MyBoughtMaterialCategoryBean;

/**
 * description: 我的素材 分类管理类
 * author : ZP
 * date: 2018/1/9 0009.
 */

public class MyMaterialCategoryAdapter extends BaseQuickAdapter<MyBoughtMaterialCategoryBean, BaseViewHolder> {
    private int selected;

    public MyMaterialCategoryAdapter() {
        super(R.layout.item_my_material_category);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyBoughtMaterialCategoryBean item) {
        helper.setText(R.id.tv_item_my_material_category, item.getName());
        helper.getView(R.id.tv_item_my_material_category).setSelected(item.isSelected());
    }


    public void setselected(int position) {
        if (selected >= 0 && getData().size() > selected) {
            getItem(selected).setSelected(false);
            notifyItemChanged(selected);
        }
        getItem(position).setSelected(true);
        notifyItemChanged(position);
        selected = position;
    }

    public int getSelected() {
        return selected;
    }
}
