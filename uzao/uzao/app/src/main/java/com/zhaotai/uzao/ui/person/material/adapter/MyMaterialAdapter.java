package com.zhaotai.uzao.ui.person.material.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.StringUtil;

/**
 * description: 我的素材 已购素材
 * author : ZP
 * date: 2018/1/9 0009.
 */

public class MyMaterialAdapter extends BaseRecyclerAdapter<MyMaterialBean, BaseViewHolder> {
    public MyMaterialAdapter() {
        super(R.layout.item_my_material);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyMaterialBean item) {
        helper.setText(R.id.tv_my_material_title, item.getSourceMaterialName());
        ImageView ivPic = helper.getView(R.id.iv_my_material_pic);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.getThumbnail(), ivPic);
        String designerName = "";
        if (item.getAssignDesigner() != null) {
            designerName = item.getAssignDesigner().getNickName();
        }else if(item.getDesignInfo() != null){
            designerName = item.getDesignInfo().getNickName();
        }else {
            designerName = GlobalVariable.UZAO_MATERIAL_NAME;
        }
        helper.setText(R.id.tv_my_material_author, "设计师：" + designerName);

        helper.setText(R.id.tv_my_material_status, "free".equals(item.getSaleMode()) ? "已获取" : "已购买");
        String lastTime;
        if ("forever".equals(item.getPeriodUnit())) {
            lastTime = "永久";
        } else {
            lastTime = item.getRemainTimeFormat();
        }
        String remainTimeFormat = item.getRemainTimeFormat();
        helper.setText(R.id.tv_my_material_time_left, "剩余时长：" + lastTime);

        helper.getView(R.id.rl_my_material_bar).setVisibility("已过期".equals(remainTimeFormat) ? View.VISIBLE : View.GONE);

        helper.addOnClickListener(R.id.tv_material_del)
                .addOnClickListener(R.id.tv_material_buy_again);
    }
}
