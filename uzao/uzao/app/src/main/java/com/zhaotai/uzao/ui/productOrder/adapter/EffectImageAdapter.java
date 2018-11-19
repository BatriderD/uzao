package com.zhaotai.uzao.ui.productOrder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.List;

/**
 * Time: 2017/8/23
 * Created by LiYou
 * Description : 效果图
 */

public class EffectImageAdapter extends BaseAdapter {

    private List<String> data;
    private Context context;

    public EffectImageAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment_image, null);
            viewHolder.mImage = (ImageView) convertView.findViewById(R.id.iv_grid_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GlideLoadImageUtil.load(context, ApiConstants.UZAOCHINA_IMAGE_HOST + data.get(position),viewHolder.mImage);
        return convertView;
    }

    class ViewHolder {
         ImageView mImage;
    }
}
