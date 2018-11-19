package com.zhaotai.uzao.ui.design.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.DesignBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.TimeUtils;

/**
 * Time: 2017/9/4
 * Created by LiYou
 * Description : 设计师主页 设计列表
 */

public class DesignAdapter extends BaseQuickAdapter<DesignBean, BaseViewHolder> {

    public DesignAdapter() {
        super(R.layout.item_design);
    }

    @Override
    protected void convert(BaseViewHolder helper, DesignBean item) {
        helper.setText(R.id.tv_design_name, item.designName)
                .setText(R.id.tv_design_time, mContext.getString(R.string.publish_time,
                        TimeUtils.dateFormatToYear_Month_Day(Long.parseLong(item.createTime))));
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.thmbnail, (ImageView) helper.getView(R.id.iv_design_image));
    }
}
