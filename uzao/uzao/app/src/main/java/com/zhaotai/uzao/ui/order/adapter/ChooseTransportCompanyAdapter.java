package com.zhaotai.uzao.ui.order.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.List;

/**
 * Time: 2018/8/2 0002
 * Created by LiYou
 * Description : 添加物流
 */
public class ChooseTransportCompanyAdapter extends BaseQuickAdapter<DictionaryBean, BaseViewHolder> {

    public ChooseTransportCompanyAdapter(@Nullable List<DictionaryBean> data) {
        super(R.layout.item_choose_transport_company, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DictionaryBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.extend1,
                (ImageView) helper.getView(R.id.iv_transport_company_logo));
        helper.setText(R.id.tv_transport_company_name, item.description);
    }
}
