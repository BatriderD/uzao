package com.zhaotai.uzao.ui.person.setting.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.CategoryTagsBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

/**
 * description:  个人标签页面
 * author : ZP
 * date: 2018/1/22 0022.
 */

public class TagsAdapter extends BaseQuickAdapter<CategoryTagsBean.ChildrenBean, BaseViewHolder> {
    private MyItemClickListener myItemClickListener;


    public TagsAdapter() {
        super(R.layout.item_person_tags);

    }

    public void setOnMyclickerListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CategoryTagsBean.ChildrenBean item) {

        helper.setText(R.id.tv_item_tag_title, item.getCategoryName());
        final LayoutInflater mInflater = LayoutInflater.from(mContext);

        final TagFlowLayout mFlowLayout = helper.getView(R.id.id_flowlayout);
        TagAdapter tagAdapter = new TagAdapter<CategoryTagsBean.ChildrenBean.TagsBean>(item.getTags()) {
            @Override
            public View getView(FlowLayout parent, int position, CategoryTagsBean.ChildrenBean.TagsBean s) {

                TextView tv = (TextView) mInflater.inflate(R.layout.item_flowlayout_tags, mFlowLayout, false);
                tv.setText(s.getTagName());
                return tv;
            }

            @Override
            public boolean setSelected(int position, CategoryTagsBean.ChildrenBean.TagsBean tagsBean) {
                return tagsBean.isSelected();
            }
        };
        mFlowLayout.setAdapter(tagAdapter);
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (myItemClickListener != null) {
                    CategoryTagsBean.ChildrenBean.TagsBean tagsBean = item.getTags().get(position);
                    myItemClickListener.itemClick(view, tagsBean);
                }
                return true;
            }
        });
    }

    public interface MyItemClickListener {
        void itemClick(View view, CategoryTagsBean.ChildrenBean.TagsBean item);
    }
}
