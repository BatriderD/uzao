package com.zhaotai.uzao.ui.person.collection.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;


/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description : 我关注的设计师列表  应用地方: 个人中心
 */

public class CollectionProductListAdapter extends BaseRecyclerAdapter<GoodsBean, BaseViewHolder> {

    private boolean selectState;

    public CollectionProductListAdapter() {
        super(R.layout.item_collection_product);
        this.selectState = false;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        helper.addOnClickListener(R.id.collection_spu_bg);

        //商品图片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.thumbnail, (ImageView) helper.getView(R.id.collection_spu_image));
        //商品名
        helper.setText(R.id.collection_spu_name, item.spuName);
        //商品价格
        helper.setText(R.id.tv_collect_product_price, item.priceY);
        //商品描述
        helper.setText(R.id.tv_collect_product_produce, item.description);

        if (selectState) {
            //编辑状态
            helper.setVisible(R.id.btn_collect_product_select, true);
            if (item.isSelected) {
                helper.setImageResource(R.id.iv_collect_product_select, R.drawable.icon_circle_selected);
            } else {
                helper.setImageResource(R.id.iv_collect_product_select, R.drawable.icon_circle_unselected);
            }
            helper.addOnClickListener(R.id.btn_collect_product_select);
        } else {
            helper.setVisible(R.id.btn_collect_product_select, false);
        }

        if (item.status.equals("published")) {
            //正常商品
            helper.setVisible(R.id.iv_collect_product_cut_price, false)
                    .setVisible(R.id.ll_spu_tag, true)
                    .setVisible(R.id.view_failure_mask, false)//蒙层
                    .setTextColor(R.id.collection_spu_name, Color.parseColor("#262626"))//设置字体颜色
                    .setTextColor(R.id.tv_collect_product_produce, Color.parseColor("#666666"))//设置字体颜色
                    .setTextColor(R.id.tv_collect_product_price_icon, Color.parseColor("#d30000"))//设置字体颜色
                    .setTextColor(R.id.tv_collect_product_price, Color.parseColor("#d30000"))//设置字体颜色
                    .setVisible(R.id.fl_collect_product_similar_product, false)
                    .setVisible(R.id.tv_collect_product_original_price, false);//原价
        } else {
            //失效商品
            helper.setVisible(R.id.iv_collect_product_cut_price, false)
                    .setVisible(R.id.fl_collect_product_similar_product, true)
                    .setTextColor(R.id.collection_spu_name, Color.parseColor("#999999"))//设置字体颜色
                    .setTextColor(R.id.tv_collect_product_produce, Color.parseColor("#999999"))//设置字体颜色
                    .setTextColor(R.id.tv_collect_product_price_icon, Color.parseColor("#999999"))//设置字体颜色
                    .setTextColor(R.id.tv_collect_product_price, Color.parseColor("#999999"))//设置字体颜色
                    .setVisible(R.id.view_failure_mask, true)//蒙层
                    .setVisible(R.id.tv_collect_product_original_price, false);//原价
            helper.getView(R.id.ll_spu_tag).setVisibility(View.INVISIBLE);

            helper.addOnClickListener(R.id.fl_collect_product_similar_product);
        }

        //商品标签
        if (item.activity) {
            helper.setVisible(R.id.tv_spu_tag_activity, true);
            //.setText(R.id.tv_spu_tag_activity, item.data.activityName);
        } else {
            helper.setVisible(R.id.tv_spu_tag_activity, false);
        }
        if (item.spuType.equals("mall")) {
            helper.setVisible(R.id.tv_spu_tag_design, false);
        } else {
            helper.setVisible(R.id.tv_spu_tag_design, true)
                    .setText(R.id.tv_spu_tag_design, "可定制");
        }
    }

    public void setSelectState(boolean selectState) {
        this.selectState = selectState;
        notifyDataSetChanged();
    }
}
