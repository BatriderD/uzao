package com.zhaotai.uzao.ui.main.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.MultiCustomBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;

import java.util.List;

/**
 * Time: 2018/3/23
 * Created by LiYou
 * Description :
 */

public class MainCustomAdapter extends BaseMultiItemQuickAdapter<MultiCustomBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MainCustomAdapter(List<MultiCustomBean> data) {
        super(data);
        addItemType(MultiCustomBean.TYPE_CATEGORY, R.layout.item_main_custom_category);
        addItemType(MultiCustomBean.TYPE_LINE, R.layout.item_main_line);
        addItemType(MultiCustomBean.TYPE_RECOMMEND_SPU_TITLE, R.layout.item_main_child_title);
        addItemType(MultiCustomBean.TYPE_RECOMMEND_SPU, R.layout.item_main_custom_recommend_spu);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiCustomBean item) {
        switch (helper.getItemViewType()) {
            case MultiCustomBean.TYPE_CATEGORY:
                initCategory(helper, item);
                break;
            case MultiCustomBean.TYPE_RECOMMEND_SPU_TITLE:
                helper.setText(R.id.tv_main_child_title, "热卖商品");
                break;
            case MultiCustomBean.TYPE_RECOMMEND_SPU:
                initRecommendSpu(helper, item);
                break;
        }
    }

    /**
     * 推荐商品
     */
    private void initRecommendSpu(BaseViewHolder helper, MultiCustomBean item) {
        helper.setText(R.id.tv_main_custom_recommend_spu_name, item.spuName);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.thumbnail),
                (ImageView) helper.getView(R.id.iv_main_custom_recommend_spu_pic));

        helper.addOnClickListener(R.id.iv_main_custom_recommend_spu_pic);
    }

    /**
     * 勾选 分类
     */
    private void initCategory(BaseViewHolder helper, MultiCustomBean item) {
        helper.setText(R.id.iv_main_custom_category_name, item.navigateName);

        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(item.icon),
                (ImageView) helper.getView(R.id.iv_main_custom_category_pic));
        helper.addOnClickListener(R.id.iv_main_custom_category_pic);
    }
}
