package com.zhaotai.uzao.ui.order.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.List;

/**
 * Time: 2018/1/10
 * Created by LiYou
 * Description :
 */

public class MaterialPayConfirmAdapter extends BaseQuickAdapter<MaterialDetailBean, BaseViewHolder> {

    public MaterialPayConfirmAdapter(List<MaterialDetailBean> data) {
        super(R.layout.item_material_pay_comfirm, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialDetailBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.thumbnail, (ImageView) helper.getView(R.id.iv_product_pic));
        helper.setText(R.id.tv_product_name, item.sourceMaterialName);
        if (item.designer != null) {
            helper.setText(R.id.tv_product_designer, item.designer.nickName);
        }

        if (GlobalVariable.MATERIAL_MODE_CHARGE.equals(item.saleMode)) {
            helper.setText(R.id.tv_product_price, "¥" + item.priceY);
        } else {
            helper.setText(R.id.tv_product_price, "免费");
        }
    }
}
