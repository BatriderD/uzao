package com.zhaotai.uzao.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.PropertyBean;

/**
 * Time: 2017/5/17
 * Created by LiYou
 * Description : 设计里面 规格选择
 */

public class DesignPropertyAdapter extends BaseQuickAdapter<PropertyBean, BaseViewHolder> {

    public DesignPropertyAdapter() {
        super(R.layout.item_design_property);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PropertyBean item) {
        helper.setText(R.id.tv_property_title, item.propertyTypeName);

        RecyclerView mRecycler = helper.getView(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        DesignPropertyItemAdapter mAdapter = new DesignPropertyItemAdapter(item.spuProperties);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < item.spuProperties.size(); i++) {
                    item.spuProperties.get(i).isSelect = position == i;
                }
                adapter.notifyDataSetChanged();
                item.isSelect = true;
            }
        });
    }
}
