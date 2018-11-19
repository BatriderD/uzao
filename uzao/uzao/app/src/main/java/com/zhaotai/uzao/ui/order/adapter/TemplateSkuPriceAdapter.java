package com.zhaotai.uzao.ui.order.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.TemplateBean;

import java.util.List;

/**
 * Time: 2017/9/9
 * Created by LiYou
 * Description : 填写sku成本价
 */

public class TemplateSkuPriceAdapter extends BaseAdapter {

    private List<TemplateBean.Sku> data;
    private Context context;

    public TemplateSkuPriceAdapter(List<TemplateBean.Sku> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_template_putaway_price, null);
            viewHolder.mCheckBoxImage = (ImageView) convertView.findViewById(R.id.iv_template_putaway_checkbox);
            viewHolder.mSkuValue = (TextView) convertView.findViewById(R.id.tv_template_putaway_sku_value);
            viewHolder.mPrice = (EditText) convertView.findViewById(R.id.et_sale_price);
            viewHolder.mRecommendedPrice = (TextView) convertView.findViewById(R.id.tv_recommended_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final TemplateBean.Sku info = data.get(position);
        viewHolder.mSkuValue.setText(info.skuValues);
        viewHolder.mRecommendedPrice.setText("成本价："+info.priceY);
        viewHolder.mCheckBoxImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isSelected = !info.isSelected;
                notifyDataSetChanged();
            }
        });
        if (info.isSelected) {
            viewHolder.mCheckBoxImage.setImageResource(R.drawable.icon_circle_selected);
        } else {
            viewHolder.mCheckBoxImage.setImageResource(R.drawable.icon_circle_unselected);
        }
        viewHolder.mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                info.salePrice = s.toString();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView mCheckBoxImage;
        TextView mSkuValue;
        EditText mPrice;
        TextView mRecommendedPrice;
    }
}
