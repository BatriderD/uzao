package com.zhaotai.uzao.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.OrderMaterialBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.List;

/**
 * Time: 2018/1/9
 * Created by LiYou
 * Description :
 */

public class OrderChildMaterialAdapter extends BaseQuickAdapter<OrderMaterialBean, BaseViewHolder> {

    private List<OrderMaterialBean> data;

    public OrderChildMaterialAdapter(List<OrderMaterialBean> data) {
        super(R.layout.item_order_goods, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderMaterialBean item) {

        helper.setText(R.id.tv_order_goods_title, item.materialInfo.sourceMaterialName)
                .setText(R.id.tv_order_goods_properties, "")
                .setText(R.id.tv_order_goods_num, "")
                .setText(R.id.tv_order_goods_price, mContext.getString(R.string.account, item.materialInfo.countPriceY))
                .setVisible(R.id.tv_material_time, true);

        //授权时长
        String periodUnit = "";
        if ("month".equals(item.periodUnit)) {
            periodUnit = "个月";
        } else if ("year".equals(item.periodUnit)) {
            periodUnit = "年";
        } else if ("forever".equals(item.periodUnit)) {
            periodUnit = "永久";
            item.authPeriod = "";
        }
        helper.setText(R.id.tv_material_time, "授权时长:    " + item.authPeriod + periodUnit);

        //商品图片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.materialInfo.thumbnail, (ImageView) helper.getView(R.id.iv_order_goods_image));

        if (helper.getLayoutPosition() == data.size() - 1) {
            helper.setVisible(R.id.tv_order_bottom_divider, false);
        } else {
            helper.setVisible(R.id.tv_order_bottom_divider, true);
        }
    }
}
