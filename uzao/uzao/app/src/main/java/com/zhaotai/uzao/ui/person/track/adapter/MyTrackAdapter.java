package com.zhaotai.uzao.ui.person.track.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseEmptySectionQuickAdapter;
import com.zhaotai.uzao.bean.MyTrackBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;

import java.util.List;

/**
 * description: 我的足迹页面
 * author : ZP
 * date: 2017/12/2 0002.
 */

public class MyTrackAdapter extends BaseEmptySectionQuickAdapter<MyTrackBean, BaseViewHolder> {

    // true 编辑状态  false 未编辑
    private boolean manageState;

    public MyTrackAdapter(List data) {
        super(R.layout.item_my_track_content, R.layout.item_my_track_head, data);
        manageState = false;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyTrackBean item) {

        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.getFootprintModel().getThumbnail(), ImageSizeUtil.ImageSize.X200), (ImageView) helper.getView(R.id.iv_my_track_content));
        helper.setText(R.id.tv_my_track_name, item.getFootprintModel().getEntityName());
        if (item.getFootprintModel().getPrice() == 0) {
            helper.getView(R.id.tv_my_track_price_icon).setVisibility(View.INVISIBLE);
            helper.setText(R.id.tv_my_track_price, "免费");
        } else {
            helper.getView(R.id.tv_my_track_price_icon).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_my_track_price, String.valueOf(item.getFootprintModel().getPriceY()));
        }



        if (manageState) {
            helper.setVisible(R.id.ll_my_track_selected, true);
            helper.getView(R.id.iv_my_track_selected).setSelected(item.getFootprintModel().isSelected());
        } else {
            helper.setVisible(R.id.ll_my_track_selected, false);
        }
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final MyTrackBean item) {
        helper.setText(R.id.tv_my_track_date, item.getDate());
    }

    public void setManageState(boolean state) {
        if (state) {
            setSelected(!state);
        }
        manageState = state;
        notifyDataSetChanged();
    }

    public boolean getManageState() {
        return manageState;
    }

    /**
     * 设置选中状态
     */
    public void setSelected(boolean selected) {
        List<MyTrackBean> data = getData();
        for (MyTrackBean track : data) {
            if (!track.isHeader) {
                track.getFootprintModel().setSelected(selected);
            }
        }
    }
}
