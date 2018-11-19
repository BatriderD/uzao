package com.zhaotai.uzao.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.ui.order.listener.OnCommentImageListener;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.widget.CustomGridLayoutManager;

import java.util.List;

/**
 * Time: 2017/8/19
 * Created by LiYou
 * Description : 发布评价
 */

public class CommentPostAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    private OnCommentImageListener commentImageListener;
    private ImageAdapter commentImageAdapter;

    public CommentPostAdapter(@Nullable List<CommentBean> data) {
        super(R.layout.item_comment_post, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommentBean item) {

        //商品图片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.pic, (ImageView) helper.getView(R.id.iv_comment_post_spu_pic));
        //评价
        if (item.starScore > 0) {
            ((RatingBar) helper.getView(R.id.rb_comment)).setRating(item.starScore);
        }
        ((RatingBar) helper.getView(R.id.rb_comment)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    if (rating == 0) {
                        ((RatingBar) helper.getView(R.id.rb_comment)).setRating(1);
                        item.starScore = 1;
                    } else {
                        item.starScore = (int) rating;
                    }
                }
            }
        });
        //评论
        helper.setText(R.id.et_comment_post_text, item.commentBody);

        RecyclerView recyclerView = helper.getView(R.id.recycler_comment_post);
        CustomGridLayoutManager gridLayoutManager = new CustomGridLayoutManager(mContext, 3);
        gridLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(gridLayoutManager);

        commentImageAdapter = new ImageAdapter(R.layout.item_upload_image, item.imgList);
        recyclerView.setAdapter(commentImageAdapter);
        commentImageAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (commentImageListener != null) {
                    commentImageListener.OnItemClick(adapter, view, position, helper.getAdapterPosition());
                }
            }
        });

        //记录评价
        EditText et = helper.getView(R.id.et_comment_post_text);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CommentBean cb = getItem(helper.getAdapterPosition());
                if (cb != null)
                    cb.commentBody = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void setOnCommentImageListener(OnCommentImageListener commentImageListener) {
        this.commentImageListener = commentImageListener;
    }
}
