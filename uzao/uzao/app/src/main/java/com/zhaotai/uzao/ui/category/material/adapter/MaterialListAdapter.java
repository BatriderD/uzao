package com.zhaotai.uzao.ui.category.material.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description : 素材列表
 */

public class MaterialListAdapter extends BaseRecyclerAdapter<MaterialListBean, BaseViewHolder> {

    public MaterialListAdapter() {
        super(R.layout.item_material_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialListBean item) {
        helper.setText(R.id.iv_product_spu_name, item.materialName);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic, (ImageView) helper.getView(R.id.iv_product_spu_image));
        switch (item.saleMode) {
            case GlobalVariable.MATERIAL_MODE_CHARGE://收费
                helper.setText(R.id.tv_product_price, "¥" + item.priceY);
                break;
            case GlobalVariable.MATERIAL_MODE_FREE://免费
                helper.setText(R.id.tv_product_price, "免费");
                break;
        }

        //设计师名字
        if (item.data != null && item.data.nickName != null) {
            helper.setText(R.id.tv_product_designer, "设计师:  " + item.data.nickName);
        } else {
            helper.setText(R.id.tv_product_designer, "设计师:  " + GlobalVariable.UZAO_MATERIAL_NAME);
        }
    }
}
