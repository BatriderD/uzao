package com.zhaotai.uzao.ui.search.adapter;

import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.ProductOptionBean.FilterBean;


/**
 * Time: 2017/11/30
 * Created by LiYou
 * Description : 筛选 类型
 */

public class FilterTypeAdapter extends BaseQuickAdapter<FilterBean, BaseViewHolder> {

    public FilterTypeAdapter() {
        super(R.layout.item_filter);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilterBean item) {
        helper.setText(R.id.tv_name, getTypeName(item.name));
        if (item.isSelected) {
            helper.setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.red))
                    .setBackgroundRes(R.id.tv_name, R.drawable.shape_bg_tag);
        } else {
            helper.setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.black))
                    .setBackgroundRes(R.id.tv_name, R.drawable.shape_bg_tag_grey);
        }
    }

    private String getTypeName(String type) {
        switch (type) {
            case "template":
                return "模板商品";
            case "mall":
                return "商品";
            case "design":
                return "定制";
            default:
                return "全部";
        }
    }
}
