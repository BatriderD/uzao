package com.zhaotai.uzao.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.ui.util.PreviewActivity;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.utils.transform.CircleTransform;
import com.zhaotai.uzao.widget.CustomGridLayoutManager;
import com.zhaotai.uzao.widget.RatingBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/8/18
 * Created by LiYou
 * Description : 评价列表
 */

public class CommentListAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    private Gson gson;

    public CommentListAdapter(@Nullable List<CommentBean> data) {
        super(R.layout.item_comment_list, data);
        this.gson = new Gson();
    }

    @Override
    protected void convert(BaseViewHolder helper, final CommentBean item) {
        OrderGoodsDetailBean goodsDetail = gson.fromJson(item.entityProfile, OrderGoodsDetailBean.class);
        OrderGoodsDetailBean orderDetail = gson.fromJson(goodsDetail.orderDetail, OrderGoodsDetailBean.class);
        PersonBean personInfo = gson.fromJson(item.userInfo, PersonBean.class);
        ((RatingBar) helper.getView(R.id.rb_comment)).setStar(item.starScore);
        //用户名
        helper.setText(R.id.tv_comment_name, personInfo.nickName)
                //时间
                .setText(R.id.tv_comment_time, TimeUtils.millis2String(Long.parseLong(item.recDate)))
                //评价内容
                .setText(R.id.tv_comment_list_content, item.commentBody)
                //规格
                .setText(R.id.tv_comment_property, orderDetail.category);

        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + personInfo.avatar, (ImageView) helper.getView(R.id.tv_comment_pic), R.drawable.ic_default_head, R.drawable.ic_default_head, new CircleTransform(mContext));
        //评价图片

        RecyclerView recyclerView = helper.getView(R.id.recycler_comment_pic);
        CustomGridLayoutManager gridLayoutManager = new CustomGridLayoutManager(mContext, 3);
        gridLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(gridLayoutManager);

        CommentImageAdapter commentImageAdapter = new CommentImageAdapter(R.layout.item_comment_image, item.commentImage);
        recyclerView.setAdapter(commentImageAdapter);
        commentImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PreviewActivity.launch(mContext, (ArrayList<String>) item.commentImage, position, ApiConstants.UZAOCHINA_IMAGE_HOST);
            }
        });

        //评论回复
        if (item.haveComment.equals("Y") && item.children != null && item.children.size() > 0) {
            helper.setText(R.id.tv_comment_reply, "回复:    " + item.children.get(0).commentBody)
                    .setGone(R.id.tv_comment_reply,true);
        }else {
            helper.setGone(R.id.tv_comment_reply,false);
        }
    }
}
