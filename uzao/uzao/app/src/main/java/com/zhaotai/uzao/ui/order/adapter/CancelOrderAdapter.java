package com.zhaotai.uzao.ui.order.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.DictionaryBean;

/**
 * Time: 2018/1/17
 * Created by LiYou
 * Description :
 */

public class CancelOrderAdapter extends BaseQuickAdapter<DictionaryBean, BaseViewHolder> {

    public CancelOrderAdapter() {
        super(R.layout.item_cancel_order);
    }

    @Override
    protected void convert(BaseViewHolder helper, DictionaryBean item) {
        helper.setText(R.id.tv_cancel_order, item.entryValue);
    }
}
