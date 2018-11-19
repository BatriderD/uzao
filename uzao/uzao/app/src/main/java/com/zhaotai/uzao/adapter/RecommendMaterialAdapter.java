package com.zhaotai.uzao.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;

import java.util.List;

/**
 * Time: 2018/3/7
 * Created by LiYou
 * Description : 推荐的素材
 */

public class RecommendMaterialAdapter extends BaseQuickAdapter<MaterialListBean, BaseViewHolder> {

    public RecommendMaterialAdapter(@Nullable List<MaterialListBean> data) {
        super(R.layout.item_recommend_material_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialListBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.thumbnail)
                , (ImageView) helper.getView(R.id.iv_material_image));

        helper.setText(R.id.iv_material_name, item.sourceMaterialName)
                .setText(R.id.tv_material_price, "¥" + item.priceY);
    }
}
