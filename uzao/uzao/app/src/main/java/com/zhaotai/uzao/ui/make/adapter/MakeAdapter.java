package com.zhaotai.uzao.ui.make.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.MakeBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * Time: 2017/8/26
 * Created by LiYou
 * Description :
 */

public class MakeAdapter extends BaseQuickAdapter<MakeBean,BaseViewHolder> {


    public MakeAdapter() {
        super(R.layout.item_make);
    }

    @Override
    protected void convert(BaseViewHolder helper, MakeBean item) {
        //订单编号
        helper.setText(R.id.tv_make_order_id,mContext.getString(R.string.order_num,item.designNo))
                    .setText(R.id.tv_make_order_spu_name,item.designName)
                    .setText(R.id.tv_make_order_spu_category,mContext.getString(R.string.belong_category,item.extend3))
                    .addOnClickListener(R.id.ll_make_item)
                    .addOnClickListener(R.id.tv_make_order_copy);

        GlideLoadImageUtil.load(mContext,ApiConstants.UZAOCHINA_IMAGE_HOST +item.thumbnail,(ImageView) helper.getView(R.id.tv_make_order_spu_img));

        ImageView img1 = helper.getView(R.id.iv_make_state_img1);
        ImageView img2 = helper.getView(R.id.iv_make_state_img2);
        ImageView img3 = helper.getView(R.id.iv_make_state_img3);
        ImageView img4 = helper.getView(R.id.iv_make_state_img4);

        switch (item.produceStatus){
            case OrderStatus.DRAFT://草稿
            case OrderStatus.WAIT_CONFIRM_DESIGN://基本信息待确认
                img1.setImageResource(R.drawable.ic_make_normal);
                img2.setImageResource(R.drawable.ic_make_normal);
                img3.setImageResource(R.drawable.ic_make_normal);
                img4.setImageResource(R.drawable.ic_make_normal);
                break;
            case OrderStatus.DESIGN_UNAPPROVED://基本信息审核不通过
                img1.setImageResource(R.drawable.ic_make_un_pass);
                img2.setImageResource(R.drawable.ic_make_normal);
                img3.setImageResource(R.drawable.ic_make_normal);
                img4.setImageResource(R.drawable.ic_make_normal);
                break;
            case OrderStatus.WAIT_PRODUCE://基本信息审核通过
                img1.setImageResource(R.drawable.ic_make_pass);
                img2.setImageResource(R.drawable.ic_make_normal);
                img3.setImageResource(R.drawable.ic_make_normal);
                img4.setImageResource(R.drawable.ic_make_normal);
                break;
            case OrderStatus.VBP_WAIT_CONFIRM:///生产确认待审核
            case OrderStatus.VBP_NO_TPAY://生产 服务 支付
                img1.setImageResource(R.drawable.ic_make_pass);
                img2.setImageResource(R.drawable.ic_make_normal);
                img3.setImageResource(R.drawable.ic_make_normal);
                img4.setImageResource(R.drawable.ic_make_normal);
                break;
            case OrderStatus.VBP_UNAPPROVED://生产确认审核不通过
                img1.setImageResource(R.drawable.ic_make_pass);
                img2.setImageResource(R.drawable.ic_make_un_pass);
                img3.setImageResource(R.drawable.ic_make_normal);
                img4.setImageResource(R.drawable.ic_make_normal);
                break;
            case OrderStatus.WAIT_CONFIRM_SUPPLIER://生产确认审核通过
            case OrderStatus.WAIT_CONFIRM_PRODUCE://生产信息待确认
                img1.setImageResource(R.drawable.ic_make_pass);
                img2.setImageResource(R.drawable.ic_make_pass);
                img3.setImageResource(R.drawable.ic_make_normal);
                img4.setImageResource(R.drawable.ic_make_normal);
                break;
            case OrderStatus.PRODUCE_UNAPPROVED://生产信息审核不通过
                img1.setImageResource(R.drawable.ic_make_pass);
                img2.setImageResource(R.drawable.ic_make_pass);
                img3.setImageResource(R.drawable.ic_make_un_pass);
                img4.setImageResource(R.drawable.ic_make_normal);
                break;
            case OrderStatus.WAIT_CONFIRM_CONTRACT://生产信息审核通过
            case OrderStatus.CONTRACT_WAIT_APPROVED://合同待审核
                img1.setImageResource(R.drawable.ic_make_pass);
                img2.setImageResource(R.drawable.ic_make_pass);
                img3.setImageResource(R.drawable.ic_make_pass);
                img4.setImageResource(R.drawable.ic_make_normal);
                break;
            case OrderStatus.CONTRACT_UNAPPROVED://合同审核不通过
                img1.setImageResource(R.drawable.ic_make_pass);
                img2.setImageResource(R.drawable.ic_make_pass);
                img3.setImageResource(R.drawable.ic_make_pass);
                img4.setImageResource(R.drawable.ic_make_un_pass);
                break;
            case OrderStatus.WAIT_SUBMIT_PRODUCE://合同审核通过
                img1.setImageResource(R.drawable.ic_make_pass);
                img2.setImageResource(R.drawable.ic_make_pass);
                img3.setImageResource(R.drawable.ic_make_pass);
                img4.setImageResource(R.drawable.ic_make_pass);
                break;
            case "finish":
                img1.setImageResource(R.drawable.ic_make_pass);
                img2.setImageResource(R.drawable.ic_make_pass);
                img3.setImageResource(R.drawable.ic_make_pass);
                img4.setImageResource(R.drawable.ic_make_pass);
                break;
        }
    }
}
