package com.zhaotai.uzao.ui.person.address.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.RegionBean;

/**
 * description:
 * author : zp
 * date: 2017/7/14
 */

public class RegionAdapter extends BaseQuickAdapter<RegionBean, BaseViewHolder> {

    public RegionAdapter() {
        super(R.layout.item_region);
    }

    @Override
    protected void convert(BaseViewHolder helper, RegionBean item) {
        helper.setText(R.id.tv_region_name, item.locationName);
    }
}