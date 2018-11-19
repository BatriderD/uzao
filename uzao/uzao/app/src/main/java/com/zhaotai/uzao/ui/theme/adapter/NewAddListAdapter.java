package com.zhaotai.uzao.ui.theme.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.NewAddListBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * description: 新增主题列表的adapter
 * author : ZP
 * date: 2018/1/24 0024.
 */

public class NewAddListAdapter extends BaseRecyclerAdapter<NewAddListBean, BaseViewHolder> {
    public NewAddListAdapter() {
        super(R.layout.item_add_theme_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewAddListBean item) {

        helper.setText(R.id.tv_name, item.entityName);
        if (item.entityPrice == 0) {
            helper.setText(R.id.tv_price, "免费");
        } else {
            helper.setText(R.id.tv_price, "¥" + item.entityPriceY);
        }

        ImageView ivPic = helper.getView(R.id.iv_pic);

        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.entityPic, ivPic);
    }
}
