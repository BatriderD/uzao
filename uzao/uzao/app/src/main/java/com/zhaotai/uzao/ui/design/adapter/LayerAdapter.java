package com.zhaotai.uzao.ui.design.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.LayerDataBean;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * 图层控件
 */

public class LayerAdapter extends BaseItemDraggableAdapter<LayerDataBean, BaseViewHolder> {
    private int selectedItem = -1;
    public LayerAdapter() {
        super(R.layout.item_rc_layer, new ArrayList<LayerDataBean>());
    }


    @Override
    protected void convert(BaseViewHolder helper, LayerDataBean item) {
        View iv_locker = helper.getView(R.id.iv_layer_locker);
        View llBar = helper.getView(R.id.ll_action_bar);
        if (item.isLock()) {
            iv_locker.setVisibility(View.VISIBLE);
            llBar.setVisibility(View.GONE);
        } else {
            iv_locker.setVisibility(View.GONE);
            llBar.setVisibility(View.VISIBLE);
        }

        helper.getView(R.id.iv_hide_layer).setSelected(item.isVisable());


        ImageView ivContent = helper.getView(R.id.iv_layer_content);
        Bitmap bitmap = item.getBitmapContent();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();


        Glide.with(mContext)
                .load(bytes)
                .placeholder(R.mipmap.ic_place_holder)
                .into(ivContent);

        helper.addOnClickListener(R.id.iv_hide_layer)
                .addOnClickListener(R.id.iv_del_layer);
    }



    public void setselected(int position) {
        if (selectedItem >= 0 && getData().size() > selectedItem) {
            LayerDataBean item = getItem(selectedItem);
            item.setSelected(false);
            notifyItemChanged(selectedItem);
        }
        getItem(position).setSelected(true);
        selectedItem = position;
        notifyItemChanged(selectedItem);
    }

    public void clearSelected(){
        if (selectedItem >= 0 && getData().size() > selectedItem) {
            LayerDataBean item = getItem(selectedItem);
            item.setSelected(false);
            notifyItemChanged(selectedItem);
        }
    }

//    @Override
//    public void onViewDetachedFromWindow(BaseViewHolder holder) {
//        ImageView ivContent = holder.getView(R.id.iv_layer_content);
//        ivContent.setImageBitmap(null);
//        super.onViewDetachedFromWindow(holder);
//    }
}
