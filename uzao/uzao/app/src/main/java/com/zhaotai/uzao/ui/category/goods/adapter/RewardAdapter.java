package com.zhaotai.uzao.ui.category.goods.adapter;

import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.DictionaryBean;

import java.util.List;

/**
 * Time: 2018/1/10
 * Created by LiYou
 * Description : 打赏金额
 */

public class RewardAdapter extends BaseQuickAdapter<DictionaryBean, BaseViewHolder> {

    public RewardAdapter(List<DictionaryBean> data) {
        super(R.layout.item_filter, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DictionaryBean item) {
        if (item.isSelect) {
            helper.setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.red))
                    .setBackgroundRes(R.id.tv_name, R.drawable.shape_bg_tag)
                    .setText(R.id.tv_name, item.entryValue);
        } else {
            helper.setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.black))
                    .setBackgroundRes(R.id.tv_name, R.drawable.shape_bg_tag_grey)
                    .setText(R.id.tv_name, item.entryValue);
        }
    }
}
