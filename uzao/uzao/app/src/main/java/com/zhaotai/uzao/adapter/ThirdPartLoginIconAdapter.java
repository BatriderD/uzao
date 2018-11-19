package com.zhaotai.uzao.adapter;

import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.ThirdPartLoginIconBean;

/**
 * Time: 2017/6/5
 * Created by zp
 * Description :  三方登录 图标
 */

public class ThirdPartLoginIconAdapter extends BaseQuickAdapter<ThirdPartLoginIconBean, BaseViewHolder> {

    public ThirdPartLoginIconAdapter() {
        super(R.layout.item_third_part_login_icon);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThirdPartLoginIconBean item) {
        helper.setImageDrawable(R.id.iv_third_part_icon, ContextCompat.getDrawable(mContext, item.getIcon()));
        helper.setText(R.id.tv_third_part_name, item.getName());
    }
}
