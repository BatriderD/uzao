package com.zhaotai.uzao.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.TransportInfo;

import java.util.List;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 物流信息
 */

public class TransportAdapter extends BaseQuickAdapter<TransportInfo,BaseViewHolder>{

    public TransportAdapter(@Nullable List<TransportInfo> data) {
        super(R.layout.item_transport,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransportInfo item) {
        helper.setText(R.id.transport_context,item.context)
                .setText(R.id.transport_time,item.ftime);
        if(helper.getLayoutPosition() == 1){
            helper.getView(R.id.transport_top_line).setVisibility(View.INVISIBLE);
            helper.setImageResource(R.id.transport_circle,R.drawable.transport_circle);
        }
        if(helper.getLayoutPosition() == getItemCount()-1){
            helper.setVisible(R.id.transport_bottom_line,false);
        }
    }
}
