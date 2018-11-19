package com.zhaotai.uzao.ui.person.address.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.AddressBean;


/**
 * time:2017/4/17
 * description:  我的地址 适配器
 * author: LiYou
 */

public class AddressListAdapter extends BaseRecyclerAdapter<AddressBean, BaseViewHolder> {


    public AddressListAdapter() {
        super(R.layout.item_address);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressBean addressBean) {
        View view = helper.getView(R.id.view_address_address_line);
        if (helper.getAdapterPosition() == getData().size() - 1) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        helper.setText(R.id.address_name, addressBean.recieverName);
        helper.setText(R.id.address_phone, addressBean.recieverPhone);
        helper.setText(R.id.address_detail, addressBean.addrAlias);

        CheckBox mDefaultCheckBox = helper.getView(R.id.checkbox_is_default);
        TextView mDefaultText = helper.getView(R.id.is_default_text);
        if (addressBean.isDefault.equals("Y")) {
            mDefaultCheckBox.setChecked(true);

            mDefaultText.setText(R.string.default_address);
            mDefaultText.setTextColor(Color.parseColor("#ff6600"));
        } else {
            mDefaultCheckBox.setChecked(false);
            mDefaultText.setText(R.string.set_default);
            mDefaultText.setTextColor(Color.parseColor("#4a4a4a"));
        }
        helper.addOnClickListener(R.id.address_delete)
                .addOnClickListener(R.id.tv_address_address_modify)
                .addOnClickListener(R.id.address_ll)
                .addOnClickListener(R.id.ll_is_default);
    }

}
