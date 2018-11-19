package com.zhaotai.uzao.ui.search.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.AssociateBean;

/**
 * Time: 2018/1/11
 * Created by LiYou
 * Description : 联想
 */

public class AssociateAdapter extends BaseQuickAdapter<AssociateBean, BaseViewHolder> {

    public AssociateAdapter() {
        super(R.layout.item_associate);
    }

    @Override
    protected void convert(BaseViewHolder helper, AssociateBean item) {
        helper.setText(R.id.tv_associate, item.word);
    }
}
