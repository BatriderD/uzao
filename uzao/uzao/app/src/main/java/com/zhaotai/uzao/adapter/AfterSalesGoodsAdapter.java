package com.zhaotai.uzao.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.AfterSalesBean;
import com.zhaotai.uzao.utils.DecimalUtil;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.List;

/**
 * Time: 2018/8/6
 * Created by LiYou
 * Description : 申请售后列表商品信息
 */

public class AfterSalesGoodsAdapter extends BaseRecyclerAdapter<AfterSalesBean.SpuInfo, BaseViewHolder> {

    private Gson gson;

    public AfterSalesGoodsAdapter(List<AfterSalesBean.SpuInfo> data) {
        super(R.layout.item_after_sale_content, data);
        gson = new Gson();
    }

    @Override
    protected void convert(BaseViewHolder helper, final AfterSalesBean.SpuInfo item) {
        AfterSalesBean.SpuInfo info = gson.fromJson(item.orderDetail, AfterSalesBean.SpuInfo.class);
        helper.setText(R.id.tv_product_title, info.name)
//                规格
                .setText(R.id.tv_product_condition, info.category.replace(";", "  "))
//                单价
                .setText(R.id.tv_product_money, mContext.getString(R.string.account, DecimalUtil.getPrice(item.unitPrice)))
//                数量
                .setText(R.id.tv_product_count, mContext.getString(R.string.goods_number, item.count));
        //图片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + info.pic, (ImageView) helper.getView(R.id.iv_product_image));
    }

}
