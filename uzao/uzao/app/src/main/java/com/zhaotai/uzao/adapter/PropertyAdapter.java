package com.zhaotai.uzao.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.PropertyBean;
import com.zhaotai.uzao.listener.ItemChangeListenerInterface;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.Set;

/**
 * Time: 2017/5/17
 * Created by LiYou
 * Description : 详情 属性里面的  颜色 规格
 */

public class PropertyAdapter extends BaseQuickAdapter<PropertyBean, BaseViewHolder> {

    private ItemChangeListenerInterface listener;

    public PropertyAdapter(@Nullable List<PropertyBean> data) {
        super(R.layout.item_property_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PropertyBean item) {
        helper.setText(R.id.tv_property_title, item.propertyTypeName);
        TagFlowLayout tagFlowLayout = helper.getView(R.id.tf_flowlayout);

        tagFlowLayout.setAdapter(new TagAdapter<PropertyBean>(item.spuProperties) {
            @Override
            public View getView(FlowLayout parent, int position, PropertyBean s) {
                TextView tv = (TextView) View.inflate(mContext, R.layout.view_property, null);
                tv.setText(s.propertyValue);
                return tv;
            }
        });

        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet.size() > 0) {
                    item.isSelect = true;
                    for (Integer position : selectPosSet) {
                        for (int i = 0; i < item.spuProperties.size(); i++) {
                            item.spuProperties.get(i).isSelect = position == i;
                        }
                    }
                } else {
                    item.isSelect = false;
                }
                if (listener != null)
                    listener.OnItemChange();
            }
        });

    }

    public void setListener(ItemChangeListenerInterface listener) {
        this.listener = listener;
    }
}
