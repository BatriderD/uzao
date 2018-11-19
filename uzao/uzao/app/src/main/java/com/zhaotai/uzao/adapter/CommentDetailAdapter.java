package com.zhaotai.uzao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.ui.util.PreviewActivity;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.widget.CustomGridLayoutManager;

import java.util.ArrayList;

/**
 * Time: 2017/6/15
 * Created by LiYou
 * Description : 评价详情
 */

public class CommentDetailAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    private Gson gson;

    public CommentDetailAdapter() {
        super(R.layout.item_comment_detail);
        this.gson = new Gson();
    }

    @Override
    protected void convert(BaseViewHolder helper, final CommentBean item) {
        OrderGoodsDetailBean detail = gson.fromJson(item.entityProfile, OrderGoodsDetailBean.class);
        OrderGoodsDetailBean orderGoodsDetail = gson.fromJson(detail.orderDetail, OrderGoodsDetailBean.class);
        //商品图片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + orderGoodsDetail.pic, (ImageView) helper.getView(R.id.iv_comment_detail_spu_pic));
        //商品名字
        helper.setText(R.id.tv_comment_detail_spu_name, orderGoodsDetail.name)
                .setText(R.id.tv_comment_detail_text, item.commentBody);

        //星级评价
        ((RatingBar) helper.getView(R.id.rb_comment)).setRating(item.starScore);

        RecyclerView recyclerView = helper.getView(R.id.recycler_comment_detail);
        CustomGridLayoutManager gridLayoutManager = new CustomGridLayoutManager(mContext, 3);
        gridLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(gridLayoutManager);
        CommentImageAdapter commentImageAdapter = new CommentImageAdapter(R.layout.item_apply_after_sales_detail_image, item.commentImage);
        recyclerView.setAdapter(commentImageAdapter);
        //查看预览图
        commentImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PreviewActivity.launch(mContext, (ArrayList<String>) item.commentImage, position, ApiConstants.UZAOCHINA_IMAGE_HOST);
            }
        });
    }
}
