package com.zhaotai.uzao.ui.brand.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.BrandBean;

import java.util.List;

/**
 * Time: 2018/1/29
 * Created by LiYou
 * Description :品牌列表
 */

public class BrandListAdapter extends BaseQuickAdapter<BrandBean, BaseViewHolder> {

    public BrandListAdapter(@Nullable List<BrandBean> data) {
        super(R.layout.item_brand_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BrandBean item) {
        helper.setText(R.id.tv_brand_name, item.brandName);
    }
}
