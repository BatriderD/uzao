package com.zhaotai.uzao.ui.theme.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.StringUtil;

/**
 * description: 主题设置页面模块
 * author : ZP
 * date: 2018/1/22 0022.
 */

public class ThemeSettingModuleAdapter extends BaseQuickAdapter<ThemeModuleBean.ThemeContentModel, BaseViewHolder> {
    private static final String TYPE_SPU = "spu";
    private static final String TYPE_MATERIAL = "material";


    public ThemeSettingModuleAdapter() {
        super(R.layout.item_theme_module);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThemeModuleBean.ThemeContentModel item) {
//        spu  material
        String entityType = item.getEntityType();
        if (StringUtil.isEmpty(entityType)) {
            TextView tvName = helper.getView(R.id.tv_module_name);
            ImageView ivPic = helper.getView(R.id.iv_module_pic);
            tvName.setSelected(false);
            helper.setText(R.id.tv_module_name, "添加");
            Glide.with(mContext)
                    .load(R.drawable.icon_add_module)
                    .into(ivPic);
        } else {
            TextView tvName = helper.getView(R.id.tv_module_name);
            ImageView ivPic = helper.getView(R.id.iv_module_pic);
            tvName.setSelected(false);
            helper.setText(R.id.tv_module_name, item.getEntityName());
            //设置图片
            String picUrl;
            if (TYPE_MATERIAL.equals(entityType)) {
                picUrl = ApiConstants.UZAOCHINA_IMAGE_HOST + item.getEntityPic();
            } else {
                picUrl = ApiConstants.UZAOCHINA_IMAGE_HOST + item.getEntityPic();
            }
            GlideLoadImageUtil.load(mContext,picUrl,ivPic);
        }
    }
}
