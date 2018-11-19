package com.zhaotai.uzao.ui.design.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.MyPicBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

/**
 * Time: 2017/11/23
 * Created by zp
 * Description : 我的图片
 */

public class MyPicAdapter extends BaseQuickAdapter<MyPicBean, BaseViewHolder> {

    public MyPicAdapter() {
        super(R.layout.item_my_picture);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyPicBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.getFileId(), (ImageView) helper.getView(R.id.iv_my_image));
    }
}
