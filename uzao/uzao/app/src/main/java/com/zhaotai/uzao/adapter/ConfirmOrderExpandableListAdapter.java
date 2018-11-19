package com.zhaotai.uzao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.ShoppingCartBean;
import com.zhaotai.uzao.bean.ShoppingGoodsBean;
import com.zhaotai.uzao.utils.DecimalUtil;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;


public class ConfirmOrderExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ShoppingCartBean.Cart> mListGoods = new ArrayList<>();

    public ConfirmOrderExpandableListAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<ShoppingCartBean.Cart> mListGoods) {
        this.mListGoods = mListGoods;
    }

    @Override
    public int getGroupCount() {
        return mListGoods.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListGoods.get(groupPosition).cartModels.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListGoods.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListGoods.get(groupPosition).cartModels.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_confirm_order_shopping_cart_section, parent, false);
            holder.tvGroup = (TextView) convertView.findViewById(R.id.tv_confirm_order_section_activity);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_confirm_order_section_icon);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        if (mListGoods.get(groupPosition).activityModel != null) {
            holder.tvGroup.setVisibility(View.GONE);
            //活动照片
            holder.ivIcon.setVisibility(View.VISIBLE);
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + mListGoods.get(groupPosition).activityModel.icon, holder.ivIcon);
        } else {
            holder.tvGroup.setVisibility(View.VISIBLE);
            holder.tvGroup.setText("未参加活动");
            holder.ivIcon.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * child view
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_confirm_order_child_goods, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.shopping_cart_title);
            holder.properties = (TextView) convertView.findViewById(R.id.shopping_cart_spu_properties);
            holder.price = (TextView) convertView.findViewById(R.id.shopping_cart_spu_price);
            holder.number = (TextView) convertView.findViewById(R.id.tv_confirm_order_child_num);
            holder.spuImage = (ImageView) convertView.findViewById(R.id.shopping_cart_spu_image);
            holder.divider = convertView.findViewById(R.id.view_shopping_cart_bottom_divider);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        ShoppingGoodsBean goods = mListGoods.get(groupPosition).cartModels.get(childPosition);
        String goodName = goods.spuName;
        String price = goods.unitPrice;
        String number = goods.count;
        String properties = goods.properties;

        holder.title.setText(goodName);
        //价格
        holder.price.setText(String.format(mContext.getResources().getString(R.string.account), DecimalUtil.getPrice(price)));
        holder.number.setText(mContext.getString(R.string.goods_number, number));
        if (!StringUtil.isEmpty(properties)) {
            holder.properties.setText(properties.replace(";", "  "));
        }
        //商品图片
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + goods.spuPic, holder.spuImage);
        if (isLastChild) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }


    private class GroupViewHolder {
        TextView tvGroup;//活动名称
        ImageView ivIcon;//活动图片
    }

    private class ChildViewHolder {
        /**
         * 商品名称
         */
        TextView title;
        /**
         * 商品规格
         */
        TextView properties;
        /**
         * 价格
         */
        TextView price;
        /**
         * 商品状态的数量
         */
        TextView number;
        /**
         * 商品图片
         */
        ImageView spuImage;
        View divider;
    }
}
