package com.zhaotai.uzao.ui.design.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.MKUCarrierBean;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class SelectedAreaAdapter extends BaseQuickAdapter<MKUCarrierBean.MkusBean.MaskGroupsBean, BaseViewHolder> {
    public SelectedAreaAdapter() {
        super(R.layout.item_rc_selected_area);
    }

    @Override
    protected void convert(BaseViewHolder helper, MKUCarrierBean.MkusBean.MaskGroupsBean item) {
        helper.setText(R.id.tv_content, item.getAspectName());
    }
}
