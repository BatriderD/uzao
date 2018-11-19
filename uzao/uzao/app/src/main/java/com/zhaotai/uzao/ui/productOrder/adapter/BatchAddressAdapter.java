package com.zhaotai.uzao.ui.productOrder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.ProductOrderDetailBean;
import com.zhaotai.uzao.ui.order.activity.LogisticsActivity;

import java.util.List;

import anet.channel.util.StringUtils;

/**
 * Time: 2017/8/24
 * Created by LiYou
 * Description : 批量地址
 */

public class BatchAddressAdapter extends BaseAdapter {

    private List<ProductOrderDetailBean.BatchAddress> data;
    private Context context;
    private boolean hasLogistics;//是否有物流

    public BatchAddressAdapter(Context context, List<ProductOrderDetailBean.BatchAddress> data,boolean hasLogistics) {
        this.data = data;
        this.context = context;
        this.hasLogistics = hasLogistics;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_batch_address, null);
            viewHolder.mAddressPosition = (TextView) convertView.findViewById(R.id.tv_batch_address_position);
            viewHolder.mName = (TextView) convertView.findViewById(R.id.tv_batch_address_receive_people);
            viewHolder.mPhone = (TextView) convertView.findViewById(R.id.tv_batch_address_receive_phone);
            viewHolder.mAddress = (TextView) convertView.findViewById(R.id.tv_batch_address_receive_address);
            viewHolder.mLogistics = (TextView) convertView.findViewById(R.id.tv_batch_address_logistics);
            viewHolder.mLine = convertView.findViewById(R.id.tv_batch_address_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ProductOrderDetailBean.BatchAddress address = data.get(position);
        int addressPosition = position + 1;
        viewHolder.mAddressPosition.setText(String.valueOf(addressPosition));
        viewHolder.mName.setText(address.receiverName);
        viewHolder.mPhone.setText(address.receiverMobile);
        viewHolder.mAddress.setText(address.receiverAddress.trim());
        if(hasLogistics){
            //物流信息
            viewHolder.mLogistics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogisticsActivity.launch(context, LogisticsActivity.TYPE_TRANSPORT, address.transportId);
                }
            });
        }else {
            viewHolder.mLogistics.setVisibility(View.GONE);
        }

        if(position == data.size()-1){
            viewHolder.mLine.setVisibility(View.VISIBLE);
        }else {
            viewHolder.mLine.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView mAddressPosition;
        TextView mName;
        TextView mPhone;
        TextView mAddress;
        TextView mLogistics;
        View mLine;
    }
}
