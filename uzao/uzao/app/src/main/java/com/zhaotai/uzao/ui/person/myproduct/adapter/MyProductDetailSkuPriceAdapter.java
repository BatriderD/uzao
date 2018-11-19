package com.zhaotai.uzao.ui.person.myproduct.adapter;

import android.content.Context;
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
 * Time: 2017/9/12
 * Created by LiYou
 * Description : 填写sku成本价
 */

public class MyProductDetailSkuPriceAdapter extends BaseAdapter {

    private String price;
    private List<TemplateBean.Sku> data;
    private Context context;

    public MyProductDetailSkuPriceAdapter(List<TemplateBean.Sku> data, String price, Context context) {
        this.data = data;
        this.price = price;
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
        viewHolder.mRecommendedPrice.setText("成本价：" + info.marketPriceY);
        viewHolder.mPrice.setText(info.priceY);
        viewHolder.mPrice.setEnabled(false);
        viewHolder.mPrice.setFocusable(false);
        viewHolder.mCheckBoxImage.setVisibility(View.GONE);

        return convertView;
    }


    private static class ViewHolder {
        ImageView mCheckBoxImage;
        TextView mSkuValue;
        EditText mPrice;
        TextView mRecommendedPrice;
    }
}
