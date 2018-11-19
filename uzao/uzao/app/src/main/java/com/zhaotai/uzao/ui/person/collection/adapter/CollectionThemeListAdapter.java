package com.zhaotai.uzao.ui.person.collection.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description :
 */

public class CollectionThemeListAdapter extends BaseRecyclerAdapter<ThemeBean, BaseViewHolder> {

    private boolean selectState;

    public CollectionThemeListAdapter() {
        super(R.layout.item_collect_theme_list);
        this.selectState = false;
    }

    @Override
    protected void convert(BaseViewHolder helper, ThemeBean item) {
        helper.setText(R.id.iv_theme_name, item.name);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.cover, (ImageView) helper.getView(R.id.iv_theme_image));

        if ("I".equals(item.recStatus)) {
            helper.setVisible(R.id.iv_theme_failure, true);
        } else {
            helper.setVisible(R.id.iv_theme_failure, false);
        }

        if (selectState) {
            //编辑状态
            helper.setVisible(R.id.iv_collect_theme_select, true);
            if (item.isSelected()) {
                helper.setImageResource(R.id.iv_collect_theme_select, R.drawable.icon_circle_selected);
            } else {
                helper.setImageResource(R.id.iv_collect_theme_select, R.drawable.icon_circle_unselected);
            }
        } else {
            helper.setVisible(R.id.iv_collect_theme_select, false);
        }
    }

    public boolean getSelectState() {
        return this.selectState;
    }

    public void setSelectState(boolean selectState) {
        this.selectState = selectState;
        notifyDataSetChanged();
    }
}
