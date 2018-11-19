package com.zhaotai.uzao.ui.person.collection.adapter;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.StringUtil;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description :
 */

public class CollectionMaterialListAdapter extends BaseRecyclerAdapter<MaterialListBean, BaseViewHolder> {

    private boolean selectState;

    public CollectionMaterialListAdapter() {
        super(R.layout.item_collect_material_list);
        this.selectState = false;
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialListBean item) {
        helper.setText(R.id.iv_product_spu_name, item.sourceMaterialName);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.thumbnail, (ImageView) helper.getView(R.id.iv_product_spu_image));
        switch (item.saleMode) {
            case GlobalVariable.MATERIAL_MODE_CHARGE://收费
                helper.setText(R.id.tv_product_price, "¥" + item.priceY);
                if (item.obtained) {
                    helper.setText(R.id.tv_material_status, "已购买");
                }
                break;
            case GlobalVariable.MATERIAL_MODE_FREE://免费
                helper.setText(R.id.tv_product_price, "免费");
                if (item.obtained) {
                    helper.setText(R.id.tv_material_status, "已获取");
                }
                break;
        }

        if (item.status.equals("published")) {
            helper.setVisible(R.id.view_mask, false)
                    .setTextColor(R.id.iv_product_spu_name, ContextCompat.getColor(mContext, R.color.black))
                    .setTextColor(R.id.tv_product_designer, ContextCompat.getColor(mContext, R.color.grey))
                    .setTextColor(R.id.tv_product_price, ContextCompat.getColor(mContext, R.color.red));

        } else {
            helper.setVisible(R.id.view_mask, true)
                    .setText(R.id.tv_material_status, "已下架")
                    .setTextColor(R.id.iv_product_spu_name, ContextCompat.getColor(mContext, R.color.grey))
                    .setTextColor(R.id.tv_product_designer, ContextCompat.getColor(mContext, R.color.grey))
                    .setTextColor(R.id.tv_product_price, ContextCompat.getColor(mContext, R.color.grey));
        }

        //设计师名字
        if (StringUtil.isEmpty(item.nickName)) {
            helper.setText(R.id.tv_product_designer, "设计师:  " + GlobalVariable.UZAO_MATERIAL_NAME);
        } else {
            helper.setText(R.id.tv_product_designer, "设计师:  " + item.nickName);
        }

        if (selectState) {
            //编辑状态
            helper.setVisible(R.id.iv_collect_material_select, true);
            if (item.isSelected) {
                helper.setImageResource(R.id.iv_collect_material_select, R.drawable.icon_circle_selected);
            } else {
                helper.setImageResource(R.id.iv_collect_material_select, R.drawable.icon_circle_unselected);
            }
        } else {
            helper.setVisible(R.id.iv_collect_material_select, false);
        }
    }

    public void setSelectState(boolean selectState) {
        this.selectState = selectState;
        notifyDataSetChanged();
    }

    public boolean getSelectState() {
        return this.selectState;
    }
}
