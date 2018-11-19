package com.zhaotai.uzao.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;

/**
 * Time: 2018/1/29
 * Created by LiYou
 * Description :素材
 */

public class DesignerMaterialAdapter extends BaseRecyclerAdapter<MaterialListBean, BaseViewHolder> {

    public DesignerMaterialAdapter() {
        super(R.layout.item_designer_material);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialListBean item) {
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.pic), (ImageView) helper.getView(R.id.iv_item_designer_list_material_img));
        helper.setText(R.id.tv_item_designer_list_material_name, item.materialName)
                .setText(R.id.tv_comment_count, item.commentCount)
                .setText(R.id.tv_view_count, item.viewCount)
                .setText(R.id.tv_like_count, item.favoriteCount);
    }
}
