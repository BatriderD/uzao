package com.zhaotai.uzao.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.utils.DecimalUtil;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;


/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description : 我的商品列表  应用地方: 个人中心
 */

public class MyProductListAdapter extends BaseRecyclerAdapter<GoodsBean, BaseViewHolder> {

    private Context context;

    public MyProductListAdapter(Context context) {
        super(R.layout.item_my_product);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        //商品图片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.thumbanil, (ImageView) helper.getView(R.id.iv_my_product_goods_image));
        //商品名
        helper.setText(R.id.tv_my_product_goods_title, item.spuName);
        if (item.displayPrice != null) {
            //商品价格
            helper.setText(R.id.tv_my_product_goods_price, DecimalUtil.getPrice(item.displayPrice));
        }
        //商品状态
        switch (item.status) {
            case "unReviewed":
                helper.setText(R.id.tv_my_product_status, "未审核").setGone(R.id.ll_my_product_reason, false);
                helper.setGone(R.id.ll_my_product_bottom, true)
                        .setText(R.id.tv_my_product_bottom_left_button, "删除")
                        .setText(R.id.tv_my_product_bottom_middle_button, "编辑")
                        .setText(R.id.tv_my_product_bottom_right_button, "提交审核")
                        .setGone(R.id.tv_my_product_bottom_left_button, true)
                        .setGone(R.id.tv_my_product_bottom_middle_button, true)
                        .setGone(R.id.tv_my_product_bottom_right_button, true)
                        .setGone(R.id.tv_my_product_bottom_line, true)
                        .addOnClickListener(R.id.tv_my_product_bottom_left_button)
                        .addOnClickListener(R.id.tv_my_product_bottom_middle_button)
                        .addOnClickListener(R.id.tv_my_product_bottom_right_button);
                break;
            case "waitApprove":
                helper.setText(R.id.tv_my_product_status, "审核中").setGone(R.id.ll_my_product_reason, false);
                helper.setGone(R.id.ll_my_product_bottom, false)
                        .setGone(R.id.tv_my_product_bottom_line, false);
                break;
            case "unPublished":
                helper.setText(R.id.tv_my_product_status, "未发布").setGone(R.id.ll_my_product_reason, false);
                helper.setGone(R.id.ll_my_product_bottom, true)
                        .setText(R.id.tv_my_product_bottom_left_button, "删除")
                        .setText(R.id.tv_my_product_bottom_middle_button, "编辑")
                        .setText(R.id.tv_my_product_bottom_right_button, "立即发布")
                        .setGone(R.id.tv_my_product_bottom_left_button, true)
                        .setGone(R.id.tv_my_product_bottom_middle_button, true)
                        .setGone(R.id.tv_my_product_bottom_right_button, true)
                        .setGone(R.id.tv_my_product_bottom_line, true)
                        .addOnClickListener(R.id.tv_my_product_bottom_left_button)
                        .addOnClickListener(R.id.tv_my_product_bottom_middle_button)
                        .addOnClickListener(R.id.tv_my_product_bottom_right_button);
                break;
            case "published":
                helper.setText(R.id.tv_my_product_status, "已发布").setGone(R.id.ll_my_product_reason, false);
                helper.setGone(R.id.ll_my_product_bottom, true)
                        .setText(R.id.tv_my_product_bottom_middle_button, "下架")
                        .setText(R.id.tv_my_product_bottom_right_button, "查看详情")
                        .setGone(R.id.tv_my_product_bottom_left_button, false)
                        .setGone(R.id.tv_my_product_bottom_middle_button, true)
                        .setGone(R.id.tv_my_product_bottom_right_button, true)
                        .setGone(R.id.tv_my_product_bottom_line, true)
                        .addOnClickListener(R.id.tv_my_product_bottom_middle_button)
                        .addOnClickListener(R.id.tv_my_product_bottom_right_button);
                break;
            case "unApproved":
                helper.setText(R.id.tv_my_product_status, "审核未通过")
                        .setText(R.id.tv_my_product_reason, "审核不同过原因:  " + item.approveIdea)
                        .setGone(R.id.ll_my_product_reason, true);
                helper.setGone(R.id.ll_my_product_bottom, true)
                        .setText(R.id.tv_my_product_bottom_left_button, "删除")
                        .setText(R.id.tv_my_product_bottom_right_button, "编辑")
                        .setGone(R.id.tv_my_product_bottom_left_button, true)
                        .setGone(R.id.tv_my_product_bottom_middle_button, false)
                        .setGone(R.id.tv_my_product_bottom_right_button, true)
                        .setGone(R.id.tv_my_product_bottom_line, true)
                        .addOnClickListener(R.id.tv_my_product_bottom_left_button)
                        .addOnClickListener(R.id.tv_my_product_bottom_right_button);
                break;
            default:
                helper.setText(R.id.tv_my_product_status, "");
                break;
        }
    }
}
