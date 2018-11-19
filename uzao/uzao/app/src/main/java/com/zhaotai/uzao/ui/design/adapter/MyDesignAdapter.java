package com.zhaotai.uzao.ui.design.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.DesignBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.TimeUtils;

/**
 * Time: 2017/9/4
 * Created by LiYou
 * Description : 设计师主页 设计列表
 */

public class MyDesignAdapter extends BaseRecyclerAdapter<DesignBean, BaseViewHolder> {

    private boolean selectState;

    public MyDesignAdapter() {
        super(R.layout.item_my_design);
        this.selectState = false;
    }

    @Override
    protected void convert(BaseViewHolder helper, DesignBean item) {
        helper.setText(R.id.tv_design_name, "源自:" + item.designName)
                .setText(R.id.tv_design_time, "编辑日期:    " + TimeUtils.dateFormatToYear_Month_Day(Long.parseLong(item.createTime)))
                .addOnClickListener(R.id.tv_to_design)
                .addOnClickListener(R.id.iv_design_image);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.thmbnail), (ImageView) helper.getView(R.id.iv_design_image));


        if (selectState) {
            //编辑状态
            helper.setVisible(R.id.ll_my_design_select, true);
            if (item.isSelected) {
                helper.setImageResource(R.id.iv_my_design_select, R.drawable.icon_circle_selected);
            } else {
                helper.setImageResource(R.id.iv_my_design_select, R.drawable.icon_circle_unselected);
            }
            helper.addOnClickListener(R.id.ll_my_design_select);
        } else {
            helper.setVisible(R.id.ll_my_design_select, false);
        }
    }

    public void setSelectState(boolean selectState) {
        this.selectState = selectState;
        notifyDataSetChanged();
    }
}
