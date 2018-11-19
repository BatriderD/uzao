package com.zhaotai.uzao.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.PropertyBean;

import java.util.List;

/**
 * Time: 2018/1/13
 * Created by LiYou
 * Description :
 */

public class DesignPropertyItemAdapter extends BaseQuickAdapter<PropertyBean, BaseViewHolder> {


    public DesignPropertyItemAdapter(@Nullable List<PropertyBean> data) {
        super(R.layout.view_design_property, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PropertyBean item) {
        helper.setText(R.id.property_name, item.propertyValue);
         helper.getView(R.id.property_name).setSelected(item.isSelect);
    }
}
