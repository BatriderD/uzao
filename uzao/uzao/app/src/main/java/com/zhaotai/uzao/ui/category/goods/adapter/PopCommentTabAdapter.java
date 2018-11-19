package com.zhaotai.uzao.ui.category.goods.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.StartCountBean;
import com.zhaotai.uzao.widget.RatingBar;

/**
 * Time: 2017/12/5
 * Created by LiYou
 * Description : 评价tab下拉框
 */

public class PopCommentTabAdapter extends BaseQuickAdapter<StartCountBean, BaseViewHolder> {

    public PopCommentTabAdapter() {
        super(R.layout.pop_commend_rating_start);
    }

    @Override
    protected void convert(BaseViewHolder helper, StartCountBean item) {
        if (helper.getLayoutPosition() == 0) {
            helper.setVisible(R.id.pop_tv_all_comment, true)
                    .setVisible(R.id.rb_comment, false)
                    .setText(R.id.pop_tv_comment_count, item.COUNT1);
        } else {
            helper.setVisible(R.id.pop_tv_all_comment, false)
                    .setVisible(R.id.rb_comment, true)
                    .setText(R.id.pop_tv_comment_count, item.COUNT1);
            ((RatingBar) helper.getView(R.id.rb_comment)).setStar(item.STAR_SCORE);
        }

    }
}
